package core.ast.Expression.Literal;

import core.ast.Expression.ExpressionNode;
import core.ast.Expression.InfixExpressionNode;
import core.ast.Expression.Literal.NumberLiteral.DoubleLiteralNode;
import core.ast.Expression.Literal.NumberLiteral.IntegerLiteralNode;
import core.ast.Expression.Literal.NumberLiteral.NumberLiteralNode;
import org.eclipse.jdt.core.dom.*;

public abstract class LiteralNode extends ExpressionNode {
    /*For Postfix Expression Node*/
//    public static LiteralNode analyzeOnePostfixLiteral(LiteralNode literal, PostfixExpression.Operator operator) {
////        ??
//    }

    /*For Prefix Expression Node*/
    public static LiteralNode analyzeOnePrefixLiteral(PrefixExpression.Operator operator, LiteralNode literal) {
        if(literal.isNumberLiteralNode()) {
            return calculateOnePrefixNumberLiteral((NumberLiteralNode) literal, operator);
        } else if(literal.isCharacterLiteralNode()) {
            return calculateOnePrefixCharacterLiteral((CharacterLiteralNode) literal, operator);
        } else if(literal.isBooleanLiteralNode()) {
            return calculateOnePrefixBooleanLiteral((BooleanLiteralNode) literal, operator);
        } else {
            throw new RuntimeException("Invalid literal to analyze!!!");
        }
    }

    /*For Infix Expression Node*/
    public static LiteralNode analyzeTwoInfixLiteral(LiteralNode literal1, InfixExpression.Operator operator,
                                                LiteralNode literal2) {
        if (isComputableAndComparableLiteral(literal1) && isComputableAndComparableLiteral(literal2)) {
            return calculateTwoInfixComputableAndComparableLiteral(literal1, operator, literal2);
        } else if (literal1.isBooleanLiteralNode() && literal2.isBooleanLiteralNode()) {
            return calculateTwoInfixBooleanLiteral((BooleanLiteralNode) literal1, operator,
                    (BooleanLiteralNode) literal2);
        } else if (literal1.isStringLiteralNode() && literal2.isStringLiteralNode()) {
            return calculateTwoInfixStringLiteral((StringLiteralNode) literal1, operator,
                    (StringLiteralNode) literal2);
        } else if (literal1.isStringLiteralNode() || literal2.isStringLiteralNode()) {
            return calculateTwoInfixConcatenableLiteral(literal1, operator, literal2);
        } else {
            throw new RuntimeException("Invalid literals to analyze!!!");
        }
    }

    private static NumberLiteralNode calculateOnePrefixNumberLiteral(NumberLiteralNode literal,
                                                                     PrefixExpression.Operator operator) {
        DoubleLiteralNode tmpRes = computeOnePrefixNumberLiteral(literal, operator);
        if(literal.isDoubleLiteralNode()) {
            return tmpRes;
        } else {
            IntegerLiteralNode newRes = new IntegerLiteralNode();
            newRes.setTokenValue(tmpRes.getDoubleValue());
            return newRes;
        }
    }

    private static LiteralNode calculateOnePrefixCharacterLiteral(CharacterLiteralNode literal,
                                                                  PrefixExpression.Operator operator) {
        return computeOnePrefixCharacterLiteral(literal, operator);
    }

    private static BooleanLiteralNode calculateOnePrefixBooleanLiteral(BooleanLiteralNode literal,
                                                                       PrefixExpression.Operator operator) {
        return computeOnePrefixBooleanLiteral(literal, operator);
    }

    private static StringLiteralNode calculateTwoInfixConcatenableLiteral(LiteralNode literal1,
                                                                    InfixExpression.Operator operator,
                                                                    LiteralNode literal2) {

        StringLiteralNode stringLiteralNode1 = changeToStringLiteral(literal1);
        StringLiteralNode stringLiteralNode2 = changeToStringLiteral(literal2);

        if(InfixExpressionNode.isStringConcatenationOperator(operator)) {
            return concatenateTwoInfixStringLiteral(stringLiteralNode1, operator, stringLiteralNode2);
        } else {
            throw new RuntimeException("Invalid infix concatenate operator!!!");
        }
    }

    private static LiteralNode calculateTwoInfixStringLiteral(StringLiteralNode literal1,
                                                                    InfixExpression.Operator operator,
                                                                    StringLiteralNode literal2) {
        if(InfixExpressionNode.isStringConcatenationOperator(operator)) {
            return concatenateTwoInfixStringLiteral(literal1, operator, literal2);
        } else if(InfixExpressionNode.isStringComparisonOperator(operator)) {
            return compareTwoInfixStringLiteral(literal1, operator, literal2);
        } else {
            throw new RuntimeException("Invalid infix operator for applying to String type!!!");
        }
    }

    private static LiteralNode calculateTwoInfixComputableAndComparableLiteral(LiteralNode literal1,
                                                                          InfixExpression.Operator operator,
                                                                          LiteralNode literal2) {
        if (InfixExpressionNode.isMathOperator(operator)) {
            DoubleLiteralNode tmpRes = computeTwoInfixLiteral(literal1, operator, literal2);
            if (literal1 instanceof DoubleLiteralNode || literal2 instanceof DoubleLiteralNode) {
                return tmpRes;
            } else {
                IntegerLiteralNode newRes = new IntegerLiteralNode();
                newRes.setTokenValue(tmpRes.getDoubleValue());
                return newRes;
            }
        } else if (InfixExpressionNode.isComparisonOperator(operator)) {
            return compareTwoInfixLiteral(literal1, operator, literal2);
        } else {
            throw new RuntimeException("Operator given is neither infix comparison nor math operator");
        }
    }

    private static LiteralNode calculateTwoInfixBooleanLiteral(BooleanLiteralNode literal1, InfixExpression.Operator operator,
                                                          BooleanLiteralNode literal2) {
        if (InfixExpressionNode.isConditionalOperator(operator) ||
                InfixExpressionNode.isBooleanComparisonOperator(operator)) {
            return compareTwoInfixBooleanLiteral(literal1, operator, literal2);
        } else {
            throw new RuntimeException("Invalid infix operator for applying to boolean type");
        }
    }

    private static boolean isComputableAndComparableLiteral(LiteralNode literalNode) {
        return (literalNode.isCharacterLiteralNode() ||
                literalNode.isNumberLiteralNode());
    }

    private static BooleanLiteralNode computeOnePrefixBooleanLiteral(BooleanLiteralNode literal, PrefixExpression.Operator operator) {
        boolean booleanValue = literal.getValue();

        BooleanLiteralNode result = new BooleanLiteralNode();

        if(operator.equals(PrefixExpression.Operator.NOT)) {
            result.setValue(!booleanValue);
        } else {
            throw new RuntimeException("Invalid prefix operator for boolean literal");
        }

        return result;
    }

    private static LiteralNode computeOnePrefixCharacterLiteral(CharacterLiteralNode literal, PrefixExpression.Operator operator) {
        char characterValue = literal.getCharacterValue();

        CharacterLiteralNode result = new CharacterLiteralNode();

        if(operator.equals(PrefixExpression.Operator.INCREMENT)) {
            result.setCharacterValue(++characterValue);
        } else if(operator.equals(PrefixExpression.Operator.DECREMENT)) {
            result.setCharacterValue(--characterValue);
        } else if(operator.equals(PrefixExpression.Operator.PLUS)) {
            IntegerLiteralNode newRes = new IntegerLiteralNode();
            newRes.setTokenValue(+characterValue);
            return newRes;
        } else if(operator.equals(PrefixExpression.Operator.MINUS)) {
            IntegerLiteralNode newRes = new IntegerLiteralNode();
            newRes.setTokenValue(-characterValue);
            return newRes;
        } else {
            throw new RuntimeException("Invalid prefix operator for character literal");
        }

        return result;
    }

    private static DoubleLiteralNode computeOnePrefixNumberLiteral(NumberLiteralNode literal, PrefixExpression.Operator operator) {
        double doubleValue = changeToDoubleLiteral(literal).getDoubleValue();

        DoubleLiteralNode result = new DoubleLiteralNode();

        if(operator.equals(PrefixExpression.Operator.INCREMENT)) {
            result.setTokenValue(++doubleValue);
        } else if(operator.equals(PrefixExpression.Operator.DECREMENT)) {
            result.setTokenValue(--doubleValue);
        } else if(operator.equals(PrefixExpression.Operator.PLUS)) {
            result.setTokenValue(+doubleValue);
        } else if(operator.equals(PrefixExpression.Operator.MINUS)) {
            result.setTokenValue(-doubleValue);
        } else {
            throw new RuntimeException("Invalid prefix operator for number literal");
        }

        return result;
    }

    private static BooleanLiteralNode compareTwoInfixBooleanLiteral(BooleanLiteralNode literal1, InfixExpression.Operator operator,
                                                               BooleanLiteralNode literal2) {
        BooleanLiteralNode result = new BooleanLiteralNode();

        boolean booleanValue1 = literal1.getValue();
        boolean booleanValue2 = literal2.getValue();

        if (operator.equals(InfixExpression.Operator.CONDITIONAL_OR)) {
            result.setValue(booleanValue1 || booleanValue2);
        } else if (operator.equals(InfixExpression.Operator.CONDITIONAL_AND)) {
            result.setValue(booleanValue1 && booleanValue2);
        } else if (operator.equals(InfixExpression.Operator.EQUALS)) {
            result.setValue(booleanValue1 == booleanValue2);
        } else if (operator.equals(InfixExpression.Operator.NOT_EQUALS)) {
            result.setValue(booleanValue1 != booleanValue2);
        } else {
            throw new RuntimeException("Invalid infix operator for applying to boolean type");
        }

        return result;
    }

    private static BooleanLiteralNode compareTwoInfixStringLiteral(StringLiteralNode literal1,
                                                                   InfixExpression.Operator operator,
                                                                   StringLiteralNode literal2) {
        BooleanLiteralNode result = new BooleanLiteralNode();

        String stringValue1 = literal1.getStringValue();
        String stringValue2 = literal2.getStringValue();

        if (operator.equals(InfixExpression.Operator.EQUALS)) {
            result.setValue(stringValue1 == stringValue2);
        } else if (operator.equals(InfixExpression.Operator.NOT_EQUALS)) {
            result.setValue(stringValue1 != stringValue2);
        } else {
            throw new RuntimeException("Invalid infix operator for applying to String type");
        }

        return result;
    }

    private static StringLiteralNode concatenateTwoInfixStringLiteral(StringLiteralNode literal1,
                                                                 InfixExpression.Operator operator,
                                                                 StringLiteralNode literal2) {
        StringLiteralNode result = new StringLiteralNode();

        String stringValue1 = literal1.getStringValue();
        String stringValue2 = literal2.getStringValue();

        if(operator.equals(InfixExpression.Operator.PLUS)) {
            result.setStringValue(stringValue1 + stringValue2);
        } else {
            throw new RuntimeException("Invalid infix concat operator!!!");
        }

        return result;
    }

    private static DoubleLiteralNode computeTwoInfixLiteral(LiteralNode literal1, InfixExpression.Operator operator, LiteralNode literal2) {
        if (!(isComputableAndComparableLiteral(literal1) &&
                isComputableAndComparableLiteral(literal2))) {
            throw new RuntimeException("Literals given isn't computable!!!");
        }

        double doubleValue1 = changeToDoubleLiteral(literal1).getDoubleValue();
        double doubleValue2 = changeToDoubleLiteral(literal2).getDoubleValue();

        DoubleLiteralNode result = new DoubleLiteralNode();

        if (operator.equals(InfixExpression.Operator.PLUS)) {
            result.setTokenValue(doubleValue1 + doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.MINUS)) {
            result.setTokenValue(doubleValue1 - doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.TIMES)) {
            result.setTokenValue(doubleValue1 * doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.DIVIDE)) {
            result.setTokenValue(doubleValue1 / doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.REMAINDER)) {
            result.setTokenValue(doubleValue1 % doubleValue2);
        } else {
            throw new RuntimeException("Invalid infix math operator!!!");
        }

        return result;
    }

    private static BooleanLiteralNode compareTwoInfixLiteral(LiteralNode literal1, InfixExpression.Operator operator, LiteralNode literal2) {
        if (!(isComputableAndComparableLiteral(literal1) &&
                isComputableAndComparableLiteral(literal2))) {
            throw new RuntimeException("Literals given isn't comparable!!!");
        }

        double doubleValue1 = changeToDoubleLiteral(literal1).getDoubleValue();
        double doubleValue2 = changeToDoubleLiteral(literal2).getDoubleValue();

        BooleanLiteralNode result = new BooleanLiteralNode();

        if (operator.equals(InfixExpression.Operator.GREATER)) {
            result.setValue(doubleValue1 > doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.LESS)) {
            result.setValue(doubleValue1 < doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.GREATER_EQUALS)) {
            result.setValue(doubleValue1 >= doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
            result.setValue(doubleValue1 <= doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.EQUALS)) {
            result.setValue(doubleValue1 == doubleValue2);
        } else if (operator.equals(InfixExpression.Operator.NOT_EQUALS)) {
            result.setValue(doubleValue1 != doubleValue2);
        } else {
            throw new RuntimeException("Invalid infix comparison operator!!!");
        }

        return result;
    }

    /*Change char, int, double to double*/
    private static DoubleLiteralNode changeToDoubleLiteral(LiteralNode literal) {
        DoubleLiteralNode doubleLiteralNode = new DoubleLiteralNode();

        if (literal instanceof IntegerLiteralNode) {
            doubleLiteralNode.setTokenValue(Double.parseDouble(((IntegerLiteralNode) literal).getTokenValue()));
            return doubleLiteralNode;
        } else if (literal instanceof CharacterLiteralNode) {
            doubleLiteralNode.setTokenValue(((CharacterLiteralNode) literal).getIntegerValue());
            return doubleLiteralNode;
        } else if (literal instanceof DoubleLiteralNode) {
            return (DoubleLiteralNode) literal;
        } else {
            throw new RuntimeException("You only can change IntegerLiteral, CharacterLiteral or DoubleLiteral to DoubleLiteral!!!");
        }
    }

    /*Change char, number, boolean to double*/
    private static StringLiteralNode changeToStringLiteral(LiteralNode literal) {
        StringLiteralNode stringLiteralNode = new StringLiteralNode();

        if(literal.isStringLiteralNode()) {
            return  (StringLiteralNode) literal;
        } else if(literal.isNumberLiteralNode()) {
            stringLiteralNode.setStringValue(((NumberLiteralNode) literal).getTokenValue());
            return stringLiteralNode;
        } else if(literal.isCharacterLiteralNode()) {
            stringLiteralNode.setStringValue(String.valueOf(((CharacterLiteralNode) literal).getCharacterValue()));
            return stringLiteralNode;
        } else if(literal.isBooleanLiteralNode()) {
            stringLiteralNode.setStringValue(String.valueOf(((BooleanLiteralNode) literal).getValue()));
            return stringLiteralNode;
        } else {
            throw new RuntimeException("You only can change NumberLiteral, CharacterLiteral or BooleanLiteral to StringLiteral!!!");
        }
    }

    /*Change Number literal, Character literal to int type*/
    public static int changeLiteralNodeToInteger(LiteralNode literalNode) {
        if (literalNode instanceof NumberLiteralNode) {
            NumberLiteralNode numberLiteralNode = (NumberLiteralNode) literalNode;
            if(NumberLiteralNode.isIntegerValue((numberLiteralNode.getTokenValue()))) {
                return Integer.parseInt(numberLiteralNode.getTokenValue());
            } else {
                throw new RuntimeException("Can't change double to integer");
            }
        } else if(literalNode instanceof CharacterLiteralNode) {
            CharacterLiteralNode characterLiteralNode = (CharacterLiteralNode) literalNode;
            return (int) characterLiteralNode.getIntegerValue();
        } else {
            throw new RuntimeException("Invalid literal to change to integer");
        }
    }

    public final boolean isNumberLiteralNode() {
        return this instanceof NumberLiteralNode;
    }

    public final boolean isCharacterLiteralNode() {
        return this instanceof CharacterLiteralNode;
    }

    public final boolean isBooleanLiteralNode() {
        return this instanceof BooleanLiteralNode;
    }

    public final boolean isStringLiteralNode() {
        return this instanceof StringLiteralNode;
    }

    public final boolean isNullLiteralNode() {
        return this instanceof NullLiteralNode;
    }

    public final boolean isTypeLiteralNode() {
        return this instanceof TypeLiteralNode;
    }

    public static boolean isLiteral(ASTNode astNode) {
        return (astNode instanceof NumberLiteral ||
                astNode instanceof CharacterLiteral ||
                astNode instanceof StringLiteral ||
                astNode instanceof BooleanLiteral ||
                astNode instanceof NullLiteral ||
                astNode instanceof TypeLiteral);
    }
}
