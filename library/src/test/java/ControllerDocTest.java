import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.doc.HtmlControllerDocBuilder;
import io.github.yedaxia.apidocs.doc.IControllerDocBuilder;
import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.SpringControllerParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ControllerDocTest {

    private static final String SPRING_API_JAVA_FILE = Projects.SpringProject + "src/main/java/controller/UserController.java";

    @Before
    public void init_path(){
        Resources.setDebug();
        DocsConfig config = new DocsConfig();
        config.setProjectPath(Projects.SpringProject);
        config.setDocsPath(Projects.ROOT_PATH + "ApiTestDocs");
        config.setApiVersion("V1.0");
        config.setAutoGenerate(true);
        DocContext.init(config);
    }

    @Test
    public void test_generate_springControllerDoc() throws IOException {
        SpringControllerParser springControllerParser = new SpringControllerParser();
        ControllerNode controllerNode = springControllerParser.parse(new File(SPRING_API_JAVA_FILE));
        IControllerDocBuilder controllerDocBuilder = new HtmlControllerDocBuilder();
        controllerDocBuilder.buildDoc(controllerNode);
    }
}
