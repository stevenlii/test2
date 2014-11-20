/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlInfServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-20
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.ChnlInfDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlInfService;
import com.umpay.sso.org.User;


/** ******************  类说明  *********************
 * class       :  ChnlInfServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class ChnlInfServiceImpl implements ChnlInfService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ChnlInfDao chnlInfDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditService auditService;
	
	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-3-20
	 * @see          : @see com.umpay.hfmng.service.ChnlInfService#load(java.lang.String)
	 * ********************************************/     
	public ChnlInf load(String channelId) {
		return chnlInfDao.get(channelId);
	}

	public String saveChnlAudit(ChnlInf chnlInf) {
		String result="1";//操作结果 1表示成功, 0 表示失败
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_HF_CHNL_INF");
		audit.setIxData(chnlInf.getChannelId());
		audit.setIxData2(chnlInf.getChannelName());
		audit.setState("0");// 审核状态 0：待审核；1：审核通过；2：审核不通过
		audit.setAuditType("1");// 审核类型 1：新增
		chnlInf.setState(2);//默认启用
		String jsonStr = JsonHFUtil.getJsonArrStrFrom(chnlInf);
		audit.setModData(jsonStr);
		audit.setCreator(LoginUtil.getUser().getId());//创建人 是当前登录用户
		try {
			auditDao.insert(audit);
			log.info("新增审核信息成功"+audit.toString());
		} catch (Exception e) {
			result="0";
			throw new DAOException("新增审核信息失败！");	
		}
		return result;
	}
	
	/**
	 * 检查渠道编号或名称的唯一性，不存在返回1，存在则返回0
	 */
	@SuppressWarnings("unchecked")
	public String getCheckChnlIdOrName(Map<String, String> mapWhere) {
		List<Object> result1 = (List<Object>) chnlInfDao.getCheckFromChnls(mapWhere);
		if (result1.size() != 0) {
			return "0"; // 0 表示存在
		}
		mapWhere.put("ixData", mapWhere.get("channelId"));
		mapWhere.put("ixData2", mapWhere.get("channelName"));//此列记录新增的或者修改后未审核的新名称
		mapWhere.put("tableName", "UMPAY.T_HF_CHNL_INF");
		List<Object> result2 = (List<Object>) auditDao.getCheckExactlyFromAudit(mapWhere);
		if (result2.size() != 0) {
			return "0"; // 0 表示存在
		}
		return "1"; // 1 表示不存在
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String chnlAuditPass(String[] array, User user, String resultDesc) {
		String result = "yes"; // 返回结果初始化为yes
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = auditService.checkAuditData(array, auditList);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setState("2"); // 审核状态改为通过
			audit.setModUser(user.getId());
			audit.setResultDesc(resultDesc);
			ChnlInf chnl = (ChnlInf) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), ChnlInf.class);
			chnl.setModLock(0);
			chnl.setModTime(new Timestamp(System.currentTimeMillis()));
			if ("1".equals(audit.getAuditType())) { // 新增入库
				chnlInfDao.saveChnl(chnl);
				log.info("新增渠道成功" + chnl.toString());
				if (auditDao.updateState(audit) == 1) {
					log.info("更新审核状态成功" + audit.toString());
				} else {
					log.error("更新审核状态失败" + audit.toString());
					throw new DAOException("更新审核状态失败");
				}
			} else if ("2".equals(audit.getAuditType())) { // 修改入库
				if (chnlInfDao.updateChnl(chnl) == 1) {
					log.info("修改商户表成功" + chnl.toString());
				} else {
					log.error("修改商户表失败" + chnl.toString());
					throw new DAOException("修改商户表失败");
				}
				if (auditDao.updateState(audit) == 1) { // t_hfaudit
					log.info("修改审核状态成功" + audit.toString());
				} else {
					log.error("修改审核状态失败" + audit.toString());
					throw new DAOException("修改审核状态失败");
				}
			} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
				if (chnlInfDao.isOrNotAble(chnl) == 1) {
					log.info("修改商户状态成功" + chnl.toString());
				} else {
					log.error("修改商户状态失败" + chnl.toString());
					throw new DAOException("修改商户状态失败");
				}
				if (chnlInfDao.updateChnlLock(chnl) == 1) {
					log.info("修改锁成功" + chnl.toString());
				} else {
					log.error("修改锁失败" + chnl.toString());
					throw new DAOException("修改锁失败");
				}
				if (auditDao.updateState(audit) == 1) {//t_hfaudit
					log.info("修改审核状态成功" + audit.toString());
				} else {
					log.error("修改审核状态失败" + audit.toString());
					throw new DAOException("修改审核状态失败");
				}
			} else {
				log.error("非法的审核类型");
			}
			if ("yes".equals(result)) { // 入库成功，刷新缓存
				HfCache cache = (HfCache) SpringContextUtil.getBean("HfCache");
				cache.getChnlInfMap().remove(chnl.getChannelId());
				cache.getChnlInfMap().put(chnl.getChannelId(), chnl);
			}
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String chnlAuditNotPass(String[] array, User user,String resultDesc) {
		String result = "no"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = auditService.checkAuditData(array, auditList);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		for (int i = 0; i < auditList.size();i++){
			Audit audit = auditList.get(i);
			audit.setState("1"); //审核状态改为不通过
			audit.setModUser(user.getId());
			audit.setResultDesc(resultDesc);
			ChnlInf chnl = new ChnlInf();
			chnl.setChannelId(audit.getIxData());
			chnl.setModLock(0);
			if ("2".equals(audit.getAuditType())
					|| "3".equals(audit.getAuditType())
					|| "4".equals(audit.getAuditType())) {
				if (chnlInfDao.updateChnlLock(chnl) == 1) { // umpay.t_hf_chnl_inf
					log.info("修改锁成功" + chnl.toString());
				} else {
					log.error("修改锁失败" + chnl.toString());
					throw new DAOException("修改锁失败");
				}
			}
			if (auditDao.updateState(audit) == 1) { // umpay.t_hfaudit
				log.info("修改审核状态成功" + audit.toString());
				result = "yes";
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String checkModChnlName(Map<String, String> mapWhere) {
		List<Object> result1 = (List<Object>) chnlInfDao.checkModChnlName(mapWhere);
		if (result1.size() != 0) {
			return "0"; // 0 表示存在
		}
		mapWhere.put("ixData", mapWhere.get("channelId"));
		mapWhere.put("ixData2", mapWhere.get("channelName"));
		mapWhere.put("tableName", "UMPAY.T_HF_CHNL_INF");
		List<Object> result2 = (List<Object>) auditDao.checkModChnlName(mapWhere);
		if (result2.size() != 0) {
			return "0"; // 0 表示存在
		}
		return "1"; // 1 表示不存在
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String modifyChnlInf(ChnlInf chnl) {
		String result = "0";
		int modLock = chnl.getModLock();
		if (modLock == 1){  //判断当前的渠道锁定状态 ,如果是已经锁定 ,则提示已锁定
			log.error("当前渠道已处于锁定状态，无法操作");
			throw new DAOException("当前渠道已处于锁定状态，无法操作");		
		}
		chnl.setModLock(1); //更新锁状态
		String jsonString = JsonHFUtil.getJsonArrStrFrom(chnl);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_HF_CHNL_INF");
		audit.setModData(jsonString);
		audit.setState("0");          // 审核状态 0：待审核；1：审核通过；2：审核不通过
		audit.setAuditType("2");      // 审核类型 1：新增:2：修改:3：启用:4：禁用 0:未知
		audit.setCreator(LoginUtil.getUser().getId()); // 创建人是当前登录用户
		audit.setIxData(chnl.getChannelId());//ixData记录渠道编号
		audit.setIxData2(chnl.getChannelName());//ixData2记录审核中的渠道名称
		audit.setResultDesc("");
		auditDao.insert(audit); // 入审核表	
		log.info("渠道信息入审核表成功"+audit.toString());
	    int lock = chnlInfDao.updateChnlLock(chnl); // 更新锁状态
	    if(lock==1){
			log.info("渠道状态锁修改成功"+audit.toString());
			result="1";
		}else{
			log.error("渠道信息修改锁失败："+audit.toString());
			throw new DAOException("渠道信息修改锁失败");
		}
		return result; 
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String[] array, User user,int action) {
	    String result="0";
		for (int i = 0; i < array.length; i++) {
			result="0";   //循环操作时，每次操作之前应该先将操作状态置为失败（"0"）
			ChnlInf chnl = load(array[i].trim());// 先查找要修改的内容对象
			chnl.trim();
			if(chnl.getModLock() == 1){
				result = "0";
				log.error("渠道已锁定，无法操作！"+chnl.toString());
				throw new DAOException("渠道已锁定，无法操作！");
			}
			// 以下为初始化修改数据 包括查询参数 和要修改的目标状态
			chnl.setModLock(1);//加锁
			chnl.setModTime(null); //置为null,防止JSON序列化时出错
			chnl.setInTime(null);//置为null,防止JSON序列化时出错
			chnl.setMerCert(null);//置为null,防止JSON序列化时出错
			chnl.setState(action);// 目标设置状态 2为启用 ,4为禁用
			// 以下输将merInfo数据转化为audit数据对象进行保存
			String jsonString = JsonHFUtil.getJsonArrStrFrom(chnl);
			Audit audit = new Audit();
			audit.setTableName("UMPAY.T_HF_CHNL_INF");
			audit.setModData(jsonString);
			audit.setState("0");        // 审核状态 0：待审核
			 if(action==2){
				 audit.setAuditType("3") ;   // 目标状态为2  启用操作,审核类型为启用（3） 
			 }
			 if(action==4){
				 audit.setAuditType("4");   //目标状态为4 禁用操作，审核类型为禁用（4）
			 }
			audit.setCreator(user.getId());  // 提交人
			audit.setResultDesc("");
			audit.setIxData(chnl.getChannelId());
	     	auditDao.insert(audit);
	     	log.info("启用/禁用渠道入审核表成功"+audit.toString());
			int lock=chnlInfDao.updateChnlLock(chnl);
			if(lock == 1){
				result = "1";
				log.info("渠道修改锁成功"+chnl.toString());
			}else{
				result = "0";
				log.error("渠道修改锁失败"+chnl.toString());
				throw new DAOException("商户修改锁失败");
			}
		  }		
		return result;		
	}
	
//	public ChnlInf loadConfig(String channelId) {
//		return chnlInfDao.getConfig(channelId);
//	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String updateConf(MultipartFile file,ChnlInf chnl)throws Exception{
		InputStream input=null;
		 String result="0";
		try {
			input = file.getInputStream();
		    ByteArrayOutputStream output = new ByteArrayOutputStream();
		    byte[] buffer = new byte[4096];
		    int n = 0;
		    while (-1 != (n = input.read(buffer))) {
		        output.write(buffer, 0, n);
		    }
		    int merCertSize=output.size();
		    //数据库中证书的字段类型为BLOB(4000)
		    if(merCertSize>4000){
		    	return "2";//表示文件超过4000Byte时不更新
		    }
			chnl.setMerCert(output.toByteArray());
			chnlInfDao.updateConf(chnl);
			result="1";
		} catch (IOException e) {
			log.info("获取文件流出错", e);
			throw e;
		}
		return result;
	}
}
