/** *****************  JAVA头文件说明  ****************
 * file name  :  PayltdRollback.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-4-15
 * *************************************************/ 

package com.umpay.hfmng.model;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  PayltdRollback
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  限额回滚
 * ************************************************/

public class PayltdRollback implements Entity {
	
	private String provCode; //省代码
	private String bankId;  //银行号
	private String amt;  //回滚金额
	private String merId;  //商户号
	private String goodsId;  //商品号
	private String mobileId;  //手机号
	private String rpId;  //流水号
	private String monthPayed;  //当月累计额
	
	public String toString(){
		return "PayltdRollback[provCode:"+provCode+";bankId:"+bankId+";amt:"+amt+";merId:"+merId
			+";goodsId:"+goodsId+";mobileId:"+mobileId+";rpId:"+rpId+";monthPayed:"+monthPayed+"]";
	}
	
	public void trim(){
		this.setProvCode(StringUtils.trim(this.provCode));
		this.setBankId(StringUtils.trim(this.bankId));
		this.setAmt(StringUtils.trim(this.amt));
		this.setMerId(StringUtils.trim(this.merId));
		this.setGoodsId(StringUtils.trim(this.goodsId));
		this.setMobileId(StringUtils.trim(this.mobileId));
		this.setRpId(StringUtils.trim(this.rpId));
		this.setMonthPayed(StringUtils.trim(this.monthPayed));
	}
	
	public String getProvCode() {
		return provCode;
	}
	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
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
	public String getMobileId() {
		return mobileId;
	}
	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	public String getRpId() {
		return rpId;
	}
	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	public String getMonthPayed() {
		return monthPayed;
	}

	public void setMonthPayed(String monthPayed) {
		this.monthPayed = monthPayed;
	}

}
