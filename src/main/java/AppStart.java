import core.algorithms.FindAllPath;
import core.algorithms.FindPath;
import core.algorithms.SymbolicExecution;
import core.cfg.CfgBlockNode;
import core.cfg.CfgEndBlockNode;
import core.cfg.CfgNode;
import core.dataStructure.MarkedPath;
import core.dataStructure.MarkedPathV2;
import core.dataStructure.Path;
import core.parser.ASTHelper;
import core.parser.ProjectParser;
import core.utils.Utils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static core.testDriver.Utils.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class AppStart {
    //    private static final Logger LOGGER = LoggerFactory.getLogger(CppApi.class);
    private static long totalUsedMem = 0;
    private static long tickCount = 0;

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        String path = "src\\main\\java\\data\\CFG4J_Test.java";
        System.out.println("Start parsing...");
        ArrayList<ASTNode> funcAstNodeList = ProjectParser.parseFile(path);

        System.out.println("count = " + funcAstNodeList.size());

        String methodName = "function";
        String className = "data.CFG4J_Test";

        for (ASTNode func : funcAstNodeList) {
            if (((MethodDeclaration) func).getName().getIdentifier().equals(methodName)) {
                System.out.println("func = " + ((MethodDeclaration) func).getName());
                List<ASTNode> parameters = ((MethodDeclaration) func).parameters();
                System.out.println("parameters.size() = " + ((MethodDeclaration) func).parameters().size());

                Timer T = new Timer(true);

                TimerTask memoryTask = new TimerTask() {
                    @Override
                    public void run() {
                        totalUsedMem += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
//                        System.out.println("totalUsedMem = " + totalUsedMem);
                        tickCount += 1;
//                        System.out.println("tickCount = " + tickCount);
                        //callback.accept(totalUsedMem);
                    }
                };

                T.scheduleAtFixedRate(memoryTask, 0, 1); //0 delay and 5 ms tick

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

                ASTHelper.generateCFGFromASTBlockNode(block);
                CfgNode cfgNode = cfgBeginCfgNode;
                //===========================



                //============================
                LocalDateTime beforeTime = LocalDateTime.now();
//                methodName = methodName + "CloneV1";
                methodName = "functionCloneV1";

                Class<?>[] parameterClasses = getParameterClasses(parameters);
                Method method = Class.forName(className).getDeclaredMethod(methodName, parameterClasses);

                method.invoke(parameterClasses, createRandomTestData(parameterClasses));
                MarkedPath.markPathToCFG(cfgNode);

                List<Object[]> testData = new ArrayList<>();

                for (CfgNode uncoveredNode = MarkedPath.findUncoveredNode(cfgNode, null); uncoveredNode != null; ) {

                    Path newPath = (new FindPath(cfgNode, uncoveredNode, cfgEndCfgNode)).getPath();

                    SymbolicExecution solution = new SymbolicExecution(newPath, parameters);

                    testData.add(getParameterValue(parameterClasses));
                    for(int i = 0; i < testData.size(); i++) {
                        method.invoke(parameterClasses, testData.get(i));
                        MarkedPath.markPathToCFG(cfgNode);
                    }

                    uncoveredNode = MarkedPath.findUncoveredNode(cfgNode, null);
                }
                System.out.println("Tested successfully with 100% coverage");

                LocalDateTime afterTime = LocalDateTime.now();

                Duration duration = Duration.between(beforeTime, afterTime);

                float diff = Math.abs((float) duration.toMillis());
                System.out.println("Total Concolic time: " + diff);
                //========================================

                T.cancel();

                System.out.println("func = " + ((MethodDeclaration) func).getName());
//                System.out.println("tickCount = " + tickCount);
                float usedMem = ((float) totalUsedMem) / tickCount / 1024 / 1024;
                System.out.print("used mem = ");
                System.out.printf("%.2f", usedMem);
                System.out.println(" MB");

                break;
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
