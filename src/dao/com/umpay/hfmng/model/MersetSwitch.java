/**
 * @Title: MersetSwitch.java
 * @Package com.umpay.hfmng.model
 * @Description: TODO
 * @author MARCO
 * @date 2014-6-5 上午9:49:20
 * @version V1.0
 */

package com.umpay.hfmng.model;

import java.sql.Timestamp;

public class MersetSwitch {
	private String seqid;
	private String merid;
	private String mername;
	private String configtime;
	private int isopen;
	private int isuse;
	private Timestamp modtime;
	private Timestamp intime;
	public String getSeqid() {
		return seqid;
	}
	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public String getMername() {
		return mername;
	}
	public void setMername(String mername) {
		this.mername = mername;
	}
	public String getConfigtime() {
		return configtime;
	}
	public void setConfigtime(String configtime) {
		this.configtime = configtime;
	}
	public int getIsopen() {
		return isopen;
	}
	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}
	public int getIsuse() {
		return isuse;
	}
	public void setIsuse(int isuse) {
		this.isuse = isuse;
	}
	public Timestamp getModtime() {
		return modtime;
	}
	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}
	public Timestamp getIntime() {
		return intime;
	}
	public void setIntime(Timestamp intime) {
		this.intime = intime;
	}
}
