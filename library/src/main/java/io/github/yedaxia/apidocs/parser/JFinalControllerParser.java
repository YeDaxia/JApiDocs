package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import io.github.yedaxia.apidocs.Utils;

/**
 *
 * use for JFinal
 *
 * @author yeguozhong yedaxia.github.com
 */
public class JFinalControllerParser extends AbsControllerParser{

    @Override
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
        String methodName = md.getNameAsString();
        requestNode.setUrl(getUrl(methodName));
        md.getAnnotationByName("ActionKey").ifPresent(an -> {
            if(an instanceof SingleMemberAnnotationExpr){
                String url = ((SingleMemberAnnotationExpr)an).getMemberValue().toString();
                requestNode.setUrl(Utils.cleanUrl(url));
            }
        });
    }

    private String getUrl(String methodName){
        JFinalRoutesParser.RouteNode routeNode = JFinalRoutesParser.INSTANCE.getRouteNode(getControllerFile().getAbsolutePath());
        return routeNode == null ? "" :routeNode.basicUrl +"/"+ methodName;
    }
}
