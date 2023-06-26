package core.ast.Statement;

import core.ast.AstNode;
import core.ast.Expression.ExpressionNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.ExpressionStatement;

public class ExpressionStatementNode extends StatementNode {

    private ExpressionNode expression = null;

    public static AstNode executeExpressionStatement(ExpressionStatement expressionStatement, MemoryModel memoryModel) {
        ExpressionStatementNode expressionStatementNode = new ExpressionStatementNode();
        expressionStatementNode.expression = (ExpressionNode) ExpressionNode.executeExpression(expressionStatement.getExpression(), memoryModel);
        return expressionStatementNode;
    }

}
