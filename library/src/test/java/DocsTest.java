import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.Resources;
import org.junit.Test;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class DocsTest {

    @Test
    public void test_generatePlayApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.PlayProject,Projects.DocsPath));
    }

    @Test
    public void test_generateSpringApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.SpringProject,Projects.DocsPath));
    }

    @Test
    public void test_generateGenericApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.GenericProject,Projects.DocsPath));
    }

    @Test
    public void test_generateJFinalApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.JFinalProject,Projects.DocsPath));
    }

    private Docs.DocsConfig getDocsConfig(String projectPath, String doscPath){
        Docs.DocsConfig config = new Docs.DocsConfig();
        config.setProjectPath(projectPath);
        config.setDocsPath(doscPath);
        return config;
    }
}
