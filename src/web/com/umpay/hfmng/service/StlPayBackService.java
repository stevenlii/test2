package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.StlPayBack;

/** 
 * 退费明细业务处理接口
 * <p>创建日期：2013-12-13 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public interface StlPayBackService {
	/**
	 * @Title: saveStlPayBack
	 * @Description: 保存退费明细
	 * @param stlPayBack
	 * @return 成功保存条数
	 * @author jxd
	 * @date 2013-12-13 下午4:12:35
	 */
	int saveStlPayBack(StlPayBack stlPayBack);
	
	/**
	 * @Title: saveStlPayBackBatch
	 * @Description: 批量保存退费明细
	 * @param stlPayBackList
	 * @return 成功保存条数
	 * @author jxd
	 * @date 2013-12-13 下午4:13:08
	 */
	int saveStlPayBackBatch(List<StlPayBack> stlPayBackList);
	
	/**
	 * @Title: getMaxStlMonth
	 * @Description: 获取最大结算月份
	 * @return 最大结算月份，格式：YYYYMM
	 * @author jxd
	 * @date 2013-12-24 下午3:23:09
	 */
	String getMaxStlMonth();

}
