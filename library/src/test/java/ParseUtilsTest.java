import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.parser.ClassNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import result.BookVO;
import result.ResultVO;
import result.Student;

import java.io.File;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ParseUtilsTest {

    String projectPath = "/Users/yeguozhong/IdeaProjects/play-apidocs/library/";

    String javaSrcPath = projectPath + "src/main/java/";

    File testFile = new File(javaSrcPath + "io/github/yedaxia/apidocs/Null.java");

    @Before
    public void setup() {
        DocContext.getJavaSrcPaths().add(Projects.LibraryTestPath);
    }

    @Test
    public void test_searchJavaFile() {
        File innerClassFile = ParseUtils.searchJavaFile(new File(Projects.LibraryTestPath, "controller/UserApi.java"), "Student.InnerStudent");
        Assert.assertEquals(innerClassFile.getAbsolutePath(), Projects.LibraryTestPath + "result/Student.java");
    }

    @Test
    public void test_searchJavaFile1(){
        final String javaSrcPath = "H:/koushare/ks-user/src";
        DocContext.getJavaSrcPaths().add(javaSrcPath);
        File javaFile = ParseUtils.searchJavaFile(new File("UserController所在目录"), "UserRGMVO");
        Assert.assertNotNull(javaFile);
    }

    @Test
    public void test_parseClassNode_InnerClass1() {
        ClassNode classNode = new ClassNode();
        classNode.setClassName("Student.InnerStudent");
        File resultJavaFile = Projects.getTestJavaFile(Student.class);
        ParseUtils.parseClassNode(resultJavaFile, classNode);
        System.out.println(classNode.toJsonApi());
    }

    @Test
    public void test_parseClassNode() {
        ClassNode classNode = new ClassNode();
        classNode.setClassName("BookVO");
        File resultJavaFile = Projects.getTestJavaFile(BookVO.class);
        ParseUtils.parseClassNode(resultJavaFile, classNode);
        System.out.println(classNode.toJsonApi());
    }

    @Test
    public void test_parseGenericClassNode() {
        File resultJavaFile = Projects.getTestJavaFile(ResultVO.class);

        ParseUtils.compilationUnit(resultJavaFile).getChildNodesByType(MethodDeclaration.class).forEach(md -> {
            md.getType();
        });

        ParseUtils.compilationUnit(resultJavaFile).getClassByName("ResultVO")
                .ifPresent(classDeclaration -> {
                    NodeList<TypeParameter> typeParameters = classDeclaration.getTypeParameters();
                    for (int i = 0, len = typeParameters.size(); i != len; i++) {
                        System.out.println(typeParameters.get(i).getName());
                    }
                });
    }

    @Test
    public void test_isCollectionType() {
        Assert.assertTrue(ParseUtils.isCollectionType("List<Demo1.Demo2>"));
    }

    @Test
    public void test_parseClassNodeByType(){
        ClassNode classNode = new ClassNode();
        Class modelClass = Student.class;
        ClassOrInterfaceType modelType = new ClassOrInterfaceType(modelClass.getSimpleName());
        ParseUtils.parseClassNodeByType(Projects.getTestJavaFile(modelClass), classNode, modelType);
        System.out.println(classNode.toJsonApi());
    }

    @Test
    public void test_parseClassNodeByReflection(){
        ClassNode classNode = new ClassNode();
        classNode.setModelClass(ResultVO.class);
        ParseUtils.parseClassNodeByReflection(classNode);
        System.out.println(classNode.toJsonApi());
    }

    private ClassOrInterfaceDeclaration getClassDeclarationByClass(Class clazz){
        File modelJavaFile = Projects.getTestJavaFile(clazz);
        return ParseUtils.compilationUnit(modelJavaFile).getClassByName(clazz.getSimpleName()).get();
    }

    public ResultVO<Student> getGenericMethod(){
        return null;
    }

}
