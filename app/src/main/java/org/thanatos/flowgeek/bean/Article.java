package org.thanatos.flowgeek.bean;

import org.thanatos.base.domain.Entity;

/**
 * *************************************************
 * <p>
 * 版权:HZX 版权所有©2015
 * <p>
 * 作者:HZX
 * <p>
 * 版本:1.0
 * <p>
 * 创建日期:2015/12/30   20:19
 * <p>
 * 描述:
 * <p>
 * 修订说明:
 * <p>
 * *************************************************
 */
public class Article<T> extends Entity {

    private String author;
    private String key;
    private Long authorId;
    private String portrait;
    private String title;
    private String body;
    private int favorite;
    private String pubDate;
    private int cmmCount;
    private T article;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getCmmCount() {
        return cmmCount;
    }

    public void setCmmCount(int cmmCount) {
        this.cmmCount = cmmCount;
    }

    public <O> O getArticle() {
        return (O) article;
    }

    public void setArticle(T article) {
        this.article = article;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
