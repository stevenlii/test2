package com.umpay.hfmng.model;

import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;

public class MerInfo implements Entity {

	private String id;
	private String merId; // 商户编号
	private String exMerId;
	private String merName; // 商户名称
	private String merType; // 商户类型
	private String state; // 状态,2-开通,4-注销
	private Timestamp inTime; // 入库时间
	private int auditType = 0; // 审核类型 1：新增:2：修改:3：启用:4：禁用 0:未知
	private int auditState = 0; // 审核状态 0：待审核；1：审核通过；2：审核不通过
	/**
	 * 对应列表busi_attr 业务属性。表中列名busiType不再使用
	 */
	private String busiType; // 业务属性
	private int addWay; // 添加方式 0：本地，1：基地
	private String interType; // 接口类型 0：标准；1：特殊
	private String chnlCheck; // 是否渠道报备 0:否；1：是
	private int fileDL; // 是否开通对账文件下载 0:否；1：是
	private String fileDLName; // 是否开通对账文件下载中文显示
	private String operator; // 运营负责人
	private int modLock; // 修改锁 1：锁定中,0:未锁定
	private Timestamp modTime; // 修改时间
	private String modUser; // 修改人
	private String category; // 商户分类
	private String companyName; // 公司名称

	/** start 以下字段为满足商户、商品报备平台化新增(2014-07-18 by wanyong) */
	private String companyDesc; // 公司介绍
	private String busiDesc; // 业务介绍
	private String merWeb; // 商户网址
	private String saleChannel; // 自有营销渠道
	private Double regCapital; // 注册资本
	private String regTime; // 成立时间
	private String srcMer; // 商户来源
	private Double yearProfit; // 年收益
	private Integer userScale; // 用户规模
	private Integer support; // 运营支撑
	private String sharedRate; // 分成比例

	/** end ***************************************************************/

	public String toString() {
		return "MerInfo[id:" + id + ";merId:" + merId + ";merName:" + merName + ";merType:" + merType + ";state:"
				+ state + ";inTime:" + inTime + ";auditType:" + auditType + ";auditState:" + auditState + ";busiType:"
				+ busiType + ";addWay:" + addWay + ";interType:" + interType + ";chnlCheck:" + chnlCheck + ";fileDL:"
				+ fileDL + ";operator:" + operator + ";modLock:" + modLock + ";modTime:" + modTime + ";modUser:"
				+ modUser + ";category:" + category + ";companyName:" + companyName + ";companyDesc:" + companyDesc
				+ ";busiDesc:" + busiDesc + ";merWeb:" + merWeb + ";saleChannel:" + saleChannel + ";regCapital:"
				+ regCapital + ";regTime:" + regTime + ";srcMer:" + srcMer + ";yearProfit:" + yearProfit
				+ ";userScale:" + userScale + ";support:" + support + ";sharedRate:" + sharedRate + ";]";
	}

	public String getFileDLName() {
		return fileDLName;
	}

	public void setFileDLName(String fileDLName) {
		this.fileDLName = fileDLName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getExMerId() {
		return exMerId;
	}

	public void setExMerId(String exMerId) {
		this.exMerId = exMerId;
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

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public int getModLock() {
		return modLock;
	}

	public void setModLock(int modLock) {
		this.modLock = modLock;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public int getAddWay() {
		return addWay;
	}

	public void setAddWay(int addWay) {
		this.addWay = addWay;
	}

	public String getInterType() {
		return interType;
	}

	public void setInterType(String interType) {
		this.interType = interType;
	}

	public String getChnlCheck() {
		return chnlCheck;
	}

	public void setChnlCheck(String chnlCheck) {
		this.chnlCheck = chnlCheck;
	}

	public int getFileDL() {
		return fileDL;
	}

	public void setFileDL(int fileDL) {
		this.fileDL = fileDL;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public String getTableName() {
		return "T_MER_INF";
	}

	public String getId() {
		return id;
	}

	// public Timestamp getAddTime() {
	// return inTime;
	// }

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getMerType() {
		return merType;
	}

	public void setMerType(String merType) {
		this.merType = merType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getBusiDesc() {
		return busiDesc;
	}

	public void setBusiDesc(String busiDesc) {
		this.busiDesc = busiDesc;
	}

	public String getMerWeb() {
		return merWeb;
	}

	public void setMerWeb(String merWeb) {
		this.merWeb = merWeb;
	}

	public String getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(String saleChannel) {
		this.saleChannel = saleChannel;
	}

	public Double getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(Double regCapital) {
		this.regCapital = regCapital;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getSrcMer() {
		return srcMer;
	}

	public void setSrcMer(String srcMer) {
		this.srcMer = srcMer;
	}

	public Double getYearProfit() {
		return yearProfit;
	}

	public void setYearProfit(Double yearProfit) {
		this.yearProfit = yearProfit;
	}

	public Integer getUserScale() {
		return userScale;
	}

	public void setUserScale(Integer userScale) {
		this.userScale = userScale;
	}

	public Integer getSupport() {
		return support;
	}

	public void setSupport(Integer support) {
		this.support = support;
	}

	public String getSharedRate() {
		return sharedRate;
	}

	public void setSharedRate(String sharedRate) {
		this.sharedRate = sharedRate;
	}

	public void trim() {
		if (this.id != null) {
			this.setId(StringUtils.trim(this.id));
		}
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}
		if (this.merName != null) {
			this.setMerName(StringUtils.trim(this.merName));
		}
		if (this.merType != null) {
			this.setMerType(StringUtils.trim(this.merType));
		}
		if (this.state != null) {
			this.setState(StringUtils.trim(this.state));
		}
		if (this.busiType != null) {
			this.setBusiType(StringUtils.trim(this.busiType));
		}
		if (this.interType != null) {
			this.setInterType(StringUtils.trim(this.interType));
		}
		if (this.chnlCheck != null) {
			this.setChnlCheck(StringUtils.trim(this.chnlCheck));
		}
		if (this.operator != null) {
			this.setOperator(StringUtils.trim(this.operator));
		}
		if (this.category != null) {
			this.setCategory(StringUtils.trim(this.category));
		}
		if (this.companyName != null) {
			this.setCompanyName(StringUtils.trim(this.companyName));
		}
		if (this.companyDesc != null) {
			this.setCompanyDesc(StringUtils.trim(this.companyDesc));
		}
		if (this.busiDesc != null) {
			this.setBusiDesc(StringUtils.trim(this.busiDesc));
		}
		if (this.merWeb != null) {
			this.setMerWeb(StringUtils.trim(this.merWeb));
		}
		if (this.saleChannel != null) {
			this.setSaleChannel(StringUtils.trim(this.saleChannel));
		}
		if (this.regTime != null) {
			this.setRegTime(StringUtils.trim(this.regTime));
		}
		if (this.srcMer != null) {
			this.setSrcMer(StringUtils.trim(this.srcMer));
		}
		if (this.sharedRate != null) {
			this.setSharedRate(StringUtils.trim(this.sharedRate));
		}
	}
}
