/**
 * @ClassName: CouponRuleStats
 * @Description: TODO
 * @author panyouliang
 * @date 2013-3-25 下午1:49:44
 */
package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * @author MARCO.PAN
 *
 */
@ExpTypeAnnotation(fieldStr = { "规则编号", "商户编号", "商户名称", "商品编号", "商品名称", "售出数量", "售出总金额 (元)", "兑换数量", "兑换总金额(元)", "注销数量", "注销总金额(元)", "过期数量", "过期总金额(元)" }, 
fieldWidth = {12, 10, 20, 10, 20, 8, 16, 8, 16, 8, 16, 8, 16},
fileName="兑换券规则统计", sheetName="兑换券规则统计")
public class CouponRuleStats implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String ruleId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String merId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String merName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 3)
	private String goodsId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String goodsName;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 5)
	private Integer saledCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 6)
	private Double saledSum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 7)
	private Integer exchangedCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 8)
	private Double exchangedSum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 9)
	private Integer cancelCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 10)
	private Double cancelSum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 11)
	private Integer overdueCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 12)
	private Double overdueSum;
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
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
	
	public Integer getSaledCount() {
		return saledCount;
	}
	public void setSaledCount(Integer saledCount) {
		this.saledCount = saledCount;
	}
	
	public Double getSaledSum() {
		return saledSum;
	}
	public void setSaledSum(Double saledSum) {
		this.saledSum = saledSum;
	}
	
	public Integer getExchangedCount() {
		return exchangedCount;
	}
	public void setExchangedCount(Integer exchangedCount) {
		this.exchangedCount = exchangedCount;
	}
	
	public Double getExchangedSum() {
		return exchangedSum;
	}
	public void setExchangedSum(Double exchangedSum) {
		this.exchangedSum = exchangedSum;
	}
	
	public Integer getCancelCount() {
		return cancelCount;
	}
	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}
	
	public Double getCancelSum() {
		return cancelSum;
	}
	public void setCancelSum(Double cancelSum) {
		this.cancelSum = cancelSum;
	}
	
	public Integer getOverdueCount() {
		return overdueCount;
	}
	public void setOverdueCount(Integer overdueCount) {
		this.overdueCount = overdueCount;
	}
	
	public Double getOverdueSum() {
		return overdueSum;
	}
	public void setOverdueSum(Double overdueSum) {
		this.overdueSum = overdueSum;
	}
}
