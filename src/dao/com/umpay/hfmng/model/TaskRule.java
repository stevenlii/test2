package com.umpay.hfmng.model;

import java.io.Serializable;

/**
 * @author helin created in 2012-12-24 下午4:16:42
 * 
 */
public class TaskRule implements Entity, Serializable {
	private static final long serialVersionUID = 6647944509534464522L;
	private String ruleType;// 规则类型：cyc-周期性,space-定间隔,onlyone-仅一次
	private String cycType;// 周期类型：y-年,m-月,w-周,d-日,h-小时
	private int weeks = -1;// 星期 0-6
	private String isLastDay;// 是否最后一天
	private String cycTime;// 周期时间点
	private String cycTimeDF;// 周期时间数据格式
	private int spaceTime = -1;// 间隔时间（分钟），支持表达式。
	private String runTime;// 单次执行时间点

	public String getRuleType() {
		return null == ruleType ? null : ruleType.trim();
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getCycType() {
		return null == cycType ? null : cycType.trim();
	}

	public void setCycType(String cycType) {
		this.cycType = cycType;
	}

	public int getWeeks() {
		return weeks;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public String getIsLastDay() {
		return null == isLastDay ? null : isLastDay.trim();
	}

	public void setIsLastDay(String isLastDay) {
		this.isLastDay = isLastDay;
	}

	public String getCycTime() {
		return null == cycTime ? null : cycTime.trim();
	}

	public void setCycTime(String cycTime) {
		this.cycTime = cycTime;
	}

	public String getCycTimeDF() {
		return null == cycTimeDF ? null : cycTimeDF.trim();
	}

	public void setCycTimeDF(String cycTimeDF) {
		this.cycTimeDF = cycTimeDF;
	}

	public int getSpaceTime() {
		return spaceTime;
	}

	public void setSpaceTime(int spaceTime) {
		this.spaceTime = spaceTime;
	}

	public String getRunTime() {
		return null == runTime ? null : runTime.trim();
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

}
