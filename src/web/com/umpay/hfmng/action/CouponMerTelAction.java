package com.umpay.hfmng.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponMerTel;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.CouponMerTelService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: CouponMerTelAction
 * @Description: 电子兑换券商户兑换电话管理
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-6-3
 */
@Controller
@RequestMapping("/couponmertel")
public class CouponMerTelAction extends BaseAction {

	@Autowired
	private CouponMerTelService couponMerTelService;

	protected final static Logger log = LoggerFactory.getLogger(CouponMerTelAction.class);

	/**
	 * @Title: list
	 * @Description: 链接到商户兑换电话管理页面
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午3:25:07
	 */
	@RequestMapping(value = "/index")
	public String list() {
		return "couponmertel/index";
	}

	/**
	 * @Title: add
	 * @Description: 链接到添加兑换电话信息页面
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:07:07
	 */
	@RequestMapping(value = "/add")
	public String add() {
		return "couponmertel/addcouponmertel";
	}

	/**
	 * @Title: modify
	 * @Description: 链接到修改兑换电话信息页面
	 * @return
	 * @author wanyong
	 * @date 2013-6-6 上午11:41:39
	 */
	@RequestMapping(value = "/modify")
	public String modify(String merTelId, ModelMap modelMap) {
		CouponMerTel couponMerTel = couponMerTelService.loadCouponMerTel(merTelId);
		// 渲染商户
		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		if (null != couponMerTel && null != couponMerTel.getMerId()) {
			MerInfo merInfo = (MerInfo) merInfoMap.get(couponMerTel.getMerId());
			couponMerTel.setMerName(null == merInfo ? null : merInfo.getMerName());
		}
		modelMap.addAttribute("couponMerTel", couponMerTel);
		return "couponmertel/modifycouponmertel";
	}

	/**
	 * @Title: query
	 * @Description: 查询兑换电话信息
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:02:36
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染商户
					Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
					if (null != subMap.get("MERID")) {
						MerInfo merInfo = (MerInfo) merInfoMap.get(subMap.get("MERID").toString().trim());
						subMap.put("MERNAME", null == merInfo ? null : merInfo.getMerName());
					}
					// 渲染兑换方式
					if (null != subMap.get("EXCHANGETYPE")) {
						subMap.put("EXCHANGETYPENAME",
								ParameterPool.couponCodeExchangeTypes.get(subMap.get("EXCHANGETYPE").toString().trim()));
					}
					// 渲染启用状态
					if (null != subMap.get("STATE"))
						subMap.put("STATENAME",
								ParameterPool.couponMerTelStates.get(subMap.get("STATE").toString().trim()));
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					log.error(e1.getMessage());
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: save
	 * @Description: 保存商户兑换电话信息
	 * @param couponMerTel
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:17:37
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(CouponMerTel couponMerTel, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponMerTel.setMerTelId("MT" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_COUPONINF, 8));
			couponMerTelService.saveCouponMerTel(couponMerTel);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			log.error("添加商户兑换电话信息时发生未知异常：【" + e.getMessage() + "】", e);
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: update
	 * @Description: 修改商户兑换电话信息
	 * @param couponMerTel
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-6-6 上午11:42:30
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(CouponMerTel couponMerTel, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponMerTelService.modifyCouponMerTel(couponMerTel);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			log.error("修改商户兑换电话信息时发生未知异常：【" + e.getMessage() + "】", e);
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
}
