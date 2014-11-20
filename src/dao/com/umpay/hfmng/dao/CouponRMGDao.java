package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponRMG;

/**
 * @ClassName: CouponRMGDao
 * @Description: TODO
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-11-4
 */
public interface CouponRMGDao extends EntityDao<CouponRMG> {
	public void insertCouponRMGBatch(List<CouponRMG> list)throws Exception;
}
