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
 * 创建日期:2015/12/31   12:04
 * <p>
 * 描述:
 * <p>
 * 修订说明:
 * <p>
 * *************************************************
 */
@Root(name = "oschina")
public class RespPostDetail implements Serializable{

    @Element(name = "post")
    private Post post;

    @Element(name = "notice", required = false)
    private Notice notice;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
