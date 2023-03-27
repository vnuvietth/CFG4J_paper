package core.cfg;

import core.node.Node;

public class CfgBeginForEachNode extends CfgNode
{
    private CfgEndBlockNode endBlockNode = null;

    public CfgEndBlockNode getEndBlockNode()
    {
        return endBlockNode;
    }

    public void setEndBlockNode(CfgEndBlockNode endBlockNode)
    {
        this.endBlockNode = endBlockNode;
    }
}
