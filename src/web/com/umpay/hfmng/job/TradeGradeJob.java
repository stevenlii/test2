package com.umpay.hfmng.job;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.GradeRule;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ThreadUtil;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.model.MerGrade;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.thread.SMSSendThread;

/* ******************  类说明  *********************
 * class       :  TradeGradeJob
 * @author     :  lz
 * @version    :  1.0  
 * description :  系统自动获取上月交易金额、上月交易笔数、上上月交易金额，计算上月交易增长率，然后入库
 * @see        :                        
 * ************************************************/
@Repository("tradeGradeJob")
public class TradeGradeJob {
	protected Logger log = Logger.getLogger(this.getClass());
	private MerGradeDao merGradeDao;
	private MessageService messageService;
	private String filePath;
	private static String[] phones;
	private Map<String,Map<String,Object>> lastLMGrade;//上上月的交易数据
	
	private final static String MERID = "merId";
	private final static String TURNOVER = "turnover";
	private final static String TRADINGCOUNT = "tradingCount";
	
	public void doJob(){
		try {
			log.info("交易数据入库：开始");
			//1 初始化参数
			ini();
			//2 加载上上月商户交易数据文件，为了计算交易额增长率。上月生成的文件中保存了上上月的数据
			loadLastMonthFile();
			//3 处理上月商户交易数据
			dealFile();
		} catch (Exception e) {
			log.info("定时任务[TradeGradeJob]执行异常",e);
		}	
		log.info("交易数据入库：结束");
	}
	private void ini() throws Exception{
		log.info("1 初始化参数开始...");
		lastLMGrade = new HashMap<String,Map<String,Object>>();
		//加载bean
		merGradeDao=(MerGradeDao)SpringContextUtil.getBean("merGradeDaoImpl");
		messageService=(MessageService)SpringContextUtil.getBean("messageService");
		//获取交易数据所在的路径
		filePath = messageService.getSystemParam("MerGrade.TradeGradeFilePath");
		if(filePath==null || filePath.equals(""))
			throw new Exception("MerGrade.TradeGradeFilePath参数未配置");
		// 加载需要短信通知的电话
		String phoneStr = messageService.getSystemParam("MerGrade.fileNotFoundNotifyPhone");
		phones = phoneStr.split(",");
		log.info("1 初始化参数完成！");
	}
	private void loadLastMonthFile()throws Exception{
		log.info("2 加载上上月交易数据文件开始...");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);//记录上上月交易数据的文件名中的日期是上个月
		try{
			lastLMGrade = loadLastMGradeFile(cal);
			if(lastLMGrade.size()==0){
				throw new Exception("上上月交易数据文件中无有效数据！");
			}
		}catch (Exception e) {
			log.info("加载上上月交易数据文件异常！");
			throw e;
		}
		log.info("2 加载上上月交易数据文件完成！");
	}
	private void dealFile()throws Exception{
		log.info("3 处理上月商户交易数据...");
		Calendar cal = Calendar.getInstance();
		List<Map<String,Object>> list = loadGradeFile(cal);
		if(list.size()==0){
			throw new Exception("上月交易数据文件中不存在有效数据！");
		}
		Map<String, String> mapWhere = new HashMap<String, String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		cal.add(Calendar.MONTH, -1);
		String lastMonth=df.format(cal.getTime());
		mapWhere.put("month", lastMonth);
		for(Map<String,Object> m : list){
			if(m.get(MERID)==null ||m.get(TURNOVER)==null||m.get(TRADINGCOUNT)==null)
				break;
			//设置商户号、商户名称、交易额、交易笔数、月份
			String merid=m.get(MERID).toString();
			MerGrade gradeBean=new MerGrade();
			gradeBean.setMerId(merid);
			gradeBean.setTurnover((BigDecimal)m.get(TURNOVER));
			gradeBean.setTradingCount((Integer)m.get(TRADINGCOUNT));
			gradeBean.setMonth(lastMonth);
			if(lastLMGrade.get(merid)!=null){
				//设置上上月交易额
				gradeBean.setLastTurnover((BigDecimal)lastLMGrade.get(merid).get(TURNOVER));
				//设置交易额增长率
				BigDecimal lastLTurnover=gradeBean.getLastTurnover();
				BigDecimal lastTurnover=gradeBean.getTurnover();
				BigDecimal riseRate=GradeRule.getRiseRate(lastTurnover,lastLTurnover);
				gradeBean.setRiseRate(riseRate);
			}
			//如果该商户号的上月评分数据已存在，则更新；否则插入
			mapWhere.put("merId", gradeBean.getMerId());
			MerGrade gb=null;
			try {
				gb=merGradeDao.get(mapWhere);
			} catch (Exception e) {
				log.info("查询商户号["+merid+"]上月评分数据失败",e);
				continue;
			}
			if(gb==null){
				try {
					merGradeDao.saveMerGrade(gradeBean);
					log.info("添加商户["+merid+"]交易数据成功");
				} catch (Exception e) {
					log.info("添加商户["+merid+"]交易数据失败",e);
					continue;
				}
			}
			else{
				try {
					merGradeDao.updateMerGradeNoAudit(gradeBean);
					log.info("更新商户["+merid+"]交易数据成功");
				} catch (Exception e) {
					log.info("更新商户["+merid+"]交易数据失败",e);
					continue;
				}
				
			}
		}
		log.info("3 处理上月商户交易数据完成！");
	}
	
	//加载上月的交易数据
	private List<Map<String,Object>> loadGradeFile(Calendar cal)throws Exception{
		DateFormat df = new SimpleDateFormat("yyyyMM");
		String yearMonth=df.format(cal.getTime());
		String fileName = "mertrans_"+ yearMonth + Const.TRADE_GRADE_DAY + ".txt";
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		BufferedReader reader= null;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"GB2312"));
			String line = null;
			while((line=reader.readLine())!=null){
				if(line.equals(""))continue;
				String[] lineArr = line.split(",");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(MERID, lineArr[0].trim());
				map.put(TURNOVER, new BigDecimal(lineArr[2].trim()));
				map.put(TRADINGCOUNT, Integer.valueOf(lineArr[3].trim()));
				result.add(map);
			}
		}catch(FileNotFoundException ee){
			SMSSendThread sms = new SMSSendThread();
			sms.setPhones(phones);
			sms.setMsg("商户考核评分所需的文件"+fileName+"不存在，请及时处理。");
			ThreadUtil.startThread(sms);
			throw ee;
		}catch(Exception e){
			throw e;
		}
		return  result;
	}
	//加载上上月的交易数据，返回的map的key为merid，value为map对象
	private Map<String,Map<String,Object>> loadLastMGradeFile(Calendar cal)throws Exception{
		DateFormat df = new SimpleDateFormat("yyyyMM");
		String yearMonth=df.format(cal.getTime());
		String fileName = "mertrans_"+ yearMonth + Const.TRADE_GRADE_DAY + ".txt";
		Map<String,Map<String,Object>> result = new HashMap<String,Map<String,Object>>();
		BufferedReader reader= null;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"GB2312"));
			String line = null;
			while((line=reader.readLine())!=null){
				if(line.equals(""))continue;
				String[] lineArr = line.split(",");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(TURNOVER, new BigDecimal(lineArr[2].trim()));
				map.put(TRADINGCOUNT, Integer.valueOf(lineArr[3].trim()));
				result.put(lineArr[0].trim(),map);
			}
		}catch(FileNotFoundException ee){
			SMSSendThread sms = new SMSSendThread();
			sms.setPhones(phones);
			sms.setMsg("商户考核评分所需的文件"+fileName+"不存在，请及时处理。");
			ThreadUtil.startThread(sms);
			throw ee;
		}catch(Exception e){
			throw e;
		}
		return  result;
	}
}
