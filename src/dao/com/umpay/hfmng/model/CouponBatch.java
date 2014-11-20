package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: CouponBatch
 * @Description: 兑换码批量导入批次信息model类
 * @author wanyong
 * @date 2012-12-20 下午09:10:41
 */
public class CouponBatch implements Entity {
	private String batchId;
	private String ruleId;
	private Timestamp modTime;
	private Integer goodsSum;
	private Integer saledSum;
	private String sellStartDate;
	private String sellEndDate;
	private String useStartDate;
	private String useEndDate;
	private String modUser;
	private Timestamp inTime;
	private Integer state;

	public void trim() {
		if (this.batchId != null) {
			this.setBatchId(StringUtils.trim(this.batchId));
		}
		if (this.ruleId != null) {
			this.setRuleId(StringUtils.trim(this.ruleId));
		}
		if (this.sellStartDate != null) {
			this.setSellStartDate(StringUtils.trim(this.sellStartDate));
		}
		if (this.sellEndDate != null) {
			this.setSellEndDate(StringUtils.trim(this.sellEndDate));
		}
		if (this.useStartDate != null) {
			this.setUseStartDate(StringUtils.trim(this.useStartDate));
		}
		if (this.useEndDate != null) {
			this.setUseEndDate(StringUtils.trim(this.useEndDate));
		}
		if (this.modUser != null) {
			this.setModUser(StringUtils.trim(this.modUser));
		}
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public Integer getGoodsSum() {
		return goodsSum;
	}

	public void setGoodsSum(Integer goodsSum) {
		this.goodsSum = goodsSum;
	}

	public Integer getSaledSum() {
		return saledSum;
	}

	public void setSaledSum(Integer saledSum) {
		this.saledSum = saledSum;
	}

	public String getSellStartDate() {
		return sellStartDate;
	}

	public void setSellStartDate(String sellStartDate) {
		this.sellStartDate = sellStartDate;
	}

	public String getSellEndDate() {
		return sellEndDate;
	}

	public void setSellEndDate(String sellEndDate) {
		this.sellEndDate = sellEndDate;
	}

	public String getUseStartDate() {
		return useStartDate;
	}

	public void setUseStartDate(String useStartDate) {
		this.useStartDate = useStartDate;
	}

	public String getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(String useEndDate) {
		this.useEndDate = useEndDate;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
