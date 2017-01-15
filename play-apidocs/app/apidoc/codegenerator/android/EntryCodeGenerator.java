package apidoc.codegenerator.android;

import java.io.IOException;
import java.util.List;

import apidoc.ResponseNode;
import apidoc.codegenerator.CodeGenerator;
import apidoc.codegenerator.IEntryProvider;
import apidoc.codegenerator.TemplateProvider;
import apidoc.codegenerator.android.builder.EntryClassBuilder;
import apidoc.codegenerator.android.builder.EntryFieldBuilder;
import apidoc.codegenerator.android.builder.EntryGetterBuilder;
import apidoc.codegenerator.android.builder.EntrySetterBuilder;
import apidoc.codegenerator.model.EntryFieldModel;
import apidoc.codegenerator.provider.ProviderFactory;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryCodeGenerator extends CodeGenerator{

	private static final String FILE_FIELD_TEMPLATE = "Entry_Field_template";
	private static final String FILE_GETTER_TEMPLATE = "Entry_Getter_template";
	private static final String FILE_SETTER_TEMPLATE = "Entry_Setter_template";
	private static final String FILE_CLASS_TEMPLATE = "Entry_template";
	private static final String FILE_CODE_TEMPLATE = "Code_File_template";
	private static final String JAVA_CODE_DIR = "javaCodes";

	private static String sFieldTemplate, sGetterTemplate, sSetterTemplate, sClassTemplate,sCodeTemplate;
	static{
		EntryTemplateProvider resourceTemplateProvider = new EntryTemplateProvider();
		try {
			sFieldTemplate = resourceTemplateProvider.provideTemplateForName(FILE_FIELD_TEMPLATE);
			sGetterTemplate = resourceTemplateProvider.provideTemplateForName(FILE_GETTER_TEMPLATE);
			sSetterTemplate = resourceTemplateProvider.provideTemplateForName(FILE_SETTER_TEMPLATE);
			sClassTemplate = resourceTemplateProvider.provideTemplateForName(FILE_CLASS_TEMPLATE);
			sCodeTemplate = TemplateProvider.provideTemplateForName(FILE_CODE_TEMPLATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public EntryCodeGenerator(ResponseNode responseNode) {
		super(responseNode);
	}
	
	@Override
	public String generateNodeCode(ResponseNode respNode) throws IOException {
		String className = respNode.className;

		IEntryProvider entryProvider = ProviderFactory.createProvider();
		List<EntryFieldModel> entryFields = entryProvider.provideEntryFields(respNode);
		
		if(entryFields == null || entryFields.isEmpty()){
			return "";
		}
		
		StringBuilder fieldStrings = new StringBuilder();
		StringBuilder methodStrings = new StringBuilder();

		String fieldTemplate = sFieldTemplate;
		String getterTemplate = sGetterTemplate;
		String setterTemplate = sSetterTemplate;
		String classTemplate = sClassTemplate;

		for (EntryFieldModel entryFieldModel : entryFields) {
			EntryFieldBuilder fieldBuilder = new EntryFieldBuilder(fieldTemplate, entryFieldModel);
			fieldStrings.append(fieldBuilder.builtString());
			EntryGetterBuilder getterBuilder = new EntryGetterBuilder(getterTemplate, entryFieldModel);
			methodStrings.append(getterBuilder.builtString());
			EntrySetterBuilder setterBuilder = new EntrySetterBuilder(setterTemplate, entryFieldModel);
			methodStrings.append(setterBuilder.builtString());
		}

		if (methodStrings.charAt(methodStrings.length() - 1) == '\n') {
			methodStrings.deleteCharAt(methodStrings.length() - 1);
		}

		EntryClassBuilder classBuilder = new EntryClassBuilder(classTemplate, className, fieldStrings.toString(),
				methodStrings.toString());
		return classBuilder.builtString();
	}

	@Override
	public String getRelativeCodeDir() {
		return JAVA_CODE_DIR;
	}

	@Override
	public String getCodeTemplate() {
		return sCodeTemplate;
	}
}
