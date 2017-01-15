package apidoc.codegenerator.provider;
/**
 * Created by user on 2016/12/25.
 */
public class ProviderFactory {

    public static apidoc.codegenerator.IEntryProvider createProvider(){
        return new DocEntryProvider();
    }
}
