package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * @ClassName: Nbsm
 * @Description: 不均衡短信实体
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-12-16
 */
@ExpTypeAnnotation(fieldStr = { "日期", "上行短信笔数", "下行短信笔数", "不均衡短信笔数"}, fieldWidth = { 10, 20, 20, 20}, fileName = "不均衡短信明细", sheetName = "不均衡短信明细")
public class Nbsm {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String date;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 1)
	private Integer monum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 2)
	private Integer mtnum;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 3)
	private Integer nbsmnum;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getMonum() {
		return monum;
	}
	public void setMonum(Integer monum) {
		this.monum = monum;
	}
	public Integer getMtnum() {
		return mtnum;
	}
	public void setMtnum(Integer mtnum) {
		this.mtnum = mtnum;
	}
	public Integer getNbsmnum() {
		return nbsmnum;
	}
	public void setNbsmnum(Integer nbsmnum) {
		this.nbsmnum = nbsmnum;
	}
	
}
