package core.dataStructure;

public class ConstraintExpressionValue extends ConstraintExpressionNode {

    private Element<?> value;

    public ConstraintExpressionValue() {
        super.setValueNode(true);
    }

    public Element<?> getValue() {
        return value;
    }

    public void setValue(Element<?> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.getElementToString();
    }

    @Override
    public String prefixToString() {
        StringBuilder result = new StringBuilder("");
        if(this.isRootNode()) result.append("assert ");
        result.append(value.getElementToString() + " ");
        return result.toString();
    }
}
