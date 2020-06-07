import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.utils.Utils;
import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.parser.ResponseNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import result.GenericResult;
import result.ResultVO;
import result.Student;

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
        DocContext.getJavaSrcPaths().add(Projects.LibraryTestPath);
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
    public void test_parseClassNode(){
        ResponseNode responseNode = new ResponseNode();
        responseNode.setClassName("Student");
        File resultJavaFile = Projects.getTestJavaFile(Student.class);
        ParseUtils.parseClassNode(resultJavaFile, responseNode);
        System.out.println(responseNode.toJsonApi());
    }

    @Test
    public void test_parseGenericClassNode(){
        File resultJavaFile = Projects.getTestJavaFile(GenericResult.class);

        ParseUtils.compilationUnit(resultJavaFile).getChildNodesByType(MethodDeclaration.class).forEach(md->{
             md.getType();
        });

        ParseUtils.compilationUnit(resultJavaFile).getClassByName("GenericResult")
                .ifPresent(classDeclaration -> {
                    NodeList<TypeParameter> typeParameters = classDeclaration.getTypeParameters();
                    for(int i = 0, len = typeParameters.size(); i != len; i++){
                        System.out.println(typeParameters.get(i).getName());
                    }
                });
    }


    @Test
    public void test_isCollectionType(){
        Assert.assertTrue(ParseUtils.isCollectionType("List<Demo1.Demo2>"));
    }
}
