/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.MerOperDao;
import com.umpay.hfmng.dao.SecMerCnfDao;
import com.umpay.hfmng.dao.SecMerInfDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.model.SecMerCnf;
import com.umpay.hfmng.model.SecMerInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.SecMerService;


/** ******************  类说明  *********************
 * class       :  SecMerServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class SecMerServiceImpl implements SecMerService {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private SecMerInfDao secMerInfDao;
	@Autowired
	private SecMerCnfDao secMerCnfDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditService auditService;
	@Autowired
	private MerOperDao merOperDao;
	@Autowired
	private MerOperService merOperService;

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.service.SecMerService#load(java.lang.String)
	 * ********************************************/
	public SecMerInf load(String subMerId) {
		SecMerInf mer = secMerInfDao.get(subMerId);
		if(mer != null){
			mer.trim();
		}
		return mer;
	}

	/** ********************************************
	 * method name   : auditNotPass 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.service.SecMerService#auditNotPass(java.lang.String[])
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditNotPass(String[] ids, String resultDesc) throws Exception {
		String res = "1"; // 返回结果初始化为成功
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = auditService.checkAuditData(ids, auditList);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		
		log.info("校验通过,开始入库");
		String userId = LoginUtil.getUser().getId();
		for(int i=0;i<ids.length;i++){
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("1"); //审核状态改为不通过
			audit.setResultDesc(resultDesc);
			String subMerId = audit.getIxData();
			if ("2".equals(audit.getAuditType()) || "3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
				if (secMerInfDao.setLock(subMerId, 0) == 1) {
					log.info("修改锁成功,subMerId=" + subMerId);
				} else {
					log.error("修改锁失败,subMerId=" + subMerId);
					throw new DAOException("修改锁失败");
				}
			}
			
			if (auditDao.updateState(audit) == 1) {
				log.info("修改审核状态成功" + audit);
			} else {
				log.error("修改审核状态失败" + audit);
				throw new DAOException("修改审核状态失败");
			}
		}
		return res;
	}
	
	/** ********************************************
	 * method name   : auditPass 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.service.SecMerService#auditPass(java.lang.String[])
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditPass(String[] ids, String resultDesc) throws Exception {
		String res = "1"; // 返回结果初始化为成功
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = auditService.checkAuditData(ids, auditList);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		
		log.info("校验通过,开始入库");
		String userId = LoginUtil.getUser().getId();
		Map<String, SecMerInf> merMap = HfCacheUtil.getCache().getSecMerMap();
		for(int i=0;i<ids.length;i++){
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("2"); //审核状态改为通过
			audit.setResultDesc(resultDesc);
			SecMerInf secMerInf = (SecMerInf) JsonHFUtil.getObjFromJsonArrStr(audit
					.getModData(), SecMerInf.class);
			secMerInf.setModLock(0); // 修改锁定状态为未锁定
			String secMerId = secMerInf.getSubMerId();
			List<HfMerOper> merOperList = new ArrayList<HfMerOper>();
			String operatorStr = secMerInf.getOperator();
			String[] operator = operatorStr.split(",");
			if ("1".equals(audit.getAuditType())) { // 新增入库
				secMerInf.setState(2);
				secMerInfDao.insert(secMerInf);
				log.info("新增入二级商户表成功" + secMerInf);
				
				for(int j=0;j<operator.length;j++){
					HfMerOper merOper = new HfMerOper();
					merOper.setMerId(secMerId);
					merOper.setOperator(operator[j]);
					merOper.setState(2);
					merOper.setCreator(userId);
					merOper.setModUser(userId);
					merOperList.add(merOper);
				}
				merOperDao.bacthInsert(merOperList);
				log.info("新增商户运营负责人成功");
			} else if ("2".equals(audit.getAuditType())) { // 修改入库
				if(secMerInfDao.update(secMerInf) == 1){
					log.info("修改二级商户表成功" + secMerInf);
				} else {
					log.error("修改二级商户表失败" + secMerInf);
					throw new DAOException("修改二级商户表失败");
				}
				
				merOperService.batchUpdate(secMerId, operator, userId);
				log.info("更新商户["+secMerId+"]的运营负责人成功");
				
				HfMerOper temp = new HfMerOper();
				temp.setMerId(secMerId);
				merOperList = merOperDao.findBy(temp);
				for(int j=0;j<merOperList.size();j++){
					merOperList.get(j).trim();
				}
			} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
				if(secMerInfDao.isOrNotAble(secMerInf) == 1){
					log.info("启用/禁用成功" + secMerInf);
				}else{
					log.error("启用/禁用失败" + secMerInf);
					throw new DAOException("启用/禁用失败");
				}
			} else {
				log.error("非法的审核类型");
				throw new DAOException("非法的审核类型");
			}
			
			if (auditDao.updateState(audit) == 1) {
				log.info("修改审核状态成功" + audit);
			} else {
				log.error("修改审核状态失败" + audit);
				throw new DAOException("修改审核状态失败");
			}
			
			//更新缓存
			merMap.put(secMerInf.getSubMerId(), secMerInf);
			if ("1".equals(audit.getAuditType()) || "2".equals(audit.getAuditType())){
				List<HfMerOper> list = HfCacheUtil.getCache().getMerOperListByMerId(secMerId);
				list.clear();
				list.addAll(merOperList);
			}
		}
		return res;
	}
	
	/** ********************************************
	 * method name   : enableAndDisable 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.service.SecMerService#enableAndDisable(java.lang.String[], int)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String[] ids, int action) throws Exception {
		String result = "1";
		List<SecMerInf> merList = new ArrayList<SecMerInf>();
		for (int i = 0; i < ids.length; i++) {
			SecMerInf mer=load(ids[i].trim());
			if (mer.getModLock() == 1) {
				result = "操作失败，商户[" + mer.getSubMerId() + "]正在审核中";
				log.error(result);
				return result;
			}
			merList.add(mer);
		}
		String userId = LoginUtil.getUser().getId();
		for (int i = 0; i < merList.size(); i++) {
			SecMerInf mer = merList.get(i);
			// 以下为初始化修改数据 包括查询参数 和要修改的目标状态
			mer.setModTime(null); // 入库时间和修改时间 都置为空,防止JSON序列化时出错
			mer.setInTime(null);
			mer.setState(action); // 目标设置状态 2为启用 ,4为禁用
			// 以下输将数据转化为audit数据对象进行保存
			String jsonString = JsonHFUtil.getJsonArrStrFrom(mer);
			Audit audit = new Audit();
			audit.setTableName("UMPAY.T_SECMER_INF");
			audit.setModData(jsonString);
			if (action == 2) {
				audit.setAuditType("3"); // 目标状态为2 启用操作,审核类型为启用（3）
			}
			if (action == 4) {
				audit.setAuditType("4"); // 目标状态为4 禁用操作，审核类型为禁用（4）
			}
			audit.setCreator(userId); // 提交人
			audit.setIxData(mer.getSubMerId());
			auditDao.insert(audit);
			log.info("启用/禁用商户入审核表成功" + audit);
			mer.setModLock(1); // 更新锁状态
			int lock = secMerInfDao.updateLock(mer);
			if (lock == 1) {
				log.info("商户修改锁成功" + mer);
			} else {
				log.error("商户修改锁失败" + mer);
				throw new DAOException("商户修改锁失败");
			}
		}
		return result;
	}

	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.service.SecMerService#save(com.umpay.hfmng.model.SecMerInf)
	 * ********************************************/     
	public String save(SecMerInf secMerInf) throws Exception {
		String result="1";                      //操作结果 1表示成功, 0 表示失败
		secMerInf.trim();
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", secMerInf.getSubMerId());
		mapWhere.put("tableName", "UMPAY.T_SECMER_INF");
		if(auditDao.checkDataAdd(mapWhere) != 0){
			result = "此商户已存在或正在审核中";
			log.info(result);
			return result;
		}
		
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_SECMER_INF");
		audit.setAuditType("1");                 // 审核类型 1：新增
		String jsonString = JsonHFUtil.getJsonArrStrFrom(secMerInf);
		audit.setModData(jsonString);
		audit.setCreator(secMerInf.getModUser());    //创建人 是当前登录用户
		audit.setIxData(secMerInf.getSubMerId());
		
		auditDao.insert(audit);
		log.info("新增审核信息成功"+audit);
		return result;
	}
	
	/** ********************************************
	 * method name   : updateSecMerInf 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.service.SecMerService#updateSecMerInf(com.umpay.hfmng.model.SecMerInf)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String updateSecMerInf(SecMerInf secMerInf) throws Exception {
		String result = "0";
		secMerInf.trim();
		String subMerId = secMerInf.getSubMerId();
		SecMerInf mer = HfCacheUtil.getCache().getSecMerMap().get(secMerInf.getSubMerId());
		if(mer == null){
			log.info("找不到该商户信息:"+subMerId);
			throw new DAOException("找不到该商户信息:"+subMerId);
		}
		int modLock = mer.getModLock();
		if (modLock == 1){  //判断当前的商品锁定状态 ,如果是已经锁定 ,则提示出错
			result = "当前商户已处于锁定状态，无法操作";
			log.info(result);
			return result;		
		}
		secMerInf.setModLock(1); //更新锁状态
		String jsonString = JsonHFUtil.getJsonArrStrFrom(secMerInf);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_SECMER_INF");
		audit.setModData(jsonString);
		audit.setAuditType("2");      // 审核类型 1：新增;2：修改;3：启用;4：禁用
		audit.setCreator(secMerInf.getModUser()); // 创建人是当前登录用户
		audit.setIxData(subMerId);
		
		auditDao.insert(audit); // 入审核表	
		log.info("二级商户信息入审核表成功"+audit);
	    int lock = secMerInfDao.setLock(subMerId, 1); // 更新锁状态
	    if(lock==1){
			log.info("商户状态锁修改成功"+audit);
			result="1";
		}else{
			log.error("商户信息修改锁失败："+audit);
			throw new DAOException("商户信息修改锁失败");
		}
	    log.info("修改二级商户操作成功");
		return result;
	}

	/** ********************************************
	 * method name   : checkKey 
	 * modified      : xuhuafeng ,  2013-10-8
	 * @see          : @see com.umpay.hfmng.service.SecMerService#checkKey(java.lang.String)
	 * ********************************************/     
	public String checkKey(String subMerId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ixData", subMerId);
		map.put("tableName", "UMPAY.T_SECMER_INF");
		return auditService.checkDataAdd(map);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String updateConf(MultipartFile file,SecMerCnf smc)throws Exception{
		InputStream input=null;
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
		    smc.setMerCert(output.toByteArray());
		    secMerCnfDao.update(smc);
			return "1";
		} catch (IOException e) {
			log.info("获取证书文件流出错", e);
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String addConf(MultipartFile file, SecMerCnf smc) throws Exception {
		InputStream input=null;
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
		    if(merCertSize==0){
		    	log.info("未添加证书");
		    	return "4";//插入数据时必须添加证书
		    }
		    if(merCertSize>4000){
		    	log.info("文件超过了4000字节");
		    	return "2";//表示文件超过4000Byte时不更新
		    }
		    smc.setMerCert(output.toByteArray());
		    secMerCnfDao.insert(smc);
			return "1";
		} catch (IOException e) {
			log.info("获取证书文件流出错", e);
			throw e;
		}
	}

	public List<SecMerCnf> loadCnf(String subMerId) throws Exception {
		return secMerCnfDao.findBySubMerId(subMerId);
	}

}
