import core.algorithms.FindAllPath;
import core.algorithms.SymbolicExecution;
import core.cfg.CfgBlockNode;
import core.cfg.CfgEndBlockNode;
import core.cfg.CfgNode;
import core.dataStructure.MarkedPath;
import core.dataStructure.Path;
import core.parser.ASTHelper;
import core.parser.ProjectParser;
import core.utils.Utils;
import org.eclipse.jdt.core.dom.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class AppStart {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CppApi.class);
    private static long totalUsedMem = 0;
    private static long tickCount = 0;

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        String path = "src\\main\\java\\data\\child\\CFG4J_Test.java";
        System.out.println("Start parsing...");
        ArrayList<ASTNode> funcAstNodeList = ProjectParser.parseFile(path);

        System.out.println("count = " + funcAstNodeList.size());

        String methodName = "testSymbolicExecution";

        for (ASTNode func : funcAstNodeList) {
            if (((MethodDeclaration)func).getName().getIdentifier().equals(methodName))
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

                // Generate CFG
                Block functionBlock = Utils.getFunctionBlock(func);

                CfgNode cfgBeginCfgNode = new CfgNode();
                cfgBeginCfgNode.setIsBeginCfgNode(true);

                CfgEndBlockNode cfgEndCfgNode = new CfgEndBlockNode();
                cfgEndCfgNode.setIsEndCfgNode(true);

                CfgNode block = new CfgBlockNode();
                block.setAst(functionBlock);

                block.setBeforeStatementNode(cfgBeginCfgNode);
                block.setAfterStatementNode(cfgEndCfgNode);

                CfgNode cfgNode = ASTHelper.generateCFGFromASTBlockNode(block);
                //===========================

                // Find all paths
                FindAllPath paths = new FindAllPath(cfgNode);
                //============================

                // Symbolic Execute
                System.out.println("Number of paths: " + paths.getPaths().size());
                Path testPath = paths.getPaths().get(0);
                System.out.println(testPath);
                SymbolicExecution solution = new SymbolicExecution(testPath, parameters);
                //============================

                // Dynamic Execute
//                methodName = methodName + "Clone";
                System.out.println("Dynamic Execute");
                Method m = Class.forName("data.child.CFG4J_Test").getDeclaredMethod(methodName, int.class, int.class);
                m.invoke(null, 2, 1);
                if(MarkedPath.check(testPath)) {
                    System.out.println("PATH IS COVERED");
                } else {
                    System.out.println("PATH IS NOT COVERED");
                }
                //============================

                LocalDateTime afterTime = LocalDateTime.now();

                Duration duration = Duration.between(beforeTime, afterTime);

                float diff = Math.abs((float) duration.toMillis());

                T.cancel();

                break;

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
