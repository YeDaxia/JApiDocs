package apidoc.codegenerator.provider;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import apidoc.RecordNode;
import apidoc.RequestNode;
import apidoc.ResponseBuilder;
import apidoc.ResponseNode;
import apidoc.codegenerator.IEntryProvider;
import apidoc.codegenerator.model.EntryFieldModel;

public class DocEntryProvider implements IEntryProvider{

	@Override
	public List<EntryFieldModel> provideEntryFields(ResponseNode respNode) {
		List<RecordNode>recordNodes = respNode.getChildNodes();
		if(recordNodes == null || recordNodes.isEmpty()){
			return null;
		}
		List<EntryFieldModel> entryFieldList = new ArrayList<>();
		EntryFieldModel entryField;
		for (RecordNode recordNode : recordNodes) {
			entryField = new EntryFieldModel();
			String fieldName = EntryFieldHelper.getPrefFieldName(recordNode.name);
			entryField.setCaseFieldName(StringUtils.capitalize(fieldName));
			entryField.setFieldName(fieldName);
			entryField.setFieldType(EntryFieldHelper.getPrefFieldType(recordNode.type));
			entryField.setRemoteFieldName(recordNode.name);
			entryField.setComment(recordNode.description);
			entryFieldList.add(entryField);
		}
		return entryFieldList;
	}
	
}
