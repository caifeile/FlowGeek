package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanatos on 15/12/28.
 */
@Root(name = "tweet")
public class Tweet extends Entity {

    @Element(name = "id", required = false)
    private Long id;

    @Element(name = "portrait", required = false)
    private String portrait;

    @Element(name = "author", required = false)
    private String author;

    @Element(name = "authorid", required = false)
    private int authorId;

    @Element(name = "body", required = false)
    private String body;

    @Element(name = "appclient", required = false)
    private int appClient;

    @Element(name = "commentCount", required = false)
    private String cmmCount;

    @Element(name = "pubDate", required = false)
    private String pubDate;

    @Element(name = "imgSmall", required = false)
    private String imgSmall;

    @Element(name = "imgBig", required = false)
    private String imgBig;

    @Element(name = "attach", required = false)
    private String attach;

    @Element(name = "likeCount", required = false)
    private int likeCount;

    @Element(name = "isLike", required = false)
    private int isLike;

    @ElementList(name = "likeList", required = false)
    private List<User> likeUser = new ArrayList<User>();

    private String imageFilePath;
    private String audioPath;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAppClient() {
        return appClient;
    }

    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }

    public String getCmmCount() {
        return cmmCount;
    }

    public void setCmmCount(String cmmCount) {
        this.cmmCount = cmmCount;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public List<User> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<User> likeUser) {
        this.likeUser = likeUser;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
