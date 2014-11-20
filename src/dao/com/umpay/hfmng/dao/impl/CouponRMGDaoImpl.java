package com.umpay.hfmng.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.dao.CouponRMGDao;
import com.umpay.hfmng.model.CouponRMG;

/**
 * @ClassName: CouponRMGDaoImpl
 * @Description: TODO
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-11-4
 */
@Repository
public class CouponRMGDaoImpl extends EntityDaoImpl<CouponRMG> implements CouponRMGDao {

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.dao.CouponRMGDao#insertCouponRMGBatch(java.util.List)
	 */
	public void insertCouponRMGBatch(final List<CouponRMG> list) throws Exception {
		if (list != null ) {  
	          dao.execute( new SqlMapClientCallback() { 
	              public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException { 
	                 executor.startBatch(); 
	                 int batch= 0 ;
	                 for ( int i = 0, n = list.size(); i < n; i++) { 
	                     executor.insert("CouponRMG.insert", list.get(i)); 
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
	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#get(java.io.Serializable)
	 */
	public CouponRMG get(Serializable o) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#findBy(java.lang.Object)
	 */
	public List<CouponRMG> findBy(CouponRMG t) throws DataAccessException {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ruleid", t.getRuleid());
		return this.find("CouponRMG.get", t);
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#pagedFindBy(com.umpay.hfmng.base.PageBean, java.lang.Object)
	 */
	public void pagedFindBy(PageBean pageBean, CouponRMG t) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#insert(java.lang.Object)
	 */
	public void insert(CouponRMG t) throws DataAccessException {
		this.save("CouponRMG.insert", t);
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#update(java.lang.Object)
	 */
	public int update(CouponRMG t) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#delete(java.lang.Object)
	 */
	public void delete(CouponRMG t) throws DataAccessException {
		super.delete("CouponRMG.delete", t);
	}

	

}
