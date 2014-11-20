package com.umpay.hfmng.job;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.GradeRule;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.dao.MerInfoDao;
import com.umpay.hfmng.model.MerGrade;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.MessageService;

/* ******************  类说明  *********************
 * class       :  CalculateGradeJob
 * @author     :  lz
 * @version    :  1.0  
 * description :  根据已经入库的交易额、交易额增长率、核减率计算交易额指标、增长率指标、虚假交易指标、客诉指标。
 * @see        :                        
 * ************************************************/
@Repository("calculateGradeJob")
public class CalculateGradeJob {

	protected Logger log = Logger.getLogger(this.getClass());
	private MerGradeDao merGradeDao;
	private MerInfoDao merInfoDao;
	private MessageService messageService;
	private List<MerGrade> gbList;//保存从数据库中查询到的所有的上月评分
	private Map<String,String> ywlx;//商户业务类型
	private final static String GAME = "数字及游戏点卡";//商户的业务类型
	
	public void doJob(){
		log.info("系统生成评分：开始");
		try {
			//1 初始化参数
			ini();
			//2 加载库中的上月数据
			loadLastMData();
			//3 处理上月数据
			dealData();
		} catch (Exception e) {
			log.info("定时任务[CalculateGradeJob]执行异常",e);
		}
		log.info("系统生成评分：结束");
	}
	private void ini(){
		log.info("1 初始化参数开始...");
		gbList=new ArrayList<MerGrade>();
		//加载bean
		merGradeDao=(MerGradeDao)SpringContextUtil.getBean("merGradeDaoImpl");
		merInfoDao=(MerInfoDao)SpringContextUtil.getBean("merInfoDaoImpl");
		messageService=(MessageService)SpringContextUtil.getBean("messageService");
		//获取商户全部业务类型
		try {
			String merType = messageService.getMessage("merType");
			this.ywlx = JsonHFUtil.getMapFromJsonArrStr(merType);
		} catch (Exception e) {
			log.info("获取商户全部业务类型失败",e);
		}
		log.info("1 初始化参数完成！");
	}
	private void loadLastMData()throws Exception{
		log.info("1 加载库中的上月数据开始...");
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		Map<String, String> mapWhere = new HashMap<String, String>();
		cal.add(Calendar.MONTH, -1);
		String lastMonth=df.format(cal.getTime());
		mapWhere.put("month", lastMonth);
		try {
			gbList=merGradeDao.find(mapWhere);
		} catch (Exception e) {
			log.info("查询上月评分数据失败");	
			throw e;
		}
		log.info("1 加载库中的上月数据结束");
	}
	private void dealData()throws Exception{
		for(MerGrade gb:gbList){
			gb.trim();
			MerGrade gradeBean=new MerGrade();
			gradeBean.setMerId(gb.getMerId());
			gradeBean.setMonth(gb.getMonth());
			//设置交易额指标
			gradeBean.setTurnoverIndex(GradeRule.getTurnoverIndex(gb.getTurnover()));
			//设置交易额增长率指标
			gradeBean.setRiseRateIndex(GradeRule.getRiseRateIndex(gb.getRiseRate()));
			//设置虚假交易指标
			gradeBean.setFalseTradeIndex(this.getFalseTradeIndex(gb.getMerId(), gb.getReduceRate()));
			//设置客诉指标
			gradeBean.setComplaintIndex(GradeRule.getComplaintIndex(gb.getComplaintCount(), gb.getTradingCount()));
			//交易额指标、增长率指标、虚假交易指标均为null时，不更新
			if(gradeBean.getTurnoverIndex()==null && gradeBean.getRiseRateIndex()==null && gradeBean.getFalseTradeIndex()==null){
				continue;
			}
			//设置新的总分。优先累加新的分数，没有新分数时使用旧分数，旧分数不存在时（即等于数据库默认值时），不再累加该项
			BigDecimal total=new BigDecimal(0);
			BigDecimal defaultGrade=new BigDecimal(Const.MERGRADE_DEFAULT);
			if(gradeBean.getTurnoverIndex()!=null)
				total=total.add(gradeBean.getTurnoverIndex());
			else if(gb.getTurnoverIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getTurnoverIndex());
			
			if(gradeBean.getRiseRateIndex()!=null)
				total=total.add(gradeBean.getRiseRateIndex());
			else if(gb.getRiseRateIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getRiseRateIndex());
			
			if(gradeBean.getFalseTradeIndex()!=null)
				total=total.add(gradeBean.getFalseTradeIndex());
			else if(gb.getFalseTradeIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getFalseTradeIndex());
			
			if(gradeBean.getComplaintIndex()!=null)
				total=total.add(gradeBean.getComplaintIndex());
			else if(gb.getComplaintIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getComplaintIndex());
			
			
			if(gb.getSysStabIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getSysStabIndex());
			if(gb.getCooperateIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getCooperateIndex());
			if(gb.getBreachIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getBreachIndex());
			if(gb.getUpgradeIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getUpgradeIndex());
			if(gb.getMarketingIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getMarketingIndex());
			if(gb.getSupportIndex().compareTo(defaultGrade)!=0)
				total=total.add(gb.getSupportIndex());
			gradeBean.setTotal(total);
			try {
				merGradeDao.updateMerGradeNoAudit(gradeBean);
			} catch (Exception e) {
				log.info("更新商户号["+gb.getMerId()+"]的评分失败",e);
				throw e;
			}
		}
	}
	private BigDecimal getFalseTradeIndex(String merId,BigDecimal reduceRate){
		if(reduceRate==null)
			return null;
		if(reduceRate.doubleValue()==Const.MERGRADE_DEFAULT)
			return null;
		
		BigDecimal falseTradeIndex=null;
		MerInfo mer=merInfoDao.get(merId);
		if(mer==null){
			log.info("查询该商户的业务类型失败：merid["+merId+"]");
		}else{
			mer.trim();
			Set<Map.Entry<String, String>> set = ywlx.entrySet();
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				if(GAME.equals(entry.getValue())){
					if(entry.getKey().equals(mer.getCategory())){
						falseTradeIndex = GradeRule.getGameMerFTGrade(reduceRate);
					}else{
						falseTradeIndex = GradeRule.getOtherMerFTGrade(reduceRate);
					}
					break;
				}
			}
		}
		return falseTradeIndex;
	}
	
	public static void main(String[] sd){
		String merType="[{\"002\":\"数字及游戏点卡\"},{\"004\":\"电子书\"},{\"005\":\"邮箱\"},{\"099\":\"其他\"}]";
		List<Map<String,String>> list=JsonHFUtil.getListFromJsonArrStr(merType, Map.class);
		Map<String,String> m=list.get(0);
		String s=m.get("002");
		System.out.println(s);
		
		Map<String,String> map=JsonHFUtil.getMapFromJsonArrStr(merType);
		String ss=map.get("002");
		System.out.println(ss);
	}
}
