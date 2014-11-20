/** *****************  JAVA头文件说明  ****************
 * file name  :  UpServiceServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-9-29
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.UPServiceDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.UPService;
import com.umpay.hfmng.service.UPServiceService;


/** ******************  类说明  *********************
 * class       :  UpServiceServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class UPServiceServiceImpl implements UPServiceService {
	
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private UPServiceDao upServiceDao;

	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2014-9-29
	 * @see          : @see com.umpay.hfmng.service.UpServiceService#save(com.umpay.hfmng.model.UpService)
	 * ********************************************/
	public String save(UPService upService) throws Exception {
		UPService us = load(upService.getMerId(), upService.getGoodsId(), upService.getGateId());
		if(us != null){
			log.info("该配置已存在。" + upService.toString());
			return "操作失败，该配置已存在。";
		}
		String message = amountCompare(upService);
		if(!"success".equals(message)){
			return message;
		}
		upServiceDao.insert(upService);
		log.info("新增商品计费代码成功。" + upService.toString());
		return "1";
	}

	/** ********************************************
	 * method name   : update 
	 * modified      : xuhuafeng ,  2014-9-29
	 * @see          : @see com.umpay.hfmng.service.UpServiceService#update(com.umpay.hfmng.model.UpService)
	 * ********************************************/
	public String update(UPService upService) throws Exception {
		UPService us = load(upService.getMerId(), upService.getGoodsId(), upService.getGateId());
		if("2".equals(us.getState())){
			String message = amountCompare(upService);
			if(!"success".equals(message)){
				return message;
			}
		}
		int num = upServiceDao.update(upService);
		if(num == 1){
			log.info("更新商品计费代码成功。" + upService.toString());
		}else if(num == 0){
			log.info("未找到商品计费代码" + upService.toString());
			throw new DAOException("未找到商品计费代码");
		}
		return "1";
	}

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2014-9-29
	 * @see          : @see com.umpay.hfmng.service.UpServiceService#load(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/     
	public UPService load(String merId, String goodsId, String gateId) {
		UPService us = new UPService();
		us.setMerId(merId);
		us.setGoodsId(goodsId);
		us.setGateId(gateId);
		return upServiceDao.get(us);
	}

	/** ********************************************
	 * method name   : updateState 
	 * modified      : xuhuafeng ,  2014-9-30
	 * @see          : @see com.umpay.hfmng.service.UpServiceService#updateState(com.umpay.hfmng.model.UpService)
	 * ********************************************/     
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String updateState(String ID, String action) throws Exception {
		String user = LoginUtil.getUser().getId();
		String[] IDs = ID.split(",");
		List<UPService> usList = new ArrayList<UPService>();
		for(int i=0;i<IDs.length;i++){
			String[] id = IDs[i].split("#");
			if(id.length != 3){
				throw new DAOException("传递参数错误,"+IDs[i]);
			}
			UPService us = new UPService();
			us.setMerId(id[0]);
			us.setGoodsId(id[1]);
			us.setGateId(id[2]);
			us.setState(action);
			us.setModUser(user);
			if("2".equals(action)){
				UPService upService = load(id[0], id[1], id[2]);
				String message = amountCompare(upService);
				if(!"success".equals(message)){
					return message;
				}
			}
			usList.add(us);
		}
		log.info("金额校验通过");
		
		for(UPService us : usList){
			int num = upServiceDao.updateState(us);
			if(num == 1){
				log.info("更新商品计费代码状态成功。" + us.toString());
			}else if(num == 0){
				log.info("未找到商品计费代码" + us.toString());
				throw new DAOException("未找到商品计费代码");
			}
		}
		return "1";
	}
	/** ********************************************
	 * method name   : findBy 
	 * modified      : lizhiqiang 2014年10月20日 11:11:36
	 * @see          : com.umpay.hfmng.service.UpServiceService#findBy(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/ 
	public List<UPService> findBy(String merId, String goodsId) {
		UPService us = new UPService();
		us.setMerId(merId);
		us.setGoodsId(goodsId);
		us.setState("2");
		return upServiceDao.findBy(us);
	}
	/**
	 * ********************************************
	 * method name   : checkAmount 
	 * description   : 先比较该计费代码不同通道的金额是否一致，再比较与对应商品银行的金额是否一致，一致则返回“success”
	 * @return       : String
	 * @param        : @param us
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-21  下午06:42:05
	 * *******************************************
	 */
	private String amountCompare(UPService us){

		String merId = StringUtils.trim(us.getMerId());
		String goodsId = StringUtils.trim(us.getGoodsId());
		String amount = StringUtils.trim(String.valueOf(us.getAmount()));
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache()
				.getGoodsInfoMap2();
		String goodsMapKey = merId + "-" + goodsId;
		GoodsInfo goods = goodsMap.get(goodsMapKey);
		String dbAmount = goods.getAmount();
		String priceMod = String.valueOf(goods.getPriceMode());//1 cannot pass
		if (!priceMod.equals("0")) {
			log.info("综合支付只支持定价商品 ！此商品：" + goodsMapKey + "非定价！");
			return "综合支付只支持定价商品 ！此商品：" + goodsMapKey + "非定价！";
		}
		if (amount.equals(dbAmount)) {
			// success
			log.info("成功，数据库存在此金额，新加数据金额匹配成功！");
			return "success";
		} else {
			// this step indispensable
			log.info("计费代码金额: " + amount + "，与商品：" + goodsMapKey + "金额： " + dbAmount + "匹配不成功！");
			return "计费代码金额: " + amount + "，与商品：" + goodsMapKey + "金额： " + dbAmount + "匹配不成功！";
		}
	}

}
