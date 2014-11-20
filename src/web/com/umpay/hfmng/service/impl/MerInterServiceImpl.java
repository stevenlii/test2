/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-23
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.MerInterDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.MerInter;
import com.umpay.hfmng.service.MerInterService;
import com.umpay.sso.org.User;


/** ******************  类说明  *********************
 * class       :  MerInterServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class MerInterServiceImpl implements MerInterService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private MerInterDao merInterDao;

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-12-23
	 * @see          : @see com.umpay.hfmng.service.MerInterService#load(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/
	public MerInter load(String merId, String inFunCode, String inVersion) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("inFunCode", inFunCode);
		mapWhere.put("inVersion", inVersion);
		MerInter mer = merInterDao.get(mapWhere);
		if(mer != null){
			mer.trim();
		}
		return mer;
	}
	
	/** ********************************************
	 * method name   : enableAndDisable 
	 * modified      : xuhuafeng ,  2013-12-23
	 * @see          : @see com.umpay.hfmng.service.MerInterService#enableAndDisable(com.umpay.hfmng.model.MerInter)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String merIds, String inFunCodes, String inVersions, int action) throws Exception {
		String res = "1";
		User user = LoginUtil.getUser();
		String[] merId = merIds.split(",");
		String[] inFunCode = inFunCodes.split(",");
		String[] inVersion = inVersions.split(",");
		if(merId.length != inFunCode.length || inFunCode.length != inVersion.length || merId.length != inVersion.length){
			log.error("页面传递参数错误，无法启用/禁用。操作人:"+user.getId()+user.getName());
			return "0";
		}
		List<MerInter> merList = new ArrayList<MerInter>();
		for(int i=0;i<merId.length;i++){
			MerInter mer = load(merId[i], inFunCode[i], inVersion[i]);
			if(mer != null){
				if(mer.getState() != action){
					MerInter merInter = new MerInter();
					merInter.setMerId(merId[i]);
					merInter.setInFunCode(inFunCode[i]);
					merInter.setInVersion(inVersion[i]);
					merInter.setState(action);
					merList.add(merInter);
				}else{
					String name = action == 2 ? "启用" : "禁用";
					return "商户号["+merId[i]+"]接口标识["+inFunCode[i]+"]版本号码["+inVersion[i]+"]的商户接口已"+name+",不能再次"+name;
				}
			}else{
				return "操作失败，没有找到数据";
			}
		}
		for(int i=0;i<merList.size();i++){
			if(merInterDao.updateState(merList.get(i)) != 1){
				log.error("启用/禁用失败" + merList.get(i)+"。操作人:"+user.getId()+user.getName());
				throw new DAOException("启用禁用操作失败");
			}
			log.info("启用/禁用成功"+merList.get(i)+"。操作人:"+user.getId()+user.getName());
		}
		return res;
	}
	
	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2013-12-23
	 * @see          : @see com.umpay.hfmng.service.MerInterService#save(com.umpay.hfmng.model.MerInter)
	 * ********************************************/     
	public String save(MerInter merInter) throws Exception {
		String res = "1";
		merInter.trim();
		MerInter mer = load(merInter.getMerId(), merInter.getInFunCode(), merInter.getInVersion());
		if(mer != null){
			log.info("主键重复，不能插入");
			return "该商户接口已存在，不能再次插入";
		}
		merInterDao.insert(merInter);
		User user = LoginUtil.getUser();
		log.info("新增商户接口信息成功。" + merInter+"。操作人:"+user.getId()+user.getName());
		return res;
	}
	
	/** ********************************************
	 * method name   : update 
	 * modified      : xuhuafeng ,  2013-12-23
	 * @see          : @see com.umpay.hfmng.service.MerInterService#update(com.umpay.hfmng.model.MerInter)
	 * ********************************************/     
	public String update(MerInter merInter) throws Exception {
		String res = "1";
		merInter.trim();
		User user = LoginUtil.getUser();
		if(merInterDao.update(merInter) == 1){
			log.info("修改商户接口信息成功。" + merInter+"。操作人:"+user.getId()+user.getName());
		}else{
			throw new DAOException("修改商户接口信息失败" + merInter);
		}
		return res;
	}

}
