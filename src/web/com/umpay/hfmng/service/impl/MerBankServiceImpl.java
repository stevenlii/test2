/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankServiceImpl.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-25
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
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerBankService;
import com.umpay.sso.org.User;

/**
 * ****************** 类说明 ********************* class : MerBankServiceImpl
 * 
 * @author : xhf
 * @version : 1.0 description :
 * ************************************************/
@Service
public class MerBankServiceImpl implements MerBankService {

	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditService auditService;
	@Autowired
	private MerBankDao merBankDao;

	/**
	 * ******************************************** method name : load modified
	 * : xhf , 2012-9-25
	 * 
	 * @see : @see com.umpay.hfmng.service.MerBankService#load(java.util.Map)
	 * ********************************************/
	public MerBank load(Map<String, String> mapWhere) {
		return merBankDao.get(mapWhere);
	}

	/**
	 * ******************************************** method name : findByMerId
	 * modified : xhf , 2012-9-27
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.MerBankService#findByMerId(java.lang.String)
	 * ********************************************/
	public Map<String, MerBank> findByMerId(String merId) {
		List<MerBank> list = merBankDao.findByMerId(merId);
		Map<String, MerBank> map = new HashMap<String, MerBank>();
		for (int i = 0; i < list.size(); i++) {
			MerBank bank = (MerBank) list.get(i);
			bank.trim();
			map.put(bank.getBankId(), bank);
		}
		return map;
	}

	/**
	 * ******************************************** method name : saveAudit
	 * modified : xhf , 2012-9-28
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.MerBankService#saveAudit(com.umpay.hfmng
	 *      .model.MerBank)
	 * ********************************************/
	// 删除事务 wanyong 2014-04-17 工单号：PKS00005415
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveAudit(MerBank merBank) {
		String result = "no"; // 操作结果 1表示成功, 0 表示失败
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_MER_BANK");
		audit.setState("0"); // 审核状态 0：待审核
		String auditType = merBank.getAuditType();
		audit.setAuditType(auditType); // 审核类型 1：新增开通；3：修改开通；4：修改关闭
		String jsonString = JsonHFUtil.getJsonArrStrFrom(merBank);
		audit.setModData(jsonString);
		audit.setCreator(merBank.getModUser()); // 创建人 是当前登录用户
		audit.setResultDesc("");
		audit.setIxData(merBank.getMerId() + "-" + merBank.getBankId());
		auditDao.insert(audit);
		log.info("新增审核信息成功" + audit.toString());
		if ("3".equals(auditType) || "4".equals(auditType)) {
			if (merBankDao.updateMerBankModLock(merBank) == 1) {
				log.info("更新锁定状态成功" + merBank.toString());
			} else {
				log.error("更新锁定状态失败" + merBank.toString());
				throw new DAOException("更新锁定状态失败！");
			}
		}

		/*
		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。
		 * 需求开发人：万勇（工单号：PKS00005415）
		 * 需求开发思路：尽量避免改动现有代码，修改商品银行时，系统自动模拟审核人员调用审核通过操作
		 */
		auditService.merBankauditPass(new String[] { audit.getId() }, LoginUtil.getUser());

		result = "yes";
		return result;
	}

	// 启用禁用方法
	/**
	 * ******************************************** method name :
	 * enableAnddisable modified : xhf , 2012-10-8
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.MerBankService#enableAnddisable(java.lang
	 *      .String[], com.umpay.sso.org.User, java.lang.String)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAnddisable(String[] array, User user, String action) {
		String result = "no";
		try {
			for (int i = 0; i < array.length; i++) {
				result = "no"; // 循环操作时，每次操作之前 应该先将操作状态置为失败（”0“）
				String[] merBankId = array[i].split("-");
				MerBank merBank = new MerBank();
				merBank.setMerId(merBankId[0]);
				merBank.setBankId(merBankId[1]);
				merBank.setState(action); // 目标设置状态 2为开通 、4为关闭
				merBank.setModLock(1);

				String jsonString = JsonHFUtil.getJsonArrStrFrom(merBank);
				Audit audit = new Audit();
				audit.setTableName("UMPAY.T_MER_BANK");
				audit.setIxData(array[i]);
				audit.setModData(jsonString);
				if (action.equals("2")) {
					audit.setAuditType("3"); // 目标状态为2 启用操作，审核类型为启用（3）
				} else if (action.equals("4")) {
					audit.setAuditType("4"); // 目标状态为4 禁用操作，审核类型为禁用（4）
				}
				audit.setCreator(user.getId()); // 提交人

				auditDao.insert(audit);
				log.info("入审核表启用/禁用操作成功！" + audit.toString());
				if (merBankDao.updateMerBankModLock(merBank) == 1) {
					log.info("修改锁操作成功！" + merBank.toString());
					result = "yes";
				} else {
					log.error("修改锁操作失败！" + merBank.toString());
					throw new DAOException("操作失败");
				}

				/*
				 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。
				 * 需求开发人：万勇（工单号：PKS00005415）
				 * 需求开发思路：尽量避免改动现有代码，修改商品银行时，系统自动模拟审核人员调用审核通过操作
				 */
				auditService.merBankauditPass(new String[] { audit.getId() }, user);
			}
		} catch (Exception e) {
			log.error("启用/禁用操作失败！", e);
			throw new DAOException("启用/禁用操作失败");
		}
		return result;
	}

	/**
	 * ******************************************** method name : saveMerBank
	 * modified : xhf , 2012-11-21
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.MerBankService#saveMerBank(com.umpay.hfmng
	 *      .model.MerBank, java.util.List, java.util.List, java.util.List)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveMerBank(MerBank merBank, List<String> newOpen, List<String> modOpen, List<String> modClose) {
		String result = "no";
		for (int i = 0; i < newOpen.size(); i++) {
			result = "no";
			merBank.setBankId(newOpen.get(i));
			merBank.setState("2");
			merBank.setAuditType("1");
			result = saveAudit(merBank);
			log.info("商户银行提交审核成功：审核类型[新增]，" + merBank.toString());
		}
		for (int i = 0; i < modOpen.size(); i++) {
			result = "no";
			merBank.setBankId(modOpen.get(i));
			merBank.setState("2");
			merBank.setAuditType("3");
			result = saveAudit(merBank);
			log.info("商户银行提交审核成功：审核类型[启用]，" + merBank.toString());
		}
		for (int i = 0; i < modClose.size(); i++) {
			result = "no";
			merBank.setBankId(modClose.get(i));
			merBank.setState("4");
			merBank.setAuditType("4");
			result = saveAudit(merBank);
			log.info("商户银行提交审核成功：审核类型[禁用]，" + merBank.toString());
		}
		return result;
	}

	/**
	 * ******************************************** method name :
	 * saveMerBankList description : 批量人审核表
	 * 
	 * @return : String
	 * @param : @param merBankList
	 * @param : @return modified : xuhuafeng , 2013-4-24 下午04:27:43
	 *        *******************************************
	 */
	// 删除事务 wanyong 2014-04-17 工单号：PKS00005415
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	private String saveMerBankList(List<MerBank> merBankList) {
		String result = "yes";
		for (MerBank merBank : merBankList) {
			saveAudit(merBank);
		}
		log.info("merBankList入审核表成功"); // 若失败则直接抛异常，执行不到此处
		return result;
	}

	/**
	 * ******************************************** method name : batchUpdate
	 * modified : xuhuafeng , 2013-4-24
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.MerBankService#batchUpdate(java.lang.String
	 *      [], java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/
	@SuppressWarnings("unchecked")
	// 增加方法事务 wanyong 2014-04-17 工单号：PKS00005415
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String batchUpdate(String[] merIds, String bankId, String state, String userId) {
		String bankMsg = ""; // 出错时打印错误信息
		Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		String result = "yes";
		List<MerBank> merBankList = new ArrayList<MerBank>();
		try {
			for (int j = 0; j < merIds.length; j++) {
				String id = merIds[j];
				Object mer = merMap.get(id);
				if (mer == null) {
					result = "不存在商户号为" + id + "的商户！";
					log.error(result);
					return result;
				}
				// 1-获取页面的商户号、小额银行串a
				String[] banksId = bankId.split(",");
				// 2-获取此商户号在库里的小额银行状态b和锁定状态
				Map<String, MerBank> merBankMap = findByMerId(id);
				Map<String, String> mapWhere = new HashMap<String, String>();
				mapWhere.put("ixData", id + "-");
				mapWhere.put("tableName", "UMPAY.T_MER_BANK");
				Map auditMap = auditService.getMerBankAudit(mapWhere);
				for (int i = 0; i < banksId.length; i++) {
					if ("1".equals(banksId[i]) || "2".equals(banksId[i])) {
						continue;
					}
					MerBank merBankToAudit = new MerBank();
					merBankToAudit.setMerId(id);
					merBankToAudit.setBankId(banksId[i]);
					merBankToAudit.setState(state);
					merBankToAudit.setModUser(userId);
					merBankToAudit.setModLock(1);
					bankMsg = id + "-" + banksId[i] + "(" + bankNameMap.get(banksId[i]) + ")";
					MerBank merBank = (MerBank) merBankMap.get(banksId[i]);
					if (merBank == null) {
						// 如果此银行和此商户号没有关联关系
						if ("4".equals(state)) {
							// 如果此次操作为禁用
							result = "商户号" + id + "下的[" + bankNameMap.get(banksId[i]) + "]银行不存在，无法禁用操作！";
							log.error(result);
							return result;
						}
						// 如果此次操作为启用
						Object audit = auditMap.get(banksId[i]); // 查询是否存在
						if (audit == null) {
							merBankToAudit.setAuditType("1");
							merBankList.add(merBankToAudit);
							log.info("新增商户银行启用" + bankMsg);
						} else {
							result = "商户银行[" + bankMsg + "]已送交审核，无法重复提交！";
							log.error(result);
							return result;
						}
					} else {
						// 如果此商户和此银行存在关联关系,则再看锁定状态，若未锁定，则再看state，若为4，则为修改开通；若为2，则为修改关闭
						if (merBank.getModLock() == 1) {
							// 若已锁定
							result = "商户银行[" + bankMsg + "]已经锁定！";
							log.error(result);
							return result;
						} else {
							if (state.equals(merBank.getState())) {
								// 如果此商户银行已经跟将要修改成的状态一致，则跳过
								continue;
							} else {
								if ("2".equals(merBank.getState())) {
									merBankToAudit.setAuditType("4");
									merBankList.add(merBankToAudit);
									log.info("修改商户银行禁用" + bankMsg);
								} else if ("4".equals(merBank.getState())) {
									merBankToAudit.setAuditType("3");
									merBankList.add(merBankToAudit);
									log.info("修改商户银行启用" + bankMsg);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			result = "操作失败，请稍后再试";
			log.error(result, e);
			return result;
		}
		log.info("批量商户银行配置验证通过，下面开始入审核表");
		try {
			// 向审核表插入数据
			result = saveMerBankList(merBankList);
			log.info("批量商户银行配置入审核表成功");
		} catch (Exception e) {
			result = "提交审核失败，请稍后再试";
			log.error(result, e);
		}

		return result;
	}

	/**
	 * @Title: saveMerBankForSynMwMerBank
	 * @Description: 新增商户银行（此功能不走审核表，直接新增。并忽略主键冲突异常。工单PKS00005782定制方法）
	 * @param merBank
	 * @return
	 * @author wanyong
	 * @date 2014-8-14 下午3:34:27
	 */
	public String saveMerBankForSynMwMerBank(MerBank merBank) {
		try {
			merBankDao.saveMerBank(merBank);
		} catch (Exception e) {
			// 杀掉异常
		}
		return "1";
	}

	/**
	 * 
	 * @Title: getMerBankInfo
	 * @Description: 获取商户银行
	 * @param merId
	 * @return
	 * @author lituo
	 * @date 2014-8-11 下午04:56:46
	 */
	public List<BankInfo> getMerBankInfo(String merId) {
		return merBankDao.getMerBankInfo(merId);
	}
}
