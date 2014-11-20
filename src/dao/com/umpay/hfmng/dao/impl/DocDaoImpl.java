package com.umpay.hfmng.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.DocDao;
import com.umpay.hfmng.model.DocInfo;

@Repository("docDaoImpl")
public class DocDaoImpl extends EntityOffLineDaoImpl<DocInfo> implements DocDao{

	public void insert(DocInfo doc) throws DataAccessException {
		doc.setId(TimeUtil.date8().substring(2, 8) + 
				SequenceUtil.formatSequence(SequenceUtil.getInstance()
						.getSequence4File(Const.SEQ_FILENAME_DOC), 10));
		super.insert(doc);

	}
}
