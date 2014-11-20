/** *****************  JAVA头文件说明  ****************
 * file name  :  HfMerOper.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-2-28
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  HfMerOper
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户负责人关系表T_HFMER_OPER
 * ************************************************/

public class HfMerOper implements Entity, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商户号，映射列名MER_ID
	 */
	private String merId;
	/**
	 * 运营负责人，映射列名OPERATOR
	 */
	private String operator;
	/**
	 * 状态。 2、启用  ；4、禁用
	 */
	private Integer state;
	/**
	 * 创建人。 映射列名ADD_USER_ID
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
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("HfMerOper[merId:").append(merId);
		sb.append(";operator:").append(operator);
		sb.append(";state:").append(state);
		sb.append(";creator:").append(creator);
		sb.append(";modUser:").append(modUser);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append("]");
		return sb.toString();
	}
	
	public void trim(){
		this.setMerId(StringUtils.trim(this.merId));
		this.setOperator(StringUtils.trim(this.operator));
		this.setCreator(StringUtils.trim(this.creator));
		this.setModUser(StringUtils.trim(this.modUser));
	}
	
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
