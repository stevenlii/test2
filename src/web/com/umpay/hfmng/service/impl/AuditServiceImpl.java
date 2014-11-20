/** *****************  JAVA头文件说明  ****************
 * file name  :  AuditServiceImpl.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-8-30
 * *************************************************/

package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.BankDao;
import com.umpay.hfmng.dao.ChnlBankDao;
import com.umpay.hfmng.dao.CouponInfDao;
import com.umpay.hfmng.dao.CouponRuleDao;
import com.umpay.hfmng.dao.GoodsBankDao;
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.dao.MerInfoDao;
import com.umpay.hfmng.dao.MerOperDao;
import com.umpay.hfmng.dao.ReportDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.ChnlBank;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponRule;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.model.MerGrade;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.model.ReportInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.CouponRuleService;
import com.umpay.hfmng.service.MerGradeService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.sso.org.User;

/**
 * ****************** 类说明 ********************* class : AuditServiceImpl
 * 
 * @author : xhf
 * @version : 1.0 description : 商品、商户审核
 *          ***********************************************
 */
@Service
public class AuditServiceImpl implements AuditService {

	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MerInfoDao merDao;
	@Autowired
	private GoodsInfoDao goodsDao;
	@Autowired
	private MerBankDao merBankDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private GoodsBankDao goodsBankDao;
	@Autowired
	private CouponInfDao couponInfDao;
	@Autowired
	private CouponRuleDao couponRuleDao;
	@Autowired
	private CouponRuleService couponRuleService;
	@Autowired
	private MerGradeDao merGradeDao;
	@Autowired
	private MerGradeService merGradeService;
	@Autowired
	private ChnlBankDao chnlBankDao;
	@Autowired
	private MerOperDao merOperDao;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private MerOperService merOperService;

	/**
	 * ******************************************** method name :
	 * goodsAuditNotPass modified : xhf , 2012-8-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#goodsAuditNotPass(com.umpay
	 *      .hfmng.model.Audit) *
	 *******************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String goodsAuditNotPass(Audit audit) {
		String result = "no"; // 返回结果初始化为no
		GoodsInfo goods = new GoodsInfo();
		String[] arr = audit.getIxData().split("-");
		goods.setMerId(arr[0]);
		goods.setGoodsId(arr[1]);
		goods.setModLock(0);
		if ("2".equals(audit.getAuditType()) || "3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
			if (goodsDao.updateGoodsLock(goods) == 1) {
				log.info("修改锁成功" + goods.toString());
			} else {
				log.error("修改锁失败" + goods.toString());
				throw new DAOException("修改锁失败！");
			}
		}
		if (auditDao.updateState(audit) == 1) {
			result = "yes"; // 入库成功，返回结果yes
			log.info("修改审核状态成功" + audit.toString());
		} else {
			log.error("修改审核状态失败" + audit.toString());
			throw new DAOException("修改审核状态失败！");
		}
		return result;
	}

	/**
	 * ******************************************** method name : goodsAuditPass
	 * modified : xhf , 2012-8-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#goodsAuditPass(com.umpay.
	 *      hfmng.model.Audit) *
	 *******************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String goodsAuditPass(Audit audit) {
		String result = "no"; // 返回结果初始化为no
		GoodsInfo goods = (GoodsInfo) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), GoodsInfo.class);
		goods.setModLock(0); // 修改锁定状态为未锁定
		goods.setModTime(new Timestamp(System.currentTimeMillis()));

		if ("1".equals(audit.getAuditType())) { // 新增入库
			/**
			 * start 为满足商户商品报备平台化功能，在新增商品时添加该商品报备源数据，即新增31个省的报备数据到表T_REPORT_INF中
			 * 2014-07-18 by wanyong
			 */
			ReportInf reportInf = new ReportInf();
			reportInf.setMerId(goods.getMerId());
			reportInf.setGoodsId(goods.getGoodsId());
			List<BankInfo> bankInfoList = bankDao.findXEBankInfos();
			for (BankInfo bankInfo : bankInfoList) {
				reportInf.setBackupStat(0); // 未报备
				reportInf.setBankId(bankInfo.getBankId());
				// 生成商户商品报备信息ID
				reportInf.setBackupId(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())
						+ SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_REPORTINF, 10));

				reportDao.insertReportInf(reportInf);
			}
			log.debug("新增商户商品报备信息成功");
			/** end ****************************************************************************************************/

			goodsDao.saveGoodsExp(goods);
			log.info("新增入商品扩展表成功" + goods.toString());
			if ("3".equals(goods.getServType())) {
				goodsDao.saveMonGoods(goods);
				log.info("新增入包月信息表成功" + goods.toString());
			}
			goodsDao.saveGoodsInf(goods);
			log.info("新增入商品表成功" + goods.toString());
			if (auditDao.updateState(audit) == 1) {
				log.info("修改审核状态成功" + audit.toString());
				result = "yes";
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("商品审核失败！");
			}
		} else if ("2".equals(audit.getAuditType())) { // 修改入库
			if ("3".equals(goods.getServType())) {
				if (goodsDao.updateMonGoods(goods) == 1) {
					log.info("更新包月信息表成功" + goods.toString());
				} else {
					log.error("更新包月信息表失败" + goods.toString());
					throw new DAOException("更新包月信息表失败");
				}
			}
			int updateGoodsExp = goodsDao.updateGoodsExp(goods);
			int updateGoodsInf = goodsDao.updateGoodsInf(goods);
			int updateState = auditDao.updateState(audit);

			if (updateGoodsExp == 1) {
				log.info("更新商品扩展表信息成功" + goods.toString());
			} else {
				log.error("更新商品扩展表失败！" + goods.toString());
				throw new DAOException("更新商品扩展表失败");
			}
			if (updateGoodsInf == 1) {
				log.info("更新商品表信息成功" + goods.toString());
			} else {
				log.error("更新商品表信息失败！" + goods.toString());
				throw new DAOException("更新商品表失败");
			}
			if (updateState == 1) {
				log.info("更新审核状态成功" + audit.toString());
				result = "yes";
			} else {
				log.error("更新审核状态失败！" + audit.toString());
				throw new DAOException("更新审核状态失败");
			}
		} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
			int notAble = goodsDao.isOrNotAble(goods);
			int updateGoodsLock = goodsDao.updateGoodsLock(goods);
			int ustate = auditDao.updateState(audit);
			if (notAble == 1) {
				log.info("启用/禁用操作成功" + goods.toString());
			} else {
				log.error("启用/禁用失败" + goods.toString());
				throw new DAOException("启用/禁用失败");
			}
			if (updateGoodsLock == 1) {
				log.info("修改锁操作成功" + goods.toString());
			} else {
				log.error("修改锁失败" + goods.toString());
				throw new DAOException("修改锁失败");
			}
			if (ustate == 1) {
				log.info("修改审核状态成功" + audit.toString());
				result = "yes";
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		} else {
			log.error("非法的审核类型");
		}
		if ("yes".equals(result)) { // 入库成功，刷新缓存
			HfCache cache = HfCacheUtil.getCache();
			String key = goods.getMerId() + "-" + goods.getGoodsId();
			cache.getGoodsInfoMap().remove(key);
			cache.getGoodsInfoMap().put(key, goods);
		}
		return result;
	}

	/**
	 * ******************************************** method name : load modified
	 * : xhf , 2012-8-30
	 * 
	 * @see : @see com.umpay.hfmng.service.AuditService#load(java.util.Map) *
	 *******************************************/
	public Audit load(Map<String, String> mapWhere) throws DataAccessException {
		return auditDao.get(mapWhere);
	}

	public Audit loadBatchId(Map<String, String> mapWhere) throws DataAccessException {
		return auditDao.getBatchId(mapWhere);
	}

	@SuppressWarnings("unchecked")
	public List findByBatchId(String batchId) {
		List list = auditDao.findByBatchId(batchId);
		return list;
	}

	/**
	 * ******************************************** method name :
	 * merAuditNotPass modified : xhf , 2012-8-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#merAuditNotPass(com.umpay
	 *      .hfmng.model.Audit) *
	 *******************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String merAuditNotPass(Audit audit) {
		String result = "no";
		MerInfo mer = new MerInfo();
		mer.setMerId(audit.getIxData());
		mer.setModLock(0);
		if ("2".equals(audit.getAuditType()) || "3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
			if (merDao.updateMerLock(mer) == 1) { // t_hfmer_exp
				log.info("修改锁成功" + mer.toString());
			} else {
				log.error("修改锁失败" + mer.toString());
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
		return result;
	}

	/**
	 * ******************************************** method name : merAuditPass
	 * modified : xhf , 2012-8-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#merAuditPass(com.umpay.hfmng
	 *      .model.Audit) *
	 *******************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String merAuditPass(Audit audit) {
		String result = "yes"; // 返回结果初始化为no
		HfCache cache = HfCacheUtil.getCache();
		MerInfo mer = (MerInfo) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), MerInfo.class);
		mer.setModLock(0);
		mer.setModTime(new Timestamp(System.currentTimeMillis()));
		List<HfMerOper> merOperList = new ArrayList<HfMerOper>();
		String operatorStr = mer.getOperator();
		String[] operator = operatorStr.split(",");
		if ("1".equals(audit.getAuditType())) { // 新增入库
			merDao.saveMerExp(mer);
			log.info("新增商户扩展表成功" + mer.toString());
			merDao.saveMerInfo(mer);
			log.info("新增商户表成功" + mer.toString());
			//新增商户时并审核通过后默认开通全网所有银行modify by wangyuxin in 20140815
			List<BankInfo> bankInfo = bankDao.findQWBankInfos();
			for(BankInfo b:bankInfo){
				MerBank m = new MerBank();
				m.setBankId(b.getBankId());
				m.setMerId(mer.getMerId());
				m.setModLock(0);
				m.setState("2");
				merBankDao.saveMerBank(m);
			}
			//end modify

			for (int i = 0; i < operator.length; i++) {
				HfMerOper merOper = new HfMerOper();
				merOper.setMerId(mer.getMerId());
				merOper.setOperator(operator[i]);
				merOper.setState(2);
				merOper.setCreator(mer.getModUser());
				merOper.setModUser(mer.getModUser());
				merOperList.add(merOper);
			}
			merOperDao.bacthInsert(merOperList);
			log.info("新增商户运营负责人成功");

			/** start 增加商户、商品报备平台化功能 2014-07-18 by wanyong */
			// 由于增加商户属性，在不影响现有功能的情况下，新加一张商户扩展表（T_HFMER_EXP_ATTR）
			merDao.saveMerExpAttr(mer);
			log.debug("新增报备平台化商户扩展表成功");
			/** end ***************************************************/

			if (auditDao.updateState(audit) == 1) {
				log.info("更新审核状态成功" + audit.toString());
			} else {
				log.error("更新审核状态失败" + audit.toString());
				throw new DAOException("更新审核状态失败");
			}
		} else if ("2".equals(audit.getAuditType())) { // 修改入库
			/** start 增加商户、商品报备平台化功能 2014-07-18 by wanyong */
			// 由于增加商户属性，在不影响现有功能的情况下，新加一张商户扩展表（T_HFMER_EXP_ATTR）
			if (merDao.updateMerExpAttr(mer) == 1) {
				log.info("修改报备平台化商户扩展表成功" + mer.toString());
			} else {
				merDao.saveMerExpAttr(mer);
				log.debug("新增报备平台化商户扩展表成功");
				// log.error("修改报备平台化商户扩展表失败" + mer.toString());
				// throw new DAOException("修改报备平台化商户扩展表失败");
			}
			/** end ***************************************************/

			if (merDao.updateMerExp(mer) == 1) { // t_hfmer_exp
				log.info("修改商户扩展表成功" + mer.toString());
			} else {
				log.error("修改商户扩展表失败" + mer.toString());
				throw new DAOException("修改商户扩展表失败");
			}

			if (merDao.updateMerInfo(mer) == 1) { // t_mer_inf
				log.info("修改商户表成功" + mer.toString());
			} else {
				log.error("修改商户表失败" + mer.toString());
				throw new DAOException("修改商户表失败");
			}

			merOperService.batchUpdate(mer.getMerId(), operator, mer.getModUser());
			log.info("更新商户[" + mer.getMerId() + "]的运营负责人成功");

			HfMerOper temp = new HfMerOper();
			temp.setMerId(mer.getMerId());
			merOperList = merOperDao.findBy(temp);
			for (int i = 0; i < merOperList.size(); i++) {
				merOperList.get(i).trim();
			}

			if (auditDao.updateState(audit) == 1) { // t_hfaudit
				log.info("修改审核状态成功" + audit.toString());
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
			if (merDao.isOrNotAble(mer) == 1) { // t_mer_inf
				log.info("修改商户状态成功" + mer.toString());
			} else {
				log.error("修改商户状态失败" + mer.toString());
				throw new DAOException("修改商户状态失败");
			}
			if (merDao.updateMerLock(mer) == 1) { // t_hfmer_exp
				log.info("修改锁成功" + mer.toString());
			} else {
				log.error("修改锁失败" + mer.toString());
				throw new DAOException("修改锁失败");
			}
			if (auditDao.updateState(audit) == 1) { // t_hfaudit
				log.info("修改审核状态成功" + audit.toString());
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		} else {
			log.error("非法的审核类型");
			result = "no";
		}
		if ("yes".equals(result)) { // 入库成功，刷新缓存
			cache.getMerInfoMap().put(mer.getMerId(), mer);
			if ("1".equals(audit.getAuditType()) || "2".equals(audit.getAuditType())) {
				List<HfMerOper> list = cache.getMerOperListByMerId(mer.getMerId());
				list.clear();
				list.addAll(merOperList);
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name : getCheck
	 * modified : zhaojunbao , 2012-8-28
	 * 
	 * @see : @see com.umpay.hfmng.service.AuditService#getCheck(java.util.Map)
	 *      *
	 *******************************************/
	@SuppressWarnings("unchecked")
	public String getCheck(Map<String, String> mapWhere) {

		List<Object> AuditTable = (List<Object>) auditDao.getCheckFromAudit(mapWhere);
		List<Object> GoodsTable = (List<Object>) goodsDao.getCheckFromTgoods(mapWhere);
		if (AuditTable.size() == 0 && GoodsTable.size() == 0) {
			return "1"; // 1 表示不存在
		}
		return "0"; // 表示存在

	}

	@SuppressWarnings("unchecked")
	public String getCheckMerId(Map<String, String> mapWhere) {
		List<Object> result1 = (List<Object>) merDao.getCheckFromMers(mapWhere);
		if (result1.size() != 0) {
			return "0"; // 0 表示存在
		}
		mapWhere.put("ixData", mapWhere.get("merId"));
		mapWhere.put("tableName", "UMPAY.T_MER_INF");
		List<Object> result2 = (List<Object>) auditDao.getCheckFromAudit(mapWhere);
		if (result2.size() != 0) {
			return "0"; // 0 表示存在
		}
		return "1"; // 1 表示不存在
	}

	// @SuppressWarnings("unchecked")
	// public String getCheckMerName(Map<String, String> mapWhere) {
	// List<Object> result1 = (List<Object>) merDao.getCheckFromMers(mapWhere);
	// if (result1.size() != 0) {
	// return "0"; // 0 表示存在
	// }
	// mapWhere.put("ixData", mapWhere.get("merId"));
	// mapWhere.put("modData", mapWhere.get("merName"));
	// mapWhere.put("tableName", "UMPAY.T_MER_INF");
	// List<Object> result2 = (List<Object>) auditDao
	// .getCheckFromAudit(mapWhere);
	// if (result2.size() != 0) {
	// return "0"; // 0 表示存在
	// }
	// return "1"; // 1 表示不存在
	// }
	//
	// @SuppressWarnings("unchecked")
	// public String checkModMerName(Map<String, String> mapWhere) {
	// List<Object> result1 = (List<Object>) merDao.checkModMerName(mapWhere);
	// if (result1.size() != 0) {
	// return "0"; // 0 表示存在
	// }
	// mapWhere.put("ixData", mapWhere.get("merId"));
	// mapWhere.put("modData", mapWhere.get("merName"));
	// mapWhere.put("tableName", "UMPAY.T_MER_INF");
	// List<Object> result2 = (List<Object>) auditDao
	// .checkModMerName(mapWhere);
	// if (result2.size() != 0) {
	// return "0"; // 0 表示存在
	// }
	// return "1"; // 1 表示不存在
	// }

	/**
	 * ******************************************** method name :
	 * merBankauditNotPass modified : xhf , 2012-10-10
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#merBankauditNotPass(com.umpay
	 *      .hfmng.model.Audit) *
	 *******************************************/
	public String merBankauditNotPass(String[] array, User user, String resultDesc) {
		String result = "yes"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = checkAuditData(array, auditList);
		if (!"SUCCESS".equals(checkResult)) {
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setState("1"); // 审核状态改为不通过
			audit.setModUser(user.getId());
			audit.setResultDesc(resultDesc);
			MerBank merBank = new MerBank();
			String[] arr = audit.getIxData().split("-");
			merBank.setMerId(arr[0]);
			merBank.setBankId(arr[1]);
			merBank.setModLock(0);
			if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
				int updateMerBank = merBankDao.updateMerBankModLock(merBank); // 入库t_mer_bank
				if (updateMerBank == 1) {
					log.info("更新商户银行锁成功！" + merBank.toString());
				} else {
					log.error("更新商户银行锁失败！" + merBank.toString());
					throw new DAOException("更新商户银行锁失败");
				}
			}
			int updateState = auditDao.updateState(audit); // 入库t_hfaudit
			if (updateState == 1) {
				log.info("更新审核表成功！" + audit.toString());
			} else {
				log.error("更新审核表失败!" + audit.toString());
				throw new DAOException("更新审核表失败");
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name :
	 * merBankauditPass modified : xhf , 2012-10-10
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#merBankauditPass(com.umpay
	 *      .hfmng.model.Audit) *
	 *******************************************/
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	// -- (modify by wanyong 2014-04-15 工单号：PKS00005415)
	public String merBankauditPass(String[] array, User user) {
		String result = "yes"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = checkAuditData1(array, auditList); // 审核数据验证（忽略自己不能审核验证）

		if (!"SUCCESS".equals(checkResult)) {
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setState("2"); // 审核状态改为通过
			audit.setModUser(user.getId());
			MerBank merBank = (MerBank) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), MerBank.class);
			merBank.setModLock(0); // 修改锁定状态为未锁定
			if ("1".equals(audit.getAuditType())) { // 新增启用入库
				merBankDao.saveMerBank(merBank); // 入库t_mer_bank
				log.info("新增启用商户银行成功！" + merBank.toString());
				int updateState = auditDao.updateState(audit); // 入库t_hfaudit
				if (updateState == 1) {
					log.info("更新审核表成功！" + audit.toString());
				} else {
					log.error("更新审核表失败!" + audit.toString());
					throw new DAOException("更新审核表失败");
				}
			} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 修改启用/禁用入库
				int updateMerBank = merBankDao.updateMerBank(merBank); // 入库t_mer_bank
				if (updateMerBank == 1) {
					log.info("启用/禁用商户银行成功！" + merBank.toString());
				} else {
					log.error("启用/禁用商户银行失败！" + merBank.toString());
					throw new DAOException("启用/禁用商户银行失败！");
				}
				int updateState = auditDao.updateState(audit); // 入库t_hfaudit
				if (updateState == 1) {
					log.info("更新审核表成功！" + audit.toString());
				} else {
					log.error("更新审核表失败!" + audit.toString());
					throw new DAOException("更新审核表失败");
				}
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name :批量通过
	 * goodsBankauditPass modified : anshuqiang , 2012-10-17
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#goodsBankauditPass(java.lang
	 *      .String[], com.umpay.sso.org.User)
	 *      *******************************************
	 */
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	// -- (modify by wanyong 2014-04-15 工单号：PKS00005415)
	public String goodsBankauditPass(String[] array, User user) {
		String result = "yes"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = checkAuditData1(array, auditList); // 审核数据验证（忽略不能自己审核验证）
		if (!"SUCCESS".equals(checkResult)) {
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setState("2"); // 审核状态改为通过
			audit.setModUser(user.getId());
			GoodsBank goodsBank = (GoodsBank) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), GoodsBank.class);
			goodsBank.setModLock(0); // 修改锁定状态为未锁定
			// 新增启用入库
			if ("1".equals(audit.getAuditType())) {
				// 入库t_goods_bank
				goodsBankDao.saveGoodsBank(goodsBank);
				log.info("新增启用商品银行成功！" + goodsBank.toString());
				// 入库t_hfaudit
				int updateState = auditDao.updateState(audit);
				if (updateState == 1) {
					log.info("更新审核表成功！" + audit.toString());
				} else {
					log.error("更新审核表失败!" + audit.toString());
					throw new DAOException("更新审核表失败");
				}
			} else if ("2".equals(audit.getAuditType())) { // 修改入库
				// 入库t_goods_bank
				int updateGoodsBank = goodsBankDao.updateGoodsBank(goodsBank);
				if (updateGoodsBank == 1) {
					log.info("修改商品银行成功！" + goodsBank.toString());
				} else {
					log.error("修改商品银行失败！" + goodsBank.toString());
					throw new DAOException("修改商品银行失败！");
				}
				// 入库t_hfaudit
				int updateState = auditDao.updateState(audit);
				if (updateState == 1) {
					log.info("更新审核表成功！" + audit.toString());
				} else {
					log.error("更新审核表失败!" + audit.toString());
					throw new DAOException("更新审核表失败");
				}
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name :批量不通过
	 * goodsBankauditNotPass modified : anshuqiang , 2012-10-17
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#goodsBankauditNotPass(java
	 *      .lang.String[], com.umpay.sso.org.User, java.lang.String)
	 *      *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String goodsBankauditNotPass(String[] array, User user, String resultDesc) {
		String result = "yes"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = checkAuditData(array, auditList);
		if (!"SUCCESS".equals(checkResult)) {
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setState("1"); // 审核状态改为不通过
			audit.setModUser(user.getId());
			audit.setResultDesc(resultDesc);
			GoodsBank goodsBank = new GoodsBank();
			String[] arr = audit.getIxData().split("-");
			goodsBank.setMerId(arr[0]);
			goodsBank.setGoodsId(arr[1]);
			goodsBank.setBankId(arr[2]);
			goodsBank.setModLock(0);// 解锁
			if ("2".equals(audit.getAuditType())) {// 审核修改
				// 入库t_goods_bank
				int updateGoodsBank = goodsBankDao.updateGoodsBankLock(goodsBank);
				if (updateGoodsBank == 1) {
					log.info("商品银行解锁成功！" + goodsBank.toString());
				} else {
					log.error("商品银行解锁失败！" + goodsBank.toString());
					throw new DAOException("商品银行解锁失败");
				}
			}
			// 入库t_hfaudit
			int updateState = auditDao.updateState(audit);
			if (updateState == 1) {
				log.info("更新审核表成功！" + audit.toString());
			} else {
				log.error("更新审核表失败!" + audit.toString());
				throw new DAOException("更新审核表失败");
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name : getCheckBankId
	 * modified : anshuqiang , 2012-10-17
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#getCheckBankId(java.util.Map)
	 *      *******************************************
	 */
	@SuppressWarnings("unchecked")
	public String getCheckBankId(Map<String, String> mapWhere) {
		List<Object> result1 = (List<Object>) auditDao.getCheckFromAudit(mapWhere);
		List<Object> result2 = (List<Object>) bankDao.getCheckFromBank(mapWhere);
		if (result1.size() == 0 && result2.size() == 0) {
			return "1"; // 1 表示不存在
		}
		return "0";
	}

	/**
	 * ******************************************** method name : getCheckBank
	 * description : 校验商品银行是否存在
	 * 
	 * @return : String
	 * @param : @param mapWhere
	 * @param : @return modified : zhaojunbao , 2012-10-15 下午05:01:24
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	public String getCheckBank(Map<String, String> mapWhere) {
		List<Object> AuditTable = (List<Object>) auditDao.getCheckFromAudit(mapWhere);
		if (AuditTable.size() == 0) {

			return "1"; // 1 表示不存在
		}
		return "0"; // 表示存在

	}

	/**
	 * ******************************************** method name :
	 * getCheckMerBank modified : xhf , 2012-10-16
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#getCheckMerBank(java.util.Map)
	 *      *
	 *******************************************/
	@SuppressWarnings("unchecked")
	public Map getMerBankAudit(Map<String, String> mapWhere) {
		List<Audit> result = auditDao.getMerBankList(mapWhere);
		Map bankMap = new HashMap();
		for (Audit a : result) {
			bankMap.put(a.getIxData().split("-")[1].trim(), a);
		}
		return bankMap;
	}

	/**
	 * ******************************************** method name :
	 * getCheckMerBankList description : 返回值为list类型
	 * 
	 * @return : List<Audit>
	 * @param : @param mapWhere
	 * @param : @return modified : zhaojunbao , 2012-11-15 下午02:38:57
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	public List<Audit> getCheckMerBankList(Map<String, String> mapWhere) {
		List<Audit> result1 = (List<Audit>) auditDao.getCheckFromAudit(mapWhere);
		return result1;
	}

	/**
	 * ******************************************** method name :
	 * goodsBankauditGoPass description : 批量号审核通过方法 modified : anshuqiang ,
	 * 2012-10-19
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#goodsBankauditGoPass(java
	 *      .lang.String[], com.umpay.sso.org.User)
	 *      *******************************************
	 */
	@SuppressWarnings("unchecked")
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	// -- (modify by wanyong 2014-04-15 工单号：PKS00005415)
	public String goodsBankauditGoPass(String batchId, User user) {
		String result = "no"; // 返回结果初始化为no
		List list = findByBatchId(batchId);
		for (int i = 0; i < list.size(); i++) {
			Audit a = (Audit) list.get(i);
			a.trim(); // 去空格
			if (!"0".equals(a.getState())) {
				return "部分数据已审核，不能再次审核，请刷新后再试";
			}
			// 忽略不能自己审核验证
			/*
			 * if (!checkAuditUser(a.getCreator())) { return
			 * "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。"; }
			 */
		}
		for (int i = 0; i < list.size(); i++) {
			Audit a = (Audit) list.get(i);
			List<GoodsBank> modData = (List<GoodsBank>) JsonHFUtil.getListFromJsonArrStr(a.getModData(),
					GoodsBank.class);
			GoodsBank goodsBank = modData.get(0);
			goodsBank.setModLock(0); // 修改锁定状态为未锁定
			// 新增启用入库
			if ("1".equals(a.getAuditType())) {
				// 入库t_goods_bank
				goodsBankDao.saveGoodsBank(goodsBank);
				log.info("新增启用商品银行成功！" + goodsBank.toString());
			} else if ("2".equals(a.getAuditType())) { // 修改入库
				// 批量配置时，若为修改，则不修改商品银行金额,故置空
				goodsBank.setAmount(null);
				// 入库t_goods_bank
				int updateGoodsBank = goodsBankDao.updateGoodsBank(goodsBank);
				if (updateGoodsBank == 1) {
					log.info("修改商品银行成功！" + goodsBank.toString());
				} else {
					log.error("修改商品银行失败！" + goodsBank.toString());
					throw new DAOException("修改商品银行失败！");
				}
			}
		}
		Audit audit = new Audit();
		audit.setBatchId(batchId);
		audit.setModUser(user.getId());
		audit.setState("2"); // 审核状态改为通过
		// 入库t_hfaudit
		int updateState = auditDao.batchUpdateState(audit);
		if (updateState == list.size()) {
			log.info("更新审核表成功！" + audit.toString());
			result = "yes";
		} else {
			log.error("更新审核表失败!" + audit.toString());
			throw new DAOException("更新审核表失败");
		}
		return result;
	}

	/**
	 * ******************************************** method name :
	 * goodsBankAuditNotPass description : 批量号审核不通过方法 modified : anshuqiang ,
	 * 2012-10-19
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#goodsBankAuditNotPass(java
	 *      .lang.String[], com.umpay.sso.org.User, java.lang.String)
	 *      *******************************************
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String goodsBankAuditNotPass(String batchId, User user, String resultDesc) {
		String result = "no"; // 返回结果初始化为no
		List list = findByBatchId(batchId);
		for (int i = 0; i < list.size(); i++) {
			Audit a = (Audit) list.get(i);
			a.trim(); // 去空格
			if (!"0".equals(a.getState())) {
				return "部分数据已审核，不能再次审核，请刷新后再试";
			}
			if (!checkAuditUser(a.getCreator())) {
				return "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。";
			}
		}
		for (int i = 0; i < list.size(); i++) {
			Audit a = (Audit) list.get(i);
			GoodsBank goodsBank = new GoodsBank();
			String[] arr = a.getIxData().split("-");
			goodsBank.setMerId(arr[0]);
			goodsBank.setGoodsId(arr[1]);
			goodsBank.setBankId(arr[2]);
			goodsBank.setModLock(0);
			if ("2".equals(a.getAuditType())) {
				// 入库t_goods_bank
				int updateGoodsBank = goodsBankDao.updateGoodsBankLock(goodsBank);
				if (updateGoodsBank == 1) {
					log.info("更新商品银行锁成功！" + goodsBank.toString());
				} else {
					log.error("更新商品银行锁失败！" + goodsBank.toString());
					throw new DAOException("更新商品银行锁失败");
				}
			}
		}
		Audit audit = new Audit();
		audit.setBatchId(batchId);
		audit.setState("1"); // 审核状态改为不通过
		audit.setModUser(user.getId());
		audit.setResultDesc(resultDesc);
		// 入库t_hfaudit
		int updateState = auditDao.batchUpdateState(audit);
		if (updateState == list.size()) {
			log.info("更新审核表成功！" + audit.toString());
			result = "yes";
		} else {
			log.error("更新审核表失败!" + audit.toString());
			throw new DAOException("");
		}
		return result;
	}

	/**
	 * @Title: couponAuditNotPass
	 * @Description: 审核不通过处理，支持兑换券审核/兑换券规则
	 * @param
	 * @param audit
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:57:21
	 */
	public void couponAuditNotPass(Audit audit) throws BusinessException, DataAccessException {
		if (auditDao.updateState(audit) == 1) {
			// 修改成功
			log.info("修改审核状态成功，数据" + audit.toString());
		} else {
			log.error("修改审核状态失败，数据" + audit.toString());
			throw new BusinessException("修改审核状态失败！");
		}
	}

	/**
	 * @Title: couponAuditPass
	 * @Description: 兑换券审核通过处理
	 * @param
	 * @param audit
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午11:02:00
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void couponAuditPass(Audit audit) throws BusinessException, DataAccessException {
		CouponInf couponInf = (CouponInf) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), CouponInf.class);
		couponInf.setModTime(new Timestamp(System.currentTimeMillis()));
		couponInf.setAuditUser(audit.getModUser());
		if ("1".equals(audit.getAuditType())) {
			// 新增入库
			couponInf.setState(Const.COUPON_INFSTATE_ENABLE); // 设置兑换券状态为启用
			couponInfDao.insertCouponInf(couponInf);
			log.info("新增兑换券信息表成功，数据" + couponInf.toString());
		} else if ("2".equals(audit.getAuditType())) {
			// 修改入库
			if (couponInfDao.updateCouponInf(couponInf) == 1) { // t_coupon_inf
				log.info("修改兑换券信息表成功，数据" + couponInf.toString());
			} else {
				log.error("修改兑换券信息表失败，数据" + couponInf.toString());
				throw new BusinessException("修改兑换券信息表失败！");
			}
		} else {
			log.error("非法的审核类型！");
			throw new BusinessException("非法的审核类型！");
		}
		if (auditDao.updateState(audit) == 1) { // t_hfaudit
			log.info("更新审核状态成功，数据" + audit.toString());
		} else {
			log.error("更新审核状态失败，数据" + audit.toString());
			throw new BusinessException("更新审核状态失败！");
		}
	}

	/**
	 * @Title: couponRuleAuditPass
	 * @Description: 兑换券规则审核通过处理
	 * @param
	 * @param audit
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午11:02:49
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void couponRuleAuditPass(Audit audit) throws BusinessException, DataAccessException {
		CouponRule couponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), CouponRule.class);
		couponRule.setModTime(new Timestamp(System.currentTimeMillis()));
		couponRule.setAuditUser(audit.getModUser());

		// /** start 效验 ******************************/
		// if ("1".equals(audit.getAuditType())
		// || Const.COUPON_RULESTATE_ENABLE.equals(couponRule.getState())) {
		// // 新增兑换券/修改成启用状态，需要增加兑换券规则唯一性判断
		// // 判断主键：兑换券、商户、商品、启用状态下是否存在数据，存在提示该兑换券规则已存在，不能添加
		// couponRule.setState(Const.COUPON_RULESTATE_ENABLE); // 设置启用状态为启用
		// if (couponRuleDao.findCouponRuleCount(couponRule) > 0) {
		// throw new BusinessException("已存在状态为【启用】的兑换券规则");
		// }
		// }
		// /** end 效验 ******************************/

		if ("1".equals(audit.getAuditType())) {
			/** start 效验 ******************************/
			// 新增兑换券，需要增加兑换券-商品规则唯一性判断
			// 判断：商户、商品在【启用】、【暂停】状态下是否存在数据，存在提示该商品存在已存在的兑换券规则，不能添加
			couponRule.setState(Const.COUPON_RULESTATE_PAUSE); // 设置启用状态为暂停
			if (couponRuleDao.findCouponRuleCount(couponRule) > 0) {
				throw new BusinessException("该商品已存在【暂停】状态的兑换券规则");
			}
			couponRule.setState(Const.COUPON_RULESTATE_ENABLE); // 设置启用状态为启用
			if (couponRuleDao.findCouponRuleCount(couponRule) > 0) {
				throw new BusinessException("该商品已存在【启用】状态的兑换券规则");
			}
			/** end 效验 ******************************/
			// 新增入库
			couponRuleDao.insertCouponRule(couponRule);
			log.info("新增兑换券规则表成功，数据" + couponRule.toString());
		} else if ("2".equals(audit.getAuditType())) {
			/** start 效验 ******************************/
			// 修改兑换券，需要惊醒兑换券规则-商品唯一性判断
			CouponRule checkCouponRule = couponRuleService.load(couponRule.getRuleId());
			if ((Const.COUPON_RULESTATE_ENABLE.equals(couponRule.getState()) || Const.COUPON_RULESTATE_PAUSE
					.equals(couponRule.getState())) && !checkCouponRule.getState().equals(couponRule.getState())) {
				// 判断：商户、商品在【启用】、【暂停】状态下是否存在数据，存在提示该商品存在已存在的兑换券规则，不能添加
				if (couponRuleDao.findCouponRuleCount(couponRule) > 0) {
					throw new BusinessException("该商品已存在【启用】或【暂停】状态的兑换券规则");
				}
			}
			/** end 效验 ******************************/

			// 修改入库
			if (couponRuleDao.updateCouponRule(couponRule) == 1) { // t_coupon_rule
				log.info("修改兑换券规则表成功，数据" + couponRule.toString());
			} else {
				log.error("修改兑换券规则表失败，数据" + couponRule.toString());
				throw new BusinessException("修改兑换券规则表失败！");
			}
		} else {
			log.error("非法的审核类型！");
			throw new BusinessException("非法的审核类型！");
		}
		if (auditDao.updateState(audit) == 1) { // t_hfaudit
			log.info("更新审核状态成功，数据" + audit.toString());
		} else {
			log.error("更新审核状态失败，数据" + audit.toString());
			throw new BusinessException("更新审核状态失败！");
		}
	}

	/**
	 * ******************************************** method name :
	 * gradeAuditNotPass modified : xuhuafeng , 2013-2-26
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#gradeAuditNotPass(com.umpay
	 *      .hfmng.model.Audit)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String gradeAuditNotPass(Audit audit) {
		String result = "no";
		MerGrade merGrade = new MerGrade();
		merGrade.setMerId(audit.getIxData());
		merGrade.setMonth(audit.getIxData2());
		merGrade.setModLock(0);
		if ("0".equals(audit.getState())) { // 待审核状态
			audit.setState("1"); // 审核状态改为不通过
			if (merGradeDao.updateModLock(merGrade) == 1) {
				log.info("修改锁成功" + merGrade.toString());
			} else {
				log.error("修改锁失败" + merGrade.toString());
				throw new DAOException("修改锁失败");
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

	/**
	 * ******************************************** method name : gradeAuditPass
	 * modified : xuhuafeng , 2013-2-26
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#gradeAuditPass(com.umpay.
	 *      hfmng.model.Audit)
	 * ********************************************/
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String gradeAuditPass(Audit audit) {
		String result = "no"; // 返回结果初始化为no
		List<MerGrade> merGrades = (List<MerGrade>) JsonHFUtil
				.getListFromJsonArrStr(audit.getModData(), MerGrade.class);
		MerGrade merGrade = merGrades.get(1);
		merGrade.setModLock(0);
		if ("0".equals(audit.getState())) { // 待审核状态
			audit.setState("2"); // 审核状态改为通过
			if (merGradeDao.update(merGrade) == 1) {
				log.info("修改商户评分表成功" + merGrade.toString());
			} else {
				log.error("修改商户评分表失败" + merGrade.toString());
				throw new DAOException("修改商户评分表失败");
			}
			result = modAuditState(audit, merGrade);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private String modAuditState(Audit audit, MerGrade merGrade) {
		String result = "no"; // 返回结果初始化为no
		if (auditDao.updateState(audit) == 1) { // t_hfaudit
			MerGrade grade = merGradeService.load(merGrade.getMerId(), merGrade.getMonth());
			grade.trim();
			merGradeService.operateLog(grade); // 修改操作入日志表
			log.info("修改审核状态成功" + audit.toString());
		} else {
			log.error("修改审核状态失败" + audit.toString());
			throw new DAOException("修改审核状态失败");
		}
		result = "yes";
		return result;
	}

	/**
	 * ******************************************** method name :
	 * chnlBankAuditNotPass modified : xuhuafeng , 2013-3-20
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#chnlBankAuditNotPass(com.
	 *      umpay.hfmng.model.Audit)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String chnlBankAuditNotPass(String[] id, String userId, String resultDesc) {
		String result = "yes";
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = checkAuditData(id, auditList);
		if (!"SUCCESS".equals(checkResult)) {
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("1"); // 审核状态改为不通过
			audit.setResultDesc(resultDesc);
			ChnlBank chnlBank = (ChnlBank) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), ChnlBank.class);
			chnlBank.setModLock(0); // 修改锁定状态为未锁定
			if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
				if (chnlBankDao.updateModLock(chnlBank) == 1) {
					log.info("修改锁成功" + chnlBank.toString());
				} else {
					log.error("修改锁失败" + chnlBank.toString());
					throw new DAOException("修改锁失败");
				}
			}
			if (auditDao.updateState(audit) == 1) { // t_hfaudit
				log.info("修改审核状态成功" + audit.toString());
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name :
	 * chnlBankAuditPass modified : xuhuafeng , 2013-3-20
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#chnlBankAuditPass(com.umpay
	 *      .hfmng.model.Audit)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String chnlBankAuditPass(String[] id, String userId, String resultDesc) {
		String result = "yes"; // 返回结果初始化为no
		List<Audit> auditList = new ArrayList<Audit>();
		String checkResult = checkAuditData(id, auditList);
		if (!"SUCCESS".equals(checkResult)) {
			return checkResult;
		}
		for (int i = 0; i < auditList.size(); i++) {
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("2"); // 审核状态改为通过
			audit.setResultDesc(resultDesc);
			ChnlBank chnlBank = (ChnlBank) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(), ChnlBank.class);
			chnlBank.setModLock(0); // 修改锁定状态为未锁定
			if ("1".equals(audit.getAuditType())) { // 新增入库
				chnlBankDao.insert(chnlBank);
				log.info("新增入渠道银行表成功" + chnlBank.toString());
			} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
				if (chnlBankDao.update(chnlBank) == 1) {
					log.info("启用/禁用渠道银行成功" + chnlBank.toString());
				} else {
					log.error("启用/禁用渠道银行失败" + chnlBank.toString());
					throw new DAOException("启用/禁用渠道银行失败");
				}
			} else {
				log.error("非法的审核类型");
				throw new DAOException("非法的审核类型");
			}
			if (auditDao.updateState(audit) == 1) {
				log.info("修改审核状态成功" + audit.toString());
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		}
		return result;
	}

	/**
	 * ******************************************** method name : checkDataAdd
	 * modified : xuhuafeng , 2013-10-8
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#checkDataAdd(java.util.Map)
	 * ********************************************/
	public String checkDataAdd(Map<String, String> map) {
		int res = auditDao.checkDataAdd(map);
		return res == 0 ? "1" : "0"; // 1为无主键冲突，可添加
	}

	/**
	 * ******************************************** method name : checkAuditUser
	 * modified : xuhuafeng , 2014-1-9
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.AuditService#checkAuditUser(java.lang.
	 *      String, java.lang.String)
	 * ********************************************/
	public boolean checkAuditUser(String creator) {
		creator = StringUtils.trim(creator);
		User user = LoginUtil.getUser();
		if (user.getLoginName().equals("admin")) {
			return true;
		}
		if (user.getId().equals(creator)) {
			return false;
		}
		return true;
	}

	public String checkAuditData(String[] id, List<Audit> auditList) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < id.length; i++) {
			map.put("id", id[i]);
			Audit audit = auditDao.get(map);
			if (audit == null) {
				log.error("不存在的审核数据id:" + id[i]);
				throw new DAOException("不存在的审核数据");
			}
			audit.trim(); // 去空格
			if (!"0".equals(audit.getState())) {
				log.error("该数据已审核，不能再次审核" + audit);
				return "部分数据已审核，不能再次审核，请刷新后再试";
			}
			if (!checkAuditUser(audit.getCreator())) {
				return "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。";
			}
			auditList.add(audit);
		}
		return "SUCCESS";
	}

	/**
	 * @Title: checkAuditData1
	 * @Description: 审核数据验证（忽略不能自己审核验证）
	 * @param id
	 * @param auditList
	 * @return String
	 * @author wanyong
	 * @date 2014-4-25 上午9:55:37
	 */
	public String checkAuditData1(String[] id, List<Audit> auditList) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < id.length; i++) {
			map.put("id", id[i]);
			Audit audit = auditDao.get(map);
			if (audit == null) {
				log.error("不存在的审核数据id:" + id[i]);
				throw new DAOException("不存在的审核数据");
			}
			audit.trim(); // 去空格
			if (!"0".equals(audit.getState())) {
				log.error("该数据已审核，不能再次审核" + audit);
				return "部分数据已审核，不能再次审核，请刷新后再试";
			}
			auditList.add(audit);
		}
		return "SUCCESS";
	}

	/**
	 * 查询审核表的最近一条记录
	 * 
	 * @Title: getOneObj
	 * @Description: TODO
	 * @param mapWhere
	 * @return
	 * @return Audit
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-19 下午06:25:08
	 */
	public Audit getOneObj(Map<String, String> mapWhere) {
		return auditDao.getOneObj(mapWhere);
	}
}
