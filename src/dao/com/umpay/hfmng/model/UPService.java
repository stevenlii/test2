/** *****************  JAVA头文件说明  ****************
 * file name  :  UpService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-9-25
 * *************************************************/ 

package com.umpay.hfmng.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  UpService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  综合支付计费代码信息表(UMPAY.T_UPSERVICE_INF)
 * ************************************************/

public class UPService implements Entity, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String merId; 	// 所属商户号
	private String goodsId;	// 商品号
	private String gateId;	//通道ID
	private String serviceId;	//计费代码
	private String serviceName;	//计费代码名称
	private String serviceId1;	//计费代码
	private String serviceName1;	//计费代码名称
	private String serviceId2;	//计费代码
	private String serviceName2;	//计费代码名称
	private String state; 	//状态
	private Integer amount;	//交易金额（扣款金额） 
	private String feeType;	//计费类型
	private String busiDesc;	//业务描述
	private String modUser; // 修改人
	private String merPriv;	//商家私有域(保留字段)
	private String expand;	//商家扩展信息(保留字段)
	private Timestamp inTime; // 入库时间
	private Timestamp modTime; // 修改时间
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("merId:").append(merId);
		sb.append(";goodsId:").append(goodsId);
		sb.append(";gateId:").append(gateId);
		sb.append(";serviceId:").append(serviceId);
		sb.append(";serviceName:").append(serviceName);
		sb.append(";serviceId1:").append(serviceId1);
		sb.append(";serviceName1:").append(serviceName1);
		sb.append(";serviceId2:").append(serviceId2);
		sb.append(";serviceName2:").append(serviceName2);
		sb.append(";state:").append(state);
		sb.append(";amount:").append(amount);
		sb.append(";feeType:").append(feeType);
		sb.append(";busiDesc:").append(busiDesc);
		sb.append(";merPriv:").append(merPriv);
		sb.append(";extend:").append(expand);
		sb.append(";modUser:").append(modUser);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		return sb.toString();
	}
	
	public String getServiceId() {
		return StringUtils.trim(serviceId);
	}
	public void setServiceId(String serviceId) {
		this.serviceId = StringUtils.trim(serviceId);
	}
	public String getMerId() {
		return StringUtils.trim(merId);
	}
	public void setMerId(String merId) {
		this.merId = StringUtils.trim(merId);
	}
	public String getGoodsId() {
		return StringUtils.trim(goodsId);
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = StringUtils.trim(goodsId);
	}
	public String getMerPriv() {
		return StringUtils.trim(merPriv);
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = StringUtils.trim(merPriv);
	}
	public String getExpand() {
		return StringUtils.trim(expand);
	}
	public void setExpand(String expand) {
		this.expand = StringUtils.trim(expand);
	}
	public String getGateId() {
		return StringUtils.trim(gateId);
	}
	public void setGateId(String gateId) {
		this.gateId = StringUtils.trim(gateId);
	}
	public String getState() {
		return StringUtils.trim(state);
	}
	public void setState(String state) {
		this.state = StringUtils.trim(state);
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getFeeType() {
		return StringUtils.trim(feeType);
	}
	public void setFeeType(String feeType) {
		this.feeType = StringUtils.trim(feeType);
	}
	public String getBusiDesc() {
		return StringUtils.trim(busiDesc);
	}
	public void setBusiDesc(String busiDesc) {
		this.busiDesc = StringUtils.trim(busiDesc);
	}
	public String getModUser() {
		return StringUtils.trim(modUser);
	}
	public void setModUser(String modUser) {
		this.modUser = StringUtils.trim(modUser);
	}
	public Timestamp getInTime() {
		return inTime;
	}
	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
	public Timestamp getModTime() {
		return modTime;
	}
	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceId1() {
		return serviceId1;
	}

	public void setServiceId1(String serviceId1) {
		this.serviceId1 = serviceId1;
	}

	public String getServiceName1() {
		return serviceName1;
	}

	public void setServiceName1(String serviceName1) {
		this.serviceName1 = serviceName1;
	}

	public String getServiceId2() {
		return serviceId2;
	}

	public void setServiceId2(String serviceId2) {
		this.serviceId2 = serviceId2;
	}

	public String getServiceName2() {
		return serviceName2;
	}

	public void setServiceName2(String serviceName2) {
		this.serviceName2 = serviceName2;
	}

}
