package apidoc.codegenerator.ios;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.fileupload.util.Streams;

import apidoc.codegenerator.TemplateProvider;
import apidoc.codegenerator.android.EntryTemplateProvider;
import play.Play;

public class ModelTemplateProvider {
	
	public String provideTemplateForName(String templateName) throws IOException {
		return TemplateProvider.provideTemplateForName("ios/"+templateName);
    }
}
