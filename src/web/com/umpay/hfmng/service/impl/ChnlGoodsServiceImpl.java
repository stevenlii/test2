/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoodsServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-27
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

import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.ChnlGoodsDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ChnlGoods;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlGoodsService;


/** ******************  类说明  *********************
 * class       :  ChnlGoodsServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class ChnlGoodsServiceImpl implements ChnlGoodsService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ChnlGoodsDao chnlGoodsDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditService auditService;

	/** ********************************************
	 * method name   : getList 
	 * modified      : xuhuafeng ,  2013-3-27
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#getList(java.lang.String, java.lang.String)
	 * ********************************************/
	public List<ChnlGoods> getList(String channelId, String merId) {
		return chnlGoodsDao.getList(channelId, merId);
	}

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-3-27
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#load(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/
	public ChnlGoods load(String channelId, String merId, String goodsId) {
		return chnlGoodsDao.get(channelId, merId, goodsId);
	}

	
	/** ********************************************
	 * method name   : getMap 
	 * modified      : xuhuafeng ,  2013-3-27
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#getMap(java.lang.String, java.lang.String)
	 * ********************************************/     
	public Map<String, ChnlGoods> getMap(String channelId, String merId) {
		List<ChnlGoods> list = chnlGoodsDao.getList(channelId, merId);
		Map<String, ChnlGoods> map = new HashMap<String, ChnlGoods>();
		for(int i=0;i<list.size();i++){
			ChnlGoods chnlGoods = list.get(i);
			chnlGoods.trim();
			map.put(chnlGoods.getGoodsId(), chnlGoods);
		}
		return map;
	}

	
	/** ********************************************
	 * method name   : getAuditMap 
	 * modified      : xuhuafeng ,  2013-3-27
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#getAuditMap(java.lang.String, java.lang.String)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public Map getAuditMap(String channelId, String merId) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", channelId + "-" + merId + "-");
		mapWhere.put("tableName", "UMPAY.T_HF_CHNL_GOODS");
		List<Audit> result = auditDao.getMerBankList(mapWhere);
		Map audit = new HashMap();
		for (Audit a : result) {
			audit.put(a.getIxData().split("-")[2].trim(), a);
		}
		return audit;
	}

	
	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2013-3-28
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#save(com.umpay.hfmng.model.ChnlGoods, java.util.List, java.util.List, java.util.List)
	 * ********************************************/     
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String save(ChnlGoods chnlGoods, List<String> newOpen,
			List<String> modOpen, List<String> modClose) {
		String result = "no";
		for(int i=0;i<newOpen.size();i++){
			chnlGoods.setGoodsId(newOpen.get(i));
			chnlGoods.setState(2);
        	result = saveAudit(chnlGoods, "1");
        	log.info("新增渠道商品配置成功"+chnlGoods.toString());
        }
        for(int i=0;i<modOpen.size();i++){
        	chnlGoods.setGoodsId(modOpen.get(i));
        	chnlGoods.setState(2);
        	result = saveAudit(chnlGoods, "3");
        	log.info("启用渠道商品配置成功"+chnlGoods.toString());
        }
        for(int i=0;i<modClose.size();i++){
        	chnlGoods.setGoodsId(modClose.get(i));
        	chnlGoods.setState(4);
        	result = saveAudit(chnlGoods, "4");
        	log.info("禁用渠道商品配置成功"+chnlGoods.toString());
        }
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private String saveAudit(ChnlGoods chnlGoods, String auditType) {
		String result="no";                      //操作结果 1表示成功, 0 表示失败
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_HF_CHNL_GOODS");
		audit.setState("0");                     // 审核状态 0：待审核
		audit.setAuditType(auditType);                 // 审核类型 1：新增开通；3：修改开通；4：修改关闭
		String jsonString = JsonHFUtil.getJsonArrStrFrom(chnlGoods);
		audit.setModData(jsonString);
		audit.setCreator(chnlGoods.getService_user());    //创建人 是当前登录用户
		audit.setIxData(chnlGoods.getChannelId()+"-"+chnlGoods.getMerId()+"-"+chnlGoods.getGoodsId());
		auditDao.insert(audit);
		log.info("新增审核信息成功"+audit.toString());
		if("3".equals(auditType) || "4".equals(auditType)){
			chnlGoods.setModLock(1);
			if(chnlGoodsDao.updateModLock(chnlGoods) == 1){
				log.info("更新锁定状态成功"+chnlGoods.toString());
			}else{
				log.error("更新锁定状态失败"+chnlGoods.toString());
				throw new DAOException("更新锁定状态失败！");
			}
		}
		result = "yes";
		return result;
	}

	
	/** ********************************************
	 * method name   : enableAnddisable 
	 * modified      : xuhuafeng ,  2013-3-28
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#enableAnddisable(java.lang.String, java.lang.String, int)
	 * ********************************************/     
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String enableAnddisable(String ID, String userId, int action) {
		String result = "no";
		if(ID == null || "".equals(ID)){
			return result;
		}
		String auditType = "";
		if(action == 2){
			auditType = "3";
		}else if(action == 4){
			auditType = "4";
		}
		ChnlGoods chnlGoods = new ChnlGoods();
		chnlGoods.setState(action);
		chnlGoods.setService_user(userId);
		String[] array = ID.split(",");
		for (int i = 0; i < array.length; i++) {
			result = "no";
			String[] key = array[i].split("-");
			if(key.length != 3){
				return result;
			}
			ChnlGoods chGoods = load(key[0], key[1], key[2]);
			if(chGoods == null){
				log.error("不存在的渠道商品：" + array[i]);
				return result;
			}
			chGoods.trim();
			if(chGoods.getModLock() == 1){
				log.error("该渠道商品已锁定，无法操作" + chGoods.toString());
				return result;
			}
			if(chGoods.getState() == action){
				log.error("该渠道商品已为目标状态：" + chGoods.toString());
				return result;
			}
			chnlGoods.setChannelId(key[0]);
			chnlGoods.setMerId(key[1]);
			chnlGoods.setGoodsId(key[2]);
			result = saveAudit(chnlGoods, auditType);
		}
		return result;
	}

	
	/** ********************************************
	 * method name   : auditNotPass 
	 * modified      : xuhuafeng ,  2013-3-29
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#auditNotPass(java.lang.String[], java.lang.String, java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditNotPass(String[] id, String userId, String resultDesc) {
		String result = "no";
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = auditService.checkAuditData(id, auditList);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		for(int i=0;i<auditList.size();i++){
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("1"); //审核状态改为不通过
			audit.setResultDesc(resultDesc);
			ChnlGoods chnlGoods = (ChnlGoods) JsonHFUtil.getObjFromJsonArrStr(audit
					.getModData(), ChnlGoods.class);
			chnlGoods.setModLock(0); // 修改锁定状态为未锁定
			if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
				if (chnlGoodsDao.updateModLock(chnlGoods) == 1) {
					log.info("修改锁成功" + chnlGoods.toString());
				} else {
					log.error("修改锁失败" + chnlGoods.toString());
					throw new DAOException("修改锁失败");
				}
			}
			if (auditDao.updateState(audit) == 1) { // t_hfaudit
				log.info("修改审核状态成功" + audit.toString());
				result = "yes";
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		}
		return result;
	}

	
	/** ********************************************
	 * method name   : auditPass 
	 * modified      : xuhuafeng ,  2013-3-29
	 * @see          : @see com.umpay.hfmng.service.ChnlGoodsService#auditPass(java.lang.String[], java.lang.String, java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditPass(String[] id, String userId, String resultDesc) {
		String result = "no"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = auditService.checkAuditData(id, auditList);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		for(int i=0;i<auditList.size();i++){
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("2"); //审核状态改为通过
			audit.setResultDesc(resultDesc);
			ChnlGoods chnlGoods = (ChnlGoods) JsonHFUtil.getObjFromJsonArrStr(audit
					.getModData(), ChnlGoods.class);
			chnlGoods.setModLock(0); // 修改锁定状态为未锁定
			if ("1".equals(audit.getAuditType())) { // 新增入库
				chnlGoodsDao.insert(chnlGoods);
				log.info("新增入渠道商品表成功" + chnlGoods.toString());
			} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
				if(chnlGoodsDao.update(chnlGoods) == 1){
					log.info("启用/禁用渠道商品成功" + chnlGoods.toString());
				}else{
					log.error("启用/禁用渠道商品失败" + chnlGoods.toString());
					throw new DAOException("启用/禁用渠道商品失败");
				}
			} else {
				log.error("非法的审核类型");
				throw new DAOException("非法的审核类型");
			}
			if (auditDao.updateState(audit) == 1) {
				log.info("修改审核状态成功" + audit.toString());
				result = "yes";
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		}
		return result;
	}

}
