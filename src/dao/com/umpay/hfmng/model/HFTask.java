package com.umpay.hfmng.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author helin created in 2012-12-24 下午4:16:42
 * 
 */
/**
 * @ClassName: HFTask
 * @Description: 对应T_HFTask
 * @author helin
 * @date 2013-1-16 下午4:30:56
 */
public class HFTask implements Entity, Serializable {
	private static final long serialVersionUID = -5476295493674665276L;
	private String taskId;// 任务编号
	private Timestamp nextRunTime;// 下次触发时间
	private String taskRule;// 任务规则表达式
	private String taskRuleDesc;// 任务规则描述
	private int state = -1;// 状态(是否启用)
	private String platName;// 所属应用
	private String taskDesc;// 任务说明
	private String postUrl;// 触发地址
	private int isReTry = -1;// 反馈异常是否重试
	private int reTryTimes = -1;// 反馈异常重试次数
	private int reTryInterval = -1;// 重试间隔时间（分钟）
	private int retTimeout = -1;// 反馈响应超时阀值（分钟）
	private String modUser;// 最后编制人
	private Timestamp modTime;// 修改时间
	private Timestamp inTime;// 插入时间

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Timestamp getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(Timestamp nextRunTime) {
		this.nextRunTime = nextRunTime;
	}

	public String getTaskRule() {
		return null == taskRule ? "" : taskRule.trim();
	}

	public void setTaskRule(String taskRule) {
		this.taskRule = taskRule;
	}

	public String getTaskRuleDesc() {
		return null == taskRuleDesc ? "" : taskRuleDesc.trim();
	}

	public void setTaskRuleDesc(String taskRuleDesc) {
		this.taskRuleDesc = taskRuleDesc;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	public String getModUser() {
		return null == modUser ? "" : modUser.trim();
	}

	public void setModUser(String modUser) {
		this.modUser = modUser;
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

}
