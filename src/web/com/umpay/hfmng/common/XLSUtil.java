package com.umpay.hfmng.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * @ClassName: XLSUtil
 * @Description: Excel 2003版本文件处理类
 * @author wanyong
 * @date 2012-12-27 下午03:04:25
 */
public class XLSUtil {
	protected Logger log = Logger.getLogger(this.getClass());
	private Workbook work;

	/**
	 * @Title: XLSUtil
	 * @Description: 空构造方法
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public XLSUtil() {
	}

	/**
	 * @Title: XLSUtil
	 * @Description: 得到Workbook对象
	 * @param filePath
	 *            文件路径
	 * @throws BiffException
	 * @throws IOException
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public XLSUtil(String filePath) throws BiffException, IOException {
		try {
			InputStream is = new FileInputStream(filePath);
			this.work = Workbook.getWorkbook(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (BiffException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @Title: XLSUtil
	 * @Description: 得到Workbook对象
	 * @param inputStream
	 *            文件流
	 * @throws BiffException
	 * @throws IOException
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public XLSUtil(InputStream inputStream) throws BiffException, IOException {
		try {
			this.work = Workbook.getWorkbook(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (BiffException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @Title: getSheet
	 * @Description: 得到Sheet，像数组一样，下标从0开始
	 * @param number
	 *            Sheet数
	 * @return Sheet
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public Sheet getSheet(int number) {
		try {
			return work.getSheet(number);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @Title: getRows
	 * @Description: 得到总行数
	 * @param sheet
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public int getRows(Sheet sheet) {
		return sheet.getRows();
	}

	/**
	 * @Title: getCells
	 * @Description: 得到总列数
	 * @param sheet
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public int getCells(Sheet sheet) {
		return sheet.getColumns();
	}

	/**
	 * 得到sheet表格中每一个单元格的具体数据
	 * 
	 * @param sheet
	 * @param cell
	 *            列
	 * @param row
	 *            行
	 * @return
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public String getDate(Sheet sheet, int cell, int row) {
		return sheet.getCell(cell, row).getContents();
	}

	/**
	 * @Title: write
	 * @Description: 生成Excel文件(不支持追加写入)
	 * @param
	 * @param path
	 *            生成文件路径
	 * @param content
	 *            内容
	 * @author wanyong
	 * @date 2012-12-27 下午03:11:31
	 */
	public void write(String path, List<List<String>> content) {
		WritableWorkbook writableWorkbook = null;
		String folder = path.substring(0, path.lastIndexOf("/"));
		File file = new File(folder);
		// 检查文件夹是否存在
		if (!file.exists() && !file.isDirectory()) {
			// 创建
			file.mkdirs();
		}
		try {
			// 新建excel的工作薄文件
			writableWorkbook = Workbook.createWorkbook(new File(path));
			// 生成名为"Sheet1"的工作表，参数0表示这是第一页
			WritableSheet sheet1 = writableWorkbook.createSheet("Sheet1", 0);
			for (int i = 0; i < content.size(); i++) {
				// 每行
				List<String> columns = content.get(i);
				for (int j = 0; j < columns.size(); j++) {
					// 单元格
					// 在label对象的构造方法中指名单元格位置是第一列，第一行(0,0)
					Label label = new Label(j, i, columns.get(j));
					// 将定义好的单元格添加到工作表中
					sheet1.addCell(label);
				}
			}
			writableWorkbook.write();
		} catch (Exception e) {
			log.error("写入Excel文件异常：" + e.getMessage(), e);
		} finally {
			try {
				if (writableWorkbook != null) {
					writableWorkbook.close();
				}
			} catch (Exception e2) {
				log.error("关闭Excel文件异常：" + e2.getMessage(), e2);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		XLSUtil xlsUtil = new XLSUtil();
		System.out.println(xlsUtil.getClass().getResource("/"));
	}

}
