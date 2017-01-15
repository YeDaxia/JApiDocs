package apidoc.codegenerator.android;

import java.io.IOException;

import apidoc.codegenerator.TemplateProvider;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryTemplateProvider {

    public String provideTemplateForName(String templateName) throws IOException {
    	return TemplateProvider.provideTemplateForName("android/"+templateName);
    }
    
}
