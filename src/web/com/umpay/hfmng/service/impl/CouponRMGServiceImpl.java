package com.umpay.hfmng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.CouponRMGDao;
import com.umpay.hfmng.model.CouponRMG;
import com.umpay.hfmng.service.CouponRMGService;

/**
 * @ClassName: CouponRMGServiceImpl
 * @Description: TODO
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-11-4
 */
@Service
public class CouponRMGServiceImpl implements CouponRMGService {
	@Autowired
	private CouponRMGDao couponRMGDao;
	/* (non-Javadoc)
	 * @see com.umpay.hfmng.service.CouponRMGService#queryRMG(java.lang.String)
	 */
	public List<CouponRMG> queryRMG(String ruleid) throws Exception {
		CouponRMG rmg = new CouponRMG();
		rmg.setRuleid(ruleid);
		return couponRMGDao.findBy(rmg);
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.service.CouponRMGService#deleteRMG(java.lang.String)
	 */
	public int deleteRMG(String ruleid) throws Exception {
		CouponRMG rmg = new CouponRMG();
		rmg.setRuleid(ruleid);
		couponRMGDao.delete(rmg);
		return 1;
	}

}
