/** *****************  JAVA头文件说明  ****************
 * file name  :  MerOperServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-3-3
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.dao.MerOperDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.service.MerOperService;


/** ******************  类说明  *********************
 * class       :  MerOperServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class MerOperServiceImpl implements MerOperService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private MerOperDao merOperDao;

	/** ********************************************
	 * method name   : findByMerId 
	 * modified      : xuhuafeng ,  2014-3-3
	 * @see          : @see com.umpay.hfmng.service.MerOperService#findByMerId(java.lang.String)
	 * ********************************************/
	public List<HfMerOper> findByMerId(String merId) {
		HfMerOper merOper = new HfMerOper();
		merOper.setMerId(merId);
		merOper.setState(2);
		List<HfMerOper> list = merOperDao.findBy(merOper);
		return list;
	}

	
	/** ********************************************
	 * method name   : getOperStrByMerId 
	 * modified      : xuhuafeng ,  2014-3-6
	 * @see          : @see com.umpay.hfmng.service.MerOperService#getOperStrByMerId(java.lang.String)
	 * ********************************************/     
	public String getOperStrByMerId(String merId) {
		Map<String, String> operatorNameMap = HfCacheUtil.getCache().getOperIdAndName();
		StringBuilder sb = new StringBuilder();
		List<HfMerOper> list = HfCacheUtil.getCache().getMerOperListByMerId(merId);
		for(HfMerOper oper : list){
			if(oper.getState() == 2 ){
				if(sb.length() != 0){
					sb.append(",");
				}
				sb.append(operatorNameMap.get(oper.getOperator()));
			}
		}
		return sb.toString();
	}
	
	public void batchUpdate(String merId, String[] operator, String userId) {
		List<HfMerOper> nowList = HfCacheUtil.getCache().getMerOperListByMerId(merId);//当前运营负责人列表
		Set<String> modSet = new HashSet<String>();//本次有变化列表
		List<HfMerOper> addList = new ArrayList<HfMerOper>();//本次新增列表
		List<HfMerOper> mod2List = new ArrayList<HfMerOper>();//本次修改为启用列表
		List<HfMerOper> mod4List = new ArrayList<HfMerOper>();//本次修改为禁用列表
		for(int i=0;i<operator.length;i++){
			modSet.add(operator[i]);
		}
		for(int i=nowList.size()-1;i>=0;i--){
			HfMerOper merOper = new HfMerOper();
			merOper = nowList.get(i);
			if(modSet.contains(merOper.getOperator())){
				modSet.remove(merOper.getOperator());//相同的去掉，剩下的则为新增的
				merOper.setState(2);
				mod2List.add(merOper);
			}else{
				merOper.setState(4);
				mod4List.add(merOper);
			}
		}
		for(String oper : modSet){
			HfMerOper merOper = new HfMerOper();
			merOper.setMerId(merId);
			merOper.setOperator(oper);
			merOper.setState(2);
			merOper.setCreator(userId);
			merOper.setModUser(userId);
			addList.add(merOper);
		}
		if(addList.size() != 0){
			merOperDao.bacthInsert(addList);
			log.info("新增商户运营负责人成功");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modUser", userId);
		if(mod2List.size() != 0){
			map.put("state", 2);
			map.put("list", mod2List);
			if(merOperDao.bacthUpdate(map) == mod2List.size()){
				log.info("修改商户运营负责人为启用成功");
			}else{
				log.info("修改商户运营负责人为启用失败");
				throw new DAOException("修改商户运营负责人为启用失败");
			}
		}
		if(mod4List.size() != 0){
			map.put("state", 4);
			map.put("list", mod4List);
			if(merOperDao.bacthUpdate(map) == mod4List.size()){
				log.info("修改商户运营负责人为禁用成功");
			}else{
				log.info("修改商户运营负责人为禁用失败");
				throw new DAOException("修改商户运营负责人为禁用失败");
			}
		}
	}
	
	public String getOperNameStrByOperStr(String operIdStr){
		Map<String,String> operatorNameMap= HfCacheUtil.getCache().getOperIdAndName();
		String[] operatorId = operIdStr.split(",");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<operatorId.length;i++){
			if(sb.length() != 0){
				sb.append(",");
			}
			sb.append(operatorNameMap.get(operatorId[i]));
		}
		return sb.toString();
	}

}
