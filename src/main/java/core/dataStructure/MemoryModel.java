package core.dataStructure;

import core.ast.AstNode;

import java.util.HashMap;

public class MemoryModel {
    private HashMap<String, AstNode> S = new HashMap<>();;
    public MemoryModel() {}

    public void put(String name, AstNode element) {
        S.put(name, element);
    }

    public AstNode get(String name) {
        return S.get(name);
    }
}
