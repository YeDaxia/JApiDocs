package apidoc.codegenerator.android.builder;

import apidoc.codegenerator.model.EntryFieldModel;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryFieldBuilder implements CodeBuilder {

    private String fieldTemplate;
    private EntryFieldModel entryFieldModel;

    public EntryFieldBuilder(String fieldTemplate, EntryFieldModel entryFieldModel) {
        this.fieldTemplate = fieldTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String builtString() {
        fieldTemplate = fieldTemplate.replace("${FIELD_TYPE}",entryFieldModel.getFieldType());
        fieldTemplate = fieldTemplate.replace("${FIELD_NAME}",entryFieldModel.getFieldName());
        fieldTemplate = fieldTemplate.replace("${COMMENT}",entryFieldModel.getComment());
        return fieldTemplate + "\n";
    }

    @Override
    public String getKey() {
        return null;
    }
}
