package com.umpay.hfmng.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.service.RestService;

@Controller
@RequestMapping("/hfrest")
public class RestBusiAction extends BaseAction{
	@Autowired
	private RestService restService;
	
	/**
	 * ********************************************
	 * method name   : list 
	 * description   : 跳转到资源层管理页面
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : lz ,  2013-4-9  下午04:07:51
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/index")
	public String list(ModelMap modeMap) throws DataAccessException {
		HfCache hfCache=HfCacheUtil.getCache();
		String opts = hfCache.getUrlAcl("hfrest");
		modeMap.addAttribute("opts", opts);
		return "hfrest/index";
	}
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 刷新资源层缓存
	 * @return       : ModelAndView
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : lz ,  2013-4-9  下午04:08:31
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/updateCache")
	public ModelAndView update(ModelMap modeMap) {
		String result = "0";
		try {
			result = restService.updateRestCache();
			log.info("更新资源层缓存成功");
		} catch (Exception e) {
			log.error("更新资源层缓存失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
}
