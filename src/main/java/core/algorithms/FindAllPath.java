package core.algorithms;

import core.cfg.CfgBoolExprNode;
import core.cfg.CfgForEachExpressionNode;
import core.cfg.CfgNode;
import core.dataStructure.Path;

import java.util.ArrayList;
import java.util.List;

public class FindAllPath {

    private List<Path> paths = new ArrayList<>();
    private List<CfgNode> currentPath = new ArrayList<>();

    private final int DEPTH = 2;

    public FindAllPath() {}

    public FindAllPath(CfgNode cfgRootNode) {
        findPaths(cfgRootNode);
    }

    private void findPaths(CfgNode cfgNode) {
        if(cfgNode == null) return;

        // Add a path to the list of path if the node is endNode
        if(cfgNode.getIsEndCfgNode()) {
            currentPath.add(cfgNode);
            Path path = new Path();
            for(CfgNode node : currentPath) {
                path.addNextNode(node);
            }
            paths.add(path);
            currentPath.remove(currentPath.size() - 1);
        } else {
            currentPath.add(cfgNode);
            if(cfgNode instanceof CfgBoolExprNode) {

                // CfgBoolExprNode has 2 child node is trueNode and falseNode
                if(numberOfDuplicateNode(cfgNode) < DEPTH) {
                    findPaths(((CfgBoolExprNode) cfgNode).getTrueNode());
                }
                findPaths(((CfgBoolExprNode) cfgNode).getFalseNode());

            } else if(cfgNode instanceof CfgForEachExpressionNode) {

                // CfgForEachExpressionNode has 2 child node is hasElementNode and noMoreElementNode
                if(numberOfDuplicateNode(cfgNode) < DEPTH) {
                    findPaths(((CfgForEachExpressionNode) cfgNode).getHasElementAfterNode());
                }
                findPaths(((CfgForEachExpressionNode) cfgNode).getNoMoreElementAfterNode());

            } else {

                // Every other node has only one child node
                findPaths(cfgNode.getAfterStatementNode());

            }
            currentPath.remove(currentPath.size() - 1);
        }
    }

    private int numberOfDuplicateNode(CfgNode node) {
        int duplicateNode = 0;
        for(CfgNode nodeI : currentPath) {
            if(nodeI == node) duplicateNode++;
        }
        return duplicateNode;
    }

    public List<Path> getPaths() {
        return paths;
    }
}
