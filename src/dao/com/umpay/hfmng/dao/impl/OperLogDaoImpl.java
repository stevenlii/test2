/** *****************  JAVA头文件说明  ****************
 * file name  :  OperLogDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-16
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.OperLogDao;
import com.umpay.hfmng.model.OperLog;


/** ******************  类说明  *********************
 * class       :  OperLogDaoImpl
 * @author     :  xhf
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository
public class OperLogDaoImpl extends EntityDaoImpl<OperLog> implements OperLogDao{
	public void insert(OperLog operLog){
		operLog.setOperId(TimeUtil.date8().substring(2, 8) 
				+ SequenceUtil.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_OPERLOG), 10));
		super.insert(operLog);
	}
}
