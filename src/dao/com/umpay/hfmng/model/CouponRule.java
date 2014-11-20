package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: CouponRule
 * @Description: 兑换券规则model类
 * @author wanyong
 * @date 2012-12-11 下午04:44:16
 */
public class CouponRule implements Entity {
	private String ruleId;
	private String auditUser;
	private Timestamp modTime;
	private Integer goodsSum;
	private Integer state;
	private String stateName; // 状态中文显示
	private String merId;
	private String merName; // 商户名称
	private String goodsId;
	private String goodsName; // 商品名称
	private String couponId;
	private String couponName; // 兑换券名称
	private Integer generateMethod;
	private String generateMethodName; // 兑换码生成方式中文显示
	private String couponCodeType;
	private String couponCodeTypeName; // 兑换码形式中文显示
	private Integer saledSum; // 售出量
	private String sellStartDate;
	private String sellEndDate;
	private String useStartDate;
	private String useEndDate;
	private Integer effType; // 兑换码使用有效期类型
	private String effTypeName; // 兑换码使用有效期类型中文显示
	private Integer effDays; // 兑换码使用有效期天数
	private Integer price;
	private String submitUser;
	private Timestamp inTime;
	private Integer effWarnDays; // 订单兑换有效期告警天数
	private Integer switchAmt; // 库存告警阀值
	private Integer alarmCount; // 剩余告警次数
	private String alarmEmail;
	private String alarmMobile;
	private Integer ruletype;
	private Integer originalprice;
	private Integer export;
	
	private Integer consumetype;
	private Integer ismulti;
	private Integer istransfer;
	

	public Integer getConsumetype() {
		return consumetype;
	}

	public void setConsumetype(Integer consumetype) {
		this.consumetype = consumetype;
	}

	public Integer getIsmulti() {
		return ismulti;
	}

	public void setIsmulti(Integer ismulti) {
		this.ismulti = ismulti;
	}

	public Integer getIstransfer() {
		return istransfer;
	}

	public void setIstransfer(Integer istransfer) {
		this.istransfer = istransfer;
	}

	public void trim() {
		if (this.ruleId != null) {
			this.setRuleId(StringUtils.trim(this.ruleId));
		}
		if (this.auditUser != null) {
			this.setAuditUser(StringUtils.trim(this.auditUser));
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
		if (this.couponCodeType != null) {
			this.setCouponCodeType(StringUtils.trim(this.couponCodeType));
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
		if (this.submitUser != null) {
			this.setSubmitUser(StringUtils.trim(this.submitUser));
		}
		if (this.alarmEmail != null) {
			this.setAlarmEmail(StringUtils.trim(this.alarmEmail));
		}
		if (this.alarmMobile != null) {
			this.setAlarmMobile(StringUtils.trim(this.alarmMobile));
		}
	}

	public String toString() {
		return "CouponRule[ruleId:" + ruleId + ";auditUser:" + auditUser
				+ ";modTime:" + modTime + ";goodsSum:" + goodsSum + ";state:"
				+ state + ";merId:" + merId + ";goodsId:" + goodsId
				+ ";couponId:" + couponId + ";generateMethod:" + generateMethod
				+ ";couponCodeType:" + couponCodeType + ";saledSum:" + saledSum
				+ ";sellStartDate:" + sellStartDate + ";sellEndDate:"
				+ sellEndDate + ";useStartDate:" + useStartDate
				+ ";useEndDate:" + useEndDate + ";effType:" + effType
				+ ";effDays:" + effDays + ";price:" + price + ";submitUser:"
				+ submitUser + ";inTime:" + inTime + ";effWarnDays:"
				+ effWarnDays + ";switchAmt:" + switchAmt + ";alarmCount:"
				+ alarmCount + ";alarmEmail:" + alarmEmail + ";alarmMobile:"
				+ alarmMobile + "]";
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(Integer generateMethod) {
		this.generateMethod = generateMethod;
	}

	public String getGenerateMethodName() {
		return generateMethodName;
	}

	public void setGenerateMethodName(String generateMethodName) {
		this.generateMethodName = generateMethodName;
	}

	public String getCouponCodeType() {
		return couponCodeType;
	}

	public void setCouponCodeType(String couponCodeType) {
		this.couponCodeType = couponCodeType;
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

	public Integer getEffType() {
		return effType;
	}

	public void setEffType(Integer effType) {
		this.effType = effType;
	}

	public String getEffTypeName() {
		return effTypeName;
	}

	public void setEffTypeName(String effTypeName) {
		this.effTypeName = effTypeName;
	}

	public Integer getEffDays() {
		return effDays;
	}

	public void setEffDays(Integer effDays) {
		this.effDays = effDays;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public Integer getEffWarnDays() {
		return effWarnDays;
	}

	public void setEffWarnDays(Integer effWarnDays) {
		this.effWarnDays = effWarnDays;
	}

	public String getAlarmEmail() {
		return alarmEmail;
	}

	public void setAlarmEmail(String alarmEmail) {
		this.alarmEmail = alarmEmail;
	}

	public String getAlarmMobile() {
		return alarmMobile;
	}

	public void setAlarmMobile(String alarmMobile) {
		this.alarmMobile = alarmMobile;
	}

	public String getCouponCodeTypeName() {
		return couponCodeTypeName;
	}

	public void setCouponCodeTypeName(String couponCodeTypeName) {
		this.couponCodeTypeName = couponCodeTypeName;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public Integer getSwitchAmt() {
		return switchAmt;
	}

	public void setSwitchAmt(Integer switchAmt) {
		this.switchAmt = switchAmt;
	}

	public Integer getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount) {
		this.alarmCount = alarmCount;
	}

	public Integer getRuletype() {
		return ruletype;
	}

	public void setRuletype(Integer ruletype) {
		this.ruletype = ruletype;
	}

	public Integer getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(Integer originalprice) {
		this.originalprice = originalprice;
	}

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}
}
