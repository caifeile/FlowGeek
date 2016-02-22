package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanatos on 16/2/16.
 */
@Root(name = "oschina")
public class RespTweetList implements Serializable {

    public static final int TYPE_NEW = 0;
    public static final int TYPE_HOT = -1;


    @Element(name = "tweetCount", required = false)
    private int tweetCount;

    @Element(name = "pagesize", required = false)
    private int pageSize;

    @ElementList(name = "tweets", required = false)
    private ArrayList<Tweet> tweets;

    @Element(name = "notice", required = false)
    private Notice notice;

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
