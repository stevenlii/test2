package com.umpay.hfmng.service;

import org.springframework.web.multipart.MultipartFile;

public interface SegMblService {
	/**
	 * *****************  方法说明  *****************
	 * method name   :  handleSegMbls
	 * @param		 :  @param file
	 * @param		 :  @param flag 0：部分更新（不覆盖已有的数据）；1：全部更新（覆盖已有的数据）
	 * @param		 :  @return
	 * @return		 :  String
	 * @author       :  LiZhen 2013-12-25 下午4:59:36
	 * description   :  处理上传的号段文件
	 * @see          :  
	 * **********************************************
	 */
	public String handleSegMbls(MultipartFile file,String flag,String netType) throws Exception;
}
