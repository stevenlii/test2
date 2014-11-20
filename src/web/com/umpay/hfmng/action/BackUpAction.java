package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.service.BackUpService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/backup")
public class BackUpAction extends BaseAction {
	@Autowired
	private BackUpService backUpService;

	@Autowired
	private OptionService optionService;

	/**
	 * 跳转到商户/商品报表页面
	 * 
	 * @Title: index
	 * @Description:
	 * @return
	 * @return String
	 * @throws
	 * @author lituo
	 * @date 2013-12-25 上午11:39:44
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modelMap) {
		return "backup/index";
	}

	/**
	 * 商户/商品列表页面查询
	 * 
	 * @Title: query
	 * @Description:
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author lituo
	 * @date 2013-12-10 下午04:21:46
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		HashMap map = (HashMap) getParametersFromRequest(super.getHttpRequest());
		map.put("addUserId", getUser().getId());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, false);
				// 格式化数据
				ObjectUtil.trimData(data);
				transQueryList(data);
				long count = queryCount(queryKey, map, false);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					e.printStackTrace();
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
	 * 
	 * @Title: transQueryList
	 * @Description: 数据转换
	 * @param list
	 * @return
	 * @author lituo
	 * @date 2014-7-18 上午09:54:31
	 */
	private void transQueryList(List<Map<String, Object>> list) {
		for (Map<String, Object> m : list) {
			// 商户分类
			String mer_category = (String) m.get("MER_CATEGORY");// 商户分类
			if (mer_category != null && !"".equals(mer_category)) {
				Map<String, String> merCateGorymap = optionService.getMerCategoryMap();
				m.put("MER_CATEGORY", merCateGorymap.containsKey(mer_category) ? merCateGorymap.get(mer_category)
						: mer_category);
			}
			// 商户业务属性
			String mer_busitype = (String) m.get("MER_BUSITYPE");
			if (mer_busitype != null && !"".equals(mer_busitype)) {
				Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
				m.put("MER_BUSITYPE", busiTypeMap.get(mer_busitype));
			}
			// 商品分类
			String goods_category = (String) m.get("GOODS_CATEGORY");// 商品分类
			if (goods_category != null && !"".equals(goods_category)) {
				Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
				m.put("GOODS_CATEGORY",
						GoodsCateGorymap.containsKey(goods_category) ? GoodsCateGorymap.get(goods_category)
								: goods_category);
			}
		}
	}

	/**
	 * 
	 * @Title: deleteBackUpOper
	 * @Description: 删除商户报备信息
	 * @param checkBackupOperId
	 * @return
	 * @throws Exception
	 * @author lituo
	 * @date 2014-7-16 上午10:46:11
	 */
	@RequestMapping(value = "/deleteBackUpOper")
	public ModelAndView deleteBackUpOper(String checkBackupOperId) throws Exception {
		log.info("删除商户/商品报备信息,编号：" + checkBackupOperId);
		String str[] = checkBackupOperId.split(",");
		String condition = "";
		for (int i = 0; i < str.length; i++) {
			// 开始删除
			condition += "'" + str[i] + "',";
		}
		String userId = this.getUser().getId();
		int backupstat = 0;
		int result = backUpService.deleteBackUpOper(condition.substring(0, condition.length() - 1), userId,backupstat);
		return new ModelAndView("jsonView", "ajax_json", String.valueOf(result));
	}

	/**
	 * 
	 * @Title: submitOper
	 * @Description: 提交商户报备信息
	 * @param checkBackupOperId
	 * @return
	 * @throws Exception
	 * @author lituo
	 * @date 2014-7-16 上午10:46:26
	 */
	@RequestMapping(value = "/submitBackUpOper")
	public ModelAndView submitBackUpOper(String checkBackupOperId) throws Exception {
		log.info("删除商户/商品报备信息,编号：" + checkBackupOperId);
		String userId = this.getUser().getId();
		String str[] = checkBackupOperId.split(",");
		String condition = "";
		for (int i = 0; i < str.length; i++) {
			// 开始删除
			condition += "'" + str[i] + "',";
		}
		int backupstat = 1;
		int result = backUpService.submitBackUpOper(condition.substring(0, condition.length() - 1), userId,backupstat);
		return new ModelAndView("jsonView", "ajax_json", String.valueOf(result));
	}

}
