package com.umpay.hfmng.dao;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.model.DemoInfo;


/* ************************************************
 * class       :  Customer
 * @author     :  util
 * @version    :  1.0  
 * description :  dao interface
 * @see        :                        
 * ************************************************/

public interface DemoDao  extends EntityDao<DemoInfo> {
	/**
	 * 根据主键取对象
	 * @param customerId
	 * @return
	 * @throws DataAccessException
	 */
	public DemoInfo get(String customerId)throws DataAccessException;
	/**
	/**
	 * 多个条件查询会员
	 */
	public void findDemoPO(PageBean pageBean,Map map)throws DataAccessException;
	
	
}