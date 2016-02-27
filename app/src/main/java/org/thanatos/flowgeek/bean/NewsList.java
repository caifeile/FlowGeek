package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯列表封装数据对象
 */
@Root(name = "oschina")
public class NewsList extends Entity {

    public final static String PREF_READED_NEWS_LIST = "readed_news_list.pref";

    public final static int CATALOG_ALL = 1; // -> 所有
    public final static int CATALOG_INTEGRATION = 2; // -> 综合
    public final static int CATALOG_SOFTWARE = 3; // -> 软件更新
    public final static int CATALOG_WEEK = 4; // -> 每周热点
    public final static int CATALOG_MONTH = 5; // -> 每月热点

    @Element(name = "catalog")
    private int catalog;

    @Element(name = "pagesize")
    private int pageSize;

    @Element(name = "newsCount")
    private int newsCount;

    @ElementList(name = "newslist")
    private ArrayList<News> list = new ArrayList<News>();

    @Element(name = "notice", required = false)
    private Notice notice;

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public ArrayList<News> getList() {
        return list;
    }

    public void setList(ArrayList<News> list) {
        this.list = list;
    }
}