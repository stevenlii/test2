package com.umpay.hfmng.view;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.umpay.hfmng.model.MerInfo;


public class MerExcelView extends AbstractView {
	private static final Logger log = Logger.getLogger(MerExcelView.class);
	
	@Override
	protected void renderMergedOutputModel(Map model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		response.setContentType("application/json;charset=UTF-8");   
		// Set standard HTTP/1.1 no-cache headers.   
		response.setHeader("Cache-Control",   
		        "no-store, max-age=0, no-cache, must-revalidate");   
		   
		 //Set IE extended HTTP/1.1 no-cache headers.   
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		   
		 //Set standard HTTP/1.0 no-cache header.   
		response.setHeader("Pragma", "no-cache");  
		List<MerInfo> listMerInfos= (List<MerInfo>) model.get("excel");
		    

		String[] title = { "更新时间","商户名称","商户号","业务覆盖","商户分类","接口类型","渠道报备","运营负责人","添加方式","状态" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
		
		String flag = new SimpleDateFormat("MMddHHmmss").format(new Date()
				.getTime());
		String fileName = "商户信息.xls";
		// 获得开始时间
		long start = System.currentTimeMillis();
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "";

		sheetNameString = "商户信息列表";

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
		for (int i = 1; i < listMerInfos.size() + 1; i++) {
			MerInfo merInfo = (MerInfo) listMerInfos.get(i - 1);
			label = new Label(0, i, merInfo.getModTime().toString()
					.substring(0, 19));
			sheet.addCell(label);
            label =new Label(1,i,merInfo.getMerName());
            sheet.addCell(label);
            label =new Label(2,i,merInfo.getMerId());
            sheet.addCell(label);
            label =new Label(3,i,merInfo.getBusiType());
            sheet.addCell(label);
            label =new Label(4,i,merInfo.getCategory());
            sheet.addCell(label);
            label =new Label(5,i,merInfo.getInterType().equals("0")?"标准":"特殊");
            sheet.addCell(label);
            label =new Label(6,i,merInfo.getChnlCheck().equals("0")?"否":"是");
            sheet.addCell(label);
            label =new Label(7,i,(merInfo.getOperator()));
            sheet.addCell(label);
            label =new Label(8,i,merInfo.getAddWay()==0?"本地":"基地");       //添加方式
			sheet.addCell(label);
			label =new Label(9,i,merInfo.getState().equals("2")?"启用":"禁用");
            sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}
}
