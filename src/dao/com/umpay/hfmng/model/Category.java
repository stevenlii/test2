/** *****************  JAVA头文件说明  ****************
 * file name  :  Category.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-24
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  Category
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  类型表。目前只记录商品类型
 * ************************************************/

public class Category implements Entity {
	
	private String categoryId; //种类编号 两位一级，从01开始，如一级为01 二级为0101，最多支持八级
	private String categoryName;  //种类名称
	private int state;  //种类状态 2：启用；4：禁用
	private String desc;  //描述
	private Timestamp inTime;  //入库时间
	private Timestamp modTime;  //修改时间
	private String modUser;    //审核人
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("[categoryId:").append(categoryId);
		sb.append(";categoryName:").append(categoryName);
		sb.append(";state:").append(state);
		sb.append(";desc:").append(desc);
		sb.append(";modUser:").append(modUser);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append("]");
		return sb.toString();
	}
	
	public void trim(){
		this.setCategoryId(StringUtils.trim(this.categoryId));
		this.setCategoryName(StringUtils.trim(this.categoryName));
		this.setDesc(StringUtils.trim(this.desc));
		this.setModUser(StringUtils.trim(this.modUser));
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public String getModUser() {
		return modUser;
	}
	public void setModUser(String modUser) {
		this.modUser = modUser;
	}

}
