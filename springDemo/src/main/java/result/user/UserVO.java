package result.user;

import io.github.yedaxia.apidocs.RapMock;
import result.book.BookVO;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class UserVO extends SimpleUser {

    @RapMock(limit = "1-10")
    private SimpleUser[] friends; //好友

    private List<BookVO> readBooks; //阅读图书

    private Boolean isFollow; //是否关注

    private List<UserVO> follower;

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
