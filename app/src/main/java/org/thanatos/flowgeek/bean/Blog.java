package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

/**
 * @author thanatos
 */
@Root(name = "blog")
public class Blog extends Entity {
	
	public final static int DOC_TYPE_REPASTE = 0; //转帖
	public final static int DOC_TYPE_ORIGINAL = 1; //原创

	@Element(name = "id")
	private Long id;

	@Element(name = "title")
	private String title;

	@Element(name = "url")
	private String url;

	@Element(name = "where")
	private String where;

	@Element(name = "commentCount")
	private int cmmCount;

	@Element(name = "body")
	private String body;

	@Element(name = "author")
	private String author;

	@Element(name = "authorid")
	private int authorId;

	@Element(name = "documentType")
	private int docType;

	@Element(name = "pubDate")
	private String pubDate;

	@Element(name = "favorite")
	private int favorite;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public int getCmmCount() {
		return cmmCount;
	}

	public void setCmmCount(int cmmCount) {
		this.cmmCount = cmmCount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getDocumenttype() {
		return docType;
	}

	public void setDocumenttype(int documenttype) {
		this.docType = documenttype;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
}
