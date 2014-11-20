package com.umpay.hfmng.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.umpay.hfmng.base.PageBean;

/**
 *分页共通处理类 
 * 
 */
public class PageUtil {
	public PageBean getPageBean(HttpServletRequest request) {
		int pageSize = 10;
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		int start;
		try {
			start = Integer.parseInt(startStr);
		} catch (NumberFormatException e) {
			start = 0;
		}
		int limit;
		try {
			limit = Integer.parseInt(limitStr);
		} catch (Exception e) {
			limit =pageSize;
		}
		int currentPage=start/limit+1;
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(currentPage);
		pageBean.setLength(limit);
		return pageBean;
	}

	public List<Integer> getPageNos(int currentPage, int totalPage, int size) {
		List<Integer> nosList = new ArrayList<Integer>();
		if (totalPage <= size) {
			for (int i = 1; i <= totalPage; i++) {
				nosList.add(i);
			}
		} else if (currentPage <= ((size + 1) / 2)) {
			for (int i = 1; i <= size; i++) {
				nosList.add(i);
			}
		} else if (currentPage > (totalPage - (size + 1) / 2)) {
			for (int i = totalPage - size + 1; i <= totalPage; i++) {
				nosList.add(i);
			}
		} else {
			for (int i = currentPage - ((size - 1) / 2); i <= currentPage
					+ ((size - 1) / 2); i++) {
				nosList.add(i);
			}
		}
		return nosList;
	}
//	public PageBean<T> getPageBean(HttpServletRequest request) {
//	int pageSize = 10;
//	String pageLengthS = request.getParameter("numPerPage");
//
//	String currentPageS = request.getParameter("pageNum");
//	int pageLength;
//	try {
//		pageLength = Integer.parseInt(pageLengthS);
//	} catch (NumberFormatException e) {
//		pageLength = pageSize;
//	}
//	int currentPage;
//	try {
//		currentPage = Integer.parseInt(currentPageS);
//	} catch (Exception e) {
//		currentPage =1;
//	}
//	PageBean<T> pageBean = new PageBean<T>();
//	pageBean.setCurrentPage(currentPage);
//	pageBean.setLength(pageLength);
//	return pageBean;
//}
}
