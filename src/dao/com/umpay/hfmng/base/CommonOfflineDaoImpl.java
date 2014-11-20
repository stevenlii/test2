package com.umpay.hfmng.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

@SuppressWarnings("unchecked")
public class CommonOfflineDaoImpl {
	@Autowired
	public SqlMapClientTemplate offlineDao;
	
	public int delete(String statementName, Object obj) throws DataAccessException {
			return offlineDao.delete(statementName, obj);
	}
	
	public int deleteAll(String statementName, Map paramMap) throws DataAccessException {
			return offlineDao.delete(statementName, paramMap);
	}

	public List find(String statementName) throws DataAccessException {
			return offlineDao.queryForList(statementName);
	}

	public List find(String statementName, Map paramMap) throws DataAccessException {
			return offlineDao.queryForList(statementName, paramMap);
	}

	public void save(String statementName, Object obj) throws DataAccessException{
		offlineDao.insert(statementName, obj);
	}

	public int update(String statementName, Object obj) throws DataAccessException {			
			return offlineDao.update(statementName, obj);
	}

	public Object get(String statementName, Serializable id) throws DataAccessException {
			return offlineDao.queryForObject(statementName, id);
	}

	public Object get(String statementName) throws DataAccessException {		
			return offlineDao.queryForObject(statementName, null);
	}

	public Object get(String statementName, Map pkMap) throws DataAccessException {	
			return offlineDao.queryForObject(statementName, pkMap);
	}

	public void find(String statementName, PageBean pageObj, Map paramMap) throws DataAccessException {
			Integer total = (Integer) offlineDao.queryForObject(statementName
					+ "_count", paramMap);
			if (total != null&& total.intValue()>0){
				pageObj.setTotalRecords(total.intValue());
				List rs = offlineDao.queryForList(statementName, paramMap,
						pageObj.getRsFirstNumber() - 1, pageObj.getLength());
				pageObj.setResults(rs);
			}else{
				pageObj.setTotalRecords(0);
			}
	}

	

	public List find(String statementName, Object obj) throws DataAccessException {
			return offlineDao.queryForList(statementName, obj);
	}

	public void find(String statementName, PageBean pageObj, Object obj) throws DataAccessException {
			Integer total = (Integer) offlineDao.queryForObject(statementName+ "_count", obj);
			if (total != null && total.intValue()>0){
				pageObj.setTotalRecords(total.intValue());
				List rs = offlineDao.queryForList(statementName, obj, pageObj
						.getRsFirstNumber() - 1, pageObj.getLength());
				pageObj.setResults(rs);
			}else{
				pageObj.setTotalRecords(0);
			}
	}

}

