package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;


public class SecMerExcelView extends AbstractView {
	private static final Logger log = Logger.getLogger(SecMerExcelView.class);
	@Override
	protected void renderMergedOutputModel(Map model,HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		response.setContentType("application/json;charset=UTF-8");   
		// Set standard HTTP/1.1 no-cache headers.   
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");   
		 //Set IE extended HTTP/1.1 no-cache headers.   
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		 //Set standard HTTP/1.0 no-cache header.   
		response.setHeader("Pragma", "no-cache");  
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> secMerList= (List<Map<String, Object>>) model.get("excel");

		String[] title = { "二级商户号","二级商户名称","状态","运营负责人","修改时间","接口标识","下单地址","支付通知地址" };
		String fileName = "二级商户信息.xls";
		// 获得开始时间
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "";

		sheetNameString = "二级商户信息列表";

		OutputStream os = null;
		os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
		Label label;
		jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 12);
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		//设置列宽
		sheet.setColumnView(0, 10);
		sheet.setColumnView(1, 20);
		sheet.setColumnView(2, 6);
		sheet.setColumnView(3, 10);
		sheet.setColumnView(4, 19);
		sheet.setColumnView(5, 8);
		sheet.setColumnView(6, 60);
		sheet.setColumnView(7, 60);
		// 下面是填充数据
		for (int i = 1; i < secMerList.size() + 1; i++) {
			Map<String, Object> secMer = secMerList.get(i - 1);
            label =new Label(0,i,getLineData(secMer,"SUBMERID"));
            sheet.addCell(label);
			label =new Label(1,i,getLineData(secMer,"SUBMERNAME"));
			sheet.addCell(label);
			label =new Label(2,i,getState(secMer,"STATE"));
            sheet.addCell(label);
			label = new Label(3, i, getLineData(secMer,"OPERATOR"));
            sheet.addCell(label);
            label =new Label(4,i,getLineData(secMer,"MODTIME").substring(0, 19));
            sheet.addCell(label);
            label =new Label(5,i,getInFunCode(secMer,"INFUNCODE"));
            sheet.addCell(label);
            label =new Label(6,i,getLineData(secMer,"ORDERURL"));
            sheet.addCell(label);
            label =new Label(7,i,getLineData(secMer,"NOTIFYURL"));
            sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}
	
	private String getLineData(Map<String, Object> secMer,String columnName){
		return secMer.get(columnName) == null?"" : secMer.get(columnName).toString();
	}
	
	private String getState(Map<String, Object> secMer,String columnName){
		if(secMer.get(columnName) == null)
			return "";
		if(secMer.get(columnName).toString().equals("2"))
			return "启用";
		else if(secMer.get(columnName).toString().equals("4"))
			return "禁用";
		else
			return secMer.get(columnName).toString();
	}
	
	private String getInFunCode(Map<String, Object> secMer,String columnName){
		if(secMer.get(columnName) == null)
			return "";
		if(secMer.get(columnName).toString().equals("2"))
			return "按次";
		else if(secMer.get(columnName).toString().equals("3"))
			return "包月";
		else
			return secMer.get(columnName).toString();
	}
}
