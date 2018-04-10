package common;

import java.io.Serializable;

public class Article implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String title;
	private final String author;
	private final String content;
	
	public Article(String title, String author, String content) {
		super();
		this.title = title;
		this.author = author;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

}
