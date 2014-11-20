package com.umpay.hfmng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.StlPayBackDao;
import com.umpay.hfmng.model.StlPayBack;
import com.umpay.hfmng.service.StlPayBackService;

/** 
 * 退费明细业务处理实现类
 * <p>创建日期：2013-12-13 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
@Service
public class StlPayBackServiceImpl implements StlPayBackService {
	
	@Autowired
	private StlPayBackDao stlPayBackDao;
	
	/**
	 * @Title: saveStlPayBack
	 * @Description: 保存退费明细
	 * @param stlPayBack
	 * @return 成功保存条数
	 * @author jxd
	 * @date 2013-12-13 下午4:12:35
	 */
	public int saveStlPayBack(StlPayBack stlPayBack) {
		return stlPayBackDao.insertStlPayBack(stlPayBack);
	}

	/**
	 * @Title: saveStlPayBackBatch
	 * @Description: 批量保存退费明细
	 * @param stlPayBackList
	 * @return 成功保存条数
	 * @author jxd
	 * @date 2013-12-13 下午4:13:08
	 */
	public int saveStlPayBackBatch(List<StlPayBack> stlPayBackList) {
		return stlPayBackDao.insertStlPayBackBatch(stlPayBackList); 
	}

	/**
	 * @Title: getMaxStlMonth
	 * @Description: 获取最大结算月份
	 * @return 最大结算月份，格式：YYYYMM
	 * @author jxd
	 * @date 2013-12-24 下午3:23:09
	 */
	public String getMaxStlMonth() {
		return stlPayBackDao.getMaxStlMonth();
	}

}
