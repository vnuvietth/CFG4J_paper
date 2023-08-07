package core.testDriver;

import core.ast.VariableDeclaration.VariableDeclarationFragmentNode;
import org.eclipse.jdt.core.dom.*;

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
     * @param type
     * @return the class of the type
     */
    private static Class<?> getTypeClass(Type type) {
        if (type instanceof PrimitiveType) {
            PrimitiveType.Code primitiveTypeCode = (((PrimitiveType) type).getPrimitiveTypeCode());
            return getPrimitiveClass(primitiveTypeCode);
        } else if (type instanceof ArrayType){
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
     * @param primitiveTypeCode
     * @return class of the code
     */
    private static Class<?> getPrimitiveClass(PrimitiveType.Code primitiveTypeCode) {
        String primitiveTypeStr = primitiveTypeCode.toString();
        switch (primitiveTypeStr) {
            case "int": return int.class;
            case "boolean": return boolean.class;
            case "byte": return byte.class;
            case "short": return short.class;
            case "char": return char.class;
            case "long": return long.class;
            case "float": return float.class;
            case "double": return double.class;
            default: throw new RuntimeException("Unsupported primitive type: " + primitiveTypeStr);
        }
    }

    /**
     * Get class of SimpleType by is name
     * @param className
     * @return class
     */
    private static Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
    }
}
