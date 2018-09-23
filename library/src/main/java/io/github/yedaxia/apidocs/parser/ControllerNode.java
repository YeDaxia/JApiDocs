package io.github.yedaxia.apidocs.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * a controller node corresponds to a controller file
 *
 * @author yeguozhong yedaxia.github.com
 */
public class ControllerNode {

    private String author;
    private String description;
    private String baseUrl;
    private String className;
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private List<RequestNode> requestNodes = new ArrayList<>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseUrl() {
        return baseUrl == null ? "" : baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<RequestNode> getRequestNodes() {
        return requestNodes;
    }

    public void setRequestNodes(List<RequestNode> requestNodes) {
        this.requestNodes = requestNodes;
    }

    public void addRequestNode(RequestNode requestNode){
        requestNodes.add(requestNode);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
