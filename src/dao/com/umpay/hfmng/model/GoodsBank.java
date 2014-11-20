/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsBank.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-9
 * *************************************************/

package com.umpay.hfmng.model;

import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

/**
 * ****************** 类说明 ********************* 
 * class : GoodsBank
 * 
 * @author : zhaojunbao
 * @version : 1.0 description :
 * @see :
 * ************************************************/

public class GoodsBank implements Entity, Serializable {
	/**
	 * needs Serializable.  lizhiqiang 2014年10月20日 19:26:00
	 */
	private static final long serialVersionUID = 1L;

	private String merId; // 商户号
	private String goodsId; // 商品号
	private String bankId; // 银行号
	private String bankName; // 银行名称
	private String amtType; // 付款类型, 01：人民币 02：移动话费 03：移动积分
	private String kState; // 开通状态
	private String buyPrice;
	private String salePrice;
	@Transient
	private String amount; // 商品金额
	private String timeLtd;
	private String verifyTag; // 二次确认码
	private String checkDay;
	private String isRealTime;
	private String bankMerId;
	private String bankPosId;
	private int modLock; // 修改锁
	private String batchId;
	private String modTime;
	private String inTime;

	private String desc; // 操作原因

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("GoodsBank[merId:").append(merId);
		sb.append(";goodsId:").append(goodsId);
		sb.append(";bankId:").append(bankId);
		sb.append(";bankName:").append(bankName);
		sb.append(";amtType:").append(amtType);
		sb.append(";kState:").append(kState);
		sb.append(";amount:").append(amount);
		sb.append(";verifyTag:").append(verifyTag);
		sb.append(";checkDay:").append(checkDay);
		sb.append(";isRealTime:").append(isRealTime);
		sb.append(";bankMerId:").append(bankMerId);
		sb.append(";bankPosId:").append(bankPosId);
		sb.append(";modLock:").append(modLock);
		sb.append(";batchId:").append(batchId);
		sb.append(";desc:").append(desc);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append(";buyPrice:").append(buyPrice);
		sb.append(";salePrice:").append(salePrice);
		sb.append(";timeLtd:").append(timeLtd);
		sb.append("]");
		return sb.toString();
	}

	public void trim() {
		this.setMerId(StringUtils.trim(this.merId));
		this.setGoodsId(StringUtils.trim(this.goodsId));
		this.setBankId(StringUtils.trim(this.bankId));
		this.setBankName(StringUtils.trim(this.bankName));
		this.setAmtType(StringUtils.trim(this.amtType));
		this.setkState(StringUtils.trim(this.kState));
		this.setAmount(StringUtils.trim(this.amount));
		this.setVerifyTag(StringUtils.trim(this.verifyTag));
		this.setCheckDay(StringUtils.trim(this.checkDay));
		this.setIsRealTime(StringUtils.trim(this.isRealTime));
		this.setBankMerId(StringUtils.trim(this.bankMerId));
		this.setBankPosId(StringUtils.trim(this.bankPosId));
		this.setBatchId(StringUtils.trim(this.batchId));
		this.setDesc(StringUtils.trim(this.desc));
		this.setBuyPrice(StringUtils.trim(this.buyPrice));
		this.setSalePrice(StringUtils.trim(this.salePrice));
		this.setTimeLtd(StringUtils.trim(this.timeLtd));
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

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAmtType() {
		return amtType;
	}

	public void setAmtType(String amtType) {
		this.amtType = amtType;
	}

	public String getkState() {
		return kState;
	}

	public void setkState(String kState) {
		this.kState = kState;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getVerifyTag() {
		return verifyTag;
	}

	public void setVerifyTag(String verifyTag) {
		this.verifyTag = verifyTag;
	}

	public String getCheckDay() {
		return checkDay;
	}

	public void setCheckDay(String checkDay) {
		this.checkDay = checkDay;
	}

	public String getIsRealTime() {
		return isRealTime;
	}

	public void setIsRealTime(String isRealTime) {
		this.isRealTime = isRealTime;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public int getModLock() {
		return modLock;
	}

	public void setModLock(int modLock) {
		this.modLock = modLock;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getModTime() {
		return modTime;
	}

	public void setModTime(String modTime) {
		this.modTime = modTime;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getTimeLtd() {
		return timeLtd;
	}

	public void setTimeLtd(String timeLtd) {
		this.timeLtd = timeLtd;
	}

}
