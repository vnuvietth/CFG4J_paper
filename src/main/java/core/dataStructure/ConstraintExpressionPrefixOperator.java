package core.dataStructure;

public class ConstraintExpressionPrefixOperator extends ConstraintExpressionNode {
    private String operator;
    private ConstraintExpressionNode operand;

    public ConstraintExpressionPrefixOperator() {
        super.setOperatorNode(true);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ConstraintExpressionNode getOperand() {
        return operand;
    }

    public void setOperand(ConstraintExpressionNode operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(operator);
        result.append("(");
        result.append(operand.toString());
        result.append(")");
        return result.toString();
    }

    @Override
    public String prefixToString() {
        StringBuilder result = new StringBuilder("");
        result.append("( ");
        if(this.isRootNode()) result.append("assert ");
        result.append(operator + " ");
        result.append("( ");
        result.append(operand.prefixToString() + " ");
        result.append(") ");
        result.append(") ");
        return result.toString();
    }
}
