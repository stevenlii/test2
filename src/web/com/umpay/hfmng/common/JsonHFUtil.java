package com.umpay.hfmng.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import com.umpay.hfmng.model.MerGrade;

public class JsonHFUtil {

	/**
	 * @Title: getObjFromJsonArrStr1
	 * @Description: Object中有timestamp时间类型，必须使用该方法把jsonstr转化成Object
	 * @param
	 * @param jsonArrStr
	 * @param clazz
	 * @return
	 * @author wanyong
	 * @date 2013-1-18 上午10:42:05
	 */
	public static Object getObjFromJsonArrStr1(String jsonArrStr, Class clazz) {
		String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
		JSONUtils.getMorpherRegistry().registerMorpher(
				new TimestampMorpher(formats));
		JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(jsonArrStr);
		return JSONObject.toBean(jsonObj, clazz);
	}

	public static Object getObjFromJsonArrStr(String source, Class beanClass) {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(source);
		return JSONObject.toBean(jsonObject, beanClass);
	}

	/**
	 * 由字符串反序列化成实体类 针对的是一个实体，此实体中的属性包括自定义的类型，如Teacher类型，或者List<Teacher>类型
	 * 
	 * @param jsonArrStr
	 * @param clazz
	 * @param classMap
	 * @return
	 */
	public static Object getObjFromJsonArrStr(String jsonArrStr, Class clazz,
			Map classMap) {
		JSONObject jsonObj = JSONObject.fromObject(jsonArrStr);
		return JSONObject.toBean(jsonObj, clazz, classMap);
	}

	/**
	 * 将string转换成listBean
	 * 
	 * @param jsonArrStr
	 *            需要反序列化的字符串
	 * @param clazz
	 *            被反序列化之后的类
	 * @return 实体list
	 */
	@SuppressWarnings("unchecked")
	public static List getListFromJsonArrStr(String jsonArrStr, Class clazz) {
		JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);
		String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
		JSONUtils.getMorpherRegistry().registerMorpher(
				new TimestampMorpher(formats));
		List list = new ArrayList();
		for (int i = 0; i < jsonArr.size(); i++) {
			list.add(JSONObject.toBean(jsonArr.getJSONObject(i), clazz));
		}
		return list;
	}
	/**
	 * ********************************************
	 * method name   : getMapFromJsonArrStr 
	 * description   : 将Json转为Map
	 * @return       : Map
	 * @param        : @param jsonArrStr
	 * @param        : @return
	 * modified      : lz ,  2013-2-25  下午09:30:29
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	public static Map getMapFromJsonArrStr(String jsonArrStr) {
		JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);
		Map map = new HashMap();
		for (int i = 0; i < jsonArr.size(); i++) {
			map.putAll((Map)JSONObject.toBean(jsonArr.getJSONObject(i), Map.class));
		}
		return map;
	}

	/**
	 * 序列化操作，无论是单个的对象，还是list，抑或是list中的属性仍包含list，都可以直接序列化成String类型
	 * 
	 * @param obj
	 *            需要被序列化的对象
	 * @return 序列化之后的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String getJsonArrStrFrom(Object obj) {
		// 返回結果
		String jsonStr = null;
		// 判空
		if (obj == null) {
			return "{}";
		}
		// Json配置
		JsonConfig jsonCfg = new JsonConfig();
		// 注册日期处理器
		jsonCfg.registerJsonValueProcessor(java.util.Date.class,
				new JsonTimeValueProcessorImpl());
		jsonCfg.registerJsonValueProcessor(Timestamp.class,
				new JsonTimeValueProcessorImpl());
		// 判断是否是list
		if (obj instanceof Collection || obj instanceof Object[]) {
			jsonStr = JSONArray.fromObject(obj, jsonCfg).toString();
		} else {
			jsonStr = JSONObject.fromObject(obj, jsonCfg).toString();
		}
		return jsonStr;
	}
	/**
	 * ********************************************
	 * method name   : getJson 
	 * description   : 对象转为json字符串。增加了Date、Timestamp、BigDecimal、Integer和Long的处理器
	 * 					BigDecimal、Integer和Long型对象为null时，将被转空串，这样可以保证该json被转为
	 * 					对象时上述三种类型仍然为null。
	 * @return       : String
	 * @param        : @param obj
	 * @param        : @return
	 * modified      : lz ,  2013-3-19  上午10:18:00
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	public static String getJson(Object obj) {
		// 返回結果
		String jsonStr = null;
		// 判空
		if (obj == null) {
			return "{}";
		}
		// Json配置
		JsonConfig jsonCfg = new JsonConfig();
		// 注册日期处理器
		jsonCfg.registerJsonValueProcessor(java.util.Date.class,
				new JsonTimeValueProcessorImpl());
		jsonCfg.registerJsonValueProcessor(Timestamp.class,
				new JsonTimeValueProcessorImpl());
		// 注册数字处理器BigDecimal、Integer和Long型属性为null时，转换后也为null
		jsonCfg.registerJsonValueProcessor(java.math.BigDecimal.class,
				new JsonNumberValueProcessorImpl());
		jsonCfg.registerJsonValueProcessor(java.lang.Integer.class,
				new JsonNumberValueProcessorImpl());
		jsonCfg.registerJsonValueProcessor(java.lang.Long.class,
				new JsonNumberValueProcessorImpl());
		// 判断是否是list
		if (obj instanceof Collection || obj instanceof Object[]) {
			jsonStr = JSONArray.fromObject(obj, jsonCfg).toString();
		} else {
			jsonStr = JSONObject.fromObject(obj, jsonCfg).toString();
		}
		return jsonStr;
	}

	public static void main(String[] args) {
		MerGrade m=new MerGrade();
		m.setMerId("1212");
		m.setBreachIndex(new BigDecimal("1.23"));
		m.setComplaintCount(null);
		m.setComplaintIndex(null);
		m.setCooperateIndex(new BigDecimal(0));
		m.setMarketingIndex(new BigDecimal(Double.toString(2.001)));
		m.setInTime(new Timestamp(System.currentTimeMillis()));
		String mstr = getJson(m);
		System.out.println(mstr); 
		
		String merGradeStr = "[{\"turnover\":27236900,\"breachIndex\":0,\"modUser\":\"00000000000000000000000000000004\",\"complaintCount\":-99,\"turnoverIndex\":18,\"tradingCount\":32635,\"total\":50.5,\"cooperateIndex\":5,\"supportIndex\":0,\"inTime\":null,\"month\":\"2013-01\",\"marketingIndex\":0,\"modTime\":null,\"upgradeIndex\":0,\"riseRateIndex\":10.5,\"reduceSum\":-99,\"sysStabIndex\":12,\"modLock\":0,\"merName\":\"体彩\",\"merId\":\"2880\",\"reduceRate\":-99,\"falseTradeIndex\":0,\"complaintIndex\":5,\"state\":\"2\",\"riseRate\":0.47,\"lastTurnover\":-99},{\"turnover\":0,\"breachIndex\":0,\"modUser\":\"00000000000000000000000000000004\",\"complaintCount\":0,\"turnoverIndex\":18,\"tradingCount\":0,\"total\":33.5,\"cooperateIndex\":5,\"supportIndex\":0,\"inTime\":null,\"month\":\"2013-01\",\"marketingIndex\":0,\"modTime\":null,\"upgradeIndex\":0,\"riseRateIndex\":10.5,\"reduceSum\":0,\"sysStabIndex\":0,\"modLock\":1,\"merName\":\"\",\"merId\":\"2880\",\"reduceRate\":0,\"falseTradeIndex\":0,\"complaintIndex\":0,\"state\":\"\",\"riseRate\":0,\"lastTurnover\":0}]";
		merGradeStr="{\"breachIndex\":1.23,\"turnover\":0,\"modUser\":\"\",\"complaintCount\":0,\"tradingCount\":0,\"turnoverIndex\":0,\"total\":0,\"cooperateIndex\":0,\"supportIndex\":0,\"inTime\":null,\"modTime\":null,\"marketingIndex\":0,\"month\":\"\",\"upgradeIndex\":0,\"riseRateIndex\":0,\"reduceSum\":0,\"sysStabIndex\":0,\"modLock\":0,\"merName\":\"\",\"merId\":\"1212\",\"reduceRate\":0,\"falseTradeIndex\":0,\"complaintIndex\":0,\"state\":\"\",\"riseRate\":0,\"lastTurnover\":0}";
		Object merGrade = getObjFromJsonArrStr1(mstr, MerGrade.class);
		System.out.println("" + merGrade);
		
		List mlist=new ArrayList<MerGrade>();
		mlist.add(m);
		String mliststr = getJson(mlist);
		//mliststr="[{\"turnover\":27236900,\"breachIndex\":0,\"modUser\":\"00000000000000000000000000000004\",\"complaintCount\":-99,\"turnoverIndex\":18,\"tradingCount\":32635,\"total\":50.5,\"cooperateIndex\":5,\"supportIndex\":0,\"inTime\":\"2013-02-22 15:16:55\",\"month\":\"2013-01\",\"marketingIndex\":0,\"modTime\":\"2013-02-26 18:09:39\",\"upgradeIndex\":0,\"riseRateIndex\":10.5,\"reduceSum\":-99,\"sysStabIndex\":12,\"modLock\":0,\"merName\":\"体彩\",\"merId\":\"2880\",\"reduceRate\":-99,\"falseTradeIndex\":0,\"complaintIndex\":5,\"state\":\"2\",\"riseRate\":0.47,\"lastTurnover\":-99},{\"turnover\":null,\"breachIndex\":0,\"modUser\":\"00000000000000000000000000000004\",\"complaintCount\":null,\"turnoverIndex\":18,\"tradingCount\":null,\"total\":0,\"cooperateIndex\":null,\"supportIndex\":0,\"inTime\":null,\"month\":\"2013-01\",\"marketingIndex\":null,\"modTime\":null,\"upgradeIndex\":3.4,\"riseRateIndex\":null,\"reduceSum\":null,\"sysStabIndex\":null,\"modLock\":1,\"merName\":\"\",\"merId\":\"2880\",\"reduceRate\":null,\"falseTradeIndex\":null,\"complaintIndex\":null,\"state\":\"\",\"riseRate\":null,\"lastTurnover\":null}]";
		List mlist1=getListFromJsonArrStr(mliststr,MerGrade.class);
		System.out.println(mlist1.get(0));
//		DemoInfo info = new DemoInfo();
//		info.setMerId("9996");
//		info.setMerName("测试商户\"9996");
//		String str = getJsonArrStrFrom(info);
//		System.out.println(str);
//		DemoInfo info2 = (DemoInfo) getObjFromJsonArrStr(str, DemoInfo.class);
//		System.out.println(info2.getMerId() + ":" + info2.getMerName());
//
//		Map map = (Map) getObjFromJsonArrStr(str, HashMap.class);
//		System.out.println(map.get("merId"));
//
//		List ls = new ArrayList();
//		ls.add(info);
//		ls.add(info);
//		String str2 = getJsonArrStrFrom(ls);
//		System.out.println(str2);
//
//		List ls2 = getListFromJsonArrStr(str2, DemoInfo.class);
//		System.out.println(ls2.size());
//
//		// String str3 = "[{\"001\":\"软件\"},{\"002\":\"音乐\"}]";
//		String str3 = "[{'001':'软件'},{'002':'音乐'}]";
//		System.out.println(str3);
//		List ls3 = getListFromJsonArrStr(str3, Map.class);
//		System.out.println("" + ls3.get(0));
//		System.out.println("" + ls3.get(1));
//
//		Map map2 = new HashMap();
//		map2.put("result", "ok");
//		String jsonStr = getJsonArrStrFrom(map2);
//		System.out.println(jsonStr);
//		Map map3 = (Map) getObjFromJsonArrStr(jsonStr, HashMap.class);
//		System.out.println(map3);

	}

}
