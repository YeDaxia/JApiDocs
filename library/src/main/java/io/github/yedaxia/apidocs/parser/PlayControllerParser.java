package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * user for play framework
 *
 * @author yeguozhong yedaxia.github.com
 */
public class PlayControllerParser extends AbsControllerParser {

    @Override
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
        PlayRoutesParser.RouteNode routeNode = PlayRoutesParser.INSTANCE.getRouteNode(getControllerFile(), md.getNameAsString());
        if(routeNode == null){
            return;
        }

        requestNode.setMethod(routeNode.method.contains("POST")? RequestMethod.POST : RequestMethod.GET);
        requestNode.setUrl(routeNode.routeUrl);

        md.getParameters().forEach(p -> {
            String paraName  = p.getName().asString();
            ParamNode paramNode = requestNode.getParamNodeByName(paraName);
            if(paramNode != null){
                p.getAnnotationByName("Required").ifPresent(r -> {
                    paramNode.setRequired(true);
                });
            }
        });
    }

}
