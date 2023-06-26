package core.dataStructure;

public class ConstraintExpressionOperator extends ConstraintExpressionNode {
    private ConstraintExpressionNode leftOperand;
    private ConstraintExpressionNode rightOperand;
    private String operator;

    public ConstraintExpressionOperator() {
        super.setOperatorNode(true);
    }

    public ConstraintExpressionNode getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(ConstraintExpressionNode leftOperand) {
        this.leftOperand = leftOperand;
    }

    public ConstraintExpressionNode getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(ConstraintExpressionNode rightOperand) {
        this.rightOperand = rightOperand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        result.append("(");
        result.append(leftOperand.toString());
        result.append(operator);
        result.append(rightOperand.toString());
        result.append(")");
        return result.toString();
    }

    @Override
    public String prefixToString() {
        StringBuilder result = new StringBuilder("");
        result.append("( ");
        if(this.isRootNode()) result.append("assert ");
        result.append(operator + " ");
        result.append(leftOperand.prefixToString() + " ");
        result.append(rightOperand.prefixToString() + " ");
        result.append(") ");
        return result.toString();
    }
}
