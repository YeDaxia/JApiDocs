package io.github.yedaxia.apidocs.parser;

/**
 * a field node corresponds to a response field
 * @author yeguozhong yedaxia.github.com
 */
public class FieldNode {

    private String name;
    private String type;
    private String description;
    private MockNode mockNode;
    private ClassNode childNode; // 表示该field持有的对象类
    private ClassNode classNode; // 该field所在的类
    private Boolean loopNode = Boolean.FALSE; // 有循环引用的类
    private Boolean notNull = Boolean.FALSE;
    public Boolean getLoopNode() {
        return loopNode;
    }

    public void setLoopNode(Boolean loopNode) {
        this.loopNode = loopNode;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

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

    public MockNode getMockNode() {
        return mockNode;
    }

    public void setMockNode(MockNode mockNode) {
        this.mockNode = mockNode;
    }

    public ClassNode getChildNode(){
        return childNode;
    }

    public void setChildNode(ClassNode childNode) {
        this.childNode = childNode;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public void setClassNode(ClassNode classNode) {
        this.classNode = classNode;
    }

}
