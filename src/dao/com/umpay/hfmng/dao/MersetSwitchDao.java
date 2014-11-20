/**
 * @Title: MersetSwitchDao.java
 * @Package com.umpay.hfmng.dao
 * @Description: TODO
 * @author MARCO
 * @date 2014-6-5 下午4:26:20
 * @version V1.0
 */

package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.model.MersetSwitch;

public interface MersetSwitchDao {
	/**
	  * selectAll
	  * @Title: selectAll
	  * @Description: TODO
	  * @param @return    
	  * @return List<MersetSwitch> 
	  * @throws
	 */
	public List<MersetSwitch> selectOpen();
	public List<MersetSwitch> selectClosed();
	
	public void insertSwitch(MersetSwitch msSwitch);
	
	public void updateSwitch(MersetSwitch msSwitch);
	public void updateSwitch2(MersetSwitch msSwitch);
	public void updateSwitch3(MersetSwitch msSwitch);
}
