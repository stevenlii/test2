/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankExcelView.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-19
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

import com.umpay.hfmng.model.MerBank;


/** ******************  类说明  *********************
 * class       :  MerBankExcelView
 * @author     :  xhf
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class MerBankExcelView extends AbstractView {
	
	/** ********************************************
	 * method name   : renderMergedOutputModel 
	 * modified      : xhf ,  2012-10-19
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
		Map<String, Object> res=(Map<String, Object>) model.get("excel");
		List<MerBank> listMerBank = (List<MerBank>)res.get("merBank");
		
		String[] title = { "时间", "商户名称", "商户号", "支付银行", "运营负责人", "状态" };

		String fileName = "商户银行配置信息.xls";

		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "";

		sheetNameString = "商户银行信息列表";

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
		for(int i=1;i<listMerBank.size()+1;i++){
			MerBank merBank = listMerBank.get(i-1);
			label = new Label(0, i, merBank.getModTime().toString().substring(0, 19));
			sheet.addCell(label);
			label = new Label(1, i, merBank.getMerName());
			sheet.addCell(label);
			label = new Label(2, i, merBank.getMerId());
			sheet.addCell(label);
			label = new Label(3, i, merBank.getBankId());
			sheet.addCell(label);
			label = new Label(4, i, merBank.getOperator());
			sheet.addCell(label);
			label = new Label(5, i, merBank.getState());
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();

	}

}
