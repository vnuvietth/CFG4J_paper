import core.algorithms.FindAllPath;
import core.algorithms.SymbolicExecution;
import core.cfg.CfgBlockNode;
import core.cfg.CfgEndBlockNode;
import core.cfg.CfgNode;
import core.dataStructure.Path;
import core.parser.ASTHelper;
import core.parser.ProjectParser;
import core.utils.Utils;
import org.eclipse.jdt.core.dom.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import com.microsoft.z3.*;

public class AppStart {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CppApi.class);
    private static long totalUsedMem = 0;
    private static long tickCount = 0;

    public static void main(String[] args) throws IOException {
//        System.load("D:/CFG4J_paper/z3-4.12.2-x64-win/bin/libz3java.dll");
//        Version version = new Version();
//        Context ctx = new Context();
//        Expr a = ctx.mkToRe(ctx.mkString("abcd"));
        String path = "data\\child\\CFG4J_Test.java";
        System.out.println("Start parsing...");
        ArrayList<ASTNode> funcAstNodeList = ProjectParser.parseFile(path);

        System.out.println("count = " + funcAstNodeList.size());

        for (ASTNode func : funcAstNodeList) {
            if (((MethodDeclaration)func).getName().getIdentifier().equals("LeapYear"))
            {
                System.out.println("func = " + ((MethodDeclaration)func).getName());
                List<ASTNode> parameters = ((MethodDeclaration) func).parameters();
                System.out.println("parameters.size() = " + ((MethodDeclaration) func).parameters().size());
//                System.out.println(((SingleVariableDeclaration)((MethodDeclaration) func).parameters().get(0)).getName().getIdentifier());

                Timer T = new Timer(true);

                TimerTask memoryTask = new TimerTask(){
                    @Override
                    public void run(){
                        totalUsedMem += (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
//                        System.out.println("totalUsedMem = " + totalUsedMem);
                        tickCount += 1;
//                        System.out.println("tickCount = " + tickCount);
                        //callback.accept(totalUsedMem);
                    }
                };

                T.scheduleAtFixedRate(memoryTask, 0, 1); //0 delay and 5 ms tick

                LocalDateTime beforeTime = LocalDateTime.now();

                Block functionBlock = Utils.getFunctionBlock(func);

                CfgNode cfgBeginCfgNode = new CfgNode();
                cfgBeginCfgNode.setIsBeginCfgNode(true);

                CfgEndBlockNode cfgEndCfgNode = new CfgEndBlockNode();
                cfgEndCfgNode.setIsEndCfgNode(true);

                CfgNode block = new CfgBlockNode();
                block.setAst(functionBlock);

                block.setBeforeStatementNode(cfgBeginCfgNode);
                block.setAfterStatementNode(cfgEndCfgNode);

                FindAllPath paths = new FindAllPath(ASTHelper.generateCFGFromASTBlockNode(block));

                System.out.println("Number of paths: " + paths.getPaths().size());
                Path testPath = paths.getPaths().get(1);
                System.out.println(testPath);
                SymbolicExecution solution = new SymbolicExecution(testPath, parameters);

//                System.out.println(((Assignment)((ExpressionStatement)testPath.getBeginNode().getAst()).getExpression()).getRightHandSide());
//                System.out.println(((InfixExpression)((Assignment)((ExpressionStatement)testPath.getBeginNode().getAst()).getExpression()).getRightHandSide()).getLeftOperand().getClass());
//                testPath.symbolicExecution(((MethodDeclaration) func).parameters());
//                for(Object node : ((InfixExpression)((Assignment)((ExpressionStatement)testPath.getBeginNode().getAst()).getExpression()).getRightHandSide()).extendedOperands()) {
//                    System.out.println(node);
//                    System.out.println(node.getClass());
//                }


                LocalDateTime afterTime = LocalDateTime.now();

                Duration duration = Duration.between(beforeTime, afterTime);

                float diff = Math.abs((float) duration.toMillis());

                T.cancel();

//                System.out.println("func = " + ((MethodDeclaration)func).getName());
//                System.out.println("used time = " + diff + " ms");
////                System.out.println("tickCount = " + tickCount);
//                float usedMem = ((float)totalUsedMem)/tickCount/1024/1024;
//                System.out.print("used mem = ");
//                System.out.printf("%.2f", usedMem);
//                System.out.println(" MB");
            }
        }

        //print the template of test report
//        try {
//            Utils.printReport(AppStart.class.getResource(Exporter._TEMPLATE_REPORT_PATH).toURI().getPath());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }
}
