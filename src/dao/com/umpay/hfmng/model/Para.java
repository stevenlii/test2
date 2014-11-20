package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

public class Para implements Entity {
	private String sysId;
	private String paraType;
	private String paraCode;
	private Timestamp modTime;
	private String paraValue;
	private String paraDesc;
	private String modUser;
	private Timestamp inTime;

	
	@Override
	public String toString(){
		return "Para[sysId:"+ sysId +";paraType:"+paraType+ ";paraCode:"+ paraCode + ";modTime:"+ modTime + ";paraValue:" + paraValue + ";paraDesc:" + paraDesc + ";modUser:" + modUser + ";inTime:" + inTime +"]";
	}
	
	public void trim(){
		if(this.sysId != null){
    		this.setSysId(StringUtils.trim(this.sysId));
    	}
		if(this.paraType != null){
			this.setParaType(StringUtils.trim(this.paraType));
		}
		if(this.paraCode != null){
			this.setParaCode(StringUtils.trim(this.paraCode));
		}
		if(this.paraValue != null){
			this.setParaValue(StringUtils.trim(this.paraValue));
		}
		if(this.paraDesc != null){
			this.setParaDesc(StringUtils.trim(this.paraDesc));
		}
		if(this.modUser != null){
			this.setModUser(StringUtils.trim(this.modUser));
		}
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public String getParaCode() {
		return paraCode;
	}

	public void setParaCode(String paraCode) {
		this.paraCode = paraCode;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	public String getParaDesc() {
		return paraDesc;
	}

	public void setParaDesc(String paraDesc) {
		this.paraDesc = paraDesc;
	}

	public String getModUser() {
		return modUser;
	}

	public void setModUser(String modUser) {
		this.modUser = modUser;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
	
}
