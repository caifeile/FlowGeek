package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by thanatos on 2/25/16.
 */
@Root(name = "oschina")
public class RespResult {

    @Element(name = "result", required = false)
    private RespUser.Result result;

    @Element(name = "notice", required = false)
    private Notice notice;

    @Element(name = "comment", required = false)
    private Comment comment;

    @Element(name = "relation", required = false)
    private int relation;

    public RespUser.Result getResult() {
        return result;
    }

    public void setResult(RespUser.Result result) {
        this.result = result;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
