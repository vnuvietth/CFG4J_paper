package core.algorithms;

import com.microsoft.z3.*;
import core.ast.AstNode;
import core.ast.Expression.*;
import core.ast.Expression.OperationExpression.*;
import core.ast.additionalNodes.Node;
import core.cfg.CfgBoolExprNode;
import core.cfg.CfgNode;
import core.dataStructure.MemoryModel;
import core.dataStructure.Path;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PrefixExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class SymbolicExecution {
    private MemoryModel memoryModel;
    private Path testPath;

    public SymbolicExecution(Path testPath, List<ASTNode> parameters) {
        this.testPath = testPath;
        this.memoryModel = new MemoryModel();

        List<Expr> vars = new ArrayList<>();
        HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);

        for (ASTNode astNode : parameters) {
            AstNode.executeASTNode(astNode, this.memoryModel);
        }

        Node currentNode = testPath.getBeginNode();

        Expr finalZ3Expression = null;

        while (currentNode != null) {
            CfgNode currentCfgNode = currentNode.getData();

            ASTNode astNode = currentCfgNode.getAst();

            if (astNode != null) {
                AstNode executedAstNode = AstNode.executeASTNode(astNode, memoryModel);
                if (currentNode.getData() instanceof CfgBoolExprNode) {

                    // Kiểm tra xem path hiện tại chứa node bool phủ định
                    if (currentNode.getNext() != null && currentNode.getNext().getData().isFalseNode()) {
                        PrefixExpressionNode newAstNode = new PrefixExpressionNode();
                        newAstNode.setOperator(PrefixExpression.Operator.NOT);
                        newAstNode.setOperand((ExpressionNode) executedAstNode);

                        executedAstNode = PrefixExpressionNode.executePrefixExpressionNode(newAstNode, memoryModel);
                    }

                    BoolExpr constrain = (BoolExpr) OperationExpressionNode.createZ3Expression((ExpressionNode) executedAstNode, ctx, vars, memoryModel);

//                    if (executedAstNode instanceof InfixExpressionNode) {
//                        System.out.println("CONSTRAIN: " + executedAstNode.toString());
//                        constrain = (BoolExpr) InfixExpressionNode.createZ3Expression((InfixExpressionNode) executedAstNode, ctx, vars, memoryModel);
//                        Model model = createModel(ctx, constrain);
//                        System.out.println("RESULT:");
//                        for (Expr var : vars) {
//                            System.out.println(var.toString() + " = " + model.evaluate(var, false));
//                        }
//                    } else if (executedAstNode instanceof PostfixExpressionNode) {
//                        System.out.println("CONSTRAIN: " + executedAstNode.toString());
//                        constrain = (BoolExpr) PostfixExpressionNode.createZ3Expression((PostfixExpressionNode) executedAstNode, ctx, vars, memoryModel);
//                        Model model = createModel(ctx, constrain);
//                        System.out.println("RESULT:");
//                        for (Expr var : vars) {
//                            System.out.println(var.toString() + " = " + model.evaluate(var, false));
//                        }
//                    } else if (executedAstNode instanceof PrefixExpressionNode) {
//                        System.out.println("CONSTRAIN: " + executedAstNode.toString());
//                        constrain = (BoolExpr) PrefixExpressionNode.createZ3Expression((PrefixExpressionNode) executedAstNode, ctx, vars, memoryModel);
//                        Model model = createModel(ctx, constrain);
//                        System.out.println("RESULT:");
//                        for (Expr var : vars) {
//                            System.out.println(var.toString() + " = " + model.evaluate(var, false));
//                        }
//                    } else if (executedAstNode instanceof ParenthesizedExpressionNode) {
//                        System.out.println("CONSTRAIN: " + executedAstNode.toString());
//                        constrain = (BoolExpr) ParenthesizedExpressionNode.createZ3Expression((ParenthesizedExpressionNode) executedAstNode, ctx, vars, memoryModel);
//                        Model model = createModel(ctx, constrain);
//                        System.out.println("RESULT:");
//                        for (Expr var : vars) {
//                            System.out.println(var.toString() + " = " + model.evaluate(var, false));
//                        }
//                    }

                    if (finalZ3Expression == null) finalZ3Expression = constrain;
                    else {
                        finalZ3Expression = ctx.mkAnd(finalZ3Expression, constrain);
                    }
                }
            }
            currentNode = currentNode.getNext();
        }

        Model model = createModel(ctx, (BoolExpr) finalZ3Expression);
        evaluateModel(model, vars);
    }

    private Model createModel(Context ctx, BoolExpr f) {
        Solver s = ctx.mkSolver();
        s.add(f);
        System.out.println(s);

        Status satisfaction = s.check();
        if (satisfaction != Status.SATISFIABLE) {
            System.out.println("Expression is UNSATISFIABLE");
            return null;
        } else {
            return s.getModel();
        }
    }

    private void evaluateModel(Model model, List<Expr> vars) {
        if (model == null) {
            System.out.println("There is nothing to evaluate");
        } else {
            System.out.println("RESULT OF EVALUATION:");
            for (Expr var : vars) {
                System.out.println(var.toString() + " = " + model.evaluate(var, false));
            }
        }
    }
}
