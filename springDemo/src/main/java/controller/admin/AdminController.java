package controller.admin;

import form.AdminForm;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import result.user.AdminVO;

import java.util.List;

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

    /**
     * 添加管理员
     *
     * @param adminForm 管理员信息
     */
    @PostMapping("/api/v1/admin/add")
    @ApiDoc(AdminVO.class)
    public void addAdmin(@RequestBody(required = false) AdminForm adminForm){

    }

    /**
     * 添加多个管理员
     *
     * @param adminForms 管理员信息
     */
    @PostMapping("/api/v1/admin/addMany")
    @ApiDoc(AdminVO[].class)
    public void addAdmins(@RequestBody List<AdminForm> adminForms){

    }
}
