package core.dataStructure;

import core.ast.additionalNodes.Node;
import core.cfg.CfgNode;

import java.util.ArrayList;
import java.util.List;

public final class Mark {

    private static List<String> statements = new ArrayList<>();

    private Mark(){}

    public static void reset() {
        statements = new ArrayList<>();
    }

    public static void add(String statement) {
        statements.add(statement);
    }

    public static boolean check(Path path) {
        Node currentNode = path.getBeginNode();
        int i = 0;
        while (currentNode != null && i < statements.size()) {
            CfgNode cfgNode = currentNode.getData();

            if(!cfgNode.getContent().equals(statements.get(i))) {
//                System.out.println(cfgNode.getContent() + "\n" + statements.get(i));
                return false;
            }

            // Updater
            i++;
            currentNode = currentNode.getNext();
            while (currentNode != null && currentNode.getData().getContent().equals("")) {
                currentNode = currentNode.getNext();
            }
        }
        return true;
    }
}
