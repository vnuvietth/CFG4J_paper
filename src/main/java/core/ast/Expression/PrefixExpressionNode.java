package core.ast.Expression;

import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.PrefixExpression;

public class PrefixExpressionNode extends ExpressionNode {
    private ExpressionNode operand;
    private PrefixExpression.Operator operator;

    public static PrefixExpressionNode executePrefixExpression(PrefixExpression prefixExpression) {
        return null;
    }

    public static ExpressionNode executePrefixExpressionNode(PrefixExpressionNode prefixExpressionNode, MemoryModel memoryModel) {
        return null;
    }
}
