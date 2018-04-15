package io.github.yedaxia.apidocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    public static void setDebug(){
        isDebug = true;
        sResourcePath = System.getProperty("user.dir") + "/build/resources/main";
        sUserCodeTplPath = sResourcePath;
    }
}
