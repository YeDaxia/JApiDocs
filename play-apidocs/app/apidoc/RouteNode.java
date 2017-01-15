package apidoc;

public class RouteNode {
	public String method;
	public String routeUrl;
	public Class<?> controller;
	public String actionMethod;
	public String comment;
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRouteUrl() {
		return routeUrl;
	}
	public void setRouteUrl(String routeUrl) {
		this.routeUrl = routeUrl;
	}
	public Class<?> getController() {
		return controller;
	}
	public void setController(Class<?> controller) {
		this.controller = controller;
	}
	public String getActionMethod() {
		return actionMethod;
	}
	public void setActionMethod(String actionMethod) {
		this.actionMethod = actionMethod;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
