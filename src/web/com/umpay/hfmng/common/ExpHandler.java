package com.umpay.hfmng.common;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExpHandler {
	private static final Logger log = Logger.getLogger(ExpHandler.class);

	/**
	 * ******************************************** method name : handler
	 * description : 将输出流写入Excel
	 * 
	 * @return : String
	 * @param : @param response
	 * @param : @param os
	 * @param : @param data
	 * @modified : panyouliang
	 */
	@SuppressWarnings("unchecked")
	public static void handler(HttpServletResponse response, OutputStream os, List data, Class clz) throws Exception {
		log.info("ExpHandler处理Excel");
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		try {
			int row = 0;
			ExpTypeAnnotation typeAnnotation = (ExpTypeAnnotation) clz.getAnnotation(ExpTypeAnnotation.class); // 获取Class上的ExpTypeAnnotation注解信息
			if (typeAnnotation == null) {
				return;
			}
			String fileName = getFileName(typeAnnotation.fileName());// 从注解上获取文件名称
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
			WritableSheet sheet = wwb.createSheet(typeAnnotation.sheetName(), 0);// 创建sheet
			String[] fieldArr = typeAnnotation.fieldStr(); // 从注解中获取列
			int[] fieldWidthArr = typeAnnotation.fieldWidth();// 从注解中获取列的宽度
			Label label;
			for (int i = 0; i < fieldArr.length; i++) {// 循环写Excel表头
				label = new Label(i, row, fieldArr[i]);
				sheet.addCell(label);
				sheet.setColumnView(i, fieldWidthArr[i]);
			}
			row++;
			if (data != null && data.size() > 0) {
				for (Object obj : data) {
					Field[] fields = clz.getDeclaredFields(); // 获取在POJO定义的成员变量
					for (Field field : fields) {
						ExpFieldAnnotation fieldAnnotation = field.getAnnotation(ExpFieldAnnotation.class);// 获取成员变量的ExpFieldAnnotation注解信息
						if (fieldAnnotation != null) {// 当字段的注解信息不为空的时候,将这个字段写入Excel中
							int col = fieldAnnotation.index();
							FieldType type = fieldAnnotation.type();
							field.setAccessible(true);// 设置成员变量可访问
							Object result = field.get(obj);// 获取字段的值
							// 判断字段在注解中定义的类型
							if (type == FieldType.INTEGER) {
								sheet.addCell(new jxl.write.Number(col, row, (Integer) result));
							} else if (type == FieldType.STRING) {
								sheet.addCell(new Label(col, row, result.toString().trim()));
							} else if (type == FieldType.DOUBLE) {
								sheet.addCell(new jxl.write.Number(col, row, (Double) result));
							} else if (type == FieldType.FLOAT) {
								sheet.addCell(new jxl.write.Number(col, row, (Float) result));
							}
						}
					}
					row++;
				}
			}
			// 写入数据
			wwb.write();
		} finally {
			// 关闭文件
			if (wwb != null)
				try {
					wwb.close();
				} catch (Exception e) {
					log.error("处理Excel出现异常.", e);
				}
		}
	}

	/**
	 * 获取下载文件名称
	 * 
	 * @Title: getFileName
	 * @Description: 获取下载文件名称
	 * @param name
	 * @return 文件名
	 * @author panyouliang
	 * @date 2013-3-29 上午11:33:05
	 */
	private static String getFileName(String name) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder();
		sb.append("综合业务管理平台_");
		sb.append(name);
		sb.append("_");
		sb.append(df.format(new Date()));
		sb.append(".xls");
		return sb.toString();
	}
}
