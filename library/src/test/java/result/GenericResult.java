package result;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class GenericResult<V, M> {

    private V object;
    private SecondGeneric<M> secondGeneric;
    private Integer code;

    public List<V> getList(){
        return null;
    }

    public V getObject() {
        return object;
    }

    public void setObject(V object) {
        this.object = object;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public SecondGeneric<M> getSecondGeneric() {
        return secondGeneric;
    }

    public void setSecondGeneric(SecondGeneric<M> secondGeneric) {
        this.secondGeneric = secondGeneric;
    }
}
