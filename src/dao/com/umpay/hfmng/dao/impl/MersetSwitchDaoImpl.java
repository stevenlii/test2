/**
 * @Title: MersetSwitchDaoImpl.java
 * @Package com.umpay.hfmng.dao.impl
 * @Description: TODO
 * @author MARCO
 * @date 2014-6-5 下午4:53:22
 * @version V1.0
 */

package com.umpay.hfmng.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.MersetSwitchDao;
import com.umpay.hfmng.model.MersetSwitch;
@Repository("mersetSwitchDao")
public class MersetSwitchDaoImpl extends EntityOffLineDaoImpl<MersetSwitch> implements MersetSwitchDao {

	public List<MersetSwitch> selectOpen() {
		return this.find("MersetSwitch.selectopen");
	}
	
	public List<MersetSwitch> selectClosed() {
		return this.find("MersetSwitch.selectclosed");
	}
	
	public void insertSwitch(MersetSwitch msSwitch){
		this.insert(msSwitch);
	}
	
	public void updateSwitch(MersetSwitch msSwitch){
		this.update(msSwitch);
	}

	public void updateSwitch2(MersetSwitch msSwitch) {
		this.update("MersetSwitch.Update2", msSwitch);
	}
	
	public void updateSwitch3(MersetSwitch msSwitch) {
		this.update("MersetSwitch.Update3", msSwitch);
	}
}
