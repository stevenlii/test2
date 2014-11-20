package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * @ClassName: GoodsBankOptLogExcel
 * @Description: 商品银行操作日志导出Excel模型类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2014-4-26
 */
@ExpTypeAnnotation(fieldStr = { "修改类型", "商户号", "商品号", "商品名称", "支付银行", "2次确认校验码", "商品最长计费天数", "商品开通状态", "下发商品时间", "提交人",
		"提交日期", "操作原因" }, fieldWidth = { 10, 10, 20, 8, 16, 10, 10, 20, 8, 16, 20, 20 }, fileName = "商品银行操作日志", sheetName = "操作日志")
public class GoodsBankOptLogExcel implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String optType;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String merId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String goodsId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 3)
	private String goodsName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String bank;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 5)
	private String verifyTag;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 6)
	private String checkDay;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 7)
	private String kState;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 8)
	private String isRealTime;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 9)
	private String creator; // 提交人
	@ExpFieldAnnotation(type = FieldType.STRING, index = 10)
	private String inTime; // 提交时间
	@ExpFieldAnnotation(type = FieldType.STRING, index = 11)
	private String desc; // 操作原因
	
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getVerifyTag() {
		return verifyTag;
	}
	public void setVerifyTag(String verifyTag) {
		this.verifyTag = verifyTag;
	}
	public String getCheckDay() {
		return checkDay;
	}
	public void setCheckDay(String checkDay) {
		this.checkDay = checkDay;
	}
	public String getkState() {
		return kState;
	}
	public void setkState(String kState) {
		this.kState = kState;
	}
	public String getIsRealTime() {
		return isRealTime;
	}
	public void setIsRealTime(String isRealTime) {
		this.isRealTime = isRealTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
