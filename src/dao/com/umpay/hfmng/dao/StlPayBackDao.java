package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.StlPayBack;

/** 
 * 退费明细数据处理接口
 * <p>创建日期：2013-12-13 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public interface StlPayBackDao extends EntityDao<StlPayBack> {
	/**
	 * @Title: insertStlPayBack
	 * @Description: 添加退费信息
	 * @param stlPayBack
	 * @return 成功插入条数
	 * @author jxd
	 * @date 2013-12-13 下午3:57:45
	 */
	int insertStlPayBack(StlPayBack stlPayBack);
	
	/**
	 * @Title: insertStlPayBackBatch
	 * @Description: 批量添加退费信息
	 * @param stlPayBackList
	 * @return 成功插入条数
	 * @author jxd
	 * @date 2013-12-13 下午3:58:22
	 */
	int insertStlPayBackBatch(List<StlPayBack> stlPayBackList);

	/**
	 * @Title: getMaxStlMonth
	 * @Description: 获取最大结算月份
	 * @return 最大结算月份
	 * @author jxd
	 * @date 2013-12-24 下午3:27:15
	 */
	String getMaxStlMonth();

}
