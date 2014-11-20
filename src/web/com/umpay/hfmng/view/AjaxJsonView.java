package com.umpay.hfmng.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

public class AjaxJsonView extends AbstractView{
	private static final Logger log = Logger.getLogger(AjaxJsonView.class);
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.setContentType("application/json;charset=UTF-8");   
		response.setContentType("text/html;charset=UTF-8");
		// Set standard HTTP/1.1 no-cache headers.   
		response.setHeader("Cache-Control",   
		        "no-store, max-age=0, no-cache, must-revalidate");   
		   
		 //Set IE extended HTTP/1.1 no-cache headers.   
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		   
		 //Set standard HTTP/1.0 no-cache header.   
		response.setHeader("Pragma", "no-cache");   
		String json = (String)model.get("ajax_json");    
		log.debug("--------"+json);
//		String jsonStr = "";
//		if("true".equals(json.get("manageFlag"))&& "true".equals(json.get("manageError"))){
//			jsonStr = (String)json.get("message");
//		}else{
//			jsonStr = json.toJSONString();
//		}
		response.getWriter().write(json);  
		
	}
	
}