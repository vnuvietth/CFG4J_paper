package core.dataStructure;

import core.ast.AstNode;
import core.variable.ArrayTypeVariable;
import core.variable.PrimitiveTypeVariable;
import core.variable.Variable;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.PrimitiveType;

import java.util.HashMap;
import java.util.Map;

public class MemoryModel { // ONLY FOR PRIMITIVE TYPES!!!!
    private HashMap<Variable, AstNode> S = new HashMap<>();
    ;

    public MemoryModel() {
    }

    public void assignVariable(String name, AstNode element) {
        for (Map.Entry<Variable, AstNode> set : S.entrySet()) {
            if (set.getKey().getName().equals(name)) {
                set.setValue(element);
                break;
            }
        }
    }

    public void declarePrimitiveTypeVariable(PrimitiveType.Code typeCode, String name, AstNode element) {
        S.put(new PrimitiveTypeVariable(typeCode, name), element);
    }

    public void declareArrayTypeVariable(ArrayType type, String name, AstNode element) {
        S.put(new ArrayTypeVariable(type, name), element);
    }

    public AstNode getValue(String name) {
        for (Map.Entry<Variable, AstNode> set : S.entrySet()) {
            if (set.getKey().getName().equals(name)) {
                return set.getValue();
            }
        }
        throw new RuntimeException("There's no variable with name: " + name + " in memory model!");
    }

    public Variable getVariable(String name) {
        for (Map.Entry<Variable, AstNode> set : S.entrySet()) {
            if(set.getKey().getName().equals(name)) {
                return set.getKey();
            }
        }
        throw new RuntimeException("There's no variable with name: " + name + " in memory model!");
    }
}
