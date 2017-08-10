package result.user;

import result.book.BookDetailVO;
import result.book.BookVO;

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

    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }

    public void setFriends(SimpleUser[] friends) {
        this.friends = friends;
    }

    public List<BookVO> getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(List<BookVO> readBooks) {
        this.readBooks = readBooks;
    }
}
