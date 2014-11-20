package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: ReportInf
 * @Description: 商户商品报备信息描述model类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2014-7-21
 */
public class ReportInf implements Entity {

	private String backupId;
	private String merId;
	private String goodsId;
	private String bankId;
	private Integer backupStat;
	private Timestamp inTime;
	private Timestamp updtTime;
	private String modUserId;

	public void trim() {
		if (this.backupId != null) {
			this.setBackupId(StringUtils.trim(this.backupId));
		}
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}
		if (this.goodsId != null) {
			this.setGoodsId(StringUtils.trim(this.goodsId));
		}
		if (this.bankId != null) {
			this.setBankId(StringUtils.trim(this.bankId));
		}
		if (this.modUserId != null) {
			this.setModUserId(StringUtils.trim(this.modUserId));
		}
	}

	public String toString() {
		return "ReportInf[backupId:" + backupId + ";merId:" + merId + ";goodsId:" + goodsId + ";bankId:" + bankId
				+ ";backupStat:" + backupStat + ";inTime:" + inTime + ";updtTime:" + updtTime + ";modUserId:"
				+ modUserId + "]";
	}

	public String getBackupId() {
		return backupId;
	}

	public void setBackupId(String backupId) {
		this.backupId = backupId;
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

	public Integer getBackupStat() {
		return backupStat;
	}

	public void setBackupStat(Integer backupStat) {
		this.backupStat = backupStat;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public Timestamp getUpdtTime() {
		return updtTime;
	}

	public void setUpdtTime(Timestamp updtTime) {
		this.updtTime = updtTime;
	}

	public String getModUserId() {
		return modUserId;
	}

	public void setModUserId(String modUserId) {
		this.modUserId = modUserId;
	}

}
