package controller.admin;

import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import result.user.AdminVO;

/**
 * 管理员接口
 * @author yeguozhong yedaxia.github.com
 */
@Controller
public class AdminController {

    /**
     * 管理员登录
     * @param name 登录名
     * @param password 密码
     */
    @RequestMapping(path = "/api/v1/admin/login", method = RequestMethod.POST)
    @ApiDoc(AdminVO.class)
    public void login(@RequestParam String name, @RequestParam String password){

    }

    /**
     * 邮箱登录
     * @param email
     * @param password
     */
    @RequestMapping(path = "/api/v1/admin/emailLogin", method = RequestMethod.POST)
    @ApiDoc(AdminVO.class)
    @Deprecated
    public void emailLogin(@RequestParam String email, String password){

    }

}
