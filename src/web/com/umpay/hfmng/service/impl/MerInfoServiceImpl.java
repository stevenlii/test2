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
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.dao.MerInfoDao;
import com.umpay.hfmng.dao.MerOperDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.sso.org.User;

@Service
public class MerInfoServiceImpl implements MerInfoService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MerInfoDao merInfoDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private GoodsInfoDao goodsInfoDao;
	@Autowired
	private MerOperDao merOperDao;
	
	/**
	 * ********************************************
	 * method name   : load 
	 * modified      : xhf ,  2012-11-22
	 * @see          : @see com.umpay.hfmng.service.MerInfoService#load(java.lang.String)
	 * *******************************************
	 */
	public MerInfo load(String merInfoId) {
		MerInfo merInfo = (MerInfo) merInfoDao.get(merInfoId);
		return merInfo;
	}

	/**
	 * ******************************************** method name : saveMersAudit
	 * 保存到商户信息审核表方法 
	 * modified : anshuqiang , 2012-8-13
	 * com.umpay.hfmng.service.MerInfoService#saveMersAudit(com.umpay
	 * .hfmng.model.MerInfo) *******************************************
	 */
	@SuppressWarnings("finally")
	public String saveMerAudit(MerInfo merInfo) throws Exception{
		String result="1";                      //操作结果 1表示成功, 0 表示失败
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_MER_INF");
		audit.setState("0");                     // 审核状态 0：待审核；1：审核通过；2：审核不通过
		audit.setAuditType("1");                 // 审核类型 1：新增
		String jsonString = JsonHFUtil.getJsonArrStrFrom(merInfo);
		audit.setModData(jsonString);
		audit.setCreator(merInfo.getModUser());    //创建人 是当前登录用户
		audit.setModUser("");      
		audit.setIxData2(merInfo.getBusiType());
		audit.setModTime(null);
		audit.setResultDesc("");
		audit.setIxData(merInfo.getMerId());
		try {
			auditDao.insert(audit);
			log.info("新增审核信息成功"+audit.toString());
			return result; // 返回成功
		} catch (Exception e) {
			log.error("新增审核信息失败",e);
			result="0";
			throw new DAOException("新增审核信息失败！");	
		}finally{
			return result;
		}
	}
	/**
	 * ********************************************
	 * method name   : modifyMerInfo 
	 * modified      : xhf ,  2012-11-22
	 * @see          : @see com.umpay.hfmng.service.MerInfoService#modifyMerInfo(com.umpay.hfmng.model.MerInfo)
	 * *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String modifyMerInfo(MerInfo merInfo) {
		String result = "0";
		int modLock = merInfo.getModLock();
		if (modLock == 1){  //判断当前的商品锁定状态 ,如果是已经锁定 ,则提示出错
			log.error("当前商户已处于锁定状态，无法操作");
			throw new DAOException("当前商户已处于锁定状态，无法操作");		
		}
		merInfo.setModLock(1); //更新锁状态
		String jsonString = JsonHFUtil.getJsonArrStrFrom(merInfo);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_MER_INF");
		audit.setModData(jsonString);
		audit.setState("0");          // 审核状态 0：待审核；1：审核通过；2：审核不通过
		audit.setAuditType("2");      // 审核类型 1：新增:2：修改:3：启用:4：禁用 0:未知
		audit.setCreator(merInfo.getModUser()); // 创建人是当前登录用户
		audit.setIxData2(merInfo.getBusiType());
		audit.setResultDesc("");
		audit.setIxData(merInfo.getMerId());
		auditDao.insert(audit); // 入审核表	
		log.info("商户信息入审核表成功"+audit.toString());
	    int lock = merInfoDao.updateMerLock(merInfo); // 更新锁状态
	    if(lock==1){
			log.info("商户状态锁修改成功"+audit.toString());
			result="1";
		}else{
			log.error("商户信息修改锁失败："+audit.toString());
			throw new DAOException("商户信息修改锁失败");
		}
		return result; 
	}

	/**
	 * ******************************************** method name : enable
	 * modified : anshuqiang , 2012-8-31
	 * @see : @see
	 *      com.umpay.hfmng.service.MerInfoService#enable(java.lang.String,
	 *      com.umpay.sso.org.User) *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String[] array, User user,String action) {
		    String result="0";
			for (int i = 0; i < array.length; i++) {
				result="0";   //循环操作时，每次操作之前应该先将操作状态置为失败（"0"）
				MerInfo merInfo = load(array[i].trim());// 先查找要修改的内容对象
				merInfo.trim();
				if(merInfo.getModLock() == 1){
					result = "0";
					log.error("商户已锁定，无法操作！"+merInfo.toString());
					throw new DAOException("商户已锁定，无法操作！");
				}
				// 以下为初始化修改数据 包括查询参数 和要修改的目标状态
				merInfo.setModLock(1);     //更新锁状态
				merInfo.setMerId(merInfo.getMerId().trim());
			    merInfo.setModTime(null); //入库时间和修改时间 都置为空,防止JSON序列化时出错
			    merInfo.setInTime(null);	
				merInfo.setState(action);                // 目标设置状态 2为启用 ,4为禁用
				// 以下输将merInfo数据转化为audit数据对象进行保存
				String jsonString = JsonHFUtil.getJsonArrStrFrom(merInfo);
				Audit audit = new Audit();
				audit.setTableName("UMPAY.T_MER_INF");
				audit.setModData(jsonString);
				audit.setState("0");        // 审核状态 0：待审核
				 if(action.equals("2")){
					 audit.setAuditType("3") ;   // 目标状态为2  启用操作,审核类型为启用（3） 
				 }
				 if(action.equals("4")){
					 audit.setAuditType("4");   //目标状态为4 禁用操作，审核类型为禁用（4）
				 }
				audit.setCreator(user.getId());  // 提交人
				audit.setIxData2(merInfo.getBusiType());
				audit.setResultDesc("");
				audit.setIxData(merInfo.getMerId().trim());
		     	auditDao.insert(audit);
		     	log.info("启用/禁用商户入审核表成功"+audit.toString());
				int lock=merInfoDao.updateMerLock(merInfo);
				if(lock == 1){
					result = "1";
					log.info("商户修改锁成功"+merInfo.toString());
				}else{
					result = "0";
					log.error("商户修改锁失败"+merInfo.toString());
					throw new DAOException("商户修改锁失败");
				}
			  }		
			return result;		
	}

	
	/** ********************************************
	 * method name   : importMer 
	 * modified      : xuhuafeng ,  2013-1-30
	 * @see          : @see com.umpay.hfmng.service.MerInfoService#importMer(java.lang.String)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, String> importMer(String merId, String userId) {
		String result = "";
		Map<String, String> message = new HashMap<String, String>();
		List<GoodsInfo> newGoodsList = new ArrayList<GoodsInfo>();
		message.put("flag", "no");
		MerInfo oldMer = merInfoDao.loadMerInf(merId);
		if(oldMer == null){
			result = "该商户号不存在";
			message.put("result", result);
			log.error("不存在的商户号"+merId);
		}else{
			List<GoodsInfo> goodsList = goodsInfoDao.loadGoodsInf(merId);
			String importGoodsId = "";
			if(goodsList == null){
				log.info("该商户["+merId+"]下没有商品");
			}else{
				List<String> goodsExpList = goodsInfoDao.loadGoodsExp(merId);
				for(int i=0;i<goodsList.size();i++){
					GoodsInfo goods = goodsList.get(i);
					goods.trim();
					String oldGoodsId = goods.getGoodsId();
					if(goodsExpList.contains(oldGoodsId)){
						log.info("商品已存在于扩展表" + goods.toString());
						continue;
					}
					goods.setModUser(userId);
					goodsInfoDao.saveGoodsExp(goods);
					newGoodsList.add(goods);
					importGoodsId += oldGoodsId + " ";
					log.info("老商品入商品扩展表成功"+goods.toString());
				}
			}
			
			MerInfo mer = merInfoDao.loadMerExp(merId); //查询exp表是否存在老商户号
			if(mer != null){
				if("".equals(importGoodsId)){
					result = "该商户及其商品已存在,不能重复操作";
					message.put("result", result);
					log.error("商户号["+merId+"]已导入扩展表，不能再次导入");
				}else{
					message.put("flag", "yes");
					result = "该商户已存在,导入其下商品"+importGoodsId+"成功，请尽快修改商品信息并提交审核";
					message.put("result", result);
					log.info(result);
				}
			}else{
				try {
					oldMer.setModUser(userId);
					oldMer.setModTime(null);
					merInfoDao.saveMerExp(oldMer);
					log.info("老商户号导入商户扩展表成功"+oldMer.toString());
					
					HfMerOper merOper = new HfMerOper();
					merOper.setMerId(merId);
					merOper.setOperator(userId);
					merOper.setState(2);
					merOper.setCreator(userId);
					merOper.setModUser(userId);
					List<HfMerOper> merOperList = new ArrayList<HfMerOper>();
					merOperList.add(merOper);
					merOperDao.bacthInsert(merOperList);
					log.info("新增商户运营负责人成功");
					
					//更新商户、商品缓存
					Map<String, Object> merCache = HfCacheUtil.getCache().getMerInfoMap();
					Map<String, Object> goodsCache = HfCacheUtil.getCache().getGoodsInfoMap();
					merCache.put(merId, oldMer);
					for(int i=0;i<newGoodsList.size();i++){
						GoodsInfo g = newGoodsList.get(i);
						goodsCache.put(merId+"-"+g.getGoodsId(), g);
					}
					List<HfMerOper> list = HfCacheUtil.getCache().getMerOperListByMerId(merId);
					list.clear();
					list.addAll(merOperList);
					
					String mes = "老商户号导入成功";
					if("".equals(importGoodsId)){
						result = mes + ",其下无商品,请尽快修改商户信息并提交审核";
						message.put("result", result);
					}else{
						result = mes + ",导入其下商品"+importGoodsId+"成功，请尽快修改商户、商品信息并提交审核";
						message.put("result", result);
					}
					message.put("flag", "yes");
					log.info("老商户["+merId+"]导入成功");
				} catch (Exception e) {
					log.error("导入老商户失败"+oldMer.toString(), e);
					message.put("result", "发生未知错误，导入老商户失败");
					throw new DAOException("导入老商户失败");
				}
			}
		}
		return message;
	}
	public List<MerInfo> loadAll() {
		return merInfoDao.selectAll();
	}

	/*
	  * 
	  * 
	  * @param name
	  * @return
	  * @see com.umpay.hfmng.service.MerInfoService#filtrationMerByName(java.lang.String)
	  */
	
	
	public List<MerInfo> filtrationMerByName(String name) {
		return merInfoDao.filtrationMerByName(name);
	}
}
