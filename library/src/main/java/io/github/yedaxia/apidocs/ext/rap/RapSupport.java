package io.github.yedaxia.apidocs.ext.rap;

import io.github.yedaxia.apidocs.parser.ControllerNode;

import java.util.List;

/**
 * post request to rap
 *
 * @author yeguozhong yedaxia.github.com
 */
public class RapSupport {

    private int projectId; // project id in rap
    private List<ControllerNode> controllerNodeList;

    /**
     * do post
     */
    public void postToRap(){
        if(controllerNodeList == null || controllerNodeList.isEmpty()){
            return;
        }
    }
}
