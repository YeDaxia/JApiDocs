import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.parser.ResponseNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import result.ResultVO;

import java.io.File;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ParseUtilsTest {

    String projectPath = "/Users/yeguozhong/IdeaProjects/play-apidocs/library/";

    String javaSrcPath = projectPath + "src/main/java/";

    File testFile = new File(javaSrcPath+"io/github/yedaxia/apidocs/Null.java");

    @Before
    public void setup(){
        //DocContext.setProjectPath(projectPath);
    }

    @Test
    public void test_searchJavaFile(){
        File ParamNodeFile = ParseUtils.searchJavaFile(testFile, "ParamNode");
        Assert.assertEquals(ParamNodeFile.getAbsolutePath(), javaSrcPath + "io/github/yedaxia/apidocs/ParamNode.java");

        File ControllerNodeFile = ParseUtils.searchJavaFile(testFile, "ControllerNode");
        Assert.assertEquals(ControllerNodeFile.getAbsolutePath(), javaSrcPath + "io/github/yedaxia/apidocs/doc/ControllerNode.java");

        File SamePackageInnerFile = ParseUtils.searchJavaFile(testFile,"RecordNode.KA");
        Assert.assertEquals(SamePackageInnerFile.getAbsolutePath(), javaSrcPath + "io/github/yedaxia/apidocs/RecordNode.java");

        File SameClassInnerFile = ParseUtils.searchJavaFile(testFile, "InnerClass");
        Assert.assertEquals(SameClassInnerFile.getAbsolutePath(), javaSrcPath + "io/github/yedaxia/apidocs/Null.java");

        File dimClassFile = ParseUtils.searchJavaFile(testFile ,"FieldNode");
        Assert.assertEquals(dimClassFile.getAbsolutePath(), javaSrcPath + "io/github/yedaxia/apidocs/doc/FieldNode.java");

        File outerInnerClassFile = ParseUtils.searchJavaFile(testFile, "FieldNode.HelloWord");
        Assert.assertEquals(outerInnerClassFile.getAbsolutePath(), javaSrcPath + "io/github/yedaxia/apidocs/doc/FieldNode.java");
    }

    @Test
    public void test_parseResponseNode(){
        ResponseNode responseNode = new ResponseNode();
        responseNode.setClassName("ResultVO");
        File resultJavaFile = Projects.getTestJavaFile(ResultVO.class);
        ParseUtils.parseResponseNode(resultJavaFile, responseNode);
        System.out.println(responseNode);
    }
}
