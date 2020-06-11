package controller;

import form.PageForm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import result.ApiResult;
import result.PageResult;
import result.book.BookDetailVO;
import result.book.BookVO;

/**
 * 图书接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/book/")
@RestController
public class BookController {

    /**
     * 图书列表
     * @param pageForm
     * @return
     */
    @GetMapping("list")
    public ApiResult<PageResult<BookVO>> getBookList(PageForm pageForm){
        return null;
    }

    /**
     * 图书详情
     * @param bookId 图书ID
     * @return
     */
    @GetMapping("book-detail")
    public ApiResult<BookDetailVO> getBookDetail(@RequestParam Long bookId){
        return null;
    }
}
