package controllers;

import apidoc.ApiResult;
import play.mvc.Controller;
import result.Book;
import result.User;

/**
 * 
 * @Description 用户接口
 *
 */
public class UserController extends Controller {
	
	/**
	 * @Description 保存用户信息
	 * @param name 用户名
	 * @param avatar 用户头像
	 * @author yedaxia
	 */
	@ApiResult(name=User.class)
	public static void saveUser(String name,String avatar){
		
	}
}
