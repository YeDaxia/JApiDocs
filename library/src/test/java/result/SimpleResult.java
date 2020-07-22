package result;

import io.github.yedaxia.apidocs.RapMock;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class SimpleResult<K> {

    @RapMock(value="@ID")
    private String id; // id

    private K name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
