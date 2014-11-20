/**
 * @Title: MersetSwitchService.java
 * @Package com.umpay.hfmng.service
 * @Description: 商户账单配置
 * @author MARCO
 * @date 2014-6-5 下午3:56:57
 * @version V1.0
 */

package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.MersetSwitch;


public interface MersetSwitchService {
	public List<MersetSwitch> mersetValues();
	public void updateMersetSwitch(String merids)throws Exception;
}
