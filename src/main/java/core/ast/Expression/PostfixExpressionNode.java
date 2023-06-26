package core.ast.Expression;

import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.PostfixExpression;

public class PostfixExpressionNode extends ExpressionNode {
    private ExpressionNode operand;
    private PostfixExpression.Operator operator;

    public static PrefixExpressionNode executePostfixExpression(PostfixExpression postfixExpression) {
        return null;
    }

    public static ExpressionNode executePostfixExpressionNode(PostfixExpressionNode postfixExpressionNode, MemoryModel memoryModel) {
        return null;
    }
}
