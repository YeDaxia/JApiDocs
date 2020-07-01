package result;

import java.math.BigDecimal;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class BookVO {
    private Long bookId; //图书id
    private String bookName; //图书名称
    private BookPrice price;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public static class BookPrice{
        private BigDecimal price; // 价格
        private Integer country; //国家
    }
}
