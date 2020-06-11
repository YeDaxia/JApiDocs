package form;

import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class UserListForm extends PageForm{
    private Integer status; //用户状态
    @NotNull
    private String name; //用户名
}
