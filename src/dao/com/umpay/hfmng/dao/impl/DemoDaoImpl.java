package com.umpay.hfmng.dao.impl;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.dao.DemoDao;
import com.umpay.hfmng.model.DemoInfo;

/* ************************************************
 * class       :  Customer
 * @author     :  util
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository
public class DemoDaoImpl extends EntityDaoImpl<DemoInfo> implements DemoDao {
	public DemoInfo get(String id)throws DataAccessException{
		return (DemoInfo)this.get("DemoInfo.Get",id);
	}
	public void findDemoPO(PageBean pageBean,Map map)throws DataAccessException{
		 super.find("DemoInfo.Find", pageBean,map);
	}
}