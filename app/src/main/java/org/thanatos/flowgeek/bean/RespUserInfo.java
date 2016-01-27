package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by thanatos on 15/12/29.
 */
@Root(name = "oschina")
public class RespUserInfo implements Serializable{

    @Element(name = "user")
    private User user;

    @Element(name = "notice", required = false)
    private Notice notice;

    @Element(name = "pagesize", required = false)
    private int pageSize;

    @ElementList(name = "activies", required = false)
    private List<Active> actives;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Active> getActives() {
        return actives;
    }

    public void setActives(List<Active> actives) {
        this.actives = actives;
    }
}
