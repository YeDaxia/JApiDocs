package apidoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import apidoc.codegenerator.android.EntryCodeGenerator;
import apidoc.codegenerator.ios.ModelCodeGenerator;

public class HtmlDocBuilder implements IDocBuilder {

	private Class controller;
	private List<RequestNode> requestNodes;

	public HtmlDocBuilder(Class controller, List<RequestNode> requestNodes) {
		this.controller = controller;
		this.requestNodes = requestNodes;
	}

	@Override
	public String build() throws IOException {
		String controllerDescription = Utils.getCtrlDescription(this.controller);
		File apiCtrlFile = Utils.getTemplateFile("api-controller.html.tpl");
		final String ctrlTemplate = org.apache.commons.fileupload.util.Streams
				.asString(new FileInputStream(apiCtrlFile));

		String ctrlDoc = ctrlTemplate;

		if (StringUtils.isNotEmpty(controllerDescription)) {
			ctrlDoc = ctrlDoc.replace("${CONTROLLER_DESCRIPTION}", controllerDescription);
		}

		File apiActionFile = Utils.getTemplateFile("api-action.html.tpl");
		final String actionTemplte = org.apache.commons.fileupload.util.Streams
				.asString(new FileInputStream(apiActionFile));
		StringBuilder actionsBuilder = new StringBuilder();
		StringBuilder tocBuilder = new StringBuilder();
		String actionDoc;
		for (RequestNode requestNode : requestNodes) {
			if (Utils.isMSiteUrl(requestNode.getUrl())) {
				continue;
			}
			actionDoc = actionTemplte;
			if (StringUtils.isNotEmpty(requestNode.description)) {
				actionDoc = actionDoc.replace("${ACTION_DESCRIPTION}", requestNode.description);
				tocBuilder.append(String.format("<li><a href=\"#%s\">%s</a></li>", requestNode.description,
						requestNode.description));
			}
			if (StringUtils.isNotEmpty(requestNode.method)) {
				actionDoc = actionDoc.replace("${METHOD}", requestNode.method);
			}
			if (StringUtils.isNotEmpty(requestNode.url)) {
				actionDoc = actionDoc.replace("${APIURL}", requestNode.getUrl());
			}
			if (requestNode.paramNodes != null) {
				StringBuilder paramListBuilder = new StringBuilder();
				for (ParamNode paramNode : requestNode.paramNodes) {
					paramListBuilder.append("<tr>");
					paramListBuilder.append(String.format("<td>%s</td><td>%s</td><td>%s</td><td>%s</td>",
							paramNode.name, paramNode.type, paramNode.required, paramNode.description));
					paramListBuilder.append("</tr>");
				}
				actionDoc = actionDoc.replace("${PARAM_LIST}", paramListBuilder.toString());
			}
			if (requestNode.resultClass != null) {
				ResponseBuilder responseBuilder = new ResponseBuilder(requestNode.resultClass);
				ResponseNode responseNode = responseBuilder.build();
				actionDoc = actionDoc.replace("${RESPONSE}", responseNode.toJsonApi());
				EntryCodeGenerator javaCodeGenerator = new EntryCodeGenerator(responseNode);
				String javaurl = javaCodeGenerator.generateCode();
				actionDoc = actionDoc.replace("${ANDROID_CODE}", javaurl);
				ModelCodeGenerator iosCodeGenerator = new ModelCodeGenerator(responseNode);
				String iosUrl = iosCodeGenerator.generateCode();
				actionDoc = actionDoc.replace("${IOS_CODE}", iosUrl);
			}
			actionsBuilder.append(actionDoc);
		}
		ctrlDoc = ctrlDoc.replace("${TOC}", tocBuilder.toString());
		ctrlDoc = ctrlDoc.replace("${ACTION_LIST}", actionsBuilder.toString());
		return ctrlDoc;
	}
}
