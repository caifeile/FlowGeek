package org.thanatos.base.domain;

import java.util.ArrayList;

/**
 * Created by thanatos on 15-9-24.
 */
public class Page<T> {

    public static final int PAGE_SIZE = 20;

    private Integer pageNum;
    private Integer recordCount;
    private Integer pageCount;
    private Integer pageSize;
    private ArrayList<T> list;

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", recordCount=" + recordCount +
                ", pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", list=" + list.get(0).toString() +
                '}';
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}
