package com.umpay.hfmng.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.dao.ParameterLoadDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.service.ParameterLoadService;

/**
 * @ClassName: ParameterLoadServiceImpl
 * @Description: 系统及业务参数缓存加载服务接口
 * @author wanyong
 * @date 2013-1-6 下午09:39:42
 */
@Service("parameterLoadServiceImpl")
public class ParameterLoadServiceImpl implements ParameterLoadService {

	public static Logger log = Logger.getLogger(ParameterLoadServiceImpl.class);

	@Autowired
	private ParameterLoadDao parameterLoadDao;

	/**
	 * @Title: loadAll
	 * @Description: 加载所有参数
	 * @param
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-6 下午09:42:22
	 */
	@PostConstruct
	public void loadAll() throws BusinessException, DataAccessException {
		log.info("******** 开始加载系统及业务参数缓存 ********");
		List<Para> paras = parameterLoadDao.findAllParas("coupon");
		for (Para para : paras) {
			para.trim();
			if ("SV_审核状态".equals(para.getParaType())) {
				ParameterPool.auditStates.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_兑换券状态".equals(para.getParaType())) {
				ParameterPool.couponInfStates.put(para.getParaCode(), para.getParaValue());
			} else if ("BP_兑换券业务类型".equals(para.getParaType())) {
				ParameterPool.couponInfTypes.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_规则状态".equals(para.getParaType())) {
				ParameterPool.couponRuleStates.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_批次状态".equals(para.getParaType())) {
				ParameterPool.couponBatchStates.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_兑换码状态".equals(para.getParaType())) {
				ParameterPool.couponCodeStates.put(para.getParaCode(), para.getParaValue());
			} else if ("BV_兑换码形式".equals(para.getParaType())) {
				ParameterPool.couponCodeTypes.put(para.getParaCode(), para.getParaValue());
			} else if ("BV_兑换码使用有效期类型".equals(para.getParaType())) {
				ParameterPool.couponCodeEffTypes.put(para.getParaCode(), para.getParaValue());
			} else if ("BV_兑换码生成方式".equals(para.getParaType())) {
				ParameterPool.couponCodeMethod.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_日志操作类型".equals(para.getParaType())) {
				ParameterPool.couponLogOptTypes.put(para.getParaCode(), para.getParaValue());
			} else if ("SP_后台参数".equals(para.getParaType())) {
				ParameterPool.couponSystemBackParas.put(para.getParaCode(), para.getParaValue());
			} else if ("BP_后台参数".equals(para.getParaType())) {
				ParameterPool.couponBusiBackParas.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_交易状态".equals(para.getParaType())) {
				ParameterPool.couponTransStates.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_订单状态".equals(para.getParaType())) {
				ParameterPool.couponOrderStates.put(para.getParaCode(), para.getParaValue());
			} else if ("BP_平台名称".equals(para.getParaType())) {
				ParameterPool.platNames.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_定时任务监控状态".equals(para.getParaType())) {
				ParameterPool.timerTaskStates.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_接入类型".equals(para.getParaType())) {
				ParameterPool.couponJoinTypes.put(para.getParaCode(), para.getParaValue());
			} else if ("BV_兑换码兑换方式".equals(para.getParaType())) {
				ParameterPool.couponCodeExchangeTypes.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_商户兑换电话状态".equals(para.getParaType())) {
				ParameterPool.couponMerTelStates.put(para.getParaCode(), para.getParaValue());
			} else if ("SV_账单操作类型".equals(para.getParaType())) {
				ParameterPool.stlStates.put(para.getParaCode(), para.getParaValue());
			} else if ("BV_结算账期".equals(para.getParaType())) {
				ParameterPool.stlCycles.put(para.getParaCode(), para.getParaValue());
			}

		}
		log.info("******** 加载系统及业务参数缓存完成 ********");
	}

}
