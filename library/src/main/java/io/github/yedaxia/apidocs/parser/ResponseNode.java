package io.github.yedaxia.apidocs.parser;

import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * response node
 *
 * @author yeguozhong yedaxia.github.com
 */
public class ResponseNode extends ClassNode {

    private RequestNode requestNode;

    public RequestNode getRequestNode() {
        return requestNode;
    }

    public void setRequestNode(RequestNode requestNode) {
        this.requestNode = requestNode;
    }

}
