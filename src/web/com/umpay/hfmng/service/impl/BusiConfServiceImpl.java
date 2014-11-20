/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConfServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-15
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.BusiConfDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.MerBusiConf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.BusiConfService;
import com.umpay.hfmng.service.OptionService;


/** ******************  类说明  *********************
 * class       :  BusiConfServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class BusiConfServiceImpl implements BusiConfService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditService auditService;
	@Autowired
	private BusiConfDao busiConfDao;
	@Autowired
	private OptionService optionService;
	
	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2014-1-15
	 * @see          : @see com.umpay.hfmng.service.BusiConfService#load(java.lang.String, java.lang.String)
	 * ********************************************/
	public MerBusiConf load(String merId, String bizType) {
		MerBusiConf mbc=new MerBusiConf();
		mbc.setMerId(merId);
		mbc.setBizType(bizType);
		MerBusiConf m = busiConfDao.get(mbc);
		if(m != null){
			m.trim();
		}
		return m;
	}

	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2014-1-15
	 * @see          : @see com.umpay.hfmng.service.BusiConfService#save(com.umpay.hfmng.model.MerBusiConf)
	 * ********************************************/
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String save(MerBusiConf merBusiConf) throws Exception {
		String result="0";
		String[] bizTypeArr=merBusiConf.getBizType().split(",");
		String merId=merBusiConf.getMerId();
		//本次新增的商户支付类型如果正在审核中，则本次新增失败
		List<Audit> list=busiConfDao.findAuditingBizType(merId);
		for(String bizType : bizTypeArr){
			for(Audit au : list){
				if(bizType.equals(au.getIxData2())){
					//商户支付类型正在审核中
					Map<String, String> bizTypeMap = optionService.getMerBizTypeMap();
					log.info("商户支付类型【"+merId+","+bizType+"】正在审核中");
					return "商户支付类型【"+merId+","+bizTypeMap.get(bizType)+"】正在审核中";
				}
			}
		}
		//本次新增的商户支付类型如果已存在，则跳过
		List<MerBusiConf> mbcList=getListByMerId(merBusiConf.getMerId());
		Set<String> set=new HashSet<String>();
		for(MerBusiConf mbc:mbcList){
			set.add(mbc.getBizType());
		}
		boolean hasInsert=false;
		for(String bizType : bizTypeArr){
			if(!set.contains(bizType)){
				MerBusiConf mbc=new MerBusiConf();
				mbc.setMerId(merId);
				mbc.setBizType(bizType);
				mbc.setCreator(LoginUtil.getUser().getId());
				mbc.setModUser(LoginUtil.getUser().getId());
				Audit audit = new Audit();
				audit.setTableName("UMPAY.T_MERBUSI_CONF");
				audit.setAuditType("1");// 审核类型 1：新增；2：修改；3：启用；4：禁用
				audit.setState("0");
				audit.setIxData(merId);
				audit.setIxData2(bizType);
				audit.setCreator(LoginUtil.getUser().getId());
				String jsonString = JsonHFUtil.getJsonArrStrFrom(mbc);
				audit.setModData(jsonString);
				auditDao.insert(audit);
				log.info("新增审核信息成功"+audit.toString());
				hasInsert=true;
			}
		}
		
		if(hasInsert){
			result="1";
		}else{
			result="请选择新的支付类型";
			log.info("未增加新的支付类型："+merBusiConf.toString());
		}
		return result;
	}

	public List<MerBusiConf> getListByMerId(String merId) {
		MerBusiConf mbc=new MerBusiConf();
		mbc.setMerId(merId);
		return busiConfDao.findBy(mbc);
	}

	/** ********************************************
	 * method name   : auditNotPass 
	 * modified      : xuhuafeng ,  2014-1-15
	 * @see          : @see com.umpay.hfmng.service.BusiConfService#auditNotPass(java.lang.String, java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditNotPass(String id, String resultDesc) throws Exception {
		String res = "1"; // 返回结果初始化为成功
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		Audit audit = auditDao.get(map);
		String checkResult = checkAuditData(audit);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		audit.setModUser(LoginUtil.getUser().getId());
		audit.setResultDesc(resultDesc);
		audit.setState("1");
		if(auditDao.updateState(audit) == 1){
			log.info("更新审核表成功");
		}else{
			throw new DAOException("更新审核表失败");
		}
		if("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())){
			MerBusiConf mer = new MerBusiConf();
			mer.setMerId(audit.getIxData());
			mer.setBizType(audit.getIxData2());
			mer.setModLock(0);
			busiConfDao.updateLock(mer);
			log.info("修改锁成功");
		}
		log.info("审核不通过成功");
		return res;
	}
	
	/** ********************************************
	 * method name   : auditPass 
	 * modified      : xuhuafeng ,  2014-1-15
	 * @see          : @see com.umpay.hfmng.service.BusiConfService#auditPass(java.lang.String, java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditPass(String id, String resultDesc) throws Exception {
		String res = "1"; // 返回结果初始化为成功
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		Audit audit = auditService.load(map);
		String checkResult = checkAuditData(audit);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		audit.setModUser(LoginUtil.getUser().getId());
		audit.setResultDesc(resultDesc);
		audit.setState("2");
		if(auditDao.updateState(audit) == 1){
			log.info("更新审核表成功");
		}else{
			throw new DAOException("更新审核表失败");
		}
		MerBusiConf mer = (MerBusiConf) JsonHFUtil.getObjFromJsonArrStr(audit
					.getModData(), MerBusiConf.class);
		mer.setModLock(0);
		if("1".equals(audit.getAuditType())){
			mer.setState(2);
			busiConfDao.insert(mer);
			log.info("新增业务类型成功");
		}else if("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())){
			MerBusiConf merBusiConf = load(mer.getMerId(), mer.getBizType());
			if(merBusiConf == null){
				res = "无效数据";
				log.info(res);
				return res;
			}
			if("3".equals(audit.getAuditType())){
				mer.setState(2);
			}else{
				mer.setState(4);
			}
			if(busiConfDao.update(mer) == 1){
				log.info("更新业务类型成功");
			}else{
				throw new DAOException("更新审核表失败");
			}
		}
		log.info("审核通过成功");
		return res;
	}
	
	private String checkAuditData(Audit audit){
		if(audit == null){
			log.error("不存在的审核数据");
			throw new DAOException("不存在的审核数据");
		}
		audit.trim();  //去空格
		if(!"0".equals(audit.getState())){
			log.error("该数据已审核，不能再次审核"+audit);
			return "该数据已审核，不能再次审核，请刷新后再试";
		}
		if(!auditService.checkAuditUser(audit.getCreator())){
			return "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。";
		}
		return "SUCCESS";
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String merId, String[] bizTypes, int action) throws Exception {
		String result = "1";
		List<MerBusiConf> mbcList = new ArrayList<MerBusiConf>();
		for (int i = 0; i < bizTypes.length; i++) {
			MerBusiConf merBusiConf = load(merId, bizTypes[i].trim());
			if (merBusiConf.getModLock() == 1) {
				Map<String, String> bizTypeMap = optionService.getMerBizTypeMap();
				result = "操作失败，商户支付类型【" + merId+","+bizTypeMap.get(bizTypes[i]) + "】正在审核中";
				log.error(result);
				return result;
			}
			mbcList.add(merBusiConf);
		}
		String userId = LoginUtil.getUser().getId();
		for (int i = 0; i < mbcList.size(); i++) {
			MerBusiConf mbc = mbcList.get(i);
			// 以下为初始化修改数据 包括查询参数 和要修改的目标状态
			mbc.setModTime(null); // 入库时间和修改时间 都置为空,防止JSON序列化时出错
			mbc.setInTime(null);
			mbc.setState(action); // 目标设置状态 2为启用 ,4为禁用
			mbc.setModUser(LoginUtil.getUser().getId());
			// 以下输将数据转化为audit数据对象进行保存
			String jsonString = JsonHFUtil.getJsonArrStrFrom(mbc);
			Audit audit = new Audit();
			audit.setTableName("UMPAY.T_MERBUSI_CONF");
			audit.setModData(jsonString);
			if (action == 2) {
				audit.setAuditType("3"); // 目标状态为2 启用操作,审核类型为启用（3）
			}else if (action == 4) {
				audit.setAuditType("4"); // 目标状态为4 禁用操作，审核类型为禁用（4）
			}
			audit.setCreator(userId); // 提交人
			audit.setIxData(mbc.getMerId());
			audit.setIxData2(mbc.getBizType());
			auditDao.insert(audit);
			log.info("启用/禁用商户支付类型入审核表成功" + audit);
			mbc.setModLock(1); // 更新锁状态
			int lock = busiConfDao.updateLock(mbc);
			if (lock == 1) {
				log.info("商户支付类型修改锁成功" + mbc);
			} else {
				log.error("商户支付类型修改锁失败" + mbc);
				throw new DAOException("商户支付类型修改锁失败");
			}
		}
		return result;
	}
}
