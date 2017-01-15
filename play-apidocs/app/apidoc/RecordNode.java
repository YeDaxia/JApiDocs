package apidoc;

public class RecordNode {
	public String name;
	public String type;
	public String description;
	public ResponseNode childTableNode;
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
	public ResponseNode getChildTableNode() {
		return childTableNode;
	}
	public void setChildTableNode(ResponseNode childTableNode) {
		this.childTableNode = childTableNode;
	}
	@Override
	public String toString() {
		return "RecordNode [name=" + name + ", type=" + type +"]";
	}
	
}
