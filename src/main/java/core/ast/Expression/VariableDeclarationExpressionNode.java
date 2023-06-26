package core.ast.Expression;

import core.ast.AstNode;
import core.ast.VariableDeclaration.VariableDeclarationFragmentNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.List;

public class VariableDeclarationExpressionNode extends ExpressionNode {
    private List<ASTNode> modifiers = null;
    private Type baseType = null;
    private List<AstNode> variableDeclarationFragments;

    public static void executeVariableDeclarationExpression(VariableDeclarationExpression variableDeclarationExpression,
                                                            MemoryModel memoryModel) {
        List<VariableDeclarationFragment> fragments = variableDeclarationExpression.fragments();
        for(VariableDeclarationFragment fragment : fragments) {
            VariableDeclarationFragmentNode.executeVariableDeclarationFragment(fragment, variableDeclarationExpression.getType(), memoryModel);
        }
    }
}
