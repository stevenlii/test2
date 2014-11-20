/** *****************  JAVA头文件说明  ****************
 * file name  :  AuditService.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-8-30
 * *************************************************/

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.Audit;
import com.umpay.sso.org.User;

/**
 * ****************** 类说明 ********************* class : AuditService
 * 
 * @author : xhf
 * @version : 1.0 description : 审核service
 *          ***********************************************
 */
public interface AuditService {

	/**
	 * ******************************************** method name : load
	 * description : 根据id查询
	 * 
	 * @return : Audit
	 * @param : @param mapWhere
	 * @param : @throws DataAccessException modified : xhf , 2012-9-5 上午11:13:43
	 *        *******************************************
	 */
	public Audit load(Map<String, String> mapWhere) throws DataAccessException;

	public Audit loadBatchId(Map<String, String> mapWhere)
			throws DataAccessException;

	public List findByBatchId(String batchId);

	/**
	 * ******************************************** method name : merAuditPass
	 * description : 商户审核通过
	 * 
	 * @return : String
	 * @param : @param audit modified : xhf , 2012-9-5 上午11:14:19
	 *        *******************************************
	 */
	public String merAuditPass(Audit audit);

	/**
	 * ******************************************** method name :
	 * merAuditNotPass description : 商户审核不通过
	 * 
	 * @return : String
	 * @param : @param audit modified : xhf , 2012-9-5 上午11:14:39
	 *        *******************************************
	 */
	public String merAuditNotPass(Audit audit);

	/**
	 * ******************************************** method name : goodsAuditPass
	 * description : 商品审核通过
	 * 
	 * @return : String
	 * @param : @param audit modified : xhf , 2012-9-5 上午11:14:53
	 *        *******************************************
	 */
	public String goodsAuditPass(Audit audit);

	/**
	 * ******************************************** method name :
	 * goodsAuditNotPass description : 商品审核不通过
	 * 
	 * @return : String
	 * @param : @param audit modified : xhf , 2012-9-5 上午11:15:14
	 *        *******************************************
	 */
	public String goodsAuditNotPass(Audit audit);

	/**
	 * ******************************************** method name : getCheck
	 * description :
	 * 
	 * @return : Audit
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @throws DataAccessException modified : zhaojunbao , 2012-8-28
	 *        下午03:49:29 *******************************************
	 */
	public String getCheck(Map<String, String> mapWhere)
			throws DataAccessException;

	/**
	 * ******************************************** method name : getCheckMerId
	 * description : 商户号唯一性验证
	 * 
	 * @return : String
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-9-4
	 *        下午07:32:22
	 * @see : *******************************************
	 */
	public String getCheckMerId(Map<String, String> mapWhere)
			throws DataAccessException;

//	public String getCheckMerName(Map<String, String> mapWhere);
//	
//	public String checkModMerName(Map<String, String> mapWhere);
	
	public String merBankauditPass(String[] array, User user);

	public String merBankauditNotPass(String[] array, User user,
			String resultDesc);

	public String goodsBankauditPass(String[] array, User user);

	public String goodsBankauditNotPass(String[] array, User user,
			String resultDesc);

	public String goodsBankauditGoPass(String batchId, User user);

	public String goodsBankAuditNotPass(String batchId, User user,
			String resultDesc);

	/**
	 * ******************************************** method name : getCheckBankId
	 * description : 支付服务商编号唯一性验证
	 * 
	 * @return : String
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-9-26
	 *        下午01:44:17
	 * @see : *******************************************
	 */
	public String getCheckBankId(Map<String, String> mapWhere)
			throws DataAccessException;

	/**
	 * ******************************************** method name : getCheckBank
	 * description :
	 * 
	 * @return : String
	 * @param : @param mapWhere
	 * @param : @return modified : zhaojunbao , 2012-10-15 下午05:03:18
	 * @see : *******************************************
	 */
	public String getCheckBank(Map<String, String> mapWhere);

	/**
	 * ******************************************** method name :
	 * getCheckMerBank description :
	 * 
	 * @return : int
	 * @param : @param mapWhere
	 * @param : @return modified : xhf , 2012-10-16 下午04:25:18
	 *        *******************************************
	 */
	public Map getMerBankAudit(Map<String, String> mapWhere);

	/**
	 * ******************************************** method name :
	 * getCheckMerBankList description : 返回list类型值
	 * 
	 * @return : List<Audit>
	 * @param : @param mapWhere
	 * @param : @return modified : zhaojunbao , 2012-11-15 下午02:40:32
	 * @see : *******************************************
	 */
	public List<Audit> getCheckMerBankList(Map<String, String> mapWhere);

	/**
	 * @Title: couponAuditNotPass
	 * @Description: 审核不通过处理接口，支持兑换券审核/兑换券规则
	 * @param
	 * @param audit
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:58:12
	 */
	public void couponAuditNotPass(Audit audit) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: couponAuditPass
	 * @Description: 兑换券审核通过处理接口
	 * @param
	 * @param audit
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:58:33
	 */
	public void couponAuditPass(Audit audit) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: couponRuleAuditPass
	 * @Description: 兑换券规则审核通过处理接口
	 * @param
	 * @param audit
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午11:04:11
	 */
	public void couponRuleAuditPass(Audit audit) throws BusinessException,
			DataAccessException;
	
	public String gradeAuditPass(Audit audit);
	
	public String gradeAuditNotPass(Audit audit);
	
	public String chnlBankAuditPass(String[] id, String userId, String resultDesc);
	
	public String chnlBankAuditNotPass(String[] id, String userId, String resultDesc);
	
	public String checkDataAdd(Map<String, String> map);
	
	/**
	 * ********************************************
	 * method name   : checkAuditUser 
	 * description   : 校验审核人是否可以审核，规则：审核人不能审核自己修改的信息，除非是admin
	 * @return       : boolean
	 * @param        : @param creator
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-1-9  下午05:46:25
	 * *******************************************
	 */
	public boolean checkAuditUser(String creator);
	/**
	 * ********************************************
	 * method name   : checkAuditData 
	 * description   : 校验审核数据，其中包含审核人的校验
	 * @return       : String
	 * @param        : @param id
	 * @param        : @param auditList
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-1-13  下午05:09:19
	 * *******************************************
	 */
	public String checkAuditData(String[] id, List<Audit> auditList);
	
	/**
	 * 查询审核表的最近一条记录
	 * @Title: getOneObj
	 * @Description: TODO
	 * @param mapWhere
	 * @return
	 * @return Audit
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-19 下午06:25:08
	 */
	public Audit getOneObj(Map<String, String> mapWhere);
}
