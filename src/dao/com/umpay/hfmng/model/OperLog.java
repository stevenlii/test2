package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * ******************  类说明  *********************
 * class       :  OperLog
 * @author     :  xhf
 * @version    :  1.0  
 * description :  操作日志类
 * ***********************************************
 */
public class OperLog implements Entity {
	
	private String operId;    //YYYYMMDD10位序列号
	private String tableName;  //操作的数据库表英文名
	private String dataId;     //所操作数据的主键
	private String operType;   //操作类型  1：创建；2：修改；3：审核；4：删除；5：启用；6：禁用
	private String detail;     //明细
	private String modUser;	   //操作人
	private Timestamp inTime; //操作时间
	
	public String toString(){
		return "OperLog[operId:"+operId+";tableName:"+tableName+";dataId:"+dataId+";operType:"+operType
					+";detail:"+detail+";modUser:"+modUser+";inTime:"+inTime+";]";
	}
	
	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getModUser() {
		return modUser;
	}
	public void setModUser(String modUser) {
		this.modUser = modUser;
	}
	
	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public void trim(){
		if(this.tableName != null){
    		this.setTableName(StringUtils.trim(this.tableName));
    	}
		if(this.dataId != null){
    		this.setDataId(StringUtils.trim(this.dataId));
    	}
		if(this.operType != null){
    		this.setOperType(StringUtils.trim(this.operType));
    	}
		if(this.detail != null){
    		this.setDetail(StringUtils.trim(this.detail));
    	}
		if(this.modUser != null){
    		this.setModUser(StringUtils.trim(this.modUser));
    	}
	}

}
