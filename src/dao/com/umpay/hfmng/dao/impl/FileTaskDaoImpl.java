package com.umpay.hfmng.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.FileTaskDao;
import com.umpay.hfmng.model.CheckFileParseInf;
import com.umpay.hfmng.model.MerInfo;

@Repository("fileTaskDao")
public class FileTaskDaoImpl extends
		EntityOffLineDaoImpl<CheckFileParseInf> implements FileTaskDao {

	/**
	 * 查询商户ID集合
	 */
	@SuppressWarnings("unchecked")
	public List<MerInfo> queryMerInf() {
		Map<String, String> pkMap = new HashMap<String, String>();
		return (List<MerInfo>) this.find("CheckFileParseInf.allMers", pkMap);
	}

	/**
	 * 插入单个任务
	 */
	public void insertFileTask(CheckFileParseInf fileTask) {
		try {
			this.save("CheckFileParseInf.insert", fileTask);
		} catch (Exception e) {
			if (e.getMessage().indexOf("SQLSTATE=23505") != -1) {
				// 忽略主键异常
			} else {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 插入多条任务
	 */
	public void insertFileTask(List<MerInfo> listMer, Timestamp[] times,
			int[] fileTypes) {
		CheckFileParseInf fileTask;
		SimpleDateFormat sdfD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfY = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdfFileDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfFileMonDate = new SimpleDateFormat("yyyy-MM");
		for (int i = 0; i < fileTypes.length; i++) {
			for (int j = 0; j < times.length; j++) {
				for (int k = 0; k < listMer.size(); k++) {
					String fileName;
					String statDate;
					if (fileTypes[i] == 3) {
						fileName = listMer.get(k).getMerId() + "."
								+ sdfY.format(times[j]);
						statDate = sdfFileMonDate.format(times[j]);
					} else {
						fileName = listMer.get(k).getMerId() + "."
								+ sdfD.format(times[j]);
						statDate = sdfFileDate.format(times[j]);
					}
					fileTask = new CheckFileParseInf();
					fileTask.setFileName(fileName);
					fileTask.setFileState(1);
					fileTask.setFileType(fileTypes[i]);
					fileTask.setDealTimes(0);
					fileTask.setMerId(listMer.get(k).getMerId());
					fileTask.setStatDate(statDate);
					insertFileTask(fileTask);
				}
			}
		}
	}

}
