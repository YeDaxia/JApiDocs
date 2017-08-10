package io.github.yedaxia.apidocs.codegenerator;

import io.github.yedaxia.apidocs.Resources;
import io.github.yedaxia.apidocs.Utils;

import java.io.FileInputStream;
import java.io.IOException;


public class TemplateProvider {
	public static String provideTemplateForName(String templateName) throws IOException {
		return Utils.streamToString(Resources.getCodeTemplateFile(templateName));
    }
}
