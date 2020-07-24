package result;

import java.io.Serializable;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class BaseResult<T extends Serializable> implements Serializable {

    private T body;

}
