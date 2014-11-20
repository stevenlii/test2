/** *****************  JAVA头文件说明  ****************
 * file name  :  MerGradeService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-2-22
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.model.MerGrade;


/** ******************  类说明  *********************
 * class       :  MerGradeService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface MerGradeService {
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 根据主键查询
	 * @return       : MerGrade
	 * @param        : @param merId
	 * @param        : @param month
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-25  下午02:31:42
	 * *******************************************
	 */
	public MerGrade load(String merId, String month);
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 修改信息入审核表
	 * @return       : String
	 * @param        : @param merGrade
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-25  下午02:31:55
	 * *******************************************
	 */
	public String update(MerGrade merGrade);
	/**
	 * ********************************************
	 * method name   : uploadGrades 
	 * description   : 导入手工评分
	 * @return       : List<MerGrade>
	 * @param        : @param file
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-27  下午04:36:40
	 * *******************************************
	 */
	public List<List<String>> uploadGrades(MultipartFile file) throws Exception;
	/**
	 * ********************************************
	 * method name   : writeFile 
	 * description   : 导入失败列表写入文件
	 * @return       : void
	 * @param        : @param fl
	 * @param        : @param feeCodeList
	 * modified      : xuhuafeng ,  2013-2-27  下午04:40:22
	 * *******************************************
	 */
	public void writeFile(File fl,List<List<String>> merGradeList);
	
	public List<List<String>> readFile(String fileName);
	
	public void operateLog(MerGrade merGrade);
	/**
	 * ********************************************
	 * method name   : loadTradeGrade 
	 * description   : 交易数据手工入库
	 * @return       : String
	 * @param        : @return
	 * modified      : lz ,  2013-3-5  上午10:06:09
	 * @see          : 
	 * *******************************************
	 */
	public String loadTradeGrade();
	/**
	 * ********************************************
	 * method name   : loadReduceDataGrade 
	 * description   : 核减数据入库
	 * @return       : String
	 * @param        : @return
	 * modified      : lz ,  2013-3-5  上午11:24:57
	 * @see          : 
	 * *******************************************
	 */
	public String loadReduceDataGrade();
	/**
	 * ********************************************
	 * method name   : calculateGrade 
	 * description   : 手工计算评分
	 * @return       : String
	 * @param        : @return
	 * modified      : lz ,  2013-3-5  上午11:25:01
	 * @see          : 
	 * *******************************************
	 */
	public String calculateGrade();

}
