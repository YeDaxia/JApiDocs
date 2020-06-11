import freemarker.template.Configuration;
import freemarker.template.Template;
import io.github.yedaxia.apidocs.I18n;
import io.github.yedaxia.apidocs.ParseUtils;
import io.github.yedaxia.apidocs.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class UtilsTest {

    @Test
    public void test_getGradleModuleNames(){
        List<String> gradleModules = Utils.getModuleNames(new File("/Users/yeguozhong/Desktop/gitLibrary/JApiDocs"));
        Assert.assertArrayEquals(gradleModules.toArray(new String[gradleModules.size()]),
                new String[]{"library","playDemo","springDemo","genericDemo","jfinalDemo"});
    }

    @Test
    public void test_getMavenModuleNames(){
        List<String> gradleModules = Utils.getModuleNames(new File("/Users/yeguozhong/Desktop/svnLibrary/jap"));
        Assert.assertArrayEquals(gradleModules.toArray(new String[gradleModules.size()]), new String[]{"webapi","model"});
    }

    @Test
    public void test_i18N(){
        I18n i18n = new I18n(Locale.ENGLISH);
        String text = i18n.getMessage("searchPlaceholder");
        System.out.println(text);
    }
}
