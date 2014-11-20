/**
 * @ClassName: CouponOrder
 * @Description: 兑换劵订单实体
 * @author panyouliang
 * @date 2013-1-14 上午11:16:57
 */
package com.umpay.hfmng.model;

import java.util.List;

import org.springframework.util.StringUtils;

import com.umpay.hfmng.common.ParameterPool;
import com.umpay.uniquery.util.StringUtil;

public class CouponOrder implements Entity {
	private String orderId;
	private String phone;
	private Double amount;
	private String amountStr;
	private String merId;
	private String merName;
	private String goodsId;
	private String goodsName;
	private String accessType;
	private String orderState;
	private String payTime;
	private String payState;
	private String genTime;
	private Integer state;
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	private List<CouponOrderItem> items;
	public List<CouponOrderItem> getItems() {
		return items;
	}
	public void setItems(List<CouponOrderItem> items) {
		this.items = items;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		setAmountStr(amount);
		this.amount = amount;
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
	public String getAccessType() {
		if(!StringUtil.isEmpty(accessType)){
			accessType = ParameterPool.couponJoinTypes.get(accessType.trim());
		}
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getOrderState() {
		if(!StringUtil.isEmpty(orderState)){
			state = new Integer(orderState.trim());
			orderState = ParameterPool.couponOrderStates.get(orderState.trim());
		}
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getPayTime() {
		if(!StringUtil.isEmpty(payTime)){
			if(payTime.contains(".")){
				payTime = payTime.substring(0, payTime.indexOf("."));
			}
		}
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayState() {
		if(!StringUtil.isEmpty(payState)){
			payState = ParameterPool.couponTransStates.get(payState.trim());
		}
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}
	public String getGenTime() {
		if(!StringUtil.isEmpty(genTime)){
			if(genTime.contains(".")){
				genTime = genTime.substring(0, genTime.indexOf("."));
			}
		}
		return genTime;
	}
	public void setGenTime(String genTime) {
		this.genTime = genTime;
	}
	
	public String getAmountStr() {
		return amountStr;
	}
	private void setAmountStr(Double amount) {
		if(amount == null){
			this.amountStr = "0.00";
			return;
		}
		String amountTemp = amount.toString();
		int index = amountTemp.indexOf(".");
		if(index < 0){
			this.amountStr = amount + ".00";
			return;
		}else if(amountTemp.trim().substring(index + 1).length() < 2){
			this.amountStr = amount + "0";
			return;
		}
		this.amountStr = amountTemp;
	}
}
