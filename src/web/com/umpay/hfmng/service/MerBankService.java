/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankService.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-25
 * *************************************************/

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.MerBank;
import com.umpay.sso.org.User;

/**
 * ****************** 类说明 ********************* class : MerBankService
 * 
 * @author : xhf
 * @version : 1.0 description :
 * ************************************************/

public interface MerBankService {
	/**
	 * ******************************************** method name : load
	 * description : 根据主键查询商户银行
	 * 
	 * @return : MerBank
	 * @param : @param mapWhere
	 * @param : @return modified : xhf , 2012-11-21 下午05:02:43
	 *        *******************************************
	 */
	public MerBank load(Map<String, String> mapWhere);

	/**
	 * ******************************************** method name : findByMerId
	 * description : 获取某个商户号下的商户银行
	 * 
	 * @return : Map
	 * @param : @param merId
	 * @param : @return modified : xhf , 2012-11-21 下午05:03:17
	 *        *******************************************
	 */
	public Map<String, MerBank> findByMerId(String merId);

	/**
	 * ******************************************** method name : saveAudit
	 * description : 入审核表
	 * 
	 * @return : String
	 * @param : @param merBank
	 * @param : @return modified : xhf , 2012-11-21 下午05:04:15
	 *        *******************************************
	 */
	public String saveAudit(MerBank merBank);

	/**
	 * ******************************************** method name :
	 * enableAnddisable description : 启用/禁用入审核表
	 * 
	 * @return : String
	 * @param : @param array
	 * @param : @param user
	 * @param : @param action
	 * @param : @return modified : xhf , 2012-11-21 下午05:04:59
	 *        *******************************************
	 */
	public String enableAnddisable(String[] array, User user, String action);

	/**
	 * ******************************************** method name : saveMerBank
	 * description : 批量入审核表
	 * 
	 * @return : String
	 * @param : @param merBank
	 * @param : @param newOpen
	 * @param : @param modOpen
	 * @param : @param modClose
	 * @param : @return modified : xhf , 2012-11-21 下午05:05:18
	 *        *******************************************
	 */
	public String saveMerBank(MerBank merBank, List<String> newOpen, List<String> modOpen, List<String> modClose);

	/**
	 * ******************************************** method name : batchUpdate
	 * description : 批量配置商户银行
	 * 
	 * @return : String
	 * @param : @param channelIds
	 * @param : @param bankId
	 * @param : @param state
	 * @param : @param userId
	 * @param : @return modified : xuhuafeng , 2013-4-24 上午11:23:00
	 *        *******************************************
	 */
	public String batchUpdate(String[] merIds, String bankId, String state, String userId);

	/**
	 * @Title: saveMerBankForSynMwMerBank
	 * @Description: 新增商户银行（此功能不走审核表，直接新增。并忽略主键冲突异常。工单PKS00005782定制方法）
	 * @param merBank
	 * @return
	 * @author wanyong
	 * @date 2014-8-14 下午3:53:12
	 */
	public String saveMerBankForSynMwMerBank(MerBank merBank);
	
	/**
	 * 
	 * @Title: getMerBankInfo
	 * @Description: 获取商户银行
	 * @param merId
	 * @return
	 * @author lituo
	 * @date 2014-8-11 下午04:56:46
	 */
	public List<BankInfo> getMerBankInfo(String merId);
}
