/**
 * @ClassName: ChannelInfDao
 * @Description: TODO
 * @author panyouliang
 * @date 2013-3-25 下午2:55:43
 */
package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.ChannelInf;

/**
 * @author MARCO.PAN
 *
 */
public interface ChannelInfDao extends EntityDao<ChannelInf> {
	/**
	 * @Title: getChannelInfs
	 * @Description: 获取所有渠道实体接口
	 * @param
	 * @return
	 * @author panyouliang
	 * @date 2013-03-25 下午05:58:14
	 */
	public List<ChannelInf> getChannelInfs();
}
