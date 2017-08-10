package io.github.yedaxia.apidocs.doc;

import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.ResponseNode;

import java.io.File;
import java.io.IOException;

/**
 * an interface of build a controller api docs
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface IControllerDocBuilder {

    /**
     * build api docs and return as string
     *
     * @param controllerNode
     * @return
     */
    String buildDoc(ControllerNode controllerNode) throws IOException;

}
