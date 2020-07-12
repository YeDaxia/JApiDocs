package io.github.yedaxia.apidocs.doc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.parser.ParamNode;
import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.codegenerator.ios.ModelCodeGenerator;
import io.github.yedaxia.apidocs.codegenerator.java.JavaCodeGenerator;
import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.RequestNode;

import javax.print.Doc;
import java.io.*;
import java.util.*;

/**
 * build html api docs for a controller
 *
 * @author yeguozhong yedaxia.github.com
 */
public class HtmlControllerDocBuilder implements IControllerDocBuilder {

    @Override
    public String buildDoc(ControllerNode controllerNode) throws IOException {

        for (RequestNode requestNode : controllerNode.getRequestNodes()) {
            if (requestNode.getResponseNode() != null && !requestNode.getResponseNode().getChildNodes().isEmpty()) {
                JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(requestNode.getResponseNode());
                final String javaSrcUrl = javaCodeGenerator.generateCode();
                requestNode.setAndroidCodePath(javaSrcUrl);
                ModelCodeGenerator iosCodeGenerator = new ModelCodeGenerator(requestNode.getResponseNode());
                final String iosSrcUrl = iosCodeGenerator.generateCode();
                requestNode.setIosCodePath(iosSrcUrl);
            }
        }

        final Template ctrlTemplate = getControllerTpl();
        final File docFile = new File(DocContext.getDocPath(), controllerNode.getDocFileName());
        FileWriter docFileWriter = new FileWriter(docFile);
        Map<String, Object> data = new HashMap<>();
        data.put("controllerNodeList", DocContext.getControllerNodeList());
        data.put("controller", controllerNode);
        data.put("currentApiVersion", DocContext.getCurrentApiVersion());
        data.put("apiVersionList", DocContext.getApiVersionList());
        data.put("projectName", DocContext.getDocsConfig().getProjectName());
        data.put("i18n", DocContext.getI18n());

        try {
            ctrlTemplate.process(data, docFileWriter);
        } catch (TemplateException ex) {
            ex.printStackTrace();
        } finally {
            Utils.closeSilently(docFileWriter);
        }
        return Utils.streamToString(new FileInputStream(docFile));
    }

    private Template getControllerTpl() throws IOException {
        return Resources.getFreemarkerTemplate("api-controller.html.ftl");
    }

}
