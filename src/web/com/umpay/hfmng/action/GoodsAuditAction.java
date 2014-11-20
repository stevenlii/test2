package com.umpay.hfmng.action;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.GoodsInfoService;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * ******************  类说明  *********************
 * class       :  GoodsAuditAction
 * @author     :  xhf
 * @version    :  1.0  
 * description :  商品审核
 * ***********************************************
 */
@Controller
@RequestMapping("/goodsaudit")
public class GoodsAuditAction extends BaseAction {
	@Autowired
	private AuditService service;
	@Autowired
	private GoodsInfoService goodsService;
	@Autowired
	private MerInfoService merService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;

	/**
	 * ********************************************
	 * method name   : index 
	 * description   : 商品审核首页
	 * @return       : String
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-9-3  下午06:34:27
	 * *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("goodsinf");
		modeMap.addAttribute("opts", opts);
		return "goodsaudit/index";
	}

	/**
	 * ********************************************
	 * method name   : list 
	 * description   : 多条件查询
	 * @return       : ModelAndView
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-9-3  下午06:42:18
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map audit = data.get(i);
					String ixData = (String) audit.get("ixData");
					String[] arr = ixData.split("-"); //ixData是以   merId-goodsId 的格式存放
					audit.put("MERID", arr[0]);
					audit.put("GOODSID", arr[1]);
					//解析json串
					GoodsInfo modData = (GoodsInfo) JsonHFUtil.getObjFromJsonArrStr(
													(String) audit.get("modData"), GoodsInfo.class);
					if(modData.getCategory() != null){
						String GoodsCategory = GoodsCateGorymap.get(modData.getCategory().trim());
						audit.put("CATEGORY", GoodsCategory);
					}
					String creatorId = ((String) audit.get("creator")).trim();
					audit.put("CREATOR", users.get(creatorId));
					if(audit.get("CREATOR") == null){
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					if("0".equals(audit.get("state").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = ((String) audit.get("modUser")).trim();
						audit.put("MODUSER", users.get(modUserId));
						if(audit.get("MODUSER") == null){
							audit.put("MODUSER", messageService.getSystemParam(modUserId));
						}
					}
					audit.put("GOODSNAME", modData.getGoodsName());
					audit.put("SERVTYPE", modData.getServType());
					audit.put("PRICEMODE", modData.getPriceMode());
					data.set(i, audit);
				}
				long count = queryCount(queryKey, map);
				ObjectUtil.trimData(data);
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
	 * method name   : goodsAuditPass 
	 * description   : 商品审核通过
	 * @return       : ModelAndView
	 * @param        : @param id
	 * @param        : @param resultDesc
	 * @param        : @param modeMap
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-9-3  下午06:45:25
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsAuditPass")
	public ModelAndView goodsAuditPass(String id,String resultDesc, ModelMap modeMap){
		Map map = new HashMap();
		map.put("id", id);
		Audit audit = service.load(map);
		audit.trim();  //去空格
		if(!"0".equals(audit.getState())){
			String string = "该数据已审核，不能再次审核";
			log.error(string + audit);
			return new ModelAndView("jsonView","ajax_json",string);
		}
		if(!service.checkAuditUser(audit.getCreator())){
			return new ModelAndView("jsonView", "ajax_json", "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。");
		}
		audit.setState("2"); //审核状态改为通过
		audit.setModTime(new Timestamp(System.currentTimeMillis()));
		audit.setModUser(getUser().getId());
		if(resultDesc != null){
			audit.setResultDesc(resultDesc);
		}
		String string = "no";
		try{
			string = service.goodsAuditPass(audit);  //返回值为yes或者no
			log.info("商品审核通过成功");
		}catch (Exception e) {
			log.error("商品审核通过失败");
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	/**
	 * ********************************************
	 * method name   : goodsAuditNotPass 
	 * description   : 商品审核不通过
	 * @return       : ModelAndView
	 * @param        : @param id
	 * @param        : @param resultDesc
	 * @param        : @param modeMap
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-9-3  下午06:46:39
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsAuditNotPass")
	public ModelAndView goodsAuditNotPass(String id, String resultDesc, ModelMap modeMap){
		Map map = new HashMap();
		map.put("id", id);
		Audit audit = service.load(map);
		audit.trim();
		if(!"0".equals(audit.getState())){
			String string = "该数据已审核，不能再次审核";
			log.error(string + audit);
			return new ModelAndView("jsonView","ajax_json",string);
		}
		if(!service.checkAuditUser(audit.getCreator())){
			return new ModelAndView("jsonView", "ajax_json", "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。");
		}
		audit.setState("1"); //审核状态改为不通过
		audit.setModTime(new Timestamp(System.currentTimeMillis()));
		audit.setModUser(getUser().getId());
		audit.setResultDesc(resultDesc);
		String string = "no";
		try{
			string = service.goodsAuditNotPass(audit);  //返回值为yes或者no
			log.info("商品审核不通过成功");
		}catch (Exception e) {
			log.error("商品审核不通过失败");
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}

	/**
	 * ********************************************
	 * method name   : detail 
	 * description   : 详情展示页面
	 * @return       : String
	 * @param        : @param id
	 * @param        : @param modeMap
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-9-3  下午06:46:53
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap)
			throws DataAccessException {
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("goodsinf");
		modeMap.addAttribute("opts", opts);
		
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();  // 获取商户缓存信息
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit au = service.load(mapId);
		modeMap.addAttribute("auditType", au.getAuditType());
		//反序列化审核信息
		GoodsInfo audit = (GoodsInfo) JsonHFUtil
						.getObjFromJsonArrStr(au.getModData(),GoodsInfo.class);
		audit.trim(); //去首尾空格
		audit.setId(id);
		MerInfo mer = merService.load(audit.getMerId());
		if(mer != null){
			audit.setMerName(((MerInfo)merMap.get(audit.getMerId())).getMerName()); //设置商户名
		}
		String categoryName = HfCacheUtil.getCache().getCategoryAbsoluteName(audit.getCategory());
		audit.setCategory(categoryName);
		//获取风空类型list
		List goodsTypeList = optionService.getGoodsType();
		for(int i=0;i<goodsTypeList.size();i++){
			GoodsTypeModel goodstype = (GoodsTypeModel) goodsTypeList.get(i);
			if(goodstype.getGoodsType().trim().equals(audit.getGoodsType())){
				audit.setGoodsType(goodstype.getDetail().trim());
			}
		}
		//渲染业务覆盖
		if ("01".equals(audit.getBusiType())) {
			audit.setBusiType("省网");
		} else if ("10".equals(audit.getBusiType())) {
			audit.setBusiType("全网");
		} else if ("11".equals(audit.getBusiType())) {
			audit.setBusiType("全网、省网");
		}
		//渲染服务类型
		if ("2".equals(audit.getServType())){
			audit.setServType("按次商品");
		} else if ("3".equals(audit.getServType())){
			audit.setServType("包月商品");
		}
		
		modeMap.addAttribute("audit", audit);
		modeMap.addAttribute("resultDesc", au.getResultDesc());
		String state = au.getState();
		if ("0".equals(state)) { //是否为待审核状态
			if ("2".equals(au.getAuditType())) {  //审核类型为修改,需获取历史数据展示到页面
				Map map = new HashMap();
				String[] arr = au.getIxData().split("-");
				map.put("merId", arr[0]);
				map.put("goodsId", arr[1]);
				GoodsInfo oldGoods = goodsService.load(map);
				if(oldGoods != null){
					if(oldGoods.getServType()=="3"||oldGoods.getServType().equals("3")){
						GoodsInfo monGoodsInfo=goodsService.loadMonGoods(map);
						if(monGoodsInfo != null){
							oldGoods.setServMonth(monGoodsInfo.getServMonth());
							oldGoods.setConMode(monGoodsInfo.getConMode());
							oldGoods.setInterval(monGoodsInfo.getInterval());
						}
						
					}
				}
				oldGoods.trim();
				oldGoods.setMerName(((MerInfo)merMap.get(oldGoods.getMerId())).getMerName().trim());
				String oldCategoryName = HfCacheUtil.getCache().getCategoryAbsoluteName(oldGoods.getCategory());
				oldGoods.setCategory(oldCategoryName);
				
				for(int j=0;j<goodsTypeList.size();j++){
					GoodsTypeModel goodstype = (GoodsTypeModel) goodsTypeList.get(j);
					if(goodstype.getGoodsType().trim().equals(oldGoods.getGoodsType())){
						oldGoods.setGoodsType(StringUtils.trim(goodstype.getDetail()));
					}
				}
				
				if ("01".equals(oldGoods.getBusiType())) {
					oldGoods.setBusiType("省网");
				} else if ("10".equals(oldGoods.getBusiType())) {
					oldGoods.setBusiType("全网");
				} else if ("11".equals(oldGoods.getBusiType())) {
					oldGoods.setBusiType("全网、省网");
				}
				
				if ("2".equals(oldGoods.getServType())){
					oldGoods.setServType("按次商品");
				} else if ("3".equals(oldGoods.getServType())){
					oldGoods.setServType("包月商品");
				}
				
				modeMap.addAttribute("oldGoods", oldGoods);
				return "goodsaudit/detailMod";  //修改详情页面
			} else {
				return "goodsaudit/detailAdd";  //新增，启用/禁用详情页面
			}
		} else {
			return "goodsaudit/showDetail";  //审核后详情页面
		}
	}
	
}
