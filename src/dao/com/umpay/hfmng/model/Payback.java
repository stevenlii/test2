package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * @ClassName: Payback
 * @Description: 退费实体
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-12-16
 */
@ExpTypeAnnotation(fieldStr = { "日期", "手机号", "地区", "退费金额(元)", "所属商户", "商户号", "子商户号", "子商户名称", "退费原因", "详细描述"}, 
fieldWidth = {10, 10, 12, 12, 14, 8, 8, 14, 24, 34}, 
fileName = "退费明细", sheetName = "退费明细")
public class Payback {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String date;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String mobile;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String proname;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 3)
	private Double amount;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String belongMer;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 5)
	private String merid;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 6)
	private String submerid;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 7)
	private String submername;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 8)
	private String paybackreason;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 9)
	private String desc;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProname() {
		return proname;
	}
	public void setProname(String proname) {
		this.proname = proname;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getBelongMer() {
		return belongMer;
	}
	public void setBelongMer(String belongMer) {
		this.belongMer = belongMer;
	}
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public String getSubmerid() {
		return submerid;
	}
	public void setSubmerid(String submerid) {
		this.submerid = submerid;
	}
	public String getSubmername() {
		return submername;
	}
	public void setSubmername(String submername) {
		this.submername = submername;
	}
	public String getPaybackreason() {
		return paybackreason;
	}
	public void setPaybackreason(String paybackreason) {
		this.paybackreason = paybackreason;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
