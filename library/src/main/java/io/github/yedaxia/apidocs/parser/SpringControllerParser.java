package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.Utils;

import java.util.Arrays;

/**
 * use for spring mvc
 *
 * @author yeguozhong yedaxia.github.com
 */
public class SpringControllerParser extends AbsControllerParser {

    private final static String[] mappingAnnotations = {
            "GetMapping", "PostMapping", "PutMapping",
            "PatchMapping", "DeleteMapping", "RequestMapping"
    };

    @Override
    protected void afterHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {
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
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
        md.getAnnotations().forEach(an -> {
            String name = an.getNameAsString();
            if (Arrays.asList(mappingAnnotations).contains(name)) {
                name = name.toUpperCase().replace("MAPPING", "");
                if (!"REQUEST".equals(name)) {
                    requestNode.addMethod(RequestMethod.valueOf(name).name());
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
                                    requestNode.addMethod(RequestMethod.valueOf(n.toString().replace("RequestMethod.", "")).name());
                                }
                            } else {
                                requestNode.addMethod(RequestMethod.valueOf(p.getValue().toString().replace("RequestMethod.", "")).name());
                            }
                        }
                    });
                }
                if (an instanceof SingleMemberAnnotationExpr) {
                    String url = ((SingleMemberAnnotationExpr) an).getMemberValue().toString();
                    requestNode.setUrl(Utils.removeQuotations(url));
                    return;
                }

            }
        });

        md.getParameters().forEach(p -> {
            String paraName = p.getName().asString();
            ParamNode paramNode = requestNode.getParamNodeByName(paraName);
            if (paramNode != null) {

                p.getAnnotations().forEach(an -> {
                    String name = an.getNameAsString();
                    if (!"RequestParam".equals(name) && !"RequestBody".equals(name)) {
                        return;
                    }

                    if ("RequestBody".equals(name)) {
                        String type = p.getType().asString();
                        setRequestBody(paramNode, type);
                    }

                    if (an instanceof MarkerAnnotationExpr) {
                        paramNode.setRequired(true);
                        return;
                    }

                    if (an instanceof NormalAnnotationExpr) {
                        ((NormalAnnotationExpr) an).getPairs().stream()
                                .filter(n -> n.getNameAsString().equals("required"))
                                .findFirst()
                                .ifPresent(v -> {
                                    paramNode.setRequired(Boolean.valueOf(v.getValue().toString()));
                                });
                    }

                });
            }
        });
    }

    private void setRequestBody(ParamNode paramNode, String rawType) {
        String modelType;
        boolean isList;
        if (rawType.endsWith("[]")) {
            isList = true;
            modelType = rawType.replace("[]", "");
        } else if (ParseUtils.isCollectionType(rawType)) {
            isList = true;
            modelType = rawType.substring(rawType.indexOf("<") + 1, rawType.length() - 1);
        } else {
            isList = false;
            modelType = rawType;
        }

        if (ParseUtils.isModelType(modelType)) {
            ClassNode classNode = new ClassNode();
            classNode.setClassName(modelType);
            classNode.setList(isList);
            ParseUtils.parseResponseNode(ParseUtils.searchJavaFile(getControllerFile(), modelType), classNode);
            paramNode.setJsonBody(true);
            paramNode.setDescription(classNode.toJsonApi());
        }
    }

    private boolean isUrlPathKey(String name) {
        return name.equals("path") || name.equals("value");
    }
}
