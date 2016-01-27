package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

/**
 * Created by thanatos on 15/12/28.
 */
@Root(name = "user")
public class User extends Entity {

    public final static int RELATION_ACTION_DELETE = 0x00;// 取消关注
    public final static int RELATION_ACTION_ADD = 0x01;// 加关注
    public final static int RELATION_TYPE_BOTH = 0x01;// 双方互为粉丝
    public final static int RELATION_TYPE_FANS_HIM = 0x02;// 你单方面关注他
    public final static int RELATION_TYPE_NULL = 0x03;// 互不关注
    public final static int RELATION_TYPE_FANS_ME = 0x04;// 只有他关注我

    @Element(name = "id", required = false)
    private Long id;

    @Element(name = "uid")
    private long uid;

    @Element(name = "location", required = false)
    private String location;

    @Element(name = "name")
    private String name;

    @Element(name = "followers", required = false)
    private int followers;

    @Element(name = "fans", required = false)
    private int fans;

    @Element(name = "score", required = false)
    private int score;

    @Element(name = "portrait", required = false)
    private String portrait;

    @Element(name = "jointime", required = false)
    private String joinTime;

    @Element(name = "gender", required = false)
    private String gender;

    @Element(name = "devplatform", required = false)
    private String devPlatform;

    @Element(name = "expertise", required = false)
    private String expertise;

    @Element(name = "relation", required = false)
    private int relation;

    @Element(name = "latestonline", required = false)
    private String latestOnline;

    @Element(name = "from", required = false)
    private String from;

    @Element(name = "favoritecount", required = false)
    private int favoriteCount;

    private String account;

    private String pwd;

    private boolean isRememberMe;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        setId(uid);
        this.uid = uid;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDevPlatform() {
        return devPlatform;
    }

    public void setDevPlatform(String devPlatform) {
        this.devPlatform = devPlatform;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getLatestOnline() {
        return latestOnline;
    }

    public void setLatestOnline(String latestOnline) {
        this.latestOnline = latestOnline;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public void setIsRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }
}
