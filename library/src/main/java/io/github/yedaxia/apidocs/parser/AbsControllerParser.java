package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.javadoc.JavadocBlockTag;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.Utils;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * parse Controller Java the common part, get all request nodes
 *
 * @author yeguozhong yedaxia.github.com
 */
public abstract class AbsControllerParser {

    private CompilationUnit compilationUnit;
    private ControllerNode controllerNode;
    private File javaFile;

    public ControllerNode parse(File javaFile){

        this.javaFile = javaFile;
        this.compilationUnit = ParseUtils.compilationUnit(javaFile);
        this.controllerNode = new ControllerNode();

        String controllerName = Utils.getJavaFileName(javaFile);
        compilationUnit.getClassByName(controllerName)
                .ifPresent(c -> {
                    parseClassDoc(c);
                    parseMethodDocs(c);
                    afterHandleController(controllerNode, c);
                });

        return controllerNode;
    }

    protected File getControllerFile(){
        return javaFile;
    }

    private void parseClassDoc(ClassOrInterfaceDeclaration c){
        c.getJavadoc().ifPresent( d -> {
            String description = d.getDescription().toText();
            controllerNode.setDescription(Utils.isNotEmpty(description)? description: c.getNameAsString());
            List<JavadocBlockTag> blockTags = d.getBlockTags();
            if(blockTags != null){
                for(JavadocBlockTag blockTag : blockTags){
                    if("author".equalsIgnoreCase(blockTag.getTagName())){
                        controllerNode.setAuthor(blockTag.getContent().toText());
                    }
                }
            }
        });

        if(controllerNode.getDescription() == null){
            controllerNode.setDescription(c.getNameAsString());
        }

    }

    private void parseMethodDocs(ClassOrInterfaceDeclaration c){
        c.getChildNodesByType(MethodDeclaration.class).stream()
                .filter(m -> m.getModifiers().contains(Modifier.PUBLIC) && m.getAnnotationByName("ApiDoc").isPresent())
                .forEach(m -> {
                    m.getAnnotationByName("ApiDoc").ifPresent(an -> {
                        RequestNode requestNode = new RequestNode();
                        m.getAnnotationByClass(Deprecated.class).ifPresent(f -> {requestNode.setDeprecated(true);});
                        m.getJavadoc().ifPresent( d -> {
                            String description = d.getDescription().toText();
                            requestNode.setDescription(description);
                            d.getBlockTags().stream()
                                    .filter(t -> t.getTagName().equals("param"))
                                    .forEach(t ->{
                                        ParamNode paramNode = new ParamNode();
                                        paramNode.setName(t.getName().get());
                                        paramNode.setDescription(t.getContent().toText());
                                        requestNode.addParamNode(paramNode);
                                    });
                        });

                        m.getParameters().forEach(p -> {
                            String paraName  = p.getName().asString();
                            ParamNode paramNode = requestNode.getParamNodeByName(paraName);
                            if(paramNode != null){
                                paramNode.setType(ParseUtils.unifyType(p.getType().asString()));
                            }
                        });

                        afterHandleMethod(requestNode, m);

                        com.github.javaparser.ast.type.Type resultClassType = null;
                        if(an instanceof SingleMemberAnnotationExpr){
                            resultClassType = ((ClassExpr) ((SingleMemberAnnotationExpr) an).getMemberValue()).getType();
                        }else if(an instanceof NormalAnnotationExpr){
                            Optional<MemberValuePair> opPair = ((NormalAnnotationExpr)an)
                                    .getPairs().stream()
                                    .filter(rs -> rs.getNameAsString().equals("result"))
                                    .findFirst();
                            if(opPair.isPresent()){
                                resultClassType = ((ClassExpr) opPair.get().getValue()).getType();
                            }
                        }

                        if(resultClassType == null){
                            if(m.getType() == null){
                                return;
                            }
                            resultClassType = m.getType();
                        }

                        ResponseNode responseNode = new ResponseNode();
                        ParseUtils.parseClassNodeByType(javaFile, responseNode, resultClassType.getElementType());
                        requestNode.setResponseNode(responseNode);
                        controllerNode.addRequestNode(requestNode);
                    });
                });
    }

    /**
     * called after controller node has handled
     * @param clazz
     */
    protected void afterHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz){}

    /**
     * called after request method node has handled
     */
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md){}
}
