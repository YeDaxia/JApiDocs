package io.github.yedaxia.apidocs.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * a request node  corresponds to a controller method
 *
 * @author yeguozhong yedaxia.github.com
 */
public class RequestNode {

    private String method;
    private String url;
    private String description;
    private List<ParamNode> paramNodes = new ArrayList<>();
    private Boolean deprecated;
    private ResponseNode responseNode;

    public String getMethod() {
        return method == null ? "get|post" : method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParamNode> getParamNodes() {
        return paramNodes;
    }

    public void addParamNode(ParamNode paramNode){
        paramNodes.add(paramNode);
    }

    public ParamNode getParamNodeByName(String name){
        for(ParamNode node : paramNodes){
            if(node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }

    public Boolean getDeprecated() {
        return deprecated == null ? Boolean.FALSE : deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public ResponseNode getResponseNode() {
        return responseNode;
    }

    public void setResponseNode(ResponseNode responseNode) {
        this.responseNode = responseNode;
    }
}
