package core.algorithms;

import core.ast.AstNode;
import core.ast.Expression.InfixExpressionNode;
import core.ast.additionalNodes.Node;
import core.cfg.CfgBoolExprNode;
import core.cfg.CfgNode;
import core.dataStructure.MemoryModel;
import core.dataStructure.Path;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public final class SymbolicExecution {
    private MemoryModel memoryModel;
    private Path testPath;

    public SymbolicExecution(Path testPath, List<ASTNode> parameters) {
        this.testPath = testPath;
        this.memoryModel = new MemoryModel();

        List<String> constrains = new ArrayList<>();

        for (ASTNode astNode : parameters) {
            AstNode.executeASTNode(astNode, this.memoryModel);
        }

        Node currentNode = testPath.getBeginNode();

        while (currentNode != null) {
            CfgNode currentCfgNode = currentNode.getData();

            ASTNode astNode = currentCfgNode.getAst();

            if(astNode != null) {
                AstNode executedAstNode = AstNode.executeASTNode(astNode, memoryModel);
                if(currentNode.getData() instanceof CfgBoolExprNode) {
                    if(executedAstNode instanceof InfixExpressionNode) {
                        String constrain = ((InfixExpressionNode) executedAstNode).constrainToString();

                        if(currentNode.getNext() != null && currentNode.getNext().getData().isFalseNode()) {
                            constrain = "! " + constrain;
                        }
                        constrains.add(constrain);
                    }
                }
            }
            currentNode = currentNode.getNext();
        }
        System.out.println(constrains);
    }
}
