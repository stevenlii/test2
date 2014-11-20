package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: CouponGcOrder
 * @Description: 取码订单表实体类
 * @author wanyong
 * @date 2013-4-3 上午10:17:01
 */
public class CouponGcOrder implements Entity {
	private String orderId;
	private String orderDate;
	private String merOrderId;
	private String merOrderDate;
	private String channelId;
	private String channelName; // 渠道名称
	private String couponId;
	private String couponName; // 兑换券名称
	private String ruleId;
	private String ruleName; // 兑换券规则名称
	private int state;
	private String stateName; // 订单状态中文名称
	private String merId;
	private String merName; // 商户名称
	private String goodsId;
	private String goodsName; // 商品名称
	private int goodsSum;
	private double amount;
	private int merCheck;
	private String mobileId;
	private Timestamp modTime;
	private Timestamp inTime;

	public void trim() {
		if (this.orderId != null) {
			this.setOrderId(StringUtils.trim(this.orderId));
		}
		if (this.orderDate != null) {
			this.setOrderDate(StringUtils.trim(this.orderDate));
		}
		if (this.merOrderId != null) {
			this.setMerOrderId(StringUtils.trim(this.merOrderId));
		}
		if (this.merOrderDate != null) {
			this.setMerOrderDate(StringUtils.trim(this.merOrderDate));
		}
		if (this.channelId != null) {
			this.setChannelId(StringUtils.trim(this.channelId));
		}
		if (this.channelName != null) {
			this.setChannelName(StringUtils.trim(this.channelName));
		}
		if (this.couponId != null) {
			this.setCouponId(StringUtils.trim(this.couponId));
		}
		if (this.couponName != null) {
			this.setCouponName(StringUtils.trim(this.couponName));
		}
		if (this.ruleId != null) {
			this.setRuleId(StringUtils.trim(this.ruleId));
		}
		if (this.ruleName != null) {
			this.setRuleName(StringUtils.trim(this.ruleName));
		}
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}
		if (this.merName != null) {
			this.setMerName(StringUtils.trim(this.merName));
		}
		if (this.goodsId != null) {
			this.setGoodsId(StringUtils.trim(this.goodsId));
		}
		if (this.goodsName != null) {
			this.setGoodsName(StringUtils.trim(this.goodsName));
		}
		if (this.mobileId != null) {
			this.setMobileId(StringUtils.trim(this.mobileId));
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getMerOrderId() {
		return merOrderId;
	}

	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}

	public String getMerOrderDate() {
		return merOrderDate;
	}

	public void setMerOrderDate(String merOrderDate) {
		this.merOrderDate = merOrderDate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	public int getGoodsSum() {
		return goodsSum;
	}

	public void setGoodsSum(int goodsSum) {
		this.goodsSum = goodsSum;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getMerCheck() {
		return merCheck;
	}

	public void setMerCheck(int merCheck) {
		this.merCheck = merCheck;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

}
