package core.ast.VariableDeclaration;

import core.ast.AstNode;
import core.ast.Expression.Name.SimpleNameNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

public class SingleVariableDeclarationNode extends VariableDeclarationNode {

    private List<AstNode> modifiers = null;
    private Type type = null;
    private List<AstNode> varargsAnnotations = null;
    private boolean variableArity = false;

    public static void executeSingleVariableDeclaration(SingleVariableDeclaration singleVariableDeclaration, MemoryModel memoryModel) {
        // For parameters!!!
        Type type = singleVariableDeclaration.getType();
        SimpleName variableName = singleVariableDeclaration.getName();
        SimpleNameNode simpleNameNode = new SimpleNameNode();
        simpleNameNode.setIdentifier(variableName.getIdentifier());

        String key = simpleNameNode.getIdentifier();

        if(type instanceof PrimitiveType) {
            memoryModel.declarePrimitiveTypeVariable(((PrimitiveType) type).getPrimitiveTypeCode(), key, simpleNameNode);
        } else { // OTHER TYPES

        }


//        if(type instanceof PrimitiveType) {
//            memoryModel.put(key, PrimitiveTypeNode.changePrimitiveTypeToLiteralInitialization((PrimitiveType) type));
//        } else {
//            /*????*/
//            throw new RuntimeException("Did not handle other type yet");
//        }
    }

}
