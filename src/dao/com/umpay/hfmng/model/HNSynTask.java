package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  HNSynTask
 * @author     :  chewnei
 * @version    :  1.0  
 * description :  河南批量同步商户、商品信息
 * ************************************************/

public class HNSynTask implements Entity {
	private String taskId;      //任务id
	private int taskType;     //任务类别
	private String creator;      //任务创建人
	private int synState;  //任务状态
	private int synResult;  //任务结果
	private String resultDesc;       //任务结果描述
	private Timestamp inTime;    //添加时间
	private Timestamp modTime;    //修改时间
	private String modUser;    //修改人
	
	public String toString(){
		return "HNSynTask[taskId:"+taskId+";taskType:"+taskType+";creator:"+creator
				+";synState:"+synState+";synResult:"+synResult+";resultDesc:"
				+resultDesc+";inTime:"+inTime+";modTime:"+modTime+";modUser:"+modUser+"]";
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getSynState() {
		return synState;
	}

	public void setSynState(int synState) {
		this.synState = synState;
	}

	public int getSynResult() {
		return synResult;
	}

	public void setSynResult(int synResult) {
		this.synResult = synResult;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
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
		if(this.taskId != null){
			this.setTaskId(StringUtils.trim(this.taskId));
		}
		if(this.creator != null){
			this.setCreator(StringUtils.trim(this.creator));
		}
		if(this.resultDesc != null){
			this.setResultDesc(StringUtils.trim(this.resultDesc));
		}
		if(this.modUser != null){
			this.setModUser(StringUtils.trim(this.modUser));
		}
	}

}
