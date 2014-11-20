package com.umpay.hfmng.action;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponBatch;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.CouponRule;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.CouponBatchService;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.hfmng.service.CouponRuleService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: CouponBatchAction
 * @Description: 兑换券批次管理
 * @author wanyong
 * @date 2012-12-21 下午08:41:33
 */
@Controller
@RequestMapping("/couponbatch")
public class CouponBatchAction extends BaseAction {

	@Autowired
	private CouponBatchService couponBatchService;

	@Autowired
	private CouponLogService couponLogService;

	@Autowired
	private CouponRuleService couponRuleService;
	// 导入操作对象
	private String LOGBUSSOBJ = "umpay.T_COUPON_BATCH";
	// 日志操作对象
	private String LOGBUSSOBJ2 = "umpay.BATCHCODE";

	/**
	 * @Title: add
	 * @Description: 链接到新增批次页面
	 * @param ruleId
	 * @param couponId
	 * @param merId
	 * @param goodsId
	 * @param batchId
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2012-12-31 上午01:10:43
	 */
	@RequestMapping(value = "/add")
	public String add(String ruleId, String couponId, String merId, String goodsId, String batchId, ModelMap modelMap) {
		modelMap.addAttribute("ruleId", ruleId);
		modelMap.addAttribute("couponId", couponId);
		modelMap.addAttribute("merId", merId);
		modelMap.addAttribute("goodsId", goodsId);
		modelMap.addAttribute("batchId", batchId);
		if (batchId == null || "".equals(batchId))
			// 新批次
			return "couponbatch/addcouponbatch";
		return "couponbatch/appendcouponbatch";
	}

	/**
	 * @Title: modify
	 * @Description: 链接到修改批次页面
	 * @param
	 * @param batchId
	 * @param batchName
	 * @return
	 * @author wanyong
	 * @date 2012-12-29 下午04:28:04
	 */
	@RequestMapping(value = "/modify")
	public String modify(String batchId, String batchName, ModelMap modelMap) {
		CouponBatch couponBatch = couponBatchService.loadCouponBatch(batchId);
		modelMap.addAttribute("couponBatch", couponBatch);
		modelMap.addAttribute("batchName", batchName);
		return "couponbatch/modifycouponbatch";
	}

	/**
	 * @Title: update
	 * @Description: 更新批次
	 * @param
	 * @param couponBatch
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2012-12-29 下午05:19:50
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(CouponBatch couponBatch, String optdata, String batchName, ModelMap modelMap) {
		String msg = "0"; // 默认业务处理失败
		try {
			couponBatch.trim();
			couponBatch.setModUser(getUser().getId()); // 修改人
			couponBatch.setModTime(new Timestamp(System.currentTimeMillis())); // 修改时间
			couponBatchService.modifyCouponBatch(couponBatch);

			// 批次导入操作日志
			CouponLog log2 = new CouponLog();
			// 从兑换券规则表取出兑换券规则信息
			CouponRule couponRule = couponRuleService.load(couponBatchService.loadCouponBatch(couponBatch.getBatchId())
					.getRuleId());
			log2.setBusinessobject(LOGBUSSOBJ2);
			log2.setOpertype(Const.LOG_OPT_MODIFY);
			log2.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log2.setIxdata1(couponRule.getCouponId()); // 兑换券编号
			log2.setIxdata2(couponRule.getMerId()); // 商户ID
			log2.setIxdata3(batchName); // 商户ID
			log2.setIxdata4(couponRule.getGoodsId()); // 商品ID
			log2.setOperdata(optdata);
			couponLogService.addLog(log2);
			msg = "1"; // 成功
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: query
	 * @Description: 查询批次信息
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-22 下午03:30:16
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染兑换券状态
					subMap.put("STATENAME", ParameterPool.couponBatchStates.get(subMap.get("STATE").toString())); // 批次状态中文先显示
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
	 * @Title: importBatch
	 * @Description: 批量导入兑换码
	 * @param
	 * @param couponId
	 * @param merId
	 * @param goodsId
	 * @return
	 * @author wanyong
	 * @date 2012-12-25 上午11:00:38
	 */
	@RequestMapping(value = "/importbatch")
	public ModelAndView importBatch() {
		String msg = "0"; // 操作结果，默认为失败
		String successContent = ""; // 操作成功描述：共导入**条，成功导入**条，导入失败**条
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		CouponRule couponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr(map.get("paras"), CouponRule.class);
		ServletFileUpload upload = new ServletFileUpload();
		upload.setHeaderEncoding("UTF-8");
		InputStream stream = null;
		String errorPath = this.getHttpSession().getServletContext().getRealPath("/")
				+ ParameterPool.couponSystemBackParas.get("ERRFILEPATH");

		try {
			if (ServletFileUpload.isMultipartContent(super.getHttpRequest())) {
				FileItemIterator iter = upload.getItemIterator(super.getHttpRequest());
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (!item.isFormField()) {
						// 得到文件的扩展名(无扩展名时将得到全名)
						String ext = item.getName().substring(item.getName().lastIndexOf(".") + 1);
						// 初始化批次ID
						CouponBatch couponBatch = new CouponBatch();
						couponBatch.setBatchId(couponBatchService.getBatchId(couponRule.getRuleId()));
						if ("xls".equals(ext.toLowerCase())) {
							successContent = couponBatchService.importBatchForXls(stream, couponRule, couponBatch, "0",
									errorPath, getUser());
						} else {
							successContent = couponBatchService.importBatchForXlsx(stream, couponRule, couponBatch,
									"0", errorPath, getUser());
						}
						// 批次导入日志
						CouponLog log = new CouponLog();
						log.setBusinessobject(LOGBUSSOBJ);
						log.setOpertype(Const.LOG_OPT_CREATE);
						log.setResultdesc(Const.LOG_RES_SUCC);
						// 索引字段 用于查找
						log.setIxdata1(couponRule.getCouponId()); // 兑换券编号
						log.setIxdata2(couponRule.getMerId()); // 商户ID
						log.setIxdata4(couponRule.getGoodsId()); // 商品ID
						// content 成功*条，失败*条， errorfile 错误日志文件名
						String optData = "{succContent:'<content>',errorfile:'<filename>'}";
						if (successContent.indexOf("点击") > 0) {
							optData = optData.replace("<content>", successContent.substring(0, successContent
									.indexOf("点击")));
							optData = optData.replace("<filename>", successContent.substring(successContent
									.indexOf("error/") + 6, successContent.indexOf(".xls") + 4));
						} else {
							optData = optData.replace("<content>", successContent);
							optData = optData.replace("<filename>", "");
						}
						log.setOperdata(optData);
						log.setIxdata3("第" + couponBatch.getBatchId().substring(10) + "批次"); // 批次名称

						couponLogService.addLog(log);

						// 批次导入操作日志
						CouponLog log2 = new CouponLog();
						log2.setBusinessobject(LOGBUSSOBJ2);
						log2.setOpertype(Const.LOG_OPT_CREATE);
						log2.setResultdesc(Const.LOG_RES_SUCC);
						// 索引字段 用于查找
						log2.setIxdata1(couponRule.getCouponId()); // 兑换券编号
						log2.setIxdata2(couponRule.getMerId()); // 商户ID
						log2.setIxdata3("第" + couponBatch.getBatchId().substring(10) + "批次"); // 批次名称
						log2.setIxdata4(couponRule.getGoodsId()); // 商品ID
						log2.setOperdata("批次新建：电子兑换券有效期：" + couponRule.getUseStartDate() + "至"
								+ couponRule.getUseEndDate() + ";销售期:" + couponRule.getSellStartDate() + "至"
								+ couponRule.getSellEndDate() + ";"
								+ successContent.substring(0, successContent.indexOf("！") + 1));
						couponLogService.addLog(log2);
					}
				}
				msg = "1"; // 成功
			}
		} catch (FileUploadException uploadException) {
			uploadException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (BusinessException busiException) {
			busiException.printStackTrace();
			msg = busiException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("msg", msg);
		retMap.put("successContent", successContent);
		return new ModelAndView("jsonView", "ajax_json", JSONObject.fromObject(retMap).toString());
	}

	/**
	 * @Title: appendImportBatch
	 * @Description: 追加导入兑换码
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-29 下午05:47:26
	 */
	@RequestMapping(value = "/appendimportbatch")
	public ModelAndView appendImportBatch() {
		String msg = "0"; // 默认业务处理失败
		String successContent = ""; // 操作成功描述：共导入**条，成功导入**条，导入失败**条
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		CouponRule couponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr(map.get("paras1"), CouponRule.class);
		CouponBatch couponBatch = (CouponBatch) JsonHFUtil.getObjFromJsonArrStr(map.get("paras2"), CouponBatch.class);
		ServletFileUpload upload = new ServletFileUpload();
		upload.setHeaderEncoding("UTF-8");
		InputStream stream = null;
		String errorPath = this.getHttpSession().getServletContext().getRealPath("/")
				+ ParameterPool.couponSystemBackParas.get("ERRFILEPATH");
		try {
			if (ServletFileUpload.isMultipartContent(super.getHttpRequest())) {
				FileItemIterator iter = upload.getItemIterator(super.getHttpRequest());
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (!item.isFormField()) {
						// 得到文件的扩展名(无扩展名时将得到全名)
						String ext = item.getName().substring(item.getName().lastIndexOf(".") + 1);
						if ("xls".equals(ext.toLowerCase())) {
							successContent = couponBatchService.importBatchForXls(stream, couponRule, couponBatch, "1",
									errorPath, getUser());
						} else {
							successContent = couponBatchService.importBatchForXlsx(stream, couponRule, couponBatch,
									"1", errorPath, getUser());
						}

						// 操作日志
						CouponLog log = new CouponLog();
						log.setBusinessobject(LOGBUSSOBJ);
						log.setOpertype(Const.LOG_OPT_CREATE);
						log.setResultdesc(Const.LOG_RES_SUCC);
						// 索引字段 用于查找
						log.setIxdata1(couponRule.getCouponId()); // 兑换券编号
						log.setIxdata2(couponRule.getMerId()); // 商户ID
						log.setIxdata4(couponRule.getGoodsId()); // 商品ID
						// content 成功*条，失败*条， errorfile 错误日志文件名
						String optData = "{succContent:'<content>',errorfile:'<filename>'}";
						if (successContent.indexOf("点击") > 0) {
							optData = optData.replace("<content>", successContent.substring(0, successContent
									.indexOf("点击")));
							optData = optData.replace("<filename>", successContent.substring(successContent
									.indexOf("error/") + 6, successContent.indexOf(".xls") + 4));
						} else {
							optData = optData.replace("<content>", successContent);
							optData = optData.replace("<filename>", "");
						}
						log.setOperdata(optData);
						log.setIxdata3("第" + couponBatch.getBatchId().substring(10) + "批次"); // 批次名称
						couponLogService.addLog(log);

						// 批次导入操作日志
						CouponLog log2 = new CouponLog();
						log2.setBusinessobject(LOGBUSSOBJ2);
						log2.setOpertype(Const.LOG_OPT_CREATE);
						log2.setResultdesc(Const.LOG_RES_SUCC);
						// 索引字段 用于查找
						log2.setIxdata1(couponRule.getCouponId()); // 兑换券编号
						log2.setIxdata2(couponRule.getMerId()); // 商户ID
						log2.setIxdata3("第" + couponBatch.getBatchId().substring(10) + "批次"); // 批次名称
						log2.setIxdata4(couponRule.getGoodsId()); // 商品ID
						log2.setOperdata("批次追加：" + successContent.substring(0, successContent.indexOf("！") + 1));
						couponLogService.addLog(log2);
					}
				}
				msg = "1"; // 成功
			}
		} catch (FileUploadException uploadException) {
			uploadException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (BusinessException busiException) {
			busiException.printStackTrace();
			msg = busiException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("msg", msg);
		retMap.put("successContent", successContent);
		return new ModelAndView("jsonView", "ajax_json", JSONObject.fromObject(retMap).toString());
	}

	/**
	 * @Title: stopBatch
	 * @Description: 停止批次
	 * @param
	 * @param batchId
	 * @return
	 * @author wanyong
	 * @date 2012-12-29 下午03:18:18
	 */
	@RequestMapping(value = "/stopbatch")
	public ModelAndView stopBatch(String batchId) {
		String msg = "0"; // 默认返回处理失败
		try {
			couponBatchService.stopBatch(batchId);

			// 批次导入操作日志
			CouponLog log2 = new CouponLog();
			// 从兑换券规则表取出兑换券规则信息
			CouponBatch couponBatch = couponBatchService.loadCouponBatch(batchId);
			CouponRule couponRule = couponRuleService.load(couponBatch.getRuleId());
			log2.setBusinessobject(LOGBUSSOBJ2);
			log2.setOpertype(Const.LOG_OPT_MODIFY);
			log2.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log2.setIxdata1(couponRule.getCouponId()); // 兑换券编号
			log2.setIxdata2(couponRule.getMerId()); // 商户ID
			log2.setIxdata3("第" + couponBatch.getBatchId().substring(10) + "批次"); // 批次名称
			log2.setIxdata4(couponRule.getGoodsId()); // 商品ID
			log2.setOperdata("批次停用：兑换券规则ID为" + couponRule.getRuleId() + " 批次为第"
					+ couponBatch.getBatchId().substring(10) + "批次");
			couponLogService.addLog(log2);

			msg = "1";
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: startBatch
	 * @Description: 启用批次
	 * @param
	 * @param batchId
	 * @return
	 * @author wanyong
	 * @date 2012-12-29 下午03:59:40
	 */
	@RequestMapping(value = "/startbatch")
	public ModelAndView startBatch(String batchId) {
		String msg = "0"; // 默认返回处理失败
		try {
			couponBatchService.startBatch(batchId);

			// 批次导入操作日志
			CouponLog log2 = new CouponLog();
			// 从兑换券规则表取出兑换券规则信息
			CouponBatch couponBatch = couponBatchService.loadCouponBatch(batchId);
			CouponRule couponRule = couponRuleService.load(couponBatch.getRuleId());
			log2.setBusinessobject(LOGBUSSOBJ2);
			log2.setOpertype(Const.LOG_OPT_MODIFY);
			log2.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log2.setIxdata1(couponRule.getCouponId()); // 兑换券编号
			log2.setIxdata2(couponRule.getMerId()); // 商户ID
			log2.setIxdata3("第" + couponBatch.getBatchId().substring(10) + "批次"); // 批次名称
			log2.setIxdata4(couponRule.getGoodsId()); // 商品ID
			log2.setOperdata("批次启用：兑换券规则ID为" + couponRule.getRuleId() + " 批次为第"
					+ couponBatch.getBatchId().substring(10) + "批次");
			couponLogService.addLog(log2);

			msg = "1";
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * 跳转到兑换码批量导入日志查询
	 * 
	 * @Title: queryImportLog
	 * @Description: 兑换码批量导入日志查询
	 * @param
	 * @return
	 * @author wangliangyi
	 * @date 2013-1-11 下午05:19:22
	 */
	@RequestMapping(value = "/importLog")
	public String toImportLog() {
		return "couponbatch/queryImportLog";
	}

	/**
	 * 跳转到兑换码批量导入操作日志查询
	 * 
	 * @Title: queryImportLog
	 * @Description: 兑换码批量导入操作日志查询
	 * @param
	 * @return
	 * @author wangliangyi
	 * @date 2013-1-11 下午05:19:22
	 */
	@RequestMapping(value = "/importOptLog")
	public String toImportOptLog() {
		return "couponbatch/queryImportOptLog";
	}

	/**
	 * 兑换码批量导入日志查询
	 * 
	 * @Title: queryImportLog
	 * @Description: TODO
	 * @param
	 * @return
	 * @author wangliangyi
	 * @date 2013-1-15 上午11:59:18
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryimportlog")
	public ModelAndView queryImportLog() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染商户名称
					subMap.put("MERNAME", merMap.containsKey(subMap.get("MERID").toString()) ? ((MerInfo) (merMap
							.get(subMap.get("MERID").toString()))).getMerName() : "无此商户");
					// 渲染商品名称
					subMap.put("GOODSNAME", goodsMap.containsKey(subMap.get("MERID").toString() + "-"
							+ subMap.get("GOODSID").toString()) ? ((GoodsInfo) goodsMap.get(subMap.get("MERID")
							.toString()
							+ "-" + subMap.get("GOODSID").toString())).getGoodsName() : "无此商品");
					// 渲染兑换券名称
					subMap.put("COUPONNAME",
							couponInfMap.containsKey(subMap.get("COUPONID").toString()) ? ((CouponInf) couponInfMap
									.get(subMap.get("COUPONID").toString())).getCouponName() : "无此兑换券");
				}
				long count = queryCount(queryKey, map);
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
}
