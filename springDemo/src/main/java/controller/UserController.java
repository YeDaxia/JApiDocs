package controller;

import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.web.bind.annotation.*;
import result.book.BookDetailVO;
import result.user.UserVO;

/**
 * 用户接口
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/v1")
@RestController
public class UserController {

    /**
     * 用户列表
     * @param page 页数
     * @param pageSize 每页条数
     */
    @ApiDoc(UserVO[].class)
    @RequestMapping(path = "/u/list", method = {RequestMethod.GET,  RequestMethod.OPTIONS}  )
    public void list(Integer page, int pageSize){

    }

    /**
     * 用户信息
     * @param userId 用户id
     */
    @ApiDoc(UserVO.class)
    @GetMapping(path = "/u/info")
    public void userInfo(@RequestParam String userId){

    }

    /**
     * 收藏图书
     * @param bookId 图书id
     */
    @ApiDoc(result = BookDetailVO.class)
    @PostMapping(path = "/u/favorite")
    public void favoriteBook(@RequestParam String bookId){

    }
}
