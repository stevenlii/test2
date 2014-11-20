/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBank.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-20
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  MerBank
 * @author     :  xhf
 * @version    :  1.0  
 * description :  商户银行
 * ************************************************/

public class MerBank implements Entity {
	
	private String merId;      //商户号
	private String bankId;     //支付银行号
	private String state;      //状态,2-开通,4-注销
	private String bankMerId;  //平台在机构的商户号
	private String bankPosId;  //平台在机构的终端号
	private int modLock;       //修改锁 0-未锁定   1-锁定
	
	private String merName;    //商户名称
	private String bankName;   //支付银行名称
	private String operator;   //运营负责人
	private String modUser;    //修改人
	private Timestamp inTime;    //添加时间
	private Timestamp modTime;    //修改时间
	private String auditType;  //表示审核表中的审核类型
	
	public String toString(){
		return "MerBank[merId:"+merId+";merName:"+merName+";bankId:"+bankId
				+";bankName:"+bankName+";state:"+state+";bankMerId:"
				+bankMerId+";bankPosId:"+bankPosId+";modUser:"+modUser
				+";operator:"+operator+";auditType:"+auditType+";inTime:"+inTime
				+";modTime:"+modTime+";modLock:"+modLock+"]";
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
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

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBankMerId() {
		return bankMerId;
	}

	public void setBankMerId(String bankMerId) {
		this.bankMerId = bankMerId;
	}

	public String getBankPosId() {
		return bankPosId;
	}

	public void setBankPosId(String bankPosId) {
		this.bankPosId = bankPosId;
	}
	
	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public void trim(){
		if(this.merId != null){
			this.setMerId(StringUtils.trim(this.merId));
		}
		if(this.bankId != null){
			this.setBankId(StringUtils.trim(this.bankId));
		}
		if(this.state != null){
			this.setState(StringUtils.trim(this.state));
		}
		if(this.bankMerId != null){
			this.setBankMerId(StringUtils.trim(this.bankMerId));
		}
		if(this.bankPosId != null){
			this.setBankPosId(StringUtils.trim(this.bankPosId));
		}
		if(this.merName != null){
			this.setMerName(StringUtils.trim(this.merName));
		}
		if(this.bankName != null){
			this.setBankName(StringUtils.trim(this.bankName));
		}
		if(this.operator != null){
			this.setOperator(StringUtils.trim(this.operator));
		}
		if(this.modUser != null){
			this.setModUser(StringUtils.trim(this.modUser));
		}
	}

}
