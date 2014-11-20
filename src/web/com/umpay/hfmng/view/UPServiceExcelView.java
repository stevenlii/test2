/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankExcelView.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-26
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
 * class       :  ChnlBankExcelView
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public class UPServiceExcelView extends AbstractView {

	/** ********************************************
	 * method name   : renderMergedOutputModel 
	 * modified      : xuhuafeng ,  2013-3-26
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
		String[] title = { "更新时间", "商户号", "商户名称", "商品号", "商品名称", "通道", "商品分类",
				"计费代码1", "计费代码名称1", "计费代码2", "计费代码名称2", "计费代码3", "计费代码名称3",
				"金额(分)", "计费类型", "状态", "修改人" };
		String fileName = "综合支付商品计费代码信息.xls";
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "商品计费代码列表";

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
			int j = 0;
			Map<String, Object> db = data.get(i-1);
			label = new Label(j++, i, db.get("MODTIME") == null ? "" : db.get("MODTIME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("MERID") == null ? "" : db.get("MERID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("MERNAME") == null ? "" : db.get("MERNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("GOODSID") == null ? "" : db.get("GOODSID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("GOODSNAME") == null ? "" : db.get("GOODSNAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("GATENAME") == null ? "" : db.get("GATENAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("CATEGORY") == null ? "" : db.get("CATEGORY").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("SERVICEID") == null ? "" : db.get("SERVICEID").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("SERVICENAME") == null ? "" : db.get("SERVICENAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("SERVICEID1") == null ? "" : db.get("SERVICEID1").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("SERVICENAME1") == null ? "" : db.get("SERVICENAME1").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("SERVICEID2") == null ? "" : db.get("SERVICEID2").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("SERVICENAME2") == null ? "" : db.get("SERVICENAME2").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("AMOUNT") == null ? "" : db.get("AMOUNT").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("FEETYPE") == null ? "" : db.get("FEETYPE").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("STATENAME") == null ? "" : db.get("STATENAME").toString());
			sheet.addCell(label);
			label = new Label(j++, i, db.get("MODUSER") == null ? "" : db.get("MODUSER").toString());
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}

}
