package com.umpay.hfmng.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.umpay.hfmng.model.StlPayBack;

/** 
 * 退费明细数据处理回调类
 * <p>创建日期：2013-12-13 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
@SuppressWarnings("rawtypes")
public class StlPayBackDaoCallback implements SqlMapClientCallback {
	
	private List<StlPayBack> list;//退费明细列表
	
	public StlPayBackDaoCallback(List<StlPayBack> list){
		this.list = list;
	}
	
	/**
	 * @Title: doInSqlMapClient
	 * @Description: 回调主方法，实现批量插入
	 * @param executor
	 * @return
	 * @throws SQLException
	 * @author jxd
	 * @date 2013-12-13 下午3:55:25
	 */
	public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
		executor.startBatch();
		int batch = 0;
		int count = 0;
		for (int i = 0, n = list.size(); i < n; i++) {
			executor.insert("StlPayBack.insert", list.get(i));
			if (++batch == 1000) {
				count = count + executor.executeBatch();
				batch = 0;
			}
		}
		count = count + executor.executeBatch();
		return count;
	}

}
