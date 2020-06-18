import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.IResponseWrapper;
import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.parser.ResponseNode;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
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

    @Test
    public void test_generateMultiModuleDocs(){
        Resources.setDebug();
        Docs.buildHtmlDocs(getDocsConfig("/Users/yeguozhong/Desktop/svnLibrary/jap"));
    }

    @Test
    public void test_Mvn(){
        DocsConfig config = new DocsConfig();
        config.setProjectPath("/Users/yeguozhong/Downloads/test_JApiDocs"); // 项目根目录
        config.setProjectName("test_JApiDocs"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }

    private DocsConfig getDocsConfig(String projectPath){
        DocsConfig config = new DocsConfig();
        config.setProjectPath(projectPath);
        config.setAutoGenerate(Boolean.TRUE);
        config.setLocale(Locale.SIMPLIFIED_CHINESE);
        config.setApiVersion("V2.0");
        config.setProjectName("ProjectName");
        config.addPlugin(new MarkdownDocPlugin());
        //config.setRapProjectId("1");
        //config.setRapHost("http://rap.yedaxia.me");
        //config.setRapAccount("***");
        //config.setRapPassword("123456");
        return config;
    }
}
