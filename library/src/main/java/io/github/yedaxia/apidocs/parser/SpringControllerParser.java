package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * use for spring mvc
 *
 * @author yeguozhong yedaxia.github.com
 */
public class SpringControllerParser extends AbsControllerParser {

    private final static String[] MAPPING_ANNOTATIONS = {
            "GetMapping", "PostMapping", "PutMapping",
            "PatchMapping", "DeleteMapping", "RequestMapping"
    };

    @Override
    protected void beforeHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {
        clazz.getAnnotationByName("RequestMapping").ifPresent(a -> {
            if (a instanceof SingleMemberAnnotationExpr) {
                String baseUrl = ((SingleMemberAnnotationExpr) a).getMemberValue().toString();
                controllerNode.setBaseUrl(Utils.removeQuotations(baseUrl));
                return;
            }
            if (a instanceof NormalAnnotationExpr) {
                ((NormalAnnotationExpr) a).getPairs().stream()
                        .filter(v -> isUrlPathKey(v.getNameAsString()))
                        .findFirst()
                        .ifPresent(p -> {
                            controllerNode.setBaseUrl(Utils.removeQuotations(p.getValue().toString()));
                        });
            }
        });
    }

    @Override
    protected boolean shouldIgnoreMethod(MethodDeclaration m) {
        boolean ignore = super.shouldIgnoreMethod(m);
        if (ignore) {
            return true;
        }
        // 没有 Mapping 注解的忽略
        for (AnnotationExpr an : m.getAnnotations()) {
            for (String mappingAn : MAPPING_ANNOTATIONS) {
                if (mappingAn.contains(an.getNameAsString())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
        md.getAnnotations().forEach(an -> {
            String name = an.getNameAsString();
            if (Arrays.asList(MAPPING_ANNOTATIONS).contains(name)) {
                String method = Utils.getClassName(name).toUpperCase().replace("MAPPING", "");
                if (!"REQUEST".equals(method)) {
                    requestNode.addMethod(RequestMethod.valueOf(method).name());
                }

                if (an instanceof NormalAnnotationExpr) {
                    ((NormalAnnotationExpr) an).getPairs().forEach(p -> {
                        String key = p.getNameAsString();
                        if (isUrlPathKey(key)) {
                            requestNode.setUrl(Utils.removeQuotations(p.getValue().toString()));
                        }

                        if ("headers".equals(key)) {
                            Expression methodAttr = p.getValue();
                            if (methodAttr instanceof ArrayInitializerExpr) {
                                NodeList<Expression> values = ((ArrayInitializerExpr) methodAttr).getValues();
                                for (Node n : values) {
                                    String[] h = n.toString().split("=");
                                    requestNode.addHeaderNode(new HeaderNode(h[0], h[1]));
                                }
                            } else {
                                String[] h = p.getValue().toString().split("=");
                                requestNode.addHeaderNode(new HeaderNode(h[0], h[1]));
                            }
                        }

                        if ("method".equals(key)) {
                            Expression methodAttr = p.getValue();
                            if (methodAttr instanceof ArrayInitializerExpr) {
                                NodeList<Expression> values = ((ArrayInitializerExpr) methodAttr).getValues();
                                for (Node n : values) {
                                    requestNode.addMethod(RequestMethod.valueOf(Utils.getClassName(n.toString())).name());
                                }
                            } else {
                                requestNode.addMethod(RequestMethod.valueOf(Utils.getClassName(p.getValue().toString())).name());
                            }
                        }
                    });
                }

                if (an instanceof SingleMemberAnnotationExpr) {
                    String url = ((SingleMemberAnnotationExpr) an).getMemberValue().toString();
                    requestNode.setUrl(Utils.removeQuotations(url));
                }

                requestNode.setUrl(Utils.getActionUrl(getControllerNode().getBaseUrl(), requestNode.getUrl()));
            }
        });

        md.getParameters().forEach(p -> {
            String paraName = p.getName().asString();
            ParamNode paramNode = requestNode.getParamNodeByName(paraName);
            if (paramNode != null) {

                p.getAnnotations().forEach(an -> {
                    String name = an.getNameAsString();

                    // @NotNull, @NotBlank, @NotEmpty
                    if (ParseUtils.isNotNullAnnotation(name)) {
                        paramNode.setRequired(true);
                        return;
                    }

                    if (!"RequestParam".equals(name) && !"RequestBody".equals(name) && !"PathVariable".equals(name)) {
                        return;
                    }

                    if ("RequestBody".equals(name)) {
                        setRequestBody(paramNode, p.getType());
                    }

                    // @RequestParam String name
                    if (an instanceof MarkerAnnotationExpr) {
                        paramNode.setRequired(true);
                        return;
                    }

                    //  @RequestParam("email") String email
                    if (an instanceof SingleMemberAnnotationExpr) {
                        paramNode.setName(((StringLiteralExpr) ((SingleMemberAnnotationExpr) an).getMemberValue()).getValue());
                        return;
                    }

                    // @RequestParam(name = "email", required = true)
                    if (an instanceof NormalAnnotationExpr) {
                        ((NormalAnnotationExpr) an).getPairs().forEach(pair -> {
                            String exprName = pair.getNameAsString();
                            if ("required".equals(exprName)) {
                                Boolean exprValue = ((BooleanLiteralExpr) pair.getValue()).getValue();
                                paramNode.setRequired(Boolean.valueOf(exprValue));
                            } else if ("value".equals(exprName)) {
                                String exprValue = ((StringLiteralExpr) pair.getValue()).getValue();
                                paramNode.setName(exprValue);
                            }
                        });
                    }
                });

                //如果参数是个对象
                if (!paramNode.getJsonBody() && ParseUtils.isModelType(paramNode.getType())) {
                    ClassNode classNode = new ClassNode();
                    parseClassNodeByType(classNode, p.getType());
                    List<ParamNode> paramNodeList = new ArrayList<>();
                    toParamNodeList(paramNodeList, classNode, "");
                    requestNode.getParamNodes().remove(paramNode);
                    requestNode.getParamNodes().addAll(paramNodeList);
                }
            }
        });
    }

    @Override
    protected void handleResponseNode(ResponseNode responseNode, Type resultType) {
        if (resultType instanceof ClassOrInterfaceType) {
            String className = ((ClassOrInterfaceType) resultType).getName().getIdentifier();
            if ("org.springframework.http.ResponseEntity".endsWith(className)) {
                Optional<NodeList<Type>> nodeListOptional = ((ClassOrInterfaceType) resultType).getTypeArguments();
                if (nodeListOptional.isPresent()) {
                    NodeList<Type> typeNodeList = nodeListOptional.get();
                    if (!typeNodeList.isEmpty()) {
                        resultType = typeNodeList.get(0).getElementType();
                    }
                } else {
                    responseNode.setClassName(className);
                    return;
                }
            }
        }
        super.handleResponseNode(responseNode, resultType);
    }

    private void setRequestBody(ParamNode paramNode, Type paramType) {
        if (ParseUtils.isModelType(paramType.asString())) {
            ClassNode classNode = new ClassNode();
            parseClassNodeByType(classNode, paramType);
            paramNode.setJsonBody(true);
            classNode.setShowFieldNotNull(Boolean.TRUE);
            paramNode.setDescription(classNode.toJsonApi());
        }
    }

    private void toParamNodeList(List<ParamNode> paramNodeList, ClassNode formNode, String parentName) {
        formNode.getChildNodes().forEach(filedNode -> {
            if (filedNode.getChildNode() != null) {
                toParamNodeList(paramNodeList, filedNode.getChildNode(), filedNode.getName() + ".");
            } else {
                ParamNode paramNode = new ParamNode();
                paramNode.setName(parentName + filedNode.getName());
                paramNode.setType(filedNode.getType());
                paramNode.setDescription(filedNode.getDescription());
                paramNode.setRequired(filedNode.getNotNull());
                paramNodeList.add(paramNode);
            }
        });
    }

    private boolean isUrlPathKey(String name) {
        return name.equals("path") || name.equals("value");
    }
}
