package com.umpay.hfmng.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.umpay.sso.client.SSOConst;
import com.umpay.sso.org.User;


public class LoginUtil {
	/**
	 * ********************************************
	 * method name   : getUser 
	 * description   : 
	 * @return       : User
	 * @param        : @return
	 * modified      : lz ,  2012-10-24  上午11:06:18
	 * @see          : 
	 * *******************************************
	 */
	public static User getUser(){
		//HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		ServletRequestAttributes servletRequestAttributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		User user=null;
		if(servletRequestAttributes==null){
			user = new User();
			user.setName("ServletRequestAttributes不存在");
		}else{
			HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
			HttpSession session=httpServletRequest.getSession();
			user=(User)session.getAttribute(SSOConst.PORTAL_SESSION_NAME);
			if(user == null){
				user = new User();
				user.setName("未登陆用户");
			}
		}
		return user;
	}
}
