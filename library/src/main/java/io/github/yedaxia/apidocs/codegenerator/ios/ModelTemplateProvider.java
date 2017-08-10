package io.github.yedaxia.apidocs.codegenerator.ios;

import io.github.yedaxia.apidocs.codegenerator.TemplateProvider;

import java.io.IOException;


public class ModelTemplateProvider {
	
	public String provideTemplateForName(String templateName) throws IOException {
		return TemplateProvider.provideTemplateForName(templateName);
    }

}
