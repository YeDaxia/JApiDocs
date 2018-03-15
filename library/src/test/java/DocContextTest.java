import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.parser.PlayControllerParser;
import io.github.yedaxia.apidocs.parser.SpringControllerParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class DocContextTest {

    @Test
    public void test_init(){
        //DocContext.init(Projects.SpringProject,"");
//
//        Assert.assertEquals(DocContext.getJavaSrcPath(), Projects.SpringProject + "src/main/java/");
//        Assert.assertTrue(DocContext.controllerParser() instanceof SpringControllerParser);
//
//        File[] controllerFiles = DocContext.getControllerFiles();
//        List<File> acControllerFiles = new ArrayList<>();
//        Utils.wideSearchFile(new File(DocContext.getJavaSrcPath() + "controller"), new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".java");
//            }
//        },acControllerFiles,  false);
//
//        Assert.assertEquals(sortFilesByName(controllerFiles), sortFilesByName(acControllerFiles.toArray(new File[acControllerFiles.size()])));
//
//        //DocContext.init(Projects.PlayProject,"");
//        Assert.assertEquals(DocContext.getJavaSrcPath(), Projects.PlayProject + "app/");
//        Assert.assertTrue(DocContext.controllerParser() instanceof PlayControllerParser);
//
//        controllerFiles = DocContext.getControllerFiles();
//        acControllerFiles.clear();
//        Utils.wideSearchFile(new File(DocContext.getJavaSrcPath() + "controller"), new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".java");
//            }
//        },acControllerFiles,  false);

//        Assert.assertEquals(sortFilesByName(controllerFiles), sortFilesByName(acControllerFiles.toArray(new File[acControllerFiles.size()])));

    }

    private String sortFilesByName(File[] files){
        SortedSet sortedSet = new TreeSet();
        for(File f: files){
            sortedSet.add(f.getAbsolutePath());
        }
        return Arrays.toString(sortedSet.toArray());
    }
}
