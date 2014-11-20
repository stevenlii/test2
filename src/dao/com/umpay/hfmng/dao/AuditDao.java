package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.Audit;

/**
 * ****************** 类说明 ********************* class : AuditDao
 * 
 * @author : xhf
 * @version : 1.0 description : ***********************************************
 */
public interface AuditDao extends EntityDao<Audit> {

	/**
	 * ******************************************** method name : get
	 * description : 根据主键查询审核信息
	 * 
	 * @return : GoodsHfauditInfo
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @throws DataAccessException modified : xhf , 2012-8-30
	 *        上午11:45:27
	 * @see : *******************************************
	 */
	public Audit get(Map<String, String> mapWhere);
	
	public Audit getOneObj(Map<String, String> mapWhere);

	public Audit getBatchId(Map<String, String> mapWhere);

	public List<Audit> findByBatchId(String batchId);

	/**
	 * ******************************************** method name : updateState
	 * description : 更新审核状态
	 * 
	 * @return : int
	 * @param : @param audit
	 * @param : @return modified : xhf , 2012-9-4 上午09:52:25
	 *        *******************************************
	 */
	public int updateState(Audit audit);
	/**
	 * ********************************************
	 * method name   : batchUpdateState 
	 * description   : 根据批量号，批量修改审核表
	 * @return       : int
	 * @param        : @param audit
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-10-15  下午02:57:04
	 * *******************************************
	 */
	public int batchUpdateState(Audit audit);

	/**
	 * ******************************************** method name :
	 * getCheckFromAudit description : 验证审核表是否存在待插入数据
	 * 
	 * @return : Audit
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @throws DataAccessException modified : zhaojunbao , 2012-8-28
	 *        下午03:45:19
	 * @see : *******************************************
	 */
	public List getCheckFromAudit(Map<String, String> mapWhere)
			throws DataAccessException;

	public List getMerBankList(Map<String, String> mapWhere);

	public List checkModMerName(Map<String, String> mapWhere);
	
	public Audit checkMerGrade(Map<String, String> mapWhere);
	/**
	 * ********************************************
	 * method name   : checkExactlyFromAudit 
	 * description   : 根据条件精确查找审核表的数据
	 * @return       : List
	 * @param        : @param mapWhere
	 * @param        : @return
	 * modified      : lz ,  2013-3-25  下午02:32:14
	 * @see          : 
	 * *******************************************
	 */
	public List getCheckExactlyFromAudit(Map<String, String> mapWhere);
	/**
	 * ********************************************
	 * method name   : checkModChnlName 
	 * description   : 修改渠道信息时验证渠道名称唯一性， ixData记录渠道编号，ixData2记录审核中的渠道名称（如果该渠道的名称有修改的话）
	 * @return       : List
	 * @param        : @param mapWhere
	 * @param        : @return
	 * modified      : lz ,  2013-3-26  下午02:40:31
	 * @see          : 
	 * *******************************************
	 */
	public List checkModChnlName(Map<String, String> mapWhere);
	/**
	 * ********************************************
	 * method name   : checkDataAdd 
	 * description   : 检查添加的数据是否已存在
	 * @return       : List
	 * @param        : @param mapWhere
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-5-3  下午05:46:03
	 * *******************************************
	 */
	public int checkDataAdd(Map<String, String> mapWhere);

}
