package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * ****************** 类说明 ********************* class : GoodsInfo
 * 
 * @author : Administrator
 * @version : 1.0 description : 商品信息描述model类
 * @see : ***********************************************
 */
public class GoodsInfo implements Entity {

	private String merId; // 所属商户号
	private String merName; // 商户名称
	private String goodsId; // 商品号
	private String goodsName; // 商品名称
	private String goodsType; // 风控类型
	private String servType; // 服务类型
	private int priceMode; // 价格模式
	private int pushInf; // 是否下发短信 0 不下行， 1 下行
	private int mtNum; // 下发短信条数 默认值 1
	private String cusPhone; // 客服电话
	private String goodsDesc; // 商品描述
	private String modUser; // 修改人
	private Timestamp inTime; // 入库时间
	private Timestamp modTime; // 修改时间
	private String state; // 商品状态

	private String busiType; // 业务覆盖
	private String interval; // 间隔周期
	private int modLock; // 修改锁状态
	private String category; // 商品分类
	private String servMonth; // 服务月份
	private int conMode; // 续费方式
	private int addWay; // 添加方式
	
	private String id;   //审核用id
	private String amount;  //商品金额     
	private String  verifyTag;//确认校验码
	private String  bankMerId;  //平台在机构商户号
	private String  bankPosId;  //平台在机构终端号 
	private String  checkDay;   //包月商品最长计费天数  
	private String  bankName;   //商品银行名称            
	private String  isRealTime;    //下发商品时间
	private String  kState;      //商品开通状态
   
/**
 * ********************************************
 * method name   : toString 
 * modified      : zhaojunbao ,  2012-8-30
 * @see          : @see java.lang.Object#toString()
 * *******************************************
 */
	public String toString() {
		return "GoodsInfo[merId:" + merId + ";merName:" + merName + ";goodsId:"
				+ goodsId + ";goodsName:" + goodsName + ";goodsType:"
				+ goodsType + ";servType:" + servType + ";priceMode:"
				+ priceMode + ";pushInf:" + pushInf + ";mtNum:" + mtNum
				+ ";cusPhone:" + cusPhone + ":goodsDesc:" + goodsDesc
				+ ";modUser:" + modUser + ";inTime:" + inTime + ";modTime:"
				+ modTime + ";state:" + state + ";busiType:" + busiType
				+ ";interval:" + interval + ";modLock:" + modLock
				+ ";category:" + category + ";servMonth:" + servMonth
				+ ";conMode:" + conMode + ";addWay:" + addWay + "]";
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
    public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}	
    public String getCheckDay() {
		return checkDay;
	}

    public void setCheckDay(String checkDay) {
		this.checkDay = checkDay;
	}
    public String getBankMerId() {
		return bankMerId;
	}
	public void setBankMerId(String bankMerId) {
		this.bankMerId = bankMerId;
	}
	public String getBankPosId() {
		return bankPosId;
	}
	public void setBankPosId(String bankPosId) {
		this.bankPosId = bankPosId;
	}	
   public String getVerifyTag() {
		return verifyTag;
	}
	public void setVerifyTag(String verifyTag) {
		this.verifyTag = verifyTag;
	}	
   public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = StringUtils.trim(merId);
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = StringUtils.trim(merName);
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId =StringUtils.trim(goodsId) ;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = StringUtils.trim(goodsName) ;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getServType() {
		return servType;
	}

	public void setServType(String servType) {
		this.servType = servType;
	}

	public int getPriceMode() {
		return priceMode;
	}

	public void setPriceMode(int priceMode) {
		this.priceMode = priceMode;
	}

	public int getPushInf() {
		return pushInf;
	}

	public void setPushInf(int pushInf) {
		this.pushInf = pushInf;
	}

	public int getMtNum() {
		return mtNum;
	}

	public void setMtNum(int mtNum) {
		this.mtNum = mtNum;
	}

	public String getCusPhone() {
		return cusPhone;
	}

	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public int getModLock() {
		return modLock;
	}

	public void setModLock(int modLock) {
		this.modLock = modLock;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getServMonth() {
		return servMonth;
	}

	public void setServMonth(String servMonth) {
		this.servMonth = servMonth;
	}

	public int getConMode() {
		return conMode;
	}

	public void setConMode(int conMode) {
		this.conMode = conMode;
	}

	public int getAddWay() {
		return addWay;
	}

	public void setAddWay(int addWay) {
		this.addWay = addWay;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void trim(){		
			if(this.goodsId!=null){
				this.setGoodsId(StringUtils.trim(this.goodsId));
			}				
			if(this.goodsDesc!=null){
				this.setGoodsDesc(StringUtils.trim(this.goodsDesc));
			}				
			if(this.goodsName!=null){
				this.setGoodsName(StringUtils.trim(this.goodsName));
			}				
			if(this.goodsType!=null){
				this.setGoodsType(StringUtils.trim(this.goodsType));
			}				
			if(this.servMonth!=null){
				this.setServMonth(StringUtils.trim(this.servMonth));
			}			
			if(this.servType!=null){
				this.setServType(StringUtils.trim(this.servType));
			}				
			if(this.state!=null){
				this.setState(StringUtils.trim(this.state));
			}				
			if(this.id!=null){
				this.setId(StringUtils.trim(this.id));
			}				
			if(this.interval!=null){
				this.setInterval(StringUtils.trim(this.interval));
			}				
			if(this.cusPhone!=null){
				this.setCusPhone(StringUtils.trim(this.cusPhone));
			}				
			if(this.category!=null){
				this.setCategory(StringUtils.trim(this.category));
			}				
			if(this.busiType!=null){
				this.setBusiType(StringUtils.trim(this.busiType));
			}
			
		
	}
	
}
