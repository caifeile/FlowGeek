package org.thanatos.flowgeek.bean;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

/**
 * 软件实体类
 * 
 */
@Root(name = "software")
public class Software extends Entity {

	@Element(name = "id", required = false)
	private Long id;

	@Element(name = "title")
	private String title;
	
	@Element(name = "author", required = false)
	private String author;
	
	@Element(name = "authorid", required = false)
	private int authorId;
	
	@Element(name = "recommended")
	private int recommended;
	
	@Element(name = "extensionTitle", required = false)
	private String extensionTitle;

	@Element(name = "license")
	private String license;

	@Element(name = "body")
	private String body;

	@Element(name = "homepage")
	private String homepage;

	@Element(name = "document")
	private String document;

	@Element(name = "download")
	private String download;

	@Element(name = "logo")
	private String logo;

	@Element(name = "language")
	private String language;

	@Element(name = "os")
	private String os;

	@Element(name = "recordtime")
	private String recordTime;

	@Element(name = "url")
	private String url;

	@Element(name = "favorite")
	private int favorite;

	@Element(name = "tweetCount")
	private int tweetCount;

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

	public int getRecommended() {
		return recommended;
	}

	public void setRecommended(int recommended) {
		this.recommended = recommended;
	}

	public String getExtensionTitle() {
		return extensionTitle;
	}

	public void setExtensionTitle(String extensionTitle) {
		this.extensionTitle = extensionTitle;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public int getTweetCount() {
		return tweetCount;
	}

	public void setTweetCount(int tweetCount) {
		this.tweetCount = tweetCount;
	}
}
