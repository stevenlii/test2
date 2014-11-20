/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.ProxyGoodsInfDao;
import com.umpay.hfmng.dao.ProxyGoodsLimitDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ProxyGoods;
import com.umpay.hfmng.model.ProxyGoodsLimit;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ProxyGoodsService;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class ProxyGoodsServiceImpl implements ProxyGoodsService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProxyGoodsInfDao proxyGoodsInfDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private ProxyGoodsLimitDao proxyGoodsLimitDao;
	@Autowired
	private AuditService auditService;

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-10-11
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#load(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/
	public ProxyGoods load(String merId, String subMerId, String goodsId) {
		ProxyGoods goods = proxyGoodsInfDao.get(merId, subMerId, goodsId);
		if(goods != null){
			goods.trim();
		}
		return goods;
	}
	
	/** ********************************************
	 * method name   : findByKey 
	 * modified      : xuhuafeng ,  2013-10-12
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#findByKey(com.umpay.hfmng.model.ProxyGoods)
	 * ********************************************/     
	public List<ProxyGoods> findByKey(String merId, String subMerId) {
		ProxyGoods goods = new ProxyGoods();
		goods.setMerId(merId);
		goods.setSubMerId(subMerId);
		return proxyGoodsInfDao.findBy(goods);
	}

	/** ********************************************
	 * method name   : getAuditMap 
	 * modified      : xuhuafeng ,  2013-10-12
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#getAuditMap(java.lang.String, java.lang.String)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public Map<String, Audit> getAuditMap(String merId, String subMerId) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", merId + "-" + subMerId + "-");
		mapWhere.put("tableName", "UMPAY.T_PROXYGOODS");
		List<Audit> result = auditDao.getMerBankList(mapWhere);
		Map<String, Audit> audit = new HashMap<String, Audit>();
		for (Audit a : result) {
			a.trim();
			audit.put(a.getIxData().split("-")[2].trim(), a);
		}
		return audit;
	}
	
	/** ********************************************
	 * method name   : batchSave 
	 * modified      : xuhuafeng ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#batchSave(com.umpay.hfmng.model.ProxyGoodsLimit)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String batchSave(ProxyGoodsLimit proxyGoodsLimit) throws Exception {
		String result="1";                      //操作结果 1表示成功, 0 表示失败
		proxyGoodsLimit.trim();
		String merId = proxyGoodsLimit.getMerId();
		String subMerId = proxyGoodsLimit.getSubMerId();
		Map<String, Audit> auditMap = getAuditMap(merId, subMerId);
		List<ProxyGoods> ProxyGoodsList = findByKey(merId, null);
		Map<String, ProxyGoods> ProxyGoodsMap = new HashMap<String, ProxyGoods>();
		for(ProxyGoods g : ProxyGoodsList){
			g.trim();
			ProxyGoodsMap.put(g.getGoodsId(), g);
		}
		String[] goodsId = proxyGoodsLimit.getGoodsId().split(",");
		for(int i=0;i<goodsId.length;i++){
			if("goods".equals(goodsId[i])){
				continue;
			}
			Audit a = auditMap.get(goodsId[i]);
			if(a != null){
				return "代理商品"+goodsId[i]+"正在审核，不能重复提交";
			}
			ProxyGoods goods = ProxyGoodsMap.get(goodsId[i]);
			if(goods != null && subMerId.equals(goods.getSubMerId())){
				return "代理商品"+goodsId[i]+"已存在，不能再次添加";
			}
		}
		Timestamp nowTime=new Timestamp(System.currentTimeMillis());
		//生成批次id
		String batchId= TimeUtil.date8().substring(2, 8) 
			+ SequenceUtil.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
		Audit audit = new Audit();
		audit.setInTime(nowTime);
		audit.setModTime(nowTime);
		audit.setBatchId(batchId);
		audit.setTableName("UMPAY.T_PROXYGOODS");
		audit.setAuditType("1");                 // 审核类型 1：新增
		audit.setCreator(proxyGoodsLimit.getModUser());    //创建人 是当前登录用户
		for(int i=0;i<goodsId.length;i++){
			if("goods".equals(goodsId[i])){
				continue;
			}
			proxyGoodsLimit.setGoodsId(goodsId[i]);
			String jsonString = JsonHFUtil.getJsonArrStrFrom(proxyGoodsLimit);
			audit.setModData(jsonString);
			audit.setIxData(merId+"-"+subMerId+"-"+goodsId[i]);
			
			auditDao.insert(audit);
			log.info("新增审核信息成功"+audit);
		}
		log.info("批量新增成功");
		return result;
	}

	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#save(com.umpay.hfmng.model.ProxyGoodsLimit)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String save(ProxyGoodsLimit proxyGoodsLimit) throws Exception {
		String result="1";                      //操作结果 1表示成功, 0 表示失败
		proxyGoodsLimit.trim();
		Map<String, String> mapWhere = new HashMap<String, String>();
		String ixData = proxyGoodsLimit.getMerId()+"-"+proxyGoodsLimit.getSubMerId()+"-"+proxyGoodsLimit.getGoodsId();
		mapWhere.put("ixData", ixData);
		mapWhere.put("tableName", "UMPAY.T_PROXYGOODS");
		if(auditDao.checkDataAdd(mapWhere) != 0){
			result = "此代理商品已存在或正在审核中";
			log.info(result);
			return result;
		}
		
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_PROXYGOODS");
		audit.setAuditType("1");                 // 审核类型 1：新增
		String jsonString = JsonHFUtil.getJsonArrStrFrom(proxyGoodsLimit);
		audit.setModData(jsonString);
		audit.setCreator(proxyGoodsLimit.getModUser());    //创建人 是当前登录用户
		audit.setIxData(ixData);
		
		auditDao.insert(audit);
		log.info("新增审核信息成功"+audit);
		return result;
	}

	
	/** ********************************************
	 * method name   : auditNotPass 
	 * modified      : zhaojunbao ,  2013-10-14
	 * desc          : 代理商品审核不通过方法 
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#auditNotPass(java.lang.String[], java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditNotPass(String[] ids, String resultDesc)
			throws Exception {
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
			String key = audit.getIxData();
			if ("2".equals(audit.getAuditType()) || "3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) {
				if (proxyGoodsInfDao.setLock(key, 0) == 1 ) {
					log.info("修改锁成功,merId-subMerId-goodsId =" + key);
				} else {
					log.error("修改锁失败,merId-subMerId-goodsId =" + key);
					throw new DAOException("修改锁失败");
				}
				
			}
			
			if (auditDao.updateState(audit) == 1) {
				log.info("修改审核状态成功" + audit.toString());
			} else {
				log.error("修改审核状态失败" + audit.toString());
				throw new DAOException("修改审核状态失败");
			}
		}
		return res;
	}

	
	/** ********************************************
	 * method name   : auditPass 
	 * modified      : zhaojunbao ,  2013-10-14
	 * desc          : 代理商品审核通过方法 
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#auditPass(java.lang.String[], java.lang.String)
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
		for(int i=0;i<ids.length;i++){
			Audit audit = auditList.get(i);
			audit.setModUser(userId);
			audit.setState("2"); //审核状态改为通过
			audit.setResultDesc(resultDesc);
			
			ProxyGoodsLimit  proxyGoodsLimit = (ProxyGoodsLimit) JsonHFUtil.getObjFromJsonArrStr(audit
					.getModData(), ProxyGoodsLimit.class);
			ProxyGoods proxyGoods=new ProxyGoods();
			proxyGoods.setMerId(proxyGoodsLimit.getMerId());
			proxyGoods.setSubMerId(proxyGoodsLimit.getSubMerId());
			proxyGoods.setGoodsId(proxyGoodsLimit.getGoodsId());
			proxyGoods.setState(proxyGoodsLimit.getState()); 
			proxyGoods.setModLock(0); // 修改锁定状态为未锁定
			proxyGoods.setModUser(proxyGoodsLimit.getModUser());
			//新增或启用时需判断商品是否已被关联
			if("1".equals(audit.getAuditType()) || "3".equals(audit.getAuditType())){
				String checkGoodsRes=checkGoodsEnable(proxyGoodsLimit.getMerId(),proxyGoodsLimit.getGoodsId());
				if(checkGoodsRes!=null)
					return checkGoodsRes;
				//校验该二级商户号是否被其他商户号代理并启用
				String checkSubMerRes=checkSubMerEnable(proxyGoodsLimit.getMerId(),proxyGoodsLimit.getSubMerId());
				if(checkSubMerRes!=null)
					return checkSubMerRes;
				
			}
			
		    ProxyGoodsLimit proxyGoodsLimitNew=new ProxyGoodsLimit();
		    proxyGoodsLimitNew.setMerId(proxyGoodsLimit.getMerId());
		    proxyGoodsLimitNew.setSubMerId(proxyGoodsLimit.getSubMerId());
		    proxyGoodsLimitNew.setGoodsId(proxyGoodsLimit.getGoodsId());
		    proxyGoodsLimitNew.setState(proxyGoodsLimit.getState());
		    proxyGoodsLimitNew.setModUser(proxyGoodsLimit.getModUser());
		    proxyGoodsLimitNew.setAlarmLimit(proxyGoodsLimit.getAlarmLimit());
		    proxyGoodsLimitNew.setAlarmTel(proxyGoodsLimit.getAlarmTel());
		    proxyGoodsLimitNew.setDayLimit(proxyGoodsLimit.getDayLimit());
			
			if ("1".equals(audit.getAuditType())) { // 新增入库
				proxyGoodsInfDao.insert(proxyGoods);
			    proxyGoodsLimitDao.insert(proxyGoodsLimitNew);
				log.info("新增代理商品信息成功" + proxyGoods.toString());
				log.info("新增商品销售控制配置表成功" + proxyGoodsLimitNew.toString());
			} else if ("2".equals(audit.getAuditType())) { // 修改入库
				if(proxyGoodsInfDao.update(proxyGoods) == 1 && proxyGoodsLimitDao.update(proxyGoodsLimitNew)==1){
					log.info("修改代理商品信息表成功" +proxyGoods.toString());
					log.info("商品销售控制配置表成功" + proxyGoodsLimitNew.toString());
				} else {
					log.error("修改代理商品信息表失败" +proxyGoods.toString());
					log.error("修改商品销售控制配置表失败" + proxyGoodsLimitNew.toString());
					throw new DAOException("修改代理商品信息表失败");
				}
			
			} else if ("3".equals(audit.getAuditType()) || "4".equals(audit.getAuditType())) { // 启用/禁用入库
				if(proxyGoodsInfDao.isOrNotAble(proxyGoods) == 1 && proxyGoodsLimitDao.isOrNotAble(proxyGoodsLimitNew)==1){
					log.info("启用/禁用代理商品信息成功" +proxyGoods.toString());
					log.info("启用/禁用商品销售控制配置成功" + proxyGoodsLimitNew.toString());
				} else {
					log.error("启用/禁用代理商品信息失败" +proxyGoods.toString());
					log.error("启用/禁用商品销售控制配置失败" + proxyGoodsLimitNew.toString());
					throw new DAOException("启用、禁用失败");
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
		}
		return res;
	}

	
	/** ********************************************
	 * method name   : checkKey 
	 * desc          ： 主键检查   
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#checkKey(java.lang.String)
	 * ********************************************/     
	public String checkKey(String subMerId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/** ********************************************
	 * method name   : loadGoodsLimit 
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#loadGoodsLimit(java.lang.String, java.lang.String)
	 * ********************************************/     
	public ProxyGoodsLimit loadGoodsLimit(String merId, String subMerId, String goodsId) {
		ProxyGoodsLimit goods = proxyGoodsLimitDao.loadProxGoodsLimit(merId, subMerId, goodsId);
		if(goods != null){
			goods.trim();
		}
		return goods;
	}
	
	/** ********************************************
	 * method name   : enableAndDisable 
	 * modified      : xuhuafeng ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#enableAndDisable(java.lang.String[], int)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String[] ids, int action) throws Exception {
		String res = "1";
		String auditType = "";
		if(action == 2){
			auditType = "3";
		}else if(action == 4){
			auditType = "4";
		}else{
			return "0";
		}
		List<ProxyGoods> goodsList = new ArrayList<ProxyGoods>();
		for (int i = 0; i < ids.length; i++) {
			String[] id = ids[i].split("-");
			if(id.length != 3){
				return "0";
			}
			ProxyGoods g = load(id[0], id[1], id[2]);
			if(g.getModLock() == 1){
				res = "操作失败，商品["+ids[i]+"]正在审核中";
				log.error(res);
				return res;
			}
			goodsList.add(g);
		}
		String userId = LoginUtil.getUser().getId();
		for (int i = 0; i < goodsList.size(); i++) {
			ProxyGoods goods = goodsList.get(i);
			goods.setState(action);
			goods.setModUser(userId);
			goods.setInTime(null);
			goods.setModTime(null); // 入库时间和修改时间 都置为空,防止JSON序列化时出错
			String jsonString = JsonHFUtil.getJsonArrStrFrom(goods);
			Audit audit = new Audit();
			audit.setTableName("UMPAY.T_PROXYGOODS");
			audit.setModData(jsonString);
			audit.setAuditType(auditType);      // 审核类型 1：新增;2：修改;3：启用;4：禁用
			audit.setCreator(userId); // 创建人是当前登录用户
			audit.setIxData(ids[i]);
			
			auditDao.insert(audit); // 入审核表	
			log.info("代理商品信息入审核表成功"+audit);
		    int lock = proxyGoodsInfDao.setLock(ids[i], 1); // 更新锁状态
		    if(lock==1){
				log.info("代理商品状态锁修改成功"+goods);
			}else{
				log.error("代理商品信息修改锁失败："+goods);
				throw new DAOException("代理商品信息修改锁失败");
			}
		}
		log.info("启用禁用代理商品操作成功");
		return res;
	}
	
	/** ********************************************
	 * method name   : update 
	 * modified      : xuhuafeng ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#update(com.umpay.hfmng.model.ProxyGoodsLimit)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String update(ProxyGoodsLimit proxyGoodsLimit) throws Exception {
		String res = "1";
		String merId = proxyGoodsLimit.getMerId();
		String subMerId = proxyGoodsLimit.getSubMerId();
		String goodsId = proxyGoodsLimit.getGoodsId();
		ProxyGoods goods = load( merId, subMerId, goodsId);
		if(goods == null){
			return "不存在的数据";
		}
		if(goods.getModLock() == 1){
			return "数据已锁定，不能修改";
		}
		proxyGoodsLimit.setState(goods.getState());
		String jsonString = JsonHFUtil.getJsonArrStrFrom(proxyGoodsLimit);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_PROXYGOODS");
		audit.setModData(jsonString);
		audit.setAuditType("2");      // 审核类型 1：新增;2：修改;3：启用;4：禁用
		audit.setCreator(proxyGoodsLimit.getModUser()); // 创建人是当前登录用户
		String ixData = merId+"-"+subMerId+"-"+goodsId;
		audit.setIxData(ixData);
		
		auditDao.insert(audit); // 入审核表	
		log.info("代理商品信息入审核表成功"+audit);
	    int lock = proxyGoodsInfDao.setLock(ixData, 1); // 更新锁状态
	    if(lock==1){
			log.info("代理商品状态锁修改成功"+proxyGoodsLimit);
		}else{
			log.error("代理商品信息修改锁失败："+proxyGoodsLimit);
			throw new DAOException("代理商品信息修改锁失败");
		}
	    log.info("修改代理商品操作成功");
		return res;
	}

	
	/** ********************************************
	 * method name   : auditNotPassByBatchId 
	 * modified      : zhaojunbao ,  2013-10-15
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#auditNotPassByBatchId(java.lang.String[], java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditNotPassByBatchId(String batchId, String resultDesc)
			throws Exception {
		String result = "1"; // 返回结果初始化为no
		String userId = LoginUtil.getUser().getId();
		List<Audit>  list = findByBatchId(batchId);
		for(int j=0;j<list.size();j++){
			Audit a = (Audit) list.get(j);
			if(!"0".equals(a.getState())){
				return "已审核，不能重复审核";
			}
			if(!auditService.checkAuditUser(a.getCreator())){
				return "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。";
			}
		}
		Audit audit = new Audit();
		audit.setBatchId(batchId);
		audit.setModUser(userId);
		audit.setState("1"); //审核状态改为不通过
		audit.setResultDesc(resultDesc);
		if (auditDao.batchUpdateState(audit) == list.size()) {
			log.info("修改审核状态成功" + audit.toString());
		} else {
			log.error("修改审核状态失败" + audit.toString());
			throw new DAOException("修改审核状态失败");
		}
		return result;
	}
	//检查商品是否已代理其他二级商户
	private String checkGoodsEnable(String merId,String goodsId){
		List<ProxyGoods> pgList=proxyGoodsInfDao.findEnable(merId,goodsId);
		if(pgList!=null && pgList.size()!=0){
			return "商品["+merId+"-"+goodsId+"]已代理二级商户["+pgList.get(0).getSubMerId().trim()+"]请先解除";
		}
		return null;
	}
	//检查二级商户是否已被其他商户代理
	private String checkSubMerEnable(String merId, String subMerId){
		List<ProxyGoods> pgList=findEnableBySubMer(subMerId);
		if(pgList!=null){
			for(ProxyGoods pg : pgList){
				pg.trim();
				if(!pg.getMerId().equals(merId)){
					return "二级商户["+subMerId+"]已被商户["+pg.getMerId()+"]代理";
				}
			}
		}
		return null;
	}
	
	/** ********************************************
	 * method name   : auditPassByBatchId 
	 * modified      : zhaojunbao ,  2013-10-15
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#auditPassByBatchId(java.lang.String[], java.lang.String)
	 * ********************************************/  
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditPassByBatchId(String batchId, String resultDesc)
			throws Exception {
		String result = "1"; 
		String userId = LoginUtil.getUser().getId();
		List<Audit> list = findByBatchId(batchId);
		//先全部校验，并检查商品是否被关联
		for (Audit a : list) {
			if(!"0".equals(a.getState())){
				return "部分数据已审核，不能再次审核，请刷新后再试";
			}
			if(!auditService.checkAuditUser(a.getCreator())){
				return "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。";
			}
			
			ProxyGoodsLimit proxyGoodsLimit = (ProxyGoodsLimit) JsonHFUtil
			.getObjFromJsonArrStr(a.getModData(), ProxyGoodsLimit.class);
			String checkRes=checkGoodsEnable(proxyGoodsLimit.getMerId(),proxyGoodsLimit.getGoodsId());
			if(checkRes!=null)
				return checkRes;
			//校验该二级商户号是否被其他商户号代理并启用
			String checkSubMerRes=checkSubMerEnable(proxyGoodsLimit.getMerId(),proxyGoodsLimit.getSubMerId());
			if(checkSubMerRes!=null)
				return checkSubMerRes;
		}
		//入库
		for (Audit a : list) {
			ProxyGoodsLimit proxyGoodsLimit = (ProxyGoodsLimit) JsonHFUtil
			.getObjFromJsonArrStr(a.getModData(), ProxyGoodsLimit.class);
			ProxyGoods proxyGoods=new ProxyGoods();
			proxyGoods.setMerId(proxyGoodsLimit.getMerId());
			proxyGoods.setSubMerId(proxyGoodsLimit.getSubMerId());
			proxyGoods.setGoodsId(proxyGoodsLimit.getGoodsId());
			proxyGoods.setModLock(0); // 修改锁定状态为未锁定
			proxyGoods.setModUser(proxyGoodsLimit.getModUser());

			ProxyGoodsLimit proxyGoodsLimitNew=new ProxyGoodsLimit();
			proxyGoodsLimitNew.setMerId(proxyGoodsLimit.getMerId());
			proxyGoodsLimitNew.setSubMerId(proxyGoodsLimit.getSubMerId());
			proxyGoodsLimitNew.setGoodsId(proxyGoodsLimit.getGoodsId());
			proxyGoodsLimitNew.setModUser(proxyGoodsLimit.getModUser());
			proxyGoodsLimitNew.setAlarmLimit(proxyGoodsLimit.getAlarmLimit());
			proxyGoodsLimitNew.setAlarmTel(proxyGoodsLimit.getAlarmTel());
			proxyGoodsLimitNew.setDayLimit(proxyGoodsLimit.getDayLimit());
			
			if ("1".equals(a.getAuditType())) { // 新增入库
				proxyGoodsInfDao.insert(proxyGoods);
				proxyGoodsLimitDao.insert(proxyGoodsLimitNew);
				log.info("新增代理商品信息成功" + proxyGoods.toString());
				log.info("新增商品销售控制配置表成功" + proxyGoodsLimitNew.toString());
			} else {
				log.error("非法的审核类型");
				throw new DAOException("非法的审核类型");
			}
		}
		Audit audit = new Audit();
		audit.setBatchId(batchId);
		audit.setModUser(userId);
		audit.setState("2"); //审核状态改为通过
		audit.setResultDesc(resultDesc);
		if (auditDao.batchUpdateState(audit) == list.size()) {
			log.info("修改审核状态成功" + audit);
		} else {
			log.error("修改审核状态失败" + audit);
			throw new DAOException("修改审核状态失败");
		}
		return result;
	}

	public List<Audit> findByBatchId(String batchId) {
		List<Audit> list = auditDao.findByBatchId(batchId);
		return list;
	}

	
	/** ********************************************
	 * method name   : findEnableBySubMer 
	 * modified      : xuhuafeng ,  2013-10-17
	 * @see          : @see com.umpay.hfmng.service.ProxyGoodsService#findEnableBySubMer(java.lang.String)
	 * ********************************************/     
	public List<ProxyGoods> findEnableBySubMer(String subMerId) {
		return proxyGoodsInfDao.findEnableBySubMer(subMerId);
	}
}
