/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBank.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-15
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  ChnlBank
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  渠道与支付商关系表
 * ************************************************/

public class ChnlBank implements Entity {
	
	private String channelId;  //渠道编号
	private String bankId;  //银行编号
	private int state;  //渠道银行状态
	private String service_user;  //业务负责人
	private Timestamp inTime;  //入库时间
	private Timestamp modTime;  //修改时间
	private int modLock;
	
	private String auditType;  //表示审核表中的审核类型
	
	public String toString(){
		return "ChnlBank[channelId:"+channelId+";bankId:"+bankId+";state:"+state+";modLock:"+modLock
			+";service_user:"+service_user+";auditType:"+auditType+";inTime:"+inTime+";modTime:"+modTime+"]";
	}
	public void trim(){
		this.setChannelId(StringUtils.trim(this.channelId));
		this.setBankId(StringUtils.trim(this.bankId));
		this.setService_user(StringUtils.trim(this.service_user));
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getService_user() {
		return service_user;
	}
	public void setService_user(String serviceUser) {
		service_user = serviceUser;
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
	public int getModLock() {
		return modLock;
	}
	public void setModLock(int modLock) {
		this.modLock = modLock;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

}
