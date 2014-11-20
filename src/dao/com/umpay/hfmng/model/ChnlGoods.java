/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoods.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-15
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  ChnlGoods
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  渠道与商品关系表
 * ************************************************/

public class ChnlGoods implements Entity {
	
	private String channelId;  //渠道编号
	private String merId;  //商户号
	private String goodsId;  //商品号
	private int state;  //渠道商品状态
	private String service_user;  //业务负责人
	private Timestamp inTime;  //入库时间
	private Timestamp modTime;  //修改时间
	private int modLock;
	
	public String toString(){
		return "ChnlBank[channelId:"+channelId+";merId:"+merId+";goodsId:"+goodsId+";state:"+state+";modLock:"+modLock
			+";service_user:"+service_user+";inTime:"+inTime+";modTime:"+modTime+"]";
	}
	public void trim(){
		this.setChannelId(StringUtils.trim(this.channelId));
		this.setMerId(StringUtils.trim(merId));
		this.setGoodsId(StringUtils.trim(this.goodsId));
		this.setService_user(StringUtils.trim(this.service_user));
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
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
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

}
