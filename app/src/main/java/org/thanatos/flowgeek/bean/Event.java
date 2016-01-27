package org.thanatos.flowgeek.bean;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.thanatos.base.domain.Entity;

/**
 * 活动实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年12月12日 下午3:18:08
 * 
 */
@Root(name = "event")
public class Event extends Entity {
	
	public final static int EVNET_STATUS_APPLYING = 0x02;
	public final static int EVNET_STATUS_END = 0x01;
	
	public final static int APPLYSTATUS_CHECKING = 0x00;// 审核中
	public final static int APPLYSTATUS_CHECKED = 0x01;// 已经确认
	public final static int APPLYSTATUS_ATTEND = 0x02;// 已经出席
	public final static int APPLYSTATUS_CANCLE = 0x03;// 已取消
	public final static int APPLYSTATUS_REJECT = 0X04;// 已拒绝

	@Element(name = "id")
	private Long id;

	@Element(name = "cover", required = false)
	private String cover;
	
	@Element(name = "title", required = false)
	private String title;
	
	@Element(name = "url", required = false)
	private String url;
	
	@Element(name = "createTime", required = false)
	private String createTime;
	
	@Element(name = "startTime", required = false)
	private String startTime;
	
	@Element(name = "endTime", required = false)
	private String endTime;
	
	@Element(name = "spot", required = false)
	private String spot;
	
	@Element(name = "actor_count", required = false)
	private int actor_count;
	
	@Element(name = "location", required = false)
	private String location;
	
	@Element(name = "city", required = false)
	private String city;
	
	@Element(name = "status", required = false)
	private int status;
	
	@Element(name = "applyStatus", required = false)
	private int applyStatus;
	
	@Element(name = "category", required = false)
	private int category;// 活动类型 1源创会 2技术交流 3其他 4站外活动

	@Element(name = "remark", required = false)
	private EventRemark eventRemark;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSpot() {
		return spot;
	}

	public void setSpot(String spot) {
		this.spot = spot;
	}

	public int getActor_count() {
		return actor_count;
	}

	public void setActor_count(int actor_count) {
		this.actor_count = actor_count;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}

	public EventRemark getEventRemark() {
		return eventRemark;
	}

	public void setEventRemark(EventRemark eventRemark) {
		this.eventRemark = eventRemark;
	}
}
