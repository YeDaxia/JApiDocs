package io.github.yedaxia.apidocs;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class Resources {

    private static boolean isDebug = false;
    private static String sResourcePath;
    private static String sUserCodeTplPath;

    /**
     * get template file
     * @param fileName
     * @return
     */
    public static InputStream getTemplateFile(String fileName) throws FileNotFoundException{
        if(isDebug){
            return new FileInputStream(new File(sResourcePath,fileName));
        }else{
            return Resources.class.getClass().getResourceAsStream("/" + fileName);
        }
    }

    static void setUserCodeTplPath(String userCodeTplPath){
        sUserCodeTplPath = userCodeTplPath;
    }

    /**
     * get code template file
     *
     * @param fileName
     * @return
     */
    public static InputStream getCodeTemplateFile(String fileName)throws FileNotFoundException{
        if(sUserCodeTplPath != null){
            File tplFile = new File(sUserCodeTplPath,fileName);
            if(tplFile.isFile() && tplFile.exists()){
                return new FileInputStream(tplFile);
            }else{
                return Resources.class.getClass().getResourceAsStream("/" + fileName);
            }
        }else{
            return getTemplateFile(fileName);
        }
    }

    /**
     * get freemarker template
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Template getFreemarkerTemplate(String fileName) throws IOException {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_30);
        if(isDebug){
            conf.setDirectoryForTemplateLoading(new File(sResourcePath));
        }else{
            conf.setClassForTemplateLoading(Resources.class, "/");
        }
        return conf.getTemplate(fileName);
    }


    public static void setDebug(){
        isDebug = true;
        sResourcePath = System.getProperty("user.dir") + "/build/resources/main";
        sUserCodeTplPath = sResourcePath;
    }
}
