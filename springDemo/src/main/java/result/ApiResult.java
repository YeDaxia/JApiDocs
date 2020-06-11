package result;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ApiResult<T> {
    private Integer code;
    private String errMsg;
    private T data;
}
