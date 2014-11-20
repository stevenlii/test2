package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;

public interface OptionDao extends EntityDao<GoodsTypeModel> {

	/**
	 * @Title: getGoodsList
	 * @Description: 根据商户ID获取商品ID及名称列表接口
	 * @param
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午09:53:53
	 */
	public List<GoodsInfo> getGoodsList(Map<String, String> merId);

	public List<GoodsTypeModel> getGoodsType();

	// public List<MerTypeModel> getMerType();
}
