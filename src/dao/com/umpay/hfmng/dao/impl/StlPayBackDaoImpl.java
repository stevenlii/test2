package com.umpay.hfmng.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.StlPayBackDao;
import com.umpay.hfmng.model.StlPayBack;

/**
 * 退费明细数据处理实现类
 * <p>创建日期：2013-12-13</p>
 * @version V1.0
 * @author jxd
 * @see
 */
@Repository
public class StlPayBackDaoImpl extends EntityOffLineDaoImpl<StlPayBack> implements StlPayBackDao {

	/**
	 * @Title: insertStlPayBack
	 * @Description: 添加退费信息
	 * @param stlPayBack
	 * @return 成功插入条数
	 * @author jxd
	 * @date 2013-12-13 下午3:57:45
	 */
	public int insertStlPayBack(StlPayBack stlPayBack) {
		return (Integer)offlineDao.insert("StlPayBack.insert", stlPayBack);
	}

	/**
	 * @Title: insertStlPayBackBatch
	 * @Description: 批量添加退费信息
	 * @param stlPayBackList
	 * @return 成功插入条数
	 * @author jxd
	 * @date 2013-12-13 下午3:58:22
	 */
	@SuppressWarnings("unchecked")
	public int insertStlPayBackBatch(List<StlPayBack> stlPayBackList) {
		if (stlPayBackList == null || stlPayBackList.isEmpty()) {  
			return 0;
		}
		return (Integer)offlineDao.execute(new StlPayBackDaoCallback(stlPayBackList));
	}
	
	/**
	 * @Title: getMaxStlMonth
	 * @Description: 获取最大结算月份
	 * @return 最大结算月份
	 * @author jxd
	 * @date 2013-12-24 下午3:27:15
	 */
	public String getMaxStlMonth() {
		return (String)offlineDao.queryForObject("StlPayBack.getMaxStlMonth");
	}

}
