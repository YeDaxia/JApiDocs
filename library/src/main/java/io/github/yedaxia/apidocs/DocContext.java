package io.github.yedaxia.apidocs;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.yedaxia.apidocs.parser.*;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.util.*;

/**
 * to judge project which framework is using and make some initialization
 *
 * @author yeguozhong yedaxia.github.com
 */
public class DocContext {

    private static String projectPath;
    private static String docPath;
    //multi modules
    private static List<String> javaSrcPaths = new ArrayList<>();
    private static AbsControllerParser controllerParser;
    private static List<File> controllerFiles;
    private static IResponseWrapper responseWrapper;
    private static DocsConfig config;

    public static void init(DocsConfig config) {
        DocContext.config = config;
        setProjectPath(config.projectPath);
        setDocPath(config.docsPath);
        Resources.setUserCodeTplPath(config.codeTplPath);

        if(config.projectPath == null || !new File(config.projectPath).exists()){
            LogUtils.error("projectDir doesn't exists. %s", projectPath);
            return;
        }

        File logFile = getLogFile();
        if (logFile.exists()) {
            logFile.delete();
        }

        if (config.getJavaSrcPaths().isEmpty()) {
            findOutJavaSrcPaths();
        } else {
            javaSrcPaths.addAll(config.getJavaSrcPaths());
        }

        LogUtils.info("find java src paths:  %s", javaSrcPaths);

        ProjectType projectType = findOutProjectType();
        findOutControllers(projectType);
    }

    private static void findOutJavaSrcPaths() {
        //try to find javaSrcPaths
        File projectDir = new File(projectPath);

        List<String> moduleNames = Utils.getModuleNames(projectDir);
        if (moduleNames != null && !moduleNames.isEmpty()) {
            for(String moduleName : moduleNames){
                String javaSrcPath = findModuleSrcPath(new File(projectDir, moduleName));
                javaSrcPaths.add(javaSrcPath);
            }
        }

        // is it a simple java project?
        if(javaSrcPaths.isEmpty()){
            String javaSrcPath = findModuleSrcPath(projectDir);
            javaSrcPaths.add(javaSrcPath);
        }
    }

    private static ProjectType findOutProjectType() {

        //which mvc framework
        ProjectType projectType = null;

        if (config.isSpringMvcProject()) {
            projectType = ProjectType.SPRING;
        } else if (config.isJfinalProject()) {
            projectType = ProjectType.JFINAL;
        } else if (config.isPlayProject()) {
            projectType = ProjectType.PLAY;
        } else if (config.isGeneric()) {
            projectType = ProjectType.GENERIC;
        }

        if (projectType == null) {
            LogUtils.info("project type not set, try to figure out...");
            for (String javaSrcPath : javaSrcPaths) {
                File javaSrcDir = new File(javaSrcPath);
                if (Utils.isSpringFramework(javaSrcDir)) {
                    projectType = ProjectType.SPRING;
                } else if (Utils.isPlayFramework(new File(getProjectPath()))) {
                    projectType = ProjectType.PLAY;
                } else if (Utils.isJFinalFramework(javaSrcDir)) {
                    projectType = ProjectType.JFINAL;
                }

                if (projectType != null) {
                    return projectType;
                }
            }
        }

        projectType = projectType != null ? projectType : ProjectType.GENERIC;

        LogUtils.info("found it a %s project, tell us if we are wrong.", projectType);

        return projectType;
    }

    private static void findOutControllers(ProjectType projectType) {
        controllerFiles = new ArrayList<>();
        Set<String> controllerFileNames;

        for (String javaSrcPath : getJavaSrcPaths()) {
            LogUtils.info("start find controllers in path : %s", javaSrcPath);
            File javaSrcDir = new File(javaSrcPath);
            List<File> result = new ArrayList<>();
            switch (projectType) {
                case PLAY:
                    controllerParser = new PlayControllerParser();
                    controllerFileNames = new LinkedHashSet<>();
                    List<PlayRoutesParser.RouteNode> routeNodeList = PlayRoutesParser.INSTANCE.getRouteNodeList();

                    for (PlayRoutesParser.RouteNode node : routeNodeList) {
                        controllerFileNames.add(node.controllerFile);
                    }

                    for (String controllerFileName : controllerFileNames) {
                        controllerFiles.add(new File(controllerFileName));
                    }

                    break;
                case JFINAL:
                    controllerParser = new JFinalControllerParser();
                    controllerFileNames = new LinkedHashSet<>();
                    List<JFinalRoutesParser.RouteNode> jFinalRouteNodeList = JFinalRoutesParser.INSTANCE.getRouteNodeList();

                    for (JFinalRoutesParser.RouteNode node : jFinalRouteNodeList) {
                        controllerFileNames.add(node.controllerFile);
                    }

                    for (String controllerFileName : controllerFileNames) {
                        controllerFiles.add(new File(controllerFileName));
                    }
                    break;
                case SPRING:
                    controllerParser = new SpringControllerParser();
                    Utils.wideSearchFile(javaSrcDir, new FilenameFilter() {
                        @Override
                        public boolean accept(File f, String name) {
                            return f.getName().endsWith(".java") && ParseUtils.compilationUnit(f)
                                    .getChildNodesByType(ClassOrInterfaceDeclaration.class)
                                    .stream()
                                    .anyMatch(cd -> cd.getAnnotationByName("Controller").isPresent() || cd.getAnnotationByName("RestController").isPresent());
                        }
                    }, result, false);
                    controllerFiles.addAll(result);
                    break;
                default:
                    controllerParser = new GenericControllerParser();
                    Utils.wideSearchFile(javaSrcDir, new FilenameFilter() {
                        @Override
                        public boolean accept(File f, String name) {
                            return f.getName().endsWith(".java") && ParseUtils.compilationUnit(f)
                                    .getChildNodesByType(ClassOrInterfaceDeclaration.class)
                                    .stream()
                                    .anyMatch(cd -> {
                                        return cd.getChildNodesByType(MethodDeclaration.class)
                                                .stream()
                                                .anyMatch(md -> md.getAnnotationByName("ApiDoc").isPresent());
                                    });
                        }
                    }, result, false);
                    controllerFiles.addAll(result);
                    break;
            }
            for (File controllerFile : result) {
                LogUtils.info("find controller file : %s", controllerFile.getName());
            }
        }
    }

    private static String findModuleSrcPath(File moduleDir){

        List<File> result = new ArrayList<>();
        Utils.wideSearchFile(moduleDir, new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                if (name.endsWith(".java")) {
                    Optional<PackageDeclaration> opPackageDeclaration = ParseUtils.compilationUnit(file).getPackageDeclaration();
                    if (opPackageDeclaration.isPresent()) {
                        String packageName = opPackageDeclaration.get().getNameAsString();
                        if (Utils.hasDirInFile(file, moduleDir, "test") && !packageName.contains("test")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return !Utils.hasDirInFile(file, moduleDir, "test");
                }
                return false;
            }
        }, result, true);

        if (result.isEmpty()) {
            throw new RuntimeException("cannot find any java file in this module : " + moduleDir.getName());
        }

        File oneJavaFile = result.get(0);
        Optional<PackageDeclaration> opPackageDeclaration = ParseUtils.compilationUnit(oneJavaFile).getPackageDeclaration();
        String parentPath = oneJavaFile.getParentFile().getAbsolutePath();
        if(opPackageDeclaration.isPresent()){
            return parentPath.substring(0, parentPath.length() - opPackageDeclaration.get().getNameAsString().length());
        }else{
            return parentPath + "/";
        }
    }

    /**
     * get log file path
     *
     * @return
     */
    public static File getLogFile() {
        return new File(DocContext.getDocPath(), "apidoc.log");
    }

    /**
     * get project path
     */
    public static String getProjectPath() {
        return projectPath;
    }

    private static void setProjectPath(String projectPath) {
        if(projectPath != null){
            DocContext.projectPath = new File(projectPath).getAbsolutePath() + "/";
        }
    }

    /**
     * api docs output path
     *
     * @return
     */
    public static String getDocPath() {
        return docPath;
    }

    private static void setDocPath(String docPath) {

        if (docPath == null || docPath.isEmpty()) {
            docPath = projectPath + "apidocs";
        }

        File docDir = new File(docPath);
        if (!docDir.exists()) {
            docDir.mkdirs();
        }
        DocContext.docPath = docPath;
    }

    /**
     * get java src paths
     *
     * @return
     */
    public static List<String> getJavaSrcPaths() {
        return javaSrcPaths;
    }


    /**
     * get all controllers in this project
     *
     * @return
     */
    public static File[] getControllerFiles() {
        return controllerFiles.toArray(new File[controllerFiles.size()]);
    }

    /**
     * get controller parser, it will return different parser by different framework you are using.
     *
     * @return
     */
    public static AbsControllerParser controllerParser() {
        return controllerParser;
    }

    public static IResponseWrapper getResponseWrapper() {
        if (responseWrapper == null) {
            responseWrapper = new IResponseWrapper() {
                @Override
                public Map<String, Object> wrapResponse(ResponseNode responseNode) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("code", 0);
                    resultMap.put("data", responseNode);
                    resultMap.put("msg", "success");
                    return resultMap;
                }
            };
        }
        return responseWrapper;
    }

    public static DocsConfig getDocsConfig() {
        return DocContext.config;
    }

    static void setResponseWrapper(IResponseWrapper responseWrapper) {
        DocContext.responseWrapper = responseWrapper;
    }
}
