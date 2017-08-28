package io.github.yedaxia.apidocs.doc;

import io.github.yedaxia.apidocs.parser.ParamNode;
import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.codegenerator.ios.ModelCodeGenerator;
import io.github.yedaxia.apidocs.codegenerator.java.JavaCodeGenerator;
import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.RequestNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * build html api docs for a controller
 *
 * @author yeguozhong yedaxia.github.com
 */
public class HtmlControllerDocBuilder implements IControllerDocBuilder{

    @Override
    public String buildDoc(ControllerNode controllerNode) throws IOException{

        final String ctrlTemplate = getControllerTpl();

        String ctrlDoc = ctrlTemplate;

        if (Utils.isNotEmpty(controllerNode.getDescription())) {
            ctrlDoc = ctrlDoc.replace("${CONTROLLER_DESCRIPTION}", controllerNode.getDescription());
        }

        final String actionTemplte = getActionTpl();

        StringBuilder actionsBuilder = new StringBuilder();
        StringBuilder tocBuilder = new StringBuilder();
        String actionDoc;
        for (RequestNode requestNode : controllerNode.getRequestNodes()) {
            actionDoc = actionTemplte;
            if (Utils.isNotEmpty(requestNode.getDescription())) {
                String descriptionHtml = requestNode.getDeprecated() ? String.format("<STRIKE>%s</STRIKE>", requestNode.getDescription()) : requestNode.getDescription();
                actionDoc = actionDoc.replace("${ACTION_DESCRIPTION}", descriptionHtml);
                String tocActionHtml = requestNode.getDeprecated() ? "<li><a href=\"#%s\"><STRIKE>%s</STRIKE></a></li>" : "<li><a href=\"#%s\">%s</a></li>";
                tocBuilder.append(String.format(tocActionHtml, requestNode.getDescription(),
                        requestNode.getDescription()));
            }
            String methonStr = Arrays.toString(requestNode.getMethod().toArray());
            actionDoc = actionDoc.replace("${METHOD}", methonStr.substring(1, methonStr.length() - 1));

            if (Utils.isNotEmpty(requestNode.getUrl())) {
                actionDoc = actionDoc.replace("${APIURL}", controllerNode.getBaseUrl() + requestNode.getUrl());
            }
            if (requestNode.getParamNodes() != null) {
                StringBuilder paramListBuilder = new StringBuilder();
                boolean isJsonBody = false;
                String paramHtmlBody = "";

                for (ParamNode paramNode : requestNode.getParamNodes()) {
                    if(paramNode.isJsonBody()){
                        paramHtmlBody = buildParamJsonCode(paramNode);
                        isJsonBody = true;
                        break;
                    }
                }

                if(!isJsonBody){
                    paramHtmlBody = buildParamTable(requestNode.getParamNodes());
                }

                actionDoc = actionDoc.replace("${PARAM_BODY}", paramHtmlBody);
            }

            if (requestNode.getResponseNode() != null) {
                actionDoc = actionDoc.replace("${RESPONSE}", requestNode.getResponseNode().toJsonApi());
                JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(requestNode.getResponseNode());
                String javaurl = javaCodeGenerator.generateCode();
                actionDoc = actionDoc.replace("${ANDROID_CODE}", javaurl);
                ModelCodeGenerator iosCodeGenerator = new ModelCodeGenerator(requestNode.getResponseNode());
                String iosUrl = iosCodeGenerator.generateCode();
                actionDoc = actionDoc.replace("${IOS_CODE}", iosUrl);
            }
            actionsBuilder.append(actionDoc);
        }
        ctrlDoc = ctrlDoc.replace("${TOC}", tocBuilder.toString());
        ctrlDoc = ctrlDoc.replace("${ACTION_LIST}", actionsBuilder.toString());
        return ctrlDoc;
    }

    private String buildParamJsonCode(ParamNode paramNode){
        StringBuilder codeBuilder = new StringBuilder();
        codeBuilder.append("<pre class=\"prettyprint lang-json\">");
        codeBuilder.append('\n');
        codeBuilder.append(paramNode.getDescription());
        codeBuilder.append('\n');
        codeBuilder.append("</pre>");
        return codeBuilder.toString();
    }

    private String buildParamTable(List<ParamNode> paramNodeList){
        StringBuilder paramTableBuilder = new StringBuilder();
        paramTableBuilder.append("<table>");
        paramTableBuilder.append("<tr><th>参数名</th><th>类型</th><th>必需</th><th>描述</th></tr>");
        for (ParamNode paramNode : paramNodeList) {
            paramTableBuilder.append("<tr>");
            paramTableBuilder.append(String.format("<td>%s</td><td>%s</td><td>%s</td><td>%s</td>",
                    paramNode.name, paramNode.type, paramNode.required, paramNode.description));
            paramTableBuilder.append("</tr>");
        }
        paramTableBuilder.append("</table>");
        return paramTableBuilder.toString();
    }

    private String getControllerTpl() throws IOException{
        return Utils.streamToString(Resources.getTemplateFile("api-controller.html.tpl"));
    }

    private String getActionTpl() throws IOException{
        return Utils.streamToString(Resources.getTemplateFile("api-action.html.tpl"));
    }
}
