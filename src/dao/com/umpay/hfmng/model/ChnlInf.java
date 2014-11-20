/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlInf.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-15
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  ChnlInf
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  渠道信息表
 * ************************************************/

public class ChnlInf implements Entity {
	
	private String channelId;  //渠道编号
	private String channelName;  //渠道名称
	private int state;  //渠道状态
	private String notifyUrl;  //通知地址
	private String orderUrl;  //下单地址
	private byte[] merCert;  //证书
	private String service_user;  //业务负责人
	private String contact;  //联系人
	private String linkTel;  //联系电话
	private String email;  //电子邮箱
	private Timestamp inTime;  //入库时间
	private Timestamp modTime;  //修改时间
	private int modLock;
	
	public String toString(){
		return "ChnlInf[channelId:"+channelId+";channelName:"+channelName+";state:"+state+";notifyUrl:"+notifyUrl
			+";orderUrl:"+orderUrl+";merCert:"+merCert+";service_user:"+service_user+";contact:"+contact
			+";linkTel:"+linkTel+";email:"+email+";inTime:"+inTime+";modTime:"+modTime+";modLock:"+modLock+"]";
	}
	public void trim(){
		this.setChannelId(StringUtils.trim(this.channelId));
		this.setChannelName(StringUtils.trim(this.channelName));
		this.setNotifyUrl(StringUtils.trim(this.notifyUrl));
		this.setOrderUrl(StringUtils.trim(this.orderUrl));
		this.setService_user(StringUtils.trim(this.service_user));
		this.setContact(StringUtils.trim(this.contact));
		this.setLinkTel(StringUtils.trim(this.linkTel));
		this.setEmail(StringUtils.trim(this.email));
	}
	
	public int getModLock() {
		return modLock;
	}
	public void setModLock(int modLock) {
		this.modLock = modLock;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getOrderUrl() {
		return orderUrl;
	}
	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}
	public byte[] getMerCert() {
		return merCert;
	}
	public void setMerCert(byte[] merCert) {
		this.merCert = merCert;
	}
	public String getService_user() {
		return service_user;
	}
	public void setService_user(String serviceUser) {
		service_user = serviceUser;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
