package com.umpay.hfmng.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
public class DemoInfo implements Entity {

	private String merId;
	private String merName;
	private String exMerId;
	private String merType;
	private int state;
	private String postal;
	private String address;
	private String modUser;
	private Timestamp modTime;
	private BigDecimal timeLtd;
	private int isSpecial;
	private int ifTrust;
	private String cusPhone;
	private String merAccountId;
	private Timestamp inTime;

	private int auditType = 0;
	private int auditState = 0;

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public String getTableName() {
		return "T_MER_INF";
	}


	public Timestamp getAddTime() {
		return inTime;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getExMerId() {
		return exMerId;
	}

	public void setExMerId(String exMerId) {
		this.exMerId = exMerId;
	}

	public String getMerType() {
		return merType;
	}

	public void setMerType(String merType) {
		this.merType = merType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public BigDecimal getTimeLtd() {
		return timeLtd;
	}

	public void setTimeLtd(BigDecimal timeLtd) {
		this.timeLtd = timeLtd;
	}

	public int getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(int isSpecial) {
		this.isSpecial = isSpecial;
	}

	public int getIfTrust() {
		return ifTrust;
	}

	public void setIfTrust(int ifTrust) {
		this.ifTrust = ifTrust;
	}

	public String getCusPhone() {
		return cusPhone;
	}

	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

	public String getMerAccountId() {
		return merAccountId;
	}

	public void setMerAccountId(String merAccountId) {
		this.merAccountId = merAccountId;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}


	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

}
