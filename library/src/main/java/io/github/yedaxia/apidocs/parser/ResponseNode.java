package io.github.yedaxia.apidocs.parser;

import com.alibaba.fastjson.JSONObject;
import io.github.yedaxia.apidocs.Utils;

/**
 * response node
 *
 * @author yeguozhong yedaxia.github.com
 */
public class ResponseNode extends ClassNode {

    private RequestNode requestNode;

    private String stringResult;

    public RequestNode getRequestNode() {
        return requestNode;
    }

    public void setRequestNode(RequestNode requestNode) {
        this.requestNode = requestNode;
    }

    public String getStringResult() {
        return stringResult;
    }

    public void setStringResult(String stringResult) {
        this.stringResult = stringResult;
    }

    @Override
    public String toJsonApi() {
        if(stringResult != null){
            try{
                return Utils.toPrettyJson((JSONObject.parse(stringResult)));
            }catch (Exception ex){
                // do nothing
                return stringResult;
            }
        }
        return super.toJsonApi();
    }
}
