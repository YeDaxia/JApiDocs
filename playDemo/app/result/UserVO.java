package result;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class UserVO extends SimpleUser {

    private SimpleUser[] friends; //好友
    private List<BookVO> readBooks; //阅读图书
    private Boolean isFollow; //是否关注

    public SimpleUser[] getFriends() {
        return friends;
    }

    public void setFriends(UserVO[] friends) {
        this.friends = friends;
    }

    public List<BookVO> getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(List<BookVO> readBooks) {
        this.readBooks = readBooks;
    }

    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }

    public static class BookVO{
        private String bookId; //图书id
        private String bookName;//图书名称;

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }
    }
}
