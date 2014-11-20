package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.MerBank;
import com.umpay.sso.org.User;


public interface BankService {
	public BankInfo load(String bankId) throws DataAccessException;
	public String saveBank(BankInfo bank) throws DataAccessException, Exception;
	public String  modifyBankInfo(BankInfo bank) ;
	public String enableAndDisable(String [] array,User user,String action) throws Exception;
	public BankInfo load(Map<String, String> mapWhere)throws DataAccessException;
	/**
	 * ********************************************
	 * method name   : getBankInfos 
	 * description   : 
	 * @return       : List<BankInfo>
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-11  上午10:13:40
	 * @see          : 
	 * *******************************************
	 */
	public List<BankInfo> getBankInfos();
	
	/**
	 * ********************************************
	 * method name   : getBankInfosByMerId 
	 * description   : 根据商户号获取所对应的小额支付银行
	 * @return       : List<BankInfo>
	 * @param        : @param mapwhere
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-19  下午05:09:58
	 * @see          : 
	 * *******************************************
	 */
	public List<MerBank> getBankInfosByMerId(Map mapwhere);
	public List<GoodsBank> getBankInfosByGoodsId(Map mapwhere);
}
