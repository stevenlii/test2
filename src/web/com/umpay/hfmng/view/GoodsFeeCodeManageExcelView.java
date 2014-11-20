package com.umpay.hfmng.view;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

public class GoodsFeeCodeManageExcelView extends AbstractView {
	private static final Logger log = Logger.getLogger(ExcelView.class);

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control",
				"no-store, max-age=0, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers.
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		Map<String, Object> res=(Map<String, Object>) model.get("excel");
		List<Map<String, Object>> data = (List<Map<String, Object>>)res.get("feeCode");
		String[] title = { "更新时间", "商户名称", "商户号", "商品号", "商品名称", "商品一级分类", "商品二级分类", "商品三级分类",
				"价格(分)", "服务类型", "计费代码", "匹配度", "状态" };
		String fileName = "商品计费代码管理信息.xls";
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "商品计费代码管理信息列表";

		OutputStream os = null;
		os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		File file = new File(fileName);
		WritableWorkbook wwb = Workbook.createWorkbook(os);

		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
		Label label;
		jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont
				.createFont("宋体"), 12);
		WritableCellFormat font = new WritableCellFormat(wfont);
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		// 下面是填充数据
		for(int i=1;i<data.size()+1;i++){
			int j=0;
			Map feeCode = data.get(i-1);
			label = new Label(j++, i, feeCode.get("MODTIME").toString().substring(0, 19));
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("MERNAME") == null ? "" : feeCode.get("MERNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("MERID") == null ? "" : feeCode.get("MERID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("GOODSID") == null ? "" : feeCode.get("GOODSID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("GOODSNAME") == null ? "" : feeCode.get("GOODSNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("CATEGORY1") == null ? "" : feeCode.get("CATEGORY1").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("CATEGORY2") == null ? "" : feeCode.get("CATEGORY2").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("CATEGORY3") == null ? "" : feeCode.get("CATEGORY3").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("AMOUNT") == null ? "" : feeCode.get("AMOUNT").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("SERVTYPE") == null ? "" : feeCode.get("SERVTYPE").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("SERVICEID") == null ? "" : feeCode.get("SERVICEID").toString());
			sheet.addCell(label);
			int matchType = (Integer) feeCode.get("MATCHTYPE");
			String matchTypeName = "-";
			if(matchType == 0){
				matchTypeName = "精配";
			}else if(matchType == 1){
				matchTypeName = "套用";
			}
			label = new Label(j++, i, matchTypeName);
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("STATE") == null ? "" : feeCode.get("STATE").toString());
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}

}
