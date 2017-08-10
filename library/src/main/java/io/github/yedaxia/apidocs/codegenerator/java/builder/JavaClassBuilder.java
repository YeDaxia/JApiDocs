package io.github.yedaxia.apidocs.codegenerator.java.builder;

import io.github.yedaxia.apidocs.codegenerator.ICodeBuilder;

/**
 * Created by Darcy https://yedaxia.github.io/
 */
public class JavaClassBuilder implements ICodeBuilder {

    private String className;
    private String mFieldCode;
    private String mMethodCode;
    private String entryClassTemplate;

    public JavaClassBuilder(String entryClassTemplate, String className, String mFieldCode, String mMethodCode) {
        this.className = className;
        this.mFieldCode = mFieldCode;
        this.mMethodCode = mMethodCode;
        this.entryClassTemplate = entryClassTemplate;
    }

    @Override
    public String build() {
        entryClassTemplate = entryClassTemplate.replace("${CLASS_NAME}",className);
        entryClassTemplate = entryClassTemplate.replace("${FIELDS}",mFieldCode);
        entryClassTemplate = entryClassTemplate.replace("${METHODS}",mMethodCode);
        return entryClassTemplate;
    }
}
