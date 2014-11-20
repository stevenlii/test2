package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * @ClassName: CouponStatsProvGather
 * @Description: 兑换券功能分省份汇总统计Excel模型类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-7-25
 */
@ExpTypeAnnotation(fieldStr = { "日期(日/月)", "省份", "渠道编号", "渠道名称", "商户编号", "商户名称", "商品编号", "商品名称", "售出数量", "售出总金额 (元)",
		"兑换数量", "兑换总金额 (元)" }, fieldWidth = { 10, 8, 10, 20, 10, 20, 10, 20, 8, 16, 8, 16 }, fileName = "电子兑换券分省份汇总统计", sheetName = "分省份汇总统计")
public class CouponStatsProvGather implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String statDate;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String provName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String channelId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 3)
	private String channelName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String merId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 5)
	private String merName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 6)
	private String goodsId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 7)
	private String goodsName;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 8)
	private Integer saledNum;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 9)
	private Double saledSum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 10)
	private Integer exchangedNum;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 11)
	private Double exchangedSum;

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public Integer getSaledNum() {
		return saledNum;
	}

	public void setSaledNum(Integer saledNum) {
		this.saledNum = saledNum;
	}

	public Double getSaledSum() {
		return saledSum;
	}

	public void setSaledSum(Double saledSum) {
		this.saledSum = saledSum;
	}

	public Integer getExchangedNum() {
		return exchangedNum;
	}

	public void setExchangedNum(Integer exchangedNum) {
		this.exchangedNum = exchangedNum;
	}

	public Double getExchangedSum() {
		return exchangedSum;
	}

	public void setExchangedSum(Double exchangedSum) {
		this.exchangedSum = exchangedSum;
	}

}
