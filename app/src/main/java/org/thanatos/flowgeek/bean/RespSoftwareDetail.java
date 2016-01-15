package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * *************************************************
 * <p>
 * 版权:HZX 版权所有©2015
 * <p>
 * 作者:HZX
 * <p>
 * 版本:1.0
 * <p>
 * 创建日期:2015/12/31   10:17
 * <p>
 * 描述:
 * <p>
 * 修订说明:
 * <p>
 * *************************************************
 */
@Root(name = "oschina")
public class RespSoftwareDetail {

    @Element(name = "software")
    private Software software;

    @Element(name = "notice", required = false)
    private Notice notice;

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
