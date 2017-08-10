package io.github.yedaxia.apidocs.codegenerator.ios.builder;

import io.github.yedaxia.apidocs.codegenerator.ICodeBuilder;
import io.github.yedaxia.apidocs.codegenerator.model.FieldModel;

public class ModelFieldBuilder implements ICodeBuilder{

	private String modelFieldTemplate;
	private FieldModel entryFieldModel;
	
	public ModelFieldBuilder(String modelFieldTemplate, FieldModel entryFieldModel) {
		super();
		this.modelFieldTemplate = modelFieldTemplate;
		this.entryFieldModel = entryFieldModel;
	}

	@Override
	public String build(){
		modelFieldTemplate = modelFieldTemplate.replace("${FIELD_TYPE}",entryFieldModel.getIFieldType());
		modelFieldTemplate = modelFieldTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
		modelFieldTemplate = modelFieldTemplate.replace("${COMMENT}", entryFieldModel.getComment());
		modelFieldTemplate = modelFieldTemplate.replace("${ASSIGN}", entryFieldModel.getAssign());
	    return modelFieldTemplate + "\n";
	}
}
