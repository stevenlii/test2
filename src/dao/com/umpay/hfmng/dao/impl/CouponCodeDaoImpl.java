package com.umpay.hfmng.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponCodeDao;
import com.umpay.hfmng.model.CouponCode;

/**
 * @ClassName: CouponCodeDaoImpl
 * @Description: 兑换码数据库处理
 * @author wanyong
 * @date 2012-12-28 上午11:16:50
 */
@Repository
public class CouponCodeDaoImpl extends EntityDaoImpl<CouponCode> implements CouponCodeDao {

	/**
	 * @Title: insertCouponCode
	 * @Description: 新增一条兑换码信息
	 * @param
	 * @param couponCode
	 * @author wanyong
	 * @date 2012-12-28 上午11:16:29
	 */
	public void insertCouponCode(CouponCode couponCode) {
		super.save("CouponCode.insert", couponCode);
	}

	/**
	 * @Title: updateCouponCode
	 * @Description: 更新兑换码
	 * @param
	 * @param couponCode
	 * @return
	 * @author wanyong
	 * @date 2012-12-28 下午05:11:49
	 */
	public int updateCouponCode(CouponCode couponCode) {
		return super.update("CouponCode.update", couponCode);
	}

	/**
	 * @Title: getCouponCodeCount
	 * @Description: 查询商户ID、兑换码查询记录数
	 * @param
	 * @param enCode
	 *            加密后的兑换码
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2012-12-28 下午02:46:50
	 */
	public int getCouponCodeCount(String enCode, String merId) {
		CouponCode couponCode = new CouponCode();
		couponCode.setCouponCode(enCode);
		couponCode.setMerId(merId);
		return super.update("CouponCode.update", couponCode);
	}

	/**
	 * @Title: updateSelfDefine
	 * @Description: 根据自定义条件更新自定义兑换码列
	 * @param
	 * @param map
	 *            待更新列数据KEY=列名,VALUE=值
	 * @param whereMap
	 *            where条件列数据KEY=列名,VALUE=值
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 下午06:23:18
	 */
	public int updateSelfDefine(Map<String, Object> map, Map<String, Object> whereMap) {
		for (String key : whereMap.keySet()) {
			String newKey = "where_" + key;
			map.put(newKey, whereMap.get(key));
		}
		return super.update("CouponCode.updateselfdefine", map);
	}

	/**
	 * @Title: getCouponCode
	 * @Description: 查询一个兑换码实体实现
	 * @param code
	 *            加密后的兑换码
	 * @param merId
	 *            商户ID
	 * @return
	 * @author wanyong
	 * @date 2013-5-31 下午4:59:06
	 */
	public CouponCode getCouponCode(String code, String merId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merId", merId);
		map.put("couponCode", code);
		return (CouponCode) super.get("CouponCode.get", map);
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.dao.CouponCodeDao#insertCouponCodeBatch(java.util.List)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertCouponCodeBatch(final List<CouponCode> list) throws SQLException {
       if (list != null ) {  
          dao.execute( new SqlMapClientCallback() { 
              public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException { 
                 executor.startBatch(); 
                 int batch= 0 ;
                 for ( int i = 0, n = list.size(); i < n; i++) { 
                     executor.insert("CouponCode.insert", list.get(i)); 
                     if(++batch == 1000){
                    	 executor.executeBatch();
                    	 batch = 0;
                     }
                 } 
                 executor.executeBatch();
                 return null;
              } 
          });
       }
	}
}
