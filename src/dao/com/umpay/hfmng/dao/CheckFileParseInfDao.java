package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CheckFileParseInf;

/**
 * @ClassName: CheckFileParseInfDao
 * @Description: 对账文件解析管理数据库处理接口
 * @author wanyong
 * @date 2013-3-5 上午11:41:28
 */
public interface CheckFileParseInfDao extends EntityDao<CheckFileParseInf> {

	/**
	 * @Title: insertCheckFileParseInf
	 * @Description: 新增任务接口
	 * @param
	 * @param CheckFileParseInf
	 * @author wanyong
	 * @date 2013-3-5 上午11:43:13
	 */
	public void insertCheckFileParseInf(CheckFileParseInf checkFileParseInf);

	/**
	 * @Title: updateCheckFileParseInf
	 * @Description: 更新任务接口
	 * @param
	 * @param CheckFileParseInf
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 下午05:23:25
	 */
	public int updateCheckFileParseInf(CheckFileParseInf checkFileParseInf);

	/**
	 * @Title: deleteCheckFileParseInf
	 * @Description: 删除任务接口
	 * @param
	 * @param CheckFileParseInf
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 下午07:02:58
	 */
	public int deleteCheckFileParseInf(CheckFileParseInf checkFileParseInf);

	/**
	 * @Title: findCount
	 * @Description: 查询任务记录数
	 * @param
	 * @param fileName
	 * @param fileType
	 * @param fileState
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 下午07:03:11
	 */
	public int findCount(String fileName, int fileType, int fileState,
			String merId);

}
