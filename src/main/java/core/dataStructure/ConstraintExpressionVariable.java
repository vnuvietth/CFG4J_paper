package core.dataStructure;

public class ConstraintExpressionVariable extends ConstraintExpressionNode {

    private String variableName;

    private Element<?> variableValue;

    public ConstraintExpressionVariable() {
        super.setVariableNode(true);
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Element<?> getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(Element<?> variableValue) {
        this.variableValue = variableValue;
    }

    @Override
    public String toString() {
        return variableName;
    }

    @Override
    public String prefixToString() {
        StringBuilder result = new StringBuilder("");
        if(this.isRootNode()) result.append("assert ");
        result.append(variableName + " ");
        return result.toString();
    }
}
