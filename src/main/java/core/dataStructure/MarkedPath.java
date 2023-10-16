package core.dataStructure;

import core.cfg.CfgBoolExprNode;
import core.cfg.CfgNode;

import java.util.ArrayList;

public final class MarkedPath {

//    private static List<MarkedStatement> markedStatements = new ArrayList<>();

    private MarkedPath() {
    }

    public static boolean markOneStatement(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        tmpAdd(statement, isTrueCondition, isFalseCondition);
        if (!isTrueCondition && !isFalseCondition) return true;
        return !isFalseCondition;
    }

    public static void tmpAdd(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        MarkedStatement markedStatement = new MarkedStatement(statement, isTrueCondition, isFalseCondition);
        MarkedStatement.getMarkedStatements().add(markedStatement);
    }

    public static void markPathToCFG(CfgNode rootNode) {
        int i = 0;
        while (rootNode != null && i < MarkedStatement.getMarkedStatements().size()) {
            // Kiểm tra những CfgNode không có content
            if(rootNode.getContent().equals("")) {
                rootNode.setMarked(true);
                rootNode = rootNode.getAfterStatementNode();
                continue;
            }

            MarkedStatement markedStatement = MarkedStatement.getMarkedStatements().get(i);
            if (rootNode.getContent().equals(markedStatement.getStatement())) {
                rootNode.setMarked(true);
            } else {
                reset();
                return;
            }

            if (rootNode instanceof CfgBoolExprNode) {
                CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;
                if (markedStatement.isFalseConditionalStatement()) {
                    boolExprNode.setFalseMarked(true);
                    rootNode = boolExprNode.getFalseNode();
                } else if (markedStatement.isTrueConditionalStatement()) {
                    boolExprNode.setTrueMarked(true);
                    rootNode = boolExprNode.getTrueNode();
                }
                i++;
                continue;
            }

            // Updater
            i++;
            rootNode = rootNode.getAfterStatementNode();
        }
        while (rootNode != null) {
            if(rootNode.getContent().equals("")) {
                rootNode.setMarked(true);
                rootNode = rootNode.getAfterStatementNode();
            }
        }
        reset();
    }

        public static CfgNode findUncoveredNode(CfgNode rootNode, CfgNode duplicateNode) {
        if (rootNode == null || !rootNode.isMarked()) {
            return rootNode;
        }
        if (rootNode instanceof CfgBoolExprNode) {
            CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;



            if (!boolExprNode.isTrueMarked()) {
                return boolExprNode.getTrueNode();
            }
            if (!boolExprNode.isFalseMarked()) {
                return boolExprNode.getFalseNode();
            }

            // Check for duplicateNode. Nếu có node trùng lặp tức là đã duyệt qua 1 vòng của loop đấy và k thấy node chưa mark nên return null.
            if(boolExprNode != duplicateNode) {
                duplicateNode = boolExprNode;
                CfgNode trueBranchUncoveredNode = findUncoveredNode(boolExprNode.getTrueNode(), duplicateNode);
                return trueBranchUncoveredNode;
            }
            else {
                CfgNode falseBranchUncoveredNode = findUncoveredNode(boolExprNode.getFalseNode(), duplicateNode);
                return falseBranchUncoveredNode;
            }



//            if(tmpUncoveredNode == null) {
//                return findUncoveredNode(boolExprNode.getTrueNode(), duplicateNode);
//            } else {
//                return tmpUncoveredNode;
//            }
//            return falseBranchUncoveredNode == null ? trueBranchUncoveredNode : falseBranchUncoveredNode;
        }

        CfgNode cfgNode =  findUncoveredNode(rootNode.getAfterStatementNode(), duplicateNode);
        return cfgNode;
    }

//    public static CfgNode findUncoveredNode(CfgNode rootNode, CfgNode duplicateNode) {
//        if (rootNode == null || !rootNode.isMarked()) {
//            return rootNode;
//        }
//        if (rootNode instanceof CfgBoolExprNode) {
//            CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;
//
//            // Check for duplicateNode. Nếu có node trùng lặp tức là đã duyệt qua 1 vòng của loop đấy và k thấy node chưa mark nên return null.
//            if(boolExprNode != duplicateNode) duplicateNode = boolExprNode;
//            else {
//                return null;
//            }
//
//            if (!boolExprNode.isTrueMarked()) {
//                return boolExprNode.getTrueNode();
//            }
//            if (!boolExprNode.isFalseMarked()) {
//                return boolExprNode.getFalseNode();
//            }
//
//            CfgNode falseBranchUncoveredNode = findUncoveredNode(boolExprNode.getFalseNode(), duplicateNode);
//            CfgNode trueBranchUncoveredNode = findUncoveredNode(boolExprNode.getTrueNode(), duplicateNode);
////            if(tmpUncoveredNode == null) {
////                return findUncoveredNode(boolExprNode.getTrueNode(), duplicateNode);
////            } else {
////                return tmpUncoveredNode;
////            }
//            return falseBranchUncoveredNode == null ? trueBranchUncoveredNode : falseBranchUncoveredNode;
//        }
//
//        CfgNode cfgNode =  findUncoveredNode(rootNode.getAfterStatementNode(), duplicateNode);
//        return cfgNode;
//    }

    private static void reset() {
        MarkedStatement.setMarkedStatements(new ArrayList<>());
    }
}
