package apidoc;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class RequestNode {
	public String method;
	public String url;
	public List<ParamNode> paramNodes;
	public Class resultClass;
	public String description;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		if(StringUtils.isEmpty(url)){
			return "";
		}else if(url.contains("/m/")){
			return url.replace("/m/", "dev.yiyoushuo.com/");
		}else if(url.contains("/api/")){
			return url.replace("/api/", "dev.api.yiyoushuo.com/");
		}else{
			return url;
		}
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ParamNode> getParamNodes() {
		return paramNodes;
	}
	public void setParamNodes(List<ParamNode> paramNodes) {
		this.paramNodes = paramNodes;
	}
	public Class getResultClass() {
		return resultClass;
	}
	public void setResultClass(Class resultClass) {
		this.resultClass = resultClass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
