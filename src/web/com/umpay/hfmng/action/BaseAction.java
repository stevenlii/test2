package com.umpay.hfmng.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.Option;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.XMLUtil;
import com.umpay.sso.org.User;
import com.umpay.sso.ws.WsPermissionClient;
import com.umpay.uniquery.IUniQueryService;
/**
 * ******************  类说明  *********************
 * class       :  BaseAction
 * @author     :  anshuqiang
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ***********************************************
 */
public abstract class BaseAction {
	protected Logger log = Logger.getLogger(this.getClass());
	protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	public HttpServletRequest getHttpRequest(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
		
	public HttpSession getHttpSession(){
		return getHttpRequest().getSession();
	}
	/**
	 * ********************************************
	 * method name   : getUser 
	 * description   : 从session中获取user用户
	 * @return       : User
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-8-27  下午02:28:53
	 * @see          : 
	 * *******************************************
	 */
	public User getUser(){
		return LoginUtil.getUser();
	}
	/**
	 * ********************************************
	 * method name   : getOptions 
	 * description   : 获取页面按钮的权限code
	 * @return       : String
	 * @param        : @param XMLName
	 * @param        : @return 有权限的code的连接字符串，如"001,002,005"
	 * modified      : lz ,  2012-9-4  上午10:30:16
	 * @see          : 
	 * *******************************************
	 */
	public String getOptions(String XMLName){
		XMLUtil<Option> x = new XMLUtil<Option>();
		Option option = new Option();
		String opts="";
		String path="/urlacl/"+XMLName+".xml";
		Resource res = resourcePatternResolver.getResource(path);
		//path=this.getClass().getClassLoader().getResource(path).getPath();
		try {
			path = res.getFile().getAbsolutePath();
			List<Option> list=x.readXML(path,option);
			String userid=getUser().getId();
			for (Option opt : list) {
				boolean perm=WsPermissionClient.hasPermission(userid, opt.getUrl());
				if(perm==true)
					opts+=opt.getCode()+",";
				else
					continue;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opts.equals("")?"":opts.substring(0, opts.length()-1);
	}
	/**
	 * ********************************************
	 * method name   : getOptionList 
	 * description   : 获取页面按钮的权限code
	 * @return       : List<Option>
	 * @param        : @param XMLName
	 * @param        : @return 返回按钮对象的集合，对某个按钮有权限则该对象的per属性为true
	 * modified      : lz ,  2012-9-3  下午5:12:25
	 * @see          : 
	 * *******************************************
	 */
	public List<Option> getOptionList(String XMLName){
		XMLUtil<Option> x = new XMLUtil<Option>();
		Option option = new Option();
		List<Option> list=new ArrayList<Option>();
		String path="urlacl\\"+XMLName+".xml";
		//path=Thread.currentThread().getContextClassLoader().getResource(path).getPath();
		path=this.getClass().getClassLoader().getResource(path).getPath();
		try {
			path=URLDecoder.decode(path, "utf-8");
			list=x.readXML(path,option);
			for (Option opt : list) {
				boolean perm=WsPermissionClient.hasPermission(getUser().getId(), opt.getUrl());
				opt.setPerm((perm==true)?"true":"false");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * ********************************************
	 * method name   : queryPageList 
	 * description   : 统一查询中用来分页查询
	 * @return       : List<Map<String,Object>>
	 * @param        : @param queryKey
	 * @param        : @param map
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-8-30  上午11:30:23
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String, Object>> queryPageList(String queryKey, Map map) {
		return queryPageList(queryKey, map,false);
	}
	/**
	 * ********************************************
	 * method name   : queryPageList 
	 * description   : 统一查询中用来分页查询
	 * @return       : List<Map<String,Object>>
	 * @param        : @param queryKey
	 * @param        : @param map
	 * @param        : @param isOffline 为true时连接离线库，否则连接在线库
	 * @param        : @return
	 * modified      : lz ,  2013-3-7  上午11:44:13
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String, Object>> queryPageList(String queryKey, Map map,boolean isOffline) {
		String beanId="uniQueryService";
		if(isOffline){
			beanId="uniQueryServiceOffline";
		}
		IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean(beanId);
		String startStr = getHttpRequest().getParameter("start");
		String limitStr = getHttpRequest().getParameter("limit");
		int start = startStr == null ? 0 : Integer.parseInt(startStr);
		int limit = limitStr == null ? 10 : Integer.parseInt(limitStr);
		List<Map<String, Object>> data = service.queryPageList(queryKey, map,
				start, limit);
		return data;
	}
	/**
	 * ********************************************
	 * method name   : query 
	 * description   : 统一查询中无分页查询
	 * @return       : List<Map<String,Object>>
	 * @param        : @param queryKey
	 * @param        : @param map
	 * @param        : @param isOffline
	 * @param        : @return
	 * modified      : lz ,  2013-3-7  下午03:33:54
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String, Object>> query(String queryKey, Map map,boolean isOffline) {
		String beanId="uniQueryService";
		if(isOffline){
			beanId="uniQueryServiceOffline";
		}
		IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean(beanId);
		List<Map<String, Object>> data = service.query(queryKey, map);
		return data;
	}
 /**
  * ********************************************
  * method name   : queryCount 
  * description   :  数量查询
  * @return       : long
  * @param        : @param queryKey
  * @param        : @param map
  * @param        : @return
  * modified      : zhaojunbao ,  2012-8-30  上午11:33:56
  * @see          : 
  * *******************************************
  */
	public long queryCount(String queryKey, Map map) {
		return queryCount(queryKey, map,false);
	}
	/**
	 * ********************************************
	 * method name   : queryCount 
	 * description   : 
	 * @return       : long
	 * @param        : @param queryKey
	 * @param        : @param map
	 * @param        : @param isOffline 为true时连接离线库，否则连接在线库
	 * @param        : @return
	 * modified      : lz ,  2013-3-7  上午11:47:03
	 * @see          : 
	 * *******************************************
	 */
	public long queryCount(String queryKey, Map map,boolean isOffline) {
		String beanId="uniQueryService";
		if(isOffline){
			beanId="uniQueryServiceOffline";
		}
		IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean(beanId);
		long count = service.queryCount(queryKey, map);
		return count;
	}
	
		/**
		 * ********************************************
		 * method name   : getParametersFromRequest 
		 * description   :  获取请求参数
		 * @return       : Map<String,String>
		 * @param        : @param req
		 * @param        : @return
		 * modified      : zhaojunbao ,  2012-8-30  上午11:17:46
		 * @see          : 
		 * *******************************************
		 */
		
	public Map<String, String> getParametersFromRequest(HttpServletRequest req) {
		Enumeration names = req.getParameterNames();
		Map<String, String> map = new HashMap();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			Object value = req.getParameter(name);
			if (value != null) {
				String valueStr = value.toString();
				if (value instanceof String[]) {
					valueStr = ((String[]) value)[0];
					log.debug("===== " + name + " : " + valueStr);
				}
				map.put(name, valueStr);
			}
		}
		log.debug("" + map);
		return map;
	}
}
