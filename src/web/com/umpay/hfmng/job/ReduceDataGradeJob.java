package com.umpay.hfmng.job;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.common.GradeRule;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.dao.ReduceDataDao;
import com.umpay.hfmng.model.HSMerSet;
import com.umpay.hfmng.model.MerGrade;
/* ******************  类说明  *********************
 * class       :  ReduceDataGradeJob
 * @author     :  lz
 * @version    :  1.0  
 * description :  系统自动获取上上月核减金额，然后入库
 * @see        :                        
 * ************************************************/
@Repository("reduceDataGradeJob")
public class ReduceDataGradeJob {
	protected Logger log = Logger.getLogger(this.getClass());
	
	private ReduceDataDao reduceDataDao;
	private MerGradeDao merGradeDao;
	private Map<String,Map<String,Object>> reduceData;//保存核减数据
	private final static String REDUCESUM = "reduceSum";
	private final static String TRADESUM = "tradeSum";
	private final static int reduceMonth = -3;//核减的月份，表示相对当前月的月份，如-3表示（T-3）月
	
	public void doJob(){
		try {
			log.info("核减数据入库：开始");
			//1 初始化参数
			ini();
			//2 获取核减数据
			getReduceData();
			//3 处理核减数据
			dealData();
		} catch (Exception e) {
			log.info("定时任务[ReduceDataGradeJob]执行异常",e);
		}	
		log.info("核减数据入库：结束");
	}
	private void ini() throws Exception{
		log.info("1 初始化参数开始...");
		reduceData = new HashMap<String,Map<String,Object>>();//（T-2）月的核减数据（上上月）
		
		//加载bean
		merGradeDao=(MerGradeDao)SpringContextUtil.getBean("merGradeDaoImpl");
		reduceDataDao=(ReduceDataDao)SpringContextUtil.getBean("reduceDataDaoImpl");
		
		log.info("1 初始化参数完成！");
	}
	//获取上上月的核减金额
	private void getReduceData()throws Exception{
		log.info("2 加载核减数据开始...");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, reduceMonth);
		DateFormat df = new SimpleDateFormat("yyyyMM");
		String lastLMonth=df.format(cal.getTime());
		
		try{
			//获取小额外地交易金额和核减金额
			List<HSMerSet> hsXEMerSetList = reduceDataDao.getXEReduceData(lastLMonth);
			if(hsXEMerSetList != null){
				log.info("获取小额外地交易金额和核减金额的数目为："+hsXEMerSetList.size());
				for(int i=0;i<hsXEMerSetList.size();i++){
					HSMerSet hsXEMerSet = hsXEMerSetList.get(i);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put(TRADESUM, hsXEMerSet.getBillSuccAmtm());//保存外地交易金额
					map.put(REDUCESUM, hsXEMerSet.getMuteAmt());//保存外地核减金额
					reduceData.put(hsXEMerSet.getMerId().trim(), map);
				}
			}
			//获取全网外地交易金额和核减金额，并与小额外地的数据合并
			List<HSMerSet> hsMWMerSetList = reduceDataDao.getMWReduceData(lastLMonth);
			if(hsMWMerSetList != null){
				log.info("获取全网外地交易金额和核减金额的数目为："+hsMWMerSetList.size());
				for(int i=0;i<hsMWMerSetList.size();i++){
					HSMerSet hsMWMerSet = hsMWMerSetList.get(i);
					String merId=hsMWMerSet.getMerId().trim();
					Map<String,Object> xeMap=reduceData.get(merId);
					if(xeMap!=null){
						BigDecimal tradeSum=hsMWMerSet.getBillSuccAmtm().add((BigDecimal)xeMap.get(TRADESUM));
						xeMap.put(TRADESUM, tradeSum);
						BigDecimal reduceSum=hsMWMerSet.getMuteAmt().add((BigDecimal)xeMap.get(REDUCESUM));
						xeMap.put(REDUCESUM, reduceSum);
						reduceData.put(merId, xeMap);
					}else{
						Map<String,Object> map = new HashMap<String,Object>();
						map.put(TRADESUM, hsMWMerSet.getBillSuccAmtm());//保存外地交易金额
						map.put(REDUCESUM, hsMWMerSet.getMuteAmt());//保存外地核减金额
						reduceData.put(merId, map);
					}
				}
			}
			log.info("全网和小额的外地交易金额和河间数据合并后的数目为："+reduceData.size());
		}catch (Exception e){
			log.info("2 获取核减数据失败！",e);
			throw new Exception("获取核减数据失败！");
		}
		if(reduceData.size()==0){
			throw new Exception("核减数据不存在！");
		}
		log.info("2 加载核减数据完成！");
	}
	
	private void dealData()throws Exception{
		log.info("3 处理核减数据...");
		
		//获取已导入的所有有效的上月评分数据
		Calendar cal = Calendar.getInstance();
		Map<String, String> mapWhere = new HashMap<String, String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		cal.add(Calendar.MONTH, -1);
		String lastMonth=df.format(cal.getTime());
		mapWhere.put("month", lastMonth);
		List<MerGrade> gbList;
		try {
			gbList = merGradeDao.find(mapWhere);
		} catch (Exception e) {
			log.info("查询已导入的所有上月评分数据失败",e);
			throw e;
		}
		
		//为已导入的上月评分更新核减金额和核减率
		MerGrade gradeBean=new MerGrade();
		BigDecimal tradeSum=null;
		BigDecimal reduceSum=null;
		for(MerGrade gb:gbList){
			gb.trim();
			String merId=gb.getMerId();
			String month=gb.getMonth();
			
			if(reduceData.get(merId)==null)
				continue;
			
			gradeBean.setMerId(merId);
			gradeBean.setMonth(month);
			//设置外地交易额和外地核减金额
			tradeSum=(BigDecimal)reduceData.get(merId).get(TRADESUM);
			reduceSum=(BigDecimal)reduceData.get(merId).get(REDUCESUM);
			if(tradeSum!=null && reduceSum!=null){
				gradeBean.setReduceSum(reduceSum);
				//设置核减率
				BigDecimal reduceRate=GradeRule.getReduceRate(tradeSum, reduceSum);
				gradeBean.setReduceRate(reduceRate);
			}
			try {
				merGradeDao.updateMerGradeNoAudit(gradeBean);
				log.info("更新商户["+merId+"]核减数据成功");
			} catch (Exception e) {
				log.info("更新商户["+merId+"]核减数据失败",e);
				continue;
			}
		}
		log.info("3 处理上上月核减数据完成！");
	}
}
