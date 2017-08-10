package controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import io.github.yedaxia.apidocs.ApiDoc;
import result.UserVO;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
public class UserController extends Controller{

    /**
     * 用户信息
     * @param userId 用户ID
     */
    @ApiDoc(UserVO.class)
    public void userInfo(){

    }
    /**
     * 用户列表
     * @param page 第几页
     * @param pageSize 每页条数
     */
    @ApiDoc(UserVO[].class)
    @ActionKey("/api/v1/user/list")
    public void list(){

    }
}
