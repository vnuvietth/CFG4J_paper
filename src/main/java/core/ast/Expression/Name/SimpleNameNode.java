package core.ast.Expression.Name;

import core.ast.Expression.ExpressionNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.SimpleName;

public class SimpleNameNode extends NameNode {
    private String identifier = "MISSING";

    public static ExpressionNode executeSimpleName(SimpleName simpleName, MemoryModel memoryModel) {
        SimpleNameNode simpleNameNode = new SimpleNameNode();
        simpleNameNode.identifier = simpleName.getIdentifier();
        return NameNode.executeNameNode(simpleNameNode, memoryModel);
    }

    public static ExpressionNode executeSimpleNameNode(SimpleNameNode simpleNameNode, MemoryModel memoryModel) {
        return (ExpressionNode) memoryModel.get(simpleNameNode.identifier);
    }

    public static String getStringSimpleName(SimpleName simpleName) {
        return simpleName.getIdentifier();
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
