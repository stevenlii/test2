package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: CouponMerTel
 * @Description: 电子兑换券平台商户兑换电话信息model类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-6-5
 */
public class CouponMerTel implements Entity {
	private String merTelId;
	private String merId;
	private String merName;
	private String linkTel;
	private int exchangeType = -99;
	private String remark;
	private int state = -99;
	private String modUser;
	private Timestamp modTime;
	private Timestamp inTime;

	public void trim() {
		if (this.merTelId != null) {
			this.setMerTelId(StringUtils.trim(this.merTelId));
		}
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}
		if (this.linkTel != null) {
			this.setLinkTel(StringUtils.trim(this.linkTel));
		}
		if (this.remark != null) {
			this.setRemark(StringUtils.trim(this.remark));
		}
		if (this.modUser != null) {
			this.setModUser(StringUtils.trim(this.modUser));
		}
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getMerTelId() {
		return merTelId;
	}

	public void setMerTelId(String merTelId) {
		this.merTelId = merTelId;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public int getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(int exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getModUser() {
		return modUser;
	}

	public void setModUser(String modUser) {
		this.modUser = modUser;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

}
