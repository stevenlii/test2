package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.ParaDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.service.ParaService;
import com.umpay.sso.org.User;

@Service
public class ParaServiceImpl implements ParaService{
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ParaDao paraDao;
	
	public void insertPara(Para para,User user) throws Exception {
		Timestamp curTime = new Timestamp(new Date().getTime());
		para.setInTime(curTime);
		para.setModTime(curTime);
		para.setModUser(user.getId());
		para.setSysId("coupon");
		paraDao.inserPara(para);
		log.info("参数表中成功插入参数信息" + para.toString());
	}
	
	public List<Para> getTypes(String paraType) throws Exception{
		return paraDao.getTypes(paraType);
	}
	
	public Para getPara(String paraType,String paraCode){
		return paraDao.getPara(paraType, paraCode);
	}
	
	public void updatePara(Para para,User user) throws Exception {
		Timestamp curTime = new Timestamp(new Date().getTime());
		para.setModTime(curTime);
		para.setModUser(user.getId());
		para.setSysId("coupon");
		paraDao.updatePara(para);
		log.info("参数表中成功修改参数信息" + para.toString());
	}

	public String validatePara(Para para,User user) throws Exception{
		String result="0"; //操作结果 1表示存在 0 表示不存在
		Para para1 = paraDao.getPara(para.getParaType(), para.getParaCode());
		if(para1!=null){
			result = "1";
		}
		return result;
	}
}
