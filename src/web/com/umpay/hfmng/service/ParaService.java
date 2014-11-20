package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.Para;
import com.umpay.sso.org.User;

public interface ParaService {
	public void insertPara(Para para,User user) throws Exception;
	public List<Para> getTypes(String paraType) throws Exception;
	public Para getPara(String paraType,String paraCode)throws Exception;
	public void updatePara(Para para,User user) throws Exception;
	public String validatePara(Para para,User user) throws Exception;
}
