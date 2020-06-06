package controller;

import form.AdminForm;
import form.ModelForm;
import form.UserForm;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.web.bind.annotation.*;
import result.book.BookDetailVO;
import result.user.Result;
import result.user.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/v1")
@RestController
public class UserController {

    /**
     * 关注用户
     *
     * @param user 用户
     */
    @ApiDoc(UserVO.class)
    @RequestMapping(path = "/u/follow", method = {RequestMethod.POST}  )
    public void follow(HttpServletRequest request, UserForm<String, AdminForm> user){

    }

    /**
     * 用户列表
     * @param page 页数
     * @param pageSize 每页条数
     */
    @ApiDoc(UserVO[].class)
    @RequestMapping(path = "/u/list", method = {RequestMethod.GET,  RequestMethod.OPTIONS}  )
    public void list(@RequestParam(value="page", required = true) Integer page, Integer pageSize){

    }

    /**
     * 用户信息
     * @param userId 用户id
     */
    @ApiDoc(UserVO.class)
    @GetMapping(path = "/u/info/{userId}")
    public void userInfo(@PathVariable String userId){

    }

    /**
     * 收藏图书
     * @param bookId 图书id
     */
    @ApiDoc(result = BookDetailVO.class)
    @PostMapping(path = "/u/favorite")
    public void favoriteBook(@RequestParam String bookId){

    }

    /**
     * 保存用户
     * @param userForm
     */
    @ApiDoc(result = BookDetailVO.class)
    @PostMapping(path = "/u/saveUser")
    public void saveUser(@RequestBody UserForm<String, ModelForm<String>> userForm){

    }

    /**
     * 获取用户列表2
     * @param page 页数
     * @return
     */
    @ApiDoc
    @PostMapping(path = "/u/list-new")
    public Result<String, UserVO> getUserList(Integer page){
        return null;
    }

    /**
     * 获取用户列表[无类型信息]
     * @param page 页数
     * @return
     */
    @ApiDoc
    @PostMapping(path = "/u/list-new-notype")
    public Result getUserList3(Integer page){
        return null;
    }

    /**
     * 消息列表
     * @return
     */
    @ApiDoc
    @PostMapping(path = "/u/message-list")
    public String[] getMessage(){
        return null;
    }

}
