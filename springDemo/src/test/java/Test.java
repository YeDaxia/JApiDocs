import io.github.yedaxia.apidocs.Docs;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class Test {

    @org.junit.Test
    public void test_gDocs(){
        Docs.DocsConfig docsConfig = new Docs.DocsConfig();
        docsConfig.setProjectPath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/springDemo");
        docsConfig.setDocsPath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/springDemo/apidocs");
        Docs.buildHtmlDocs(docsConfig);
    }

}
