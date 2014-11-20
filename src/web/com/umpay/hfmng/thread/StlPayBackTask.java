package com.umpay.hfmng.thread;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;

import com.umpay.coupon.system.restclient.CouponRestRequester;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.FtpUtil2;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.XLSUtil;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.model.StlPayBack;
import com.umpay.hfmng.service.StlPayBackService;
import com.umpay.uniquery.util.StringUtil;

/** 
 * 退费明细文件入库任务，每月初执行，入库上上个月退费明细
 * <p>创建日期：2013-12-11 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public class StlPayBackTask extends Thread {
	private static final Logger log = Logger.getLogger(StlPayBackTask.class);
	
	private static String CACHEKEY_TIMTERADDR="BP_后台参数-TIMERADDR";//缓存键——定时调度器URL地址	
	private static String PARATYPE_PAYBACK = "BP_退费明细文件入库任务";//退费明细文件入库任务参数类型
	private static String PARACODE_FTPIP = "FTPIP";//ftp服务器ip地址
	private static String PARACODE_FTPPORT = "FTPPORT";//ftp服务器端口
	private static String PARACODE_FTPUSER = "FTPUSER";//ftp用户名
	private static String PARACODE_FTPPASSWORD = "FTPPASSWORD";//ftp密码
	private static String PARACODE_FTPTIMEOUT = "FTPTIMEOUT";//ftp超时时间
	private static String PARACODE_FTPPATH = "FTPPATH";//文件在ftp服务器上的路径
	private static String PARACODE_LOCALPATH = "LOCALPATH";//文件本地端保存地址
	private static String PARACODE_FILENAMETEM = "FILENAMETEM";// 文件名模板

	private String taskRpid = "";//任务流水，通知定时监控服务器执行结果时使用
	
	public void setTaskRpid(String taskRpid) {
		this.taskRpid = taskRpid;
	}

	/** ftp配置 **/
	private String ftpip = ""; // ftp服务器ip地址  10.10.1.191
	private int ftpport=21; // ftp服务器端口
	private String ftpuser = ""; // ftp用户名
	private String ftppassword = ""; // ftp密码
	private int ftptimeout=600; // ftp超时时间
	private String ftppath = ""; // 文件在ftp服务器上的路径 /usr/mpsp/hfStlSvr/hfdata/payback
	private String localpath = "D:\\湖南分公司\\兑换券项目\\五期\\设计\\参考资料\\"; // 文件本地端保存地址
	private String filenametem = "";// 文件名模板，如：payback%DATE%.xls，真实文件名为：payback201310.xls 
									// 一个月一个文件，201311的文件可能在201312月底出来，也可能到201401月出来

	private String stlmonth = "201309";//结算月份，文件名中月份
	
	/**
	 * 无参构造方法
	 */
	public StlPayBackTask(){}
	
	/**
	 * 构造方法
	 * @param taskRpid 任务id
	 */
	public StlPayBackTask(String taskRpid){
		this.taskRpid = taskRpid;
	}

	/**
	 * @Title: run
	 * @Description: 任务主方法
	 * @author jxd
	 * @date 2013-12-12 下午6:03:23
	 */
	@Override
	public void run() {
		log.debug("退费明细文件入库任务，开始执行……");
		String retCode = Const.RETCODE_TIMER_FAILED;
		String retMsg = "退费明细文件入库任务执行失败";
		try {
			// 防止重复执行任务，获取最大结算月份，如果小于等于上月份，则已执行
			StlPayBackService stlPayBackService = (StlPayBackService)SpringContextUtil.getBean("stlPayBackServiceImpl");
			String maxStlMonth = stlPayBackService.getMaxStlMonth();
			if(StringUtil.isEmpty(maxStlMonth)){
				maxStlMonth = "201309";//第一次跑任务时使用，也可以使用配置参数的方式实现
			}
			stlmonth = getLast2Month();//获取上上个月份
			if(stlmonth.compareTo(maxStlMonth) <= 0){//如果上上月份小于或等于已执行最大月份，则任务已执行过
				retCode = Const.RETCODE_TIMER_SUCCESS;
				retMsg = stlmonth +"月退费明细文件入库任务已执行";
				log.info(retMsg);
				return;
			}
			
			// 初始化参数
			initPara();
			// FTP获取excel文件，如：payback201310.xls 
			String filename = filenametem.replace("%DATE%", stlmonth);
			getFtpFile(filename);
			// 解释excel文件
			List<StlPayBack> list = parseExcel(filename);
			// 退费明细入库
			stlPayBackService.saveStlPayBackBatch(list);
			// 设置任务执行成功返回信息
			retCode = Const.RETCODE_TIMER_SUCCESS;
			retMsg = "退费明细文件入库任务执行成功";
			log.info(retMsg);
		} catch (Exception e) {
			log.error("退费明细文件入库任务执行失败", e);
		} finally {
			try {
				//通知定时监控服务器执行结果
				sendResponseToTimerInvoker(retCode, retMsg);
			} catch (Exception e) {
				log.error("任务执行结果通知定时监控服务器失败", e);
			}
		}
		log.debug("退费明细文件入库任务，结束执行……");
	}

	/**
	 * @Title: parseExcel
	 * @Description: 解析Excel
	 * @param filename 文件路径
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 * @author jxd
	 * @date 2013-12-13 下午12:50:36
	 */
	private List<StlPayBack> parseExcel(String filename) throws BiffException, IOException {
		List<StlPayBack> list = new ArrayList<StlPayBack>();
		XLSUtil xlsUtil = new XLSUtil(localpath + File.separator + filename);
		// 只取第一个Sheet
		Sheet sheet = xlsUtil.getSheet(0);
		int rows = xlsUtil.getRows(sheet);
		int sucessCount = 0; // 正常退费信息数量
		int errorCount = 0; // 空行或格式不合法行数
		// 模板第一行为标题行，真正数据从第二行开始取
		for (int i = 1; i < rows; i++) {
			String undodate="";
			Cell cell = sheet.getCell(1, i);
			if(cell.getType() == CellType.DATE){
				DateCell dc = (DateCell)cell;
				undodate = new SimpleDateFormat("yyyy-MM-dd").format(dc.getDate());
			} else if (cell.getType() == CellType.NUMBER){
				undodate = xlsUtil.getDate(sheet, 1, i);//日期，格式如5月2日
				undodate = formatDate(undodate);
			} else if (cell.getContents().matches("/d{4}-/d{1,2}-/d{1,2}")){
				undodate = cell.getContents();
			} else {
				log.info(cell.getContents() + "日期格式不正确，忽略该行");
				continue;
			}
			String mobileid = xlsUtil.getDate(sheet, 2, i);//手机号
			String bankname = xlsUtil.getDate(sheet, 3, i);//商品银行
			String provname = xlsUtil.getDate(sheet, 4, i);//省份
			String refundamount = xlsUtil.getDate(sheet, 5, i);//退费金额
			String mertype = xlsUtil.getDate(sheet, 6, i);//商户类型
			String mername = xlsUtil.getDate(sheet, 7, i);//所属商户
			String accid = xlsUtil.getDate(sheet, 8, i);//商户号
			String goodsid = xlsUtil.getDate(sheet, 9, i);//子商户号
			String gooodsname = xlsUtil.getDate(sheet, 10, i);//子商户名
			String withdrawreason = xlsUtil.getDate(sheet, 11, i);//退费原因
			String desc = xlsUtil.getDate(sheet, 12, i);//详细描述
			StlPayBack payBack = new StlPayBack();
			payBack.setPaybackid(SequenceUtil.getInstance().getSequence("PAYBACKID", 16));
			payBack.setStlmonth(stlmonth);
			payBack.setUndodate(undodate);
			payBack.setMobileid(mobileid);
			payBack.setBankname(bankname);
			payBack.setProvname(provname);
			payBack.setRefundamount(refundamount);
			payBack.setMertype(mertype);
			payBack.setMername(mername);
			payBack.setAccid(accid);
			payBack.setGoodsid(goodsid);
			payBack.setGoodsname(gooodsname);
			payBack.setWithdrawreason(withdrawreason);
			payBack.setDesc(desc);
			payBack.setIsuse(2);
			payBack.trim();
			log.info(payBack);
			list.add(payBack);
			sucessCount ++;
		}
		log.info("解析Excel结束，正常退费信息数量：" + sucessCount + "，空行或格式不合法行数：" + errorCount);
		return list;
	}

	/**
	 * @Title: initPara
	 * @Description: 初始化任务执行参数
	 * @author jxd
	 * @date 2013-12-13 上午11:34:13
	 */
	private void initPara() {
		Map<String, Object> paraMap=HfCacheUtil.getCache().getParaMap();
		ftpip = ((Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FTPIP)).getParaValue();
		Para p = (Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FTPPORT);
		if(p != null){
			ftpport = Integer.valueOf(p.getParaValue());
		}
		ftpuser = ((Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FTPUSER)).getParaValue();
		ftppassword = ((Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FTPPASSWORD)).getParaValue();
		ftppath = ((Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FTPPATH)).getParaValue();
		localpath = ((Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_LOCALPATH)).getParaValue();
		p = (Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FTPTIMEOUT);
		if(p != null){
			ftptimeout = Integer.valueOf(p.getParaValue());
		}
		filenametem = ((Para)paraMap.get(PARATYPE_PAYBACK +"-"+ PARACODE_FILENAMETEM)).getParaValue();
	}

	/**
	 * @Title: getFtpFile
	 * @Description: FTP获取文件
	 * @param filename
	 * @return 文件名，如果上月文件不存在，则取上上个月的文件
	 * @throws Exception
	 * @author jxd
	 * @date 2013-12-11 下午6:06:03
	 */
	private void getFtpFile(String filename) throws Exception {
		// ftp使用，从ftp上copy文件到指定的路径
		FtpUtil2 fu = new FtpUtil2();
		try {
			fu.setTimeout(ftptimeout);
			fu.connect(ftpip, ftpport);
			fu.login(ftpuser, ftppassword);
			fu.enterLocalPassiveMode();
			// 进入存放文件的ftp路径
			fu.cd(ftppath);
			fu.bin();
			// 若指定路径不存在，则创建
			File localpathdir = new File(localpath);
			if (!localpathdir.exists()) {
				boolean flag = localpathdir.mkdirs();
				if(flag){
					log.info("退费明细文件存放指定路径不存在, 创建成功!");
				}else{
					log.error("退费明细文件存放指定路径不存在, 创建失败!");
					return;
				}
			}
			if (filename!= null && filename.length() != 0 && !filename.equals("none")) {
				// 获取文件到指定路径
				fu.get(filename, localpath);
			}

			log.info("ftp获取退费明细文件处理结果" + localpath + File.separator +filename);
		} catch (Exception e) {
			log.error("ftp获取退费明细文件异常,处理结果" + localpath + File.separator +filename, e);
			throw e;
		} finally {
			if (fu != null) {
				fu.bye();
			}
		}
	}
	
	/**
	 * @Title: sendResponseToTimerInvoker
	 * @Description: 通知定时监控服务器执行结果
	 * @param retCode
	 * @param retMsg
	 * @author jxd
	 * @throws Exception 
	 * @date 2013-12-12 下午5:37:52
	 */
	private void sendResponseToTimerInvoker(String retCode, String retMsg) throws Exception {
		Map<String, Object> paraMap=HfCacheUtil.getCache().getParaMap();
		Para cp = (Para)paraMap.get(CACHEKEY_TIMTERADDR);
		if( null == cp || "".equals(cp.getParaValue())) {
			log.error("定时调度器参数在参数表中没有配置");
			return;
		}
		String uri = cp.getParaValue();
		//String uri = "http://192.168.1.252:8787/hfMngBusi/timetaskfeedback/id.xml" ;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", taskRpid);
		map.put("retCode",retCode);
		map.put("retMsg", retMsg);
		CouponRestRequester crr=new CouponRestRequester();
		Map<String, Object> res = crr.sendRequestForPost(uri, map);
		log.info("收到定时调度器的相应报文"+res);
	}
	
	/**
	 * @Title: getLast2Month
	 * @Description: 获取上上个月份
	 * @return 上上个月份
	 * @author jxd
	 * @date 2013-12-24 下午3:50:31
	 */
	private static String getLast2Month(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -2);
		Date date = c.getTime();
		return new SimpleDateFormat("yyyyMM").format(date);
	}
	/**
	 * @Title: formatDate
	 * @Description: 日期转换
	 * @param time 距离1900年的天数
	 * @return YYYY-MM-DD格式的日期
	 * @author jxd
	 * @date 2013-12-25 上午11:21:48
	 */
	private static String formatDate(String time){
		long tempTimeLong = Long.valueOf(time).intValue(); // 将数字转化成long型
		long ss = (tempTimeLong - 70 * 365 - 17 - 2) * 24 * 3600 * 1000; // excel的那个数字是距离1900年的天数
																			// java 是距离1970年天数，但是明明期间只有17个闰年
		Date dss = new Date(ss); // 用这个数字初始化一个Date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dss);
	}
	
	public static void main(String[] args) {
//		String tempTime = "41529";
//		System.out.println(formatDate(tempTime));
		StlPayBackTask task = new StlPayBackTask();
		try {
			task.parseExcel("payback201310.xls");
//			task.parseExcel("payback201308.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
