package com.umpay.hfmng.action;


import java.util.List;
import java.util.Map;


import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
/**
 * 统一查询处理类
 */
public class UniQueryAction extends BaseAction{
	@RequestMapping(value = "/urp/uniquery")
	public ModelAndView execute() throws DataAccessException{
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String)map.get("queryKey");
		String msg = null;
		if(queryKey != null){
			 try{
				 IUniQueryService service = (IUniQueryService)SpringContextUtil.getBean("uniQueryService");
				 String startStr = super.getHttpRequest().getParameter("start");
		         String limitStr = super.getHttpRequest().getParameter("limit");
		         int start = startStr==null?0:Integer.parseInt(startStr);
		         int limit = limitStr==null?10:Integer.parseInt(limitStr);
		        // System.out.print("start--"+start);
		         List<Map<String,Object>> data = service.queryPageList(queryKey, map, start, limit);
		         long count = service.queryCount(queryKey, map);
		         msg = JsonUtil.toJson(count, data);
			 }catch(Exception e){
				 try {
					msg = JsonUtil.jsonError("-1", "查询失败"+e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			 }
		}else{
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json："+msg);
		return new ModelAndView("jsonView","ajax_json",msg);
	}
	

}
