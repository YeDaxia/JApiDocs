package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import result.Book;

import java.util.*;

import apidoc.ApiResult;
import models.*;

/**
 * 
 *@Description 主接口
 */
public class BookController extends Controller {

    /**
     * @Description 获取书的列表
     * @param page 页码
     * @param pageSize 每页数量
     * @author yedaxia
     */
    @ApiResult(name=Book[].class)
    public static void getBookList(@Required int page, int pageSize){
    	
    }
    
    /**
     * @Description 保存书的信息
     * @param name 书名
     * @param author 作者
     * @author yedaxia
     */
    @ApiResult(name=Book.class)
    public static void saveBook(String name,String author){
    	
    }
}