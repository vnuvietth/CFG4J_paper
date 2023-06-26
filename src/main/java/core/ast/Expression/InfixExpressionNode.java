package core.ast.Expression;

import core.ast.*;
import core.ast.Expression.Literal.LiteralNode;
import core.ast.Expression.Name.NameNode;
import core.ast.Expression.Name.SimpleNameNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class InfixExpressionNode extends ExpressionNode {

    private ExpressionNode leftOperand;
    private ExpressionNode rightOperand;
    private InfixExpression.Operator operator;
    private List<AstNode> extendedOperands;

    public static ExpressionNode executeInfixExpression(InfixExpression infixExpression, MemoryModel memoryModel) {
        InfixExpressionNode infixExpressionNode = new InfixExpressionNode();
        infixExpressionNode.leftOperand = (ExpressionNode) ExpressionNode.executeExpression(infixExpression.getLeftOperand(), memoryModel);
        infixExpressionNode.rightOperand = (ExpressionNode) ExpressionNode.executeExpression(infixExpression.getRightOperand(), memoryModel);
        infixExpressionNode.operator = infixExpression.getOperator();

        List<AstNode> extendedOperands = new ArrayList<>();
        for (int i = 0; i < infixExpression.extendedOperands().size(); i++) {
            extendedOperands.add(AstNode.executeASTNode((ASTNode) infixExpression.extendedOperands().get(i), memoryModel));
        }
        infixExpressionNode.extendedOperands = extendedOperands;

        ExpressionNode expressionNode = executeInfixExpressionNode(infixExpressionNode, memoryModel);

        return expressionNode;
    }

    public static ExpressionNode executeInfixExpressionNode(InfixExpressionNode infixExpressionNode, MemoryModel memoryModel) {
        ExpressionNode leftOperand = infixExpressionNode.leftOperand;
        ExpressionNode rightOperand = infixExpressionNode.rightOperand;
        InfixExpression.Operator operator = infixExpressionNode.operator;
        List<AstNode> extendedOperands = infixExpressionNode.extendedOperands;

//        if(!extendedOperands.isEmpty()) {
//            List<AstNode> operands = new ArrayList<>();
//            operands.add(leftOperand);
//            operands.add(rightOperand);
//            operands.addAll(extendedOperands);
//
//            LiteralNode literalResult = null;
//
//            for(AstNode i : operands) {
//                if(i instanceof LiteralNode) {
//                    LiteralNode literalOperand = (LiteralNode) i;
//                    if(literalResult == null) {
//                        literalResult = literalOperand;
//                    } else {
//                        literalResult = LiteralNode.analyzeTwoInfixLiteral(literalResult, operator, literalOperand);
//
//                    }
//
//                }
//            }
//        }

        if (leftOperand.isLiteralNode() && rightOperand.isLiteralNode()) {
             LiteralNode literalResult = LiteralNode.analyzeTwoInfixLiteral((LiteralNode) leftOperand, operator, (LiteralNode) rightOperand);

             if(extendedOperands.isEmpty()) {
                 return literalResult;
             } else {
                 infixExpressionNode.leftOperand = literalResult;
                 infixExpressionNode.rightOperand = (ExpressionNode) extendedOperands.remove(0);
                 return infixExpressionNode;
             }
        } else {
            if (!leftOperand.isLiteralNode()) {
                infixExpressionNode.leftOperand = executeOperand(leftOperand, memoryModel);
            }

            if (!rightOperand.isLiteralNode()) {
                infixExpressionNode.rightOperand = executeOperand(rightOperand, memoryModel);
            }
            return infixExpressionNode;
        }
    }

    private static ExpressionNode executeOperand(ExpressionNode operand, MemoryModel memoryModel) {
        if(operand instanceof InfixExpressionNode) {
            return executeInfixExpressionNode((InfixExpressionNode) operand, memoryModel);
        } else if (operand instanceof PrefixExpressionNode) {
            return PrefixExpressionNode.executePrefixExpressionNode((PrefixExpressionNode) operand, memoryModel);
        } else if (operand instanceof PostfixExpressionNode) {
            return PostfixExpressionNode.executePostfixExpressionNode((PostfixExpressionNode) operand, memoryModel);
        } else if (operand instanceof NameNode) {
            return NameNode.executeNameNode((NameNode) operand, memoryModel);
        } else {
            throw new RuntimeException("Invalid expressionNode");
        }
    }

    public static boolean isMathOperator(InfixExpression.Operator operator) {
        return (operator.equals(InfixExpression.Operator.PLUS) ||
                operator.equals(InfixExpression.Operator.MINUS) ||
                operator.equals(InfixExpression.Operator.DIVIDE) ||
                operator.equals(InfixExpression.Operator.TIMES) ||
                operator.equals(InfixExpression.Operator.REMAINDER));
    }

    public static boolean isComparisonOperator(InfixExpression.Operator operator) {
        return (operator.equals(InfixExpression.Operator.EQUALS) ||
                operator.equals(InfixExpression.Operator.NOT_EQUALS) ||
                operator.equals(InfixExpression.Operator.LESS) ||
                operator.equals(InfixExpression.Operator.GREATER) ||
                operator.equals(InfixExpression.Operator.LESS_EQUALS) ||
                operator.equals(InfixExpression.Operator.GREATER_EQUALS));
    }

    public static boolean isConditionalOperator(InfixExpression.Operator operator) {
        return (operator.equals(InfixExpression.Operator.CONDITIONAL_OR) ||
                operator.equals(InfixExpression.Operator.CONDITIONAL_AND));
    }

    public static boolean isBooleanComparisonOperator(InfixExpression.Operator operator) {
        return (operator.equals(InfixExpression.Operator.EQUALS) ||
                operator.equals(InfixExpression.Operator.NOT_EQUALS));
    }

    public static boolean isStringComparisonOperator(InfixExpression.Operator operator) {
        return (operator.equals(InfixExpression.Operator.EQUALS) ||
                operator.equals(InfixExpression.Operator.NOT_EQUALS));
    }

    public static boolean isStringConcatenationOperator(InfixExpression.Operator operator) {
        return operator.equals(InfixExpression.Operator.PLUS);
    }

    public String constrainToString() {
        StringBuilder result = new StringBuilder("");
        result.append("( ");
        result.append(operator + " ");
        result.append(tmpString(leftOperand) + " ");
        result.append(tmpString(rightOperand) + " ");
        result.append(") ");
        return result.toString();
    }
    private String tmpString(ExpressionNode expressionNode) {
        if(expressionNode.isLiteralNode()) {
            return expressionNode.toString();
        } else if(expressionNode instanceof InfixExpressionNode) {
            return ((InfixExpressionNode) expressionNode).constrainToString();
        } else if(expressionNode instanceof SimpleNameNode) {
            return ((SimpleNameNode) expressionNode).getIdentifier();
        } else {
            throw new RuntimeException("invalid constrain");
        }
    }
}
