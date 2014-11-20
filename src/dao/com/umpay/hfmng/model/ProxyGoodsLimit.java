/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoods.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.umpay.hfmng.common.JsonHFUtil;


/** ******************  类说明  *********************
 * class       :  ProxyGoods
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class ProxyGoodsLimit extends ProxyGoods implements Entity {
	
	private int dayLimit;  //日销售限制数量,-1表示不限制
	private int alarmLimit;  //预警数,-1表示不限制
	private String alarmTel;  //预警短信号码
	
	public String toString(){
		StringBuffer sb=new StringBuffer(super.toString());
		sb.append(";dayLimit:").append(dayLimit);
		sb.append(";alarmLimit:").append(alarmLimit);
		sb.append(";alarmTel:").append(alarmTel);
		return sb.toString();
	}
	
	public void trim(){
		super.trim();
		this.setAlarmTel(StringUtils.trim(this.alarmTel));
	}
	public int getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(int dayLimit) {
		this.dayLimit = dayLimit;
	}
	public int getAlarmLimit() {
		return alarmLimit;
	}
	public void setAlarmLimit(int alarmLimit) {
		this.alarmLimit = alarmLimit;
	}
	public String getAlarmTel() {
		return alarmTel;
	}
	public void setAlarmTel(String alarmTel) {
		this.alarmTel = alarmTel;
	}
	
	public static void main(String[] args) {
		ProxyGoodsLimit p = new ProxyGoodsLimit();
		p.setMerId("9996");
		p.setSubMerId("019996");
		p.setGoodsId("200");
		p.setState(2);
		p.setAlarmLimit(5);
		p.setDayLimit(5);
		p.setAlarmTel("12365478910");
		String jsonString = JsonHFUtil.getJsonArrStrFrom(p);
		System.out.println(jsonString);
	}

}
