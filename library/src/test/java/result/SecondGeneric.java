package result;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class SecondGeneric<M> {

    private List<M> model;

    public List<M> getModel() {
        return model;
    }

    public void setModel(List<M> model) {
        this.model = model;
    }
}
