package com.umpay.hfmng.model;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * ******************  类说明  *********************
 * class       :  Audit
 * @author     :  xhf
 * @version    :  1.0  
 * description :  审核表t_hfaudit
 * @see        :                        
 * ***********************************************
 */
public class Audit implements Entity{
	
	private String id;         //YYYYMMDD10位序列号
	private String tableName;   //操作的数据库表英文名,'T_MER_INF':商户类型； 'T_GOODS_INF'：商品类型
	private String ixData;     //索引字段
	private String ixData2;   //查询字段2,目前存放业务覆盖范围
	private String modData;    //修改后数据，json格式
	private String auditType;  //审核类型 1：新增； 2：修改； 3：启用； 4：禁用； 0:未知
	private String state;      //审核状态 0：待审核；1：审核不通过；2：审核通过
	private Timestamp inTime;  //添加时间
	private String creator;    //提交人
	private Timestamp modTime; //审核时间
	private String modUser;    //审核人
	private String resultDesc; //审核意见
	private String desc;       //描述信息,保留字段
	private String batchId;	   //批次id
	
	private String merId;      //所属商户号
	private String goodsId;    //商品号
	/**
	 * ********************************************
	 * method name   : toString 
	 * modified      : xhf ,  2012-9-4
	 * @see          : @see java.lang.Object#toString()
	 * *******************************************
	 */
	public String toString(){
		return "Audit[auditType:" + auditType + ";creator:" + creator
				+ ";tableName:" + tableName + ";id:" + id + ";inTime:" + inTime
				+ ";ixData:" + ixData + ";modData:" + modData + ";modTime:"
				+ modTime + ";modUser:" + modUser + ";ixData2:" + ixData2
				+ ";resultDesc:" + resultDesc + ";state:" + state + ";batchId:"+batchId+";desc:" + desc + "]";
	}
    
	/**
	 * ********************************************
	 * method name   : trim 
	 * description   : 对象去空格方法
	 * @return       : void
	 * modified      : xhf ,  2012-9-4  上午11:30:25
	 * *******************************************
	 */
    public void trim() {
    	if(this.id != null){
    		this.setId(StringUtils.trim(this.id));
    	}
    	if(this.tableName != null){
    		this.setTableName(StringUtils.trim(this.tableName));
    	}
    	if(this.creator != null){
    		this.setCreator(StringUtils.trim(this.creator));
    	}
    	if(this.goodsId != null){
    		this.setGoodsId(StringUtils.trim(this.goodsId));
    	}
    	if(this.ixData != null){
    		this.setIxData(StringUtils.trim(this.ixData));
    	}
    	if(this.merId != null){
    		this.setMerId(StringUtils.trim(this.merId));
    	}
    	if(this.modUser != null){
    		this.setModUser(StringUtils.trim(this.modUser));
    	}
    	if(this.ixData2 != null){
    		this.setIxData2(StringUtils.trim(this.ixData2));
    	}
    	if(this.state != null){
    		this.setState(StringUtils.trim(this.state));
    	}
    	if(this.batchId != null){
    		this.setBatchId(StringUtils.trim(this.batchId));
    	}
    }

	public String getId() {
		return id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIxData2() {
		return ixData2;
	}

	public void setIxData2(String ixData2) {
		this.ixData2 = ixData2;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIxData() {
		return ixData;
	}

	public void setIxData(String ixData) {
		this.ixData = ixData;
	}

	public String getModData() {
		return modData;
	}

	public void setModData(String modData) {
		this.modData = modData;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getModTime() {
		return modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

	public String getModUser() {
		return modUser;
	}

	public void setModUser(String modUser) {
		this.modUser = modUser;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
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

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
    
}
