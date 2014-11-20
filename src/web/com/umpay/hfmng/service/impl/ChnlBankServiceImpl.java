/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-19
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

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.ChnlBankDao;
import com.umpay.hfmng.dao.ChnlInfDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ChnlBank;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlBankService;


/** ******************  类说明  *********************
 * class       :  ChnlBankServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class ChnlBankServiceImpl implements ChnlBankService {
	
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ChnlBankDao chnlBankDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditService auditService;

	/** ********************************************
	 * method name   : findBankByChnlId 
	 * modified      : xuhuafeng ,  2013-3-19
	 * @see          : @see com.umpay.hfmng.service.ChnlBankService#findBankByChnlId(java.lang.String)
	 * ********************************************/
	public Map<String, ChnlBank> findBankByChnlId(String channelId) {
		List<ChnlBank> list = chnlBankDao.findBankByChnlId(channelId); 
		Map<String, ChnlBank> map = new HashMap<String, ChnlBank>();
		for(int i=0;i<list.size();i++){
			ChnlBank bank = list.get(i);
			map.put(bank.getBankId(), bank);
		}
		return map;
	}

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-3-19
	 * @see          : @see com.umpay.hfmng.service.ChnlBankService#load(java.lang.String, java.lang.String)
	 * ********************************************/
	public ChnlBank load(String channelId, String bankId) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("channelId", channelId);
		mapWhere.put("bankId", bankId);
		return chnlBankDao.get(mapWhere);
	}

	
	/** ********************************************
	 * method name   : saveChnlBank 
	 * modified      : xuhuafeng ,  2013-3-19
	 * @see          : @see com.umpay.hfmng.service.ChnlBankService#saveChnlBank(com.umpay.hfmng.model.ChnlBank, java.util.List, java.util.List, java.util.List)
	 * ********************************************/     
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String saveChnlBank(ChnlBank chnlBank, List<String> newOpen,
			List<String> modOpen, List<String> modClose) {
		String result = "no";
		for(int i=0;i<newOpen.size();i++){
			chnlBank.setBankId(newOpen.get(i));
			chnlBank.setState(2);
        	result = saveAudit(chnlBank, "1");
        	log.info("新增渠道银行配置成功"+chnlBank.toString());
        }
        for(int i=0;i<modOpen.size();i++){
        	chnlBank.setBankId(modOpen.get(i));
			chnlBank.setState(2);
        	result = saveAudit(chnlBank, "3");
        	log.info("启用渠道银行配置成功"+chnlBank.toString());
        }
        for(int i=0;i<modClose.size();i++){
        	chnlBank.setBankId(modClose.get(i));
        	chnlBank.setState(4);
        	result = saveAudit(chnlBank, "4");
        	log.info("禁用渠道银行配置成功"+chnlBank.toString());
        }
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private String saveAudit(ChnlBank chnlBank, String auditType) {
		String result="no";                      //操作结果 1表示成功, 0 表示失败
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_HF_CHNL_BANK");
		audit.setState("0");                     // 审核状态 0：待审核
		audit.setAuditType(auditType);                 // 审核类型 1：新增开通；3：修改开通；4：修改关闭
		String jsonString = JsonHFUtil.getJsonArrStrFrom(chnlBank);
		audit.setModData(jsonString);
		audit.setCreator(chnlBank.getService_user());    //创建人 是当前登录用户
		audit.setIxData(chnlBank.getChannelId()+"-"+chnlBank.getBankId());
		auditDao.insert(audit);
		log.info("新增审核信息成功"+audit.toString());
		if("3".equals(auditType) || "4".equals(auditType)){
			chnlBank.setModLock(1);
			if(chnlBankDao.updateModLock(chnlBank) == 1){
				log.info("更新锁定状态成功"+chnlBank.toString());
			}else{
				log.error("更新锁定状态失败"+chnlBank.toString());
				throw new DAOException("更新锁定状态失败！");
			}
		}
		result = "yes";
		return result;
	}

	
	/** ********************************************
	 * method name   : batchUpdate 
	 * modified      : xuhuafeng ,  2013-3-21
	 * @see          : @see com.umpay.hfmng.service.ChnlBankService#batchUpdate(java.lang.String[], java.lang.String)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String batchUpdate(String[] channelIds, String bankId, int state, String userId) {
		String result = "no";
		String bankMsg = "";  //出错时打印错误信息
		Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
		Map<String, ChnlInf> chnlMap = HfCacheUtil.getCache().getChnlInfMap();
		List<ChnlBank> chnlBankList = new ArrayList<ChnlBank>();
		for(int j=0;j<channelIds.length;j++){
			String channelId = channelIds[j];
			ChnlInf chnl = chnlMap.get(channelId);
			if(chnl == null){
				result = "不存在的渠道号"+channelId;
				log.error(result);
				return result;
			}
			//1-获取页面的渠道号、银行串a
			String[] banksId = bankId.split(",");
			//2-获取此渠道号在库里的银行状态b和锁定状态
			Map<String, ChnlBank> banks = findBankByChnlId(channelId);
			Map<String, String> mapWhere = new HashMap<String, String>();
			mapWhere.put("ixData", channelId + "-");
			mapWhere.put("tableName", "UMPAY.T_HF_CHNL_BANK");
			Map audit = auditService.getMerBankAudit(mapWhere);
			for(int i=0;i<banksId.length;i++){
				if("1".equals(banksId[i]) || "2".equals(banksId[i])){
					continue;
				}
				ChnlBank chnlBank = new ChnlBank();
				chnlBank.setChannelId(channelId);
				chnlBank.setBankId(banksId[i]);
				chnlBank.setState(state);
				chnlBank.setService_user(userId);
				chnlBank.setModLock(1);
				bankMsg = "渠道银行["+channelId+"-"+banksId[i]+"("+bankNameMap.get(banksId[i])+")]";
				ChnlBank bank = banks.get(banksId[i]);
				if(bank == null){
					if(state == 4){
						result = bankMsg+"没有启用，无法禁用操作！";
						log.error(result);
						return result;
					}
					//3-1从a中循环取出一个元素i，与b比较，若b中不含i，则为新增开通；
					Object o = audit.get(banksId[i]); // 查询是否存在
					if(o == null){
						chnlBank.setAuditType("1");
						chnlBankList.add(chnlBank);
						log.info("新增渠道银行启用"+channelId+"-"+banksId[i]);
					}else{
						result = bankMsg+"已送交审核，无法重复提交！";
						log.error(result);
						return result;
					}
				}else{
					// 3-2     若b中含i,则再看锁定状态，若为0，则再看state，若为4，则为修改开通；若为2，则为修改关闭
					if(bank.getModLock() == 1){
						result = bankMsg+"已经锁定！";
						log.error(result);
						return result; 
					}else{
						if(bank.getState() != state){
							if(bank.getState() == 2){
								chnlBank.setAuditType("4");
								chnlBankList.add(chnlBank);
								log.info("修改禁用"+bankMsg);
							}else if(bank.getState() == 4){
								chnlBank.setAuditType("3");
								chnlBankList.add(chnlBank);
								log.info("修改启用"+bankMsg);
							}
						}
					}
				}
			}
		}
		log.info("批量渠道银行配置验证通过，下面开始入审核表");
		try{
			//向审核表插入数据
			result = saveChnlBankList(chnlBankList);
			log.info("批量渠道银行配置入审核表成功");
		}catch (Exception e) {
			result = "提交审核失败，请稍后再试";
			log.error(result, e);
		}
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private String saveChnlBankList(List<ChnlBank> chnlBankList){
		String result = "yes";
		for(ChnlBank chnlBank : chnlBankList){
			saveAudit(chnlBank, chnlBank.getAuditType());
		}
		log.info("chnlBankList入审核表成功"); //若失败则直接抛异常，执行不到此处
		return result;
	}

	
	/** ********************************************
	 * method name   : enableAnddisable 
	 * modified      : xuhuafeng ,  2013-3-26
	 * @see          : @see com.umpay.hfmng.service.ChnlBankService#enableAnddisable(java.lang.String, java.lang.String, int)
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
		ChnlBank chnlBank = new ChnlBank();
		chnlBank.setState(action);
		chnlBank.setService_user(userId);
		String[] array = ID.split(",");
		for (int i = 0; i < array.length; i++) {
			result = "no";
			String[] key = array[i].split("-");
			if(key.length != 2){
				return result;
			}
			ChnlBank chBank = load(key[0], key[1]);
			if(chBank == null){
				log.error("不存在的渠道银行：" + array[i]);
				return result;
			}
			chBank.trim();
			if(chBank.getModLock() == 1){
				log.error("该渠道银行已锁定，无法操作" + chBank.toString());
				return result;
			}
			if(chBank.getState() == action){
				log.error("该渠道银行已为目标状态：" + chBank.toString());
				return result;
			}
			chnlBank.setChannelId(key[0]);
			chnlBank.setBankId(key[1]);
			result = saveAudit(chnlBank, auditType);
		}
		return result;
	}

}
