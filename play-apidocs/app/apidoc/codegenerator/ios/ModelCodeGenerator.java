package apidoc.codegenerator.ios;

import java.io.File;
import java.io.IOException;
import java.util.List;

import apidoc.Docs;
import apidoc.RecordNode;
import apidoc.ResponseNode;
import apidoc.Utils;
import apidoc.codegenerator.CodeBuilder;
import apidoc.codegenerator.CodeGenerator;
import apidoc.codegenerator.IEntryProvider;
import apidoc.codegenerator.TemplateProvider;
import apidoc.codegenerator.ios.builder.ModelBuilder;
import apidoc.codegenerator.ios.builder.ModelFieldBuilder;
import apidoc.codegenerator.model.EntryFieldModel;
import apidoc.codegenerator.provider.ProviderFactory;

public class ModelCodeGenerator extends CodeGenerator{
	
	private static final String FILE_FIELD_TEMPLATE = "Model_Field_template";
	private static final String FILE_MODEL_TEMPLATE = "Model_template";
	private static final String FILE_CODE_TEMPLATE = "Code_File_template";
	private static final String IOS_CODE_DIR = "iosCodes";
	
	private static String sFieldTemplate;
	private static String sModelTemplate;
	private static String sCodeTemplate;
	
	static{
		ModelTemplateProvider resourceTemplateProvider = new ModelTemplateProvider();
		try {
			sFieldTemplate = resourceTemplateProvider.provideTemplateForName(FILE_FIELD_TEMPLATE);
			sModelTemplate = resourceTemplateProvider.provideTemplateForName(FILE_MODEL_TEMPLATE);
			sCodeTemplate = TemplateProvider.provideTemplateForName(FILE_CODE_TEMPLATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ModelCodeGenerator(ResponseNode responseNode) {
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
		for (EntryFieldModel entryFieldModel : entryFields) {
			ModelFieldBuilder fieldBuilder = new ModelFieldBuilder(sFieldTemplate, entryFieldModel);
			fieldStrings.append(fieldBuilder.build());
		}
		ModelBuilder modelBuilder = new ModelBuilder(sModelTemplate, className, fieldStrings.toString());
		return modelBuilder.build();
	}

	@Override
	public String getRelativeCodeDir() {
		return IOS_CODE_DIR;
	}

	@Override
	public String getCodeTemplate() {
		return sCodeTemplate;
	}
}
