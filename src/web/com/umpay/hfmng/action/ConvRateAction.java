package com.umpay.hfmng.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.service.ConvRateService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * ******************  类说明  ******************
 * class       :  ConvRateAction
 * date        :  2014-1-14 
 * @author     :  LiZhen
 * @version    :  V1.0  
 * description :  转化率基础数据管理
 * @see        :                         
 * **********************************************
 */
@Controller
@RequestMapping("/convrate")
public class ConvRateAction extends BaseAction {
	@Autowired
	private ConvRateService convRateService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) throws DataAccessException {
		return "convrate/index";
	}
	
	/**
	 * *****************  方法说明  *****************
	 * method name   :  query
	 * @param		 :  @return
	 * @return		 :  ModelAndView
	 * @author       :  LiZhen 2014-1-14 上午10:09:51
	 * description   :  生成时间列表，模拟分页查询
	 * @see          :  
	 * **********************************************
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query()  {
		String from = getHttpRequest().getParameter("from");
		String to = getHttpRequest().getParameter("to");		
		String startStr = getHttpRequest().getParameter("start");
		String limitStr = getHttpRequest().getParameter("limit");
		int start = startStr == null ? 0 : Integer.parseInt(startStr);
		int limit = limitStr == null ? 15 : Integer.parseInt(limitStr);
		Date fromDate=null;
		Date toDate=new Date();
		long count=15;
		Calendar cal=Calendar.getInstance(); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(from!=null && !from.equals("") && to!=null && !to.equals("")){
			try {
				fromDate=df.parse(from);
				toDate=df.parse(to);
			} catch (ParseException e) {
				log.error("日期格式错误", e);
			}
			count=1+(toDate.getTime()-fromDate.getTime())/(24*60*60*1000);
		}
		
		List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
		String tmp="";
		int tmpDays=0;
		cal.setTime(toDate);
		if(start>0){
			cal.add(Calendar.DAY_OF_MONTH, -start);
		}
		while(!tmp.equals(from) && tmpDays!=limit){
			tmp=df.format(cal.getTime());
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("DATE", tmp);
			data.add(map);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			tmpDays++;
		}
		
		String msg = null;
		try {
			msg = JsonUtil.toJson(count, data);
		} catch (Exception e) {
			try {
				msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	/**
	 * *****************  方法说明  *****************
	 * method name   :  expConvRate
	 * @param		 :  @param flag 0表示汇总文件，1表示明细文件
	 * @param		 :  @param date 格式为2014-01-14
	 * @param		 :  @param request
	 * @param		 :  @param response
	 * @param		 :  @return
	 * @return		 :  ModelAndView
	 * @author       :  LiZhen 2014-1-14 上午10:21:42
	 * description   :  下载转化率汇总或明细文件
	 * @see          :  
	 * **********************************************
	 */
	@RequestMapping(value = "/downloadConvRate")
	public ModelAndView downloadConvRate(String flag,String date,HttpServletRequest request,HttpServletResponse response){
		if(flag==null || date==null)
			return new ModelAndView("jsonView", "ajax_json", "参数不正确");
		
		File file = null;
		String msg = "";
		date=date.replace("-", "");
		try {
			file = convRateService.getDownloadFile(flag,date);
			log.info("获取下载文件成功");
		} catch (FileNotFoundException e) {
			msg = "系统找不到指定的文件，可能还未生成最新文件";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		} catch (Exception e) {
			msg = "获取下载文件失败";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		
		try {
			if (file != null && file.exists()) {
				String fileName="";
				fileName = file.getName();
				log.info("开始下载转化率文件：" + fileName);
				response.reset();
				response.setContentType("application/x-msdownload;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
				InputStream inStream = new FileInputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
				// 循环取出流中的数据
				byte[] b = new byte[inStream.available()];
				while (inStream.read(b) != -1)
					bos.write(b, 0, b.length);
				bos.flush();
				bos.close();
				inStream.close();
				log.info("下载转化率文件[" + fileName + "]成功");
			} else {
				log.info("下载失败，目录下没有文件");
				return new ModelAndView("jsonView", "ajax_json", "下载失败，目录下没有文件");
			}
		} catch (Exception e) {
			msg = "下载文件失败,请稍后再试";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		return null;
	}
}
