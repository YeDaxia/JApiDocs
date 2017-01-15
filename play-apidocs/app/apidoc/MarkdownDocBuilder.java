package apidoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MarkdownDocBuilder implements IDocBuilder{
	
	private Class controller;
	private List<RequestNode> requestNodes;
	
	public MarkdownDocBuilder(Class controller, List<RequestNode> requestNodes) {
		super();
		this.controller = controller;
		this.requestNodes = requestNodes;
	}
	
	@Override
	public String build() throws IOException{
		String controllerDescription = Utils.getCtrlDescription(this.controller);
		File apiCtrlFile = Utils.getTemplateFile("api-controller.md.tpl");
		final String ctrlTemplate= org.apache.commons.fileupload.util.Streams.asString(new FileInputStream(apiCtrlFile));
		
		String ctrlDoc = ctrlTemplate;
		
		if(StringUtils.isNotEmpty(controllerDescription)){
			ctrlDoc = ctrlDoc.replace("${CONTROLLER_DESCRIPTION}", controllerDescription);
		}
		
		File apiActionFile = Utils.getTemplateFile("api-action.md.tpl");
		final String actionTemplte = org.apache.commons.fileupload.util.Streams.asString(new FileInputStream(apiActionFile));
		StringBuilder actionsBuilder = new StringBuilder();
		
		String actionDoc = actionTemplte;
		for (RequestNode requestNode : requestNodes) {
			if(StringUtils.isNotEmpty(requestNode.description)){
				actionDoc = actionDoc.replace("${ACTION_DESCRIPTION}", requestNode.description);
			}
			if(StringUtils.isNotEmpty(requestNode.method)){
				actionDoc = actionDoc.replace("${METHOD}", requestNode.method);
			}
			if(StringUtils.isNotEmpty(requestNode.url)){
				actionDoc = actionDoc.replace("${APIURL}", requestNode.url);
			}
			if(requestNode.paramNodes != null){
				StringBuilder paramListBuilder = new StringBuilder();
				for (ParamNode paramNode : requestNode.paramNodes) {
					paramListBuilder.append(String.format("| %s | %s | %s | %s |", paramNode.name,paramNode.type,paramNode.required,paramNode.description));
					paramListBuilder.append('\n');
				}
				actionDoc = actionDoc.replace("${PARAM_LIST}", paramListBuilder.toString());
			}
			if(requestNode.resultClass != null){
				ResponseBuilder responseBuilder = new ResponseBuilder(requestNode.resultClass);
				ResponseNode responseNode = responseBuilder.build();
				actionDoc = actionDoc.replace("${RESPONSE}", responseNode.toJsonApi());
			}
			actionsBuilder.append(actionDoc);
		}
		
		return ctrlDoc.replace("${ACTION_LIST}", actionsBuilder.toString());
	}
}
