package com.umpay.hfmng.model;

import org.apache.commons.lang.StringUtils;

import com.umpay.hfmng.common.Const;

/**
 * @ClassName: CouponMerSet
 * @Description: 账单表实体
 * @version: 1.0
 * @author: wangyuxin
 * @Create: 2013-12-9
 */

public class CouponMerSet implements Entity {
	public static final String SPLIT_IXDATA = "$_$";
	
	private String accId;
	private String accName;
	private String merId;
	private int stlCycle = Const.COUPON_MERSET_INIT_NUM;
	private String stlDate;
	private String goodsId;
	private int localFlag = Const.COUPON_MERSET_INIT_NUM;
	private int billType = Const.COUPON_MERSET_INIT_NUM;
	private String agentId;
	private String agentName;
	private int succNumm = Const.COUPON_MERSET_INIT_NUM;
	private double succAmtm = Const.COUPON_MERSET_INIT_NUM;
	private int succNums = Const.COUPON_MERSET_INIT_NUM;
	private double succAmts = Const.COUPON_MERSET_INIT_NUM;
	private int billSuccNumm = Const.COUPON_MERSET_INIT_NUM;
	private double billSuccAmtm = Const.COUPON_MERSET_INIT_NUM;
	private int muteNum = Const.COUPON_MERSET_INIT_NUM;
	private double muteAmt = Const.COUPON_MERSET_INIT_NUM;
	private int shamNum = Const.COUPON_MERSET_INIT_NUM;
	private double shamAmt = Const.COUPON_MERSET_INIT_NUM;
	private int moNum = Const.COUPON_MERSET_INIT_NUM;
	private int mtNum = Const.COUPON_MERSET_INIT_NUM;
	private int nbsmNum = Const.COUPON_MERSET_INIT_NUM;
	private double nbsmAmt = Const.COUPON_MERSET_INIT_NUM;
	private int paybackNum = Const.COUPON_MERSET_INIT_NUM;
	private double paybackAmt = Const.COUPON_MERSET_INIT_NUM;
	private double merStlAmt = Const.COUPON_MERSET_INIT_NUM;
	private double merStlPay = Const.COUPON_MERSET_INIT_NUM;
	private double cmccAmount = Const.COUPON_MERSET_INIT_NUM;
	private int settStatus = Const.COUPON_MERSET_INIT_NUM;
	private int state = Const.COUPON_MERSET_INIT_NUM;
	private int isUse = Const.COUPON_MERSET_INIT_NUM;
	private String modUser;
	private double ext1;
	private double ext2;
	private double ext3;
	private double cmccStlRates = Const.COUPON_MERSET_INIT_NUM;
	private double UmpStlRatem = Const.COUPON_MERSET_INIT_NUM;
	private double bad_bill = Const.COUPON_MERSET_INIT_NUM;
	private double ump_income = Const.COUPON_MERSET_INIT_NUM;
	private double balance_amount = Const.COUPON_MERSET_INIT_NUM;
	private int is_valid;
	
	
	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int isValid) {
		is_valid = isValid;
	}

	public void trim() {
		if (this.accId != null) {
			this.setAccId(StringUtils.trim(this.accId));
		}
		if (this.accName != null) {
			this.setAccName(StringUtils.trim(this.accName));
		}
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}

		if (this.stlDate != null) {
			this.setStlDate(StringUtils.trim(this.stlDate));
		}
		if (this.goodsId != null) {
			this.setGoodsId(StringUtils.trim(this.goodsId));
		}

		if (this.agentId != null) {
			this.setAgentId(StringUtils.trim(this.agentId));
		}
		if (this.agentName != null) {
			this.setAgentName(StringUtils.trim(this.agentName));
		}

		if (this.modUser != null) {
			this.setModUser(StringUtils.trim(this.modUser));
		}

	}
	
	@Override
	public String toString(){
		return "CouponMerSet[accId:"+accId+";accName:"+accName+";merId:"+merId+";stlCycle:"+stlCycle+";stlDate:"+stlDate+";goodsId:"+goodsId+";localFlag:"+localFlag+";billType:"+billType+";agentId:"+agentId+";agentName:"+agentName+";succNumm:"+succNumm+";succAmtm:"+succAmtm+";succNums:"+succNums+";succAmts:"+succAmts+";billSuccNumm:"+billSuccNumm+";billSuccAmtm:"+billSuccAmtm+";muteNum:"+muteNum+";muteAmt:"+muteAmt+";shamNum:"+shamNum+";shamAmt:"+shamAmt+";moNum:"+moNum+";mtNum:"+mtNum+";nbsmNum:"+nbsmNum+";nbsmAmt:"+nbsmAmt+";paybackNum:"+paybackNum+";paybackAmt:"+paybackAmt+";merStlAmt:"+merStlAmt+";merStlPay:"+merStlPay+";cmccAmount:"+cmccAmount+";settStatus:"+settStatus+";state:"+state+";isUse:"+isUse+";modUser:"+modUser+ ";ext1:" + ext1+ ";ext2:" + ext2 + ";ext3:" + ext3+ ";cmccStlRates:" + cmccStlRates+ ";UmpStlRatem:" + UmpStlRatem + ";bad_bill:" + bad_bill + ";ump_income:" + ump_income + ";balance_amount:" + balance_amount +"]";
	}
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public int getStlCycle() {
		return stlCycle;
	}
	public void setStlCycle(int stlCycle) {
		this.stlCycle = stlCycle;
	}
	public String getStlDate() {
		return stlDate;
	}
	public void setStlDate(String stlDate) {
		this.stlDate = stlDate;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getLocalFlag() {
		return localFlag;
	}
	public void setLocalFlag(int localFlag) {
		this.localFlag = localFlag;
	}
	public int getBillType() {
		return billType;
	}
	public void setBillType(int billType) {
		this.billType = billType;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public int getSuccNumm() {
		return succNumm;
	}
	public void setSuccNumm(int succNumm) {
		this.succNumm = succNumm;
	}

	public int getSuccNums() {
		return succNums;
	}
	public void setSuccNums(int succNums) {
		this.succNums = succNums;
	}

	public int getBillSuccNumm() {
		return billSuccNumm;
	}
	public void setBillSuccNumm(int billSuccNumm) {
		this.billSuccNumm = billSuccNumm;
	}

	public int getMuteNum() {
		return muteNum;
	}
	public void setMuteNum(int muteNum) {
		this.muteNum = muteNum;
	}

	public int getShamNum() {
		return shamNum;
	}
	public void setShamNum(int shamNum) {
		this.shamNum = shamNum;
	}

	public int getMoNum() {
		return moNum;
	}
	public void setMoNum(int moNum) {
		this.moNum = moNum;
	}
	public int getMtNum() {
		return mtNum;
	}
	public void setMtNum(int mtNum) {
		this.mtNum = mtNum;
	}
	public int getNbsmNum() {
		return nbsmNum;
	}
	public void setNbsmNum(int nbsmNum) {
		this.nbsmNum = nbsmNum;
	}

	public int getPaybackNum() {
		return paybackNum;
	}
	public void setPaybackNum(int paybackNum) {
		this.paybackNum = paybackNum;
	}

	public int getSettStatus() {
		return settStatus;
	}
	public void setSettStatus(int settStatus) {
		this.settStatus = settStatus;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	public String getModUser() {
		return modUser;
	}
	public void setModUser(String modUser) {
		this.modUser = modUser;
	}

	public double getExt1() {
		return ext1;
	}

	public void setExt1(double ext1) {
		this.ext1 = ext1;
	}

	public double getExt2() {
		return ext2;
	}

	public void setExt2(double ext2) {
		this.ext2 = ext2;
	}

	public double getExt3() {
		return ext3;
	}

	public void setExt3(double ext3) {
		this.ext3 = ext3;
	}

	public double getSuccAmtm() {
		return succAmtm;
	}

	public void setSuccAmtm(double succAmtm) {
		this.succAmtm = succAmtm;
	}

	public double getSuccAmts() {
		return succAmts;
	}

	public void setSuccAmts(double succAmts) {
		this.succAmts = succAmts;
	}

	public double getBillSuccAmtm() {
		return billSuccAmtm;
	}

	public void setBillSuccAmtm(double billSuccAmtm) {
		this.billSuccAmtm = billSuccAmtm;
	}

	public double getMuteAmt() {
		return muteAmt;
	}

	public void setMuteAmt(double muteAmt) {
		this.muteAmt = muteAmt;
	}

	public double getShamAmt() {
		return shamAmt;
	}

	public void setShamAmt(double shamAmt) {
		this.shamAmt = shamAmt;
	}

	public double getNbsmAmt() {
		return nbsmAmt;
	}

	public void setNbsmAmt(double nbsmAmt) {
		this.nbsmAmt = nbsmAmt;
	}

	public double getPaybackAmt() {
		return paybackAmt;
	}

	public void setPaybackAmt(double paybackAmt) {
		this.paybackAmt = paybackAmt;
	}

	public double getMerStlAmt() {
		return merStlAmt;
	}

	public void setMerStlAmt(double merStlAmt) {
		this.merStlAmt = merStlAmt;
	}

	public double getMerStlPay() {
		return merStlPay;
	}

	public void setMerStlPay(double merStlPay) {
		this.merStlPay = merStlPay;
	}

	public double getCmccAmount() {
		return cmccAmount;
	}

	public void setCmccAmount(double cmccAmount) {
		this.cmccAmount = cmccAmount;
	}

	public double getCmccStlRates() {
		return cmccStlRates;
	}

	public void setCmccStlRates(double cmccStlRates) {
		this.cmccStlRates = cmccStlRates;
	}

	public double getUmpStlRatem() {
		return UmpStlRatem;
	}

	public void setUmpStlRatem(double umpStlRatem) {
		UmpStlRatem = umpStlRatem;
	}

	public double getBad_bill() {
		return bad_bill;
	}

	public void setBad_bill(double badBill) {
		bad_bill = badBill;
	}

	public double getUmp_income() {
		return ump_income;
	}

	public void setUmp_income(double umpIncome) {
		ump_income = umpIncome;
	}

	public double getBalance_amount() {
		return balance_amount;
	}

	public void setBalance_amount(double balanceAmount) {
		balance_amount = balanceAmount;
	}
}
