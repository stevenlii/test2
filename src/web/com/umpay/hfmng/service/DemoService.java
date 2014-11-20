package com.umpay.hfmng.service;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.model.DemoInfo;

public interface DemoService {
	public void findDemoPO(PageBean pageBean,Map map)throws DataAccessException;
	public DemoInfo load(String merInfoId) throws DataAccessException;
	public void testTransaction();
}
