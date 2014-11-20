package com.umpay.hfmng.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

/**
 * 
*    
* @ClassName:ExportUtil 	      
* @Description: TODO      
* @version: 1.0   
* @author: lituo  
* @Create: 2013-7-10    
*
 */
public class ExportUtil {
	/**
	 * 
	* @Title: createCell
	* @Description: 创建单元格
	* @param @param wb
	* @param @param row
	* @param @param column
	* @param @param value
	* @param @param cellStyle
	* @return void
	* @throws
	* @author: lituo
	* @Create: 2013-7-9
	 */
	public static void createCell(Workbook wb, Row row, int column,
			String value, CellStyle cellStyle) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
	}
	
	/**
	 * 
	* @Title: createFonts
	* @Description: 设置字体
	* @param @param wb
	* @param @param bold
	* @param @param fontName
	* @param @param isItalic
	* @param @param hight
	* @param @return
	* @return Font
	* @throws
	* @author: lituo
	* @Create: 2013-7-9
	 */
	public static Font createFonts(Workbook wb, short bold, String fontName,
			boolean isItalic, short hight) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeight(hight);
		return font;
	}
	
	/**
	 * 
	 * @Title: getStrCellStyle
	 * @Description: 定义字符样式
	 * @param @param wb
	 * @param @return
	 * @return CellStyle
	 * @throws
	 * @author: lituo
	 * @Create: 2013-7-6
	 */
	public static CellStyle getStrCellStyle(HSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 居中
		cellStyle.setWrapText(true);// 设置主动换行
		return cellStyle;
	}

	/**
	 * 
	 * @Title: getNumberCellStyle
	 * @Description: 定义数字样式
	 * @param @param wb
	 * @param @return
	 * @return CellStyle
	 * @throws
	 * @author: lituo
	 * @Create: 2013-7-6
	 */
	public static CellStyle getNumberCellStyle(HSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 居中
		cellStyle.setWrapText(true);// 设置主动换行
		return cellStyle;
	}

	/**
	 * 
	 * @Title: getNumberCellStyle
	 * @Description: 定义表头样式
	 * @param @param wb
	 * @param @return
	 * @return CellStyle
	 * @throws
	 * @author: lituo
	 * @Create: 2013-7-6
	 */
	public static CellStyle getTitleCellStyle(HSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		cellStyle.setWrapText(false);// 设置主动换行
		return cellStyle;
	}
	
	public static CellStyle getRemarkCellStyle(HSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 16);//设置字体大小
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		cellStyle.setWrapText(true);// 设置主动换行
		return cellStyle;
	}
	
	/**
	 * 
	* @Title: getExportModel
	* @Description: 将导出的list和title整合成map,统一传入view
	* @param @param list
	* @param @param title
	* @param @return
	* @return Map<String,Object>
	* @throws
	* @author: lituo
	* @Create: 2013-7-10
	 */
	public static Map<String,Object> getExportModel(List<Map<String, Object>> list,String[] title,String remark,String reportName){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("list", list);
		m.put("title", title);
		m.put("remark", remark);
		m.put("reportName", reportName);
		return m;
	}
}
