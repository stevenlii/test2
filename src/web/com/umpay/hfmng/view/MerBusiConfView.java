/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBusiConfView.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-16
 * *************************************************/ 

package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.AbstractView;


/** ******************  类说明  *********************
 * class       :  MerBusiConfView
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户支付类型页面导出方法
 * ************************************************/

public class MerBusiConfView extends AbstractView {

	/** ********************************************
	 * method name   : renderMergedOutputModel 
	 * modified      : xuhuafeng ,  2014-1-16
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
		String[] title = { "商户号", "商户名称", "业务覆盖", "支付类型编码", "支付类型", "状态" };
		String fileName = "商户支付类型信息.xls";
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "商户支付类型信息列表";

		OutputStream os = null;
		os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		WritableWorkbook wwb = Workbook.createWorkbook(os);

		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
		Label label;
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		// 下面是填充数据
		for(int i=1;i<data.size()+1;i++){
			Map db = data.get(i-1);
			int j = 0;
			label = new Label(j++, i, getLineData(db.get("MERID")));
			sheet.addCell(label);
			label = new Label(j++, i, getLineData(db.get("MERNAME")));
			sheet.addCell(label);
			label = new Label(j++, i, getbusiType(db,"BUSITYPE"));
			sheet.addCell(label);
			label = new Label(j++, i, getLineData(db.get("BIZTYPE")));
			sheet.addCell(label);
			label = new Label(j++, i, getLineData(db.get("BIZTYPENAME")));
			sheet.addCell(label);
			label = new Label(j++, i, getState(db,"STATE"));
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}
	
	private String getLineData(Object columnName){
		return columnName == null ? "" : columnName.toString();
	}
	
	private String getState(Map<String, Object> secMer,String columnName){
		if(secMer.get(columnName) == null)
			return "";
		String data = secMer.get(columnName).toString();
		if(data.equals("2"))
			return "启用";
		else if(data.equals("4"))
			return "禁用";
		else
			return data;
	}
	
	private String getbusiType(Map<String, Object> secMer,String columnName){
		if(secMer.get(columnName) == null)
			return "";
		String data = secMer.get(columnName).toString();
		if(data.equals("01"))
			return "本地接入";
		else if(data.equals("02"))
			return "全网落地";
		if(data.equals("10"))
			return "全网";
		if(data.equals("11"))
			return "全网、本地接入";
		else if(data.equals("12"))
			return "全网、全网落地";
		else
			return data;
	}

}
