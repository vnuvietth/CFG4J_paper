package core.ast.Expression;

import core.ast.*;
import core.ast.Expression.Literal.*;
import core.ast.Expression.Literal.NumberLiteral.NumberLiteralNode;
import core.ast.Expression.Name.NameNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.*;

public abstract class ExpressionNode extends AstNode {

    public static AstNode executeExpression(Expression expression, MemoryModel memoryModel) {
        if (expression instanceof InfixExpression) {
            return InfixExpressionNode.executeInfixExpression((InfixExpression) expression, memoryModel);
        } else if (expression instanceof PrefixExpression) {
            return PrefixExpressionNode.executePrefixExpression((PrefixExpression) expression);
        } else if (expression instanceof PostfixExpression) {
            return PostfixExpressionNode.executePostfixExpression((PostfixExpression) expression);
        } else if (expression instanceof NumberLiteral) {
            return NumberLiteralNode.executeNumberLiteral((NumberLiteral) expression);
        } else if (expression instanceof CharacterLiteral) {
            return CharacterLiteralNode.executeCharacterLiteral((CharacterLiteral) expression);
        } else if (expression instanceof BooleanLiteral) {
            return BooleanLiteralNode.executeBooleanLiteral((BooleanLiteral) expression);
        } else if (expression instanceof StringLiteral) {
            return StringLiteralNode.executeStringLiteral((StringLiteral) expression);
        } else if (expression instanceof NullLiteral) {
            /*???*/
            return null;
        } else if (expression instanceof TypeLiteral) {
            /*???*/
            return null;
        } else if (expression instanceof ArrayInitializer) {
            return ArrayInitializerNode.executeArrayInitializer((ArrayInitializer) expression, memoryModel);
        } else if (expression instanceof ArrayCreation) {
            return ArrayCreationNode.executeArrayCreation((ArrayCreation) expression, memoryModel);
        } else if (expression instanceof ArrayAccess) {
            return ArrayAccessNode.executeArrayAccessNode((ArrayAccess) expression, memoryModel);
        } else if (expression instanceof Name) {
            return NameNode.executeName((Name) expression, memoryModel);
        } else if (expression instanceof Assignment) {
            AssignmentNode.executeAssignment((Assignment) expression, memoryModel);
            return null;
        } else if (expression instanceof VariableDeclarationExpression) {
            VariableDeclarationExpressionNode.executeVariableDeclarationExpression((VariableDeclarationExpression) expression,
                    memoryModel);
            return null;
        } else {
//            throw new RuntimeException(expression.getClass() + " is not an Expression!!!");
            return null;
        }
    }

    public final boolean isLiteralNode() {
        return this instanceof LiteralNode;
    }

}
