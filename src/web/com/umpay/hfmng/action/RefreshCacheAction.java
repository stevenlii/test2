package com.umpay.hfmng.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.sso.SSOConfig;
import com.umpay.uniquery.util.JsonUtil;
/**
 * ******************  类说明  *********************
 * class       :  ListenerAction
 * @author     :  lz
 * @version    :  1.0  
 * description :  接收uweb发送的缓存更新命令，处理完毕则给uweb返回“success”
 * @see        :                        
 * ***********************************************
 */
@Controller
@RequestMapping("/monitor")
public class RefreshCacheAction extends BaseAction{

	@RequestMapping(value = "/receiveFromServer")
	public ModelAndView receive(HttpServletRequest request)throws Exception{
		Map<String,String> paraMap=super.getParametersFromRequest(request);
		//uweb的动作和变动的数据，如增加了一个用户，则cmd为doAddUser，data为userid|username，data的形式跟cmd有关，由uweb规定。
		String cmd=paraMap.get("cmd");//如doAddUser、saveUser、deleteUser、saveRole等等
		String data=paraMap.get("data");
		if(data!=null){
				data=URLDecoder.decode(data, "UTF-8");
		}
		//只接受来自uweb的访问
		String regIP=SSOConfig.getInstance().getServer();
		String remoteAddr=request.getRemoteAddr();
		String msg="error";
		if(regIP.equals(remoteAddr)){
			//处理请求
			msg=handleRequest(cmd,data);
		}
		//返回uweb的结果必须是json格式
		msg=JsonUtil.toJson(msg);
		return new ModelAndView("jsonView","ajax_json",msg);
	}
	//处理完毕返回
	private String handleRequest(String cmd,String data){
		HfCache hfCache=(HfCache) SpringContextUtil.getBean("HfCache");
		if(cmd != null && (cmd.equals("doAddUser") || cmd.equals("saveUser") || cmd.equals("deleteUser"))){
			//doAddUser、saveUser、deleteUser分别表示uweb增、改、删了某个用户
			//data：userId|loginName
			hfCache.clear("UserInfoCache");  //清除用户缓存
		}
		else if((cmd.equals("doAddRole") || cmd.equals("saveRole") || cmd.equals("deleteRole") || cmd.endsWith("assignUser"))){
			//doAddRole、saveRole、deleteRole分别表示uweb增、改、删了某个角色，assignUser表示为更改了为某角色分配的用户
			//data格式：roleId|roleName
			hfCache.clear("OperCache");   //清除运营权限信息缓存
		}
		else if(cmd.equals("doAddUrlAclRole") || cmd.equals("saveUrlAclRole") || cmd.equals("deleteUrlAclRole")){
			//doAddUrlAclRole、saveUrlAclRole、deleteUrlAclRole分别表示增、改、删了为某个url分配的角色
			//data格式：urlAclId|roleId,roleId,roleId
			hfCache.clear("UrlAclCache");   //清除用户按钮权限的缓存
		}
		return "success";
	}
}
