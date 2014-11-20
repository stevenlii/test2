/**
 * 
 */
package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;


/**
 * @author MARCO-PAN
 *
 */
@ExpTypeAnnotation(fieldStr = { "修改类型", "商户号", "商户名称", "支付银行", "提交人", "提交日期" }, 
fieldWidth = {10, 10, 20, 35, 10, 20},
fileName="商户银行操作日志", sheetName="商户银行操作日志")
public class MerbankOpLog implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String auditType;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 1)
	private Integer merId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String merName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 3)
	private String  bank;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String creator;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 5)
	private String  intime;
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public Integer getMerId() {
		return merId;
	}
	public void setMerId(Integer merId) {
		this.merId = merId;
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getIntime() {
		return intime;
	}
	public void setIntime(String intime) {
		this.intime = intime;
	}
}
