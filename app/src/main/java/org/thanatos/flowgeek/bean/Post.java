package org.thanatos.flowgeek.bean;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子实体类
 * 
 */
@Root(name = "post")
public class Post extends Entity {

	public final static int CATALOG_ASK = 1;
	public final static int CATALOG_SHARE = 2;
	public final static int CATALOG_OTHER = 3;
	public final static int CATALOG_JOB = 4;
	public final static int CATALOG_SITE = 5;

	@Element(name = "id", required = false)
	private Long id;

	@Element(name = "title")
	private String title;
	
	@Element(name = "portrait", required = false)
	private String portrait;

	@Element(name = "url", required = false)
	private String url;

	@Element(name = "body")
	private String body;

	@Element(name = "author")
	private String author;

	@Element(name = "authorid")
	private int authorId;

	@Element(name = "answerCount")
	private int answerCount;

	@Element(name = "viewCount")
	private int viewCount;

	@Element(name = "pubDate")
	private String pubDate;

	@Element(name = "catalog", required = false)
	private int catalog;

	@Element(name = "isnoticeme", required = false)
	private int isNoticeMe;

	@Element(name = "favorite")
	private int favorite;

	@ElementList(name = "tags", required = false, entry = "tag")
	private List<String> tags;
	
	@Element(name = "answer", required = false)
	private Answer answer;
	
	@Element(name = "event", required = false)
	private Event event;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getIsNoticeMe() {
		return isNoticeMe;
	}

	public void setIsNoticeMe(int isNoticeMe) {
		this.isNoticeMe = isNoticeMe;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	@Element(name = "answer")
	public static class Answer implements Serializable {
		
		@Element(name = "name")
		private String name;
		
		@Element(name = "time")
		private String time;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

}
