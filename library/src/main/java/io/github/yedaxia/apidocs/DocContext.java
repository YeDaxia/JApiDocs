package io.github.yedaxia.apidocs;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.yedaxia.apidocs.parser.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * to judge project which framework is using and make some initialization
 *
 * @author yeguozhong yedaxia.github.com
 */
public class DocContext {

    private static String projectPath;
    private static String docPath;
    private static String javaSrcPath;
    private static AbsControllerParser controllerParser;
    private static List<File> controllerFiles;
    private static IResponseWrapper responseWrapper;
    private static Docs.DocsConfig config;

    public static void init(Docs.DocsConfig config){
        DocContext.config = config;
        setProjectPath(config.projectPath);
        setDocPath(config.docsPath);
        Resources.setUserCodeTplPath(config.codeTplPath);

        File logFile = getLogFile();
        if(logFile.exists()){
            logFile.delete();
        }

        //try to find javaSrcPath
        File projectDir = new File(projectPath);
        List<File> result = new ArrayList<>();
        Utils.wideSearchFile(projectDir, new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                if(name.endsWith(".java")){
                    Optional<PackageDeclaration> opPackageDeclaration = ParseUtils.compilationUnit(file).getPackageDeclaration();
                    if(opPackageDeclaration.isPresent()){
                        String packageName = opPackageDeclaration.get().getNameAsString();
                        if(Utils.hasDirInFile(file, projectDir, "test") && !packageName.contains("test")){
                            return false;
                        }else{
                            return true;
                        }
                    }
                    return !Utils.hasDirInFile(file, projectDir, "test");
                }
                return false;
            }
        }, result, true);

        if(result.isEmpty()){
            throw new RuntimeException("cannot find any java file in this project : " + projectPath);
        }

        File oneJavaFile = result.get(0);
        Optional<PackageDeclaration> opPackageDeclaration = ParseUtils.compilationUnit(oneJavaFile).getPackageDeclaration();
        String parentPath = oneJavaFile.getParentFile().getAbsolutePath();
        if(opPackageDeclaration.isPresent()){
            DocContext.javaSrcPath = parentPath.substring(0, parentPath.length() - opPackageDeclaration.get().getNameAsString().length());
        }else{
            DocContext.javaSrcPath = parentPath + "/";
        }

        result.clear();

        LogUtils.info("find java src path : %s", javaSrcPath);

        File javaSrcDir = new File(javaSrcPath);

        //which mvc framework
        ProjectType projectType = null;
        if(config.isSpringMvcProject()){
            projectType = ProjectType.SPRING;
        }else if(config.isJfinalProject()){
            projectType = ProjectType.JFINAL;
        }else if(config.isPlayProject()){
            projectType = ProjectType.PLAY;
        }else if(config.isGeneric()){
            projectType = ProjectType.GENERIC;
        }

        if(projectType == null){
            if(Utils.isPlayFramework(projectDir)){
                projectType = ProjectType.PLAY;
            }else if(Utils.isJFinalFramework(javaSrcDir)){
                projectType = ProjectType.JFINAL;
            }else if(Utils.isSpringFramework(javaSrcDir)){
                projectType = ProjectType.SPRING;
            }else{
                projectType = ProjectType.GENERIC;
            }
        }

        controllerFiles = new ArrayList<>();
        Set<String> controllerFileNames;

        switch (projectType){
            case PLAY:
                controllerParser = new PlayControllerParser();
                controllerFileNames = new LinkedHashSet<>();
                List<PlayRoutesParser.RouteNode> routeNodeList = PlayRoutesParser.INSTANCE.getRouteNodeList();

                for(PlayRoutesParser.RouteNode node : routeNodeList){
                    controllerFileNames.add(node.controllerFile);
                }

                for(String controllerFileName : controllerFileNames){
                    controllerFiles.add(new File(controllerFileName));
                }

                LogUtils.info("found it a play framework project, tell us if we are wrong.");
                break;
            case JFINAL:
                controllerParser = new JFinalControllerParser();
                controllerFileNames = new LinkedHashSet<>();
                List<JFinalRoutesParser.RouteNode> jFinalRouteNodeList = JFinalRoutesParser.INSTANCE.getRouteNodeList();

                for(JFinalRoutesParser.RouteNode node : jFinalRouteNodeList){
                    controllerFileNames.add(node.controllerFile);
                }

                for(String controllerFileName : controllerFileNames){
                    controllerFiles.add(new File(controllerFileName));
                }
                LogUtils.info("found it a jfinal project, tell us if we are wrong.");
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
                LogUtils.info("found it a spring mvc project, tell us if we are wrong.");
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
                LogUtils.info("it's a generic project.");
                break;
        }
    }

    /**
     * get log file path
     * @return
     */
    public static File getLogFile(){
        return new File(DocContext.getDocPath() , "apidoc.log");
    }

    /**
     * get project path
     */
    public static String getProjectPath() {
        return projectPath;
    }

    private static void setProjectPath(String projectPath) {
        DocContext.projectPath = new File(projectPath).getAbsolutePath() + "/";
    }

    /**
     * api docs output path
     * @return
     */
    public static String getDocPath() {
        return docPath;
    }

    private static void setDocPath(String docPath) {

        if(docPath == null || docPath.isEmpty()){
            docPath = projectPath + "apidocs";
        }

        File docDir = new File(docPath);
        if(!docDir.exists()){
            docDir.mkdirs();
        }
        DocContext.docPath = docPath;
    }

    /**
     * get java src path
     * @return
     */
    public static String getJavaSrcPath(){
        return javaSrcPath;
    }

    /**
     * get all controllers in this project
     * @return
     */
    public static File[] getControllerFiles(){
        return controllerFiles.toArray(new File[controllerFiles.size()]);
    }

    /**
     * get controller parser, it will return different parser by different framework you are using.
     * @return
     */
    public static AbsControllerParser controllerParser(){
        return controllerParser;
    }

    public static IResponseWrapper getResponseWrapper() {
        if(responseWrapper == null){
            responseWrapper = new IResponseWrapper() {
                @Override
                public Map<String,Object> wrapResponse(ResponseNode responseNode) {
                    Map<String,Object> resultMap = new HashMap<>();
                    resultMap.put("code", 0);
                    resultMap.put("data", responseNode);
                    resultMap.put("msg","success");
                    return resultMap;
                }
            };
        }
        return responseWrapper;
    }

    public static Docs.DocsConfig getDocsConfig(){
        return DocContext.config;
    }

    static void setResponseWrapper(IResponseWrapper responseWrapper) {
        DocContext.responseWrapper = responseWrapper;
    }
}
