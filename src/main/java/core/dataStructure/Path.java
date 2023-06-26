package core.dataStructure;

import core.algorithms.SymbolicExecutionAlgorithms;
import core.ast.additionalNodes.Node;
import core.cfg.*;
import org.eclipse.jdt.core.dom.*;

import java.util.*;

public class Path {

    private Node beginNode;

    private Node currentNode;

    public boolean isEmpty() {
        return beginNode == null;
    }

    public void addNextNode(CfgNode data) {
        Node previousNode = currentNode;
        currentNode = new Node(data);
        if (isEmpty()) beginNode = currentNode;
        else previousNode.setNext(currentNode);
    }

    @Override
    public String toString() {
        System.out.println(1 + "3" + 4 + 5 + "6");
        StringBuilder p = new StringBuilder("===============\n");
        Node tmpNode = beginNode;
        while (tmpNode != null) {
//            if(tmpNode.getData().getAst() instanceof VariableDeclarationStatement) {
//                ArrayNode arrayNode = (ArrayNode) ArrayInitializerNode.executeArrayInitializer(((ArrayInitializer)((VariableDeclarationFragment)((VariableDeclarationStatement) tmpNode.getData().getAst()).fragments().get(0)).getInitializer()), null);
//                System.out.println("abc");

//                MemoryModel memoryModel = new MemoryModel();
//                AstNode astNode = AstNode.executeASTNode((VariableDeclarationStatement) tmpNode.getData().getAst(), memoryModel);
//                System.out.println("abc");
//            }

//            if (tmpNode.getData().getAst() instanceof ExpressionStatement) {
//                if(((ExpressionStatement) tmpNode.getData().getAst()).getExpression() instanceof Assignment) {
//                    Assignment assignment = (Assignment) ((ExpressionStatement) tmpNode.getData().getAst()).getExpression();
//                    InfixExpressionNode infixExpressionNode = InfixExpressionNode.executeInfixExpression((InfixExpression) assignment.getRightHandSide());
//                    ExpressionNode res = InfixExpressionNode.executeInfixExpressionNode(infixExpressionNode, null);
//                    System.out.println("haha");
//                }
//
//            }
            p.append(tmpNode.getData().toString());
            p.append("\n");
            tmpNode = tmpNode.getNext();
        }
        p.append("===============");
        return p.toString();
    }

    public CfgNode getBeginCfgNode() {
        return beginNode.getData();
    }

    public Node getBeginNode() {
        return beginNode;
    }

    private HashMap<String, Element<?>> S = new HashMap<>();

    public void symbolicExecution(List parameters) {

        executeParameters(parameters);

        Node tmpNode = beginNode;
        while (tmpNode != null) {
            CfgNode cfgNode = tmpNode.getData();

//            if(executeScopeStatement(cfgNode)) { // Lenh Scope
//                tmpNode = tmpNode.next;
//                continue;
//            }

            ASTNode statement = cfgNode.getAst();
            if (statement instanceof VariableDeclarationStatement) { // Lenh Khai bao
                executeVariableDeclarationStatement(statement);
            } else if (statement instanceof ExpressionStatement) { // Lenh gan
                executeAssignment(statement);
            } else if (cfgNode instanceof CfgBoolExprNode) {
                System.out.println(cfgNode.getAst());
                ConstraintExpressionNode rootNode = SymbolicExecutionAlgorithms.infixExpressionToStatementTree(S, cfgNode.getAst());
                rootNode = SymbolicExecutionAlgorithms.analyzeStatementTree(rootNode);
                System.out.println(rootNode.toString());
                rootNode = SymbolicExecutionAlgorithms.analyzeStatementTree(rootNode);
                System.out.println(rootNode.toString());

                rootNode = SymbolicExecutionAlgorithms.analyzeStatementTree(rootNode);
                System.out.println(rootNode.toString());

                rootNode = SymbolicExecutionAlgorithms.analyzeStatementTree(rootNode);
                System.out.println(rootNode.toString());

                System.out.println("SMT Format:     " + rootNode.prefixToString());

//                rootNode = SymbolicExecutionAlgorithms.analyzeStatementTree(rootNode);
//                System.out.println(rootNode.toString());
//
//                rootNode = SymbolicExecutionAlgorithms.analyzeStatementTree(rootNode);
//                System.out.println(rootNode.toString());
            }
            tmpNode = tmpNode.getNext();
        }

        System.out.println(S);
    }

    private void executeParameters(List parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i) instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration parameter = (SingleVariableDeclaration) parameters.get(i);
                Element element = null;
                Type type = parameter.getType();

                if (type.isPrimitiveType()) {
                    element = dealWithPrimitiveTypeDeclaration(type);
                } else { // Deal with other data type
                    return;
                }

                S.put(parameter.getName().getIdentifier(), element);
            }
        }
    }

    private void executeVariableDeclarationStatement(ASTNode typeOfStatement) {
        VariableDeclarationStatement declarationStatement = (VariableDeclarationStatement) typeOfStatement;
        Type type = declarationStatement.getType();
        Element element = null;

        if (type.isPrimitiveType()) { // Deal with primitive type declaration
            element = dealWithPrimitiveTypeDeclaration(type);
        } else { // Deal with other data type
            return;
        }

        // Name of variables
        List<VariableDeclarationFragment> fragments = declarationStatement.fragments();
        for (VariableDeclarationFragment fragment : fragments) {
            try {
                S.put(fragment.toString(), (Element) element.clone());
            } catch (CloneNotSupportedException exception) {
                System.out.println("CLONE NOT SUPPORTED!!");
            }
        }
    }

    private void executeAssignment(ASTNode statement) {
        Expression expression = ((ExpressionStatement) statement).getExpression();
        if (expression instanceof Assignment) {  // Lenh gan
            Assignment assignment = (Assignment) expression;
            String variableName = assignment.getLeftHandSide().toString();
            String variableValue = assignment.getRightHandSide().toString();

            Element variableElement = S.get(variableName);

            Object element = variableElement.getElement();
            if (element instanceof Integer) {
                Element<Integer> newElement = new Element<>(Integer.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Character) {
                Element<Character> newElement = new Element<>(Character.valueOf(variableValue.charAt(1)), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Boolean) {
                Element<Boolean> newElement = new Element<>(Boolean.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Short) {
                Element<Short> newElement = new Element<>(Short.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Long) {
                Element<Long> newElement = new Element<>(Long.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Float) {
                Element<Float> newElement = new Element<>(Float.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Double) {
                Element<Double> newElement = new Element<>(Double.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Byte) {
                Element<Byte> newElement = new Element<>(Byte.valueOf(variableValue), true, assignment.getRightHandSide());
                S.put(variableName, newElement);
            } else if (element instanceof Void) {
                // ??
            }
        }
    }


    private Element dealWithPrimitiveTypeDeclaration(Type type) {
        String primitiveType = ((PrimitiveType) type).getPrimitiveTypeCode().toString();

        if (primitiveType.equals(PrimitiveType.INT.toString())) { // Integer type
            Element<Integer> tmpElement = new Element<>(0, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.CHAR.toString())) { // Character type
            Element<Character> tmpElement = new Element<>('#', false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.BOOLEAN.toString())) { // Boolean type
            Element<Boolean> tmpElement = new Element<>(false, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.SHORT.toString())) { // Short type
            Element<Short> tmpElement = new Element<>((short) 0, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.LONG.toString())) { // Long type
            Element<Long> tmpElement = new Element<>((long) 0, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.FLOAT.toString())) { // Float type
            Element<Float> tmpElement = new Element<>((float) 0, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.DOUBLE.toString())) { // Double type
            Element<Double> tmpElement = new Element<>((double) 0, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.BYTE.toString())) { // Byte type
            Element<Byte> tmpElement = new Element<>((byte) 0, false);
            return tmpElement;
        } else if (primitiveType.equals(PrimitiveType.VOID.toString())) { // Void type
            Element<Void> tmpElement = new Element<>(null, false);
            return tmpElement;
        }
        return null;
    }

    //    private int currentScope = 0;
//
//    private boolean executeScopeStatement(CfgNode cfgNode) {
//        if (cfgNode instanceof CfgBeginSwitchNode ||
//                cfgNode instanceof CfgBeginForNode ||
//                cfgNode instanceof CfgBeginDoNode ||
//                cfgNode instanceof CfgBeginForEachNode) {
//            currentScope++;
//            return true;
//        }
//
//        if (cfgNode instanceof CfgEndBlockNode) {
//            currentScope--;
//            return true;
//        }
//
//        return false;
//    }
}

