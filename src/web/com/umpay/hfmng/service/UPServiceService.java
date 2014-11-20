/** *****************  JAVA头文件说明  ****************
 * file name  :  UpServiceService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-9-29
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.UPService;


/** ******************  类说明  *********************
 * class       :  UpServiceService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface UPServiceService {
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 获取某条数据
	 * @return       : UPService
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @param gateId
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:43:07
	 * @see          : 
	 * *******************************************
	 */
	public UPService load(String merId, String goodsId, String gateId);
	/**
	 * ********************************************
	 * method name   : save 
	 * description   : 新增入库
	 * @return       : String
	 * @param        : @param upService
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-10-20  上午11:43:29
	 * *******************************************
	 */
	public String save(UPService upService) throws Exception;
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 修改入库
	 * @return       : String
	 * @param        : @param upService
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-10-20  上午11:43:41
	 * *******************************************
	 */
	public String update(UPService upService) throws Exception;
	/**
	 * ********************************************
	 * method name   : updateState 
	 * description   : 单个或批量启用、禁用方法
	 * @return       : String
	 * @param        : @param ID
	 * @param        : @param action
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-9-30  下午02:24:34
	 * *******************************************
	 */
	public String updateState(String ID, String action) throws Exception;
	/**
	 * *****************  方法说明  *****************
	 * method name   :  findBy
	 * @param		 :  @param merId
	 * @param		 :  @param goodsId
	 * @param		 :  @return
	 * @return		 :  List<UPService>
	 * @author       :  lizhiqiang 2014年10月20日 上午11:12:23
	 * description   :  
	 * @see          :  
	 * **********************************************
	 */
	List<UPService> findBy(String merId, String goodsId);

}
