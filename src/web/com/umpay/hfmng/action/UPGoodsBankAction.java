/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsBankAction.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-26
 * *************************************************/

package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.GoodsBankOptLogExcel;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.BankService;
import com.umpay.hfmng.service.GoodsBankService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

/** ******************  类说明  ******************
 * class       :  UPGoodsBankAction
 * date        :  2014年10月10日 
 * @author     :  lizhiqiang
 * @version    :  V1.0  
 * description :  
 * @see        :                         
 * ***********************************************/
@Controller
@RequestMapping("/upgoodsbank")
public class UPGoodsBankAction extends BaseAction {
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private GoodsBankService goodsBankService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private BankService bankService;
	@Autowired
	private MerBankDao merBankDaoImpl;


	private double getFileMB(long byteFile) {
		if (byteFile == 0)
			return 0;
		double mb = 1024 * 1024;
		return byteFile / mb;
	}

	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		HfCache hfCache = HfCacheUtil.getCache(); // zhao 获取cache
		String opts = hfCache.getUrlAcl("upgoodsbank");
		modeMap.addAttribute("opts", opts);
		return "upgoodsbank/index";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap(); // 缓存中获取商户银行的信息
				Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
				Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					// 渲染数据
					Map goodsBank = data.get(i);
					String merId = (String) goodsBank.get("MERID");
					MerInfo mer = (MerInfo) merMap.get(merId);
					if (mer != null) {
						goodsBank.put("MERNAME", mer.getMerName());
					}
					String goodsId = (String) goodsBank.get("GOODSID");
					GoodsInfo goods = (GoodsInfo) goodsMap.get(merId + "-" + goodsId);
					if (goods != null) {
						goodsBank.put("GOODSNAME", goods.getGoodsName());
					}
					String bankName = "";
					String bankId = (String) goodsBank.get("BANKID");
					BankInfo bankInfo = (BankInfo) bankMap.get(bankId);
					if (bankInfo != null) {
						bankName = bankInfo.getBankName();
					}
					goodsBank.put("BANKNAME", bankName);
					data.set(i, goodsBank);
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * ******************************************** 
	 * method name : add
	 * description : 添加页面跳转 用户点击商户号时的页面跳转
	 * 
	 * @return : String
	 * @param : @param type
	 * @param : @param modeMap
	 * @param : @return modified : zhaojunbao , 2012-10-8 下午05:57:57
	 *        *******************************************
	 */
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap) {
		String zTreeNodes = getUPBankTree2();
		modeMap.addAttribute("zNodes", zTreeNodes);
		return "upgoodsbank/addgoodsbank";
	}

	/**
	 * ******************************************** 
	 * method name : getGoodsIds
	 * description : 异步获取商户号下的商品号码 同时获取当前商户号所对应的支付银行
	 * 
	 * @return : String
	 * @param : @param type
	 * @param : @param modeMap
	 * @param : @return modified : zhaojunbao , 2012-10-9 上午11:55:09
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getgoodsids")
	public ModelAndView getGoodsIds(String merId, ModelMap modeMap) {
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		JSONArray result = new JSONArray();
		Iterator it = goodsMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			GoodsInfo goodsInfo = (GoodsInfo) entry.getValue();
			if (merId.equals(goodsInfo.getMerId())) {
				result.add(OptionAction.buildSelect(goodsInfo.getGoodsId(), goodsInfo.getGoodsId()));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());

	}

	/**
	 * ******************************************** 
	 * method name : save
	 * description : 保存方法 需要实现的是根据前台传过来的值，对已已经审核中的银行配置 不做处理，
	 * 对于要关闭的银行要做添加审核以及更新锁操作，对于新增的配置银行，要入审核表
	 * 
	 * @return : ModelAndView
	 * @param : @param goodsBank
	 * @param : @return modified : zhaojunbao , 2012-10-9 下午07:10:53
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(GoodsBank goodsBank) {
		String insertRes = "0";// 插入操作结果 默认为失败
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService.getBankTypeMap("upbankType");
		Map<String, String> json = new HashMap<String, String>();
		Map<String, String> mapWhere = new HashMap<String, String>();
		String merId = StringUtils.trim(goodsBank.getMerId());
		String goodsId = StringUtils.trim(goodsBank.getGoodsId());
		
		GoodsInfo goodsInfo = HfCacheUtil.getCache().getGoodsInfoMap2()
			.get(goodsBank.getMerId().trim() + "-" + goodsBank.getGoodsId().trim());
		String amount = goodsInfo.getAmount();
		if (goodsInfo.getPriceMode() != 0) { // 如果是定价商品
			json.put("msg", "只有定价商品才能开通商品银行，请尽快修改");
			return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(json));										// 则做判断
		}
		if (!amount.equals(goodsBank.getAmount())) { // 页面配置金额跟商品实际金额不一致
			json.put("msg", "页面配置金额跟商品实际金额不一致");
			return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(json));
		}
		
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		// 根据商户号和商品号从商品银行中获取的已经配置银行号
		mapWhere.put("modLock", "0"); // 查询条件为没有带锁的记录
		List<GoodsBank> goodsBankList = bankService.getBankInfosByGoodsId(mapWhere);
		String bankIds = StringUtils.trim(goodsBank.getBankId());
		String[] bankidArray = bankIds.split(",");
		List<String> arr = new ArrayList<String>();
		// List<String> updateBankIds=new ArrayList<String>();
		// //记录需要更新的银行列表，做银行的关闭操作
		for (int j = 0; j < bankidArray.length; j++) {
			if (upBankTypeMap.containsKey(StringUtils.trim(bankidArray[j]))) {
				continue;
			}
			arr.add(StringUtils.trim(bankidArray[j]));
		}
		
		// 处理审核表中数据 begin
		Map<String, String> AuditWhere = new HashMap<String, String>();
		AuditWhere.put("tableName", "UMPAY.T_GOODS_BANK");
		AuditWhere.put("ixData", merId + "-" + goodsId);
		List<Audit> AuditConf = auditService.getCheckMerBankList(AuditWhere); // 查询审核表中的待审核信息，条件为
																				// tablename="UMPAY.T_GOODS_BANK"
		if (AuditConf != null && AuditConf.size() != 0) {
			List<String> AuditConfStrings = new ArrayList<String>(); // 字符串类型的list
			// 存放t_hfaudit表中待审核的
			// 商品银行（merId-goodsId-bankId）
			for (Audit audit : AuditConf) {
				if (audit.getIxData().split("-").length > 2) {
					AuditConfStrings.add(audit.getIxData().split("-")[2]); // 将待审核的银行号放入数组中
				}
			}
			if (AuditConfStrings.size() > 0) { // 审核表中存在待审核数据
				for (int j = 0; j < AuditConfStrings.size(); j++) {
					if (arr.contains(AuditConfStrings.get(j))) { // 当前要配置的银行在待审核表中，则去掉
						arr.remove(AuditConfStrings.get(j));
					}
				}
			}
		}
		// end
		// 处理已经存在配置的信息
		if (goodsBankList != null && goodsBankList.size() != 0) {
			for (int i = 0; i < goodsBankList.size(); i++) {
				// 如果里面包括 已经配置的 则删除
				if (arr.contains((StringUtils.trim(((GoodsBank) goodsBankList.get(i)).getBankId())))) // 列表中包括已经配置的银行
																								// 删除
				{
					arr.remove((StringUtils.trim(((GoodsBank) goodsBankList.get(i)).getBankId())));// 将增加列表中的银行标号删除
				}
				// else //列表中不包括已经配置的银行，表示执行关闭操作
				// {
				// updateBankIds.add(((GoodsBank)list.get(i)).getBankId());
				// //将需要关闭的银行编号放入更新银行中
				// }
			}
		}
		String msg = "";
		String res = "0";
		if (arr.size() > 0) {
			goodsBank.setBankId(arr.toString().replace("[", "").replace("]", "")); // 设置配置银行的值
																					// 为数组在service中做转化
			try {
				insertRes = goodsBankService.insertGoodsBankAudit(goodsBank);
				res = "1";
			} catch (Exception e) {
				log.error("新增商品银行" + arr.toString() + "失败", e);
			} // 1表示全部成功 ; 0 表示全部失败
			msg = "新增商品银行";
			if ("1".equals(insertRes)) {

				msg += "成功";
			} else {
				msg += "失败";
			}
		}else {
			msg += "不需要增加~";
		}
		json.put("res", res);
		json.put("msg", msg);
		return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(json));
	}

	@RequestMapping(value = "/detail")
	public String showDetail(String merId, String goodsId, String bankId, ModelMap modeMap) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		mapWhere.put("bankId", bankId);
		GoodsBank goodsbank = goodsBankService.load(mapWhere);
		goodsbank.trim();
		String bankName = HfCacheUtil.getCache().getBankName(goodsbank.getBankId());
		goodsbank.setBankId(bankName + "(" + goodsbank.getBankId() + ")");
		modeMap.addAttribute("goodsbank", goodsbank);
		return "upgoodsbank/detail";
	}

	/**
	 * ******************************************** 
	 * method name : load
	 * description : 修改跳转页面
	 * 
	 * @return : String
	 * @param : @param merId
	 * @param : @param goodsId
	 * @param : @param bankId
	 * @param : @param modeMap
	 * @param : @return modified : zhaojunbao , 2012-11-12 下午08:16:48
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String load(String merId, String goodsId, String bankId, ModelMap modeMap) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		mapWhere.put("bankId", bankId);
		GoodsBank goodsbank = goodsBankService.load(mapWhere);
		goodsbank.trim();
		modeMap.addAttribute("goodsbank", goodsbank);
		return "upgoodsbank/modify";
	}

	/**
	 * ******************************************** 
	 * method name : update
	 * description : 修改后的保存方法
	 * 
	 * @return : ModelAndView
	 * @param : @param goodsBank
	 * @param : @param modeMap
	 * @param : @return modified : zhaojunbao , 2012-11-12 下午08:16:25
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(GoodsBank goodsBank, String isRealTime1) {
		String string = "0";
		goodsBank.setIsRealTime(isRealTime1);
		JSONObject json = new JSONObject();
		GoodsInfo goodsInfo = HfCacheUtil.getCache().getGoodsInfoMap2()
			.get(goodsBank.getMerId().trim() + "-" + goodsBank.getGoodsId().trim());
		try {
			json.put("res", "0");
			String amount = goodsInfo.getAmount();
			if (goodsInfo.getPriceMode() != 0) { // 如果是定价商品
				json.put("msg", "只有定价商品才能开通商品银行，请尽快修改");
				return new ModelAndView("jsonView", "ajax_json", json.toString());										// 则做判断
			}
			if (!amount.equals(goodsBank.getAmount())) { // 页面配置金额跟商品实际金额不一致
				json.put("msg", "页面配置金额跟商品实际金额不一致");
				return new ModelAndView("jsonView", "ajax_json", json.toString());
			}
			json.put("msg", "修改失败,请稍后再试");
			string = goodsBankService.modifyGoodsBank(goodsBank);

			if ("1".equals(string)) {
				json.put("res", string);
				json.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改商品银行失败");
		}
		// 修改操作 string 表示操作结果 返回值为 "1"或者"0"
		return new ModelAndView("jsonView", "ajax_json", json.toString());
	}

	@RequestMapping(value = "/batchMod")
	public String batchMod(String type, ModelMap modeMap) {
//		ZTreeUtil zTreeUtil = (ZTreeUtil)SpringContextUtil.getBean("ZTreeUtil");
		String zTreeNodes = getUPBankTree2();
		modeMap.addAttribute("zNodes", zTreeNodes);
		return "upgoodsbank/batchMod";
	}

	@RequestMapping(value = "/batchUpdateFromPage")
	public ModelAndView batchUpdateFromPage(GoodsBank goodsBank) {
		String result = "0";
		if (goodsBank != null && !goodsBank.getGoodsId().equals("") && !goodsBank.getBankId().equals("")) {
			try {
				result = goodsBankService.upBatchUpdate(goodsBank);
			} catch (Exception e) {
				log.error("批量配置失败", e);
			}
		} else {
			result = "非法数据！";
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	@RequestMapping(value = "/batchUpdateFromFile")
	public ModelAndView batchUpdateFromFile(HttpServletRequest request, GoodsBank goodsBank) {
		String result = "0";// 操作结果 1表示成功 0 表示失败
		try {
			MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
			// 获得文件：
			MultipartFile file = req.getFile("file");
			if (file == null) {
				log.info("没有获取到上传文件");
				return new ModelAndView("jsonView", "ajax_json", "非法操作");
			}
			if (!file.getOriginalFilename().endsWith(".txt")) {
				result = "上传文件类型只支持txt文件";
				log.info(result);
				return new ModelAndView("jsonView", "ajax_json", result);
			}
			double size = getFileMB(file.getSize());
			if (size == 0) {
				result = "不能上传空文件";
				log.info(result);
				return new ModelAndView("jsonView", "ajax_json", result);
			}
			if (size > 10) {
				result = "上传文件不能大于10MB";
				log.info(result);
				return new ModelAndView("jsonView", "ajax_json", result);
			}
			result = goodsBankService.upBatchUpdateFromFile(file, goodsBank);// 处理来自文本的修改
		} catch (Exception e) {
			log.error("批量配置失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e, HttpServletRequest request) {
		String result = "";
		if (e instanceof MaxUploadSizeExceededException) {
			result = "文件应不大于" + getFileMB(((MaxUploadSizeExceededException) e).getMaxUploadSize()) + "MB";
		} else {
			log.info("未知错误", e);
			result = "文件遇到未知错误，请重新上传";
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * *****************  方法说明  *****************
	 * method name   :  getTreeAndAmountByGoodsId
	 * @param		 :  @param GoodsId
	 * @param		 :  @param merId
	 * @param		 :  @return
	 * @return		 :  ModelAndView
	 * @author       :  lizhiqiang 2014年10月14日 上午11:21:13
	 * description   :  description : 通过商品号码获取已经配置过的商品银行
	 * 
	 * @see          :  
	 * **********************************************
	 */
	@RequestMapping(value = "/getTreeByGoodsId")
	public ModelAndView getTreeAndAmountByGoodsId(String GoodsId, String merId) {
		Map<String, String> responseMap = new HashMap<String, String>();
		String amount = "";
		responseMap.put("priceMode", "1");
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		if (merId != null && GoodsId != null) {
			GoodsInfo goodsInfo = (GoodsInfo) goodsMap.get(merId + "-" + GoodsId);
			if (goodsInfo != null) {
				responseMap.put("priceMode", String.valueOf(goodsInfo.getPriceMode()));
				amount = goodsInfo.getAmount(); // 根据信息获取金额
			}
		}
		responseMap.put("amount", amount);
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", GoodsId);
		
		// 根据商户号和商品号从商品银行中获取的已经配置银行号(XE,BS,GM)
		List<GoodsBank> goodsBankList = bankService.getBankInfosByGoodsId(mapWhere);
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService.getBankTypeMap("upbankType");
		//[{"3":"移动话费"},{"6":"小额"},{"61":"博升"},{"62":"游戏基地"}]
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		Map<String, String> AuditWhere = new HashMap<String, String>();
		AuditWhere.put("tableName", "UMPAY.T_GOODS_BANK");
		AuditWhere.put("ixData", merId + "-" + GoodsId);
		List<Audit> AuditConf = auditService.getCheckMerBankList(AuditWhere); // 查询审核表中的待审核信息，条件为
		
		StringBuffer zTreeNodes = new StringBuffer("[");// 构造树节点
		
		for (Map.Entry<String, String> entry : upBankTypeMap.entrySet()) {
			String upbanktype = entry.getKey();// GM
			String dbcode = entry.getValue();// 62
			List<GoodsBank> singleGoodsBank = new ArrayList<GoodsBank>();
			//树的增加只是
//			String GMNode = "{ id:'GM', pId:'0', name:'游戏基地', isParent:true , open:true}";// 游戏基地银行父节点
			String upbanktypeNode = "{ id:'"+upbanktype+"', pId:'0', name:'"+bankTypeMap.get(dbcode)+"', isParent:true}"; // 小额支付银行的父节点
			
			for (GoodsBank goodsBank : goodsBankList) {
				String id = goodsBank.getBankId();
				if (id != null) {
					if (id.startsWith(upbanktype)) {
						singleGoodsBank.add(goodsBank);
					}
				}
			}
			//dbcode为数据库银行通道代码，如XE=6.即dbcode=6,upbanktype=XE
			StringBuffer upbanktypeNodes = getSubTreeByGoodsId(GoodsId, merId,
					singleGoodsBank, AuditConf, dbcode, upbanktype);
			zTreeNodes .append(upbanktypeNode).append(upbanktypeNodes).append(",");// 再加节点内容
		}
		
		zTreeNodes = zTreeNodes.replace(zTreeNodes.length() - 1, zTreeNodes.length(), "]");

		responseMap.put("zTreeNodes", zTreeNodes.toString());
		return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(responseMap));
	}

	private StringBuffer getSubTreeByGoodsId(String GoodsId, String merId,
			List<GoodsBank> goodsBankList,
			List<Audit> auditList, String dbcode,
			String pIdNum) {
		//it should get the whole banklist which types are: XE, BS, GM 
		//2014年10月22日需求改为MerBank
				//List<BankInfo> bankInfolist = HfCacheUtil.getCache().getBankList();
				List<MerBank> merBankslist = merBankDaoImpl.findByMerId(merId);
				Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap(); // 缓存中获取商户银行的信息
//		bank.put("BANKTYPE", bankTypeMap.get(bankType.toString()));
		List<String> AuditConfStrings = new ArrayList<String>(); // 字符串类型的list
		// 存放t_hfaudit表中待审核的
		// 商品银行（merId-goodsId-bankId）
		for (Audit audit : auditList) {
			AuditConfStrings.add(audit.getIxData());
		}
		List<String> dbExitList = new ArrayList<String>(); // 字符串类型的list
																// 存放t_goods_bank表中已经做过配置的
																// 商品银行（bankId）
		for (GoodsBank goodsBank : goodsBankList) {
				// the goodsbank already exit in the DB (it will be used with the new data.)
				dbExitList.add(StringUtils.trim(goodsBank.getBankId()));
		}
		
		StringBuffer nodes = new StringBuffer(); // 小额节点
		// 长度不为0 说明有配置过得商品银行 将已经配置的银行勾选 没有配置的银行不做勾选
		if (AuditConfStrings.size() != 0 || (goodsBankList != null && goodsBankList.size() != 0)) {
		
			for (MerBank merbank : merBankslist) {
				StringBuffer singleNode = new StringBuffer();
				String bankId = StringUtils.trim(merbank.getBankId());
				BankInfo bankInfo = (BankInfo) bankMap.get(bankId);
				if (bankInfo != null && dbcode.equals(bankInfo.getBankType())) {
					singleNode.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'"+ pIdNum +"', name:'")
							.append(bankInfo.getBankName());
					if (!"2".equals(merbank.getState())) {
						singleNode.append("(已禁用)");
					}
					singleNode.append("'");
					if (dbExitList.contains(bankId)
							|| AuditConfStrings.contains(merId + "-" + GoodsId + "-" + bankId)) {
						// 判断实际t_goods_bank中有已经做过配置的银行 或在审核表中存在待审核数据
						singleNode.append(", checked:true ,chkDisabled:true");
					}
					
					// 都没有 则正常表示
					singleNode.append("}");
					nodes.append(",").append(singleNode);
				}
			}
		} 
		else {
			nodes = ZTreeUtil.getUPSubBankTree(merBankDaoImpl,merId,dbcode,pIdNum);
		}
		return nodes;
	}

	/**
	 * @Title: optLog
	 * @Description: 链接到商品银行操作日志页面
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2014-4-25 上午11:12:31
	 */
	@RequestMapping(value = "/optlog")
	public String optLog(ModelMap modeMap) {
		return "upgoodsbank/optlog";
	}

	/**
	 * @Title: queryOptLog
	 * @Description: 查询商品银行操作日志
	 * @return
	 * @author wanyong
	 * @date 2014-4-25 下午12:26:54
	 */
	@RequestMapping(value = "/queryoptlog")
	public ModelAndView queryOptLog() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				// 非系统管理人员只能查看半年内的操作日志
				if (getUser().getLoginName().equals("admin"))
					map.put("timeCheckFlag", "0"); // 把时间限制标识置为0（去除半年限制）
				else
					map.put("timeCheckFlag", "1"); // 把时间限制标识置为1（增加半年限制）
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				HfCache cache = HfCacheUtil.getCache();
				Map<String, Object> bankMap = cache.getBankInfoMap();
				for (int i = 0; i < data.size(); i++) {
					// 渲染数据
					Map<String, Object> audit = data.get(i);
					String ixData = (String) audit.get("ixData");
					String[] arr = ixData.split("-"); // ixData是以merId-goodsId-bankId的格式存放
					audit.put("MERID", arr[0].trim());
					audit.put("GOODSID", arr[1].trim());
					String bankId = arr[2].trim();
					String bankName = "";
					// 渲染银行名称
					if (bankId != null && !bankId.equals("")) {
						BankInfo bankInfo = (BankInfo) bankMap.get(bankId.trim());
						if (bankInfo != null) {
							bankName = bankInfo.getBankName();
						}
						audit.put("BANKNAME", bankName);
					}
					audit.put("BANKID", bankId);
					// 渲染提交人
					String creatorId = ((String) audit.get("creator")).trim();
					audit.put("CREATOR", users.get(creatorId));
					if (audit.get("CREATOR") == null) {
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					// 渲染商品名称
					String goodsName = (String) audit.get("ixData2");
					audit.put("GOODSNAME", goodsName.trim());
					data.set(i, audit);
				}
				long count = queryCount(queryKey, map);
				ObjectUtil.trimData(data);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				log.info("查询失败", e);
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					log.info("json序列化失败", e);
				}
			}
		} else {
			log.info("无法获得查询key");
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				log.info("json序列化失败", e);
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: exportOptLog
	 * @Description: 导出商品银行操作日志
	 * @return
	 * @author wanyong
	 * @date 2014-4-26 上午2:04:53
	 */
	@RequestMapping(value = "/exportoptlog")
	@SuppressWarnings("unchecked")
	public ModelAndView exportOptLog() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		List<GoodsBankOptLogExcel> goodsBankOptLogExcelList = null;
		if (queryKey != null) {
			// 这里不分页查询
			// IUniQueryService uniQueryService = (IUniQueryService)
			// SpringContextUtil.getBean("uniQueryServiceOffline");
			IUniQueryService uniQueryService = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> mapList = uniQueryService.query(queryKey, map);
			if (mapList != null && mapList.size() > 0) {
				// 格式化数据
				ObjectUtil.trimData(mapList);
				goodsBankOptLogExcelList = new ArrayList<GoodsBankOptLogExcel>();
				Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
				for (Map<String, Object> subMap : mapList) {
					// 渲染数据
					GoodsBankOptLogExcel goodsBankOptLogExcel = new GoodsBankOptLogExcel();
					// 修改类型
					int auditType = (Integer) subMap.get("AUDITTYPE");
					if (auditType == 1) {
						goodsBankOptLogExcel.setOptType("新增");
					} else if (auditType == 2) {
						goodsBankOptLogExcel.setOptType("修改");
					} else if (auditType == 3) {
						goodsBankOptLogExcel.setOptType("启用");
					} else if (auditType == 4) {
						goodsBankOptLogExcel.setOptType("禁用");
					} else {
						goodsBankOptLogExcel.setOptType("未知");
					}
					String modData = (String) subMap.get("MODDATA");
					GoodsBank goodsBank = (GoodsBank) JsonHFUtil.getObjFromJsonArrStr(modData, GoodsBank.class);
					goodsBankOptLogExcel.setMerId(goodsBank.getMerId()); // 商户号
					goodsBankOptLogExcel.setGoodsId(goodsBank.getGoodsId()); // 商品号
					goodsBankOptLogExcel.setGoodsName((String) subMap.get("IXDATA2")); // 商品名称
					// 渲染银行名称
					if ((goodsBank.getBankName() == null || "".equals(goodsBank.getBankName()))
							&& goodsBank.getBankId() != null && !goodsBank.getBankId().equals("")) {
						BankInfo bankInfo = (BankInfo) bankMap.get(goodsBank.getBankId().trim());
						if (bankInfo != null) {
							goodsBankOptLogExcel.setBank(bankInfo.getBankName() + "（" + goodsBank.getBankId() + "）"); // 支付银行
						}
					} else {
						goodsBankOptLogExcel.setBank(goodsBank.getBankName() + "（" + goodsBank.getBankId() + "）"); // 支付银行
					}
					goodsBankOptLogExcel.setVerifyTag(goodsBank.getVerifyTag()); // 2次确认校验码
					goodsBankOptLogExcel.setCheckDay(goodsBank.getCheckDay()); // 商品最长计费天数
					// 商品开通状态
					if ("11".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("只开通新增");
					} else if ("12".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("只保留续费");
					} else if ("13".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("新增与续费全部开通");
					} else if ("23".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("新增与续费全部注销");
					} else {
						goodsBankOptLogExcel.setkState(goodsBank.getkState());
					}
					// 下发商品时间
					if ("0".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("即时下发");
					} else if ("1".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("智能网异步发货");
					} else if ("2".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("BOSS异步发货");
					} else if ("3".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("全部异步发货");
					} else {
						goodsBankOptLogExcel.setIsRealTime(goodsBank.getIsRealTime());
					}
					// 渲染提交人
					String creatorId = ((String) subMap.get("CREATOR")).trim();
					goodsBankOptLogExcel.setCreator(users.get(creatorId) == null ? messageService
							.getSystemParam(creatorId) : users.get(creatorId)); // 提交人
					goodsBankOptLogExcel.setInTime(subMap.get("INTIME") + ""); // 提交日期
					goodsBankOptLogExcel.setDesc((String) subMap.get("DESC")); // 操作原因

					goodsBankOptLogExcelList.add(goodsBankOptLogExcel);
				}
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", GoodsBankOptLogExcel.class);
		map2.put("data", goodsBankOptLogExcelList);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: batchOptLog
	 * @Description: 链接到商品银行批量操作日志页面
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2014-4-26 下午11:11:33
	 */
	@RequestMapping(value = "/batchoptlog")
	public String batchOptLog(ModelMap modeMap) {
		return "upgoodsbank/batchoptlog";
	}

	/**
	 * @Title: queryBatchOptLog
	 * @Description: 查询商品银行批量操作日志
	 * @return
	 * @author wanyong
	 * @date 2014-4-26 下午11:13:10
	 */
	@RequestMapping(value = "/querybatchoptlog")
	public ModelAndView queryBatchOptLog() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				// 非系统管理人员只能查看半年内的操作日志
				if (getUser().getLoginName().equals("admin"))
					map.put("timeCheckFlag", "0"); // 把时间限制标识置为0（去除半年限制）
				else
					map.put("timeCheckFlag", "1"); // 把时间限制标识置为1（增加半年限制）
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> audit = data.get(i);
					// 渲染提交人
					String creatorId = ((String) audit.get("creator")).trim();
					audit.put("CREATOR", users.get(creatorId));
					if (audit.get("CREATOR") == null) {
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					data.set(i, audit);
				}
				long count = queryCount(queryKey, map);
				ObjectUtil.trimData(data);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				log.info("查询失败", e);
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					log.info("json序列化失败", e);
				}
			}
		} else {
			log.info("无法获得查询key");
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				log.info("json序列化失败", e);
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: exportBatchOptLog
	 * @Description: 导出商品银行批量操作日志
	 * @return
	 * @author wanyong
	 * @date 2014-4-26 下午11:13:49
	 */
	@RequestMapping(value = "/exportbatchoptlog")
	@SuppressWarnings("unchecked")
	public ModelAndView exportBatchOptLog() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		List<GoodsBankOptLogExcel> goodsBankOptLogExcelList = null;
		if (queryKey != null) {
			// 这里不分页查询
			// IUniQueryService uniQueryService = (IUniQueryService)
			// SpringContextUtil.getBean("uniQueryServiceOffline");
			IUniQueryService uniQueryService = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> mapList = uniQueryService.query(queryKey, map);
			if (mapList != null && mapList.size() > 0) {
				// 格式化数据
				ObjectUtil.trimData(mapList);
				goodsBankOptLogExcelList = new ArrayList<GoodsBankOptLogExcel>();
				Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
				for (Map<String, Object> subMap : mapList) {
					// 渲染数据
					GoodsBankOptLogExcel goodsBankOptLogExcel = new GoodsBankOptLogExcel();
					// 修改类型
					int auditType = (Integer) subMap.get("AUDITTYPE");
					if (auditType == 1) {
						goodsBankOptLogExcel.setOptType("新增");
					} else if (auditType == 2) {
						goodsBankOptLogExcel.setOptType("修改");
					} else if (auditType == 3) {
						goodsBankOptLogExcel.setOptType("启用");
					} else if (auditType == 4) {
						goodsBankOptLogExcel.setOptType("禁用");
					} else {
						goodsBankOptLogExcel.setOptType("未知");
					}
					String modData = (String) subMap.get("MODDATA");
					List<GoodsBank> goodsBankList = (List<GoodsBank>) JsonHFUtil.getListFromJsonArrStr(modData,
							GoodsBank.class);
					GoodsBank goodsBank = goodsBankList.get(0);
					goodsBankOptLogExcel.setMerId(goodsBank.getMerId()); // 商户号
					goodsBankOptLogExcel.setGoodsId(goodsBank.getGoodsId()); // 商品号
					goodsBankOptLogExcel.setGoodsName((String) subMap.get("IXDATA2")); // 商品名称
					// 渲染银行名称
					if ((goodsBank.getBankName() == null || "".equals(goodsBank.getBankName()))
							&& goodsBank.getBankId() != null && !goodsBank.getBankId().equals("")) {
						BankInfo bankInfo = (BankInfo) bankMap.get(goodsBank.getBankId().trim());
						if (bankInfo != null) {
							goodsBankOptLogExcel.setBank(bankInfo.getBankName() + "（" + goodsBank.getBankId() + "）"); // 支付银行
						}
					} else {
						goodsBankOptLogExcel.setBank(goodsBank.getBankName() + "（" + goodsBank.getBankId() + "）"); // 支付银行
					}
					goodsBankOptLogExcel.setVerifyTag(goodsBank.getVerifyTag()); // 2次确认校验码
					goodsBankOptLogExcel.setCheckDay(goodsBank.getCheckDay()); // 商品最长计费天数
					// 商品开通状态
					if ("11".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("只开通新增");
					} else if ("12".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("只保留续费");
					} else if ("13".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("新增与续费全部开通");
					} else if ("23".equals(goodsBank.getkState())) {
						goodsBankOptLogExcel.setkState("新增与续费全部注销");
					} else {
						goodsBankOptLogExcel.setkState(goodsBank.getkState());
					}
					// 下发商品时间
					if ("0".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("即时下发");
					} else if ("1".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("智能网异步发货");
					} else if ("2".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("BOSS异步发货");
					} else if ("3".equals(goodsBank.getIsRealTime())) {
						goodsBankOptLogExcel.setIsRealTime("全部异步发货");
					} else {
						goodsBankOptLogExcel.setIsRealTime(goodsBank.getIsRealTime());
					}
					// 渲染提交人
					String creatorId = ((String) subMap.get("CREATOR")).trim();
					goodsBankOptLogExcel.setCreator(users.get(creatorId) == null ? messageService
							.getSystemParam(creatorId) : users.get(creatorId)); // 提交人
					goodsBankOptLogExcel.setInTime(subMap.get("INTIME") + ""); // 提交日期
					goodsBankOptLogExcel.setDesc((String) subMap.get("DESC")); // 操作原因

					goodsBankOptLogExcelList.add(goodsBankOptLogExcel);
				}
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", GoodsBankOptLogExcel.class);
		map2.put("data", goodsBankOptLogExcelList);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}
	public String getUPBankTree2(){
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService.getBankTypeMap("upbankType");
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		StringBuffer zTreeNodes = new StringBuffer("[");// 构造树节点
		List<BankInfo> goodsBankList=HfCacheUtil.getCache().getBankList();
		for (Map.Entry<String, String> entry : upBankTypeMap.entrySet()) {
			String upbanktype = entry.getKey();// GM
			String dbcode = entry.getValue();// 62
			List<BankInfo> singleGoodsBank = new ArrayList<BankInfo>();
			//树的增加只是
//			String GMNode = "{ id:'GM', pId:'0', name:'游戏基地', isParent:true , open:true}";// 游戏基地银行父节点
			String upbanktypeNode = "{ id:'"+upbanktype+"', pId:'0', name:'"+bankTypeMap.get(dbcode)+"', isParent:true}"; // 小额支付银行的父节点
			
			for (BankInfo goodsBank : goodsBankList) {
				goodsBank.trim();
				String id = goodsBank.getBankId();
				if (id != null) {
					if (id.startsWith(upbanktype)) {
						singleGoodsBank.add(goodsBank);
					}
				}
			}
			
			StringBuffer upbanktypeNodes = ZTreeUtil.getUPSubBankTree(dbcode,upbanktype);
			zTreeNodes .append(upbanktypeNode).append(upbanktypeNodes).append(",");// 再加节点内容
		}
		
		zTreeNodes = zTreeNodes.replace(zTreeNodes.length() - 1, zTreeNodes.length(), "]");

		return zTreeNodes.toString();
	}
}
