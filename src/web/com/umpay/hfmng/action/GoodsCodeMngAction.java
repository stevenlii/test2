/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCodeAction.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-24
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.FeeCode;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.FeeCodeService;
import com.umpay.hfmng.service.GoodsFeeCodeService;
import com.umpay.hfmng.service.GoodsInfoService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  FeeCodeAction
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Controller
@RequestMapping("/goodscodemng")
public class GoodsCodeMngAction extends BaseAction{
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private FeeCodeService feeCodeService;
	@Autowired
	private  GoodsInfoService goodsInfoService;
	@Autowired
	private GoodsFeeCodeService goodsFeeCodeService;
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		String tree = ZTreeUtil.getGoodsCategoryWithAll();
		modeMap.addAttribute("zNodes", tree);
		//查找权限
//		String opts="001,002,007,008"; // 权限测试
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("goodscodemng");
		modeMap.addAttribute("opts", opts);
		return "goodscodemng/index";
	}
	/**
	 * ********************************************
	 * method name   : querycode 
	 * description   : 查询计费代码方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-24  下午10:05:57
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querycode")
	public ModelAndView querycode(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		Map<String,String>  feeCodeCategoryMap=optionService.getFeeCodeCategoryMap();
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).get("CATEGORY") != null) {
						String feeCodeCategory = feeCodeCategoryMap.get(data
								.get(i).get("CATEGORY").toString().trim());
						data.get(i).remove("CATEGORY");
						data.get(i).put("CATEGORY", feeCodeCategory);
					}
					if(data.get(i).get("USECOUNT") ==null ){
						data.get(i).remove("USECOUNT");
						data.get(i).put("USECOUNT", "0");
					}
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querygoodscode")
	public ModelAndView queryGoodsCode(){
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		//获取商品分类map
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map feeCode = data.get(i);
					MerInfo mer = (MerInfo) merMap.get(feeCode.get("MERID"));
					if(mer != null){
						feeCode.put("MERNAME", mer.getMerName());
					}
					feeCode.put("CATEGORY2", feeCode.get("CATEGORY")); //隐形添加一个分类信息，表示的是数字     --赵俊宝
					String category = (String) feeCode.get("CATEGORY");
					String categoryName = GoodsCateGorymap.get(category);
					feeCode.put("CATEGORY", categoryName);
					//计费代码详细信息弹出层
					StringBuffer sb = new StringBuffer();
					sb.append("代码：");
					String serviceId = (String) feeCode.get("SERVICEID");
					sb.append(serviceId).append("<br>");
					sb.append("名称：");
					String detail = (String) feeCode.get("DETAIL");
					sb.append(detail).append("<br>");
					sb.append("类型：");
					int servType = (Integer) feeCode.get("SERVTYPE");
					int codeServType = (Integer) feeCode.get("CODESERVTYPE");
					String codeServTypeName = codeServType == 2 ? "按次" : "包月";
					if(servType == codeServType){
						sb.append(codeServTypeName).append("<br>");
					}else{
						sb.append("<span style='color:#FF0000'>").append(codeServTypeName).append("</span><br/>");
					}
					sb.append("分类：");
					String codeCategory = (String) feeCode.get("CODECATEGORY");
					if(category.equals(codeCategory)){
						sb.append(categoryName).append("<br>");
					}else{
						String codeCategoryName = GoodsCateGorymap.get(codeCategory);
						codeCategoryName = codeCategoryName == null?"":codeCategoryName;
						sb.append("<span style='color:#FF0000'>").append(codeCategoryName).append("</span><br/>");
					}
					sb.append("金额：");
					Object amount = feeCode.get("AMOUNT");
					sb.append(amount).append("<br>");
					sb.append("使用：");
					Object useCount = feeCode.get("USECOUNT");
					useCount = useCount == null?"0":useCount;
					sb.append(useCount).append("次<br>");
					sb.append("状态：");
					int state = (Integer) feeCode.get("CODESTATE");
					String stateName = state == 2 ? "启用" : "禁用";
					sb.append(stateName);
					feeCode.put("ALT", sb);
					data.set(i, feeCode);
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
	@RequestMapping(value = "/feecodeDetail")
	public ModelAndView feecodeDetail(String serviceId){
		FeeCode feeCode = feeCodeService.load(serviceId);
		String result = "alert('"+feeCode.getServiceId()+"\t"+feeCode.getFeeType()+"');";
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		//获取商品分类map
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		Map<String, Object> map2 = new HashMap();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map feeCode = data.get(i);
				MerInfo mer = (MerInfo) merMap.get(feeCode.get("MERID"));
				if(mer != null){
					feeCode.put("MERNAME", mer.getMerName());
				}
				//商品分类分级显示,目前可最多显示3级
				String category = (String) feeCode.get("CATEGORY");
				if(category != null){
					String categoryName = "";
					for(int j = category.length()/2;j>=1;j--){
						categoryName = GoodsCateGorymap.get(category);
						feeCode.put("CATEGORY"+j, categoryName);
						category = category.substring(0, category.length()-2);
					}
				}
				int servType = (Integer) feeCode.get("SERVTYPE");
				feeCode.put("SERVTYPE", servType==2?"按次":"包月");
				int state = (Integer) feeCode.get("STATE");
				feeCode.put("STATE", state==2?"启用":"禁用");
				data.set(i, feeCode);
			}
			map2.put("feeCode", data);
		}
		return new ModelAndView("excelViewGoodsFeeCodeManage", "excel", map2);
	}
	/**
	 * ********************************************
	 * method name   : showDetail 
	 * description   : 商品计费代码管理页面商品详情显示
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-11-1  下午03:21:35
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
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
				goods.setServMonth(monGoodsInfo.getServMonth());
				goods.setConMode(monGoodsInfo.getConMode());
				goods.setInterval(monGoodsInfo.getInterval());
			}
		    }
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		String merid=goods.getMerId(); 
		MerInfo merInfo = (MerInfo) merMap.get(merid);
		String merName = "";
		if(merInfo != null){
			merName = merInfo.getMerName();
		}
		String categoryName = HfCacheUtil.getCache().getCategoryAbsoluteName(goods.getCategory());
		goods.setCategory(categoryName);
		goods.setMerName(merName); // 手动设置 修改页面展示商户名称
		goods.setPushInf(goods.getPushInf()+goods.getMtNum());//处理页面展示短信下发信息
		modeMap.addAttribute("goods", goods);
		return "goodscodemng/detail";
	}
	/**
	 * ********************************************
	 * method name   : showAdd 
	 * description   : 添加跳转页面
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @param modeMap
	 * @param        : @param flag  标示  如果是由“添加商品计费代码管理”跳转过来的 则说明右侧没有解绑操作，右侧的数据不能为空 定义标示为“n”
	 *                  如果是由“商品计费代码配置管理页面”跳转过的请求，有可能有解绑操作  定义标示为 “u”
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-11-8  下午02:36:15
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value="/showAdd")
	public String showAdd(String merId,String goodsId,String amount,String category,String servtype,String flag,ModelMap modeMap){
		modeMap.addAttribute("goodsId", goodsId); //  页面设置值
	   	modeMap.addAttribute("merId", merId);
		modeMap.addAttribute("category", category);
	   	modeMap.addAttribute("amount", amount);
	   	modeMap.addAttribute("servtype", servtype);
	   	modeMap.addAttribute("flag", flag);
		return "goodscodemng/add";
	}
	/**
	 * ********************************************
	 * method name   : querygoodscode 
	 * description   : add页面显示已经配置过的商品计费代码信息
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-11-8  下午02:46:11
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/querygoodsFeeCode")
	public ModelAndView querygoodscode(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
	    Map<String,String>  feeCodeCategoryMap=optionService.getFeeCodeCategoryMap();
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).get("CATEGORY") != null) {
						String feeCodeCategory = feeCodeCategoryMap.get(data
								.get(i).get("CATEGORY").toString().trim());
						data.get(i).remove("CATEGORY");
						data.get(i).put("CATEGORY", feeCodeCategory);
					}
					if(data.get(i).get("USECOUNT") ==null ){
						data.get(i).remove("USECOUNT");
						data.get(i).put("USECOUNT", "0");
					}
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
	 * method name   : add 
	 * description   : 实际保存方法
	 * @return       : ModelAndView
	 * @param        : @param feeCodes
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-30  下午08:56:01
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/add")
	public  ModelAndView add(String feeCodes,String merId,String goodsId){
		String insertRes="no"; //记录插入操作结果
		String updateRes="no"; // 记录更新结果
		User user=getUser();//获取当前登录
		Map<String, String>  mapWhere=new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		GoodsInfo goodsInfo=goodsInfoService.load(mapWhere);
		String  busiType="01";
		String  amount="";
		if(goodsInfo !=null){
			 busiType=goodsInfo.getBusiType();
			 amount=goodsInfo.getAmount();
		}
		FeeCode feeCode=new FeeCode();
		feeCode.setMerId(merId);
		feeCode.setGoodsId(goodsId);
		//根据商户号，商品号获取已经配置过得计费代码
		Map<String ,String> whereMap=new HashMap<String, String>();
		whereMap.put("merId", merId);
		whereMap.put("goodsId", goodsId);
		whereMap.put("state", "2");//查询状态为启用状态（state=2)的计费代码
		List<FeeCode> list=goodsFeeCodeService.getFeeCodesByMerIdGoodId(whereMap);
		List<String>  oldFeeCodesList=new ArrayList<String>(); //旧计费代码集合
		List<String>  nFeeCodesList = new ArrayList<String>(); //新计费代码集合
 		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
              String  serviceId=list.get(i).getServiceId();
            oldFeeCodesList.add(serviceId.trim());
			}
		}
		List<String> addList=new ArrayList<String>();  //需要新增的集合
		List<String> delList=new ArrayList<String>();   //需要删除的集合
		//old
		if (feeCodes != null && !feeCodes.equals("")) {
			feeCodes = feeCodes.substring(0, feeCodes.length() - 1); // 去掉前台传过来的最后一个逗号
			String[] nFeeCode = feeCodes.split(",");
			for (int j = 0; j < nFeeCode.length; j++) {
				nFeeCodesList.add(nFeeCode[j].trim()); // 放入list中
			}
			//计算新增的计费代码集合
			  for(int i=0;i<nFeeCodesList.size();i++){
				  if(!oldFeeCodesList.contains(nFeeCodesList.get(i))){
					  addList.add(nFeeCodesList.get(i));
					  FeeCode fCode=feeCodeService.load(nFeeCode[i]);
						if(busiType.equals("10")||busiType.equals("11")){
							if(!amount.equals(fCode.getAmount())){
								  JSONObject json=new JSONObject();
									try {
										json.put("res", "0");
										json.put("msg", "计费代码与商品金额不一致，请重新添加");
									} catch (JSONException e) {
									}
								return new ModelAndView("jsonView", "ajax_json", json.toString()); //金额不一致 返回失败操作
							}
						}
				  }
			  }
			//end
		}
 		//计算删除的计费代码集合
		for(int i=0;i<oldFeeCodesList.size();i++){
			if(!nFeeCodesList.contains(oldFeeCodesList.get(i))){
			  delList.add(oldFeeCodesList.get(i));
			}
		}
		//end
		  String res="0"; //判断操作结果
		  String msg="";
		  String nooper="";  //没有做操作提示
		  String insRes="no";
		  String upRes="no";
		  //添加操作 begin
		  if(addList!= null && addList.size()!=0){
			try {
				insRes =  goodsFeeCodeService.insertGoodsFeeCode(addList, feeCode, user);
				nooper="yes";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(insRes.equals("yes")){
				res="1";
				msg=msg+"新增计费代码"+addList.toString()+"成功！  ";
			}
			else {
				res="0";
				msg=msg+"新增计费代码"+addList.toString()+"失败！";
			}
		  }
		  // end 
		  if(delList != null && delList.size() !=0){
			  FeeCode updateFeeCode=new FeeCode();
			  updateFeeCode.setMerId(merId);
			  updateFeeCode.setGoodsId(goodsId);
			  updateFeeCode.setState("4"); //将状态设置为注销状态
			  try {
				upRes=  goodsFeeCodeService.updateGoodsFeeCode(delList,updateFeeCode,user);
				nooper="yes";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  if(upRes.equals("yes")){
				  res="1";
				  msg=msg+"解绑计费代码"+delList.toString()+"成功！";
			  }
			  else {
				  res="0";
				  msg=msg+"解绑计费代码"+delList.toString()+"失败！";
			  }
		  }
		  if(insertRes.equals("yes")&&updateRes.equals("yes")){
			  res="yes";
		  }
		  JSONObject json2=new JSONObject();
	 		try {
				json2.put("res", res);
				if(nooper.equals("")){
					json2.put("msg", "您没有做分配或者解绑计费代码操作！");
				}
				else{
					json2.put("msg", msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return new ModelAndView("jsonView", "ajax_json", json2.toString());
	}
}
