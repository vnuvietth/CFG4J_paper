package core.ast.Expression.Literal.NumberLiteral;

public class IntegerLiteralNode extends NumberLiteralNode {

    public static IntegerLiteralNode executeIntegerLiteral(int value) {
        IntegerLiteralNode integerLiteralNode = new IntegerLiteralNode();
        integerLiteralNode.setTokenValue(value);
        return integerLiteralNode;
    }

    public static IntegerLiteralNode[] createIntegerLiteralInitializationArray(int capacity) {
        IntegerLiteralNode[] array = new IntegerLiteralNode[capacity];
        for(int i = 0; i < capacity; i++) {
            array[i] = new IntegerLiteralNode();
        }
        return array;
    }

    public int getIntegerValue() {
        String token = super.getTokenValue();
        if(isIntegerValue(token)) {
            return Integer.parseInt(token);
        } else {
            return 0;
        }
    }

    public void setTokenValue(double doubleValue) {
        super.setTokenValue(String.valueOf((int) doubleValue));
    }

    public void setTokenValue(int integerValue) {
        super.setTokenValue(String.valueOf(integerValue));
    }

}