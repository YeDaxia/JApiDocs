package io.github.yedaxia.apidocs;

import io.github.yedaxia.apidocs.doc.HtmlDocGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
		new HtmlDocGenerator().generateDocs();
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

	public static class DocsConfig {

        String projectPath; // must set
        String docsPath; // default equals projectPath
        String codeTplPath; // if empty, use the default resources
        String mvcFramework; //spring, play, jfinal, generic, can be empty

        boolean isSpringMvcProject(){
            return mvcFramework != null && mvcFramework.equals("spring");
        }

        boolean isPlayProject(){
            return mvcFramework != null && mvcFramework.equals("play");
        }

        boolean isJfinalProject(){
            return mvcFramework != null && mvcFramework.equals("jfinal");
        }

        boolean isGeneric(){
            return mvcFramework != null && mvcFramework.equals("generic");
        }

        public void setProjectPath(String projectPath) {
            this.projectPath = projectPath;
        }

        public void setDocsPath(String docsPath) {
            this.docsPath = docsPath;
        }

        public void setCodeTplPath(String codeTplPath) {
            this.codeTplPath = codeTplPath;
        }

        public void setMvcFramework(String mvcFramework) {
            this.mvcFramework = mvcFramework;
        }
    }
}
