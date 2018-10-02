package controllers.user;

import io.github.yedaxia.apidocs.ApiDoc;
import play.data.validation.Required;
import play.mvc.Controller;
import result.UserVO;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
public class UserApi extends Controller{

    /**
     * 用户列表
     * @param page 页数
     * @param pageSize 每页条数
     */
    @ApiDoc(UserVO[].class)
    public void list(Integer page, Integer pageSize){

    }

    /**
     * 用户信息
     * @param userId 用户id
     */
    @ApiDoc(UserVO.class)
    public void userInfo(@Required String userId){

    }
}
