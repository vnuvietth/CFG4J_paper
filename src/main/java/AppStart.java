import core.algorithms.FindAllPath;
import core.algorithms.SymbolicExecution;
import core.cfg.CfgBlockNode;
import core.cfg.CfgEndBlockNode;
import core.cfg.CfgNode;
import core.dataStructure.Mark;
import core.dataStructure.Path;
import core.parser.ASTHelper;
import core.parser.ProjectParser;
import static core.testDriver.Utils.*;
import core.utils.Utils;
import data.child.CFG4J_Test;
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

    public static List<String> statements;

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String path = "src\\main\\java\\data\\child\\CFG4J_Test.java";
        System.out.println("Start parsing...");
        ArrayList<ASTNode> funcAstNodeList = ProjectParser.parseFile(path);

        System.out.println("count = " + funcAstNodeList.size());

        for (ASTNode func : funcAstNodeList) {
            if (((MethodDeclaration)func).getName().getIdentifier().equals("testSymbolicExecution"))
            {
                System.out.println("func = " + ((MethodDeclaration)func).getName());
                List<ASTNode> parameters = ((MethodDeclaration) func).parameters();

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
                System.out.println("Dynamic Execute");
                statements = new ArrayList<>();
                Method m = CFG4J_Test.class.getDeclaredMethod("testSymbolicExecution", getParameterClasses(parameters));
                int[] oneDArr = new int[] {1, 2, 3};
                int[][] twoDArray = new int[][] {{1, 2}, {3, 4}};
                int[][][] threeDArray = {
                        {
                                {1, 2, 3},
                                {4, 5, 6},
                                {7, 8, 9}
                        },
                        {
                                {10, 11, 12},
                                {13, 14, 15},
                                {16, 17, 18}
                        }
                };
                Object oj = m.invoke(null, 2, 5, threeDArray);
                if(Mark.check(testPath)) {
                    System.out.println("Path is covered");
                } else {
                    System.out.println("Path is not covered");

                }
                //============================

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
