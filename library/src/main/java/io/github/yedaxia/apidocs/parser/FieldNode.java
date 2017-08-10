package io.github.yedaxia.apidocs.parser;

/**
 * a field node corresponds to a response field
 * @author yeguozhong yedaxia.github.com
 */
public class FieldNode {

    private String name;
    private String type;
    private String description;
    private ResponseNode childResponseNode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResponseNode getChildResponseNode() {
        return childResponseNode;
    }

    public void setChildResponseNode(ResponseNode childResponseNode) {
        this.childResponseNode = childResponseNode;
    }
}
