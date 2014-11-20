package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.MerBank;

public interface BankDao extends EntityDao<BankInfo> {
	public BankInfo get(String bankId) throws DataAccessException;

	public BankInfo get(Map<String, String> mapWhere) throws DataAccessException;

	/**
	 * ******************************************** method name :
	 * getCheckFromMers description : 支付服务商编号唯一性验证
	 * 
	 * @return : Object
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-9-26
	 *        下午01:49:42
	 * @see : *******************************************
	 */
	public Object getCheckFromBank(Map<String, String> mapWhere) throws DataAccessException;

	/**
	 * ******************************************** method name : save
	 * description : 保存方法
	 * 
	 * @return : void
	 * @param : @param bank
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-9-28
	 *        下午02:27:56
	 * @see : *******************************************
	 */
	public void save(BankInfo bank) throws DataAccessException;

	/**
	 * ******************************************** method name : update
	 * description :更新方法 modified : anshuqiang , 2012-9-28
	 * 
	 * @see : @see com.umpay.hfmng.base.EntityDao#update(java.lang.Object)
	 *      *******************************************
	 */
	public int update(BankInfo bank) throws DataAccessException;

	public List<BankInfo> getBankInfos();

	/**
	 * ******************************************** method name :
	 * getBankInfosBymerId description : 根据商户号查所属的小额银行信息
	 * 
	 * @return : List<BankInfo>
	 * @param : @return modified : zhaojunbao , 2012-10-19 下午04:58:53
	 * @see : *******************************************
	 */
	public List<MerBank> getBankInfosBymerId(Map mapwhere);

	public List<GoodsBank> getBankInfosByGoodsId(Map mapwhere);

	/**
	 * @Title: findXEBankInfos
	 * @Description: 获取小额支付银行信息s
	 * @return
	 * @author wanyong
	 * @date 2014-7-21 下午5:14:20
	 */
	public List<BankInfo> findXEBankInfos();
	
	/**
	 * @Title: findXEBankInfos
	 * @Description: 获取全网支付银行信息
	 * @return
	 * @author wangyuxin
	 * @date 2014-8-15 下午5:13:31
	 */
	public List<BankInfo> findQWBankInfos();
}
