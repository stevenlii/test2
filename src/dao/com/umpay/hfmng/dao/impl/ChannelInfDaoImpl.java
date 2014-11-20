/**
 * @ClassName: ChannelInfDaoImpl
 * @Description: TODO
 * @author panyouliang
 * @date 2013-3-25 下午2:57:41
 */
package com.umpay.hfmng.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;


import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ChannelInfDao;
import com.umpay.hfmng.model.ChannelInf;

/**
 * @author MARCO.PAN
 *
 */
@Repository("channelInfDaoImpl")
public class ChannelInfDaoImpl extends EntityDaoImpl<ChannelInf> implements
		ChannelInfDao {

	public List<ChannelInf> getChannelInfs() {
		return (List<ChannelInf>) super.find("ChannelInf.getChannelInfs");
	}

}
