package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * 
 * @ClassName: CouponStatsChannelGather
 * @Description: 兑换券功能渠道汇总统计Excel模型类
 * @author wanyong
 * @date 2013-3-28 下午05:48:44
 */
@ExpTypeAnnotation(fieldStr = { "日期(日/月)", "渠道编号", "渠道名称", "售出数量", "售出总金额 (元)" }, fieldWidth = { 10, 10, 20, 8, 16 }, fileName = "电子兑换券功能渠道(日月)汇总统计", sheetName = "渠道(日月)汇总统计")
public class CouponStatsChannelGather implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String statDate;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String channelId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String channelName;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 3)
	private Integer saledNum;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 4)
	private Double saledSum;

	
	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
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

}
