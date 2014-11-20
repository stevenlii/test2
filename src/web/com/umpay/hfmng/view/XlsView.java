package com.umpay.hfmng.view;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.ExportUtil;

/**
 * 
 * 
 * @ClassName:XlsView
 * @Description: 导出xls view
 * @version: 1.0
 * @author: lituo
 * @Create: 2013-7-9
 * 
 */
public class XlsView extends AbstractView {

	private Logger log = LoggerFactory.getLogger(XlsView.class);

	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("[EXCEL导出日志],开始导出");
		// 获取list和title
		Map<String, Object> map = (Map<String, Object>) model.get("modelMap");
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");// 获取list
		String[] title = (String[]) map.get("title");// 获取title
		String remark = (String) map.get("remark");// 获取备注信息
		String reportName = (String) map.get("reportName");// 获取导出报表名称
		int titleColumn = 0; // 列标题所在行
		int ContentColumn = 0;// 内容所在行
		log.info("[EXCEL(" + reportName + ")导出日志],获取表头、数据集合、备注、导出名完成");

		/** 将list和title写入excel **/
		log.info("[EXCEL(" + reportName + ")导出日志],开始向HSSFWorkbook写入数据");
		// 定义HSSFWorkbook
		HSSFWorkbook wb = new HSSFWorkbook();
		// 设置样式
		CellStyle cellStrStyle = ExportUtil.getStrCellStyle(wb);// 获取字符样式
		CellStyle cellNumberStyle = ExportUtil.getNumberCellStyle(wb);// 获取数字样式
		CellStyle cellTitleStyle = ExportUtil.getTitleCellStyle(wb);// 获取表头样式
		CellStyle cellRemarkStyle = ExportUtil.getRemarkCellStyle(wb);// 获取备注样式

		// 定义sheet
		Sheet sheet = wb.createSheet();

		if (!"".equals(remark)) {
			log.info("[EXCEL(" + reportName + ")导出日志],写入备注");
			// 如果备注信息不为空，将需要添加到报表最上行
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.length - 1));// 合并单元格
			Row row = sheet.createRow(0); // 创建第一行
			row.setHeight((short) 500);// 设置行高
			ExportUtil.createCell(wb, row, 0, remark, cellRemarkStyle);
			titleColumn = 1;
			ContentColumn = 2;
		} else {
			// 备注信息为空，则列标题为第一行
			titleColumn = 0;
			ContentColumn = 1;
		}

		// 写入表头文件
		log.info("[EXCEL(" + reportName + ")导出日志],写入表头文件");
		Row row = sheet.createRow(titleColumn); // 创建表头
		for (int i = 0; i < title.length; i++) {
			ExportUtil.createCell(wb, row, i, title[i].split(":")[1], cellTitleStyle);
		}
		log.info("[EXCEL(" + reportName + ")导出日志],写入内容");
		// 循环写入内容
		if (null != list) {
			for (Map<String, Object> m : list) {
				Row rowData = sheet.createRow(ContentColumn++);// 根据待写入的list长度，创建行数
				for (int i = 0; i < title.length; i++) {
					if (title[i].split(":").length == 2) {
						// 没有进行单元格类型设置，默认为string
						ExportUtil.createCell(wb, rowData, i, String.valueOf(m.get(title[i].split(":")[0])==null?"":m.get(title[i].split(":")[0])),
								cellStrStyle);
					} else if (title[i].split(":").length == 3) {
						// 进行单元格类型设置，判断是否为number，是则走number样式
						if ("number".equals(title[i].split(":")[2])) {
							ExportUtil.createCell(wb, rowData, i, String.valueOf(m.get(title[i].split(":")[0])==null?"":m.get(title[i].split(":")[0])),
									cellNumberStyle);
						} else {
							ExportUtil.createCell(wb, rowData, i, String.valueOf(m.get(title[i].split(":")[0])==null?"":m.get(title[i].split(":")[0])),
									cellStrStyle);
						}
					} else {
						// title不符合格式要求，此列不展示
					}
				}
			}
		}

		// 自动调整列宽度
		log.info("[EXCEL(" + reportName + ")导出日志],调整列宽度");
		for (int i = 0; i < title.length; i++) {
			sheet.autoSizeColumn((short) i); // 调整指定宽度
		}
		log.info("[EXCEL(" + reportName + ")导出日志],写入HSSFWorkbook完成");
		/** 写入excel完成 **/
		// 转成字节流，导出
		log.info("[EXCEL(" + reportName + ")导出日志],开始转成字节流");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] ba = baos.toByteArray();
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			baos.flush();
			wb.write(baos);// 向输入流写数据
			log.info("[EXCEL(" + reportName + ")导出日志],转成字节流完成，准备导出");
			ba = baos.toByteArray();
			response.reset();// 重置response
			// 设置response
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(reportName.getBytes("gb2312"), "ISO8859-1") + ".xls");// 随机获取导出文件名
			response.setContentType("application/x-msdownload");
			// 设置response完毕
			log.info("[EXCEL(" + reportName + ")导出日志],导出文件");
			os.write(ba);// 导出
		} finally {
			if (null != baos) {
				baos.close();
			}
			if (null != os) {
				os.close();
			}
		}
	}
}
