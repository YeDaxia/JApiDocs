import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;

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
        docsConfig.addPlugin(new MarkdownDocPlugin());
        Docs.buildHtmlDocs(docsConfig);
    }

    @org.junit.Test
    public void test_gDocsWithResource(){
        DocsConfig docsConfig = new DocsConfig();
        docsConfig.setProjectPath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/springDemo");
        docsConfig.setApiVersion("V1.0");
        docsConfig.setAutoGenerate(Boolean.TRUE);
        docsConfig.setResourcePath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/library/src/main/resources");
        docsConfig.setDocsPath("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs/springDemo/apidocs");
        Docs.buildHtmlDocs(docsConfig);
    }
}
