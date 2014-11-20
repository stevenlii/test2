package com.umpay.hfmng.model;

import org.apache.commons.lang.StringUtils;
/**
 * ******************  类说明  ******************
 * class       :  ProxyOrder
 * date        :  2013-12-6 
 * @author     :  LiZhen
 * @version    :  V1.0  
 * description :  代理订单
 * @see        :                         
 * **********************************************
 */
public class ProxyOrder implements Entity {
	private String year;//本年，用于查询年表
	private String orderDate;//订单日期,商户生成订单的日期，格式2013MMDD
	private String orderId;//订单编号,由商户产生
	private String subMerId;//二级商户编号
	private String merId;//一级商户编号
	private String goodsId; //商品号
	private String mobileId;//手机号
	private int orderState;//订单状态, 0-订单初始化，第一次查订单表的状态， 1-正在充值中，表示正在去soa充值的状态， 2-充值成功，3-业务服务器返回充值失败， 4-业务服务器请求前置时超时， 5-冲值时发起的签权操作超时错误，6-补发初始化， 7-补发充值中， 8-重发发初始化， 9-重发充值中
	private String amount;//金额
	private String rpid;//交易流水,接收支付结果通知后更新，与交易表rpid相同
	private String businessType;//业务类型,前两位代表业务方式（00：WEB，01：MO，02：无线），后两位代表具体的业务类型（如0201代表无线R1）。0000：未知，0201：R1，0202：R2，0203：R3，0204：R4 ,0205:R5,0206:U付
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("orderDate:").append(orderDate);
		sb.append(";orderId:").append(orderId);
		sb.append(";subMerId:").append(subMerId);
		sb.append(";merId:").append(merId);
		sb.append(";goodsId:").append(goodsId);
		sb.append(";mobileId:").append(mobileId);
		sb.append(";orderState:").append(orderState);
		sb.append(";amount:").append(amount);
		sb.append(";rpid:").append(rpid);
		sb.append(";businessType:").append(businessType);
		return sb.toString();
	}
	
	public void trim(){
		this.setOrderDate(StringUtils.trim(this.orderDate));
		this.setOrderId(StringUtils.trim(this.orderId));
		this.setSubMerId(StringUtils.trim(this.subMerId));
		this.setMerId(StringUtils.trim(this.merId));
		this.setGoodsId(StringUtils.trim(this.goodsId));
		this.setMobileId(StringUtils.trim(this.mobileId));
		this.setRpid(StringUtils.trim(this.rpid));
		this.setBusinessType(StringUtils.trim(this.businessType));
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubMerId() {
		return subMerId;
	}

	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRpid() {
		return rpid;
	}

	public void setRpid(String rpid) {
		this.rpid = rpid;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
