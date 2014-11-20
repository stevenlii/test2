package com.umpay.hfmng.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


/** ******************  类说明  *********************
 * class       :  BatchTask
 * @author     :  lz
 * @version    :  1.0  
 * description :  批量任务表
 * ************************************************/
public class BatchTask implements Entity,Serializable{
	private static final long serialVersionUID = 1L;
	
	private String taskId;      //任务id
	private Integer taskType;     //任务类别
	private String creator;      //任务创建人
	private Integer synState;  //任务状态
	private Integer synResult;  //任务结果
	private String resultDesc;       //任务结果描述
	private Timestamp inTime;    //添加时间
	private Timestamp modTime;    //修改时间
	private String modUser;    //修改人
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("[taskId:").append(taskId);
		sb.append(";taskType:").append(taskType);
		sb.append(";creator:").append(creator);
		sb.append(";synState:").append(synState);
		sb.append(";synResult:").append(synResult);
		sb.append(";resultDesc:").append(resultDesc);
		sb.append(";inTime:").append(inTime);
		sb.append(";modTime:").append(modTime);
		sb.append(";modUser:").append(modUser);
		sb.append("]");
		return sb.toString();
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getSynState() {
		return synState;
	}

	public void setSynState(Integer synState) {
		this.synState = synState;
	}

	public Integer getSynResult() {
		return synResult;
	}

	public void setSynResult(Integer synResult) {
		this.synResult = synResult;
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
