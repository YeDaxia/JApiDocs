package result;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ResultVO extends SimpleResult{

    /**
     * 性别
     */
    private Gender gender;

    private Inner inner; //内部类

    public enum  Gender {
        MALE, FEMALE
    }

    static class Inner {
        private String a;
    }
}
