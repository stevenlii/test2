/**
 * 
 */
package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;


/**
 * @author MARCO_PAN
 *
 */
@ExpTypeAnnotation(fieldStr = { "日期(日/月)", "商户编号", "商户名称", "售出数量", "售出总金额 (元)", "兑换数量", "兑换总金额(元)" }, 
fieldWidth = {10, 10, 20, 8, 16, 8, 16},
fileName="商户兑换券日月统计", sheetName="商户兑换券日月统计")
public class CouponMerDayMonthStats implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String md;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String merId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String merName;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 3)
	private Integer saledNum;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 4)
	private Double saledSum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 5)
	private Integer exchangedNum;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 6)
	private Double exchangedSum;
	
	public String getMd() {
		return md;
	}
	public void setMd(String md) {
		this.md = md;
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
