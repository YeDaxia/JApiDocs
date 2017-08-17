import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.IResponseWrapper;
import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.parser.ResponseNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class DocsTest {

    @Test
    public void test_generatePlayApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.PlayProject));
    }

    @Test
    public void test_generateSpringApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.SpringProject));
    }

    @Test
    public void test_generateGenericApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.GenericProject));
    }

    @Test
    public void test_generateJFinalApiDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig(Projects.JFinalProject));
    }

    private Docs.DocsConfig getDocsConfig(String projectPath){
        Docs.DocsConfig config = new Docs.DocsConfig();
        config.setProjectPath(projectPath);
        config.setRapProjectId("1");
        config.setRapHost("http://localhost:8080");
        config.setRapLoginCookie("Idea-a5a2275=b1c68fb9-9388-4f6b-b60d-da7ffd3dec41; _ga=GA1.1.834603554.1485884157; UM_distinctid=15ddab01c37246-08a9a90e29f9f7-143a6d54-13c680-15ddab01c38328; JSESSIONID=DB69653C26E8346AC60F769370D6B765; CNZZDATA5879641=cnzz_eid%3D2117386803-1502607789-http%253A%252F%252Flocalhost%253A8080%252F%26ntime%3D1502891624");
        return config;
    }
}
