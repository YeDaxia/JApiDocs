package apidoc.codegenerator.ios.builder;

import apidoc.codegenerator.model.EntryFieldModel;

public class ModelFieldBuilder {
	private String modelFieldTemplate;
	private EntryFieldModel entryFieldModel;
	
	public ModelFieldBuilder(String modelFieldTemplate, EntryFieldModel entryFieldModel) {
		super();
		this.modelFieldTemplate = modelFieldTemplate;
		this.entryFieldModel = entryFieldModel;
	}

	public String build(){
		modelFieldTemplate = modelFieldTemplate.replace("${FIELD_TYPE}",entryFieldModel.getIFieldType());
		modelFieldTemplate = modelFieldTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
		modelFieldTemplate = modelFieldTemplate.replace("${COMMENT}", entryFieldModel.getComment());
		modelFieldTemplate = modelFieldTemplate.replace("${ASSIGN}", entryFieldModel.getAssign());
	    return modelFieldTemplate + "\n";
	}
}
