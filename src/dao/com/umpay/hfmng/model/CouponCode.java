package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

public class CouponCode implements Entity {
	private String couponCode; // 兑换码
	private String merId; // 商户ID
	private String merName;
	private String goodsId; // 商品ID
	private String goodsName;
	private String couponId; // 兑换券ID
	private String couponName;
	private Timestamp modTime; // 修改时间
	private String operator; // 兑换操作员ID
	private Timestamp exchangeTime; // 兑换时间
	private int exchangeType = -1; // 兑换类型
	private String exchangeTypeName; // 兑换类型中文描述
	private Integer state = -99; // 兑换码状态
	private String stateName; // 兑换码状态中文描述
	private Timestamp saledTime; // 售出时间
	private String useStartDate; // 兑换码使用有效期起始日期
	private String useEndDate; // 兑换码使用有效期截止日期
	private String couponCodeType; // 兑换码形式
	private Timestamp remindTime; // 兑换码到期告警消费者日期
	private String ruleId; // 兑换券规则编号
	private String batchId; // 批次号
	private String orderId; // 订单编号
	private String channelId; // 渠道ID
	private String channelName;
	private int generateMethod = -1; // 兑换码生成方式
	private String generateMethodName; // 兑换码生成方式中文描述
	private Integer price = -1; // 购买价（分）
	private String priceName; // 购买价（元，保留两位小数）
	private String paidMobileId; // 购买的手机号
	private Integer alarmCount = -99; // 剩余提醒次数
	private Timestamp inTime; // 插入时间
	private Integer originalprice;
	public CouponCode() {
	}


	/**
	 * @param couponCode
	 * @param merId
	 * @param goodsId
	 * @param couponId
	 * @param operator
	 * @param exchangeType
	 * @param state
	 * @param saledTime
	 * @param useStartDate
	 * @param useEndDate
	 * @param couponCodeType
	 * @param remindTime
	 * @param ruleId
	 * @param channelId
	 * @param price
	 * @param originalprice
	 */
	public CouponCode(String couponCode, String merId, String goodsId, String couponId, String operator,
			int exchangeType, Integer state, Timestamp saledTime, String useStartDate, String useEndDate,
			String couponCodeType, Timestamp remindTime, String ruleId, String channelId,
			Integer price, Integer originalprice) {
		super();
		this.couponCode = couponCode;
		this.merId = merId;
		this.goodsId = goodsId;
		this.couponId = couponId;
		this.operator = operator;
		this.exchangeType = exchangeType;
		this.state = state;
		this.saledTime = saledTime;
		this.useStartDate = useStartDate;
		this.useEndDate = useEndDate;
		this.couponCodeType = couponCodeType;
		this.remindTime = remindTime;
		this.ruleId = ruleId;
		this.channelId = channelId;
		this.price = price;
		this.originalprice = originalprice;
	}


	public Integer getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(Integer originalprice) {
		this.originalprice = originalprice;
	}

	public void trim() {
		if (this.couponCode != null) {
			this.setCouponCode(StringUtils.trim(this.couponCode));
		}
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}
		if (this.goodsId != null) {
			this.setGoodsId(StringUtils.trim(this.goodsId));
		}
		if (this.couponId != null) {
			this.setCouponId(StringUtils.trim(this.couponId));
		}
		if (this.operator != null) {
			this.setOperator(StringUtils.trim(this.operator));
		}
		if (this.useStartDate != null) {
			this.setUseStartDate(StringUtils.trim(this.useStartDate));
		}
		if (this.useEndDate != null) {
			this.setUseEndDate(StringUtils.trim(this.useEndDate));
		}
		if (this.couponCodeType != null) {
			this.setCouponCodeType(StringUtils.trim(this.couponCodeType));
		}
		if (this.ruleId != null) {
			this.setRuleId(StringUtils.trim(this.ruleId));
		}
		if (this.batchId != null) {
			this.setBatchId(StringUtils.trim(this.batchId));
		}
		if (this.orderId != null) {
			this.setOrderId(StringUtils.trim(this.orderId));
		}
		if (this.channelId != null) {
			this.setChannelId(StringUtils.trim(this.channelId));
		}
		if (this.paidMobileId != null) {
			this.setPaidMobileId(StringUtils.trim(this.paidMobileId));
		}
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(int generateMethod) {
		this.generateMethod = generateMethod;
	}

	public String getGenerateMethodName() {
		return generateMethodName;
	}

	public void setGenerateMethodName(String generateMethodName) {
		this.generateMethodName = generateMethodName;
	}

	public int getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(int exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getExchangeTypeName() {
		return exchangeTypeName;
	}

	public void setExchangeTypeName(String exchangeTypeName) {
		this.exchangeTypeName = exchangeTypeName;
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

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
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

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Timestamp getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Timestamp exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getSaledTime() {
		return saledTime;
	}

	public void setSaledTime(Timestamp saledTime) {
		this.saledTime = saledTime;
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

	public String getCouponCodeType() {
		return couponCodeType;
	}

	public void setCouponCodeType(String couponCodeType) {
		this.couponCodeType = couponCodeType;
	}

	public Timestamp getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Timestamp remindTime) {
		this.remindTime = remindTime;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getPaidMobileId() {
		return paidMobileId;
	}

	public void setPaidMobileId(String paidMobileId) {
		this.paidMobileId = paidMobileId;
	}

	public Integer getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount) {
		this.alarmCount = alarmCount;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
}
