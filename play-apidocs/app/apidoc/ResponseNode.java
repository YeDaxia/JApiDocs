package apidoc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

public class ResponseNode {
	public String className;
	public String description;
	public boolean isList;
	public List<RecordNode> childNodes;
	
	public boolean isList() {
		return isList;
	}
	public void setList(boolean isList) {
		this.isList = isList;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<RecordNode> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(List<RecordNode> childNodes) {
		this.childNodes = childNodes;
	}
	
	public String toJsonApi(){
		if(childNodes == null || childNodes.isEmpty()){
			return "";
		}
		Map jsonRootMap = new LinkedHashMap<>();
		for (RecordNode recordNode : childNodes) {
			toJsonApiMap(recordNode,jsonRootMap);
		}
		if(isList){
			return Utils.toJson(new Map[]{jsonRootMap});
		}else{
			return Utils.toJson(jsonRootMap);
		}
	}
	
	public void toJsonApiMap(RecordNode recordNode, Map map){
		if(recordNode.childTableNode != null){
			Map childMap = new LinkedHashMap<>();
			for (RecordNode childNode : recordNode.childTableNode.childNodes) {
				if(childNode.childTableNode != null){
					toJsonApiMap(childNode,childMap);
				}else{
					childMap.put(childNode.name, getRecordDescp(childNode));
				}
			}
			if(recordNode.type.endsWith("[]")){
				map.put(recordNode.name, new Map[]{childMap});
			}else{
				map.put(recordNode.name, childMap);
			}
		}else{
			map.put(recordNode.name, getRecordDescp(recordNode));
		}
	}
	
	private String getRecordDescp(RecordNode recordNode){
		if(StringUtils.isNotEmpty(recordNode.description)){
			return String.format("%s //%s", recordNode.type,recordNode.description);
		}else{
			return recordNode.type;
		}
	}
}
