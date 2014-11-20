/**
 * @ClassName: CouponChannel
 * @Description: TODO
 * @author panyouliang
 * @date 2013-3-25 下午2:52:23
 */
package com.umpay.hfmng.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.ibm.db2.jcc.uw.Blob;

/**
 * @author MARCO.PAN
 * 
 */
public class ChannelInf implements Entity {
	private String channelId;
	private String channelName;
	private Integer state;
	private String notifyUrl;
	private Blob mercert;
	private String serviceUser;
	private String contact;
	private String linkTel;
	private String eamil;
	private Date inTime;
	private Date modTime;

	public void trim() {
		if (this.channelId != null) {
			this.setChannelId(StringUtils.trim(this.channelId));
		}
		if (this.channelName != null) {
			this.setChannelName(StringUtils.trim(this.channelName));
		}
		if (this.notifyUrl != null) {
			this.setNotifyUrl(StringUtils.trim(this.notifyUrl));
		}
		if (this.serviceUser != null) {
			this.setServiceUser(StringUtils.trim(this.serviceUser));
		}
		if (this.contact != null) {
			this.setContact(StringUtils.trim(this.contact));
		}
		if (this.linkTel != null) {
			this.setLinkTel(StringUtils.trim(this.linkTel));
		}
		if (this.eamil != null) {
			this.setEamil(StringUtils.trim(this.eamil));
		}
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Blob getMercert() {
		return mercert;
	}

	public void setMercert(Blob mercert) {
		this.mercert = mercert;
	}

	public String getServiceUser() {
		return serviceUser;
	}

	public void setServiceUser(String serviceUser) {
		this.serviceUser = serviceUser;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getEamil() {
		return eamil;
	}

	public void setEamil(String eamil) {
		this.eamil = eamil;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Date getModTime() {
		return modTime;
	}

	public void setModTime(Date modTime) {
		this.modTime = modTime;
	}

}
