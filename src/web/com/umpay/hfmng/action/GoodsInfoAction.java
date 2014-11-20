package com.umpay.hfmng.action;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.GoodsInfoService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/goodsinf")
public class GoodsInfoAction extends BaseAction {
	@Autowired
	private GoodsInfoService goodsInfoService;
	@Autowired
	private AuditService auditService; 
	@Autowired
	private OptionService optionService;
	

	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) throws DataAccessException {
		String tree = ZTreeUtil.getGoodsCategoryWithAll();
		modeMap.addAttribute("zNodes", tree);
		//查找权限
//		String opts=getOptions("goodsinf");
//		opts="001,002,003,004,005,006,007";  权限测试
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("goodsinf");
		modeMap.addAttribute("opts", opts);
		return "goodsinf/index";
	}

 /**
  * ********************************************
  * method name   : add 
  * description   :  添加跳转
  * @return       : String
  * @param        : @param modeMap
  * @param        : @return
  * modified      : zhaojunbao ,  2012-9-11  下午04:39:12
  * @see          : 
  * *******************************************
  */
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap)  {
		String tree = ZTreeUtil.getGoodsCategory();
		modeMap.addAttribute("zNodes", tree);
		return "goodsinf/add";
	}
	
	
	
	/**
	 * ******************************************** method name : save
	 * description :保存商品信息action
	 * 
	 * @return : ModelAndView
	 * @param : @param goodsInf
	 * @param : @return
	 * @param : @throws DataAccessException modified : Administrator , 2012-8-13
	 *        下午09:04:53
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(GoodsInfo goodsInfo)  {
		if (goodsInfo.getBusiType().length() > 2) {   //页面的业务覆盖值包括三种 省网（01），全网（10），省网、全网（11），
			                            //如果两个都选择，页面传过值为两个字符串的拼接，长度会大于2
			goodsInfo.setBusiType("11");
		} 
		// 页面取到的商品发送短信方式为 pusnInf加mtNum的格式，需要重新解析单独保存
		int pushInfmtNum = goodsInfo.getPushInf();
		if (pushInfmtNum == 01) {
			goodsInfo.setPushInf(0);
			goodsInfo.setMtNum(1);
		}
		if (pushInfmtNum == 11) {
			goodsInfo.setPushInf(1);
			goodsInfo.setMtNum(1);
		}
		if (pushInfmtNum == 12) {
			goodsInfo.setPushInf(1);
			goodsInfo.setMtNum(2);
		}
		User user=this.getUser();
		
		goodsInfo.setModUser(user.getId()); // 新增修改人操作
		String string="0";
		try {
			string = goodsInfoService.insertGoodsAudit(goodsInfo,"1");
		} catch (Exception e) {
			log.error("新增商品失败", e);
		} //参数 1 表示新增
		  //string表示操作结果，由service层返回 值为 yes 或者no
      		//String string="1";
		return new ModelAndView("jsonView", "ajax_json", string);
	}

	/**
	 * ******************************************** method name : load
	 * description : 点击修改时，加载修改内容 
	 * 
	 * @return : String
	 * @param : @param merId
	 * @param : @param goodsId
	 * @param : @param modeMap
	 * @param : @return
	 * @param : @throws DataAccessException modified : Administrator , 2012-8-16
	 *        下午05:09:12
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/modifyGoods")
	public String modify(String merId, String goodsId, ModelMap modeMap) {
		String tree = ZTreeUtil.getGoodsCategory();
		modeMap.addAttribute("zNodes", tree);
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		GoodsInfo goods = goodsInfoService.load(mapWhere);
	    if(goods != null){
		if(goods.getServType()=="3"||goods.getServType().equals("3")){
			GoodsInfo monGoodsInfo=goodsInfoService.loadMonGoods(mapWhere);
			goods.setServMonth(monGoodsInfo.getServMonth());
			goods.setConMode(monGoodsInfo.getConMode());
			goods.setInterval(monGoodsInfo.getInterval());
		}
	    }
		if (goods != null) {
			Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
			String merName = ((MerInfo) merMap.get(goods.getMerId().trim()))
					.getMerName();
			goods.setMerName(merName); // 手动设置 修改页面展示商户名称

			String pushInf = String.valueOf(goods.getPushInf());
			String mtNum = String.valueOf(goods.getMtNum());
			goods.setPushInf(Integer.valueOf(pushInf + mtNum));
		}
		goods.trim();
		modeMap.addAttribute("goods", goods);
		return "goodsinf/modifyGoods";
	}

	/**
	 * ******************************************** method name : update
	 * description : 修改后保存方法
	 * 
	 * @return : String
	 * @param : @param merId
	 * @param : @param goodsId
	 * @param : @param modeMap
	 * @param : @return
	 * @param : @throws DataAccessException modified : Administrator , 2012-8-15
	 *        下午03:21:15
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(GoodsInfo goodsInfo, ModelMap modeMap) {
		if (goodsInfo.getBusiType().length() > 2) {   //页面的业务覆盖值包括三种 省网（01），全网（10），省网、全网（11），
            //如果两个都选择，页面传过值为两个字符串的拼接，长度会大于2
			goodsInfo.setBusiType("11");
		} 
		// 页面取到的商品发送短信方式为 pusnInf加mtNum的格式，需要重新解析单独保存
		int pushInfmtNum = goodsInfo.getPushInf();
		if (pushInfmtNum == 01) {
			goodsInfo.setPushInf(0);
			goodsInfo.setMtNum(1);
		}else if (pushInfmtNum == 11) {
			goodsInfo.setPushInf(1);
			goodsInfo.setMtNum(1);
		}else if (pushInfmtNum == 12) {
			goodsInfo.setPushInf(1);
			goodsInfo.setMtNum(2);
		}
		User user = this.getUser();
		goodsInfo.setModUser(user.getId()); // 修改人
		String string="0";
	    try {
			string = goodsInfoService.modifyGoodsInfo(goodsInfo);
		} catch (Exception e) {
			log.error("修改商品失败", e);
		}
		 // 修改操作   string 表示操作结果 返回值为 "1"或者"0"
		return new ModelAndView("jsonView", "ajax_json", string);
	}
 /**
  * ********************************************
  * method name   : showDetail 
  * description   :  详情显示
  * @return       : String
  * @param        : @param merId
  * @param        : @param goodsId
  * @param        : @param modeMap
  * @param        : @return
  * modified      : zhaojunbao ,  2012-8-31  上午11:05:55
  * @see          : 
  * *******************************************
  */
	@RequestMapping(value = "/detail")
	public String showDetail(String merId, String goodsId, ModelMap modeMap)
			 {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);

		GoodsInfo goods = goodsInfoService.load(mapWhere);
		goods.trim();     //将对象中属性值去空
		if(goods != null){
			if(goods.getServType()=="3"||goods.getServType().equals("3")){
				GoodsInfo monGoodsInfo=goodsInfoService.loadMonGoods(mapWhere);
				if(monGoodsInfo != null){
					goods.setServMonth(monGoodsInfo.getServMonth());
					goods.setConMode(monGoodsInfo.getConMode());
					goods.setInterval(monGoodsInfo.getInterval());
				}
			}
		    }
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		String merid=goods.getMerId(); 
		MerInfo merInfo = (MerInfo) merMap.get(merid);
		String merName = "";
		if(merInfo != null){
			merName = merInfo.getMerName();
		}
		goods.setMerName(merName); // 手动设置 修改页面展示商户名称
		
		String categoryName = HfCacheUtil.getCache().getCategoryAbsoluteName(goods.getCategory());
		goods.setCategory(categoryName);
		
		goods.setPushInf(goods.getPushInf()+goods.getMtNum());//处理页面展示短信下发信息
		//获取风空类型list
		List goodsTypeList = optionService.getGoodsType();
		for(int i=0;i<goodsTypeList.size();i++){
			GoodsTypeModel goodstype = (GoodsTypeModel) goodsTypeList.get(i);
			if(goodstype.getGoodsType().trim().equals(goods.getGoodsType())){
				goods.setGoodsType(goodstype.getDetail().trim());
			}
		}
		modeMap.addAttribute("goods", goods);
		return "goodsinf/detail";
	}
	/**
	 * ******************************************** method name : queryAndView
	 * description : uniQuery 查找方法
	 * 
	 * @return : ModelAndView
	 * @param : @return
	 * @param : @throws DataAccessException modified : Administrator , 2012-8-16
	 *        下午05:10:03
	 * @see : *******************************************
	 */

	@RequestMapping(value = "/query")
	public ModelAndView queryAndView()  {
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(
						queryKey, map);
				ObjectUtil.trimData(data);// 格式化数据
				for (int i = 0; i < data.size(); i++) {
					String GoodsCategory = GoodsCateGorymap.get(StringUtils
							.trim((String) data.get(i).get("CATEGORY")));
					data.get(i).remove("CATEGORY");
					data.get(i).put("CATEGORY", GoodsCategory);
					String merid = data.get(i).get("MERID").toString();
					MerInfo merInfo = (MerInfo) merMap.get(merid);
					String merName = "";
					if(merInfo != null){
						merName = merInfo.getMerName();
					}
					//String string =((MerInfo) merMap.get(merid)).getMerName();
					data.get(i).put("MERNAME", merName);
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
	 * method name   : exportGoodsRelated 
	 * description   : 导出商品信息（包括商品银行和商品计费代码的一些字段）,方法已不用
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : LiZhen ,  2013-5-6  上午15:31:22
	 * @see          : 
	 * *******************************************
	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/export.do")
//	public ModelAndView exportGoodsRelated() {
//		//获取商品信息和商品银行信息
//		Map map = this.getParametersFromRequest(super.getHttpRequest());
//		String queryKey = (String) map.get("queryKey");
//		Map<String, Object> expMap = new HashMap();
//		if (queryKey != null) {
//			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
//			List<Map<String, Object>> data = service.query(queryKey, map);
//			ObjectUtil.trimData(data);
//			expMap.put("goodsInfo", data);
//			
//			//获取所有的商品计费代码，生成map，key为“商户号-商品号”，value为“计费代码1,计费代码2,,计费代码3”
//			//这些商品计费代码关联的商品状态均是【开通】，详见sql：code.allGoodsfeecodeManages
//			queryKey="code.allGoodsfeecodeManages";
//			List<Map<String, Object>> goodsFeeCodeList = service.query(queryKey, null);
//			ObjectUtil.trimData(goodsFeeCodeList);
//			Map<String,String> goodsFeeCodeMap=new HashMap<String, String>();
//			for (int i = 0; i < goodsFeeCodeList.size(); i++) {
//				Map m = goodsFeeCodeList.get(i);
//				Object merId=m.get("MERID");
//				Object goodsId=m.get("GOODSID");
//				Object serviceId=m.get("SERVICEID");
//				if(merId!=null && goodsId!=null && serviceId!=null){
//					String sIds=goodsFeeCodeMap.get(merId+"-"+goodsId);
//					if(sIds!=null){
//						goodsFeeCodeMap.put(merId+"-"+goodsId, sIds+","+serviceId);
//					}else{
//						goodsFeeCodeMap.put(merId+"-"+goodsId, serviceId.toString());
//					}
//				}
//			}
//			expMap.put("goodsFeeCode", goodsFeeCodeMap);
//			//获取商品分类信息
//			Map<String, String> goodsCategoryMap=optionService.getGoodsCategoryMap();
//			expMap.put("goodsCategoryMap", goodsCategoryMap);
//			//获取商品类型
//			List<GoodsTypeModel> goodsTypeList = optionService.getGoodsType();
//			Map<String, String> goodsTypeMap = new HashMap<String, String>();
//			for (int i = 0; i < goodsTypeList.size(); i++){
//				goodsTypeMap.put(goodsTypeList.get(i).getGoodsType().trim(),goodsTypeList.get(i).getDetail().trim());
//			}
//			expMap.put("goodsTypeMap", goodsTypeMap);
//		}
//		
//		return new ModelAndView("excelView", "excel", expMap);
//	}


	/**
	 * ******************************************** method name : export
	 * description : 导出商品信息方法   ,方法已不用
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : Administrator , 2012-8-16 下午02:01:20
	 * @see : *******************************************
	 */
//	@SuppressWarnings("unchecked")
//	
//	public ModelAndView export() {
//		Map<String, String> goodsCategory=optionService.getGoodsCategoryMap(); //获取商品分类信息
//		List<GoodsInfo> listGoodsInfos = new ArrayList<GoodsInfo>();
//		Map map = this.getParametersFromRequest(super.getHttpRequest());
//		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
//		String queryKey = (String) map.get("queryKey");
//		if (queryKey != null) {
//			IUniQueryService service = (IUniQueryService) SpringContextUtil
//					.getBean("uniQueryService");
//			List<Map<String, Object>> data = service.query(queryKey, map);
//			ObjectUtil.trimData(data);
//			List<GoodsTypeModel> goodsTypeList = optionService.getGoodsType();
//			Map<String, String> GoodsTypeMap = new HashMap<String, String>();
//			for (int i = 0; i < goodsTypeList.size(); i++){
//				GoodsTypeMap.put(goodsTypeList.get(i).getGoodsType().trim(),
//						goodsTypeList.get(i).getDetail().trim());
//			}
//			for (int i = 0; i < data.size(); i++) {
//				Map<String, Object> obj = data.get(i);
//				GoodsInfo goodsInfo = new GoodsInfo();
//				goodsInfo.setMerId(obj.get("merid") == null ? "" : obj.get("merid").toString());
//				String merId=goodsInfo.getMerId();
//				MerInfo merInfo = (MerInfo) merMap.get(merId);
//				String merName = "";
//				if(merInfo != null){
//					merName = merInfo.getMerName();
//				}				
//				goodsInfo.setMerName(merName); //设置商户名称				
//				goodsInfo.setGoodsId(obj.get("goodsId") == null ? "" : obj.get("goodsId").toString());
//				goodsInfo.setModTime((Timestamp) obj.get("modTime"));
//				goodsInfo.setGoodsName(obj.get("goodsName") == null ? "" : obj.get("goodsName").toString());
//				goodsInfo.setBusiType(obj.get("busiType") == null ? "" : obj.get("busiType").toString());
//				goodsInfo.setCusPhone(obj.get("cusPhone") == null ? "" : obj.get("cusPhone").toString());
//				goodsInfo.setGoodsDesc(obj.get("goodsDesc") == null ? "" : obj.get("goodsDesc").toString());
//				String goodsType = (String) obj.get("goodsType");
//				String goodsTypeName = GoodsTypeMap.get(goodsType);
//				goodsInfo.setGoodsType(goodsTypeName == null ? "" : goodsTypeName);
//				if(obj.get("interval")!=null){
//				    goodsInfo.setInterval(obj.get("interval").toString());
//				}
//				if(obj.get("conMode")!=null){
//					goodsInfo.setConMode(Integer.valueOf(obj.get("conMode").toString()));
//				}
//				if(obj.get("servMonth")!=null){
//					goodsInfo.setServMonth(obj.get("servMonth").toString());
//				}
//				goodsInfo.setInTime((Timestamp) obj.get("inTime"));
//				goodsInfo.setMtNum(Integer.valueOf(obj.get("mtNum") == null ? "" : obj.get("mtNum").toString()));
//				goodsInfo.setState(obj.get("state") == null ? "" : obj.get("state").toString());
//				goodsInfo.setPriceMode(Integer.valueOf(obj.get("priceMode") == null ? "" : obj.get("priceMode").toString()));
//				goodsInfo.setPushInf(Integer.valueOf(obj.get("pushInf") == null ? "" : obj.get("pushInf").toString()));
//				goodsInfo.setGoodsType(obj.get("goodsType") == null ? "" : obj.get("goodsType").toString());
//				String goodscategory="";
//				if(obj.get("category").toString()!=null &&obj.get("category").toString()!=""){
//					goodscategory=goodsCategory.get(obj.get("category").toString());
//				}
//				goodsInfo.setCategory(goodscategory);
//				goodsInfo.setServType(obj.get("servType") == null ? "" : obj.get("servType").toString());  //服务类型
//				listGoodsInfos.add(goodsInfo);
//			}
//		}
//		Map<String, Object> map2 = new HashMap();
//		map2.put("goodsInfo", listGoodsInfos);
//		return new ModelAndView("excelView", "excel", map2);
//	}

	/**
	 * ******************************************** method name : start
	 * description : 启用方法
	 * 
	 * @return : void
	 * @param : modified : Administrator , 2012-8-20 下午09:31:30
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/enable.do")
	public ModelAndView enable(String ID) {	
		User user=this.getUser();	//获取当前登录人
		String[] array = ID.split(",");
		//启用操作，目标状态为 ”2“
		String result="0";
		try{
		 result=goodsInfoService.enableAnddisable(array,user,"2");		 //操作结果
		}catch(Exception e) {
			e.printStackTrace();
			log.error("商户信息管理action中启用操作失败！");
		}
		 return new ModelAndView("jsonView", "ajax_json", result);

	}

	/**
	 * ******************************************** method name : stop
	 * description : 禁用操作
	 * 
	 * @return : void
	 * @param : @param ID modified : zhaojunbao , 2012-8-21 下午02:57:16
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/disable.do")
	public ModelAndView disable(String ID) {
		ID = ID.substring(0, ID.length() - 1);// 去掉最后的一个逗号
		String[] array = ID.split(",");
		User user=this.getUser();   //获取当前登录用户
	  //禁用操作，目标状态为  ”4“  
		String result="0";
		try{
		 result=goodsInfoService.enableAnddisable(array,user,"4");   //rusult 为操作结果显示
		}catch(Exception e){
			e.printStackTrace();
			log.error("商户信息管理action中禁用操作失败！");
		}
		 return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ******************************************** 
	 * method name : modCusPhone
	 * description : 修改客服电话方法
	 * 
	 * @return : void
	 * @param : modified : Administrator , 2012-8-21 下午16:52:30
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/modCusPhone.do")
	public ModelAndView batchMod(String ID, String cusPhone) {
		
		if(ID==null || cusPhone==null)
			return new ModelAndView("jsonView", "ajax_json", "");
		
		//TODO 需要先用正则验证ID和cusPhone的格式
		ID = ID.substring(0, ID.length() - 1);
		String[] array = ID.split(",");
		List<GoodsInfo> goodslist = new ArrayList<GoodsInfo>();
		for (int i = 0; i < array.length; i++) {
			GoodsInfo goodsInfo = new GoodsInfo();
			String[] MerGoods = array[i].split("-");
			goodsInfo.setMerId(MerGoods[0]);
			goodsInfo.setGoodsId(MerGoods[1]);
			goodsInfo.setModUser(getUser().getId());
			goodsInfo.setModTime(new Timestamp(System.currentTimeMillis()));
			goodslist.add(goodsInfo);
		}
		GoodsInfo goodsInfoNew = new GoodsInfo();
		goodsInfoNew.setCusPhone(cusPhone);
		String result="0";
		try {
			result = goodsInfoService.batchModGoods(goodslist, goodsInfoNew);
		} catch (Exception e) {
			log.error("修改客服电话失败", e);
		}
		
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ********************************************
	 * method name   : checkGoodsId 
	 * description   : 用户唯一性验证
	 * @return       : ModelAndView
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-8-29  下午07:54:15
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/checkgoodsid.do")
	public ModelAndView checkGoodsId(String merId, String goodsId) {
		String msg = "1"; // 默认结果 1表示不存在        0 表示存在
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", merId.trim() + "-" + goodsId.trim());
		mapWhere.put("dataType", "T_GOODS_INF");
		mapWhere.put("goodsId", goodsId.trim());
		mapWhere.put("merId", merId.trim());
		String result= auditService.getCheck(mapWhere); // 查询是否存在
		if (result != "1") {
			msg = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * ********************************************
	 * method name   : download 
	 * description   : 全网或小额导出（即文件下载）
	 * @return       : String
	 * @param        : @param fileName
	 * @param        : @param request
	 * @param        : @param response
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-5-20  下午01:57:52
	 * *
	 * @throws Exception ******************************************
	 */
	@RequestMapping(value = "/download")
	public ModelAndView download(String fileName, HttpServletRequest request,
			HttpServletResponse response){
		File file = null;
		String msg = "";
		try {
			file = goodsInfoService.getDownloadFile(fileName);
			log.info("获取下载文件成功");
		} catch (FileNotFoundException e) {
			msg = "系统找不到指定的文件，可能还未生成最新文件";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		} catch (Exception e) {
			msg = "获取下载文件失败";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		try {
			if (file != null && file.exists()) {
				fileName = file.getName();
				log.info("开始下载商品文件：" + fileName);
				response.reset();
				response.setContentType("application/x-msdownload;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
				InputStream inStream = new FileInputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
				// 循环取出流中的数据
				byte[] b = new byte[inStream.available()];
				while (inStream.read(b) != -1)
					bos.write(b, 0, b.length);
				bos.flush();
				bos.close();
				inStream.close();
				log.info("下载商品文件[" + fileName + "]成功");
			} else {
				log.info("下载失败，目录下没有文件");
			}
		} catch (Exception e) {
			msg = "下载文件失败,请稍后再试";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		return null;
	}
	
}
