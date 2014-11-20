package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.MerGrade;

public interface MerGradeDao extends EntityDao<MerGrade>{
	
	public MerGrade get(Map<String, String> mapWhere);
	
	public int updateModLock(MerGrade merGrade);
	/**
	 * ********************************************
	 * method name   : find 
	 * description   : 
	 * @return       : List<MerGrade>
	 * @param        : @param mapWhere
	 * @param        : @return
	 * modified      : lz ,  2013-2-25  下午03:27:23
	 * @see          : 
	 * *******************************************
	 */
	public List<MerGrade> find(Map<String,String> mapWhere);
	/**
	 * ********************************************
	 * method name   : saveMerGrade 
	 * description   : 添加商户评分
	 * @return       : void
	 * @param        : @param merGrade
	 * modified      : lz ,  2013-2-25  上午10:21:07
	 * @see          : 
	 * *******************************************
	 */
	public void saveMerGrade(MerGrade merGrade);
	/**
	 * ********************************************
	 * method name   : updateMerGrade 
	 * description   : 
	 * @return       : void
	 * @param        : @param merGrade
	 * modified      : lz ,  2013-2-25  上午11:39:54
	 * @see          : 
	 * *******************************************
	 */
	public void updateMerGrade(MerGrade merGrade);
	/**
	 * ********************************************
	 * method name   : updateMerGradeNoAudit 
	 * description   : 更新商户评分，无须审核，无视锁定，用于定时任务
	 * @return       : void
	 * @param        : @param merGrade
	 * modified      : lz ,  2013-2-25  下午12:02:52
	 * @see          : 
	 * *******************************************
	 */
	public void updateMerGradeNoAudit(MerGrade merGrade);
}
