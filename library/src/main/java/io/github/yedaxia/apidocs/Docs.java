package io.github.yedaxia.apidocs;

import io.github.yedaxia.apidocs.doc.HtmlDocGenerator;
import io.github.yedaxia.apidocs.ext.rap.RapSupport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        RapSupport rapSupport = new RapSupport(docGenerator.getControllerNodeList());
        rapSupport.postToRap();
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

	public static class DocsConfig {

        String projectPath; // must set
        List<String> javaSrcPaths = new ArrayList<>(); //multi modules support
        String docsPath; // default equals projectPath
        String codeTplPath; // if empty, use the default resources
        String mvcFramework; //spring, play, jfinal, generic, can be empty

        String rapHost;
        String rapLoginCookie;
        String rapProjectId;
        String rapAccount;
        String rapPassword;

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

        public String getRapHost() {
            return rapHost;
        }

        public void setRapHost(String rapHost) {
            this.rapHost = rapHost;
        }

        public String getRapLoginCookie() {
            return rapLoginCookie;
        }

        /**
         * use http://rap.yedaxia.me , just set account and password would be better
         *
         * @param rapLoginCookie
         */
        @Deprecated
        public void setRapLoginCookie(String rapLoginCookie) {
            this.rapLoginCookie = rapLoginCookie;
        }

        public String getRapProjectId() {
            return rapProjectId;
        }

        public void setRapProjectId(String rapProjectId) {
            this.rapProjectId = rapProjectId;
        }

        public String getRapAccount() {
            return rapAccount;
        }

        public void setRapAccount(String rapAccount) {
            this.rapAccount = rapAccount;
        }

        public String getRapPassword() {
            return rapPassword;
        }

        public void setRapPassword(String rapPassword) {
            this.rapPassword = rapPassword;
        }

        public List<String> getJavaSrcPaths() {
            return javaSrcPaths;
        }

        /**
         * if cannot find the java file from other module, you can try to config the java src path yourself.
         *
         * @param javaSrcPath
         */
        public void addJavaSrcPath(String javaSrcPath){
            javaSrcPaths.add(javaSrcPath);
        }
    }
}
