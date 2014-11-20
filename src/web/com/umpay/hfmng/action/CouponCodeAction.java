package com.umpay.hfmng.action;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.HfCodec;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.ChannelInf;
import com.umpay.hfmng.model.CouponCode;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.CouponCodeService;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: CouponCodeAction
 * @Description: 兑换码管理
 * @author wanyong
 * @date 2013-1-4 下午01:58:51
 */
@Controller
@RequestMapping("/couponcode")
public class CouponCodeAction extends BaseAction {

	@Autowired
	private CouponCodeService couponCodeService;

	@Autowired
	private CouponLogService couponLogService;

	/**
	 * @Title: list
	 * @Description: 链接到兑换码管理页面
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 下午02:01:16
	 */
	@RequestMapping(value = "/index")
	public String list() {
		return "couponcode/index";
	}

	/**
	 * @Title: query
	 * @Description: 查询兑换码信息
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 下午05:23:21
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		if (map.get("couponCode") != null && !"".equals(map.get("couponCode").toString()))
			// 加密查询条件中的兑换码
			map.put("couponCode", HfCodec.encrypt(map.get("couponCode").toString()));
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				if (data != null && data.size() > 0) {
					// 渲染数据
					Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
					Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
					Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
					for (Map<String, Object> map2 : data) {
						// 解密兑换码
						map2.put("COUPONCODE", HfCodec.decrypt(map2.get("COUPONCODE").toString()));
						// 根据ID从缓存中取到名称
						String couponId = map2.get("COUPONID").toString();
						String merId = map2.get("MERID").toString();
						String goodsId = map2.get("MERID").toString() + "-" + map2.get("GOODSID").toString();
						CouponInf couponInf = (CouponInf) couponInfMap.get(couponId);
						map2.put("COUPONNAME", null == couponInf ? null : couponInf.getCouponName());

						MerInfo merInfo = (MerInfo) merInfoMap.get(merId);
						map2.put("MERNAME", null == merInfo ? null : merInfo.getMerName());

						GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(goodsId);
						map2.put("GOODSNAME", null == goodsInfo ? null : goodsInfo.getGoodsName());
						// 渲染兑换码生成方式
						if (map2.get("GENERATEMETHOD") != null) {
							map2.put("GENERATEMETHODNAME",
									ParameterPool.couponCodeMethod.get(map2.get("GENERATEMETHOD").toString()));
						}
						// 渲染兑换码状态
						map2.put("STATENAME", ParameterPool.couponCodeStates.get(map2.get("STATE").toString()));
						// 渲染兑换时间
						if (!Const.COUPON_CODESTATE_USE.equals(Integer.parseInt(map2.get("STATE").toString().trim())))
							// 该码未使用
							map2.put("EXCHANGETIME", "--");
					}
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
					e.printStackTrace();
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
	 * @Title: export
	 * @Description: 兑换码导出
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-5 上午11:56:19
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		if (map.get("couponCode") != null && !"".equals(map.get("couponCode").toString()))
			// 加密查询条件中的兑换码
			map.put("couponCode", HfCodec.encrypt(map.get("couponCode").toString()));
		String queryKey = map.get("queryKey").toString();
		List<Map<String, Object>> mapList = null;
		if (queryKey != null) {
			// 这里不分页查询
			IUniQueryService uniQueryService = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			mapList = uniQueryService.query(queryKey, map);
		}
		return new ModelAndView("couponCodeExcelView", "excel", mapList);
	}

	/**
	 * @Title: detail
	 * @Description: 链接到兑换码详情页面
	 * @param merId
	 *            商户ID
	 * @param couponCode
	 *            未加密的兑换码
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2013-5-31 下午4:20:00
	 */
	@RequestMapping(value = "/detail")
	public String detail(String merId, String couponCode, ModelMap modelMap) {
		// 加密兑换码
		couponCode = HfCodec.encrypt(couponCode);
		CouponCode couponCode2 = couponCodeService.loadCouponCode(couponCode, merId);
		// 数据渲染
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = (CouponInf) couponInfMap.get(couponCode2.getCouponId());
		couponCode2.setCouponName(null == couponInf ? null : couponInf.getCouponName());

		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = (MerInfo) merInfoMap.get(couponCode2.getMerId());
		couponCode2.setMerName(null == merInfo ? null : merInfo.getMerName());

		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(couponCode2.getMerId() + "-" + couponCode2.getGoodsId());
		couponCode2.setGoodsName(null == goodsInfo || null == merInfo ? null : goodsInfo.getGoodsName());

		Map<String, Object> channelMap = HfCacheUtil.getCache().getChannelInfMap();
		ChannelInf channelInf = (ChannelInf) channelMap.get(couponCode2.getChannelId());
		couponCode2.setChannelName(null == channelInf ? null : channelInf.getChannelName());

		couponCode2.setCouponCode(HfCodec.decrypt(couponCode2.getCouponCode())); // 解密兑换码
		couponCode2.setGenerateMethodName(ParameterPool.couponCodeMethod.get(couponCode2.getGenerateMethod() + "")); // 兑换码生成方式
		couponCode2.setStateName(ParameterPool.couponCodeStates.get(couponCode2.getState() + "")); // 兑换码状态
		couponCode2.setExchangeTypeName(ParameterPool.couponCodeExchangeTypes.get(couponCode2.getExchangeType() + "")); // 兑换码兑换方式
		// 渲染价格：从分变成元
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		couponCode2.setPriceName(df.format(couponCode2.getPrice() / 100.0));
		if (null == couponCode2.getBatchId() || "auto".equals(couponCode2.getBatchId()))
			couponCode2.setBatchId("--");

		modelMap.addAttribute("couponCode", couponCode2);
		return "couponcode/detailcouponcode";
	}

	/**
	 * @Title: modify
	 * @Description: 链接到兑换码修改页面
	 * @return
	 * @author wanyong
	 * @date 2013-5-31 上午11:57:44
	 */
	@RequestMapping(value = "/modify")
	public String modify(String merId, String couponCode, ModelMap modelMap) {
		// 加密兑换码
		couponCode = HfCodec.encrypt(couponCode);
		CouponCode couponCode2 = couponCodeService.loadCouponCode(couponCode, merId);
		// 数据渲染
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = (CouponInf) couponInfMap.get(couponCode2.getCouponId());
		couponCode2.setCouponName(null == couponInf ? null : couponInf.getCouponName());

		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = (MerInfo) merInfoMap.get(couponCode2.getMerId());
		couponCode2.setMerName(null == merInfo ? null : merInfo.getMerName());

		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(couponCode2.getMerId() + "-" + couponCode2.getGoodsId());
		couponCode2.setGoodsName(null == goodsInfo || null == merInfo ? null : goodsInfo.getGoodsName());

		Map<String, Object> channelMap = HfCacheUtil.getCache().getChannelInfMap();
		ChannelInf channelInf = (ChannelInf) channelMap.get(couponCode2.getChannelId());
		couponCode2.setChannelName(null == channelInf ? null : channelInf.getChannelName());
		couponCode2.setCouponCode(HfCodec.decrypt(couponCode2.getCouponCode())); // 解密兑换码
		modelMap.addAttribute("couponCode", couponCode2);
		return "couponcode/modifycouponcode";
	}

	/**
	 * @Title: update
	 * @Description: 修改兑换码信息
	 * @param couponCode
	 * @param operdata
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 上午10:57:34
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(CouponCode couponCode, String operdata, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponCode.trim();
			couponCode.setModTime(new Timestamp(System.currentTimeMillis())); // 设置修改时间
			couponCode.setCouponCode(HfCodec.encrypt(couponCode.getCouponCode())); // 加密兑换码
			couponCodeService.modifyCouponCode(couponCode);

			/************** start 记录日志 *****************/
			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_ORDER"); // 修改兑换码日志记录在订单操作日志中（三期需求）
			log.setOpertype(Const.LOG_OPT_MODIFY);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponCode.getOrderId()); // 订单编号
			log.setIxdata2(couponCode.getPaidMobileId()); // 手机号
			log.setIxdata3(couponCode.getCouponCode()); // 兑换码
			log.setOperdata(operdata);
			couponLogService.addLog(log);
			/************** end 记录日志 *******************/

			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
}
