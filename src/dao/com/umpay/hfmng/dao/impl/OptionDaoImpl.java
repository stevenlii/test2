package com.umpay.hfmng.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.OptionDao;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;

@Repository
public class OptionDaoImpl extends EntityDaoImpl<GoodsTypeModel> implements
		OptionDao {

	/**
	 * @Title: getGoodsList
	 * @Description: 根据商户ID获取商品ID及名称列表
	 * @param
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午09:57:15
	 */
	@SuppressWarnings("unchecked")
	public List<GoodsInfo> getGoodsList(Map<String, String> merId) {
		return (List<GoodsInfo>) this.find("GoodsInfo.Get", merId);
	}

	@SuppressWarnings("unchecked")
	public List<GoodsTypeModel> getGoodsType() {
		// TODO Auto-generated method stub
		List<GoodsTypeModel> list = super.find("GoodsType.Get");
		return list;
	}

	// public List<MerTypeModel> getMerType() {
	// List<MerTypeModel> list=super.find("MerType.Get");
	// return list;
	// }

}
