/**
 * 
 */
package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;


/**
 * @author MARCO-PAN
 *
 */
@ExpTypeAnnotation(fieldStr = { "兑换券编号", "兑换券名称", "售出数量", "售出总金额 (元)", "兑换数量", "兑换总金额 (元)" }, 
fieldWidth = {10, 20, 8, 16, 8, 16},
fileName="兑换券汇总统计", sheetName="兑换券汇总统计")
public class CouponGatherStats implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String couponId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String couponName;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 2)
	private Integer saledCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 3)
	private Double  saledSum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 4)
	private Integer exchangedCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 5)
	private Double  exchangedSum;
	
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
	
	public Integer getSaledCount() {
		return saledCount;
	}
	public void setSaledCount(Integer saledCount) {
		this.saledCount = saledCount;
	}
	
	public Double  getSaledSum() {
		return saledSum;
	}
	public void setSaledSum(Double  saledSum) {
		this.saledSum = saledSum;
	}
	
	public Integer getExchangedCount() {
		return exchangedCount;
	}
	public void setExchangedCount(Integer exchangedCount) {
		this.exchangedCount = exchangedCount;
	}
	
	public Double  getExchangedSum() {
		return exchangedSum;
	}
	public void setExchangedSum(Double exchangedSum) {
		this.exchangedSum = exchangedSum;
	}
	
}
