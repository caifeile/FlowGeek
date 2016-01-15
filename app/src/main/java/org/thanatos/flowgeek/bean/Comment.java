package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
@Root(name = "comment")
public class Comment extends Entity{

    @Element(name = "portrait")
    private String portrait;

    @Element(name = "content")
    private String content;

    @Element(name = "author")
    private String author;

    @Element(name = "authorid")
    private int authorId;

    @Element(name = "pubDate")
    private String pubDate;

    @Element(name = "appclient", required = false)
    private int appClient;

    @Element(name = "replies", required = false)
    private List<Reply> replies = new ArrayList<>();

    @Element(name = "refers", required = false)
    private List<Refer> refers = new ArrayList<>();

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getAppClient() {
        return appClient;
    }

    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public List<Refer> getRefers() {
        return refers;
    }

    public void setRefers(List<Refer> refers) {
        this.refers = refers;
    }

    @Root(name = "reply")
    private class Reply extends Entity{

        @Element(name ="rauthor")
        public String author;

        @Element(name ="rpubDate")
        public String pubDate;

        @Element(name ="rcontent")
        public String content;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Root(name = "refer")
    private class Refer extends Entity{

        @Element(name ="refertitle")
        public String title;

        @Element(name ="referbody")
        public String body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
