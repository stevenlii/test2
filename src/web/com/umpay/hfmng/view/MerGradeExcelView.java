/** *****************  JAVA头文件说明  ****************
 * file name  :  MerGradeExcelView.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-7
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
 * class       :  MerGradeExcelView
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class MerGradeExcelView extends AbstractView {

	/** ********************************************
	 * method name   : renderMergedOutputModel 
	 * modified      : xuhuafeng ,  2013-3-7
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
		String[] title = { "月份", "商户名称", "商户号", "商品号状态", "交易额", "增长率", "虚假交易", "客诉", "系统稳定性"
				, "配合力度", "违约", "重大投诉", "营销", "资源支持", "总分", "运营负责人" };
		String fileName = "商户考核信息.xls";
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "商户考核信息列表";

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
			label = new Label(0, i, feeCode.get("MONTH") == null ? null : feeCode.get("MONTH").toString());
			sheet.addCell(label);
			label = new Label(1, i, feeCode.get("MERNAME") == null ? null : feeCode.get("MERNAME").toString());
			sheet.addCell(label);
			label = new Label(2, i, feeCode.get("MERID") == null ? null : feeCode.get("MERID").toString());
			sheet.addCell(label);
			label = new Label(3, i, (String) feeCode.get("OPERSTATE"));
			sheet.addCell(label);
			label = new Label(4, i, feeCode.get("TURNOVERINDEX") == null ? null : feeCode.get("TURNOVERINDEX").toString());
			sheet.addCell(label);
			label = new Label(5, i, feeCode.get("RISERATEINDEX") == null ? null : feeCode.get("RISERATEINDEX").toString());
			sheet.addCell(label);
			label = new Label(6, i, feeCode.get("FALSETRADEINDEX") == null ? null : feeCode.get("FALSETRADEINDEX").toString());
			sheet.addCell(label);
			label = new Label(7, i, feeCode.get("COMPLAINTINDEX") == null ? null : feeCode.get("COMPLAINTINDEX").toString());
			sheet.addCell(label);
			label = new Label(8, i, feeCode.get("SYSSTABINDEX") == null ? null : feeCode.get("SYSSTABINDEX").toString());
			sheet.addCell(label);
			label = new Label(9, i, feeCode.get("COOPERATEINDEX") == null ? null : feeCode.get("COOPERATEINDEX").toString());
			sheet.addCell(label);
			label = new Label(10, i, feeCode.get("BREACHINDEX") == null ? null : feeCode.get("BREACHINDEX").toString());
			sheet.addCell(label);
			label = new Label(11, i, feeCode.get("UPGRADEINDEX") == null ? null : feeCode.get("UPGRADEINDEX").toString());
			sheet.addCell(label);
			label = new Label(12, i, feeCode.get("MARKETINGINDEX") == null ? null : feeCode.get("MARKETINGINDEX").toString());
			sheet.addCell(label);
			label = new Label(13, i, feeCode.get("SUPPORTINDEX") == null ? null : feeCode.get("SUPPORTINDEX").toString());
			sheet.addCell(label);
			label = new Label(14, i, feeCode.get("TOTAL") == null ? null : feeCode.get("TOTAL").toString());
			sheet.addCell(label);
			label = new Label(15, i, (String) feeCode.get("OPERATOR"));
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}

}
