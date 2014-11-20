/** *****************  JAVA头文件说明  ****************
 * file name  :  Huadan.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-1-14
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;


/** ******************  类说明  *********************
 * class       :  Huadan
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class Huadan {
private String  platDate;
private String rpid;
private String funCode;
private String platTime;
private Timestamp inTime;
private String platSeq;
private String mobileId;
private String bankId;
private String provCode;
private  String area;
private String merId;
private String goodsId;
private String amount;
private String state;
private String fielName;
private String merName;
private String goodsName;
private String reserved;
public String getPlatDate() {
	return platDate;
}
public void setPlatDate(String platDate) {
	this.platDate = platDate;
}
public String getRpid() {
	return rpid;
}
public void setRpid(String rpid) {
	this.rpid = rpid;
}
public String getFunCode() {
	return funCode;
}
public void setFunCode(String funCode) {
	this.funCode = funCode;
}
public String getPlatTime() {
	return platTime;
}
public void setPlatTime(String platTime) {
	this.platTime = platTime;
}
public Timestamp getInTime() {
	return inTime;
}
public void setInTime(Timestamp inTime) {
	this.inTime = inTime;
}
public String getPlatSeq() {
	return platSeq;
}
public void setPlatSeq(String platSeq) {
	this.platSeq = platSeq;
}
public String getMobileId() {
	return mobileId;
}
public void setMobileId(String mobileId) {
	this.mobileId = mobileId;
}
public String getBankId() {
	return bankId;
}
public void setBankId(String bankId) {
	this.bankId = bankId;
}
public String getProvCode() {
	return provCode;
}
public void setProvCode(String provCode) {
	this.provCode = provCode;
}
public String getArea() {
	return area;
}
public void setArea(String area) {
	this.area = area;
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
public String getAmount() {
	return amount;
}
public void setAmount(String amount) {
	this.amount = amount;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getFielName() {
	return fielName;
}
public void setFielName(String fielName) {
	this.fielName = fielName;
}
public String getMerName() {
	return merName;
}
public void setMerName(String merName) {
	this.merName = merName;
}
public String getGoodsName() {
	return goodsName;
}
public void setGoodsName(String goodsName) {
	this.goodsName = goodsName;
}
public String getReserved() {
	return reserved;
}
public void setReserved(String reserved) {
	this.reserved = reserved;
}
}
