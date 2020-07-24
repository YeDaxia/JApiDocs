package result;

import java.io.Serializable;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ApiResult<T extends Serializable> extends BaseResult<T>{

    private Integer code;
    private String errMsg;
    private T data;
    private Type errType;

    /**
     * 状态类型
     */
    public enum Type
    {
        /** 成功 */
        SUCCESS(0),
        /** 警告 */
        WARN(301),
        /** 错误 */
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}
