/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoods.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  ProxyGoods
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class ProxyGoods implements Entity {
	
	private String merId;  //一级商户号
	private String subMerId;  //二级商户编号
	private String goodsId; // 商品号
	private int state; // 状态,2-开通,4-注销
	private int modLock; // 修改锁 1：锁定中,0:未锁定
	private String modUser; // 修改人
	private Timestamp inTime; // 入库时间
	private Timestamp modTime; // 修改时间
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("merId:").append(merId);
		sb.append(";subMerId:").append(subMerId);
		sb.append(";goodsId:").append(goodsId);
		sb.append(";state:").append(state);
		sb.append(";modUser:").append(modUser);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		return sb.toString();
	}
	
	public void trim(){
		this.setSubMerId(StringUtils.trim(this.subMerId));
		this.setGoodsId(StringUtils.trim(this.goodsId));
		this.setModUser(StringUtils.trim(this.modUser));
		this.setMerId(StringUtils.trim(this.merId));
	}
	
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getSubMerId() {
		return subMerId;
	}
	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getModLock() {
		return modLock;
	}
	public void setModLock(int modLock) {
		this.modLock = modLock;
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
