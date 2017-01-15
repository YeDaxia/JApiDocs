package apidoc.codegenerator;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.fileupload.util.Streams;

public class TemplateProvider {
	public static String provideTemplateForName(String templateName) throws IOException {
    	String appPath = System.getProperty("user.dir")+"/app/";  
		String tClsName = TemplateProvider.class.getName();
		String pkgPath= tClsName.substring(0, tClsName.lastIndexOf('.')).replace('.', '/') + '/';
		return Streams.asString(new FileInputStream(appPath+pkgPath+ templateName));
    }
}
