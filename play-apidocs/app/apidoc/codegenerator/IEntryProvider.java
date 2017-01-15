package apidoc.codegenerator;

import java.util.List;

import apidoc.RequestNode;
import apidoc.ResponseBuilder;
import apidoc.ResponseNode;
import apidoc.codegenerator.model.EntryFieldModel;

public interface IEntryProvider {
	/**
	 *
	 * @param entryDictText
	 * @return
	 */
	List<EntryFieldModel> provideEntryFields(ResponseNode respNode);
}
