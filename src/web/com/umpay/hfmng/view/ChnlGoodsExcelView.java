/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoodsExcelView.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-28
 * *************************************************/ 

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

import org.springframework.web.servlet.view.AbstractView;


/** ******************  类说明  *********************
 * class       :  ChnlGoodsExcelView
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public class ChnlGoodsExcelView extends AbstractView {

	/** ********************************************
	 * method name   : renderMergedOutputModel 
	 * modified      : xuhuafeng ,  2013-3-28
	 * @see          : @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * ********************************************/
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
		List<Map<String, Object>> data = (List<Map<String, Object>>)model.get("excel");
		String[] title = { "渠道编号", "渠道名称", "商户号", "商户名称", "商户状态", "商品号",
				"商品名称", "商品状态", "渠道商品状态", "修改时间", "负责人" };
		String fileName = "渠道商品信息.xls";
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "渠道商品信息列表";

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
			Map feeCode = data.get(i-1);
			int j = 0;
			label = new Label(j++, i, feeCode.get("CHANNELID") == null ? null : feeCode.get("CHANNELID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("CHANNELNAME") == null ? null : feeCode.get("CHANNELNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("MERID") == null ? null : feeCode.get("MERID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("MERNAME") == null ? null : feeCode.get("MERNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("MERSTATE") == null ? null : feeCode.get("MERSTATE").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("GOODSID") == null ? null : feeCode.get("GOODSID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("GOODSNAME") == null ? null : feeCode.get("GOODSNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("GOODSSTATE") == null ? null : feeCode.get("GOODSSTATE").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("STATE") == null ? null : feeCode.get("STATE").toString());
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("MODTIME") == null ? null : feeCode.get("MODTIME").toString().substring(0, 19));
			sheet.addCell(label);
			label = new Label(j++, i, feeCode.get("SERVICE_USER") == null ? null : feeCode.get("SERVICE_USER").toString());
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}

}
