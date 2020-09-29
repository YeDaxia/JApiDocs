package controller;

import enums.UserType;
import form.GenericForm;
import form.PageForm;
import form.UserForm;
import form.UserListForm;
import io.github.yedaxia.apidocs.ApiDoc;
import io.github.yedaxia.apidocs.Ignore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import result.ApiResult;
import result.PageResult;
import result.user.UserVO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户接口
 * @author yeguozhong
 */
@RequestMapping("/api/user/")
@RestController
public class UserController {


    /**
     * 用户列表
     * @description 这是一行说明
     * @param listForm
     * @author yedaxia
     */
    @RequestMapping(path = "list", method = {RequestMethod.GET,  RequestMethod.POST}  )
    public ApiResult<PageResult<UserVO>> list(UserListForm listForm){
        return null;
    }

    /**
     * 用户信息
     * @param userId 用户id
     * @param userForm
     * @author 周杰伦
     */
    @GetMapping("user-info/{userId}")
    public ApiResult<UserVO> userInfo(@PathVariable Long userId, @RequestBody UserForm userForm){
        return null;
    }

    /**
     * 保存用户
     * @param req
     * @param userForm
     * @param session
     * @return
     */
    @PostMapping(path = "save")
    public ApiResult<UserVO> saveUser(HttpServletResponse req, @RequestBody UserForm userForm, HttpSession session){
        return null;
    }

    /**
     * 上传头像
     *
     * @param avatar
     * @return
     */
    @PostMapping("upload-avatar")
    public ApiResult uploadAvatar(MultipartFile avatar){
        return null;
    }

    /**
     * 修改用户信息
     * @param userForm
     * @return
     */
    @PostMapping("modify")
    public ApiResult<UserVO> modifyUser(UserForm userForm){
        return null;
    }

    /**
     * 删除用户
     * @param userId 用户ID
     */
    @PostMapping("delete")
    public ApiResult deleteUser(@RequestParam Long userId){
        return null;
    }

    public ApiResult hello(){
        return null;
    }

    /**
     * 获取图片
     */
    @GetMapping("get-image")
    public void getImage(){

    }

    /**
     * 用户列表2
     *
     * @param userId 用户ID
     * @param user
     * @return
     */
    @GetMapping("list2")
    public ApiResult<ArrayList<UserVO>> list2(@RequestParam Long userId, @RequestBody UserForm user){
        return null;
    }

    /**
     * 用户列表3
     * @param pageForm
     * @return
     */
    @GetMapping("list3")
    public List<UserVO> list3(PageForm pageForm){
        return null;
    }


    /**
     * List测试
     * @param ids 用户id
     * @return
     */
    @GetMapping("list-by-ids")
    public ApiResult getUserList(List<Long> ids){
        return null;
    }

    /**
     * 枚举参数测试
     *
     * @param userType
     * @return
     */
    @GetMapping("getByUserType")
    public ApiResult getByUserType(UserType userType){
        return null;
    }

    /**
     * 忽略该接口
     * @return
     */
    @Ignore
    @PostMapping("ignore")
    public ApiResult ignore(){
        return null;
    }

    /**
     * 字符串结果
     * @return
     */
    @ApiDoc(stringResult = "{code: 0, data: 'success'}")
    @GetMapping(value = "custom-json")
    public ApiResult customJsonResult(){
        return null;
    }

    /**
     * 泛型参数
     * @param user
     * @return
     */
    @GetMapping(value = "generic-form")
    public ApiResult testGenericForm(@RequestBody GenericForm<UserForm> user){
        return null;
    }
}
