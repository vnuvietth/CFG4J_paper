package core.dataStructure;

import core.cfg.CfgNode;

public class Path {
    private class Node {
        CfgNode data;
        Node next;

        public Node() {}

        public Node(CfgNode data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node beginNode;

    private Node currentNode;

    public boolean isEmpty() {
        return beginNode == null;
    }

    public void addNextNode(CfgNode data) {
        Node previousNode = currentNode;
        currentNode = new Node(data);
        if(isEmpty()) beginNode = currentNode;
        else previousNode.next = currentNode;
    }

    @Override
    public String toString() {
        String p = "===============\n";
        Node tmpNode = beginNode;
        while(tmpNode != null) {
            p += tmpNode.data.toString();
            p += "\n";
            tmpNode = tmpNode.next;
        }
        p += "===============";
        return p;
    }

    public CfgNode getBeginNode() {
        return beginNode.data;
    }

}
