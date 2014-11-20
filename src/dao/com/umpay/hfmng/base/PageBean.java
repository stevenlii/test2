package com.umpay.hfmng.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *翻页类 
 */
@SuppressWarnings("unchecked")
public class PageBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 当前页数
	 */
	private int currentPage;

	/**
	 * 默认显示条数
	 */
	private int length=10;   

	/**
	 * 总页数
	 */
	private int totalPages;

	/**
	 * 总记录条数
	 */
	private int totalRecords;

	/**
	 * 当前页显示数据的集合
	 */
	private List results = new ArrayList();

	public PageBean() {
		super();
	}

	public PageBean(int currentPage, int length) {
		this.setCurrentPage(currentPage);
		this.setLength(length);
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLength() {
		return length;
	}
	/**
	 * length must greater than 0
	 */
	public void setLength(int length) {
		if(length<1){
			throw new IllegalArgumentException("length must greater than 0 !");
		}
		this.length = length;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int count) {
		totalRecords = count;
		totalPages = totalRecords / length;
		if (totalRecords % length != 0) {
			totalPages++;
		}

		if (totalPages != 0) {
			if (currentPage < 1) {
				currentPage = 1;
			}
			if(currentPage>totalPages){
				currentPage=totalPages;
			}
		} else {
			currentPage = 1;               
		}
	}

	/**
	 * @return
	 */
	public boolean canToFirst() {
		return currentPage > 1;
	}

	/**
	 * @return
	 */
	public boolean canToLast() {
		return currentPage < totalPages;
	}

	/**
	 * @return
	 */
	public boolean canToNext() {
		return currentPage < totalPages;
	}

	/**
	 * @return
	 */
	public boolean canToPre() {
		return currentPage > 1;
	}

	/**
	 * 取查询的开始位置
	 * 
	 * @return
	 */
	public int getRsFirstNumber() {
		return (currentPage - 1) * length + 1;
	}

	public int getRsLastNumber() {
		return currentPage * length + 1;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("totalRecords="+ totalRecords).append(
				";totalPages="+ totalPages).append(";currentPage=" +currentPage)
				.append(";length="+length).toString();
	}
}
