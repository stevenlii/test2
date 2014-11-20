package com.umpay.hfmng.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXUtil {

	private XSSFWorkbook xssfWorkbook;

	/**
	 * 得到XSSFWorkbook对象
	 * 
	 * @param filePath
	 *            文件路径
	 * @throws IOException
	 */
	public XLSXUtil(String filePath) throws IOException {
		try {
			InputStream is = new FileInputStream(filePath);
			this.xssfWorkbook = new XSSFWorkbook(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 得到XSSFWorkbook对象
	 * 
	 * @param inputStream
	 *            文件流
	 * @throws IOException
	 */
	public XLSXUtil(InputStream inputStream) throws IOException {
		try {
			this.xssfWorkbook = new XSSFWorkbook(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 得到Sheet，像数组一样，下标从0开始
	 * 
	 * @param index
	 *            Sheet索引
	 * @return
	 */
	public XSSFSheet getSheet(int index) {
		return xssfWorkbook.getSheetAt(index);
	}

	/**
	 * 得到总行数
	 */
	public int getRows(XSSFSheet xssfSheet) {
		return xssfSheet.getLastRowNum() + 1;

	}

	/**
	 * 得到总列数
	 */
	public int getCells(XSSFRow xssfRow) {
		return xssfRow.getLastCellNum();
	}

	/**
	 * @Title: getRow
	 * @Description: 得到一行数据
	 * @param
	 * @param xssfSheet
	 * @param rowNum
	 * @return
	 * @author wanyong
	 * @date 2012-12-24 上午11:57:34
	 */
	public XSSFRow getRow(XSSFSheet xssfSheet, int rowNum) {
		return xssfSheet.getRow(rowNum);
	}

	/**
	 * @Title: getCell
	 * @Description: 得到一个单元格数据
	 * @param
	 * @param xssfRow
	 * @param cellNum
	 * @return
	 * @author wanyong
	 * @date 2012-12-24 下午02:42:02
	 */
	public XSSFCell getCell(XSSFRow xssfRow, int cellNum) {
		return xssfRow.getCell(cellNum);
	}

	/**
	 * ********************************************
	 * 
	 * @method : getData
	 * @description : 判断单元格中的数据类型并取值
	 * @return : String
	 * @param : @param xssfCell
	 * @param : @return
	 * @modified : xhf , 2012-11-21 下午05:36:09
	 *           *******************************************
	 */
	public String getData(XSSFCell xssfCell) {
		if (xssfCell == null)
			return "";
		if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN)
			return String.valueOf(xssfCell.getBooleanCellValue());
		if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
			return String.valueOf((int) xssfCell.getNumericCellValue());
		return String.valueOf(xssfCell.getStringCellValue());
	}

	public static void main(String[] args) throws Exception {
	}
}
