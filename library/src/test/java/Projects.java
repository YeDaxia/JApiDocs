import org.junit.Test;

import java.io.File;

/**
 * @author yeguozhong yedaxia.github.com
 */

public class Projects {

    private static String ROOT_PATH;

    static{
        ROOT_PATH = new File(System.getProperty("user.dir")).getParent() + "/";
    }

    public static String SpringProject = ROOT_PATH + "springDemo/";
    public static String PlayProject = ROOT_PATH + "playDemo/";
    public static String GenericProject = ROOT_PATH + "genericDemo/";
    public static String JFinalProject = ROOT_PATH + "jfinalDemo/";
    public static String LibraryProject = ROOT_PATH + "library/";

    public static String LibraryTestPath = LibraryProject + "src/test/java/";

    public static File getTestJavaFile(Class clazz){
        return new File(LibraryTestPath + clazz.getName().replace(".","/") + ".java");
    }

}
