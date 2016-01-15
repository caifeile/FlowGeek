package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by thanatos on 15/12/28.
 */
@Root(name = "oschina")
public class RespNewsDetail {

    @Element(name = "news")
    private News news;

    @Element(name = "notice", required = false)
    private Notice notice;

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
