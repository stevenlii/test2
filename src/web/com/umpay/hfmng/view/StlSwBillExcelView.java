package com.umpay.hfmng.view;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.CouponMerSet;

/**
 * @ClassName: StlSwBillExcelView
 * @Description: 财务结算管理-省网账单EXCEL视图
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-12-17
 */
public class StlSwBillExcelView extends AbstractView {

	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		String fileName = "综合业务管理平台_财务结算_省网结算账单_" + StringUtil.get8Date() + ".xls";
		OutputStream os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		String tmpPath = request.getSession().getServletContext().getRealPath("/WEB-INF/classes")
				+ "/templet/sw-mer-bill-tmp.xls"; // EXCEL模板全路径
		Workbook tmpWwb = Workbook.getWorkbook(new File(tmpPath)); // 读取模板EXCEL
		WritableWorkbook outWwb = Workbook.createWorkbook(os, tmpWwb);
		Map<String, Object> map = (Map<String, Object>) model.get("excel");
		CouponMerSet couponMerSet = (CouponMerSet) map.get("COUPONMERSET");
		// 填充数据
		Label label = new Label(0, 0, outWwb.getSheet(0).getCell(0, 0).getContents()
				.replaceAll("@MERNAME", couponMerSet.getAccName()).replaceAll("@STLDATE", couponMerSet.getStlDate()),
				outWwb.getSheet(0).getCell(0, 0).getCellFormat());
		outWwb.getSheet(0).addCell(label);
		label = new Label(9, 0, outWwb.getSheet(0).getCell(9, 0).getContents()
				.replaceAll("@CREATEDATE", StringUtil.get11Date()), outWwb.getSheet(0).getCell(9, 0).getCellFormat());
		outWwb.getSheet(0).addCell(label);
		label = new Label(0, 3, couponMerSet.getAccId(), outWwb.getSheet(0).getCell(0, 3).getCellFormat());
		outWwb.getSheet(0).addCell(label);
		label = new Label(1, 3, couponMerSet.getAccName(), outWwb.getSheet(0).getCell(1, 3).getCellFormat());
		outWwb.getSheet(0).addCell(label);
		label = new Label(2, 3, couponMerSet.getGoodsId(), outWwb.getSheet(0).getCell(2, 3).getCellFormat());
		outWwb.getSheet(0).addCell(label);
		Number number = new Number(3, 3, couponMerSet.getSuccNums(), outWwb.getSheet(0).getCell(3, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(4, 3, couponMerSet.getSuccAmts(), outWwb.getSheet(0).getCell(4, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(5, 3, couponMerSet.getMuteAmt(), outWwb.getSheet(0).getCell(5, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(6, 3, couponMerSet.getExt1() / 100.0, outWwb.getSheet(0).getCell(6, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(7, 3, couponMerSet.getNbsmNum(), outWwb.getSheet(0).getCell(7, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(8, 3, couponMerSet.getNbsmAmt(), outWwb.getSheet(0).getCell(8, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(9, 3, couponMerSet.getPaybackAmt(), outWwb.getSheet(0).getCell(9, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		number = new Number(10, 3, couponMerSet.getMerStlPay(), outWwb.getSheet(0).getCell(10, 3).getCellFormat());
		outWwb.getSheet(0).addCell(number);
		// 结算周期
		label = new Label(0, 5, outWwb.getSheet(0).getCell(0, 5).getContents()
				.replaceAll("@STLSTARTDATE", StringUtil.get11Date_SpaceXMonStart(couponMerSet.getStlDate(), 0))
				.replaceAll("@STLENDDATE", StringUtil.get11Date_SpaceXMonEnd(couponMerSet.getStlDate(), 0)), outWwb
				.getSheet(0).getCell(0, 5).getCellFormat());
		outWwb.getSheet(0).addCell(label);

		// 写入数据
		outWwb.write();
		// 关闭文件
		outWwb.close();
		tmpWwb.close();

	}
}
