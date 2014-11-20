package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.CheckFileParseInfDao;
import com.umpay.hfmng.model.CheckFileParseInf;

/**
 * @ClassName: CheckFileParseInfDaoImpl
 * @Description: 对账文件解析管理数据库处理类
 * @author wanyong
 * @date 2013-3-5 下午02:52:24
 */
@Repository("checkFileParseInfDaoImpl")
public class CheckFileParseInfDaoImpl extends
		EntityOffLineDaoImpl<CheckFileParseInf> implements CheckFileParseInfDao {

	public void insertCheckFileParseInf(CheckFileParseInf checkFileParseInf) {
		super.save("CheckFileParseInf.insert", checkFileParseInf);
	}

	public int updateCheckFileParseInf(CheckFileParseInf checkFileParseInf) {
		return super.update("CheckFileParseInf.update", checkFileParseInf);
	}

	public int deleteCheckFileParseInf(CheckFileParseInf checkFileParseInf) {
		return super.update("CheckFileParseInf.delete", checkFileParseInf);
	}

	@SuppressWarnings("unchecked")
	public int findCount(String fileName, int fileType, int fileState,
			String merId) {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("fileName", fileName);
		whereMap.put("fileType", fileType);
		whereMap.put("fileState", fileState);
		whereMap.put("merId", merId);
		Map<String, Integer> reMap = (HashMap<String, Integer>) super.get(
				"CheckFileParseInf.find_count", whereMap);
		return reMap.get("NUM");
	}
}
