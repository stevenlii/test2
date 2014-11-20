package com.umpay.hfmng.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

public interface XERevokeService {
	
	/**
	 * ********************************************
	 * method name   : batchRevokeFromFile 
	 * description   : 批量发起小额解冻请求（来自文件）
	 * @return       : String
	 * @param        : @param file
	 * @param        : @param batchProv
	 * @param        : @throws DataAccessException IOException 
	 * @author       : chenwei
	 * @param batchProv 
	 * @date         : 2013-7-8 
	 * ********************************************
	 */
	String batchRevokeFromFile(MultipartFile file, String batchProv) throws DataAccessException, IOException;

	/**
	 * ********************************************
	 * method name   : listFile 
	 * description   : 获取目录下面的所有文件名
     * @param        : path 文件路径  
     * @param        : suffix 后缀名, 为空则表示所有文件  
     * @param        : isdepth 是否遍历子目录  
     * @return       : list
	 * @author       : chenwei
	 * @date         : 2013-7-12 
	 * ********************************************
	 */
	List<Map<String, Object>> listFile(List<Map<String, Object>> data,File f, String suffix, boolean isdepth);

	File getDownloadFile(String fileName, String flag);
}