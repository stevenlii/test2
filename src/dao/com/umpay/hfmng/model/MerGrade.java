/** *****************  JAVA头文件说明  ****************
 * file name  :  MerGrade.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-2-20
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  MerGrade
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户评分表（（T-1）月的数据）
 * ************************************************/

public class MerGrade implements Entity {
	private String merName;// 商户名称
	private String merId;//商户号
	private String operator;//运营负责人
	private Timestamp merInTime;//商户生效时间，来自meri_inf表的inTime
	private String month;//所属时间（月份） yyyy-MM，（T-1）月
	private BigDecimal turnover;//交易额
	private BigDecimal lastTurnover;//（T-2）月交易额
	private BigDecimal riseRate;//交易额增产率
	private BigDecimal reduceSum;//账单核减金额   （T-2）月核减金额   （T-2）核减率=（T-2）核减金额/（T-2）交易额
	private BigDecimal reduceRate;//账单核减率
	private Integer tradingCount;//交易笔数
	private Integer complaintCount;//客诉笔数
	private BigDecimal turnoverIndex;//交易额指标
	private BigDecimal riseRateIndex;//交易额增长率指标
	private BigDecimal falseTradeIndex;//虚假交易指标
	private BigDecimal complaintIndex;//客诉指标
	private BigDecimal sysStabIndex;//系统稳定性指标
	private BigDecimal cooperateIndex;//配合力度指标
	private BigDecimal breachIndex;//违约情况指标
	private BigDecimal upgradeIndex;//升级重大投诉指标
	private BigDecimal marketingIndex;//营销活动指标
	private BigDecimal supportIndex;//业务资源支持指标
	private BigDecimal total;//总分
	private int modLock; // 修改锁    1：锁定中,0:未锁定
	private String modUser;//修改人
	private Timestamp modTime;//修改时间
	private Timestamp inTime;//添加时间
	
	private String operState;// 运营状态     试运营，正常
	
	public String toString(){
		return "MerGrade[merName:"+merName+";merId:"+merId+";month:"+month+";turnover:"+turnover+";lastTurnover:"+lastTurnover
		+";riseRate:"+riseRate+";reduceSum:"+reduceSum+";reduceRate:"+reduceRate+";tradingCount:"+tradingCount
		+";complaintCount:"+complaintCount+";turnoverIndex:"+turnoverIndex+";riseRateIndex:"+riseRateIndex
		+";falseTradeIndex:"+falseTradeIndex+";complaintIndex:"+complaintIndex+";sysStabIndex:"+sysStabIndex
		+";cooperateIndex:"+cooperateIndex+";breachIndex:"+breachIndex+";upgradeIndex:"+upgradeIndex
		+";marketingIndex:"+marketingIndex+";supportIndex:"+supportIndex+";total:"+total+";operState:"+operState
		+";modLock:"+modLock+";modUser:"+modUser+";modTime:"+modTime+";inTime:"+inTime+";]";
	}
	
	public void trim(){
		this.setMerName(StringUtils.trim(this.merName));
		this.setMerId(StringUtils.trim(this.merId));
		this.setMonth(StringUtils.trim(this.month));
		this.setModUser(StringUtils.trim(this.modUser));
		this.setOperator(StringUtils.trim(this.operator));
		this.setOperState(StringUtils.trim(this.operState));
	}

	public String getOperState() {
		return operState;
	}

	public void setOperState(String operState) {
		this.operState = operState;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Timestamp getMerInTime() {
		return merInTime;
	}

	public void setMerInTime(Timestamp merInTime) {
		this.merInTime = merInTime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public BigDecimal getTurnover() {
		return turnover;
	}

	public void setTurnover(BigDecimal turnover) {
		this.turnover = turnover;
	}

	public BigDecimal getLastTurnover() {
		return lastTurnover;
	}

	public void setLastTurnover(BigDecimal lastTurnover) {
		this.lastTurnover = lastTurnover;
	}

	public BigDecimal getRiseRate() {
		return riseRate;
	}

	public void setRiseRate(BigDecimal riseRate) {
		this.riseRate = riseRate;
	}

	public BigDecimal getReduceSum() {
		return reduceSum;
	}

	public void setReduceSum(BigDecimal reduceSum) {
		this.reduceSum = reduceSum;
	}

	public BigDecimal getReduceRate() {
		return reduceRate;
	}

	public void setReduceRate(BigDecimal reduceRate) {
		this.reduceRate = reduceRate;
	}

	public Integer getTradingCount() {
		return tradingCount;
	}

	public void setTradingCount(Integer tradingCount) {
		this.tradingCount = tradingCount;
	}

	public Integer getComplaintCount() {
		return complaintCount;
	}

	public void setComplaintCount(Integer complaintCount) {
		this.complaintCount = complaintCount;
	}

	public BigDecimal getTurnoverIndex() {
		return turnoverIndex;
	}

	public void setTurnoverIndex(BigDecimal turnoverIndex) {
		this.turnoverIndex = turnoverIndex;
	}

	public BigDecimal getRiseRateIndex() {
		return riseRateIndex;
	}

	public void setRiseRateIndex(BigDecimal riseRateIndex) {
		this.riseRateIndex = riseRateIndex;
	}

	public BigDecimal getFalseTradeIndex() {
		return falseTradeIndex;
	}

	public void setFalseTradeIndex(BigDecimal falseTradeIndex) {
		this.falseTradeIndex = falseTradeIndex;
	}

	public BigDecimal getComplaintIndex() {
		return complaintIndex;
	}

	public void setComplaintIndex(BigDecimal complaintIndex) {
		this.complaintIndex = complaintIndex;
	}

	public BigDecimal getSysStabIndex() {
		return sysStabIndex;
	}

	public void setSysStabIndex(BigDecimal sysStabIndex) {
		this.sysStabIndex = sysStabIndex;
	}

	public BigDecimal getCooperateIndex() {
		return cooperateIndex;
	}

	public void setCooperateIndex(BigDecimal cooperateIndex) {
		this.cooperateIndex = cooperateIndex;
	}

	public BigDecimal getBreachIndex() {
		return breachIndex;
	}

	public void setBreachIndex(BigDecimal breachIndex) {
		this.breachIndex = breachIndex;
	}

	public BigDecimal getUpgradeIndex() {
		return upgradeIndex;
	}

	public void setUpgradeIndex(BigDecimal upgradeIndex) {
		this.upgradeIndex = upgradeIndex;
	}

	public BigDecimal getMarketingIndex() {
		return marketingIndex;
	}

	public void setMarketingIndex(BigDecimal marketingIndex) {
		this.marketingIndex = marketingIndex;
	}

	public BigDecimal getSupportIndex() {
		return supportIndex;
	}

	public void setSupportIndex(BigDecimal supportIndex) {
		this.supportIndex = supportIndex;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public int getModLock() {
		return modLock;
	}

	public void setModLock(int modLock) {
		this.modLock = modLock;
	}

	public String getModUser() {
		return modUser;
	}

	public void setModUser(String modUser) {
		this.modUser = modUser;
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
	
}
