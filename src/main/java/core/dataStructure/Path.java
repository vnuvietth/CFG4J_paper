package core.dataStructure;

import core.ast.additionalNodes.Node;
import core.cfg.*;
import org.eclipse.jdt.core.dom.*;

import java.util.*;

public class Path {

    private Node beginNode;

    private Node currentNode;

    public boolean isEmpty() {
        return beginNode == null;
    }

    public void addNextNode(CfgNode data) {
        Node previousNode = currentNode;
        currentNode = new Node(data);
        if (isEmpty()) beginNode = currentNode;
        else previousNode.setNext(currentNode);
    }

    @Override
    public String toString() {
        StringBuilder p = new StringBuilder("===============\n");
        Node tmpNode = beginNode;
        while (tmpNode != null) {
            p.append(tmpNode.getData().toString());
            p.append("\n");
            tmpNode = tmpNode.getNext();
        }
        p.append("===============");
        return p.toString();
    }

    public CfgNode getBeginCfgNode() {
        return beginNode.getData();
    }

    public Node getBeginNode() {
        return beginNode;
    }
}

