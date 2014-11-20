package com.umpay.hfmng.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.dao.BankDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.service.BankService;
import com.umpay.sso.org.User;


@Service
public class BankServiceImpl implements BankService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private BankDao bankDao;
	
	public BankInfo load(Map<String, String> mapWhere)throws DataAccessException {
		BankInfo bank = (BankInfo) bankDao.get(mapWhere);
		if(bank != null){
			log.info("获取到支付服务商数据：" + bank.toString());
		}
        return bank;
    }
	public BankInfo load(String bankId) throws DataAccessException {
		BankInfo bank = (BankInfo) bankDao.get(bankId);
		if(bank != null){
			log.info("获取到支付服务商数据：" + bank.toString());
		}
		return bank;
	}
/**
 * ********************************************
 * method name   : saveBank 
 * description   : 
 * @return       : String
 * @param        : @param bank
 * @param        : @return
 * @param        : @throws Exception
 * modified      : zhaojunbao ,  2012-11-21  下午08:38:15
 * @see          : 
 * *******************************************
 */
	public String saveBank(BankInfo bank) throws Exception {
		String result="0";                      //操作结果 1表示成功, 0 表示失败
		try {
			bankDao.save(bank);
			log.info("支付银行保存成功"+bank.toString());
			refrashCache(bank);
			result="1";
		} catch (Exception e) {
			log.error("支付银行保存失败"+bank.toString(),e);
			throw new DAOException("保存失败！");	
		}
		return result; // 返回成功
	}
	/**
	 * ********************************************
	 * method name   : modifyBankInfo 
	 * description   : 修改支付银行
	 * @return       : String
	 * @param        : @param bank
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-11-21  下午09:20:26
	 * @see          : 
	 * *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String modifyBankInfo(BankInfo bank) {
		int res=bankDao.update(bank);
		if(res==1){
			refrashCache(bank);
			log.info("修改支付银行成功"+bank.toString());
			return "1"; // 返回成功
		}else{
			log.error("修改支付银行失败"+bank.toString());
			throw new DAOException("修改支付银行失败");
		}
	}

	/**
	 * ********************************************
	 * method name   : enableAndDisable 
	 * description   :启用和禁用方法
	 * modified      : anshuqiang ,  2012-9-28
	 * @see          : @see com.umpay.hfmng.service.BankService#enableAndDisable(java.lang.String[], com.umpay.sso.org.User, java.lang.String)
	 * *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String[] array, User user,String action) throws Exception{
		    String result="0";
		    Map<String, Object> bankCache = HfCacheUtil.getCache().getBankInfoMap();
			for (int i = 0; i < array.length; i++) {
		        BankInfo bank=new BankInfo();
				bank.setBankId(array[i].trim());
				bank.setState(action);                // 目标设置状态 2为启用 ,4为禁用
				int res=bankDao.update(bank);
				if (res == 1) {
					BankInfo bankInfo = (BankInfo) bankCache.get(bank.getBankId());
					bankInfo.setState(action);
					bankCache.put(bank.getBankId(), bankInfo);
					log.info("刷新支付服务商缓存成功");
					log.info("启用禁用成功！"+bank.toString());
					result = "1";
				} else {
					log.error("启用禁用失败"+bank.toString());
					throw new DAOException("启用禁用失败");
				}
			  }	
			return result;		
	}
	/**
	 * ********************************************
	 * method name   : getBankInfos 
	 * modified      : zhaojunbao ,  2012-10-11
	 * @see          : @see com.umpay.hfmng.service.BankService#getBankInfos()
	 * *******************************************
	 */
	public List<BankInfo> getBankInfos() {
		return bankDao.getBankInfos();
	}
	
	/** ********************************************
	 * method name   : getBankInfosByMerId 
	 * modified      : zhaojunbao ,  2012-10-19
	 * @see          : @see com.umpay.hfmng.service.BankService#getBankInfosByMerId(java.util.Map)
	 * ********************************************/     
	public List<MerBank> getBankInfosByMerId(Map mapwhere) {
		return bankDao.getBankInfosBymerId(mapwhere);
	}
	
	/** ********************************************
	 * method name   : getBankInfosByGoodsId 
	 * modified      : zhaojunbao ,  2012-10-22
	 * @see          : @see com.umpay.hfmng.service.BankService#getBankInfosByGoodsId(java.util.Map)
	 * ********************************************/     
	public List<GoodsBank> getBankInfosByGoodsId(Map mapwhere) {
		return (List<GoodsBank>) bankDao.getBankInfosByGoodsId(mapwhere);
	}
	/**
	 * ********************************************
	 * method name   : refrashCatch 
	 * description   : 刷新支付服务商缓存
	 * @return       : void
	 * @param        : @param bank
	 * modified      : xuhuafeng ,  2013-4-25  下午04:54:35
	 * *******************************************
	 */
	private void refrashCache(BankInfo bank){
		HfCache cache = HfCacheUtil.getCache();
		cache.getBankInfoMap().remove(bank.getBankId().trim());
		cache.getBankInfoMap().put(bank.getBankId().trim(), bank);
		log.info("刷新支付服务商缓存成功");
	}
}
