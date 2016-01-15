package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

/**
 * 活动备注选择
 */
@Root(name = "remark")
public class EventRemark extends Entity {

    @Element(name = "remarkTip", required = false)
    private String remarkTip;

    @Element(name = "remarkSelect", required = false)
    private RemarksSelect select;

    public class RemarksSelect extends Entity {
        @ElementList(name = "select")
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

    }

    public String getRemarkTip() {
        return remarkTip;
    }

    public void setRemarkTip(String remarkTip) {
        this.remarkTip = remarkTip;
    }

    public RemarksSelect getSelect() {
        return select;
    }

    public void setSelect(RemarksSelect select) {
        this.select = select;
    }
}
