package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.LogUtils;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.Utils;

import javax.print.Doc;
import javax.swing.plaf.ListUI;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * JFinal routes parser
 *
 * @author yeguozhong yedaxia.github.com
 */
public class JFinalRoutesParser {

    private File jfinalConfigFile;
    private MethodDeclaration mdConfigRoute;
    private List<RouteNode> routeNodeList = new ArrayList<>();

    public static final JFinalRoutesParser INSTANCE = new JFinalRoutesParser();

    private JFinalRoutesParser(){
        List<File> result = new ArrayList<>();

        for(String javaSrcPath : DocContext.getJavaSrcPaths()){
            Utils.wideSearchFile(new File(javaSrcPath), new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return ParseUtils.compilationUnit(f).getChildNodesByType(ClassOrInterfaceDeclaration.class)
                            .stream()
                            .anyMatch(cl -> cl.getMethodsByName("configRoute")
                                    .stream()
                                    .anyMatch(m -> {
                                        mdConfigRoute = m;
                                        return m.getParameters()
                                                .stream()
                                                .anyMatch(p -> p.getType().asString().endsWith("Routes"));
                                    }));
                }
            },result, true);

            if(!result.isEmpty()){
                break;
            }
        }


        if(result.isEmpty()){
            throw new RuntimeException("cannot find JFinalConfig File");
        }

        jfinalConfigFile = result.get(0);

        LogUtils.info("Jfinal config file path : %s", jfinalConfigFile.getAbsolutePath());

        parse(mdConfigRoute, jfinalConfigFile);
    }

    private void parse(MethodDeclaration mdConfigRoute, File inJavaFile){
        mdConfigRoute.getBody()
                .ifPresent(blockStmt -> blockStmt.getStatements()
                        .stream()
                        .filter(statement -> statement instanceof ExpressionStmt)
                        .forEach(statement -> {
                           Expression expression = ((ExpressionStmt)statement).getExpression();
                           if(expression instanceof MethodCallExpr && ((MethodCallExpr)expression).getNameAsString().equals("add")){
                               NodeList<Expression> arguments = ((MethodCallExpr)expression).getArguments();
                               if(arguments.size() == 1 && arguments.get(0) instanceof ObjectCreationExpr){
                                   String routeClassName = ((ObjectCreationExpr)((MethodCallExpr) expression).getArguments().get(0)).getType().getNameAsString();
                                   File childRouteFile = ParseUtils.searchJavaFile(inJavaFile, routeClassName);
                                   LogUtils.info("found child routes in file : %s" , childRouteFile.getName());
                                   ParseUtils.compilationUnit(childRouteFile)
                                           .getChildNodesByType(ClassOrInterfaceDeclaration.class)
                                           .stream()
                                           .filter(cd -> routeClassName.endsWith(cd.getNameAsString()))
                                           .findFirst()
                                           .ifPresent(cd ->{
                                               LogUtils.info("found config() method, start to parse child routes in file : %s" , childRouteFile.getName());
                                               cd.getMethodsByName("config").stream().findFirst().ifPresent(m ->{
                                                   parse(m, childRouteFile);
                                               });
                                           });
                               }else{
                                   String basicUrl = Utils.removeQuotations(arguments.get(0).toString());
                                   String controllerClass = arguments.get(1).toString();
                                   String controllerFilePath = getControllerFilePath(inJavaFile, controllerClass);
                                   routeNodeList.add(new RouteNode(basicUrl, controllerFilePath));
                               }
                           }
        }));
    }

    public List<RouteNode> getRouteNodeList() {
        return routeNodeList;
    }

    public RouteNode getRouteNode(String controllerFile){
        for(RouteNode routeNode : routeNodeList){
            if(routeNode.controllerFile.equals(controllerFile)){
                return routeNode;
            }
        }
        return null;
    }

    private String getControllerFilePath(File inJavaFile, String controllerClass){
        return ParseUtils.searchJavaFile(inJavaFile,  controllerClass.substring(0, controllerClass.indexOf("."))).getAbsolutePath();
    }

    public static class RouteNode {

        public final String basicUrl;
        public final String controllerFile;

        public RouteNode(String basicUrl, String controllerFile) {
            this.basicUrl = basicUrl;
            this.controllerFile = controllerFile;
        }
    }

}
