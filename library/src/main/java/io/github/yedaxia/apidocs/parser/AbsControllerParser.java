package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.javadoc.JavadocBlockTag;
import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.Utils;

import java.io.File;
import java.util.List;

/**
 * parse Controller Java the common part, get all request nodes
 *
 * @author yeguozhong yedaxia.github.com
 */
public abstract class AbsControllerParser {

    private CompilationUnit compilationUnit;
    private ControllerNode controllerNode;
    private File javaFile;

    public ControllerNode parse(File javaFile) {

        this.javaFile = javaFile;
        this.compilationUnit = ParseUtils.compilationUnit(javaFile);
        this.controllerNode = new ControllerNode();

        String controllerName = Utils.getJavaFileName(javaFile);
        controllerNode.setClassName(controllerName);
        compilationUnit.getClassByName(controllerName)
                .ifPresent(c -> {
                    beforeHandleController(controllerNode, c);
                    parseClassDoc(c);
                    parseMethodDocs(c);
                    afterHandleController(controllerNode, c);
                });

        return controllerNode;
    }

    File getControllerFile() {
        return javaFile;
    }

    ControllerNode getControllerNode(){
        return controllerNode;
    }

    private void parseClassDoc(ClassOrInterfaceDeclaration c) {

        c.getParentNode().get().findFirst(PackageDeclaration.class).ifPresent(pd -> {
            controllerNode.setPackageName(pd.getNameAsString());
        });

        boolean generateDocs = c.getAnnotationByName("ApiDoc").isPresent();
        controllerNode.setGenerateDocs(generateDocs);

        c.getJavadoc().ifPresent(d -> {
            String description = d.getDescription().toText();
            controllerNode.setDescription(Utils.isNotEmpty(description) ? description : c.getNameAsString());
            List<JavadocBlockTag> blockTags = d.getBlockTags();
            if (blockTags != null) {
                for (JavadocBlockTag blockTag : blockTags) {
                    if ("author".equalsIgnoreCase(blockTag.getTagName())) {
                        controllerNode.setAuthor(blockTag.getContent().toText());
                    }
                    if("description".equalsIgnoreCase(blockTag.getTagName())){
                        controllerNode.setDescription(blockTag.getContent().toText());
                    }
                }
            }
        });

        if (controllerNode.getDescription() == null) {
            controllerNode.setDescription(c.getNameAsString());
        }
    }

    private void parseMethodDocs(ClassOrInterfaceDeclaration c) {
        c.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.getModifiers().contains(Modifier.PUBLIC))
                .forEach(m -> {

                    boolean existsApiDoc = m.getAnnotationByName("ApiDoc").isPresent();
                    if(!existsApiDoc && !controllerNode.getGenerateDocs() && !DocContext.getDocsConfig().getAutoGenerate()){
                        return;
                    }

                    RequestNode requestNode = new RequestNode();
                    requestNode.setControllerNode(controllerNode);
                    requestNode.setMethodName(m.getNameAsString());
                    m.getAnnotationByClass(Deprecated.class).ifPresent(f -> {
                        requestNode.setDeprecated(true);
                    });

                    m.getJavadoc().ifPresent(d -> {
                        String description = d.getDescription().toText();
                        requestNode.setDescription(description);
                        d.getBlockTags().stream()
                                .filter(t -> t.getTagName().equals("param"))
                                .forEach(t -> {
                                    ParamNode paramNode = new ParamNode();
                                    paramNode.setName(t.getName().get());
                                    paramNode.setDescription(t.getContent().toText());
                                    requestNode.addParamNode(paramNode);
                                });
                    });

                    m.getParameters().forEach(p -> {
                        String paraName = p.getName().asString();
                        ParamNode paramNode = requestNode.getParamNodeByName(paraName);
                        if (paramNode != null) {
                            paramNode.setType(ParseUtils.unifyType(p.getType().asString()));
                        }
                    });

                    com.github.javaparser.ast.type.Type resultClassType = null;
                    if(existsApiDoc){
                        AnnotationExpr an = m.getAnnotationByName("ApiDoc").get();
                        if (an instanceof SingleMemberAnnotationExpr) {
                            resultClassType = ((ClassExpr) ((SingleMemberAnnotationExpr) an).getMemberValue()).getType();
                        } else if (an instanceof NormalAnnotationExpr) {
                            for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
                                final String pairName = pair.getNameAsString();
                                if ("result".equals(pairName) || "value".equals(pairName)) {
                                    resultClassType = ((ClassExpr) pair.getValue()).getType();
                                } else if (pairName.equals("url")) {
                                    requestNode.setUrl(((StringLiteralExpr) pair.getValue()).getValue());
                                } else if (pairName.equals("method")) {
                                    requestNode.addMethod(((StringLiteralExpr) pair.getValue()).getValue());
                                }
                            }
                        }
                    }

                    afterHandleMethod(requestNode, m);

                    if (resultClassType == null) {
                        if (m.getType() == null) {
                            return;
                        }
                        resultClassType = m.getType();
                    }

                    ResponseNode responseNode = new ResponseNode();
                    responseNode.setRequestNode(requestNode);
                    ParseUtils.parseClassNodeByType(javaFile, responseNode, resultClassType.getElementType());
                    requestNode.setResponseNode(responseNode);
                    controllerNode.addRequestNode(requestNode);

                });
    }

    /**
     * called before controller node has handled
     *
     * @param clazz
     */
    protected void beforeHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {
    }

    /**
     * called after controller node has handled
     *
     * @param clazz
     */
    protected void afterHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {
    }

    /**
     * called after request method node has handled
     */
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
    }
}
