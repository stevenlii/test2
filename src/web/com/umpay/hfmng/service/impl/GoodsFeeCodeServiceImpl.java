/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodeFeeCodeServiceImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-29
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.dao.GoodsFeeCodeDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.FeeCode;
import com.umpay.hfmng.service.GoodsFeeCodeService;
import com.umpay.sso.org.User;


/** ******************  类说明  *********************
 * class       :  GoodeFeeCodeServiceImpl
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class GoodsFeeCodeServiceImpl implements GoodsFeeCodeService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GoodsFeeCodeDao goodsFeeCodeDao;
	
	/**
	 *  ********************************************
	 * method name   : getFeeCodesByMerIdGoodId 
	 * modified      : zhaojunbao ,  2012-10-29
	 * @see          : @see com.umpay.hfmng.service.GoodsFeeCodeService#getFeeCodesByMerIdGoodId(java.util.Map)
	 * *******************************************
	 */
	public List<FeeCode> getFeeCodesByMerIdGoodId(Map whereMap) {
		return goodsFeeCodeDao.getGoodsFeeCodesByMerGoods(whereMap);
	}

	
	/** ********************************************
	 * method name   : updateGoodsFeeCode 
	 * modified      : zhaojunbao ,  2012-10-29
	 * @see          : @see com.umpay.hfmng.service.GoodsFeeCodeService#updateGoodsFeeCode(com.umpay.hfmng.model.FeeCode)
	 * ********************************************/     
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String updateGoodsFeeCode(List<String> list , FeeCode goodsFeeCode , User user)  throws Exception{
		String rs = "no";  //失败
		try {
			for (int i = 0; i < list.size(); i++) {
				goodsFeeCode.setModUser(user.getName());
				goodsFeeCode.setServiceId(list.get(i).trim());// 设置计费代码
				// goodsFeeCode.setState("4"); //设置状态 变为 4 注销状态 在action中做处理
				goodsFeeCodeDao.updateGoodsFeeCode(goodsFeeCode);
				rs="yes";
			}
		} catch (Exception e) {
			rs="no";
			log.error("更新商品计费代码信息出错！"+goodsFeeCode.toString());
			throw new DAOException("更新失败配置失败！"+goodsFeeCode.toString());
		}
		return rs;
	}


	
	/**
	 *  ********************************************
	 * method name   : insertGoodsFeeCode 
	 * modified      : zhaojunbao ,  2012-10-31
	 * @see          : @see com.umpay.hfmng.service.GoodsFeeCodeService#insertGoodsFeeCode(java.util.List, com.umpay.hfmng.model.FeeCode, com.umpay.sso.org.User)
	 * *******************************************
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String insertGoodsFeeCode(List<String> feeList, FeeCode feeCode ,User user) throws Exception {
		String rs="no";//操作结果提示
		try {
			  for(int i = 0;i < feeList.size() ;i++){
				  feeCode.setModUser(user.getLoginName());
				  feeCode.setServiceId(feeList.get(i).trim()); //添加计费代码
				  feeCode.setState("2");
				  int resCount = goodsFeeCodeDao.updateGoodsFeeCode(feeCode);
				  if(resCount == 0){
					  goodsFeeCodeDao.saveGoodsFeeCode(feeCode);
				  }
			    rs="yes";
			  }
		}catch(Exception e){
			rs="no";
			log.error("配置商品计费代码信息出错！");
			throw new DAOException("配置失败！");
		}
	    return  rs;
	}

}
