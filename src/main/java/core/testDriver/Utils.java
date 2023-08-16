package core.testDriver;

import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public final class Utils {
    private Utils() {
        throw new AssertionError("Utility class should not be instantiated.");
    }

    /**
     * Get parameter's classes list from AST Node List
     *
     * @param parameters (get from MethodDeclaration)
     * @return parameter's classes list
     */
    public static Class<?>[] getParameterClasses(List<ASTNode> parameters) {
        Class<?>[] types = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            ASTNode param = parameters.get(i);
            if (param instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration declaration = (SingleVariableDeclaration) param;
                Type type = declaration.getType();
                types[i] = getTypeClass(type);
            } else if (param instanceof VariableDeclarationFragment) {
                VariableDeclarationFragment declaration = (VariableDeclarationFragment) param;
                Type type = (Type) declaration.resolveBinding().getType();
                types[i] = getTypeClass(type);
            } else {
                throw new RuntimeException("Unsupported parameter: " + param.getClass());
            }
        }

        return types;
    }

    /**
     * Get class base on the type (can be PrimitiveType, ArrayType)
     *
     * @param type
     * @return the class of the type
     */
    private static Class<?> getTypeClass(Type type) {
        if (type instanceof PrimitiveType) {
            PrimitiveType.Code primitiveTypeCode = (((PrimitiveType) type).getPrimitiveTypeCode());
            return getPrimitiveClass(primitiveTypeCode);
        } else if (type instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) type;
            Type componentType = arrayType.getElementType();
            Class<?> componentClass = getTypeClass(componentType); // get clas of the component of array like int, double, ...
            int dimension = arrayType.getDimensions();
            return Array.newInstance(componentClass, new int[dimension]).getClass();
        } else if (type instanceof SimpleType) {
            SimpleType simpleType = (SimpleType) type;
            return getClassForName(simpleType.getName().getFullyQualifiedName());
        } else {
            throw new RuntimeException("Unsupported parameter type: " + type.getClass());
        }
    }

    /**
     * Get primitive class by its type code
     *
     * @param primitiveTypeCode
     * @return class of the code
     */
    private static Class<?> getPrimitiveClass(PrimitiveType.Code primitiveTypeCode) {
        String primitiveTypeStr = primitiveTypeCode.toString();
        switch (primitiveTypeStr) {
            case "int":
                return int.class;
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "char":
                return char.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "void":
                return void.class;
            default:
                throw new RuntimeException("Unsupported primitive type: " + primitiveTypeStr);
        }
    }

    /**
     * Get class of SimpleType by is name
     *
     * @param className
     * @return class
     */
    private static Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
    }

    public static Object[] getParameterValue(Class<?>[] parameterClasses) {
        Object[] result = new Object[parameterClasses.length];
        Scanner scanner;
        try {
            scanner = new Scanner(new File("src/main/java/data/TestData.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < parameterClasses.length; i++) {
            if(!scanner.hasNext()) {
                result[i] = createRandomVariableData(parameterClasses[i]);
                continue;
            }

            String className = parameterClasses[i].getName();

            if ("int".equals(className)) {
                result[i] = scanner.nextInt();
            } else if ("boolean".equals(className)) {
                result[i] = scanner.nextBoolean();
            } else if ("byte".equals(className)) {
                result[i] = scanner.nextByte();
            } else if ("short".equals(className)) {
                result[i] = scanner.nextShort();
            } else if ("char".equals(className)) {
                result[i] = (char) scanner.nextInt();
            } else if ("long".equals(className)) {
                result[i] = scanner.nextLong();
            } else if ("float".equals(className)) {
                result[i] = scanner.nextFloat();
            } else if ("double".equals(className)) {
                result[i] = scanner.nextDouble();
            } else if ("void".equals(className)) {
                result[i] = null;
            } else {
                throw new RuntimeException("Unsupported type: " + className);
            }
        }

        return result;
    }

    public static Object[] createRandomTestData(Class<?>[] parameterClasses) {
        Object[] result = new Object[parameterClasses.length];

        for(int i = 0; i < result.length; i++) {
            result[i] = createRandomVariableData(parameterClasses[i]);
        }

        return result;
    }

    private static Object createRandomVariableData(Class<?> parameterClass) {
        String className = parameterClass.getName();

        if ("int".equals(className)) {
            return 1;
        } else if ("boolean".equals(className)) {
            return false;
        } else if ("byte".equals(className)) {
            return (byte) 1;
        } else if ("short".equals(className)) {
            return (short) 1;
        } else if ("char".equals(className)) {
            return 'a';
        } else if ("long".equals(className)) {
            return (long) 1;
        } else if ("float".equals(className)) {
            return (float) 1.0;
        } else if ("double".equals(className)) {
            return 1.0;
        } else if ("void".equals(className)) {
            return null;
        }
        throw new RuntimeException("Unsupported type: " + className);
    }
}
