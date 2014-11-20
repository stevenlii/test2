package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.HfCodec;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;

/**
 * @ClassName: CouponCodeExcelView
 * @Description: 兑换码导出
 * @author wanyong
 * @date 2013-1-5 10:03:03
 */
public class CouponCodeExcelView extends AbstractView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers.
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// Map<String, Object> res = (Map<String, Object>) model.get("excel");
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) model.get("excel");
		String fileName = "综合业务管理平台_电子兑换券功能兑换码_" + StringUtil.get8Date() + ".xls";
		OutputStream os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet("Sheet1", 0);
		Label label = null;
		String[] title = { "兑换码", "兑换券形式", "渠道", "兑换券编号", "兑换券名称", "商户编号", "商户名称", "商品编号", "商品名称", "兑换券金额（元）",
				"兑换码生成方式", "兑换码状态", "批次号", "兑换时间", "生成时间" };
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		// 渲染数据
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = null;
		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = null;
		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = null;
		// 填充数据
		for (int i = 1; i < mapList.size() + 1; i++) {
			Map<String, Object> map = mapList.get(i - 1);
			label = new Label(0, i, HfCodec.decrypt((map.get("COUPONCODE") + "").trim())); // 兑换码
			sheet.addCell(label);
			label = new Label(1, i, ParameterPool.couponCodeTypes.get((map.get("COUPONCODETYPE") + "").trim())); // 兑换券形式
			sheet.addCell(label);
			label = new Label(2, i, (map.get("CHANNEL") + "").trim()); // 渠道
			sheet.addCell(label);
			label = new Label(3, i, map.get("COUPONID") + ""); // 兑换券编号
			sheet.addCell(label);
			// 从缓存中获取兑换券名称
			couponInf = (CouponInf) couponInfMap.get((map.get("COUPONID") + "").trim());
			label = new Label(4, i, null == couponInf ? "" : couponInf.getCouponName()); // 兑换券名称
			sheet.addCell(label);
			label = new Label(5, i, map.get("MERID") + ""); // 商户编号
			sheet.addCell(label);
			// 从缓存中获取商户名称
			merInfo = (MerInfo) merInfoMap.get((map.get("MERID") + "").trim());
			label = new Label(6, i, null == merInfo ? "" : merInfo.getMerName()); // 商户名称
			sheet.addCell(label);
			label = new Label(7, i, map.get("GOODSID") + ""); // 商品编号
			sheet.addCell(label);
			// 从缓存中获取商品名称
			goodsInfo = (GoodsInfo) goodsInfoMap.get((map.get("MERID") + "").trim() + "-"
					+ (map.get("GOODSID") + "").trim());
			label = new Label(8, i, null == goodsInfo ? "" : goodsInfo.getGoodsName()); // 商品名称
			sheet.addCell(label);
			label = new Label(9, i, StringUtil.bigDecimalToString((BigDecimal) map.get("PRICE"))); // 兑换券金额
			sheet.addCell(label);
			label = new Label(10, i, ParameterPool.couponCodeMethod.get((map.get("GENERATEMETHOD") + "").trim())); // 兑换码生成方式
			sheet.addCell(label);
			label = new Label(11, i, ParameterPool.couponCodeStates.get((map.get("STATE") + "").trim())); // 兑换码状态
			sheet.addCell(label);
			if ("auto".equals((map.get("BATCHID") + "").trim()))
				label = new Label(12, i, "--"); // 批次号
			else
				label = new Label(12, i, map.get("BATCHID") + ""); // 批次号
			sheet.addCell(label);
			if (Const.COUPON_CODESTATE_USE.equals(Integer.parseInt((map.get("STATE") + "").trim())))
				// 该码已使用
				label = new Label(13, i, map.get("EXCHANGETIME") + ""); // 兑换时间
			else
				label = new Label(13, i, "--"); // 兑换时间

			sheet.addCell(label);
			label = new Label(14, i, map.get("INTIME") + ""); // 生成时间
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}
}
