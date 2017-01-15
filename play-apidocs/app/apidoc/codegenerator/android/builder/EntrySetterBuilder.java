package apidoc.codegenerator.android.builder;

import apidoc.codegenerator.model.EntryFieldModel;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntrySetterBuilder implements CodeBuilder {

    private String setterTemplate;
    private EntryFieldModel entryFieldModel;

    public EntrySetterBuilder(String setterTemplate, EntryFieldModel entryFieldModel){
        this.setterTemplate = setterTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String builtString() {
        setterTemplate = setterTemplate.replace("${REMOTE_FIELD_NAME}",entryFieldModel.getRemoteFieldName());
        setterTemplate = setterTemplate.replace("${CASE_FIELD_NAME}",entryFieldModel.getCaseFieldName());
        setterTemplate = setterTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
        setterTemplate = setterTemplate.replace("${FIELD_TYPE}",entryFieldModel.getFieldType());
        return setterTemplate + "\n";
    }

    @Override
    public String getKey() {
        return null;
    }
}
