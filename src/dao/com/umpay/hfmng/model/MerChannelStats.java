/**
 * @ClassName: MerChannelStats
 * @Description: TODO
 * @author panyouliang
 * @date 2013-3-25 下午2:17:46
 */
package com.umpay.hfmng.model;

import com.umpay.hfmng.common.ExpFieldAnnotation;
import com.umpay.hfmng.common.ExpTypeAnnotation;
import com.umpay.hfmng.common.FieldType;

/**
 * @author MARCO.PAN
 *
 */
@ExpTypeAnnotation(fieldStr = { "规则编号", "渠道编号", "渠道名称", "商户编号", "商户名称", "商品编号", "商品名称", "售出数量", "售出总金额 (元)"}, 
fieldWidth = {12, 10, 20, 10, 20, 10, 20, 8, 16},
fileName="商户商品渠道统计", sheetName="商户商品渠道统计")
public class MerChannelStats implements Entity {
	@ExpFieldAnnotation(type = FieldType.STRING, index = 0)
	private String date;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 1)
	private String channelId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 2)
	private String channelName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 3)
	private String merId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 4)
	private String merName;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 5)
	private String goodsId;
	@ExpFieldAnnotation(type = FieldType.STRING, index = 6)
	private String goodsName;
	@ExpFieldAnnotation(type = FieldType.INTEGER, index = 7)
	private Integer saledCount;
	@ExpFieldAnnotation(type = FieldType.DOUBLE, index = 8)
	private Double saledSum;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
	
	public Integer getSaledCount() {
		return saledCount;
	}
	public void setSaledCount(Integer saledCount) {
		this.saledCount = saledCount;
	}
	
	public Double getSaledSum() {
		return saledSum;
	}
	public void setSaledSum(Double saledSum) {
		this.saledSum = saledSum;
	}
}
