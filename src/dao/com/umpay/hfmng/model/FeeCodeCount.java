/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCodeCount.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-7
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/** ******************  类说明  *********************
 * class       :  FeeCodeCount
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  计费代码计算model
 * @see        :                        
 * ************************************************/

public class FeeCodeCount {
  private  String serviceId;
  private  String  useCount;
  private String matchType;
  private Timestamp inTime;
public String getServiceId() {
	return serviceId;
}
	public String toString() {
		return "计费代码匹配度    FeeCodeCount[serviceid:" + serviceId + ",usecount:"
				+ useCount + ",matchType:" + matchType + "]";
	}
public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}
public String getUseCount() {
	return useCount;
}
public void setUseCount(String useCount) {
	this.useCount = useCount;
}
public String getMatchType() {
	return matchType;
}
public void setMatchType(String matchType) {
	this.matchType = matchType;
}
public Timestamp getInTime() {
	return inTime;
}
public void setInTime(Timestamp inTime) {
	this.inTime = inTime;
}
  public void  trim(){
	  if(this.serviceId != null){
		  this.setServiceId(StringUtils.trim(this.serviceId)); 
	  }
	  if(this.useCount != null){
		  this.setUseCount(StringUtils.trim(this.useCount));
	  }
	  if(this.matchType != null){
		  this.setMatchType(StringUtils.trim(this.matchType));
	  }
  }
}
