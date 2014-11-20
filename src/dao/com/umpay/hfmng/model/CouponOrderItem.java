package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ParameterPool;
import com.umpay.uniquery.util.StringUtil;

public class CouponOrderItem implements Entity {
	private String couponCode;
	private String couponId;
	private String couponName;
	private String useRange;
	private String couponStyle;
	private String couponState;
	private Integer state;
	private String merId;
	
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
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
	public String getUseRange() {
		return useRange;
	}
	public void setUseRange(String useRange) {
		this.useRange = useRange;
	}
	public String getCouponStyle() {
		if(!StringUtil.isEmpty(couponStyle)){
			String temp = ParameterPool.couponCodeTypes.get(couponStyle.trim());
			if(!StringUtil.isEmpty(temp)){
				couponStyle = temp;
			}
		}
		return couponStyle;
	}
	public void setCouponStyle(String couponStyle) {
		this.couponStyle = couponStyle;
	}
	public String getCouponState() {
		if(!StringUtil.isEmpty(couponState)){
			String temp = ParameterPool.couponCodeStates.get(couponState.trim());
			if(!StringUtil.isEmpty(temp)){
				couponState = temp;
			}
		}
		return couponState;
	}
	public void setCouponState(String couponState) {
		this.couponState = couponState;
	}
	
}
