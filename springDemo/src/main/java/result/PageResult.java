package result;

/**
 * @author yeguozhong yedaxia.github.com
 */

import java.util.List;

public class PageResult<T> {

    private Integer total; //总记录数
    private Integer pageCount; //页数
    private Integer currentPage; //当前页
    private Integer pageSize; //每页记录数
    private List<T> list; //列表数据
    private Boolean hasMore; //是否还有更多

}
