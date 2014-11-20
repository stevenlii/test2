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
@ExpTypeAnnotation(fieldStr = { "兑换日期", "兑换券编号", "兑换券名称", "商户编号", "商户名称", "商品编号", "商品名称", "手机号", "金额 (元)" }, 
fieldWidth = {10, 10, 20, 8, 20, 8, 20, 14, 12},
fileName="兑换券兑换明细统计", sheetName="兑换券兑换明细统计")
public class CouponExchangeDetailStats implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String date;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String couponCode;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String couponName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 3)
	private String merCode;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String merName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 5)
	private String goodsCode;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 6)
	private String goodsName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 7)
	private String phone;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 8)
	private Double total;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	
	public String getMerCode() {
		return merCode;
	}
	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}
	
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
}
