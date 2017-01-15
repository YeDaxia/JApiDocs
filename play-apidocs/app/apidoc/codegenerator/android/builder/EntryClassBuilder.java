package apidoc.codegenerator.android.builder;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class EntryClassBuilder implements CodeBuilder {

    private String className;
    private String mFieldCode;
    private String mMethodCode;
    private String entryClassTemplate;

    public EntryClassBuilder(String entryClassTemplate, String className, String mFieldCode, String mMethodCode) {
        this.className = className;
        this.mFieldCode = mFieldCode;
        this.mMethodCode = mMethodCode;
        this.entryClassTemplate = entryClassTemplate;
    }

    @Override
    public String builtString() {
        entryClassTemplate = entryClassTemplate.replace("${CLASS_NAME}",className);
        entryClassTemplate = entryClassTemplate.replace("${FIELDS}",mFieldCode);
        entryClassTemplate = entryClassTemplate.replace("${METHODS}",mMethodCode);
        return entryClassTemplate;
    }

    @Override
    public String getKey() {
        return null;
    }
}
