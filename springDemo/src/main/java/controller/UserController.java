package controller;

import form.UserForm;
import form.UserListForm;
import org.springframework.web.bind.annotation.*;
import result.ApiResult;
import result.PageResult;
import result.user.UserVO;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/user/")
@RestController
public class UserController {


    /**
     * 用户列表
     * @param listForm
     */
    @RequestMapping(path = "list", method = {RequestMethod.GET,  RequestMethod.POST}  )
    public ApiResult<PageResult<UserVO>> list(UserListForm listForm){
        return null;
    }

    /**
     * 用户信息
     * @param userId 用户id
     */
    @GetMapping("user-info/{userId}")
    public ApiResult<UserVO> userInfo(@PathVariable Long userId){
        return null;
    }

    /**
     * 保存用户
     * @param userForm
     */
    @PostMapping(path = "save")
    public ApiResult<UserVO> saveUser(@RequestBody UserForm userForm){
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
}
