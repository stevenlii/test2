package com.umpay.hfmng.dao;

import java.sql.Timestamp;
import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CheckFileParseInf;
import com.umpay.hfmng.model.MerInfo;

public interface FileTaskDao extends EntityDao<CheckFileParseInf> {

	public List<MerInfo> queryMerInf();

	public void insertFileTask(List<MerInfo> listMer, Timestamp[] times,
			int[] fileTypes);
}
