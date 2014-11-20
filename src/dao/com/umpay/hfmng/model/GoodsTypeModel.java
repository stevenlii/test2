package com.umpay.hfmng.model;

import org.apache.commons.lang.StringUtils;

/**
 * ******************  类说明  *********************
 * class       :  GoodsTypeModel
 * @author     :  Administrator
 * @version    :  1.0  
 * description :  Model类，对应数据中  表名:T_GOODSTYPE 解释:商品类型信息表
 * @see        :                        
 * ***********************************************
 */
public class GoodsTypeModel {
  private  String goodsType; //商品类型
  private String briefName;  //简称
  private String isCOntrol;  //是否控制
  private String detail;   //描述
  
  public String toString(){
	  return "GoodeTypeModel[goodsType:"+goodsType+",briefName:"+briefName+",isControl:"+isCOntrol+",detail:"+detail+"]";
  }
  public void trim(){
	  this.setGoodsType(StringUtils.trim(goodsType));
	  this.setBriefName(StringUtils.trim(briefName));
	  this.setIsCOntrol(StringUtils.trim(isCOntrol));
	  this.setDetail(StringUtils.trim(detail));
  }
public String getGoodsType() {
	return goodsType;
}
public void setGoodsType(String goodsType) {
	this.goodsType = goodsType;
}
public String getBriefName() {
	return briefName;
}
public void setBriefName(String briefName) {
	this.briefName = briefName;
}
public String getIsCOntrol() {
	return isCOntrol;
}
public void setIsCOntrol(String isCOntrol) {
	this.isCOntrol = isCOntrol;
}
public String getDetail() {
	return detail;
}
public void setDetail(String detail) {
	this.detail = detail;
}
  
}
