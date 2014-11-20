package com.umpay.hfmng.view;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.FileDirFilter;


/**
 * @ClassName: TextView
 * @Description: TODO
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-10-17
 */
public class TextView extends AbstractView {
	private static final Logger log = Logger.getLogger(ExcelCouponStatsView.class);
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.reset();
		response.setContentType("application/json;charset=UTF-8");   
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");   
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		response.setHeader("Pragma", "no-cache");
		Map<String, Object> map = (Map<String, Object>)model.get("data");
		String ruleid = (String)map.get("ruleid");
		String merName = (String)map.get("merName");
		String channel = (String)map.get("channel");
		String path = request.getSession().getServletContext().getRealPath("WEB-INF/download");
		File fileDir = new File(path);
		String[] arr = fileDir.list(new FileDirFilter(ruleid + ".txt"));
		String fileName = path + "/" + arr[0];
		String downloadfileName = getFileName(channel + "_" + merName);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(downloadfileName.getBytes("gb2312"), "iso8859-1"));
		
		OutputStream os = null;
		File file = new File(fileName);
		//
		if(file.exists()){
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			try{
				byte[] buffer = new byte[1024];
				int len;
				while((len = fis.read(buffer))>0){
					toClient.write(buffer,0,len);
				}
				toClient.flush();
				logger.info("下载文件 "+fileName+" 完成");
			}catch(Exception e){
				logger.info("下载文件 "+fileName+" 错误");
				request.getSession().setAttribute("RULE_" + ruleid, false);
				throw new Exception("下载文件 "+fileName+" 错误",e);
			}finally{
				if(null!=fis){
					fis.close();
				}
				if(null != toClient){
					toClient.close();
				}
			}
			request.getSession().setAttribute("RULE_" + ruleid, true);
		}else{
			logger.info("下载文件 "+fileName+" 错误，文件不存在");
			request.getSession().setAttribute("RULE_" + ruleid, false);
			throw new Exception("文件"+fileName+"不存在");
		}

	}
	
	/**
	 * 获取下载文件名称
	 * 
	 * @Title: getFileName
	 * @Description: 获取下载文件名称
	 * @param name
	 * @return 文件名
	 * @author panyouliang
	 * @date 2013-3-29 上午11:33:05
	 */
	private static String getFileName(String name) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder();
		//sb.append("综合业务管理平台_");
		sb.append(name);
		sb.append("_");
		sb.append(df.format(new Date()));
		sb.append(".txt");
		return sb.toString();
	}

}
