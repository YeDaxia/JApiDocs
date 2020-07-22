package result;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ResultVO<T> extends SimpleResult<Student>{

    /**
     * 性别
     */
    private Gender gender;

    private Inner inner; //内部类

    private String[] stringArray;

    private List pList;

    private List<T> list;

    private List<? extends T> zlist;

    private List<GenericResult<Student, Integer>> stuList;

    private T body;

    private GenericResult<Student, Student> result;

    private GenericResult<Student, Integer>[] resultArray;

    public enum  Gender {
        MALE, FEMALE
    }

    static class Inner {
        private String a;
    }
}
