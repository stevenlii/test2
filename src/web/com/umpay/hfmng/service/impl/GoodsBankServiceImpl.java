/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsBankServiceImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-9
 * *************************************************/

package com.umpay.hfmng.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.GoodsBankDao;
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.GoodsBankService;
import com.umpay.hfmng.service.OptionService;

/**
 * ****************** 类说明 ********************* class : GoodsBankServiceImpl
 * 
 * @author : zhaojunbao
 * @version : 1.0 description :
 * @see :
 * ************************************************/
@Service
public class GoodsBankServiceImpl implements GoodsBankService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GoodsInfoDao goodsInfoDao;
	@Autowired
	private GoodsBankDao goodsBankDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MerBankDao merBankDao;
	@Autowired
	private AuditService auditService;
	@Autowired
	private OptionService optionService;


	/**
	 * ******************************************** method name :
	 * getGoodsIdByMerId modified : zhaojunbao , 2012-10-9
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.GoodsBankService#getGoodsIdByMerId(java
	 *      .lang.String)
	 * ********************************************/
	public List<GoodsInfo> getGoodsIdByMerId(Map mapwhere) {
		return goodsInfoDao.getGoodsIdByMerId(mapwhere);
	}

	/**
	 * ******************************************** method name :
	 * insertGoodsBankAudit modified : zhaojunbao , 2012-10-9
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String insertGoodsBankAudit(GoodsBank goodsBank) {
		String bankIds = StringUtils.trim(goodsBank.getBankId());
		String[] bankidArray = bankIds.split(",");
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		String result = "0"; // 操作结果 1表示成功 0 表示失败
		try {
			for (int i = 0; i < bankidArray.length; i++) {
				GoodsBank gBank = goodsBank;
				gBank.setBankId(bankidArray[i].trim());
				String jsonString = JsonHFUtil.getJsonArrStrFrom(gBank);
				Audit audit = new Audit();
				audit.setTableName("UMPAY.T_GOODS_BANK");
				audit.setModData(jsonString);
				audit.setState("0");
				audit.setCreator(LoginUtil.getUser().getId());
				audit.setAuditType("1"); // 审核类型为1 新增
				audit.setDesc(gBank.getDesc());
				audit.setIxData(gBank.getMerId().trim() + "-" + gBank.getGoodsId().trim() + "-"
						+ gBank.getBankId().trim());

				// ixData2 索引字段使用 商品名称 填充，提供商品银行操作日志时索引字段
				if (gBank.getMerId() != null && gBank.getGoodsId() != null) {
					GoodsInfo goods = (GoodsInfo) goodsMap.get(gBank.getMerId().trim() + "-"
							+ gBank.getGoodsId().trim());
					if (goods != null) {
						audit.setIxData2(goods.getGoodsName().trim()); // 商品名称
					}
				}
				auditDao.insert(audit);
				log.info("审核表中成功插入商品银行配置信息" + audit.toString());

				/*
				 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。
				 * 需求开发人：万勇（工单号：PKS00005415）
				 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作
				 * 需求开发时间：2014-04-15
				 */
				auditService.goodsBankauditPass(new String[] { audit.getId() }, LoginUtil.getUser());

				result = "1"; // 返回成功
			}
		} catch (Exception e) {
			log.error("审核表插入信息出错", e);
			throw new DAOException("插入信息出错！");
		}
		return result;
	}

	/**
	 * ******************************************** method name :
	 * batchModGoodsBank modified : lz , 2012-10-17
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.GoodsBankService#insertGoodsBankAudit(
	 *      org.springframework.web.multipart.MultipartFile)
	 *      *******************************************
	 */
	public String batchUpdateFromFile(MultipartFile file, GoodsBank goodsBank) throws Exception {
		String result = "0";
		goodsBank.trim();
		// 处理商品银行集合
		List<GoodsBank> gbAddList = new ArrayList<GoodsBank>(); // 新增集合
		List<GoodsBank> gbModList = new ArrayList<GoodsBank>(); // 修改集合
		List<String> oldDataList = new ArrayList<String>(); // 修改前的商品银行集合，与gbModList一一对应

		// 从文件中读取商品银行
		Map<String, Set<String>> goodsbanks = new HashMap<String, Set<String>>();
		// List<String> goodsbanks=new ArrayList<String>();
		try {
			goodsbanks = getGoodsbanks(file);
		} catch (Exception e) {
			result = "从文件中读取商品银行失败";
			log.info(result, e);
			return e.getMessage();
		}

		// 缓存中获取商户银行的信息
		HfCache cache = HfCacheUtil.getCache();
		Map<String, GoodsInfo> goodsMap = cache.getGoodsInfoMap2();
		Map<String, Object> bankMap = cache.getBankInfoMap();
		// //处理商户银行集合
		// List<GoodsBank> gbList=new ArrayList<GoodsBank>();
		for (Entry<String, Set<String>> entry : goodsbanks.entrySet()) {
			String key = entry.getKey();
			String[] gbArr = key.split("\\|");
			GoodsInfo goods = goodsMap.get(gbArr[0] + "-" + gbArr[1]);
			if (goods == null) {
				return "商户" + gbArr[0] + "下没有商品" + gbArr[1];
			}
			Set<String> value = entry.getValue();
			String[] bankIdArr = value.toArray(new String[0]);
			;
			for (int i = 0; i < bankIdArr.length; i++) {
				GoodsBank gb = new GoodsBank();
				gb.setMerId(gbArr[0]);
				gb.setGoodsId(gbArr[1]);
				gb.setBankId(bankIdArr[i]);
				BankInfo bankInfo = (BankInfo) bankMap.get(gb.getBankId());
				if (bankInfo == null) {
					log.error("不存在的银行号：" + gb.getBankId());
					return "不存在的银行号：" + gb.getBankId();
				}
				gb.setBankName(bankInfo.getBankName());
				if (goods.getPriceMode() == 0) { // 若为定价商品，则该商品银行继承该商品的价格
					gb.setAmount(goods.getAmount());
				} else {
					gb.setAmount("0");
				}
				gb.setVerifyTag(goodsBank.getVerifyTag());
				gb.setCheckDay(goodsBank.getCheckDay());
				gb.setkState(goodsBank.getkState());
				gb.setIsRealTime(goodsBank.getIsRealTime());
				gb.setBankMerId(goodsBank.getBankMerId());
				gb.setBankPosId(goodsBank.getBankPosId());
				gb.setDesc(goodsBank.getDesc());
				// 检查商品银行是否已存在或待审核
				Map<String, String> map = new HashMap<String, String>();
				map.put("merId", gb.getMerId());
				map.put("bankId", gb.getBankId());
				// 检查是否存在此商户银行,商户银行默认开通所有全网银行，故只需校验小额银行 去掉
				//验证全网和小额 add by lituo 2014-08-18
				if ("6".equals(bankInfo.getBankType()) || "3".equals(bankInfo.getBankType())) {
					MerBank merBank = merBankDao.get(map);
					if (merBank == null) {
						return "商户银行[" + gb.getMerId() + "-" + gb.getBankId() + "（" + gb.getBankName()
								+ "）]不存在，无法添加商品银行";
					}
				}
				// 验证合法性通过，下面判断是新增还是修改
				map.put("goodsId", gb.getGoodsId());
				String id = gb.getMerId() + "-" + gb.getGoodsId() + "-" + gb.getBankId();
				// 检查是否存在此商品银行
				// 1.判断商品银行表
				GoodsBank exist = load(map);
				if (exist != null) {
					if (exist.getModLock() == 0) { // 未锁定
						gbModList.add(gb); // 存在则为修改商品银行
						oldDataList.add(JsonHFUtil.getJsonArrStrFrom(exist));
						continue;
					} else {
						return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能修改";
					}
				}
				// 2.判断审核表
				map.put("ixData", id);
				map.put("tableName", "UMPAY.T_GOODS_BANK");
				int audit = auditDao.checkDataAdd(map);
				if (audit == 0) {
					if (exist == null) {
						gbAddList.add(gb); // 新增商品银行
					}
				} else {
					return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能再次添加";
				}
			}
		}

		// 开始入库
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// 生成批次id
		String batchId = TimeUtil.date8().substring(2, 8)
				+ SequenceUtil
						.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
		// 新增类型入库
		if (gbAddList != null) {
			for (GoodsBank gb : gbAddList) {
				result = putGoodsBankToAudit(gb, timestamp, timestamp, batchId, "1", null);
			}
		}
		// 修改类型入库
		if (gbModList != null) {
			for (int i = 0; i < gbModList.size(); i++) {
				result = putGoodsBankToAudit(gbModList.get(i), timestamp, timestamp, batchId, "2", oldDataList.get(i));
			}
		}

		/*
		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。批量操作也如此
		 * 需求开发人：万勇（工单号：PKS00005415）
		 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作。 开发时间： 2014-04-15
		 */
		auditService.goodsBankauditGoPass(batchId, LoginUtil.getUser());

		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String upBatchUpdateFromFile(MultipartFile file, GoodsBank goodsBank) throws Exception {
		String result = "0";
		goodsBank.trim();
		// 处理商品银行集合
		List<GoodsBank> gbAddList = new ArrayList<GoodsBank>(); // 新增集合
		List<GoodsBank> gbModList = new ArrayList<GoodsBank>(); // 修改集合
		List<String> oldDataList = new ArrayList<String>(); // 修改前的商品银行集合，与gbModList一一对应

		// 从文件中读取商品银行
		Map<String, Set<String>> goodsbanks = new HashMap<String, Set<String>>();
		// List<String> goodsbanks=new ArrayList<String>();
		try {
			goodsbanks = getGoodsbanks(file);
		} catch (Exception e) {
			result = "从文件中读取商品银行失败";
			log.info(result, e);
			return e.getMessage();
		}

		// 缓存中获取商户银行的信息
		HfCache cache = HfCacheUtil.getCache();
		Map<String, Object> bankMap = cache.getBankInfoMap();
		// //处理商户银行集合
		StringBuffer errMessage = new StringBuffer();
		for (Entry<String, Set<String>> entry : goodsbanks.entrySet()) {
			String key = entry.getKey();
			String[] gbArr = key.split("\\|");
			Map<String, String> checkResult = checkAmount(gbArr[0], gbArr[1]);
			if(!"success".equals(checkResult.get("flag"))){
				errMessage.append(checkResult.get("message")).append("</BR>");
				continue;
			}
			Set<String> value = entry.getValue();
			String[] bankIdArr = value.toArray(new String[0]);
			
			for (int i = 0; i < bankIdArr.length; i++) {
				GoodsBank gb = new GoodsBank();
				gb.setMerId(gbArr[0]);
				gb.setGoodsId(gbArr[1]);
				gb.setAmount(checkResult.get("message")); //继承商品金额
				gb.setBankId(bankIdArr[i]);
				BankInfo bankInfo = (BankInfo) bankMap.get(gb.getBankId());
				if (bankInfo == null) {
					log.error("不存在的银行号：" + gb.getBankId());
					errMessage.append("不存在的银行号：").append(gb.getBankId()).append("</BR>");
					continue;
				}
				gb.setBankName(bankInfo.getBankName());
				gb.setVerifyTag(goodsBank.getVerifyTag());
				gb.setCheckDay(goodsBank.getCheckDay());
				gb.setkState(goodsBank.getkState());
				gb.setIsRealTime(goodsBank.getIsRealTime());
				gb.setBankMerId(goodsBank.getBankMerId());
				gb.setBankPosId(goodsBank.getBankPosId());
				gb.setDesc(goodsBank.getDesc());
				// 检查商品银行是否已存在或待审核
				Map<String, String> map = new HashMap<String, String>();
				map.put("merId", gb.getMerId());
				map.put("bankId", gb.getBankId());
				MerBank merBank = merBankDao.get(map);
				if (merBank == null) {
					errMessage.append("商户银行[").append(gb.getMerId())
							.append("-").append(gb.getBankId()).append("(")
							.append(gb.getBankName()).append(
									")]不存在，无法添加商品银行</BR>");
					continue;
				}
				// 验证合法性通过，下面判断是新增还是修改
				map.put("goodsId", gb.getGoodsId());
				String id = gb.getMerId() + "-" + gb.getGoodsId() + "-" + gb.getBankId();
				// 检查是否存在此商品银行
				// 1.判断商品银行表
				GoodsBank exist = load(map);
				if (exist != null) {
					if (exist.getModLock() == 0) { // 未锁定
						gbModList.add(gb); // 存在则为修改商品银行
						oldDataList.add(JsonHFUtil.getJsonArrStrFrom(exist));
						continue;
					} else {
						errMessage.append("商品银行[").append(id).append("(").append(gb.getBankName()).append(")]正在审核，不能修改</BR>");
						continue;
					}
				}
				// 2.判断审核表
				map.put("ixData", id);
				map.put("tableName", "UMPAY.T_GOODS_BANK");
				int audit = auditDao.checkDataAdd(map);
				if (audit == 0) {
					if (exist == null) {
						gbAddList.add(gb); // 新增商品银行
					}
				} else {
					errMessage.append("商品银行[").append(id).append("(").append(gb.getBankName()).append(")]正在审核，不能再次添加</BR>");
					continue;
				}
			}
		}
		if(errMessage.length() > 0){
			return errMessage.toString();
		}

		// 开始入库
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// 生成批次id
		String batchId = TimeUtil.date8().substring(2, 8)
				+ SequenceUtil
						.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
		// 新增类型入库
		if (gbAddList != null) {
			for (GoodsBank gb : gbAddList) {
				result = putGoodsBankToAudit(gb, timestamp, timestamp, batchId, "1", null);
			}
		}
		// 修改类型入库
		if (gbModList != null) {
			for (int i = 0; i < gbModList.size(); i++) {
				result = putGoodsBankToAudit(gbModList.get(i), timestamp, timestamp, batchId, "2", oldDataList.get(i));
			}
		}

		/*
		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。批量操作也如此
		 * 需求开发人：万勇（工单号：PKS00005415）
		 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作。 开发时间： 2014-04-15
		 */
		auditService.goodsBankauditGoPass(batchId, LoginUtil.getUser());

		return result;
	}
	/**
	 * *****************  方法说明  *****************
	 * method name   :  upbatchUpdateFromFile
	 * @param		 :  @param file
	 * @param		 :  @param goodsBank
	 * @param		 :  @return
	 * @param		 :  @throws Exception
	 * @return		 :  String
	 * @author       :  lizhiqiang 2014年10月20日 下午2:36:10
	 * description   :  
	 * @see          :  
	 * **********************************************
	 */
//	public String upbatchUpdateFromFile(MultipartFile file, GoodsBank goodsBank) throws Exception {
//		String result = "0";// 操作结果 1表示成功 0 表示失败
//		goodsBank.trim();
//		// 处理商品银行集合
//		List<GoodsBank> gbAddList = new ArrayList<GoodsBank>(); // 新增集合
//		List<GoodsBank> gbModList = new ArrayList<GoodsBank>(); // 修改集合
//		List<String> oldDataList = new ArrayList<String>(); // 修改前的商品银行集合，与gbModList一一对应
//
//		// 从文件中读取商品银行
//		Map<String, Set<String>> goodsbanks = new HashMap<String, Set<String>>();
//		// List<String> goodsbanks=new ArrayList<String>();
//		try {
//			goodsbanks = getUpGoodsbanks(file);
//		} catch (Exception e) {
//			result = "从文件中读取商品银行失败";
//			log.info(result, e);
//			return e.getMessage();
//		}
//
//		// 缓存中获取商户银行的信息
//		HfCache cache = HfCacheUtil.getCache();
//		Map<String, GoodsInfo> goodsMap = cache.getGoodsInfoMap2();
//		Map<String, Object> bankMap = cache.getBankInfoMap();
//		// //处理商户银行集合
//		// List<GoodsBank> gbList=new ArrayList<GoodsBank>();
//		for (Entry<String, Set<String>> entry : goodsbanks.entrySet()) {
//			String key = entry.getKey();
//			String[] gbArr = key.split("\\|");//merid|goodsId|amount
//			GoodsInfo goods = goodsMap.get(gbArr[0] + "-" + gbArr[1]);
//			if (goods == null) {
//				return "商户" + gbArr[0] + "下没有商品" + gbArr[1];
//			}
//			Set<String> value = entry.getValue();
//			String[] bankIdArr = value.toArray(new String[0]);
//			String isEquSuccess = "success";
//			StringBuffer  errMsg = new StringBuffer("");
//			for (int i = 0; i < bankIdArr.length; i++) {
//				GoodsBank gb = new GoodsBank();
//				gb.setMerId(gbArr[0]);
//				gb.setGoodsId(gbArr[1]);
//				gb.setAmount(gbArr[2]);
//				 isEquSuccess = amountCompare(gb);
//				if (!isEquSuccess.equals("success")) {
//					errMsg.append(isEquSuccess + "</BR>");
//				}
//				gb.setBankId(bankIdArr[i]);
//				BankInfo bankInfo = (BankInfo) bankMap.get(gb.getBankId());
//				if (bankInfo == null) {
//					log.error("不存在的银行号：" + gb.getBankId());
//					return "不存在的银行号：" + gb.getBankId();
//				}
//				gb.setBankName(bankInfo.getBankName());
//				gb.setVerifyTag(goodsBank.getVerifyTag());
//				gb.setCheckDay(goodsBank.getCheckDay());
//				gb.setkState(goodsBank.getkState());
//				gb.setIsRealTime(goodsBank.getIsRealTime());
//				gb.setBankMerId(goodsBank.getBankMerId());
//				gb.setBankPosId(goodsBank.getBankPosId());
//				gb.setDesc(goodsBank.getDesc());
//				// 检查商品银行是否已存在或待审核
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("merId", gb.getMerId());
//				map.put("bankId", gb.getBankId());
//				// 验证合法性通过，下面判断是新增还是修改
//				map.put("goodsId", gb.getGoodsId());
//				String id = gb.getMerId() + "-" + gb.getGoodsId() + "-" + gb.getBankId();
//				// 检查是否存在此商品银行
//				// 1.判断商品银行表
//				GoodsBank exist = load(map);
//				if (exist != null) {
//					if (exist.getModLock() == 0) { // 未锁定
//						gbModList.add(gb); // 存在则为修改商品银行
//						oldDataList.add(JsonHFUtil.getJsonArrStrFrom(exist));
//						continue;
//					} else {
//						return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能修改";
//					}
//				}
//				// 2.判断审核表
//				map.put("ixData", id);
//				map.put("tableName", "UMPAY.T_GOODS_BANK");
//				int audit = auditDao.checkDataAdd(map);
//				if (audit == 0) {
//					if (exist == null) {
//						gbAddList.add(gb); // 新增商品银行
//					}
//				} else {
//					return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能再次添加";
//				}
//			}
//			if (!isEquSuccess.equals("success")) {
//				return errMsg.toString();
//			}
//		}
//
//		// 开始入库
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		// 生成批次id
//		String batchId = TimeUtil.date8().substring(2, 8)
//				+ SequenceUtil
//						.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
//		// 新增类型入库
//		if (gbAddList != null) {
//			for (GoodsBank gb : gbAddList) {
//				result = putGoodsBankToAudit(gb, timestamp, timestamp, batchId, "1", null);
//			}
//		}
//		// 修改类型入库
//		if (gbModList != null) {
//			for (int i = 0; i < gbModList.size(); i++) {
//				result = putGoodsBankToAudit(gbModList.get(i), timestamp, timestamp, batchId, "2", oldDataList.get(i));
//			}
//		}
//
//		/*
//		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。批量操作也如此
//		 * 需求开发人：万勇（工单号：PKS00005415）
//		 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作。 开发时间： 2014-04-15
//		 */
//		auditService.goodsBankauditGoPass(batchId, LoginUtil.getUser());
//
//		return result;
//	}
	/**
	 * ******************************************** method name : load modified
	 * : zhaojunbao , 2012-10-9
	 * 
	 * @see : @see com.umpay.hfmng.service.GoodsBankService#load(java.util.Map)
	 * ********************************************/
	public GoodsBank load(Map mapwhere) {
		log.info("查找商品银行信息：" + mapwhere.toString());
		return goodsBankDao.get(mapwhere);
	}

	/**
	 * ******************************************** method name :
	 * modifyGoodsBank modified : zhaojunbao , 2012-10-15
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.GoodsBankService#modifyGoodsBank(com.umpay
	 *      .hfmng.model.GoodsBank)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String modifyGoodsBank(GoodsBank goodsBank) throws Exception {
		String result = "0";
		int modLock = goodsBank.getModLock();
		if (modLock == 1) { // 判断当前的商品锁定状态 如果是已经锁定 则提示出错
			log.error("当前商品银行已处于锁定状态，无法操作");
			throw new DAOException("当前商品银行已处于锁定状态，无法操作");
		}
		goodsBank.setModLock(1); // 更新锁状态
		// 缓存中获取银行的信息，设置银行名称
		HfCache cache = HfCacheUtil.getCache();
		Map<String, Object> bankMap = cache.getBankInfoMap();
		BankInfo bankInfo = (BankInfo) bankMap.get(goodsBank.getBankId());
		if (bankInfo != null) {
			goodsBank.setBankName(bankInfo.getBankName());
		}
		String jsonString = JsonHFUtil.getJsonArrStrFrom(goodsBank);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_GOODS_BANK");
		audit.setModData(jsonString);
		audit.setState("0");
		audit.setAuditType("2"); // 审核状态为2 表示修改
		audit.setCreator(LoginUtil.getUser().getId());// 设置提交人
		audit.setResultDesc("");
		audit.setIxData(goodsBank.getMerId().trim() + "-" + goodsBank.getGoodsId().trim() + "-"
				+ goodsBank.getBankId().trim());
		audit.setDesc(goodsBank.getDesc());

		// ixData2 索引字段使用 商品名称 填充，提供商品银行操作日志时索引字段
		Map<String, Object> goodsMap = cache.getGoodsInfoMap();
		if (goodsBank.getMerId() != null && goodsBank.getGoodsId() != null) {
			GoodsInfo goods = (GoodsInfo) goodsMap.get(goodsBank.getMerId().trim() + "-"
					+ goodsBank.getGoodsId().trim());
			if (goods != null) {
				audit.setIxData2(goods.getGoodsName().trim()); // 商品名称
			}
		}
		try {
			auditDao.insert(audit); // 入审核表
			log.info("修改商品银行信息已入审核表，详细内容" + audit.toString());
			goodsBankDao.updateGoodsBankLock(goodsBank); // 更新锁状态
			if (goodsBankDao.updateGoodsBankLock(goodsBank) == 1) {
				result = "1";
				log.info("商品银行状态锁已修改");
			} else {
				log.error("商品银行状态锁修改失败");
				throw new DAOException("商品银行状态锁修改失败");
			}

			/*
			 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。
			 * 需求开发人：万勇（工单号：PKS00005415）
			 * 需求开发思路：尽量避免改动现有代码，修改商品银行时，系统自动模拟审核人员调用审核通过操作
			 */
			auditService.goodsBankauditPass(new String[] { audit.getId() }, LoginUtil.getUser());

		} catch (Exception e) {
			log.error("修改商品银行出错！");
			throw new DAOException("修改商品银行失败！");
		}
		return result;

	}

	/**
	 * ******************************************** method name : getGoodsbanks
	 * description : 从文件中获取每行的商品银行信息,返回集合为已去重后的数据
	 * 
	 * @return : Map<String,Set<String>>
	 * @param : @param file
	 * @param : @return
	 * @param : @throws Exception modified : xuhuafeng , 2013-11-25 下午05:37:20
	 *        *******************************************
	 */
	private Map<String, Set<String>> getGoodsbanks(MultipartFile file) throws Exception {
		InputStream input = null;
		BufferedReader bufferedReader = null;
		Map<String, Set<String>> goodsbanks = new HashMap<String, Set<String>>();
		try {
			input = file.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(input);
			bufferedReader = new BufferedReader(inputStreamReader);
		} catch (IOException e) {
			log.info("获取文件流出错", e);
			throw e;
		}
		if (bufferedReader == null)
			return goodsbanks;

		// 逐行读取
		String goodsbankStr = null;
		try {
			while ((goodsbankStr = bufferedReader.readLine()) != null) {
				String[] goodsBank = goodsbankStr.split("\\|");
				if (goodsBank.length != 3) {
					log.info("文件中存在无效的数据");
					throw new Exception("文件中存在无效的数据");
				}
				String key = StringUtils.trim(goodsBank[0]) + "|" + StringUtils.trim(goodsBank[1]);
				Set<String> dataSet = goodsbanks.get(key);
				if (dataSet == null) {
					dataSet = new HashSet<String>();
				}
				String[] bankIds = goodsBank[2].split(",");
				for (int i = 0; i < bankIds.length; i++) {
					dataSet.add(bankIds[i]);
				}
				goodsbanks.put(key, dataSet);
			}
		} catch (IOException e) {
			log.info("逐行读取文件出错", e);
			throw e;
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				log.info("关闭BufferedReader出错", e);
				throw e;
			}
		}
		return goodsbanks;
	}
	/**
	 * ******************************************** method name : getGoodsbanks
	 * description : 从文件中获取每行的商品银行信息,返回集合为已去重后的数据
	 * 
	 * @return : Map<String,Set<String>>
	 * @param : @param file
	 * @param : @return
	 * @param : @throws Exception modified : lizhiqiang , 2014年10月21日 10:29:26
	 *        *******************************************
	 */
	private Map<String, Set<String>> getUpGoodsbanks(MultipartFile file) throws Exception {
		InputStream input = null;
		BufferedReader bufferedReader = null;
		Map<String, Set<String>> goodsbanks = new HashMap<String, Set<String>>();
		Map<String, String> deDuplicate = new HashMap<String, String>();
		try {
			input = file.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(input);
			bufferedReader = new BufferedReader(inputStreamReader);
		} catch (IOException e) {
			log.info("获取文件流出错", e);
			throw e;
		}
		
		// 逐行读取
		String goodsbankStr = null;
		try {
			while ((goodsbankStr = bufferedReader.readLine()) != null) {
				String[] goodsBank = goodsbankStr.split("\\|");
				if (goodsBank.length != 4) {
					log.info("文件中存在无效的数据");
					throw new Exception("文件中存在无效的数据");
				}
			if (deDuplicate(deDuplicate, goodsBank).equals("continue")) {
				continue;
			}
				String realKey = StringUtils.trim(goodsBank[0]) + "|" + StringUtils.trim(goodsBank[1]) + "|" + StringUtils.trim(goodsBank[2]);

				Set<String> dataSet = goodsbanks.get(realKey);
				if (dataSet == null) {
					dataSet = new HashSet<String>();
				}
				String[] bankIds = goodsBank[3].split(",");
				for (int i = 0; i < bankIds.length; i++) {
					dataSet.add(StringUtils.trim(bankIds[i]));
				}
				goodsbanks.put(realKey, dataSet);
			}
		} catch (IOException e) {
			log.info("逐行读取文件出错", e);
			throw e;
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				log.info("关闭BufferedReader出错", e);
				throw e;
			}
		}
		return goodsbanks;
	}
	private String deDuplicate(Map<String, String> deDuplicate,String[] goodsBank) throws Exception  {
		String preKey = StringUtils.trim(goodsBank[0]) + "|" + StringUtils.trim(goodsBank[1]);
		String amount = deDuplicate.put(preKey, StringUtils.trim(goodsBank[2]));
		if (amount != null){
			if (amount.equals(deDuplicate.get(preKey))) {
				log.info("信息出现了重复，系统通过此次检查。重复的主要数据："+ preKey  + "|" +  deDuplicate.get(preKey) );
				return "continue";
			}else {
				//value(amount)
				log.error("信息出现了商户号与商品号重复，但是金额不等的异常，请检查文件。重复的主要数据："+ preKey  + "|" +  deDuplicate.get(preKey)+ "，" + preKey  + "|" +  amount );
				throw new IOException("信息出现了商户号与商品号重复，但是金额不等的异常，请检查文件。重复的主要数据："+ preKey  + "|" +  deDuplicate.get(preKey)+ "，" + preKey  + "|" +  amount );	
			}
		}
		return "success";
	}
	/**
	 * ******************************************** method name : closeGoodsBank
	 * modified : zhaojunbao , 2012-11-16
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.GoodsBankService#closeGoodsBank(com.umpay
	 *      .hfmng.model.GoodsBank)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String closeGoodsBank(GoodsBank goodsBank) throws Exception {
		String bankIds = StringUtils.trim(goodsBank.getBankId());
		String[] bankidArray = bankIds.split(",");
		String result = "1"; // 操作结果 1表示成功 0 表示失败
		try {
			for (int i = 0; i < bankidArray.length; i++) {
				GoodsBank gBank = goodsBank;
				gBank.setBankId(bankidArray[i]);
				String jsonString = JsonHFUtil.getJsonArrStrFrom(gBank);
				Audit audit = new Audit();
				audit.setTableName("UMPAY.T_GOODS_BANK");
				audit.setModData(jsonString);
				audit.setState("0");
				audit.setAuditType("2"); // 审核类型为2 修改（关闭操作）
				audit.setIxData(gBank.getMerId().trim() + "-" + gBank.getGoodsId().trim() + "-"
						+ gBank.getBankId().trim());
				auditDao.insert(audit);
				log.info("入审核表成功,审核类型：修改;" + audit.toString());
				goodsBank.setModLock(1); // 设置锁为锁定状态
				if (goodsBankDao.updateGoodsBankLock(goodsBank) == 1) {
					log.info("修改锁定状态成功，改为锁定状态;" + audit.toString());
				} else {
					log.error("修改锁定状态失败;" + audit.toString());
					throw new DAOException("修改锁定状态失败");
				}
			}
		} catch (Exception e) {
			log.error("商品银行配置（关闭）出错", e);
			throw new DAOException("商品银行配置（关闭）出错！");
		}
		return result;
	}

	/**
	 * ******************************************** method name : batchUpdate
	 * modified : xuhuafeng , 2013-11-11
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.GoodsBankService#batchUpdate(com.umpay
	 *      .hfmng.model.GoodsBank)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String batchUpdate(GoodsBank goodsBank) throws Exception {
		String result = "1";
		goodsBank.trim();
		// 处理商品银行集合
		List<GoodsBank> gbAddList = new ArrayList<GoodsBank>(); // 新增集合
		List<GoodsBank> gbModList = new ArrayList<GoodsBank>(); // 修改集合
		List<String> oldDataList = new ArrayList<String>(); // 修改前的商品银行集合，与gbModList一一对应
		String bankIds = StringUtils.trim(goodsBank.getBankId());
		String[] bankIdArr = bankIds.split(",");
		String goodsIds = StringUtils.trim(goodsBank.getGoodsId());
		// 去重
		Set<String> dataSet = string2Set(goodsIds);
		// 缓存中获取银行的信息
		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
		for (String str : dataSet) {
			String[] merIdGoodsId = str.split("\\|");
			if (merIdGoodsId.length != 2) {
				return "请修改行[" + str + "]为正确格式";
			}
			GoodsInfo goods = goodsMap.get(merIdGoodsId[0] + "-" + merIdGoodsId[1]);
			if (goods == null) {
				return "商户" + merIdGoodsId[0] + "下没有商品" + merIdGoodsId[1];
			}
			for (int i = 0; i < bankIdArr.length; i++) {
				if ("1".equals(StringUtils.trim(bankIdArr[i])) || "2".equals(StringUtils.trim(bankIdArr[i]))) {
					continue;
				}
				GoodsBank gb = new GoodsBank();
				gb.setMerId(merIdGoodsId[0]);
				gb.setGoodsId(merIdGoodsId[1]);
				gb.setBankId(bankIdArr[i]);
				if (goods.getPriceMode() == 0) { // 若为定价商品，则该商品银行继承该商品的价格
					gb.setAmount(goods.getAmount());
				} else {
					gb.setAmount("0");
				}
				gb.setVerifyTag(goodsBank.getVerifyTag());
				gb.setCheckDay(goodsBank.getCheckDay());
				gb.setkState(goodsBank.getkState());
				gb.setIsRealTime(goodsBank.getIsRealTime());
				gb.setBankMerId(goodsBank.getBankMerId());
				gb.setBankPosId(goodsBank.getBankPosId());
				gb.setDesc(goodsBank.getDesc());
				BankInfo bankInfo = (BankInfo) bankMap.get(gb.getBankId());
				if (bankInfo == null) {
					log.error("不存在的银行号：" + gb.getBankId());
					return "操作失败，请稍后再试";
				}
				gb.setBankName(bankInfo.getBankName());
				// 检查商品银行是否已存在或待审核
				Map<String, String> map = new HashMap<String, String>();
				map.put("merId", gb.getMerId());
				map.put("bankId", gb.getBankId());
				// 检查是否存在此商户银行,商户银行默认开通所有全网银行，故只需校验小额银行 去掉
				//验证全网和小额 add by lituo 2014-08-18
				if ("6".equals(bankInfo.getBankType()) || "3".equals(bankInfo.getBankType())) {
					MerBank merBank = merBankDao.get(map);
					if (merBank == null) {
						return "商户银行[" + gb.getMerId() + "-" + gb.getBankId() + "（" + gb.getBankName()
								+ "）]不存在，无法添加商品银行";
					}
				}
				// 验证合法性通过，下面判断是新增还是修改
				map.put("goodsId", gb.getGoodsId());
				String id = gb.getMerId() + "-" + gb.getGoodsId() + "-" + gb.getBankId();
				// 检查是否存在此商品银行
				// 1.判断商品银行表
				GoodsBank exist = load(map);
				if (exist != null) {
					if (exist.getModLock() == 0) { // 未锁定
						gbModList.add(gb); // 存在则为修改商品银行
						oldDataList.add(JsonHFUtil.getJsonArrStrFrom(exist));
						continue;
					} else {
						return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能修改";
					}
				}
				// 2.判断审核表
				map.put("ixData", id);
				map.put("tableName", "UMPAY.T_GOODS_BANK");
				int audit = auditDao.checkDataAdd(map);
				if (audit == 0) {
					if (exist == null) {
						gbAddList.add(gb); // 新增商品银行
					}
				} else {
					return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能再次添加";
				}
			}
		}

		// 开始入库
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// 生成批次id
		String batchId = TimeUtil.date8().substring(2, 8)
				+ SequenceUtil
						.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
		// 新增类型入库
		if (gbAddList != null) {
			for (GoodsBank gb : gbAddList) {
				result = putGoodsBankToAudit(gb, timestamp, timestamp, batchId, "1", null);
			}
		}
		// 修改类型入库
		if (gbModList != null) {
			for (int i = 0; i < gbModList.size(); i++) {
				result = putGoodsBankToAudit(gbModList.get(i), timestamp, timestamp, batchId, "2", oldDataList.get(i));
			}
		}

		/*
		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。批量操作也如此
		 * 需求开发人：万勇（工单号：PKS00005415）
		 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作。 开发时间： 2014-04-15
		 */
		auditService.goodsBankauditGoPass(batchId, LoginUtil.getUser());

		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String upBatchUpdate(GoodsBank goodsBank) throws Exception {
		String result = "1";
		goodsBank.trim();
		// 处理商品银行集合
		List<GoodsBank> gbAddList = new ArrayList<GoodsBank>(); // 新增集合
		List<GoodsBank> gbModList = new ArrayList<GoodsBank>(); // 修改集合
		List<String> oldDataList = new ArrayList<String>(); // 修改前的商品银行集合，与gbModList一一对应
		String bankIds = StringUtils.trim(goodsBank.getBankId());
		String[] bankIdArr = bankIds.split(",");
		String goodsIds = StringUtils.trim(goodsBank.getGoodsId());
		// 去重
		Set<String> dataSet = string2Set(goodsIds);
		// 缓存中获取银行的信息
		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
		Map<String, String> bankTypeMap = optionService.getBankTypeMap("upbankType");
		StringBuffer errMessage = new StringBuffer();
		for (String str : dataSet) {
			String[] merIdGoodsId = str.split("\\|");
			if (merIdGoodsId.length != 2) {
				errMessage.append("请修改行[").append(str).append("]为正确格式</BR>");
				continue;
			}
			Map<String, String> checkResult = checkAmount(merIdGoodsId[0], merIdGoodsId[1]);
			if(!"success".equals(checkResult.get("flag"))){
				errMessage.append(checkResult.get("message")).append("</BR>");
				continue;
			}
			for (int i = 0; i < bankIdArr.length; i++) {
				if(bankTypeMap.containsKey(StringUtils.trim(bankIdArr[i]))){
					continue;
				}
				GoodsBank gb = new GoodsBank();
				gb.setMerId(merIdGoodsId[0]);
				gb.setGoodsId(merIdGoodsId[1]);
				gb.setAmount(checkResult.get("message")); //继承商品金额
				gb.setBankId(bankIdArr[i]);
				gb.setVerifyTag(goodsBank.getVerifyTag());
				gb.setCheckDay(goodsBank.getCheckDay());
				gb.setkState(goodsBank.getkState());
				gb.setIsRealTime(goodsBank.getIsRealTime());
				gb.setBankMerId(goodsBank.getBankMerId());
				gb.setBankPosId(goodsBank.getBankPosId());
				gb.setDesc(goodsBank.getDesc());
				BankInfo bankInfo = (BankInfo) bankMap.get(gb.getBankId());
				if (bankInfo == null) {
					log.error("不存在的银行号：" + gb.getBankId());
					return "操作失败，请稍后再试";
				}
				gb.setBankName(bankInfo.getBankName());
				// 检查商品银行是否已存在或待审核
				Map<String, String> map = new HashMap<String, String>();
				map.put("merId", gb.getMerId());
				map.put("bankId", gb.getBankId());
				MerBank merBank = merBankDao.get(map);
				if (merBank == null) {
					errMessage.append("商户银行[").append(gb.getMerId())
							.append("-").append(gb.getBankId()).append("(")
							.append(gb.getBankName()).append(
									")]不存在，无法添加商品银行</BR>");
					continue;
				}
				// 验证合法性通过，下面判断是新增还是修改
				map.put("goodsId", gb.getGoodsId());
				String id = gb.getMerId() + "-" + gb.getGoodsId() + "-" + gb.getBankId();
				// 检查是否存在此商品银行
				// 1.判断商品银行表
				GoodsBank exist = load(map);
				if (exist != null) {
					if (exist.getModLock() == 0) { // 未锁定
						gbModList.add(gb); // 存在则为修改商品银行
						oldDataList.add(JsonHFUtil.getJsonArrStrFrom(exist));
						continue;
					} else {
						errMessage.append("商品银行[").append(id).append("(").append(gb.getBankName()).append(")]正在审核，不能修改</BR>");
						continue;
					}
				}
				// 2.判断审核表
				map.put("ixData", id);
				map.put("tableName", "UMPAY.T_GOODS_BANK");
				int audit = auditDao.checkDataAdd(map);
				if (audit == 0) {
					if (exist == null) {
						gbAddList.add(gb); // 新增商品银行
					}
				} else {
					errMessage.append("商品银行[").append(id).append("(").append(gb.getBankName()).append(")]正在审核，不能再次添加</BR>");
					continue;
				}
			}
		}
		
		if(errMessage.length() > 0){
			return errMessage.toString();
		}

		// 开始入库
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// 生成批次id
		String batchId = TimeUtil.date8().substring(2, 8)
				+ SequenceUtil
						.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
		// 新增类型入库
		if (gbAddList != null) {
			for (GoodsBank gb : gbAddList) {
				result = putGoodsBankToAudit(gb, timestamp, timestamp, batchId, "1", null);
			}
		}
		// 修改类型入库
		if (gbModList != null) {
			for (int i = 0; i < gbModList.size(); i++) {
				result = putGoodsBankToAudit(gbModList.get(i), timestamp, timestamp, batchId, "2", oldDataList.get(i));
			}
		}

		/*
		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。批量操作也如此
		 * 需求开发人：万勇（工单号：PKS00005415）
		 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作。 开发时间： 2014-04-15
		 */
		auditService.goodsBankauditGoPass(batchId, LoginUtil.getUser());

		return result;
	}
	
//	/**
//	 * ******************************************** 
//	 * method : 	upBatchUpdate
//	 * modified : 	lizhiqiang , 2014年10月16日 14:43:59
//	 * describe:	综合支付批量修改方法
//	 * @see : 
//	 *      com.umpay.hfmng.service.GoodsBankService#upBatchUpdate(com.umpay
//	 *      .hfmng.model.GoodsBank)
//	 * ********************************************/
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	public String upBatchUpdate(GoodsBank goodsBank) throws Exception {
//		String result = "1";
//		goodsBank.trim();
//		Map<String, String> deDuplicate = new HashMap<String, String>();
//
//		// 处理商品银行集合
//		List<GoodsBank> gbAddList = new ArrayList<GoodsBank>(); // 新增集合
//		List<GoodsBank> gbModList = new ArrayList<GoodsBank>(); // 修改集合
//		List<String> oldDataList = new ArrayList<String>(); // 修改前的商品银行集合，与gbModList一一对应
//		String bankIds = StringUtils.trim(goodsBank.getBankId());
//		String[] bankIdArr = bankIds.split(",");
//		String goodsIds = StringUtils.trim(goodsBank.getGoodsId());
//		// 去重
//		Set<String> dataSet = string2Set(goodsIds);
//		// 缓存中获取银行的信息
//		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
//		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
//		for (String str : dataSet) {
//			String[] merIdGoodsId = str.split("\\|");
//			if (merIdGoodsId.length != 3) {
//				return "请修改行[" + str + "]为正确格式";
//			}
//			GoodsInfo goods = goodsMap.get(merIdGoodsId[0] + "-" + merIdGoodsId[1]);
//			if (goods == null) {
//				return "商户" + merIdGoodsId[0] + "下没有商品" + merIdGoodsId[1];
//			}
//			String amount = goods.getAmount();
//			//1:XE; 2:BS 3:GM
//			// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
//			Map<String, String> upBankTypeMap = optionService.getBankTypeMap("upbankType");
//			String isEquSuccess = "success";
//			StringBuffer  errMsg = new StringBuffer("");
//			for (int i = 0; i < bankIdArr.length; i++) {
//				if (upBankTypeMap.containsKey(StringUtils.trim(bankIdArr[i]))) {
//					continue;
//				}
//				GoodsBank gb = new GoodsBank();
//				gb.setMerId(merIdGoodsId[0]);
//				gb.setGoodsId(merIdGoodsId[1]);
//				gb.setAmount(merIdGoodsId[2]);
//				gb.setBankId(bankIdArr[i]);
//				if (deDuplicate(deDuplicate, merIdGoodsId).equals("confinue")) {
//					continue;
//				}
//					// here 3 types amount appeared(De-duplicate 1):
//					//1: goods.amount; 2: from page(merIdGoodsId[2]); 3: upservice.amount
//					//gb.setAmount(goods.getAmount());
//				 isEquSuccess = amountCompare(gb);
//					if (!isEquSuccess.equals("success")) {
//						errMsg.append(isEquSuccess + "</BR>");
//					}
//					//merIdGoodsId[2]
//				
//				gb.setVerifyTag(goodsBank.getVerifyTag());
//				gb.setCheckDay(goodsBank.getCheckDay());
//				gb.setkState(goodsBank.getkState());
//				gb.setIsRealTime(goodsBank.getIsRealTime());
//				gb.setBankMerId(goodsBank.getBankMerId());
//				gb.setBankPosId(goodsBank.getBankPosId());
//				gb.setDesc(goodsBank.getDesc());
//				BankInfo bankInfo = (BankInfo) bankMap.get(gb.getBankId());
//				if (bankInfo == null) {
//					log.error("不存在的银行号：" + gb.getBankId());
//					return "操作失败，请稍后再试";
//				}
//				gb.setBankName(bankInfo.getBankName());
//				// 检查商品银行是否已存在或待审核
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("merId", gb.getMerId());
//				map.put("bankId", gb.getBankId());
//				// 验证合法性通过，下面判断是新增还是修改
//				map.put("goodsId", gb.getGoodsId());
//				String id = gb.getMerId() + "-" + gb.getGoodsId() + "-" + gb.getBankId();
//				// 检查是否存在此商品银行
//				// 1.判断商品银行表
//				GoodsBank exist = load(map);
//				if (exist != null) {
//					if (exist.getModLock() == 0) { // 未锁定
//						gbModList.add(gb); // 存在则为修改商品银行
//						oldDataList.add(JsonHFUtil.getJsonArrStrFrom(exist));
//						continue;
//					} else {
//						return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能修改";
//					}
//				}
//				// 2.判断审核表
//				map.put("ixData", id);
//				map.put("tableName", "UMPAY.T_GOODS_BANK");
//				int audit = auditDao.checkDataAdd(map);
//				if (audit == 0) {
//					if (exist == null) {
//						gbAddList.add(gb); // 新增商品银行
//					}
//				} else {
//					return "商品银行[" + id + "（" + gb.getBankName() + "）]正在审核，不能再次添加";
//				}
//			}
//			if (!isEquSuccess.equals("success")) {
//				return errMsg.toString();
//			}
//		}
//
//		// 开始入库
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		// 生成批次id
//		String batchId = TimeUtil.date8().substring(2, 8)
//				+ SequenceUtil
//						.formatSequence(SequenceUtil.getInstance().getSequence4File(Const.SEQ_FILENAME_AUDIT), 10);
//		// 新增类型入库
//		if (gbAddList != null) {
//			for (GoodsBank gb : gbAddList) {
//				result = putGoodsBankToAudit(gb, timestamp, timestamp, batchId, "1", null);
//			}
//		}
//		// 修改类型入库
//		if (gbModList != null) {
//			for (int i = 0; i < gbModList.size(); i++) {
//				result = putGoodsBankToAudit(gbModList.get(i), timestamp, timestamp, batchId, "2", oldDataList.get(i));
//			}
//		}
//
//		/*
//		 * 2014-04-10产品提出新需求：新增或编辑商户银行、商品银行时屏蔽审核，操作成功即刻生效。批量操作也如此
//		 * 需求开发人：万勇（工单号：PKS00005415）
//		 * 需求开发思路：尽量避免改动现有代码，新增商品银行时，系统自动模拟审核人员调用审核通过操作。 开发时间： 2014-04-15
//		 */
//		auditService.goodsBankauditGoPass(batchId, LoginUtil.getUser());
//
//		return result;
//	}

	public String amountCompare(GoodsBank gb) {
		String merId = StringUtils.trim(gb.getMerId());
		String goodsId = StringUtils.trim(gb.getGoodsId());
		String amount = StringUtils.trim(gb.getAmount());
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache()
				.getGoodsInfoMap2();
		String goodsMapKey = merId + "-" + goodsId;
		GoodsInfo goods = goodsMap.get(goodsMapKey);
		String dbAmount = goods.getAmount();
		String priceMod = String.valueOf(goods.getPriceMode());//1 cannot pass
		if (!priceMod.equals("0")) {
			log.info("综合支付只支持定价商品 ！此商品：" + goodsMapKey + "非定价！");
			return "综合支付只支持定价商品 ！此商品：" + goodsMapKey + "非定价！";
		}
		if (amount.equals(dbAmount)) {
			// success
			log.info("成功，数据库存在此金额，新加数据金额匹配成功！");
			return "success";
		} else {
			// this step indispensable
			log.info("新加数据金额: " + amount + "与商品" + goodsMapKey + "金额： " + dbAmount + "匹配不成功！");
			return "新加数据金额: " + amount + "与商品" + goodsMapKey + "金额： " + dbAmount + "匹配不成功！";
		}
	}
//	public String amountCompare(GoodsBank gb) {
//		String merId = StringUtils.trim(gb.getMerId());
//		String goodsId = StringUtils.trim(gb.getGoodsId());
//		String amount = StringUtils.trim(gb.getAmount());
//		//the goodsbank.amount depends on itself first,so first 1 then 2、3 
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("merId", merId);
//		map.put("goodsId", goodsId);
//		map.put("excludeMW", "MW");
//		map.put("modLock", "0");
//		List<GoodsBank> goodsBankAmountDB = goodsBankDao.findGoodsBanks(map);
//		//1. if db amount != null. then compare with db amount
//		if (goodsBankAmountDB != null && goodsBankAmountDB.size()>0) {
//			String dbAmount = goodsBankAmountDB.get(0).getAmount();
//			if (amount.equals(dbAmount)) {
//				//success
//				log.info("成功，数据库存在此金额，新加数据金额匹配成功！");
//				return "success";
//			}else {
//				//this step indispensable
//				log.info( "新加数据金额: " + amount + "与已有金额： " + dbAmount + "匹配不成功！");
//				return "新加数据金额: " + amount + "与已有金额： " + dbAmount + "匹配不成功！";
//			}
//		}
//		//2. if db amount == null && UPService amount already exits. then compare with UPService amount
//		else {
//			List<UPService> upServicesList = uPServiceServiceImpl.findBy(merId, goodsId);
//			for (Iterator<UPService> iterator = upServicesList.iterator(); iterator
//					.hasNext();) {
//				UPService upService = (UPService) iterator.next();
//				String upServiceAmount = String.valueOf(upService.getAmount());
//				if (upServiceAmount != null) {
//					if (upServiceAmount.equals(amount)) {
//						//"success 1":use the upServiceAmount
//						log.info("success 1 : use the upServiceAmount");
//						return "success";//break
//					}else {
//						//fail
//						log.error("页面配置金额" + amount + "与计费代码金额" + upServiceAmount+ "不等！");
//						return "页面配置金额" + amount + "与计费代码金额" + upServiceAmount+ "不等！";
//					}
//				}else {
//					//3.if 1 and 2 all don't exit. then use the page.amount and return success
//					//"success 2":use the page Amount
//					gb.setAmount(amount);
//					log.info("success 2 : use the page Amount");
//				}
//				
//			}
//		}
//		return "success";
//	}

	/**
	 * ******************************************** method name :
	 * putGoodsBankToAudit description : 批量配置入审核表
	 * 
	 * @return : String
	 * @param : @param goodsBank 更新后的商品银行
	 * @param : @param inTime 提交时间
	 * @param : @param modTime 修改时间
	 * @param : @param batchId 批量号
	 * @param : @param auditType 审核类型，1新增，2修改
	 * @param : @param oldData 原商品银行，auditType=1时，次参数无效；auditType=2时，为修改前的数据
	 * @param : @return modified : xuhuafeng , 2013-11-13 下午01:55:24
	 *        *******************************************
	 */
	private String putGoodsBankToAudit(GoodsBank goodsBank, Timestamp inTime, Timestamp modTime, String batchId,
			String auditType, String oldData) {
		String result = "1";
		goodsBank.setModLock(1); // 更新锁状态
		String jsonString = JsonHFUtil.getJsonArrStrFrom(goodsBank);

		if ("2".equals(auditType)) {
			int rs = goodsBankDao.updateGoodsBankLock(goodsBank); // 更新锁状态
			if (rs == 1) {
				log.info("商品银行状态锁修改成功");
			} else {
				log.info("商品银行状态锁修改失败");
				throw new DAOException("商品银行状态锁修改失败");
			}
			jsonString = "[" + jsonString + "," + oldData + "]";
		} else {
			jsonString = "[" + jsonString + "]";
		}
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_GOODS_BANK");
		audit.setModData(jsonString);
		audit.setState("0");
		audit.setAuditType(auditType); // 审核类型.1为新增，2为修改
		audit.setInTime(inTime);
		audit.setModTime(modTime);
		audit.setBatchId(batchId);// 设置批次号
		audit.setIxData(goodsBank.getMerId().trim() + "-" + goodsBank.getGoodsId().trim() + "-"
				+ goodsBank.getBankId().trim());
		audit.setCreator(LoginUtil.getUser().getId());// 设置提交人
		audit.setDesc(goodsBank.getDesc());

		// ixData2 索引字段使用 商品名称 填充，提供商品银行操作日志时索引字段
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		if (goodsBank.getMerId() != null && goodsBank.getGoodsId() != null) {
			GoodsInfo goods = (GoodsInfo) goodsMap.get(goodsBank.getMerId().trim() + "-"
					+ goodsBank.getGoodsId().trim());
			if (goods != null) {
				audit.setIxData2(goods.getGoodsName().trim()); // 商品名称
			}
		}

		auditDao.insert(audit);
		log.info("商品银行信息已入审核表，详细内容" + audit.toString());

		return result;
	}

	/**
	 * ***************** 方法说明 ***************** method name : string2Set
	 * 
	 * @param : @param str
	 * @param : @return
	 * @return : Set<String>
	 * @author : LiZhen 2013-11-26 下午4:49:57 description :
	 *         处理来自页面的字符串，按换行符分割后放入set中
	 * @see : **********************************************
	 */
	private Set<String> string2Set(String str) {
		Set<String> set = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == ' ') {
				continue;
			} else if (c != '\r' && c != '\n') {
				sb.append(c);
			} else {
				if (sb.length() != 0)
					set.add(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		if (sb.length() != 0)
			set.add(sb.toString());
		return set;
	}
	
	public Map<String, String> checkAmount(String merId, String goodsId){
		Map<String, String> msg = new HashMap<String, String>();
		msg.put("flag", "fail"); //初始化为失败
		merId = StringUtils.trim(merId);
		goodsId = StringUtils.trim(goodsId);
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
		String goodsMapKey = merId + "-" + goodsId;
		GoodsInfo goods = goodsMap.get(goodsMapKey);
		if (goods == null) {
			msg.put("message", "商户" + merId + "下没有商品" + goodsId);
			return msg;
		}
		String priceMod = String.valueOf(goods.getPriceMode());//1 cannot pass
		if (!priceMod.equals("0")) {
			log.info("综合支付只支持定价商品 ！此商品：" + goodsMapKey + "非定价！");
			msg.put("message", "综合支付只支持定价商品 ！此商品：" + goodsMapKey + "非定价！");
			return msg;
		}
		String dbAmount = goods.getAmount();
		// success
		msg.put("flag", "success");
		msg.put("message", dbAmount);
		log.info("成功，数据库存在此金额，新加数据金额匹配成功！");
		return msg;
	}
}
