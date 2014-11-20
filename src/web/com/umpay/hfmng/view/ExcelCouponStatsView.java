package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.ExpHandler;

/**
 * @ClassName: ExcelCouponStatsView
 * @Description: 兑换劵统计视图
 * @author panyouliang
 * @date 2012-12-27
 */
public class ExcelCouponStatsView extends AbstractView {
	private static final Logger log = Logger.getLogger(ExcelCouponStatsView.class);

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("导出Excel");
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		Map<String, Object> res = (Map<String, Object>) model.get("excelStats");
		Class type = (Class) res.get("type");
		OutputStream os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		List data = (List) res.get("data");// 获取数据
		try {
			ExpHandler.handler(response, os, data, type);
		} catch (Exception ex) {
			log.error("导出Excel出现异常.", ex);
		}
	}
}
