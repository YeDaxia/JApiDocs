package result;

import java.util.List;

public class Book {
	public Long id; //书的id
	public String bookName; //书的名称
	public Author author; //作者
	public List<Comment> comments; //评论列表
}
