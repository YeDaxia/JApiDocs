package io.github.yedaxia.apidocs.parser;

/**
 * a param node corresponds to a request parameter
 */
public class ParamNode {

	public String name;
	public String type;
	public boolean required;
	public String description;
	public boolean jsonBody;// when true ,the json body set to description

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

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(boolean jsonBody) {
		this.jsonBody = jsonBody;
	}
}
