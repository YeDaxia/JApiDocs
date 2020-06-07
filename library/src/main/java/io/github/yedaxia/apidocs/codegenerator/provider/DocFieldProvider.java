package io.github.yedaxia.apidocs.codegenerator.provider;


import io.github.yedaxia.apidocs.parser.ClassNode;
import io.github.yedaxia.apidocs.parser.FieldNode;
import io.github.yedaxia.apidocs.parser.ResponseNode;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.codegenerator.IFieldProvider;
import io.github.yedaxia.apidocs.codegenerator.model.FieldModel;

import java.util.ArrayList;

import java.util.List;

public class DocFieldProvider implements IFieldProvider {

	@Override
	public List<FieldModel> provideFields(ClassNode respNode) {
		List<FieldNode>recordNodes = respNode.getChildNodes();
		if(recordNodes == null || recordNodes.isEmpty()){
			return null;
		}
		List<FieldModel> entryFieldList = new ArrayList<>();
		FieldModel entryField;
		for (FieldNode recordNode : recordNodes) {
			entryField = new FieldModel();
			String fieldName = DocFieldHelper.getPrefFieldName(recordNode.getName());
			entryField.setCaseFieldName(Utils.capitalize(fieldName));
			entryField.setFieldName(fieldName);
			entryField.setFieldType(DocFieldHelper.getPrefFieldType(recordNode.getType()));
			entryField.setRemoteFieldName(recordNode.getName());
			entryField.setComment(recordNode.getDescription());
			entryFieldList.add(entryField);
		}
		return entryFieldList;
	}
	
}
