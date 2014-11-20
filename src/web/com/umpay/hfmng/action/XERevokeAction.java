package com.umpay.hfmng.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.service.XERevokeService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: XERevokeAction
 * @Description: 对小额对账结果中需要解冻冲正的交易向trade发起请求
 * @author chenwei
 * @date   2013-07-08 新增对北京批量解冻的支持
 * @modify 2013-08-27 增加对云南批量解冻的支持
 */
@Controller
@RequestMapping("/xerevoke")
public class XERevokeAction extends BaseAction {
	@Autowired
	private XERevokeService XERevokeService;
	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		return "xerevoke/index";
	}

	@RequestMapping(value = "/bjhbindex")
	public String bjhbindex(ModelMap modeMap) {
		return "xerevoke/bjhbindex";
	}

	@RequestMapping(value = "/batchRevokeFromFile")
	public ModelAndView batchRevokeFromFile(HttpServletRequest request) throws DataAccessException, IOException{
        String result = "";//操作结果提示信息
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		// 获得文件：
        MultipartFile file = req.getFile("file");
        String batchProv = request.getParameter("batchProv");
        if (null == batchProv) {
        	return new ModelAndView("jsonView", "ajax_json", "未成功获取解冻省份值！");
		}
        if (file.getOriginalFilename().endsWith(".txt") || file.getOriginalFilename().endsWith(".TXT")) {
    		result = XERevokeService.batchRevokeFromFile(file, batchProv);
    		return new ModelAndView("jsonView", "ajax_json", result);
		} else {
			return new ModelAndView("jsonView", "ajax_json", "文件格式不合法，请重新选择！");
		}
	}
	
	@RequestMapping(value = "/fileDownload")
	public String fileDownload(ModelMap modeMap) {
		return "xerevoke/fileDownload";
	}
	
	@RequestMapping(value = "/bjhbfileDownload")
	public String bjhbfileDownload(ModelMap modeMap) {
		return "xerevoke/bjhbfileDownload";
	}
	
	@RequestMapping(value = "/queryDownload")
	public ModelAndView queryDownload(ModelMap modeMap) {
		String fileURL = ObjectUtil.trim(messageService.getSystemParam("XEREVOKE.DownloadFilePath"));
		log.info("目录地址：" + fileURL);
		File file = new File(fileURL);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		data = XERevokeService.listFile(data, file, "", false);
		long n = data.size();
		String jsonMsg = null;
		try {
			jsonMsg = JsonUtil.toJson(n, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", jsonMsg);
	}
	
	@RequestMapping(value = "/bjhbqueryDownload")
	public ModelAndView bjhbqueryDownload(ModelMap modeMap) {
		String fileURL = ObjectUtil.trim(messageService.getSystemParam("BJHBREVOKE.DownloadFilePath"));
		log.info("目录地址：" + fileURL);
		File file = new File(fileURL);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		data = XERevokeService.listFile(data, file, "", false);
		long n = data.size();
		String jsonMsg = null;
		try {
			jsonMsg = JsonUtil.toJson(n, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", jsonMsg);
	}
	
	@RequestMapping(value = "/downloadDoc")
	public ModelAndView downloadDoc(String fileName, HttpServletRequest request, HttpServletResponse response){
		File file = null;
		String msg = "";
		try {
			file = XERevokeService.getDownloadFile(fileName, "BJ");
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
//				response.setHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
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
			}
		} catch (Exception e) {
			msg = "下载文件失败,请稍后再试";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		return null;
	}
	
	@RequestMapping(value = "/bjhbdownloadDoc")
	public ModelAndView bjhbdownloadDoc(String fileName, HttpServletRequest request, HttpServletResponse response){
		File file = null;
		String msg = "";
		try {
			file = XERevokeService.getDownloadFile(fileName, "BJHB");
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
//				response.setHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
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
			}
		} catch (Exception e) {
			msg = "下载文件失败,请稍后再试";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		return null;
	}
}