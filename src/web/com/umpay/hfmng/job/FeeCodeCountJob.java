/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCodeCountJob.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-7
 * *************************************************/ 

package com.umpay.hfmng.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.FeeCodeCountDao;
import com.umpay.hfmng.model.FeeCodeCount;



/** ******************  类说明  *********************
 * class       :  FeeCodeCountJob
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  计算匹配度
 * @see        :                        
 * ************************************************/
public class FeeCodeCountJob {
	protected Logger log = Logger.getLogger(this.getClass());
	private FeeCodeCountDao feeCodeCountDao = null;
	public void doJob() {
		//1-获取所有计费代码的使用次数
		List<FeeCodeCount> listAllCode = getFeeCodeCountDao().getUseCount();
		//2-获取分类、计费类型、金额完全匹配的计费代码列表
		List<FeeCodeCount> listMatchCodeCounts = getFeeCodeCountDao().getMatch(); // 获取完全匹配的计费代码
		Map<String, Object> feeMap = new HashMap<String, Object>();
		for (FeeCodeCount feeCodeCount : listMatchCodeCounts) {
			String sid = feeCodeCount.getServiceId();
			if(sid != null && !sid.trim().equals("")){
				feeMap.put(sid.trim(), feeCodeCount);
			}
		}
		//计算匹配度，并入统计表
		int sucUpdateCount=0,sucInsertCount=0,failUpCount=0,failInsCount=0;//记录成功失败的条数
		for (FeeCodeCount fCount : listAllCode) {
			fCount.trim();
			int useCount = Integer.valueOf(fCount.getUseCount());
			if(useCount == 0){
				fCount.setMatchType("2");//其他
			}else if(useCount == 1){
				if(feeMap.containsKey(fCount.getServiceId())){
					fCount.setMatchType("0");//精确匹配
				}else{
					fCount.setMatchType("1");//套用
				}
			}else{
				fCount.setMatchType("1");//套用
			}
			int res=0;//记录更新操作结果
			try{
				res = getFeeCodeCountDao().updateCount(fCount);
			}catch(DataAccessException e){
				failUpCount++; //统计失败次数
				log.error("更新计费代码匹配表失败！计费代码匹配度信息为 "+fCount.toString());
			}
			if(res == 1) {
				sucUpdateCount++; //统计成功次数
				log.info("更新计费代码统计" + fCount.toString());
			}else if (res == 0) {
				try{
					getFeeCodeCountDao().insertCount(fCount);
					sucInsertCount++;//统计成功插入的记录条数
					log.info("插入计费代码统计" + fCount.toString());
				}catch (DataAccessException e){
					failInsCount++;//统计插入失败记录条数
					log.error("插入计费代码匹配表失败！计费代码匹配度信息 "+fCount.toString());
				}
			}
		}
	}
	/**
	 *  获取DAO
	 * @return
	 */
	private FeeCodeCountDao getFeeCodeCountDao(){
		if(feeCodeCountDao == null){
			feeCodeCountDao = (FeeCodeCountDao) SpringContextUtil.getBean("feeCodeCountImpl"); 
		}
		return feeCodeCountDao;
	}
}
