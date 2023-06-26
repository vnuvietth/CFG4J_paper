package core.ast.Statement;

import core.ast.AstNode;
import core.ast.Statement.StatementNode;
import core.ast.VariableDeclaration.VariableDeclarationFragmentNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import java.util.List;

public class VariableDeclarationStatementNode extends StatementNode {
    private List<ASTNode> modifiers = null;
    private Type baseType = null;
    private List<AstNode> variableDeclarationFragments;

    public static void executeVariableDeclarationStatement(VariableDeclarationStatement statement, MemoryModel memoryModel) {
        List<VariableDeclarationFragment> fragments = statement.fragments();
        for(VariableDeclarationFragment fragment : fragments) {
            VariableDeclarationFragmentNode.executeVariableDeclarationFragment(fragment, statement.getType(), memoryModel);
        }
    }
}
