package com.umpay.hfmng.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.dao.DemoDao;
import com.umpay.hfmng.model.DemoInfo;
import com.umpay.hfmng.service.DemoService;
@Service
public class DemoServiceImpl implements DemoService{
	@Autowired
	private DemoDao demoDao;
	
	public DemoInfo load(String merInfoId){
		DemoInfo merInfo = (DemoInfo) demoDao.get(merInfoId);
		return merInfo;
	}

	public void findDemoPO(PageBean pageBean,Map map){
		demoDao.findDemoPO(pageBean,map);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public void testTransaction(){
		DemoInfo merInfo = (DemoInfo) demoDao.get("9996");
		merInfo.getModUser();
		merInfo.setModUser("yangwr01");
		demoDao.update(merInfo);
		merInfo.setPostal("100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033100033");
		demoDao.update(merInfo);
	}

}
