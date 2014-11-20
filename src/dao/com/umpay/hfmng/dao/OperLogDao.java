/** *****************  JAVA头文件说明  ****************
 * file name  :  OperLog.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-16
 * *************************************************/ 

package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.OperLog;


/** ******************  类说明  *********************
 * class       :  OperLog
 * @author     :  xhf
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface OperLogDao extends EntityDao<OperLog> {
	
	public void insert(OperLog operLog);

}
