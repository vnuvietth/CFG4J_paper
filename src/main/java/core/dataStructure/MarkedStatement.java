package core.dataStructure;

import java.util.ArrayList;
import java.util.List;

public class MarkedStatement {
    private String statement;
    private boolean isTrueConditionalStatement;
    private boolean isFalseConditionalStatement;

    public MarkedStatement(String statement, boolean isTrueConditionalStatement, boolean isFalseConditionalStatement) {
        this.statement = statement;
        this.isTrueConditionalStatement = isTrueConditionalStatement;
        this.isFalseConditionalStatement = isFalseConditionalStatement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setTrueConditionalStatement(boolean trueConditionalStatement) {
        isTrueConditionalStatement = trueConditionalStatement;
    }

    public void setFalseConditionalStatement(boolean falseConditionalStatement) {
        isFalseConditionalStatement = falseConditionalStatement;
    }

    public String getStatement() {
        return statement;
    }

    public boolean isTrueConditionalStatement() {
        return isTrueConditionalStatement;
    }

    public boolean isFalseConditionalStatement() {
        return isFalseConditionalStatement;
    }

    private static List<MarkedStatement> markedStatements = new ArrayList<>();

    public static boolean markOneStatement(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        tmpAdd(statement, isTrueCondition, isFalseCondition);
        if (!isTrueCondition && !isFalseCondition) return true;
        return !isFalseCondition;
    }

    public static void tmpAdd(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        MarkedStatement markedStatement = new MarkedStatement(statement, isTrueCondition, isFalseCondition);
        markedStatements.add(markedStatement);
    }

    public static List<MarkedStatement> getMarkedStatements() {
        return markedStatements;
    }

    public static void setMarkedStatements(List<MarkedStatement> markedStatements) {
        MarkedStatement.markedStatements = markedStatements;
    }
}
