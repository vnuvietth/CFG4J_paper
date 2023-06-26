package core.ast.VariableDeclaration;

import core.ast.Expression.ExpressionNode;
import core.ast.Type.AnnotatableType.PrimitiveTypeNode;
import core.ast.VariableDeclaration.VariableDeclarationNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.*;

public class VariableDeclarationFragmentNode extends VariableDeclarationNode {

    public static void executeVariableDeclarationFragment(VariableDeclarationFragment fragment,
                                                          Type baseType,
                                                          MemoryModel memoryModel) {
        VariableDeclaration declaration = (VariableDeclaration) fragment;

        String name = declaration.getName().getIdentifier();
        Expression initializer = declaration.getInitializer();

        if(initializer != null) {
            memoryModel.put(name, ExpressionNode.executeExpression(initializer, memoryModel));
            /*Error!!!!: char x = 5;*/
        } else {
            if(baseType instanceof PrimitiveType) {
                memoryModel.put(name, PrimitiveTypeNode.changePrimitiveTypeToLiteralInitialization((PrimitiveType) baseType));
            } else {
                throw new RuntimeException("Only deal with PrimitiveType!!");
            }
        }
    }

}
