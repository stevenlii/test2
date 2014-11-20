/** *****************  JAVA头文件说明  ****************
 * file name  :  BankInfo.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-26
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  BankInfo
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  银行信息描述
 * @see        :                        
 * ************************************************/

public class BankInfo {
private String bankId;  //支付服务商编号
private String bankName; // 支付服务商名称
private String bankType;  // 支付服务商类型
private String state;  //  状态
private String timeLtd;  // 次限额
private String monLtd; // 月限额
private String dayLtd; // 日限额
private String minAmount;  // 最小交易金额
private String tinierval;  // 交易间隔时间
private String modUser;  //  修改人
private Timestamp modTime;  // 修改时间
private Timestamp inTime;   // 入库时间
private String depagentType;  // 依赖的媒介类型
public String toString(){
	return "bankInfo[banId:"+bankId+",bankName: "+bankName+",bankType:"+bankType+",state:"+state+",timeLtd:"+timeLtd+",monLtd:"+monLtd+",dayLtd:"+dayLtd+
	",minAmount:"+minAmount+",tinierval:"+tinierval+",modUser:"+modUser+",modTime:"+modTime+",inTime:"+inTime+",depagentType:"+depagentType+"]";
}
public String getBankId() {
	return bankId;
}
public void setBankId(String bankId) {
	this.bankId = bankId;
}
public String getBankName() {
	return bankName;
}
public void setBankName(String bankName) {
	this.bankName = bankName;
}
public String getBankType() {
	return bankType;
}
public void setBankType(String bankType) {
	this.bankType = bankType;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getTimeLtd() {
	return timeLtd;
}
public void setTimeLtd(String timeLtd) {
	this.timeLtd = timeLtd;
}
public String getMonLtd() {
	return monLtd;
}
public void setMonLtd(String monLtd) {
	this.monLtd = monLtd;
}
public String getDayLtd() {
	return dayLtd;
}
public void setDayLtd(String dayLtd) {
	this.dayLtd = dayLtd;
}
public String getMinAmount() {
	return minAmount;
}
public void setMinAmount(String minAmount) {
	this.minAmount = minAmount;
}
public String getTinierval() {
	return tinierval;
}
public void setTinierval(String tinierval) {
	this.tinierval = tinierval;
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
public String getDepagentType() {
	return depagentType;
}
public void setDepagentType(String depagentType) {
	this.depagentType = depagentType;
}

public void trim(){
	if(this.bankId != null){
		this.setBankId(StringUtils.trim(this.bankId));
	}
	if(this.bankName != null){
		this.setBankName(StringUtils.trim(this.bankName));
	}
	if(this.bankType != null){
		this.setBankType(StringUtils.trim(this.bankType));
	}
	if(this.state != null){
		this.setState(StringUtils.trim(this.state));
	}
	if(this. timeLtd!= null){
		this.setTimeLtd(StringUtils.trim(this.timeLtd));
	}
	if(this.monLtd != null){
		this.setMonLtd(StringUtils.trim(this.monLtd));
	}
	if(this.dayLtd != null){
		this.setDayLtd(StringUtils.trim(this.dayLtd));
	}
	if(this.minAmount != null){
		this.setMinAmount(StringUtils.trim(this.minAmount));
	}
	if(this.tinierval != null){
		this.setTinierval(StringUtils.trim(this.tinierval));
	}
	if(this.modUser != null){
		this.setModUser(StringUtils.trim(this.modUser));
	}
	if(this.depagentType != null){
		this.setDepagentType(StringUtils.trim(this.depagentType));
	}
}
}
