package com.umpay.hfmng.model;


import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;


public class DocInfo implements Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String docName;
	private int docType;
	private String docPath;
	private String docDesc;
	private String creator;
	private Timestamp inTime;
	private String modUser;
	private Timestamp modTime;
	
	public String toString(){
		return "DocInfoBean[id:"+id+";docName:"+docName+";docType:"+docType+";docPath:"+docPath+";creator:"+creator+";inTime:"+inTime+";modUser:"+modUser+";modeTime:"+modTime+"]";
	}
	public void trim(){
		this.setId(StringUtils.trim(this.id));
		this.setDocName(StringUtils.trim(this.docName));
		this.setDocPath(StringUtils.trim(this.docPath));
		this.setDocDesc(StringUtils.trim(this.docDesc));
		this.setCreator(StringUtils.trim(this.creator));
		this.setModUser(StringUtils.trim(this.modUser));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public int getDocType() {
		return docType;
	}
	public void setDocType(int docType) {
		this.docType = docType;
	}
	public String getDocPath() {
		return docPath;
	}
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
	public String getDocDesc() {
		return docDesc;
	}
	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getInTime() {
		return inTime;
	}
	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
	public String getModUser() {
		return modUser;
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
}
