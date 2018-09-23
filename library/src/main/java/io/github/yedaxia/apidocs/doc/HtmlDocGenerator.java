package io.github.yedaxia.apidocs.doc;

import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.LogUtils;
import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.AbsControllerParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Html Api docs generator
 *
 * @author yeguozhong yedaxia.github.com
 */
public class HtmlDocGenerator extends AbsDocGenerator {

    public HtmlDocGenerator() {
        super(DocContext.controllerParser(), new HtmlControllerDocBuilder());
    }

    @Override
    void generateIndex(List<Link> docFileNameList) {

        if(docFileNameList.isEmpty()){
            return;
        }

        try {
            InputStream tplIndexSteam = Resources.getTemplateFile("api-index.html.tpl");
            String indexTemplate = Utils.streamToString(tplIndexSteam);
            StringBuilder indexBuilder = new StringBuilder();
            for (Link link : docFileNameList) {
                indexBuilder.append(String.format("<li><a href=\"%s\">%s</a></li>",link.getUrl(), link.getName()));
            }
            indexTemplate = indexTemplate.replace("${API_LIST}", indexBuilder.toString());
            Utils.writeToDisk(new File(DocContext.getDocPath(), "index.html"), indexTemplate);
        } catch (IOException e) {
            LogUtils.error("generate index html fail. ",e);
        }

        copyCssStyle();
    }

    private void copyCssStyle(){
        try{
            String cssFileName = "style.css";
            File cssFile = new File(DocContext.getDocPath(), cssFileName);
            Utils.writeToDisk(cssFile, Utils.streamToString(Resources.getTemplateFile(cssFileName)));;
        }catch (IOException  e){
            LogUtils.error("copyCssStyle fail",e);
        }

    }
}
