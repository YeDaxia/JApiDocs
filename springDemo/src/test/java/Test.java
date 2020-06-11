import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class Test {

    @org.junit.Test
    public void test_gDocs(){
        DocsConfig docsConfig = new DocsConfig();
        docsConfig.setProjectPath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/springDemo");
        docsConfig.setApiVersion("V1.0");
        docsConfig.setAutoGenerate(Boolean.TRUE);
        docsConfig.setDocsPath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/springDemo/apidocs");
        Docs.buildHtmlDocs(docsConfig);
    }

}
