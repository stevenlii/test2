/** *****************  JAVA头文件说明  ****************
 * file name  :  BatchTaskAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-25
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.BatchTask;
import com.umpay.hfmng.service.BatchTaskService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.SegMblService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  SegMblAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  批量号段导入任务查询页面
 * ************************************************/
@Controller
@RequestMapping("/segmbl")
public class SegMblAction extends BaseAction {
	
	@Autowired
	private SegMblService segMblService;
	@Autowired
	private BatchTaskService batchTaskService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value = "/index")
	public String index() {
		return "segmbl/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> users = optionService.getAllUserIdAndName();
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> db = data.get(i);
					String creatorId = (String) db.get("creator");
					db.put("CREATOR", users.get(creatorId));
				}
				long count = queryCount(queryKey, map, true);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/downloadDoc")
	public ModelAndView downloadDoc(String fileName, HttpServletRequest request, HttpServletResponse response){
		File file = null;
		String msg = "";
		try {
			//获取下载路径
			String path = ObjectUtil.trim(messageService.getSystemParam("SegMbl.DownloadFilePath"))+fileName;
			try {
				path=URLDecoder.decode(path,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("解码路径失败", e);
				throw e;
			}			
			file = new File(path);
			log.info("获取下载文件成功");
		} catch (Exception e) {
			msg = "获取下载文件失败";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		try {
			if (file != null && file.exists()) {
				fileName = file.getName();
				log.info("开始下载文件：" + fileName);
				response.reset();
				response.setContentType("application/x-msdownload;charset=UTF-8");
				response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				InputStream inStream = new FileInputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
				// 循环取出流中的数据
				byte[] b = new byte[inStream.available()];
				while (inStream.read(b) != -1)
					bos.write(b, 0, b.length);
				bos.flush();
				bos.close();
				inStream.close();
				log.info("下载文件[" + fileName + "]成功");
			} else {
				log.info("下载失败，目录下没有文件");
				return new ModelAndView("jsonView", "ajax_json", "目标文件不存在");
			}
		} catch (Exception e) {
			msg = "下载文件失败,请稍后再试";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		return null;
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String taskId, ModelMap modeMap){
		BatchTask task=batchTaskService.load(taskId);
		Map<String, String> users = optionService.getAllUserIdAndName();
		String creatorName = users.get(task.getCreator());
		task.setCreator(creatorName);
		modeMap.addAttribute("task", task);//任务发起人
		return "segmbl/detail";
	}
	
	@RequestMapping(value = "/add")
	public String add(){
		return "segmbl/import";
	}
	
	@RequestMapping(value = "/importSegMbl")
	public ModelAndView importSegMbl(String flag,String netType,HttpServletRequest request){
		String res = "0";
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		MultipartFile file = req.getFile("file");
		try{
			if(file == null){
	        	log.info("没有获取到上传文件");
	        	return new ModelAndView("jsonView", "ajax_json", "非法操作");
	        }
	        if(!file.getOriginalFilename().endsWith(".txt")){
	        	res = "上传文件类型只支持txt文件";
	        	log.info(res);
	        	return new ModelAndView("jsonView", "ajax_json", res);
	        }
	        long size = file.getSize();
	        if(size == 0){
	        	res = "不能上传空文件";
	        	log.info(res);
	        	return new ModelAndView("jsonView", "ajax_json", res);
	        }
	        if(size > 10*1024*1024){
	        	res = "上传文件不能大于10MB";
	        	log.info(res);
	        	return new ModelAndView("jsonView", "ajax_json", res);
	        }
			res = segMblService.handleSegMbls(file,flag,netType);
        }catch (Exception e) {
        	log.error("处理上传文件失败", e);
		}
        return new ModelAndView("jsonView", "ajax_json", res);
	}
}
