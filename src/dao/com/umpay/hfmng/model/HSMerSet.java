/** *****************  JAVA头文件说明  ****************
 * file name  :  HSMerSet.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-7-16
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  HSMerSet
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  话费结算系统-话费商户结算表(夏冬_20110401)
 * ************************************************/

public class HSMerSet implements Entity {
	
	private String merId;  //商户号
	private BigDecimal billSuccNumm;  //包月计费成功笔数
	private BigDecimal billSuccAmtm;  //包月计费成功金额
	private BigDecimal muteNum;  //移动核减笔数
	private BigDecimal muteAmt;  //移动核减金额
	private String stlDate;  //6位月份和8位日期
	
	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public BigDecimal getBillSuccNumm() {
		return billSuccNumm;
	}

	public void setBillSuccNumm(BigDecimal billSuccNumm) {
		this.billSuccNumm = billSuccNumm;
	}

	public BigDecimal getBillSuccAmtm() {
		return billSuccAmtm;
	}

	public void setBillSuccAmtm(BigDecimal billSuccAmtm) {
		this.billSuccAmtm = billSuccAmtm;
	}

	public BigDecimal getMuteNum() {
		return muteNum;
	}

	public void setMuteNum(BigDecimal muteNum) {
		this.muteNum = muteNum;
	}

	public BigDecimal getMuteAmt() {
		return muteAmt;
	}

	public void setMuteAmt(BigDecimal muteAmt) {
		this.muteAmt = muteAmt;
	}

	public String getStlDate() {
		return stlDate;
	}

	public void setStlDate(String stlDate) {
		this.stlDate = stlDate;
	}

	public void trim() {
		this.setMerId(StringUtils.trim(this.merId));
		
	}

}
