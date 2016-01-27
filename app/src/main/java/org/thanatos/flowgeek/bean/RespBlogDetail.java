package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * *************************************************
 * <p>
 * 版权:HZX 版权所有©2015
 * <p>
 * 作者:HZX
 * <p>
 * 版本:1.0
 * <p>
 * 创建日期:2015/12/30   19:46
 * <p>
 * 描述:
 * <p>
 * 修订说明:
 * <p>
 * *************************************************
 */
@Root(name = "oschina")
public class RespBlogDetail implements Serializable{

    @Element(name = "blog")
    private Blog blog;

    @Element(name = "notice", required = false)
    private Notice notice;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
