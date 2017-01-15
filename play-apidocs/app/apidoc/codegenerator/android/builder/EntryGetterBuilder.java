package apidoc.codegenerator.android.builder;

import apidoc.codegenerator.model.EntryFieldModel;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryGetterBuilder implements CodeBuilder {

    private String getterTemplate;
    private EntryFieldModel entryFieldModel;

    public EntryGetterBuilder(String getterTemplate, EntryFieldModel entryFieldModel) {
        this.getterTemplate = getterTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String builtString() {
        getterTemplate = getterTemplate.replace("${CASE_FIELD_NAME}",entryFieldModel.getCaseFieldName());
        getterTemplate = getterTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
        getterTemplate = getterTemplate.replace("${FIELD_TYPE}",entryFieldModel.getFieldType());
        getterTemplate = getterTemplate.replace("${REMOTE_FIELD_NAME}",entryFieldModel.getRemoteFieldName());
        return getterTemplate + "\n";
    }

    @Override
    public String getKey() {
        return null;
    }
}
