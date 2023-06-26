package core.algorithms;

import core.dataStructure.*;
import org.eclipse.jdt.core.dom.*;

import java.util.HashMap;

public final class SymbolicExecutionAlgorithms {

    private static HashMap<String, Element<?>> S; // Memory Model
    private static ConstraintExpressionNode rootNode;

    public static ConstraintExpressionNode infixExpressionToStatementTree(HashMap<String, Element<?>> memoryModel, ASTNode expression) {
        S = memoryModel;
        ConstraintExpressionNode rootNode = analyzeExpression(expression);
        rootNode.setRootNode(true);
        return rootNode;
    }

    private static ConstraintExpressionNode analyzeExpression(ASTNode expression) {
        if(expression instanceof InfixExpression) {
            InfixExpression infixExpression = (InfixExpression) expression;

            ConstraintExpressionOperator operator = new ConstraintExpressionOperator();
            operator.setOperator(infixExpression.getOperator().toString());
            operator.setLeftOperand(analyzeExpression(infixExpression.getLeftOperand()));
            operator.setRightOperand(analyzeExpression(infixExpression.getRightOperand()));

            return operator;
        } else if(expression instanceof PrefixExpression) {
            PrefixExpression prefixExpression = (PrefixExpression) expression;

            ConstraintExpressionPrefixOperator operator = new ConstraintExpressionPrefixOperator();
            operator.setOperator(prefixExpression.getOperator().toString());
            operator.setOperand(analyzeExpression(prefixExpression.getOperand()));

            return operator;
        } else if(expression instanceof ParenthesizedExpression) {
            ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) expression;
            return analyzeExpression(parenthesizedExpression.getExpression());
        } else if(expression instanceof SimpleName) {
            String variableName = ((SimpleName) expression).getIdentifier();
            Element<?> variableValue = S.get(variableName);
            ConstraintExpressionVariable constraintExpressionVariable = new ConstraintExpressionVariable();
            constraintExpressionVariable.setVariableName(variableName);
            constraintExpressionVariable.setVariableValue(variableValue);

            return constraintExpressionVariable;
        } else if(isLiteral(expression)) {
            return literalToValueNode(expression);
        }
        return null;
    }

    private static boolean isLiteral(ASTNode expression) {
        return (expression instanceof NumberLiteral ||
                expression instanceof CharacterLiteral ||
                expression instanceof BooleanLiteral ||
                expression instanceof StringLiteral ||
                expression instanceof NullLiteral ||
                expression instanceof TypeLiteral);
    }

    private static ConstraintExpressionValue literalToValueNode(ASTNode expression) {
        if(expression instanceof NumberLiteral) {
            return numberLiteralToValueNode(expression);
        } else if(expression instanceof CharacterLiteral) {
            return characterLiteralToValueNode(expression);
        } else if(expression instanceof BooleanLiteral) {
            return booleanLiteralToValueNode(expression);
        } else if(expression instanceof StringLiteral) {
            // ??
        } else if(expression instanceof NullLiteral) {
            // ??
        } else if(expression instanceof TypeLiteral) {
            // ??
        }
        return new ConstraintExpressionValue();
    }

    private static ConstraintExpressionValue numberLiteralToValueNode(ASTNode expression) {
        NumberLiteral numberLiteral = (NumberLiteral) expression;

        Element<Double> element = new Element<>();
        element.setElement(Double.valueOf(numberLiteral.getToken()));
        element.setLiteral((Expression) expression);

        ConstraintExpressionValue constraintExpressionValue = new ConstraintExpressionValue();
        constraintExpressionValue.setValue(element);

        return constraintExpressionValue;
    }

    private static ConstraintExpressionValue characterLiteralToValueNode(ASTNode expression) {
        CharacterLiteral characterLiteral = (CharacterLiteral) expression;

        Element<Character> element = new Element<>();
        element.setElement(Character.valueOf(characterLiteral.charValue()));
        element.setLiteral((Expression) expression);

        ConstraintExpressionValue constraintExpressionValue = new ConstraintExpressionValue();
        constraintExpressionValue.setValue(element);

        return constraintExpressionValue;
    }

    private static ConstraintExpressionValue booleanLiteralToValueNode(ASTNode expression) {
        BooleanLiteral booleanLiteral = (BooleanLiteral) expression;

        Element<Boolean> element = new Element<>();
        element.setElement(Boolean.valueOf(booleanLiteral.booleanValue()));
        element.setLiteral((Expression) expression);

        ConstraintExpressionValue constraintExpressionValue = new ConstraintExpressionValue();
        constraintExpressionValue.setValue(element);
        return constraintExpressionValue;
    }


    /* =========================================== */


    public static ConstraintExpressionNode analyzeStatementTree(ConstraintExpressionNode expressionNode) {
        if(expressionNode instanceof ConstraintExpressionOperator) {
            return analyzeOperatorNode((ConstraintExpressionOperator) expressionNode);
        }
        return null;
    }

    private static ConstraintExpressionNode analyzeOperatorNode(ConstraintExpressionOperator operatorNode) {
        ConstraintExpressionNode leftOperand = operatorNode.getLeftOperand();
        ConstraintExpressionNode rightOperand = operatorNode.getRightOperand();

        if(!leftOperand.isOperatorNode() && !rightOperand.isOperatorNode()) {
            if(leftOperand.isValueNode() && rightOperand.isValueNode()) {
                String operator = operatorNode.getOperator();
                ConstraintExpressionValue leftValueOperand = (ConstraintExpressionValue) leftOperand;
                ConstraintExpressionValue rightValueOperand = (ConstraintExpressionValue) rightOperand;

                if(isMathOperator(operator)) {
                    return calculateMathOperator(leftValueOperand, operator, rightValueOperand);
                } else if(isCompareOperator(operator)) {
                    return calculateCompareOperator(leftValueOperand, operator, rightValueOperand);
                } else if(isConditionOperator(operator)) {
                    return calculateConditionOperator(leftValueOperand, operator, rightValueOperand);
                }

            } else {
                operatorNode.setLeftOperand(analyzeVariableNode(leftOperand));
                operatorNode.setRightOperand(analyzeVariableNode(rightOperand));
            }
        } else {
            if(leftOperand.isOperatorNode()) {
                operatorNode.setLeftOperand(analyzeStatementTree(leftOperand));
            }
            if(rightOperand.isOperatorNode()) {
                operatorNode.setRightOperand(analyzeStatementTree(rightOperand));
            }
        }
        return operatorNode;
    }

    private static boolean isMathOperator(String operator) {
        return (operator.equals("+") ||
                operator.equals("-") ||
                operator.equals("*") ||
                operator.equals("/") ||
                operator.equals("%"));
    }

    private static boolean isCompareOperator(String operator) {
        return (operator.equals("==") ||
                operator.equals("!=") ||
                operator.equals(">") ||
                operator.equals("<") ||
                operator.equals(">=") ||
                operator.equals("<="));
    }

    private static boolean isConditionOperator(String operator) {
        return (operator.equals("||") ||
                operator.equals("&&"));
    }

    private static ConstraintExpressionValue calculateMathOperator(ConstraintExpressionValue leftOperand, String operator, ConstraintExpressionValue rightOperand) {
        String stringLeftValue = "";
        String stringRightValue = "";

        if(leftOperand.getValue().getElement() instanceof Character) {
            stringLeftValue = String.valueOf(Character.getNumericValue((Character) leftOperand.getValue().getElement()));
        } else {
            stringLeftValue = leftOperand.getValue().getElementToString();
        }

        if(rightOperand.getValue().getElement() instanceof Character) {
            stringRightValue = String.valueOf(Character.getNumericValue((Character) rightOperand.getValue().getElement()));
        } else {
            stringRightValue = leftOperand.getValue().getElementToString();
        }

        Double leftValue = Double.parseDouble(stringLeftValue);
        Double rightValue = Double.parseDouble(stringRightValue);
        ConstraintExpressionValue newValueNode = new ConstraintExpressionValue();

        if(operator.equals("+")) {
            Element<Double> newElement = new Element<>();
            newElement.setElement(leftValue + rightValue);
            newValueNode.setValue(newElement);
        } else if(operator.equals("-")) {
            Element<Double> newElement = new Element<>();
            newElement.setElement(leftValue - rightValue);
            newValueNode.setValue(newElement);
        } else if(operator.equals("*")) {
            Element<Double> newElement = new Element<>();
            newElement.setElement(leftValue * rightValue);
            newValueNode.setValue(newElement);
        } else if(operator.equals("/")) {
            Element<Double> newElement = new Element<>();
            newElement.setElement(leftValue / rightValue);
            newValueNode.setValue(newElement);
        } else if(operator.equals("%")) {
            Element<Double> newElement = new Element<>();
            newElement.setElement(leftValue % rightValue);
            newValueNode.setValue(newElement);
        }
        return newValueNode;
    }

    private static ConstraintExpressionValue calculateCompareOperator(ConstraintExpressionValue leftOperand, String operator, ConstraintExpressionValue rightOperand) {
        Comparable leftValue = (Comparable) leftOperand.getValue().getElement();
        Comparable rightValue = (Comparable) rightOperand.getValue().getElement();
        ConstraintExpressionValue newValueNode = new ConstraintExpressionValue();

        if(operator.equals("==")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue.compareTo(rightValue) == 0);
            newValueNode.setValue(newElement);
        } else if(operator.equals("<")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue.compareTo(rightValue) < 0);
            newValueNode.setValue(newElement);
        } else if(operator.equals(">")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue.compareTo(rightValue) > 0);
            newValueNode.setValue(newElement);
        } else if(operator.equals(">=")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue.compareTo(rightValue) >= 0);
            newValueNode.setValue(newElement);
        } else if(operator.equals("<=")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue.compareTo(rightValue) <= 0);
            newValueNode.setValue(newElement);
        } else if(operator.equals("!=")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue.compareTo(rightValue) != 0);
            newValueNode.setValue(newElement);
        }

        return newValueNode;
    }

    private static ConstraintExpressionValue calculateConditionOperator(ConstraintExpressionValue leftOperand, String operator, ConstraintExpressionValue rightOperand){
        Boolean leftValue = (Boolean) leftOperand.getValue().getElement();
        Boolean rightValue = (Boolean) rightOperand.getValue().getElement();
        ConstraintExpressionValue newValueNode = new ConstraintExpressionValue();

        if(operator.equals("||")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue || rightValue);
            newValueNode.setValue(newElement);
        } if(operator.equals("&&")) {
            Element<Boolean> newElement = new Element<>();
            newElement.setElement(leftValue && rightValue);
            newValueNode.setValue(newElement);
        }

        return newValueNode;
    }

    private static ConstraintExpressionNode analyzeVariableNode(ConstraintExpressionNode variableNode) {
        if(variableNode instanceof ConstraintExpressionVariable) {
            Element<?> variableElement = ((ConstraintExpressionVariable) variableNode).getVariableValue();
            if (variableElement.isAssigned()) {
                return literalToValueNode(variableElement.getLiteral());
            } else {
                return variableNode;
            }
        } else {
            return variableNode;
        }
    }

    private static ConstraintExpressionNode analyzePrefixOperator(ConstraintExpressionPrefixOperator operatorNode) {
        ConstraintExpressionNode operand = operatorNode.getOperand();

        if(!operand.isOperatorNode()) {
            if(operand.isValueNode()) {
                String operator = operatorNode.getOperator();
                ConstraintExpressionValue operandValue = (ConstraintExpressionValue) operand;


            } else {
                operatorNode.setOperand(analyzeVariableNode(operand));
            }
        } else {
            operatorNode.setOperand(analyzeStatementTree(operand));
        }
        return null;
    }



    /*=============================*/


}
