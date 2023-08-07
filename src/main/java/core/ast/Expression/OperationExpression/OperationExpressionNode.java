package core.ast.Expression.OperationExpression;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import core.ast.AstNode;
import core.ast.Expression.ExpressionNode;
import core.ast.Expression.Literal.BooleanLiteralNode;
import core.ast.Expression.Literal.CharacterLiteralNode;
import core.ast.Expression.Literal.LiteralNode;
import core.ast.Expression.Literal.NumberLiteral.DoubleLiteralNode;
import core.ast.Expression.Literal.NumberLiteral.IntegerLiteralNode;
import core.ast.Expression.Literal.NumberLiteral.NumberLiteralNode;
import core.ast.Expression.Name.NameNode;
import core.dataStructure.MemoryModel;
import core.variable.Variable;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

public abstract class OperationExpressionNode extends ExpressionNode {

//    public static Expr createZ3Expression(OperationExpressionNode operationExpressionNode, Context ctx, List<Expr> vars, MemoryModel memoryModel) {
//        if (operationExpressionNode instanceof InfixExpressionNode) {
//            return InfixExpressionNode.createZ3Expression((InfixExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else if (operationExpressionNode instanceof PrefixExpressionNode) {
//            return PrefixExpressionNode.createZ3Expression((PrefixExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else if (operationExpressionNode instanceof PostfixExpressionNode) {
//            return PostfixExpressionNode.createZ3Expression((PostfixExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else if (operationExpressionNode instanceof ParenthesizedExpressionNode) {
//            return ParenthesizedExpressionNode.createZ3Expression((ParenthesizedExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else {
//            throw new RuntimeException(operationExpressionNode.getClass() + " is not an OperationExpression!!!");
//        }
//    }

    public static Expr createZ3Expression(ExpressionNode operand, Context ctx, List<Expr> vars, MemoryModel memoryModel) {
        if(operand instanceof InfixExpressionNode) {
            return InfixExpressionNode.createZ3Expression((InfixExpressionNode) operand, ctx, vars, memoryModel);
        } else if(operand instanceof PostfixExpressionNode) {
            return PostfixExpressionNode.createZ3Expression((PostfixExpressionNode) operand, ctx, vars, memoryModel);
        } else if(operand instanceof PrefixExpressionNode) {
            return PrefixExpressionNode.createZ3Expression((PrefixExpressionNode) operand, ctx, vars, memoryModel);
        } else if(operand instanceof ParenthesizedExpressionNode) {
            return ParenthesizedExpressionNode.createZ3Expression((ParenthesizedExpressionNode) operand, ctx, vars, memoryModel);
        } else if(operand instanceof NameNode) {
            return createZ3Variable((NameNode) operand, ctx, vars, memoryModel);
        } else if(operand instanceof LiteralNode) {
            if(operand instanceof NumberLiteralNode) {
                if(operand instanceof IntegerLiteralNode) {
                    return ctx.mkInt(((IntegerLiteralNode) operand).getIntegerValue());
                } else { // operand instanceof DoubleLiteralNode
                    return ctx.mkReal(((DoubleLiteralNode) operand).getTokenValue());
                }
            } else if(operand instanceof BooleanLiteralNode) {
                return ctx.mkBool(((BooleanLiteralNode) operand).getValue());
            } else if(operand instanceof CharacterLiteralNode) {
                return ctx.mkInt(((CharacterLiteralNode) operand).getCharacterValue());
            } else {
                throw new RuntimeException("Invalid Literal");
            }
        } else {
            throw new RuntimeException(operand.getClass() + " is not an Expression");
        }
    }

    private static Expr createZ3Variable(NameNode variableName, Context ctx, List<Expr> vars, MemoryModel memoryModel) {
        String stringName = NameNode.getStringNameNode(variableName);
        Expr variable =  Variable.createZ3Variable(memoryModel.getVariable(stringName), ctx);

        //Check duplicate and add to vars
        if(!OperationExpressionNode.haveDuplicateVariable(variable.toString(), vars)) {
            vars.add(variable);
        }
        return variable;
    }

    public static boolean haveDuplicateVariable(String var, List<Expr> vars) {
        for(int i = 0; i < vars.size(); i++) {
            if(var.equals(vars.get(i).toString())) {
                return true;
            }
        }
        return false;
    }

    public static AstNode executeOperationExpression(Expression expression, MemoryModel memoryModel) {
        if (expression instanceof InfixExpression) {
            return InfixExpressionNode.executeInfixExpression((InfixExpression) expression, memoryModel);
        } else if (expression instanceof PrefixExpression) {
            return PrefixExpressionNode.executePrefixExpression((PrefixExpression) expression, memoryModel);
        } else if (expression instanceof PostfixExpression) {
            return PostfixExpressionNode.executePostfixExpression((PostfixExpression) expression, memoryModel);
        } else if (expression instanceof ParenthesizedExpression) {
            return ParenthesizedExpressionNode.executeParenthesizedExpression((ParenthesizedExpression) expression, memoryModel);
        } else {
            throw new RuntimeException(expression.getClass() + " is not an OperationExpression!!!");
        }
    }

    public static ExpressionNode executeOperandNode(ExpressionNode operand, MemoryModel memoryModel) {
        if (operand instanceof InfixExpressionNode) {
            return InfixExpressionNode.executeInfixExpressionNode((InfixExpressionNode) operand, memoryModel);
        } else if (operand instanceof PrefixExpressionNode) {
            return PrefixExpressionNode.executePrefixExpressionNode((PrefixExpressionNode) operand, memoryModel);
        } else if (operand instanceof PostfixExpressionNode) {
            return PostfixExpressionNode.executePostfixExpressionNode((PostfixExpressionNode) operand, memoryModel);
        } else if (operand instanceof ParenthesizedExpressionNode) {
            return ParenthesizedExpressionNode.executeParenthesizedExpressionNode((ParenthesizedExpressionNode) operand, memoryModel);
        } else if (operand instanceof NameNode) {
            return NameNode.executeNameNode((NameNode) operand, memoryModel);
        } else {
            throw new RuntimeException("Invalid expressionNode");
        }
    }
}