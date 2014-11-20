package com.umpay.hfmng.model;

import org.apache.commons.lang.StringUtils;

/** 
 * 退费明细
 * <p>创建日期：2013-12-13 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public class StlPayBack implements Entity{
	
	private String	paybackid;//序列号
	private String	stlmonth;//结算月份
	private String	undodate;//日期
	private String	mobileid;//手机号
	private String	bankname;//商品银行
	private String	provname;//省份
	private String	refundamount;//退费金额
	private String	mertype;//商户类型
	private String	mername;//所属商户
	private String	accid;//商户号
	private String	goodsid;//子商户号
	private String	goodsname;//子商户名
	private String	withdrawreason;//退费原因
	private String	desc;//详细描述
	private Integer	isuse;//状态  2-有效;4-无效

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CouponPayBack[")
			.append("paybackid:"+paybackid)
			.append(";stlmonth:"+stlmonth)
			.append(";undodate:"+undodate)
			.append(";mobileid:"+mobileid)
			.append(";bankname:"+bankname)
			.append(";provname:"+provname)
			.append(";refundamount:"+refundamount)
			.append(";mertype:"+mertype)
			.append(";mername:"+mername)
			.append(";accid:"+accid)
			.append(";goodsid:"+goodsid)
			.append(";goodsname:"+goodsname)
			.append(";withdrawreason:"+withdrawreason)
			.append(";desc:"+desc)
			.append(";isuse:"+isuse)
			.append("]");
		return sb.toString();
	}

	public void trim(){
		if(this.paybackid != null){
    		this.setPaybackid(StringUtils.trim(this.paybackid));
    	}
		if(this.stlmonth != null){
			this.setStlmonth(StringUtils.trim(this.stlmonth));
		}
		if(this.undodate != null){
			this.setUndodate(StringUtils.trim(this.undodate));
		}
		if(this.mobileid != null){
			this.setMobileid(StringUtils.trim(this.mobileid));
		}
		if(this.bankname != null){
			this.setBankname(StringUtils.trim(this.bankname));
		}
		if(this.provname != null){
			this.setProvname(StringUtils.trim(this.provname));
		}
		if(this.refundamount != null){
			this.setRefundamount(StringUtils.trim(this.refundamount));
		}
		if(this.mertype != null){
			this.setMertype(StringUtils.trim(this.mertype));
		}
		if(this.mername != null){
			this.setMername(StringUtils.trim(this.mername));
		}
		if(this.accid != null){
			this.setAccid(StringUtils.trim(this.accid));
		}
		if(this.goodsid != null){
			this.setGoodsid(StringUtils.trim(this.goodsid));
		}
		if(this.goodsname != null){
			this.setGoodsname(StringUtils.trim(this.goodsname));
		}
		if(this.withdrawreason != null){
			this.setWithdrawreason(StringUtils.trim(this.withdrawreason));
		}
		if(this.desc != null){
			this.setDesc(StringUtils.trim(this.desc));
		}
	}
	
	public String getPaybackid() {
		return paybackid;
	}

	public void setPaybackid(String paybackid) {
		this.paybackid = paybackid;
	}
	public String getStlmonth() {
		return stlmonth;
	}

	public void setStlmonth(String stlmonth) {
		this.stlmonth = stlmonth;
	}

	public String getUndodate() {
		return undodate;
	}

	public void setUndodate(String undodate) {
		this.undodate = undodate;
	}

	public String getMobileid() {
		return mobileid;
	}

	public void setMobileid(String mobileid) {
		this.mobileid = mobileid;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getProvname() {
		return provname;
	}

	public void setProvname(String provname) {
		this.provname = provname;
	}

	public String getRefundamount() {
		return refundamount;
	}

	public void setRefundamount(String refundamount) {
		this.refundamount = refundamount;
	}

	public String getMertype() {
		return mertype;
	}

	public void setMertype(String mertype) {
		this.mertype = mertype;
	}

	public String getMername() {
		return mername;
	}

	public void setMername(String mername) {
		this.mername = mername;
	}

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getWithdrawreason() {
		return withdrawreason;
	}

	public void setWithdrawreason(String withdrawreason) {
		this.withdrawreason = withdrawreason;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getIsuse() {
		return isuse;
	}

	public void setIsuse(Integer isuse) {
		this.isuse = isuse;
	}

}
