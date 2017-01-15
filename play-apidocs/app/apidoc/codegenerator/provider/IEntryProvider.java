package apidoc.codegenerator.provider;
import java.util.List;

import apidoc.codegenerator.model.EntryFieldModel;

/**
 * Created by user on 2016/12/25.
 */
@Deprecated
public interface IEntryProvider {

    /**
     *
     * @param entryDictText
     * @return
     */
    List<EntryFieldModel> provideEntryFields(String entryDictText);
}