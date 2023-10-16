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
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


import static core.testDriver.Utils.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class AppStart {

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InterruptedException {
        String path = "src\\main\\java\\data\\CFG4J_Test.java";

        // Parse File
        ArrayList<ASTNode> funcAstNodeList = ProjectParser.parseFile(path);
        CompilationUnit compilationUnit = ProjectParser.parseFileToCompilationUnit(path);

        List<MethodDeclaration> constructor = ProjectParser.parseFileToConstructorList(path);

        String methodName = "fibonacci";
        String className = "data.CloneFile";

        for (ASTNode func : funcAstNodeList) {
            if (((MethodDeclaration) func).getName().getIdentifier().equals(methodName)) {

                // Clone a runnable java file

                createCloneMethod((MethodDeclaration) func, compilationUnit);

                // Re-compile cloned file
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                int compilationResult = compiler.run(null, null, null, "src\\main\\java\\data\\CloneFile.java");
                if (compilationResult == 0) {
                    System.out.println("Compilation is successful.");
                } else {
                    System.out.println("Compilation failed.");
                }

                // Move .class file of clone file to target folder
                String sourceFilePath = "src\\main\\java\\data\\CloneFile.class";
                String destinationDirectoryPath = "target\\classes\\data\\";
                try {
                    File sourceFile = new File(sourceFilePath);
                    File destinationDirectory = new File(destinationDirectoryPath);

                    if (sourceFile.exists()) {
                        if (destinationDirectory.exists() && destinationDirectory.isDirectory()) {

                            // Move file
                            java.nio.file.Path sourcePath = sourceFile.toPath();
                            java.nio.file.Path destinationPath = new File(destinationDirectory, sourceFile.getName()).toPath();
                            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }

                ProcessBuilder builder = new ProcessBuilder("javac", "src\\main\\java\\data\\CloneFile.data");
                Process abc = builder.start();
                System.out.println(abc.waitFor());

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

                ASTHelper.generateCFG(block);
                CfgNode cfgNode = cfgBeginCfgNode;
                //===========================

                LocalDateTime beforeTime;
                LocalDateTime afterTime;
                double usedTime = 0;

                //=======================FULL CONCOLIC VERSION 1===========================

                List<ASTNode> parameters = ((MethodDeclaration) func).parameters();
                Class<?>[] parameterClasses = getParameterClasses(parameters);
                Method method = Class.forName(className).getDeclaredMethod(methodName, parameterClasses);

                beforeTime = LocalDateTime.now();

                Object[] parameterValue = createRandomTestData(parameterClasses);
                method.invoke(parameterClasses, parameterValue);
                MarkedPath.markPathToCFG(cfgNode);

                afterTime = LocalDateTime.now();
                usedTime += Math.abs((float) Duration.between(beforeTime, afterTime).toMillis());

                boolean isTestedSuccessfully = true;

                beforeTime = LocalDateTime.now();

                for (CfgNode uncoveredNode = MarkedPath.findUncoveredNode(cfgNode, null); uncoveredNode != null; ) {

                    afterTime = LocalDateTime.now();
                    usedTime += Math.abs((float) Duration.between(beforeTime, afterTime).toMillis());

                    beforeTime = LocalDateTime.now();

                    Path newPath = (new FindPath(cfgNode, uncoveredNode, cfgEndCfgNode)).getPath();

                    afterTime = LocalDateTime.now();
                    usedTime += Math.abs((float) Duration.between(beforeTime, afterTime).toMillis());

                    beforeTime = LocalDateTime.now();

                    SymbolicExecution solution = new SymbolicExecution(newPath, parameters);

                    if(solution.getModel() == null) {
                        isTestedSuccessfully = false;
                        break;
                    }

                    afterTime = LocalDateTime.now();
                    usedTime += Math.abs((float) Duration.between(beforeTime, afterTime).toMillis());

                    beforeTime = LocalDateTime.now();

                    method.invoke(parameterClasses, getParameterValue(parameterClasses));
                    MarkedPath.markPathToCFG(cfgNode);

                    afterTime = LocalDateTime.now();
                    usedTime += Math.abs((float) Duration.between(beforeTime, afterTime).toMillis());

                    beforeTime = LocalDateTime.now();

                    uncoveredNode = MarkedPath.findUncoveredNode(cfgNode, null);
                    System.out.println("Uncovered Node: " + uncoveredNode);
                }

                if(isTestedSuccessfully) System.out.println("Tested successfully with 100% coverage");
                else System.out.println("Test fail due to UNSATISFIABLE constraint");

                System.out.println("Total Concolic time: " + usedTime);
//                //========================================

                break;
            }
        }
    }

}
