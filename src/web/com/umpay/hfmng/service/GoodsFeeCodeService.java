/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsFeeCodeService.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-29
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.FeeCode;
import com.umpay.sso.org.User;


/** ******************  类说明  *********************
 * class       :  GoodsFeeCodeService
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface GoodsFeeCodeService {

	public List<FeeCode> getFeeCodesByMerIdGoodId(Map whereMap);
	public String updateGoodsFeeCode(List<String> list,FeeCode goodsFeeCode ,User user) throws Exception;
/**
 * ********************************************
 * method name   : insertGoodsFeeCode 
 * description   : 批量配置商品计费代码
 * @return       : String
 * @param        : @param feeList
 * @param        : @param feeCode
 * @param        : @param user
 * @param        : @return
 * modified      : zhaojunbao ,  2012-10-31  上午11:39:11
 * @throws Exception 
 * @see          : 
 * *******************************************
 */
	public  String insertGoodsFeeCode(List<String> feeList,FeeCode feeCode,User user) throws Exception;
}
