package io.github.yedaxia.apidocs.parser;

import io.github.yedaxia.apidocs.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * response node
 *
 * @author yeguozhong yedaxia.github.com
 */
public class ResponseNode {

    private String className;
    private String description;
    private Boolean isList;
    private List<FieldNode> childNodes = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getList() {
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

    public String toJsonApi(){
        if(childNodes == null || childNodes.isEmpty()){
            return "";
        }
        Map jsonRootMap = new LinkedHashMap<>();
        for (FieldNode recordNode : childNodes) {
            toJsonApiMap(recordNode,jsonRootMap);
        }
        if(isList){
            return Utils.toJson(new Map[]{jsonRootMap});
        }else{
            return Utils.toJson(jsonRootMap);
        }
    }

    public void toJsonApiMap(FieldNode recordNode, Map map){
        ResponseNode childResponseNode = recordNode.getChildResponseNode();
        if(childResponseNode != null){
            Map childMap = new LinkedHashMap<>();
            for (FieldNode childNode : childResponseNode.childNodes) {
                if(childNode.getChildResponseNode() != null){
                    toJsonApiMap(childNode,childMap);
                }else{
                    childMap.put(childNode.getName(), getRecordDescp(childNode));
                }
            }
            if(recordNode.getType().endsWith("[]")){
                map.put(recordNode.getName(), new Map[]{childMap});
            }else{
                map.put(recordNode.getName(), childMap);
            }
        }else{
            map.put(recordNode.getName(), getRecordDescp(recordNode));
        }
    }

    private String getRecordDescp(FieldNode recordNode){
        if(Utils.isNotEmpty(recordNode.getDescription())){
            return String.format("%s //%s", recordNode.getType(),recordNode.getDescription());
        }else{
            return recordNode.getType();
        }
    }
}
