package controller;

import io.github.yedaxia.apidocs.ApiDoc;
import result.UserVO;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
public class UserApi {

    /**
     * 用户信息
     * @param userId 用户ID
     */
    @ApiDoc(result = UserVO.class, url="/api/u/userInfo", method = "get")
    public void userInfo(String userId){

    }

}
