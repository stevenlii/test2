package com.umpay.hfmng.model;

import java.io.Serializable;

/**
 * ******************  类说明  ******************
 * class       :  SegMbl
 * date        :  2013-12-25 
 * @author     :  LiZhen
 * @version    :  V1.0  
 * description :  号段
 * @see        :                         
 * **********************************************
 */
public class SegMbl implements Entity,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6348403354061878539L;
	private String mobileId;//手机号码前7位
	private String dealerName;//运营商
	private String cardType;//电话卡类型编号
	private String areaCode;//电话区号
	private String provName;//省名
	private String areaName;//市名
	private String provCode;//省代码
	private String dealerCode;//运营商ID
	private String cardName;//电话卡类型名称
	private String netType;//0为boss网 1为智能网 (modify by wangyuxin,2014-04-10)
	
//	public static boolean parserData(SegMbl segMbl,String src){
//		String[] strArr=src.split(",");
//		if(strArr.length!=4){
//			return false;
//		}
//		if(!RegExpElement.PROVCODE_ID.matcher(strArr[0]).matches())
//			return false;
//		if(!RegExpElement.SEGMOBILE_ID.matcher(strArr[1]).matches())
//			return false;
//		if(!RegExpElement.AREACODE_ID.matcher(strArr[2]).matches())
//			return false;
//		if(!RegExpElement.NETTYPE_ID.matcher(strArr[3]).matches())
//			return false;
//		segMbl.setProvName(strArr[0]);
//		segMbl.setAreaName(strArr[1]);
//		segMbl.setAreaCode(strArr[2]);
//		segMbl.setMobileId(strArr[3]);
//		return true;
//	}
	
	public String getMobileId() {
		return mobileId;
	}
	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getProvCode() {
		return provCode;
	}
	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
}
