package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import org.thanatos.base.domain.Entity;
import org.thanatos.flowgeek.convert.IntegerConvert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻、软件、帖子、博客实体类
 */
@Root(name = "news")
public class News extends Entity {

    public final static int NEWS_TYPE_NEWS = 0x00;//0 新闻
    public final static int NEWS_TYPE_SOFTWARE = 0x01;//1 软件
    public final static int NEWS_TYPE_POST = 0x02;//2 帖子
    public final static int NEWS_TYPE_BLOG = 0x03;//3 博客

    @Element(name = "id", required = false)
    private Long id;

    @Element(name = "title")
    private String title;

    @Element(name = "url", required = false)
    private String url;

    @Element(name = "body")
    private String body;

    @Element(name = "author")
    private String author;

    @Element(name = "authorid")
    private int authorId;

    @Element(name = "commentCount")
    private int commentCount;

    @Element(name = "pubDate")
    private String pubDate;

    @Element(name = "softwarelink", required = false)
    private String softwareLink;

    @Element(name = "softwarename", required = false)
    private String softwareName;

    @Element(name = "favorite", required = false)
    private int favorite;

    @Element(name = "newstype", required = false)
    private NewsType newsType;

    @ElementList(name = "relativies", required = false)
    private List<Relative> relatives = new ArrayList<Relative>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    private String authorPortrait;

    public String getAuthorPortrait() {
        return authorPortrait;
    }

    public void setAuthorPortrait(String authorPortrait) {
        this.authorPortrait = authorPortrait;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSoftwareLink() {
        return softwareLink;
    }

    public void setSoftwareLink(String softwareLink) {
        this.softwareLink = softwareLink;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public NewsType getNewType() {
        return newsType;
    }

    public void setNewType(NewsType newType) {
        this.newsType = newType;
    }

    public List<Relative> getRelatives() {
        return relatives;
    }

    public void setRelatives(List<Relative> relatives) {
        this.relatives = relatives;
    }

    /**
     * 文章类型,决定用何种方式打开这个文章
     */
    @Root(name = "newstype")
    public static class NewsType implements Serializable{
        @Element(name = "type")
        private int type;

        @Element(name = "attachment", required = false)
        private String attachment;

        @Element(name = "authoruid2", required = false)
        @Convert(IntegerConvert.class)
        private Integer authorUid;

        @Element(name = "eventurl", required = false)
        private String eventUrl;

        public String getEventUrl() {
            return eventUrl;
        }

        public void setEventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public Integer getAuthorUid() {
            return authorUid;
        }

        public void setAuthorUid(Integer authorUid) {
            this.authorUid = authorUid;
        }
    }

    /**
     * 相关文章
     */
    @Root(name = "relative")
    public static class Relative implements Serializable{

        @Element(name = "rtitle")
        public String title;

        @Element(name = "rurl")
        public String url;

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
    }
}