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

import com.umpay.hfmng.model.FeeCode;

public class FeeCodeUseQueryExcel extends AbstractView {
	private static final Logger log = Logger.getLogger(FeeCodeUseQueryExcel.class);
	@Override
	protected void renderMergedOutputModel(Map model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");   
		// Set standard HTTP/1.1 no-cache headers.   
		response.setHeader("Cache-Control","no-store, max-age=0, no-cache, must-revalidate");   
		 //Set IE extended HTTP/1.1 no-cache headers.   
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		 //Set standard HTTP/1.0 no-cache header.   
		response.setHeader("Pragma", "no-cache");  
		List<FeeCode> listFeeCode= (List<FeeCode>) model.get("excel");
		String[] title = { "代码","代码名称","类型","分类","金额(分)","状态","使用次数","匹配度"};
		String fileName = "计费代码使用查询信息.xls";
		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "";
		sheetNameString = "计费代码使用查询信息列表";
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
		jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 12);
		WritableCellFormat font = new WritableCellFormat(wfont);
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		// 下面是填充数据
		for (int i = 1; i < listFeeCode.size() + 1; i++) {
			FeeCode feeCode = (FeeCode) listFeeCode.get(i - 1);
			label = new Label(0, i, feeCode.getServiceId());
			sheet.addCell(label);
            label =new Label(1,i,feeCode.getDetail());
            sheet.addCell(label);
            label =new Label(2,i,feeCode.getFeeType().equals("2")?"按次":"包月");
            sheet.addCell(label);
            label =new Label(3,i,feeCode.getCategory());
            sheet.addCell(label);
            label =new Label(4,i,feeCode.getAmount());
            sheet.addCell(label);
			label =new Label(5,i,feeCode.getState().equals("2")?"启用":"禁用");
            sheet.addCell(label);
        	label =new Label(6,i,feeCode.getUseCount());
            sheet.addCell(label);
            String matchType = feeCode.getMatchType();
            if("0".equals(matchType)){
            	matchType = "精确匹配";
            }else if("1".equals(matchType)){
            	matchType = "套用";
            }else if("2".equals(matchType)){
            	matchType = "其他";
            }else if("-99".equals(matchType)){
            	matchType = "未知";
            }else{
            	matchType = "-";
            }
        	label =new Label(7,i,matchType);
            sheet.addCell(label);
            
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}
}

