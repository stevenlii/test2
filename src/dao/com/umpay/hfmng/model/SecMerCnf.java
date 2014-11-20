/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerCnf.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-13
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.ibm.db2.jcc.uw.Blob;


/** ******************  类说明  *********************
 * class       :  SecMerCnf
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  二级商户接口配置表
 * ************************************************/

public class SecMerCnf implements Entity {
	
	private String subMerId;  //二级商户编号
	private String inFunCode;  //接口标识
	private String subMerName;  //二级商户名称
	private String version;  //版本号码 ,默认3.0
	private byte[] merCert;  //商户证书
	private String orderUrl;  //下单地址
	private String notifyUrl;  //支付通知地址
	private String modUser; // 修改人
	private Timestamp inTime; // 入库时间
	private Timestamp modTime; // 修改时间
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("[subMerId:").append(subMerId);
		sb.append(";inFunCode:").append(inFunCode);
		sb.append(";version:").append(version);
		sb.append(";orderUrl:").append(orderUrl);
		sb.append(";notifyUrl:").append(notifyUrl);
		sb.append(";modUser:").append(modUser);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append("]");
		return sb.toString();
	}
	
	public void trim(){
		this.setSubMerId(StringUtils.trim(this.subMerId));
		this.setInFunCode(StringUtils.trim(this.inFunCode));
		this.setVersion(StringUtils.trim(this.version));
		this.setOrderUrl(StringUtils.trim(this.orderUrl));
		this.setNotifyUrl(StringUtils.trim(this.notifyUrl));
		this.setModUser(StringUtils.trim(this.modUser));
	}
	
	public String getSubMerId() {
		return subMerId;
	}
	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}
	public String getInFunCode() {
		return inFunCode;
	}
	public void setInFunCode(String inFunCode) {
		this.inFunCode = inFunCode;
	}
	public String getSubMerName() {
		return subMerName;
	}

	public void setSubMerName(String subMerName) {
		this.subMerName = subMerName;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public byte[] getMerCert() {
		return merCert;
	}
	public void setMerCert(byte[] merCert) {
		this.merCert = merCert;
	}
	public String getOrderUrl() {
		return orderUrl;
	}
	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
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
	public Timestamp getModTime() {
		return modTime;
	}
	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

}
