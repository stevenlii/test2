package com.umpay.hfmng.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.model.Para;
import com.umpay.uniquery.util.StringUtil;

/**
 * 日志查询功能
 * <p>
 * 创建日期：2014-7-2
 * </p>
 * 
 * @version V1.0
 * @author jxd
 * @see
 */
@Controller
@RequestMapping("/log")
public class LogAction extends BaseAction {
	
/*	private static String CACHEKEY_LOGPATH_TRADE="BP_日志路径-TRADE";//缓存键——hfTradeBusi日志路径
	private static String CACHEKEY_LOGPATH_WEB="BP_日志路径-WEB";//缓存键——hfWeb日志路径
	private static String CACHEKEY_LOGPATH_REST="BP_日志路径-REST";//缓存键——hfRestBusi日志路径
	private static String CACHEKEY_LOGPATH_HFMNG="BP_日志路径-HFMNG";//缓存键——hfMngBusi日志路径
*/
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		return "log/index";
	}

	@RequestMapping(value = "/query")
	public ModelAndView query(String mobile, String logType) {
		log.info("日志查询开始，条件：[mobile-" + mobile + ", logType-" + logType +"]");
		if (StringUtil.isEmpty(mobile)) {
			return new ModelAndView("jsonView", "ajax_json", "请输入手机号");
		}
		if (StringUtil.isEmpty(logType)) {
			return new ModelAndView("jsonView", "ajax_json", "请选择系统");
		}
		Map<String, Object> paraMap=HfCacheUtil.getCache().getParaMap();
		String cacheKey = "BP_日志路径-" + logType.trim();
		Para cp = (Para)paraMap.get(cacheKey);
		if( null == cp || "".equals(cp.getParaValue())) {
			log.error("日志路径在参数表中没有配置");
			return new ModelAndView("jsonView", "ajax_json", "请在管理平台业务参数管理中配置日志路径，并刷新缓存，" + cacheKey);
		}
		String path = cp.getParaValue();
		if (StringUtil.isEmpty(path)) {
			log.error("日志路径在参数表中配置为空值");
			return new ModelAndView("jsonView", "ajax_json", "请在管理平台业务参数管理中配置日志路径，并刷新缓存，" + cacheKey);
		}
		String command = "grep " + mobile + " mpsp.log*";
		if("WEB".equalsIgnoreCase(logType)){
			command = "grep " + mobile + " mpsp_hfweb.log*";
		}
		List<String> cmdlist = new ArrayList<String>();
		cmdlist.add("sh");
		cmdlist.add("-c");
		cmdlist.add(command);
		StringBuffer temp = new StringBuffer();
		for (String cmd : cmdlist) {
			temp.append(cmd).append(" ");
		}
		log.info("执行命令：" + temp.toString());
		String result = exceCommand_table(path, cmdlist);
		log.info("日志查询结束，条件：[mobile-" + mobile + ", logType-" + logType +"]");
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	/**
	 * @Title: plat
	 * @Description: 进入平台（综合业务管理平台）日志查询主页
	 * @return
	 * @author jxd
	 * @date 2014-7-3 下午3:32:14
	 */
	@RequestMapping(value = "/index_plat")
	public String index_plat() {
		return "log/index_plat";
	}
	
	/**
	 * @Title: query_plat
	 * @Description: 平台（综合业务管理平台）日志查询
	 * @param key
	 * @param logType
	 * @return
	 * @author jxd
	 * @date 2014-7-3 下午3:34:25
	 */
	@RequestMapping(value = "/query_plat")
	public ModelAndView query_plat(String key, String logType) {
		log.info("日志查询开始，条件：[key-" + key + ", logType-" + logType +"]");
		if (StringUtil.isEmpty(logType)) {
			return new ModelAndView("jsonView", "ajax_json", "请选择系统");
		}
		Map<String, Object> paraMap=HfCacheUtil.getCache().getParaMap();
		String cacheKey = "BP_日志路径-" + logType.trim();
		Para cp = (Para)paraMap.get(cacheKey);
		if( null == cp || "".equals(cp.getParaValue())) {
			log.error("日志路径在参数表中没有配置");
			return new ModelAndView("jsonView", "ajax_json", "请在管理平台业务参数管理中配置日志路径，并刷新缓存，" + cacheKey);
		}
		String path = cp.getParaValue();
		if (StringUtil.isEmpty(path)) {
			log.error("日志路径在参数表中配置为空值");
			return new ModelAndView("jsonView", "ajax_json", "请在管理平台业务参数管理中配置日志路径，并刷新缓存，" + cacheKey);
		}
		String command = "";
		if ("HFMNG".equalsIgnoreCase(logType)){
			command = "tail -200 hfmng.log";
		}
		
		List<String> cmdlist = new ArrayList<String>();
		cmdlist.add("sh");
		cmdlist.add("-c");
		cmdlist.add(command);
		StringBuffer temp = new StringBuffer();
		for (String cmd : cmdlist) {
			temp.append(cmd).append(" ");
		}
		log.info("执行命令：" + temp.toString());
		String result = exceCommand(path, cmdlist);
		log.info("日志查询结束，条件：[key-" + key + ", logType-" + logType +"]");
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * @Title: exceCommand
	 * @Description: 执行Shell命令
	 * @param path 进程工作目录
	 * @param cmdlist 命令参数
	 * @return 执行结果，TABLE显示
	 * @author jxd
	 * @date 2014-7-3 下午3:41:57
	 */
	private String exceCommand_table(String path, List<String> cmdlist) {
		ProcessBuilder ps = new ProcessBuilder(cmdlist);
		BufferedReader bufferedReader = null;
		StringBuffer stringBuffer = new StringBuffer("<table  class=\"altrowstable\" id=\"alternatecolor\" >");
		
		try {
			ps.directory(new File(path.trim()));
			Process process = ps.start();
			if (process != null) {
				LineNumberReader br = new LineNumberReader(new InputStreamReader(
						process.getInputStream()));
				String line = null;
				// 读取Shell的输出内容，并添加到stringBuffer中
				int i = 0;
				while ((line = br.readLine()) != null) {
					stringBuffer.append("<tr ");
					if(++i % 2 == 0){
						stringBuffer.append(" class=\"oddrowcolor\" >");
					}else{
						stringBuffer.append(" class=\"evenrowcolor\" >");
					}
					String[] strs = line.split(",");
					for (String str : strs) {
						stringBuffer.append("<td>").append((StringUtil.isEmpty(str)||"null".equals(str))?" ":str).append("</td>");
					}
					stringBuffer.append("</tr>");
				}
				if(i == 0){
					stringBuffer.append("<tr class=\"oddrowcolor\" ><td>无相关日志</td></tr>");
				}
			} else {
				stringBuffer.append("<tr class=\"oddrowcolor\" ><td>没有Shell命令执行进程</td></tr>");
			}
		} catch (IOException e) {
			log.error("执行Shell命令时发生异常", e);
			stringBuffer.append("<tr class=\"oddrowcolor\" ><td>执行Shell命令时发生异常：").append(e.getMessage()).append("</td></tr>");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) { }
			}
		}
		stringBuffer.append("</table>");
		return stringBuffer.toString();
	}
	
	/**
	 * @Title: exceCommand
	 * @Description: 执行Shell命令
	 * @param path 进程工作目录
	 * @param cmdlist 命令参数
	 * @return 执行结果
	 * @author jxd
	 * @date 2014-7-3 下午3:41:57
	 */
	private String exceCommand(String path, List<String> cmdlist) {
		ProcessBuilder ps = new ProcessBuilder(cmdlist);
		BufferedReader bufferedReader = null;
		StringBuffer stringBuffer = new StringBuffer();
		
		try {
			ps.directory(new File(path.trim()));
			Process process = ps.start();
			if (process != null) {
				LineNumberReader br = new LineNumberReader(new InputStreamReader(
						process.getInputStream()));
				String line = null;
				// 读取Shell的输出内容，并添加到stringBuffer中
				while ((line = br.readLine()) != null) {
					stringBuffer.append(line).append("<br/>");
				}
				if(stringBuffer.length() == 0){
					stringBuffer.append("无相关日志<br/>");
				}
			} else {
				stringBuffer.append("没有Shell命令执行进程<br/>");
			}
		} catch (IOException e) {
			log.error("执行Shell命令时发生异常", e);
			stringBuffer.append("执行Shell命令时发生异常：<br/>").append(e.getMessage()).append("<br/>");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) { }
			}
		}
		return stringBuffer.toString();
	}
}
