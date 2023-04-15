package core.dataStructure;
import core.cfg.*;
import org.eclipse.jdt.core.dom.*;

import java.io.DataOutputStream;
import java.util.*;

public class Path {
    public class Node {
        CfgNode data;
        Node next;

        public Node() {
        }

        public Node(CfgNode data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node beginNode;

    private Node currentNode;

    public boolean isEmpty() {
        return beginNode == null;
    }

    public void addNextNode(CfgNode data) {
        Node previousNode = currentNode;
        currentNode = new Node(data);
        if (isEmpty()) beginNode = currentNode;
        else previousNode.next = currentNode;
    }

    @Override
    public String toString() {
        StringBuilder p = new StringBuilder("===============\n");
        Node tmpNode = beginNode;
        while (tmpNode != null) {
            p.append(tmpNode.data.toString());
            p.append("\n");
            tmpNode = tmpNode.next;
        }
        p.append("===============");
        return p.toString();
    }

    public CfgNode getBeginNode() {
        return beginNode.data;
    }

    private HashMap<String, Element> S = new HashMap<>();

    public void symbolicExecution(List parameters) {

        executeParameters(parameters);

        Node tmpNode = beginNode;
        while (tmpNode != null) {
            CfgNode cfgNode = tmpNode.data;

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
                constraintExpressions.add(new StringBuilder(""));
                listTypeConstraintExpressions.add(new ArrayList<>());
                executeConditionStatement(statement);
                System.out.println(constraintExpressions.peek());
                System.out.println(listTypeConstraintExpressionToString(listTypeConstraintExpressions.peek()));
            }
            tmpNode = tmpNode.next;
        }

        System.out.println(S);
    }

    private void executeParameters(List parameters) {
        for(int i = 0; i < parameters.size(); i++) {
            if(parameters.get(i) instanceof SingleVariableDeclaration) {
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

    private Stack<StringBuilder> constraintExpressions = new Stack();

    private Stack<List<String>> listTypeConstraintExpressions = new Stack<>();

    private ASTNode executeConditionStatement(ASTNode statement) {
        if(statement instanceof InfixExpression) {
            InfixExpression infixExpression = (InfixExpression) statement;

            ASTNode leftOperand = executeConditionStatement(infixExpression.getLeftOperand());
            String operator = infixExpression.getOperator().toString();
            addToCurrentConstraintExpression(operator);
            ASTNode rightOperand = executeConditionStatement(infixExpression.getRightOperand());

            rewriteConditionStatement(leftOperand, operator, rightOperand);

        } else if(statement instanceof ParenthesizedExpression) {
            addToCurrentConstraintExpression("(");
            ASTNode tmp = executeConditionStatement(((ParenthesizedExpression) statement).getExpression());
            addToCurrentConstraintExpression(")");
            return tmp;
        } else if(statement instanceof SimpleName) {
            String variableName = ((SimpleName) statement).getIdentifier();
            Element variableValue = S.get(variableName);
            if (variableValue.isAssigned()) {
                addToCurrentConstraintExpression(variableValue);
                return variableValue.getLiteral();
            } else {
                addToCurrentConstraintExpression(variableName);
                return statement;
            }
        } else {
            addToCurrentConstraintExpression(statement.toString());
            return statement;
        }

        return null;

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

    private String calculateInfixExpression(ASTNode leftOperand, String operator, ASTNode rightOperand) {
        if(isSimplestOperand(leftOperand) && isSimplestOperand(rightOperand)) {
            Comparable left = assignTypeAndValue(leftOperand);
            Comparable right = assignTypeAndValue(rightOperand);

            boolean compareValue;

            if(operator.equals("==")) {
                compareValue = left.compareTo(right) == 0;
                return String.valueOf(compareValue);
            } else if(operator.equals("<")) {
                compareValue = left.compareTo(right) < 0;
                return String.valueOf(compareValue);
            } else if(operator.equals(">")) {
                compareValue = left.compareTo(right) > 0;
                return String.valueOf(compareValue);
            } else if(operator.equals(">=")) {
                compareValue = left.compareTo(right) >= 0;
                return String.valueOf(compareValue);
            } else if(operator.equals("<=")) {
                compareValue = left.compareTo(right) <= 0;
                return String.valueOf(compareValue);
            } else if(operator.equals("!=")) {
                compareValue = left.compareTo(right) != 0;
                return String.valueOf(compareValue);
            } else if(operator.equals("&&")) {
                compareValue = ((Boolean) left) && ((Boolean) right);
                return String.valueOf(compareValue);
            } else if(operator.equals("||")) {
                compareValue = ((Boolean) left) || ((Boolean) right);
                return String.valueOf(compareValue);
            } else if(operator.equals("+")) {

            }
        }
        return "Hasn't been assigned!";
    }

    private Comparable assignTypeAndValue(ASTNode operand) {
        if(operand instanceof NumberLiteral) {
            return Double.valueOf(((NumberLiteral) operand).getToken());
        } else if(operand instanceof CharacterLiteral) {
            return Character.valueOf(((CharacterLiteral) operand).charValue());
        } else if(operand instanceof BooleanLiteral) {
            return ((BooleanLiteral) operand).booleanValue();
        } else {
            return new Comparable() {
                @Override
                public int compareTo(Object o) {
                    return 0;
                }
            };
        }
    }

    private void rewriteConditionStatement(ASTNode leftOperand, String operator, ASTNode rightOperand) {
        String statement = calculateInfixExpression(leftOperand, operator, rightOperand);
        if(!statement.equals("Hasn't been assigned!")) {
            for (int i = 0; i < 3; i++) {
                listTypeConstraintExpressions.peek().remove(listTypeConstraintExpressions.peek().size() - 1);
            }
            addToCurrentConstraintExpression(statement);
        }
    }

    private boolean isSimplestOperand(ASTNode expression) {
        if(expression instanceof CharacterLiteral ||
                expression instanceof NumberLiteral ||
                expression instanceof BooleanLiteral) return true;
        else return false;
    }


    private String listTypeConstraintExpressionToString(List<String> listTypeConstraintExpression) {
        StringBuilder result = new StringBuilder();
        for(String i : listTypeConstraintExpression) {
            result.append(i + " ");
        }
        return result.toString();
    }

    private void addToCurrentConstraintExpression(String value) {
        listTypeConstraintExpressions.peek().add(value);
        constraintExpressions.peek().append(value + " ");
    }

    private void addToCurrentConstraintExpression(Element value) {
        String result = "";
        if(value.getElement() instanceof Character) {
            result = "'" + value.getElement().toString() + "'";
        } else {
            result = value.getElement().toString();
        }
        listTypeConstraintExpressions.peek().add(result);
        constraintExpressions.peek().append(result + " ");
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

