package io.github.yedaxia.apidocs.codegenerator;

import io.github.yedaxia.apidocs.parser.ClassNode;
import io.github.yedaxia.apidocs.parser.ResponseNode;
import io.github.yedaxia.apidocs.codegenerator.model.FieldModel;

import java.util.List;

public interface IFieldProvider {
	/**
	 * get response fields
	 * @param respNode
	 * @return
	 */
	List<FieldModel> provideFields(ClassNode respNode);
}
