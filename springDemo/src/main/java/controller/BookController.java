package controller;

import form.PageForm;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import result.ApiResult;
import result.PageResult;
import result.book.BookDetailVO;
import result.book.BookVO;

import javax.validation.constraints.NotNull;
import java.util.List;

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
     * @param id 图书ID
     * @return
     */
    @GetMapping("book-detail")
    public ApiResult<BookDetailVO> getBookDetail(@RequestParam Long id){
        return null;
    }

    /**
     * 删除图书
     * @param bookId 图书ID
     * @return
     */
    @GetMapping("del-book")
    public ApiResult<BookVO> delBook(@NotNull Long bookId){
        return null;
    }

    /**
     * 批量删除图书
     * @param bookIds
     * @return
     */
    @DeleteMapping("del-books")
    public ApiResult deleteBooks(List<Long> bookIds){
        return null;
    }

    /**
     * 购买图书
     *
     * @param bookId
     * @return
     */
    @PostMapping("buy-book")
    public ResponseEntity<HttpEntity<BookVO>> buyBook(Long bookId){
        return null;
    }
}
