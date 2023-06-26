package core.dataStructure;

public abstract class ConstraintExpressionNode {
    private boolean isRootNode;
    private boolean isOperatorNode;
    private boolean isValueNode;
    private boolean isVariableNode;

    public ConstraintExpressionNode() {
    }

    public boolean isRootNode() {
        return isRootNode;
    }

    public void setRootNode(boolean rootNode) {
        isRootNode = rootNode;
    }

    public boolean isOperatorNode() {
        return isOperatorNode;
    }

    public void setOperatorNode(boolean operatorNode) {
        isOperatorNode = operatorNode;
    }

    public boolean isValueNode() {
        return isValueNode;
    }

    public void setValueNode(boolean valueNode) {
        isValueNode = valueNode;
    }

    public boolean isVariableNode() {
        return isVariableNode;
    }

    public void setVariableNode(boolean variableNode) {
        isVariableNode = variableNode;
    }

    public abstract String prefixToString();
}
