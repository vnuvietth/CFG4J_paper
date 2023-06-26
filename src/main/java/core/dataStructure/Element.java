package core.dataStructure;

import org.eclipse.jdt.core.dom.Expression;

public class Element<type> implements Cloneable {
    private type element;

    private boolean isAssigned;

    private Expression literal;

    public Element() {
    }

    public Element(type element, boolean isAssigned, Expression literal) {
        this.element = element;
        this.isAssigned = isAssigned;
        this.literal = literal;
    }

    public Element(type element, boolean isAssigned) {
        this.element = element;
        this.isAssigned = isAssigned;
    }

    public void setElement(type element) {
        this.element = element;
    }

    public type getElement() {
        return this.element;
    }

    public String getElementToString() {
        return this.element.toString();
    }

    public boolean isAssigned() {
        return this.isAssigned;
    }

    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public Expression getLiteral() {
        return this.literal;
    }

    public void setLiteral(Expression literal) {
        this.literal = literal;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString() {
        return (element.toString() + "_" + (isAssigned ? "assigned" : "notAssigned"));
    }
}
