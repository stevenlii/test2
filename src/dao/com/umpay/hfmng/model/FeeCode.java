/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCode.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-25
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  FeeCode
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  计费代码
 * @see        :                        
 * ************************************************/

public class FeeCode {
private  String serviceId;  //计费代码
private  String feeType; // 计费类型
private String category; //计费分类
private String amount; // 金额
private String detail;  //  计费代码名称
private String modUser;  // 修改人
private Timestamp modTime;  // 修改时间
private Timestamp inTime;  // 入库时间
private String state;  //状态
private String goodsId; //商户号
private String merId;   //商品号
private String priority; //优先级
private  String  useCount;
private String matchType;
private String failReason; //失败原因
public String toString(){
	return "FeeCode [serviceId : "+serviceId+",feeType:"+feeType+",category:"+category+",amount:"+amount+"," +
			"detail:"+detail+",moduser:"+modUser+",modTime:"+modTime+",inTime:"+inTime+",state:"+state+"," +
					"priority:"+priority+",useCount:"+useCount+",matchType:"+matchType+"]";
}
public void trim(){
	if(this.merId!=null){
		this.setMerId(StringUtils.trim(this.merId));
	}
	if(this.goodsId!=null){
		this.setGoodsId(StringUtils.trim(this.goodsId));
	}
	if(this.serviceId!=null){
		this.setServiceId(StringUtils.trim(this.serviceId));
	}
	if(this.detail!=null){
		this.setDetail(StringUtils.trim(this.detail));
	}
	if(this.state!=null){
		this.setState(StringUtils.trim(this.state));
	}
	if(this.feeType!=null){
		this.setFeeType(StringUtils.trim(this.feeType));
	}
	if(this.category!=null){
		this.setCategory(StringUtils.trim(this.category));
	}
	if(this.matchType!=null){
		this.setMatchType(StringUtils.trim(this.matchType));
	}
}
public String getFailReason() {
	return failReason;
}
public void setFailReason(String failReason) {
	this.failReason = failReason;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getUseCount() {
	return useCount;
}
public void setUseCount(String useCount) {
	this.useCount = useCount;
}
public String getMatchType() {
	return matchType;
}
public void setMatchType(String matchType) {
	this.matchType = matchType;
}
public String getServiceId() {
	return serviceId;
}
public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}
public String getFeeType() {
	return feeType;
}
public void setFeeType(String feeType) {
	this.feeType = feeType;
}
public String getAmount() {
	return amount;
}
public void setAmount(String amount) {
	this.amount = amount;
}
public String getDetail() {
	return detail;
}
public void setDetail(String detail) {
	this.detail = detail;
}
public String getModUser() {
	return modUser;
}
public void setModUser(String modUser) {
	this.modUser = modUser;
}
public Timestamp getModTime() {
	return modTime;
}
public void setModTime(Timestamp modTime) {
	this.modTime = modTime;
}
public Timestamp getInTime() {
	return inTime;
}
public void setInTime(Timestamp inTime) {
	this.inTime = inTime;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}
public String getGoodsId() {
	return goodsId;
}
public void setGoodsId(String goodsId) {
	this.goodsId = goodsId;
}
public String getMerId() {
	return merId;
}
public void setMerId(String merId) {
	this.merId = merId;
}
public String getPriority() {
	return priority;
}
public void setPriority(String priority) {
	this.priority = priority;
}


}
