package io.github.yedaxia.apidocs;

import io.github.yedaxia.apidocs.doc.HtmlDocGenerator;
import io.github.yedaxia.apidocs.plugin.rap.RapSupportPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *  main entrance
 */
public class Docs {

    private static final String CONFIG_FILE = "docs.config";

    public static void main(String[] args){
        DocsConfig config = loadProps();
        buildHtmlDocs(config);
    }

    /**
     * build html api docs
     */
    public static void buildHtmlDocs(DocsConfig config){
        DocContext.init(config);
        HtmlDocGenerator docGenerator = new HtmlDocGenerator();
        docGenerator.generateDocs();
        DocsConfig docsConfig = DocContext.getDocsConfig();
        if(docsConfig.getRapProjectId() != null && docsConfig.getRapHost() != null){
            IPluginSupport rapPlugin = new RapSupportPlugin();
            rapPlugin.execute(docGenerator.getControllerNodeList());
        }
        for(IPluginSupport plugin: config.getPlugins()){
            plugin.execute(docGenerator.getControllerNodeList());
        }
	}

    /**
     * wrap response into a common structure,don't forget to put responseNode into map.
     *
     * default is:
     *
     * {
     *     code : 0,
     *     data: ${response}
     *     msg: 'success'
     * }
     *
     * @param responseWrapper
     */
	public static void setResponseWrapper(IResponseWrapper responseWrapper){
        DocContext.setResponseWrapper(responseWrapper);
    }

	private static DocsConfig loadProps(){
        try{
            Properties properties = new Properties();
            properties.load(new FileReader(CONFIG_FILE));
            DocsConfig config = new DocsConfig();
            config.projectPath = properties.getProperty("projectPath", null);

            if(config.projectPath == null){
                throw new RuntimeException("projectPath property is needed in the config file.");
            }

            config.docsPath = properties.getProperty("docsPath", null);
            config.codeTplPath = properties.getProperty("codeTplPath", null);
            config.mvcFramework = properties.getProperty("mvcFramework", "");
            return config;
        }catch (IOException e){
            e.printStackTrace();

            try{
                File configFile = new File(CONFIG_FILE);
                configFile.createNewFile();
            }catch (Exception ex){
                e.printStackTrace();
            }

            throw new RuntimeException("you need to set projectPath property in " + CONFIG_FILE);
        }
    }
}
