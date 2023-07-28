package core.ast.Expression;

import core.ast.*;
import core.ast.Expression.Literal.*;
import core.ast.Expression.Literal.NumberLiteral.NumberLiteralNode;
import core.ast.Expression.Name.NameNode;
import core.ast.Expression.OperationExpression.*;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.*;

public abstract class ExpressionNode extends AstNode {

    public static AstNode executeExpression(Expression expression, MemoryModel memoryModel) {
        if (isOperationExpression(expression)) {
            return OperationExpressionNode.executeOperationExpression(expression, memoryModel);
        } else if (isLiteral(expression)) {
            return LiteralNode.executeLiteral(expression, memoryModel);
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

    public static boolean isLiteral(Expression expression) {
        return (expression instanceof NumberLiteral) ||
                (expression instanceof CharacterLiteral) ||
                (expression instanceof TypeLiteral) ||
                (expression instanceof NullLiteral) ||
                (expression instanceof StringLiteral) ||
                (expression instanceof BooleanLiteral);

    }

    public static boolean isOperationExpression(Expression expression) {
        return (expression instanceof InfixExpression) ||
                (expression instanceof PostfixExpression) ||
                (expression instanceof PrefixExpression) ||
                (expression instanceof ParenthesizedExpression);
    }

}
