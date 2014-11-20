package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.model.ReportInf;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.ReportService;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/report")
public class ReportAction extends BaseAction {

	@Autowired
	private ReportService reportService;
	@Autowired
	private OptionService optionService;

	@RequestMapping(value = "/toQueryDataPage")
	public String toQueryDataPage() {
		return "report/queryData";
	}

	// 查询报备数据
	@RequestMapping(value = "/queryData")
	public ModelAndView queryData(String queryKey, String merId, String goodsId) {
		merId = StringUtils.trim(merId);
		goodsId = StringUtils.trim(goodsId);
		Map<String, String> map = new HashMap<String, String>();
		if (merId != null && !"".equals(merId))
			// 加密查询条件中的兑换码
			map.put("merId", merId);
		if (goodsId != null && !"".equals(goodsId))
			// 加密查询条件中的兑换码
			map.put("goodsId", goodsId);

		String msg = null;
		if (queryKey != null) {
			try {
				long a = System.currentTimeMillis();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				dealData(data, true);
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
				log.info("查询报备信息耗时" + (System.currentTimeMillis() - a));
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
	
	private void dealData(List<Map<String, Object>> data, boolean flag) throws Exception{
		// 格式化数据
		ObjectUtil.trimData(data);
		Map<String, String> merCateGorymap = optionService.getMerCategoryMap();
		Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map<String, Object> mers = HfCacheUtil.getCache().getMerInfoMap();
		Map<String, String> bankMap = reportService.queryForBank();
		// 渲染数据
		if (data != null && data.size() > 0) {
			int i = 0;
			for (Map<String, Object> map2 : data) {
				if((++i)%1000 == 0){
					log.info("开始处理第"+(++i)+"行");
				}
				setBanks(map2, bankMap, flag);
				String merId = map2.get("MERID").toString();
				String key = "";
				MerInfo mer = (MerInfo) mers.get(merId);
				String merName = "";
				String merCategory = "";
				String busiType = "";
				if(mer != null){
					merName = mer.getMerName();
					merCategory = mer.getCategory();
					busiType = mer.getBusiType();
					map2.put("COMPANY_DESC", mer.getCompanyDesc());
					map2.put("MER_WEB", mer.getMerWeb());
					map2.put("REG_CAPITAL", mer.getRegCapital());
					map2.put("REG_TIEM", mer.getRegTime());
					map2.put("YEAR_PROFIT", mer.getYearProfit());
					map2.put("USER_SCALE", mer.getUserScale());
					map2.put("BUSI_DESC", mer.getBusiDesc());
					map2.put("SUPPORT", mer.getSupport());
					map2.put("SALE_CHANNEL", mer.getSaleChannel());
					map2.put("SRC_MER", mer.getSrcMer());
					map2.put("SHARED_RATE", mer.getSharedRate());
				}
				map2.put("MERNAME", merName);
				// 商户分类
				if (null == merCategory) {
					key = "";
				} else {
					key = merCategory;
				}
				map2.put("MERCATEGORY", merCateGorymap.containsKey(key) ? merCateGorymap.get(key) : key);
				// 业务属性
				if (null == busiType) {
					key = "";
				} else {
					key = busiType;
				}
				map2.put("BUSITYPE", busiTypeMap.containsKey(key) ? busiTypeMap.get(key) : key);
				
				// 商品分类
				String goodsCategory = (String) map2.get("GOODCATEGORY");;
				map2.put("GOODCATEGORY", GoodsCateGorymap.containsKey(goodsCategory) ? GoodsCateGorymap.get(goodsCategory) : goodsCategory);
			}
		}
	}
	/**
	 * ********************************************
	 * method name   : setBanks 
	 * description   : 
	 * @return       : void
	 * @param        : @param data
	 * @param        : @param bankMap
	 * @param        : @param flag 为true时，新增缺少的数据
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-10-8  下午06:09:05
	 * *******************************************
	 */
	private void setBanks(Map<String, Object> data,Map<String, String> bankMap, boolean flag) throws Exception{
		String merId = data.get("MERID").toString();
		String goodsId = data.get("GOODSID").toString();
		String[] bank = { "XE010000", "XE021000", "XE028000", "XE591000",
				"XE771000", "XE431000", "XE371000", "XE024000", "XE311000",
				"XE451000", "XE471000", "XE571000", "XE027000", "XE931000",
				"XE029000", "XE351000", "XE023000", "XE898000", "XE871000",
				"XE025000", "XE791000", "XE020000" };
		String[] bankName = { "BEIJING", "SHANGHAI", "SICHUAN", "FUJIAN",
				"GUANGXI", "JILIN", "HENAN", "LIAONING", "HEBEI",
				"HEILONGJIANG", "NEIMENGGU", "ZHEJIANG", "HUBEI", "GANSU",
				"SHANXI1", "SHANXI2", "CHONGQING", "HAINAN", "YUNNAN",
				"JIANGSU", "JIANGXI", "GUANGDONG", };
		String key = "";
		String backupStat = "";
		
		//若无此省份的报备信息，则直接insert一条
		ReportInf reportInf = new ReportInf();
		reportInf.setMerId(merId);
		reportInf.setGoodsId(goodsId);
		reportInf.setBackupStat(0); // 未报备
		for(int i=0;i<bank.length;i++){
			key = merId + "-" + goodsId + "-" + bank[i];
			backupStat = bankMap.get(key);
			if(backupStat == null){
				reportInf.setBankId(bank[i]);
				if(flag){
					backupStat = reportService.insertBackupBank(reportInf) + "-0";
				}else{
					backupStat = "-0";
				}
			}
			data.put(bankName[i], backupStat);
		}
	}

	/**
	 * @Title: export
	 * @Description: 报备数据导出
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-5 上午11:56:19
	 */
	@RequestMapping(value = "/export")
	public ModelAndView export(String queryKey, String merId, String goodsId) {
		merId = StringUtils.trim(merId);
		goodsId = StringUtils.trim(goodsId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (merId != null && !"".equals(merId))
			// 加密查询条件中的兑换码
			map.put("merId", merId);
		if (goodsId != null && !"".equals(goodsId))
			// 加密查询条件中的兑换码
			map.put("goodsId", goodsId);

		List<Map<String, Object>> mapList = null;
		if (queryKey != null) {
			try {
				long a = System.currentTimeMillis();
				// 这里不分页查询
				IUniQueryService uniQueryService = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
				mapList = uniQueryService.query(queryKey, map);
				dealData(mapList, false);
				log.info("查询需要导出的报备信息耗时" + (System.currentTimeMillis() - a));
			} catch (Exception e) {
				log.error("导出失败", e);
			}
		}
		return new ModelAndView("bakeupDataView", "excel", mapList);
	}

	@RequestMapping(value = "/backupDate")
	public ModelAndView backupDate(String backupids) {
		String msg = "";
		try {
			log.info("开始插入报备信息操作数据 ，报备数据ID为 :   " + backupids);
			for (String backupid : backupids.split(",")) {
				if (StringUtil.validateNull(backupid)) {
					reportService.addBackUpData(backupid, StringUtil.validateNull(this.getUser().getId()) ? this
							.getUser().getId() : "");
				}
			}
			log.info("插入报备信息操作数据成功.");
		} catch (Exception e) {
			log.info("插入报备操作数据失败：" + e.getMessage());
			msg = "error";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

}
