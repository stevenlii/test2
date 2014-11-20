package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: CouponInf
 * @Description: 兑换券信息描述model类
 * @author wanyong
 * @date 2012-11-20 下午01:40:22
 */
public class CouponInf implements Entity {

	private String couponId;
	private String couponName;
	private Integer price;
	private String couponType;
	private String submitUser;
	private Timestamp inTime;
	private String auditUser;
	private Timestamp modTime;
	private Integer state;
	private String stateName; // 字符串类型状态值，主要提供给页面转义使用
	private String remark;
	private String auditState;
	private String auditResultDesc;

	public void trim() {
		if (this.couponId != null) {
			this.setCouponId(StringUtils.trim(this.couponId));
		}
		if (this.couponName != null) {
			this.setCouponName(StringUtils.trim(this.couponName));
		}
		if (this.couponType != null) {
			this.setCouponType(StringUtils.trim(this.couponType));
		}
		if (this.submitUser != null) {
			this.setSubmitUser(StringUtils.trim(this.submitUser));
		}
		if (this.auditUser != null) {
			this.setAuditUser(StringUtils.trim(this.auditUser));
		}
		if (this.remark != null) {
			this.setRemark(StringUtils.trim(this.remark));
		}
	}

	public String toString() {
		return "CouponInf[couponId:" + couponId + ";couponName:" + couponName
				+ ";price:" + price + ";couponType:" + couponType
				+ ";submitUser:" + submitUser + ";inTime:" + inTime
				+ ";auditUser:" + auditUser + ";modTime:" + modTime + ";state:"
				+ state + ";remark:" + remark + "]";
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditResultDesc() {
		return auditResultDesc;
	}

	public void setAuditResultDesc(String auditResultDesc) {
		this.auditResultDesc = auditResultDesc;
	}

}
