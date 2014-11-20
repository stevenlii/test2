package com.umpay.hfmng.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCodec;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.XLSUtil;
import com.umpay.hfmng.common.XLSXUtil;
import com.umpay.hfmng.dao.CouponBatchDao;
import com.umpay.hfmng.dao.CouponCodeDao;
import com.umpay.hfmng.dao.CouponRuleDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponBatch;
import com.umpay.hfmng.model.CouponCode;
import com.umpay.hfmng.model.CouponRule;
import com.umpay.hfmng.service.CouponBatchService;
import com.umpay.hfmng.service.CouponCodeService;
import com.umpay.hfmng.service.CouponRuleService;
import com.umpay.sso.org.User;

/**
 * @ClassName: CouponBatchServiceImpl
 * @Description: 兑换码批量导入批次业务处理
 * @author wanyong
 * @date 2012-12-27 下午05:51:16
 */
@Service
public class CouponBatchServiceImpl implements CouponBatchService {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private CouponCodeService couponCodeService;

	@Autowired
	private CouponRuleService couponRuleService;

	@Autowired
	private CouponBatchDao couponBatchDao;

	@Autowired
	private CouponRuleDao couponRuleDao;

	@Autowired
	private CouponCodeDao couponCodeDao;

	/**
	 * @Title: getBatchId
	 * @Description: 根据规则ID获取批次ID，批次ID=规则ID+批次序列
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2013-1-15 下午08:53:52
	 */
	public String getBatchId(String ruleId) throws BusinessException, DataAccessException {
		// 查询规则下最大批次ID
		String maxBatchId = couponBatchDao.findMaxBatchId(ruleId);
		if (maxBatchId == null || "".equals(maxBatchId))
			maxBatchId = ruleId + "1";
		else {
			maxBatchId = maxBatchId.substring(ruleId.length());
			maxBatchId = ruleId + (Integer.parseInt(maxBatchId) + 1);
		}
		return maxBatchId;
	}

	/**
	 * @Title: importBatchForXls
	 * @Description: Excel2003兑换码文件批量导入业务处理
	 * @param stream
	 *            文件流
	 * @param couponRule
	 *            所属兑换券规则
	 * @param couponBatch
	 *            批次信息，新批次导入时为NULL
	 * @param type
	 *            导入类型：0-新批次，1-旧批次追加
	 * @param errorPath
	 *            错误文件保存路径
	 * @param user
	 *            session用户
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @return 操作成功描述
	 * @author wanyong
	 * @date 2013-1-1 上午01:15:45
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String importBatchForXls(InputStream stream, CouponRule couponRule, CouponBatch couponBatch, String type,
			String errorPath, User user) throws BusinessException, DataAccessException {
		try {
			// 2003版本，使用JXL方式解析
			XLSUtil xlsUtil = new XLSUtil(stream);
			/*** start 效验 ********************************************/
			// 取出第一行数据进行兑换券、商户、商品效验
			// 样例数据【商户编号: 12345 商品编号: 9 兑换券编号: 5555555】
			String inMerId = xlsUtil.getDate(xlsUtil.getSheet(0), 1, 0);
			if (!inMerId.equals(couponRule.getMerId()))
				throw new BusinessException("待导入文件头与该兑换券规则不匹配");
			String inGoodsId = xlsUtil.getDate(xlsUtil.getSheet(0), 3, 0);
			if (!inGoodsId.equals(couponRule.getGoodsId()))
				throw new BusinessException("待导入文件头与该兑换券规则不匹配");
			String inCouponId = xlsUtil.getDate(xlsUtil.getSheet(0), 5, 0);
			if (!inCouponId.equals(couponRule.getCouponId()))
				throw new BusinessException("待导入文件头与该兑换券规则不匹配");

			if ("0".equals(type)) {
				// 新批次
				// 检查批次兑换码销售期是否跟已有批次有重叠时间
				Map<String, Object> whereMap = new HashMap<String, Object>(); // 查询条件MAP
				whereMap.put("ruleId", couponRule.getRuleId());
				whereMap.put("state", Const.COUPON_BATCHSTATE_ENABLE); // 查询条件状态：启用
				whereMap.put("sellStartDate", couponRule.getSellStartDate());
				whereMap.put("sellEndDate", couponRule.getSellEndDate());
				if (couponBatchDao.queryDisBatchCount(whereMap) > 0)
					throw new BusinessException("销售时间与其它一起用的批次销售时间重叠");
			}
			/*** end 效验 *********************************************/

			/*** start 初始化错误数据 *********************************************/
			List<List<String>> errorList = new ArrayList<List<String>>(); // 错误数据
			// 初始化错误文件头
			List<String> headList = new ArrayList<String>();
			headList.add("商户编号");
			headList.add(inMerId);
			headList.add("商品编号");
			headList.add(inGoodsId);
			headList.add("兑换券编号");
			headList.add(inCouponId);
			errorList.add(headList);
			// 初始化标题
			List<String> titleList = new ArrayList<String>();
			titleList.add("电子码");
			titleList.add("错误原因");
			errorList.add(titleList);
			/*** end 初始化错误数据 *********************************************/

			// 如果是旧批次追加则需要获取旧批次的吗使用开始截止时间
			if ("1".equals(type)) {
				// 旧批次更新批次库存
				CouponBatch oldCouponBatch = loadCouponBatch(couponBatch.getBatchId());
				couponRule.setUseStartDate(oldCouponBatch.getUseStartDate());
				couponRule.setUseEndDate(oldCouponBatch.getUseEndDate());
			}
			CouponRule useCouponRule = couponRuleService.load(couponRule.getRuleId()); // 获取启用状态的兑换券规则信息
			// 只取第一个Sheet
			int rows = xlsUtil.getRows(xlsUtil.getSheet(0));
			int sucessCount = 0; // 成功导入的兑换码数量
			// 模板第二行为标题行，真正数据从第三行开始取
			for (int i = 2; i < rows; i++) {
				// 取每一行的第一列作为兑换码值
				String code = xlsUtil.getDate(xlsUtil.getSheet(0), 0, i);
				log.debug("兑换码：【" + code + "】");
				// 兑换码最大长度为12位
				if (code == null || "".equals(code) || code.length() > 12) {
					// 兑换码格式有误，生成错误文件
					List<String> dataList = new ArrayList<String>();
					dataList.add(code);
					dataList.add("最大长度超出12位或电子码为空"); // 错误原因
					errorList.add(dataList);
					continue;
				}
				// 加密兑换码
				String enCode = HfCodec.encrypt(code);

				/******** 2013-04-07 更新成主键冲突异常方式
				 *  // 查询兑换码是否重复 if
				 * (couponCodeService.exists(enCode, inMerId)) { //
				 * 已存在的兑换码，生成错误文件 List<String> dataList = new
				 * ArrayList<String>(); dataList.add(code);
				 * dataList.add("已存在的电子码"); // 错误原因 errorList.add(dataList);
				 * continue; }
				 **********/

				CouponCode couponCode = new CouponCode();
				couponCode.setCouponCode(enCode);
				couponCode.setUseStartDate(couponRule.getUseStartDate());
				couponCode.setUseEndDate(couponRule.getUseEndDate());
				couponCode.setCouponCodeType(useCouponRule.getCouponCodeType());
				couponCode.setAlarmCount(useCouponRule.getAlarmCount());
				couponCode.setCouponId(couponRule.getCouponId());
				couponCode.setMerId(couponRule.getMerId());
				couponCode.setGoodsId(couponRule.getGoodsId());
				couponCode.setRuleId(couponRule.getRuleId());
				couponCode.setBatchId(couponBatch.getBatchId());
				couponCode.setState(Const.COUPON_CODESTATE_INIT); // 默认批量导入时兑换码的状态为：初始
				try {
					// 兑换码入库
					couponCodeService.saveCouponCode(couponCode);
					sucessCount++;
				} catch (DataAccessException e) {
					if (e.getMessage().indexOf("SQLSTATE=23505") >= 0) {
						// 主键冲突异常，已存在的兑换码，生成错误文件
						List<String> dataList = new ArrayList<String>();
						dataList.add(code);
						dataList.add("已存在的电子码"); // 错误原因
						errorList.add(dataList);
						continue;
					} else
						throw e;
				}
			}
			if ("0".equals(type)) {
				// 新批次入库
				CouponBatch newCouponBatch = new CouponBatch();
				newCouponBatch.setBatchId(couponBatch.getBatchId());
				newCouponBatch.setRuleId(couponRule.getRuleId());
				newCouponBatch.setGoodsSum(sucessCount);
				newCouponBatch.setSellStartDate(couponRule.getSellStartDate());
				newCouponBatch.setSellEndDate(couponRule.getSellEndDate());
				newCouponBatch.setUseStartDate(couponRule.getUseStartDate());
				newCouponBatch.setUseEndDate(couponRule.getUseEndDate());
				newCouponBatch.setModUser(user.getId());
				newCouponBatch.setState(Const.COUPON_BATCHSTATE_NOENABLE); // 默认状态为：未启用
				saveCouponBatch(newCouponBatch);
			} else if ("1".equals(type)) {
				// 旧批次更新批次库存
				couponBatch.setGoodsSum(sucessCount);
				couponBatch.setModUser(user.getId());
				couponBatchDao.updateGoodsSum(couponBatch);
			}
			String downMessageContent = ""; // 默认下载信息为空
			// 生成错误文件
			if (errorList.size() > 2) { // 前面两行为标题头
				String errorFileName = couponBatch.getBatchId() + ".xls";
				errorPath += "/" + errorFileName;
				xlsUtil.write(errorPath, errorList);
				downMessageContent = "点击 <a href='/hfMngBusi/" + ParameterPool.couponSystemBackParas.get("ERRFILEPATH")
						+ "/" + errorFileName + "'>下载</a> 错误详情";
			}
			int dataRows = rows - 2; // 实际兑换码条数，需要减去标题头
			return "共导入[" + dataRows + "]条数据，成功导入[" + sucessCount + "]条，导入失败[" + (dataRows - sucessCount) + "]条！"
					+ downMessageContent;
		} catch (BusinessException busiException) {
			throw busiException;
		} catch (BiffException biffException) {
			throw new BusinessException("导入文件读取失败！");
		} catch (IOException ioException) {
			throw new BusinessException("导入文件读取失败！");
		}

	}

	/**
	 * @Title: importBatchForXlsx
	 * @Description: Excel2007兑换码文件批量导入业务处理
	 * @param stream
	 *            文件流
	 * @param couponRule
	 *            所属兑换券规则
	 * @param couponBatch
	 *            批次信息，新批次导入时为NULL
	 * @param type
	 *            导入类型：0-新批次，1-旧批次追加
	 * @param errorPath
	 *            错误文件保存路径
	 * @param user
	 *            session用户
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-1 上午01:18:18
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String importBatchForXlsx(InputStream stream, CouponRule couponRule, CouponBatch couponBatch, String type,
			String errorPath, User user) throws BusinessException, DataAccessException {
		try {
			// 2007版本，使用POI方式解析
			XLSXUtil xlsxUtil = new XLSXUtil(stream);
			/*** start 效验 ********************************************/
			// 取出第一行数据进行兑换券、商户、商品效验
			// 样例数据【兑换券编号: 1239123123商户编号: 12345 商品编号: 9】
			String inMerId = xlsxUtil.getData(xlsxUtil.getCell(xlsxUtil.getSheet(0).getRow(0), 1));
			if (!inMerId.equals(couponRule.getMerId()))
				throw new BusinessException("待导入文件头与该兑换券规则不匹配");
			String inGoodsId = xlsxUtil.getData(xlsxUtil.getCell(xlsxUtil.getSheet(0).getRow(0), 3));
			if (!inGoodsId.equals(couponRule.getGoodsId()))
				throw new BusinessException("待导入文件头与该兑换券规则不匹配");
			String inCouponId = xlsxUtil.getData(xlsxUtil.getCell(xlsxUtil.getSheet(0).getRow(0), 5));
			if (!inCouponId.equals(couponRule.getCouponId()))
				throw new BusinessException("待导入文件头与该兑换券规则不匹配");
			if ("0".equals(type)) {
				// 新批次
				// 检查批次兑换码销售期是否跟已有批次有重叠时间
				Map<String, Object> whereMap = new HashMap<String, Object>(); // 查询条件MAP
				whereMap.put("ruleId", couponRule.getRuleId());
				whereMap.put("state", Const.COUPON_BATCHSTATE_ENABLE); // 查询条件状态：启用
				whereMap.put("sellStartDate", couponRule.getSellStartDate());
				whereMap.put("sellEndDate", couponRule.getSellEndDate());
				if (couponBatchDao.queryDisBatchCount(whereMap) > 0)
					throw new BusinessException("销售时间与其它一起用的批次销售时间重叠");
			}
			/*** end 效验 *********************************************/

			/*** start 初始化错误数据 ********************************************/
			List<List<String>> errorList = new ArrayList<List<String>>(); // 错误数据
			// 初始化错误文件头
			List<String> headList = new ArrayList<String>();
			headList.add("商户编号");
			headList.add(inMerId);
			headList.add("商品编号");
			headList.add(inGoodsId);
			headList.add("兑换券编号");
			headList.add(inCouponId);
			errorList.add(headList);
			// 初始化标题
			List<String> titleList = new ArrayList<String>();
			titleList.add("电子码");
			titleList.add("错误原因");
			errorList.add(titleList);
			/*** end 初始化错误数据 ********************************************/

			// 如果是旧批次追加则需要获取旧批次的吗使用开始截止时间
			if ("1".equals(type)) {
				// 旧批次更新批次库存
				CouponBatch oldCouponBatch = loadCouponBatch(couponBatch.getBatchId());
				couponRule.setUseStartDate(oldCouponBatch.getUseStartDate());
				couponRule.setUseEndDate(oldCouponBatch.getUseEndDate());
			}
			CouponRule useCouponRule = couponRuleService.load(couponRule.getRuleId()); // 获取启用状态的兑换券规则信息
			// 只取第一个Sheet
			int rows = xlsxUtil.getRows(xlsxUtil.getSheet(0));
			int sucessCount = 0; // 成功导入的兑换码数量
			// 模板第二行为标题行，真正数据从第三行开始取
			for (int i = 2; i < rows; i++) {
				// 取每一行的第一列作为兑换码值
				String code = xlsxUtil.getData(xlsxUtil.getSheet(0).getRow(i).getCell(0));
				log.debug("兑换码：【" + code + "】");
				// 兑换码最大长度为12位
				if (code == null || "".equals(code) || code.length() > 12) {
					// 兑换码格式有误，生成错误文件
					List<String> dataList = new ArrayList<String>();
					dataList.add(code);
					dataList.add("最大长度超出12位或电子码为空"); // 错误原因
					errorList.add(dataList);
					continue;
				}
				// 加密兑换码
				String enCode = HfCodec.encrypt(code);

				/******** 2013-04-07 更新成主键冲突异常方式
				 *  // 查询兑换码是否重复 if
				 * (couponCodeService.exists(enCode, inMerId)) { //
				 * 已存在的兑换码，生成错误文件 List<String> dataList = new
				 * ArrayList<String>(); dataList.add(code);
				 * dataList.add("已存在的电子码"); // 错误原因 errorList.add(dataList);
				 * continue; }
				 **********/
				CouponCode couponCode = new CouponCode();
				couponCode.setCouponCode(enCode);
				couponCode.setUseStartDate(couponRule.getUseStartDate());
				couponCode.setUseEndDate(couponRule.getUseEndDate());
				couponCode.setCouponCodeType(useCouponRule.getCouponCodeType());
				couponCode.setAlarmCount(useCouponRule.getAlarmCount());
				couponCode.setCouponId(couponRule.getCouponId());
				couponCode.setMerId(couponRule.getMerId());
				couponCode.setGoodsId(couponRule.getGoodsId());
				couponCode.setRuleId(couponRule.getRuleId());
				couponCode.setBatchId(couponBatch.getBatchId());
				couponCode.setState(Const.COUPON_CODESTATE_INIT); // 默认批量导入时兑换码的状态为：初始
				try {
					// 兑换码入库
					couponCodeService.saveCouponCode(couponCode);
					sucessCount++;
				} catch (DataAccessException e) {
					if (e.getMessage().indexOf("SQLSTATE=23505") >= 0) {
						// 主键冲突异常，已存在的兑换码，生成错误文件
						List<String> dataList = new ArrayList<String>();
						dataList.add(code);
						dataList.add("已存在的电子码"); // 错误原因
						errorList.add(dataList);
						continue;
					} else
						throw e;
				}
			}
			if ("0".equals(type)) {
				// 新批次入库
				CouponBatch newCouponBatch = new CouponBatch();
				newCouponBatch.setBatchId(couponBatch.getBatchId());
				newCouponBatch.setRuleId(couponRule.getRuleId());
				newCouponBatch.setGoodsSum(sucessCount);
				newCouponBatch.setSellStartDate(couponRule.getSellStartDate());
				newCouponBatch.setSellEndDate(couponRule.getSellEndDate());
				newCouponBatch.setUseStartDate(couponRule.getUseStartDate());
				newCouponBatch.setUseEndDate(couponRule.getUseEndDate());
				newCouponBatch.setModUser(user.getId());
				newCouponBatch.setState(Const.COUPON_BATCHSTATE_NOENABLE); // 默认状态为：未启用
				saveCouponBatch(newCouponBatch);
			} else if ("1".equals(type)) {
				// 旧批次更新批次库存
				couponBatch.setGoodsSum(sucessCount);
				couponBatch.setModUser(user.getId());
				couponBatchDao.updateGoodsSum(couponBatch);
			}
			String downMessageContent = ""; // 默认下载信息为空
			// 生成错误文件
			if (errorList.size() > 2) { // 前面两行为标题头
				String errorFileName = couponBatch.getBatchId() + ".xls";
				errorPath += "/" + errorFileName;
				XLSUtil xlsUtil = new XLSUtil();
				xlsUtil.write(errorPath, errorList);
				downMessageContent = "点击 <a href='/hfMngBusi/" + ParameterPool.couponSystemBackParas.get("ERRFILEPATH")
						+ "/" + errorFileName + "'>下载</a> 错误详情";
			}
			int dataRows = rows - 2; // 实际兑换码条数，需要减去标题头
			return "共导入[" + dataRows + "]条数据，成功导入[" + sucessCount + "]条，导入失败[" + (dataRows - sucessCount) + "]条！"
					+ downMessageContent;
		} catch (BusinessException busiException) {
			throw busiException;
		} catch (IOException ioException) {
			throw new BusinessException("导入文件读取失败！");
		}
	}

	/**
	 * @Title: modifyCouponBatch
	 * @Description: 修改兑换码批量导入批次信息
	 * @param
	 * @param couponBatch
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-26 上午11:04:42
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void modifyCouponBatch(CouponBatch couponBatch) throws BusinessException, DataAccessException {
		// 更新批次信息
		couponBatchDao.updateCouponBatch(couponBatch);
		// 由于【兑换码有效期】冗余在码表中，更新批次【兑换码有效期】同时需要更新对应兑换码信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useStartDate", couponBatch.getUseStartDate()); // 待更新有效期
		map.put("useEndDate", couponBatch.getUseEndDate()); // 待更新有效期
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("batchId", couponBatch.getBatchId()); // 条件批次号
		whereMap.put("state", Const.COUPON_CODESTATE_INIT); // 条件状态
		couponCodeDao.updateSelfDefine(map, whereMap);
	}

	/**
	 * @Title: saveCouponBatch
	 * @Description: 保存兑换码批量导入批次信息
	 * @param
	 * @param couponBatch
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-26 上午11:04:54
	 */
	public void saveCouponBatch(CouponBatch couponBatch) throws BusinessException, DataAccessException {
		couponBatchDao.insertCouponBatch(couponBatch);
	}

	/**
	 * @Title: loadCouponBatch
	 * @Description: 加载批次信息
	 * @param
	 * @param batchId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-29 下午04:33:47
	 */
	public CouponBatch loadCouponBatch(String batchId) throws BusinessException, DataAccessException {
		CouponBatch couponBatch = couponBatchDao.getCouponBatch(batchId);
		if (couponBatch != null)
			couponBatch.trim();
		return couponBatch;
	}

	/**
	 * @Title: stopBatch
	 * @Description: 批次停止使用
	 * @param
	 * @param batchId
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-29 下午03:14:42
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void stopBatch(String batchId) throws BusinessException, DataAccessException {
		CouponBatch couponBatch = loadCouponBatch(batchId);
		// 检查批次状态，【启用】、【未启用】状态才能停止
		if (Const.COUPON_BATCHSTATE_ENABLE.equals(couponBatch.getState())
				|| Const.COUPON_BATCHSTATE_NOENABLE.equals(couponBatch.getState())) {
			// 更新批次状态
			CouponBatch inCouponBatch = new CouponBatch();
			inCouponBatch.setBatchId(batchId);
			inCouponBatch.setState(Const.COUPON_BATCHSTATE_STOP); // 停止
			couponBatchDao.updateCouponBatch(inCouponBatch);

			// 更新批次下兑换码状态
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", Const.COUPON_CODESTATE_STOP); // 待更新状态
			Map<String, Object> whereMap = new HashMap<String, Object>();
			whereMap.put("batchId", batchId); // 条件批次
			if (Const.COUPON_BATCHSTATE_NOENABLE.equals(couponBatch.getState())) {
				// 批次状态从【未启用】更新到【停止】，码的状态应该从【初始】更新到【停用】
				whereMap.put("state", Const.COUPON_CODESTATE_INIT); // 条件状态
			} else if (Const.COUPON_BATCHSTATE_ENABLE.equals(couponBatch.getState())) {
				// 批次状态从【启用】更新到【停止】，码的状态应该从【可销售】更新到【停用】
				whereMap.put("state", Const.COUPON_CODESTATE_START); // 条件状态
			}
			int effectRows = couponCodeDao.updateSelfDefine(map, whereMap);

			// 更新批次所属兑换券规则库存
			CouponRule couponRule = new CouponRule();
			couponRule.setRuleId(couponBatch.getRuleId());
			couponRule.setGoodsSum(-effectRows); // 减去库存
			couponRule.setModTime(new Timestamp(System.currentTimeMillis()));
			couponRuleDao.updateGoodsSum(couponRule);
		} else
			throw new BusinessException("批次未处于启用或未启用状态，不能停止使用");
	}

	/**
	 * @Title: startBatch
	 * @Description: 批次启用
	 * @param
	 * @param batchId
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-29 下午03:58:42
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void startBatch(String batchId) throws BusinessException, DataAccessException {
		// 检查批次状态，【未启用】、【停止】状态才能启用
		CouponBatch couponBatch = couponBatchDao.getCouponBatch(batchId);
		if (Const.COUPON_BATCHSTATE_NOENABLE.equals(couponBatch.getState())
				|| Const.COUPON_BATCHSTATE_STOP.equals(couponBatch.getState())) {
			// 检查兑换码销售期是否跟已有批次有重叠时间
			Map<String, Object> checkWhereMap = new HashMap<String, Object>(); // 查询条件MAP
			checkWhereMap.put("ruleId", couponBatch.getRuleId());
			checkWhereMap.put("state", Const.COUPON_BATCHSTATE_ENABLE); // 查询条件状态：启用
			checkWhereMap.put("sellStartDate", couponBatch.getSellStartDate());
			checkWhereMap.put("sellEndDate", couponBatch.getSellEndDate());
			if (couponBatchDao.queryDisBatchCount(checkWhereMap) > 0)
				throw new BusinessException("批次销售期时间有重叠，不能启用");

			// 更新批次下兑换码状态
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("batchId", batchId);
			map.put("state", Const.COUPON_CODESTATE_START); // 待更新状态
			Map<String, Object> whereMap = new HashMap<String, Object>();
			whereMap.put("state", Const.COUPON_CODESTATE_INIT); // 条件状态
			if (Const.COUPON_BATCHSTATE_NOENABLE.equals(couponBatch.getState())) {
				// 批次状态从【未启用】更新到【启用】，码的状态应该从【初始】更新到【启用】
				whereMap.put("state", Const.COUPON_CODESTATE_INIT); // 条件状态
			} else if (Const.COUPON_BATCHSTATE_STOP.equals(couponBatch.getState())) {
				// 批次状态从【停止】更新到【启用】，码的状态应该从【停用】更新到【启用】
				whereMap.put("state", Const.COUPON_CODESTATE_STOP); // 条件状态
			}
			couponCodeDao.updateSelfDefine(map, whereMap);

			// 更新批次状态
			CouponBatch inCouponBatch = new CouponBatch();
			inCouponBatch.setBatchId(batchId);
			inCouponBatch.setState(Const.COUPON_BATCHSTATE_ENABLE); // 启用
			couponBatchDao.updateCouponBatch(inCouponBatch);

			// 更新兑换券规则库存
			CouponRule couponRule = new CouponRule();
			couponRule.setRuleId(couponBatch.getRuleId());
			couponRule.setGoodsSum(couponBatch.getGoodsSum());
			couponRule.setModTime(new Timestamp(System.currentTimeMillis()));
			couponRuleDao.updateGoodsSum(couponRule);
		} else
			throw new BusinessException("批次未处于未启用或停止状态，不能启用");

	}
}
