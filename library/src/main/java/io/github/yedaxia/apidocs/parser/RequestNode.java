package io.github.yedaxia.apidocs.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * a request node  corresponds to a controller method
 *
 * @author yeguozhong yedaxia.github.com
 */
public class RequestNode {

    private List<String> method = new ArrayList<>();
    private String url;
    private String description;
    private List<ParamNode> paramNodes = new ArrayList<>();
    private List<HeaderNode> header = new ArrayList<>();
    private Boolean deprecated = Boolean.FALSE;
    private ResponseNode responseNode;

    public List<String> getMethod() {
        return method;
    }

    public void setMethod(List<String> method) {
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

    public void setParamNodes(List<ParamNode> paramNodes) {
        this.paramNodes = paramNodes;
    }

    public List<HeaderNode> getHeader() {
        return header;
    }

    public void setHeader(List<HeaderNode> header) {
        this.header = header;
    }

    public Boolean getDeprecated() {
        return deprecated;
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

    public void addMethod(String method) {
        this.method.add(method);
    }

    public void addHeaderNode(HeaderNode headerNode){
        header.add(headerNode);
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
}
