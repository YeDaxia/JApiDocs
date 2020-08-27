package io.github.yedaxia.apidocs.parser;

import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * class node
 *
 * @author yeguozhong yedaxia.github.com
 */
public class ClassNode {

    private String className = "";
    private Class modelClass; //for reflection
    private String description;
    private Boolean isList = Boolean.FALSE;
    private List<FieldNode> childNodes = new ArrayList<>();
    private List<GenericNode> genericNodes = new ArrayList<>();

    /**
     * class ParentNode{ //parentNode;
     *    ClassNode node;
     * }
     */
    private ClassNode parentNode; //包含了该类节点的节点

    private String classFileName;

    private Boolean showFieldNotNull = Boolean.FALSE;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isList() {
        return isList;
    }

    public void setList(Boolean list) {
        isList = list;
    }

    public List<FieldNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<FieldNode> childNodes) {
        this.childNodes = childNodes;
    }

    public void addChildNode(FieldNode fieldNode){
        childNodes.add(fieldNode);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<GenericNode> getGenericNodes() {
        return genericNodes;
    }

    public void setGenericNodes(List<GenericNode> genericNodes) {
        this.genericNodes = genericNodes;
    }

    public void addGenericNode(GenericNode genericNode){
        this.genericNodes.add(genericNode);
    }

    public GenericNode getGenericNode(int index){
        return genericNodes.get(index);
    }

    public String getClassFileName() {
        return classFileName;
    }

    public void setClassFileName(String classFileName) {
        this.classFileName = classFileName;
    }

    public ClassNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(ClassNode parentNode) {
        this.parentNode = parentNode;
    }

    public Boolean getShowFieldNotNull() {
        return showFieldNotNull;
    }

    public void setShowFieldNotNull(Boolean showFieldNotNull) {
        this.showFieldNotNull = showFieldNotNull;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }

    public GenericNode getGenericNode(String  type){
        if(genericNodes == null){
            return null;
        }
        for(GenericNode genericNode : genericNodes){
            if(genericNode.getPlaceholder().equals(type)){
                return genericNode;
            }
        }
        return null;
    }

    public String toJsonApi(){
        if(childNodes == null || childNodes.isEmpty()){
            return isList? className + "[]": className + "{}";
        }
        Map<String, Object> jsonRootMap = new LinkedHashMap<>();
        for (FieldNode fieldNode : childNodes) {
            toJsonApiMap(fieldNode,jsonRootMap);
        }
        if(isList){
            return Utils.toPrettyJson(new Map[]{jsonRootMap});
        }else{
            return Utils.toPrettyJson(jsonRootMap);
        }
    }

    private void toJsonApiMap(FieldNode fieldNode, Map<String, Object> map){

        if(fieldNode.getLoopNode()){
            map.put(fieldNode.getName(), getFieldDesc(fieldNode));
            return;
        }

        ClassNode thisFieldNode = fieldNode.getChildNode();
        if(thisFieldNode != null){
            Map<String, Object> childMap = new LinkedHashMap<>();
            for (FieldNode childFieldNode : thisFieldNode.getChildNodes()) {
                if(childFieldNode.getChildNode() != null){
                    toJsonApiMap(childFieldNode,childMap);
                }else{
                    childMap.put(childFieldNode.getName(), getFieldDesc(childFieldNode));
                }
            }
            if(fieldNode.getType() != null && fieldNode.getType().endsWith("[]")){
                map.put(fieldNode.getName(), childMap.isEmpty()? new Map[]{}: new Map[]{childMap});
            }else{
                map.put(fieldNode.getName(), childMap);
            }
        }else{
            map.put(fieldNode.getName(), getFieldDesc(fieldNode));
        }
    }

    private String getFieldDesc(FieldNode fieldNode){
        final String fieldType = fieldNode.getLoopNode()? fieldNode.getChildNode().getClassName()
                + (fieldNode.getChildNode().isList()?"[]":"{}"): fieldNode.getType();
        String fieldDesc;
        if(Utils.isNotEmpty(fieldNode.getDescription())){
            fieldDesc = String.format("%s //%s", fieldType, fieldNode.getDescription());
        }else{
            fieldDesc = fieldType;
        }
        if(showFieldNotNull && fieldNode.getNotNull()){
            fieldDesc =  String.format("%s【%s】", fieldDesc, DocContext.getI18n().getMessage("parameterNeed"));
        }
        return fieldDesc;
    }

    public void reset(){
        this.childNodes.clear();
        this.genericNodes.clear();
    }
}
