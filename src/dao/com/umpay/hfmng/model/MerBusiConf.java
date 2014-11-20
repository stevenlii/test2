/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBusiConf.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-14
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  MerBusiConf
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户业务配置表
 * ************************************************/

public class MerBusiConf implements Entity, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 商户号，映射列名MER_ID
	 */
	private String merId;
	/**
	 * 业务类型，4位编码。映射列名BIZ_TYPE
	 */
	private String bizType;
	/**
	 * 状态。 2、启用  ；4、禁用
	 */
	private Integer state;
	/**
	 * 添加人。映射列名ADD_USER_ID
	 */
	private String creator;
	/**
	 * 修改人。映射列名MOD_USER_ID
	 */
	private String modUser;
	/**
	 * 添加时间。映射列名IN_TIME
	 */
	private Timestamp inTime;
	/**
	 * 最后修改时间。映射列名UPDT_TIME
	 */
	private Timestamp modTime;
	/**
	 * 修改锁。0、未锁定； 1、锁定。映射列名MOD_LOCK
	 */
	private Integer modLock;
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("MerBusiConf[merId:").append(merId);
		sb.append(";bizType:").append(bizType);
		sb.append(";state:").append(state);
		sb.append(";creator:").append(creator);
		sb.append(";modUser:").append(modUser);
		sb.append(";modLock:").append(modLock);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append("]");
		return sb.toString();
	}
	
	public void trim(){
		this.setMerId(StringUtils.trim(this.merId));
		this.setBizType(StringUtils.trim(this.bizType));
		this.setCreator(StringUtils.trim(this.creator));
		this.setModUser(StringUtils.trim(this.modUser));
	}
	
	public Integer getModLock() {
		return modLock;
	}

	public void setModLock(Integer modLock) {
		this.modLock = modLock;
	}

	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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
