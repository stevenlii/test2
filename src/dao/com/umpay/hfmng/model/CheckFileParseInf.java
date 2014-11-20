package com.umpay.hfmng.model;

import java.sql.Timestamp;

/**
 * @ClassName: CheckFileParseInf
 * @Description: 对账文件解析管理model类
 * @author wanyong
 * @date 2013-3-5 上午10:17:37
 */
public class CheckFileParseInf implements Entity {
	private String fileName;
	private int fileType;
	private String fileTypeName;
	private int fileState = -1;
	private String statDate;
	private String startDate;
	private String endDate;
	private String merId;
	private int dealTimes;
	private String remark;
	private int succNum;
	private int succAmt;
	private int failNum;
	private int failAmt;
	private Timestamp inTime;
	private Timestamp modTime;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getFileTypeName() {
		return fileTypeName;
	}

	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	public int getFileState() {
		return fileState;
	}

	public void setFileState(int fileState) {
		this.fileState = fileState;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public int getDealTimes() {
		return dealTimes;
	}

	public void setDealTimes(int dealTimes) {
		this.dealTimes = dealTimes;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSuccNum() {
		return succNum;
	}

	public void setSuccNum(int succNum) {
		this.succNum = succNum;
	}

	public int getSuccAmt() {
		return succAmt;
	}

	public void setSuccAmt(int succAmt) {
		this.succAmt = succAmt;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getFailAmt() {
		return failAmt;
	}

	public void setFailAmt(int failAmt) {
		this.failAmt = failAmt;
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
}
