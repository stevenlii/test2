package com.umpay.hfmng.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


public class MerGradeRank implements Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4698138515249058420L;
	private String merName;//商户名称
	private String merId;//商户号
	private Timestamp merInTime;//商户生效时间
	private String operState;//运营状态
	private String operName;//运营负责人名称
	private String gradeRank;//评分等级
	private BigDecimal lastMGrade;//上月评分总分
	private BigDecimal lastLMGrade;//上上月评分总分
	private BigDecimal lastLLMGrade;//上上上月评分总分
	private String isUp;//连续3月呈现上升趋势
	private String isDown;//连续3月呈现下降趋势
	private String month;//所属时间（月份）
	public String toString(){
		return "GradeRankBean[merName:"+merName+"merId:"+merId+";month:"+month+";gradeRank:"+gradeRank+";;lastMGrade:"+lastMGrade+";lastLMGrade:"+lastLMGrade+";lastLLMGrade:"+lastLLMGrade+"]";
	}
	public void trim(){
		this.setMerId(StringUtils.trim(this.merId));
		this.setMerName(StringUtils.trim(this.merName));
		this.setOperState(StringUtils.trim(this.operState));
		this.setOperName(StringUtils.trim(this.operName));
		this.setGradeRank(StringUtils.trim(this.gradeRank));
		this.setIsUp(StringUtils.trim(this.isUp));
		this.setIsDown(StringUtils.trim(this.isDown));
		this.setMonth(StringUtils.trim(this.month));
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public Timestamp getMerInTime() {
		return merInTime;
	}
	public void setMerInTime(Timestamp merInTime) {
		this.merInTime = merInTime;
	}
	public String getOperState() {
		return operState;
	}
	public void setOperState(String operState) {
		this.operState = operState;
	}
	public String getGradeRank() {
		return gradeRank;
	}
	public void setGradeRank(String gradeRank) {
		this.gradeRank = gradeRank;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public BigDecimal getLastMGrade() {
		return lastMGrade;
	}
	public void setLastMGrade(BigDecimal lastMGrade) {
		this.lastMGrade = lastMGrade;
	}
	public BigDecimal getLastLMGrade() {
		return lastLMGrade;
	}
	public void setLastLMGrade(BigDecimal lastLMGrade) {
		this.lastLMGrade = lastLMGrade;
	}
	public BigDecimal getLastLLMGrade() {
		return lastLLMGrade;
	}
	public void setLastLLMGrade(BigDecimal lastLLMGrade) {
		this.lastLLMGrade = lastLLMGrade;
	}
	public String getIsUp() {
		return isUp;
	}
	public void setIsUp(String isUp) {
		this.isUp = isUp;
	}
	public String getIsDown() {
		return isDown;
	}
	public void setIsDown(String isDown) {
		this.isDown = isDown;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
}
