/**
 * @Title: MersetSwitchAction.java
 * @Package com.umpay.hfmng.action
 * @Description: TODO
 * @author MARCO
 * @date 2014-6-5 上午10:13:35
 * @version V1.0
 */

package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.model.MersetSwitch;
import com.umpay.hfmng.model.OmItemSelector;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.hfmng.service.MersetSwitchService;
import com.umpay.uniquery.util.JsonUtil;
import com.umpay.uniquery.util.StringUtil;

@Controller
@RequestMapping("/mersetswitch")
public class MersetSwitchAction extends BaseAction {
	@Autowired
	private MerInfoService merInfoService;
	@Autowired
	private MersetSwitchService mersetSwitchService;
	/**
	  * list
	  * @Title: list
	  * @Description: 跳转至商户账单配置首页
	  * @param @param modeMap
	  * @param @return
	  * @param @throws DataAccessException    
	  * @return String 
	  * @throws
	 */
	@RequestMapping(value = "/index")
	public String list(ModelMap modeMap) throws DataAccessException {
		return "mersetswitch/index";
	}
	
	/**
	  * query
	  * @Title: query
	  * @Description: 查询商户账单配置
	  * @param @return    
	  * @return ModelAndView 
	  * @throws
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query()  {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				ObjectUtil.trimData(data);// 格式化数据
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
	
	/**
	  * toconfig
	  * @Title: toconfig
	  * @Description: 跳转至配置页
	  * @param @param modeMap
	  * @param @return    
	  * @return String 
	  * @throws
	 */
	@RequestMapping(value = "/toconfig")
	public String toconfig(ModelMap modeMap) {
		return "mersetswitch/config";
	}
	
	/**
	  * config
	  * @Title: config
	  * @Description: 新增配置或者修改商户账单配置
	  * @param @param modeMap
	  * @param @return    
	  * @return String 
	  * @throws
	 */
	@RequestMapping(value = "/config")
	public ModelAndView config(ModelMap modeMap) {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String merid = (String) map.get("merid");
		StringBuilder sb = new StringBuilder("{");
		try {
			mersetSwitchService.updateMersetSwitch(merid);
			sb.append("\"code\":" + "1," + "\"msg\":\"配置成功\"}");
		} catch (Exception e) {
			e.printStackTrace();
			sb.append("\"code\":" + "0," + "\"msg\":\"配置失败\"}");
		}
		return new ModelAndView("jsonView", "ajax_json", sb.toString());
	}
	
	@RequestMapping(value = "/initsource")
	public ModelAndView initsource() throws Exception{
		List<MerInfo> list = merInfoService.loadAll();
		List<OmItemSelector> source = new ArrayList<OmItemSelector>();
		List<MersetSwitch> list1 = mersetSwitchService.mersetValues();
		for(MerInfo info : list){
			OmItemSelector o = new OmItemSelector();
			if(list1 != null){
				for(MersetSwitch swh : list1){
					if(swh.getMerid().trim().trim().equals(info.getMerId().trim())){
						o.setIschecked(1);
					}
				}
			}
			o.setText(info.getMerName().trim());
			o.setValue(info.getMerId().trim());
			source.add(o);
		}
		
		String json = JsonUtil.toJson(source);
		log.debug("json：" + json);
		return new ModelAndView("jsonView", "ajax_json", json);
	}
	
	
	@RequestMapping(value = "/filtration")
	public ModelAndView filtration(ModelMap modeMap) throws Exception{
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String merName = (String) map.get("merId");
		List<MerInfo> list = merInfoService.filtrationMerByName(merName);
		List<OmItemSelector> source = new ArrayList<OmItemSelector>();
		List<MersetSwitch> list1 = mersetSwitchService.mersetValues();
		for(MerInfo info : list){
			OmItemSelector o = new OmItemSelector();
			if(list1 != null){
				for(MersetSwitch swh : list1){
					if(swh.getMerid().trim().trim().equals(info.getMerId().trim())){
						o.setIschecked(1);
					}
				}
			}
			o.setText(info.getMerName().trim());
			o.setValue(info.getMerId().trim());
			source.add(o);
		}
		
		String json = JsonUtil.toJson(source);
		log.debug("json：" + json);
		return new ModelAndView("jsonView", "ajax_json", json);
	}
}
