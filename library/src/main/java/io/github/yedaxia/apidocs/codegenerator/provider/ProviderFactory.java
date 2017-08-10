package io.github.yedaxia.apidocs.codegenerator.provider;

import io.github.yedaxia.apidocs.codegenerator.IFieldProvider;

/**
 * Created by user on 2016/12/25.
 */
public class ProviderFactory {

    public static IFieldProvider createProvider(){
        return new DocFieldProvider();
    }
}
