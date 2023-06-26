package core.ast.Expression;


import core.ast.AstNode;
import core.ast.Expression.Literal.LiteralNode;
import core.ast.Expression.Name.NameNode;
import core.dataStructure.MemoryModel;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;

public class AssignmentNode extends ExpressionNode {

    private Assignment.Operator operator;
    private ExpressionNode leftHandSide;
    private ExpressionNode rightHandSide;

    public static void executeAssignment(Assignment assignment, MemoryModel memoryModel) {
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.rightHandSide = (ExpressionNode) ExpressionNode.executeExpression(assignment.getRightHandSide(), memoryModel);
        assignmentNode.operator = assignment.getOperator();

        Expression leftHandSide = assignment.getLeftHandSide();

        if(leftHandSide instanceof Name) {
            String key = NameNode.getStringName((Name) leftHandSide);
            memoryModel.put(key, assignmentNode.rightHandSide);
        } else if(leftHandSide instanceof ArrayAccess){
            ArrayAccess arrayAccess = (ArrayAccess) leftHandSide;

            int index;
            ExpressionNode arrayIndex = (ExpressionNode) AstNode.executeASTNode(arrayAccess.getIndex(), memoryModel);
            if(arrayIndex instanceof LiteralNode) {
                index = LiteralNode.changeLiteralNodeToInteger((LiteralNode) arrayIndex);
            } else {
                throw new RuntimeException("Can't execute Index");
            }

            Expression arrayExpression = arrayAccess.getArray();
            ArrayNode arrayNode;
            if(arrayExpression instanceof ArrayAccess) {
                arrayNode = (ArrayNode) ArrayAccessNode.executeArrayAccessNode((ArrayAccess) arrayExpression, memoryModel);
            } else if(arrayExpression instanceof Name){
                String name = NameNode.getStringName((Name) arrayExpression);
                arrayNode = (ArrayNode) memoryModel.get(name);
            } else {
                throw new RuntimeException("Can't execute ArrayAccess");
            }
            arrayNode.set(assignmentNode.rightHandSide, index);
        }
    }
}
