package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by thanatos on 16/1/22.
 */
@Root(name = "oschina")
public class RespCmmList implements Serializable {

    public final static int CATALOG_NEWS = 1;
    public final static int CATALOG_POST = 2;
    public final static int CATALOG_TWEET = 3;
    public final static int CATALOG_ACTIVE = 4;
    public final static int CATALOG_MESSAGE = 4;// 动态与留言都属于消息中心

    @Element(name = "pagesize", required = false)
    private int pageSize;
    @Element(name = "allCount", required = false)
    private int allCount;
    @ElementList(name = "comments", required = false)
    private List<Comment> comments;

    public int getPageSize() {
        return pageSize;
    }

    public int getAllCount() {
        return allCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void sortList() {
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment lhs, Comment rhs) {
                return lhs.getPubDate().compareTo(rhs.getPubDate());
            }
        });
    }

}
