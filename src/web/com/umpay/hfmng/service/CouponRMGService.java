package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.CouponRMG;

/**
 * @ClassName: CouponRMGService
 * @Description: TODO
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-11-4
 */
public interface CouponRMGService {
	public List<CouponRMG> queryRMG(String ruleid)throws Exception;
	
	public int deleteRMG(String ruleid)throws Exception;
}
