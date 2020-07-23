package result;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class Student implements Serializable {

    private Long id;
    private String name;
    private Object value;
    private YStudent yStu;
    private InnerStudent innerStudent;

    private List<Student> reStuList;

    public static class InnerStudent{
        private String school;
    }
}
