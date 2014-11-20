package com.umpay.hfmng.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

public class MerCnf implements Entity {

	private String merId;//商户编号
	private String merName;//商户名称
	private int merGer;//合并模式,小额与梦网是否合并 1：合并 0：不合并
	private String version;//版本号码
	private byte[] merCert;//商户证书
	private int notifyMode;//通知模式
	private int notifyTimes;//通知次数
	private BigDecimal expireTime;//订单过期时长,单位为秒
	private String modUser;//修改人
	private Timestamp modTime;//修改时间
	private Timestamp inTime;//入库时间
	private int spCardPay;//点卡兑换券支付, 是否支持点卡兑换券支付,0-不支持,1-支持
	private int defBankType;//默认支付方式,1-手机银行卡,2-网银,4-积分,5-嗖付,6-话费
	private int notifyMethod;//后台通知方式,0-GET,1-POST
	private int retMethod;//前台通知方式,0-GET,1-POST
	private String certName;//证书文件名
	private String pageId;//页面模板ID
	private int styleLevel;//页面模板优先级，1——99
	private String defGateId;//默认网银,默认工行
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("[merId:").append(merId);
		sb.append(";merName:").append(merName);
		sb.append(";merGer:").append(merGer);
		sb.append(";version:").append(version);
		sb.append(";notifyMode:").append(notifyMode);
		sb.append(";notifyTimes:").append(notifyTimes);
		sb.append(";expireTime:").append(expireTime);
		sb.append(";modUser:").append(modUser);
		sb.append(";modTime:").append(modTime);
		sb.append(";inTime:").append(inTime);
		sb.append(";spCardPay:").append(spCardPay);
		sb.append(";defBankType:").append(defBankType);
		sb.append(";notifyMethod:").append(notifyMethod);
		sb.append(";retMethod:").append(retMethod);
		sb.append(";certName:").append(certName);
		sb.append(";pageId:").append(pageId);
		sb.append(";styleLevel:").append(styleLevel);
		sb.append(";defGateId:").append(defGateId);
		sb.append("]");
		return sb.toString();
	}
//	public String toString() {
//		StringBuilder sb=new StringBuilder("[");
//		Field[] fields = this.getClass().getDeclaredFields();
//		for(int i = 0; i < fields.length; i++){
//			Field field = fields[i];
//			field.setAccessible(true);
//			Object o;
//			try {
//				o = field.get(this);
//				if (o!=null && "".equals(o)){
//					if(field.isAnnotationPresent(NoSerialization.class)){
//						continue;
//					}
//					sb.append(field.getName()).append(":").append(field.get(this)).append(";");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		sb.append("]");
//		return sb.toString();
//	}

	public void trim() {
		if (this.merId != null) {
			this.setMerId(StringUtils.trim(this.merId));
		}
		if (this.merName != null) {
			this.setMerName(StringUtils.trim(this.merName));
		}
		if (this.version != null) {
			this.setVersion(StringUtils.trim(this.version));
		}
		if (this.modUser != null) {
			this.setModUser(StringUtils.trim(this.modUser));
		}
		if (this.certName != null) {
			this.setCertName(StringUtils.trim(this.certName));
		}
		if (this.pageId != null) {
			this.setPageId(StringUtils.trim(this.pageId));
		}
		if (this.defGateId != null) {
			this.setDefGateId(StringUtils.trim(this.defGateId));
		}
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public int getMerGer() {
		return merGer;
	}

	public void setMerGer(int merGer) {
		this.merGer = merGer;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public byte[] getMerCert() {
		return merCert;
	}

	public void setMerCert(byte[] merCert) {
		this.merCert = merCert;
	}

	public int getNotifyMode() {
		return notifyMode;
	}

	public void setNotifyMode(int notifyMode) {
		this.notifyMode = notifyMode;
	}

	public int getNotifyTimes() {
		return notifyTimes;
	}

	public void setNotifyTimes(int notifyTimes) {
		this.notifyTimes = notifyTimes;
	}

	public BigDecimal getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(BigDecimal expireTime) {
		this.expireTime = expireTime;
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

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public int getSpCardPay() {
		return spCardPay;
	}

	public void setSpCardPay(int spCardPay) {
		this.spCardPay = spCardPay;
	}

	public int getDefBankType() {
		return defBankType;
	}

	public void setDefBankType(int defBankType) {
		this.defBankType = defBankType;
	}

	public int getNotifyMethod() {
		return notifyMethod;
	}

	public void setNotifyMethod(int notifyMethod) {
		this.notifyMethod = notifyMethod;
	}

	public int getRetMethod() {
		return retMethod;
	}

	public void setRetMethod(int retMethod) {
		this.retMethod = retMethod;
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public int getStyleLevel() {
		return styleLevel;
	}

	public void setStyleLevel(int styleLevel) {
		this.styleLevel = styleLevel;
	}

	public String getDefGateId() {
		return defGateId;
	}

	public void setDefGateId(String defGateId) {
		this.defGateId = defGateId;
	}
}
