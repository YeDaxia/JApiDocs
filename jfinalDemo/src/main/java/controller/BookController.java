package controller;

import com.jfinal.core.Controller;
import io.github.yedaxia.apidocs.ApiDoc;
import result.BookVO;
import result.UserVO;

/**
 * 图书接口
 * @author yeguozhong yedaxia.github.com
 */
public class BookController extends Controller{


    /**
     * 图书列表
     * @param page 第几页
     * @param pageSize 每页条数
     */
    @ApiDoc(BookVO[].class)
    public void bookList(){

    }

}
