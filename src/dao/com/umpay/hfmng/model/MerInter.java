/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInter.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-18
 * *************************************************/ 

package com.umpay.hfmng.model;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  MerInter
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户接口地址信息表
 * ************************************************/

public class MerInter implements Entity {
	
	private String merId;      //商户号
	private String inFunCode;  //接口标识
	private String inVersion;  //版本号码
	private Integer protocol;  //协议类型 0-http 1-socket 2-soap
	private Integer state;      //状态,2-启用,4-禁用
	private String address;  //地址
	private String addressDesc;  //地址描述
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("MerInter[merId:").append(merId);
		sb.append(";inFunCode:").append(inFunCode);
		sb.append(";inVersion:").append(inVersion);
		sb.append(";Protocol:").append(protocol);
		sb.append(";state:").append(state);
		sb.append(";address:").append(address);
		sb.append(";addressDesc:").append(addressDesc);
		sb.append("]");
		return sb.toString();
	}
	
	public void trim(){
		this.setMerId(StringUtils.trim(this.merId));
		this.setInFunCode(StringUtils.trim(this.inFunCode));
		this.setInVersion(StringUtils.trim(this.inVersion));
		this.setAddress(StringUtils.trim(this.address));
		this.setAddressDesc(StringUtils.trim(this.addressDesc));
	}
	
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getInFunCode() {
		return inFunCode;
	}
	public void setInFunCode(String inFunCode) {
		this.inFunCode = inFunCode;
	}
	public String getInVersion() {
		return inVersion;
	}
	public void setInVersion(String inVersion) {
		this.inVersion = inVersion;
	}
	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressDesc() {
		return addressDesc;
	}
	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}

}
