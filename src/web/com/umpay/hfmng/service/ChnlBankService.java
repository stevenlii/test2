/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-19
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.ChnlBank;


/** ******************  类说明  *********************
 * class       :  ChnlBankService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ChnlBankService {
	
	public ChnlBank load(String channelId, String bankId);
	
	public Map<String, ChnlBank> findBankByChnlId(String channelId);
	
	public String saveChnlBank(ChnlBank chnlBank, List<String> newOpen,
			List<String> modOpen, List<String> modClose);
	
	public String batchUpdate(String[] channelIds, String bankId, int state, String userId);
	
	public String enableAnddisable(String ID,String userId,int action);

}
