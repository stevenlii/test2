package com.umpay.hfmng.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.BankDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.MerBank;

@Repository("bankDaoImpl")
public class BankDaoImpl extends EntityDaoImpl<BankInfo> implements BankDao {
	public BankInfo get(String bankId) throws DataAccessException {
		return (BankInfo) this.get("Bank.Get", bankId);
	}

	public BankInfo get(Map<String, String> mapWhere) throws DataAccessException {
		return (BankInfo) this.get("Bank.Get", mapWhere);
	}

	/**
	 * ******************************************** method name :
	 * getCheckFromBank description :支付服务商编号唯一性验证 modified : anshuqiang ,
	 * 2012-9-28
	 * 
	 * @see : @see com.umpay.hfmng.dao.BankDao#getCheckFromBank(java.util.Map)
	 *      *******************************************
	 */
	public Object getCheckFromBank(Map<String, String> mapWhere) throws DataAccessException {
		return this.find("Bank.checkFromBank", mapWhere);
	}

	/**
	 * ******************************************** method name : save
	 * description :保存支付服务商方法 modified : anshuqiang , 2012-9-28
	 * 
	 * @see : @see com.umpay.hfmng.dao.BankDao#save(com.umpay.hfmng.model.Bank)
	 *      *******************************************
	 */
	public void save(BankInfo bank) throws DataAccessException {
		this.save("Bank.insert", bank);
	}

	/**
	 * ******************************************** method name : update
	 * description :更新支付服务商方法 modified : anshuqiang , 2012-9-28
	 * 
	 * @see : @see com.umpay.hfmng.base.EntityDaoImpl#update(java.lang.Object)
	 *      *******************************************
	 */
	public int update(BankInfo bank) throws DataAccessException {
		return this.update("Bank.updateBank", bank);
	}

	/**
	 * ******************************************** method name : getBankInfos
	 * modified : zhaojunbao , 2012-9-26
	 * 
	 * @see : @see com.umpay.hfmng.dao.BankoDao#getBankInfos()
	 * ********************************************/
	public List<BankInfo> getBankInfos() {
		List<BankInfo> list = null;
		try {
			list = super.find("Bank.GetUPTree");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * ******************************************** method name :
	 * getBankInfosBymerId modified : zhaojunbao , 2012-10-19
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.BankDao#getBankInfosBymerId(java.util.Map)
	 * ********************************************/
	public List<MerBank> getBankInfosBymerId(Map mapwhere) {
		List<MerBank> list = null;
		try {
			list = this.find("MerBank.Get", mapwhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ******************************************** method name :
	 * getBankInfosByGoodsId modified : zhaojunbao , 2012-10-22
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.BankDao#getBankInfosByGoodsId(java.util.Map)
	 * ********************************************/
	public List<GoodsBank> getBankInfosByGoodsId(Map mapwhere) {
		List<GoodsBank> list = null;
		try {
			list = this.find("GoodsBank.GetUPbank", mapwhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * ******************************************** 
	 * method name : getBankInfosByGoodsId
	 * modified : lizhiqiang,2014年10月14日 14:28:52
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.BankDao#getBankInfosByGoodsId(java.util.Map)
	 * ********************************************/
	public List<GoodsBank> getUPBankInfosByGoodsId(Map mapwhere) {
		List<GoodsBank> list = null;
		try {
			list = this.find("GoodsBank.GetXeOrMw", mapwhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @Title: findXEBankInfos
	 * @Description: 获取小额支付银行信息
	 * @return
	 * @author wanyong
	 * @date 2014-7-21 下午5:13:31
	 */
	@SuppressWarnings("unchecked")
	public List<BankInfo> findXEBankInfos() {
		List<BankInfo> list = null;
		try {
			list = super.find("Bank.findXEBankInfos");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}
	
	/**
	 * @Title: findXEBankInfos
	 * @Description: 获取全网支付银行信息
	 * @return
	 * @author wangyuxin
	 * @date 2014-8-15 下午5:13:31
	 */
	@SuppressWarnings("unchecked")
	public List<BankInfo> findQWBankInfos() {
		List<BankInfo> list = null;
		try {
			list = super.find("Bank.findQWBankInfos");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

}
