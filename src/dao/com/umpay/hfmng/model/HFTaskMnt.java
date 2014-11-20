package com.umpay.hfmng.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName: HFTaskMnt
 * @Description: 对应T_HFTask_Mnt
 * @author helin
 * @date 2013-1-16 下午4:30:28
 */
public class HFTaskMnt implements Entity, Serializable {
	private static final long serialVersionUID = -2844604837008316820L;
	private String taskRpid;// 任务流水
	private String taskId;// 任务编号
	private int state = -1;// 任务状态 0-初始，1-触发失败，2-触发成功，3-反馈超时，4-执行失败，5-执行成功。
	private int sendType = -1;// 触发类型 1-自动，2-手动
	private String errorInf;// 异常反馈原因描述。
	private int errorTimes = -1;// 异常次数
	private Timestamp planRunTime;// 计划触发时间
	private Timestamp taskRunTime;// 实际触发时间
	private String deadTime;// 最后反馈时间
	private Timestamp modTime;// 修改时间
	private Timestamp inTime;// 插入时间

	private String taskRuleDesc;// 任务规则描述
	private String platName;// 所属应用
	private String taskDesc;// 任务说明
	private String postUrl;// 触发地址
	private int isReTry = -1;// 反馈异常是否重试
	private int reTryTimes = -1;// 反馈异常重试次数
	private int reTryInterval = -1;// 重试间隔时间（分钟）
	private int retTimeout = -1;// 反馈响应超时阀值（分钟）

	public String getTaskRpid() {
		return null == taskRpid ? "" : taskRpid.trim();
	}

	public void setTaskRpid(String taskRpid) {
		this.taskRpid = taskRpid;
	}

	public String getTaskId() {
		return null == taskId ? "" : taskId.trim();
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getErrorInf() {
		return null == errorInf ? "" : errorInf.trim();
	}

	public void setErrorInf(String errorInf) {
		this.errorInf = errorInf;
	}

	public int getErrorTimes() {
		return errorTimes;
	}

	public void setErrorTimes(int errorTimes) {
		this.errorTimes = errorTimes;
	}

	public Timestamp getPlanRunTime() {
		return planRunTime;
	}

	public void setPlanRunTime(Timestamp planRunTime) {
		this.planRunTime = planRunTime;
	}

	public Timestamp getTaskRunTime() {
		return taskRunTime;
	}

	public void setTaskRunTime(Timestamp taskRunTime) {
		this.taskRunTime = taskRunTime;
	}

	public String getDeadTime() {
		return null == deadTime ? "" : deadTime.trim();
	}

	public void setDeadTime(String deadTime) {
		this.deadTime = deadTime;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public String getTaskRuleDesc() {
		return null == taskRuleDesc ? "" : taskRuleDesc.trim();
	}

	public void setTaskRuleDesc(String taskRuleDesc) {
		this.taskRuleDesc = taskRuleDesc;
	}

	public String getPlatName() {
		return null == platName ? "" : platName.trim();
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public String getTaskDesc() {
		return null == taskDesc ? "" : taskDesc.trim();
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getPostUrl() {
		return null == postUrl ? "" : postUrl.trim();
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public int getIsReTry() {
		return isReTry;
	}

	public void setIsReTry(int isReTry) {
		this.isReTry = isReTry;
	}

	public int getReTryTimes() {
		return reTryTimes;
	}

	public void setReTryTimes(int reTryTimes) {
		this.reTryTimes = reTryTimes;
	}

	public int getReTryInterval() {
		return reTryInterval;
	}

	public void setReTryInterval(int reTryInterval) {
		this.reTryInterval = reTryInterval;
	}

	public int getRetTimeout() {
		return retTimeout;
	}

	public void setRetTimeout(int retTimeout) {
		this.retTimeout = retTimeout;
	}

}
