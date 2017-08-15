package io.github.yedaxia.apidocs;

import io.github.yedaxia.apidocs.parser.ResponseNode;

import java.util.Map;

/**
 * wrap response into a common structure, you should put the response into a map ,
 *
 * for now this just use for upload apis to rap.
 *
 * default is :{
 *     code : 0,
 *     data: ${response}
 *     msg: 'success'
 * }
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface IResponseWrapper {

    /**
     * to wrap response , don't forget to put responseNode into map.
     *
     * @param responseNode
     */
    Map<String, Object> wrapResponse(ResponseNode responseNode);

}
