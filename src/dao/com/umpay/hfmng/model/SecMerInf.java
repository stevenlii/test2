/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerInf.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-13
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  SecMerInf
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  二级商户信息表
 * ************************************************/

public class SecMerInf implements Entity {
	
	private String subMerId;  //二级商户编号
	private String subMerName;  //二级商户名称
	private int state; // 状态,2-开通,4-注销
	private String operator;  //运营负责人
	private String modUser; // 修改人
	private int modLock; // 修改锁 1：锁定中,0:未锁定
	private Timestamp inTime; // 入库时间
	private Timestamp modTime; // 修改时间
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("[subMerId:").append(subMerId);
		sb.append(";subMerName:").append(subMerName);
		sb.append(";state:").append(state);
		sb.append(";operator:").append(operator);
		sb.append(";modUser:").append(modUser);
		sb.append(";modLock:").append(modLock);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append("]");
		return sb.toString();
	}
	
	public void trim(){
		this.setSubMerId(StringUtils.trim(this.subMerId));
		this.setSubMerName(StringUtils.trim(this.subMerName));
		this.setOperator(StringUtils.trim(this.operator));
		this.setModUser(StringUtils.trim(this.modUser));
	}
	
	public String getSubMerId() {
		return subMerId;
	}
	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}
	public String getSubMerName() {
		return subMerName;
	}
	public void setSubMerName(String subMerName) {
		this.subMerName = subMerName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getModUser() {
		return modUser;
	}
	public void setModUser(String modUser) {
		this.modUser = modUser;
	}
	public int getModLock() {
		return modLock;
	}
	public void setModLock(int modLock) {
		this.modLock = modLock;
	}
	public Timestamp getInTime() {
		return inTime;
	}
	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
	public Timestamp getModTime() {
		return modTime;
	}
	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}
	
}
