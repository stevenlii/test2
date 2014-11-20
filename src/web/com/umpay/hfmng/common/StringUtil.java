package com.umpay.hfmng.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

import com.umpay.hfmng.model.TaskRule;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: StringUtil
 * @Description: 系统框架公用方法，全部是静态同步方法
 * @author helin
 * @date 2013-1-11 下午4:12:08
 */
public class StringUtil {

	private static SimpleDateFormat sdf19 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf14 = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat sdf8 = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy-MM");
	private static SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMM");
	private static String[] weeks = { "", "日", "一", "二", "三", "四", "五", "六" };

	/**
	 * 验证参数是否完整,只要有一个为空或“”，就返回false
	 * 
	 * @param args
	 *            需要检查的变量，
	 * @return 验证结果
	 */
	public static boolean validateNull(String... args) {
		for (String arg : args) {
			if (arg == null || "".equals(arg)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证参数是否完整,只要有一个不为空或“”，就返回true
	 * 
	 * @param args
	 *            需要检查的变量，
	 * @return 验证结果
	 */
	public static boolean validateLeastNull(String... args) {
		for (String arg : args) {
			if (arg != null && !"".equals(arg.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将map中的元素转化成String，方便查看
	 * 
	 * @param map
	 *            需要转化的map
	 * @return 转化后的String
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public static String mapToString(Map<Object, Object> map) 
			throws UnsupportedEncodingException {

		String str = "";
		Iterator<Entry<Object, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it
					.next();
			String key = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			if (value instanceof List) {
				String temp = "[";
				List<String> list_temp = (List<String>) value;
				for (String s : list_temp) {
					temp += s + ",";
				}
				value = temp.substring(0, temp.length() - 1) + "]";
			}
			str += key + "="
					+ URLEncoder.encode(String.valueOf(value).trim(), "utf-8")
					+ "&";

		}
		return str.substring(0, str.length() - 1);
	}

	/**
	 * 把subMap里面的内容合并到map中
	 * 
	 * @param map
	 *            原始map
	 * @param subMap
	 *            要被合并的map
	 * @return
	 */
	public static Map<String, Object> mergeRetMap(Map<String, Object> map,
			Map<String, Object> subMap) {
		if (subMap != null) {
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			map.putAll(subMap);
		}
		return map;
	}

	/**
	 * 判断字符串是否为数字字符串。是则返回true，否则返回false。
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isDigitalString(String s) {
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		String ds = "0123456789";
		for (int i = 0; i < s.length(); i++) {
			if (ds.indexOf(s.charAt(i)) < 0)
				return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否为英文字符串。是则返回true，否则返回false。
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isLetterString(String s) {
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		String ds = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < s.length(); i++) {
			if (ds.indexOf(s.charAt(i)) < 0)
				return false;
		}
		return true;
	}

	/**
	 * 打印错误堆栈 ，将Exception中的内容转化为String，比Exception.toString更加详细。
	 * 
	 * @param Exception
	 * @return String
	 */
	public static String ExceptionToString(Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.toString());
		StackTraceElement[] stes = e.getStackTrace();
		for (StackTraceElement ste : stes) {
			sb.append("\n" + ste.toString());
		}
		return sb.toString();
	}

	/**
	 * restle体系中获取rest请求URI参数
	 * 
	 * @param para
	 *            从restURL中获取的参数
	 * @return 按照restlet规则转化后的参数
	 * @throws UnsupportedEncodingException
	 */
	public static String rewriteNull(String para)
			throws UnsupportedEncodingException {
		if (para != null) {
			para = URLDecoder.decode(para.trim(), "UTF-8");
			return "*".equals(para) ? null : para;
		} else
			return null;
	}

	/**
	 * 获取系统时间
	 * 
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String get19Time() {
		return sdf19.format(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 获取系统时间
	 * 
	 * @return String yyyyMMddHHmmss
	 */
	public static String get14Time() {
		return sdf14.format(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 获取系统时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getTSTime() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 获取系统时间
	 * 
	 * @return String yyyy-MM
	 */
	public static String get7Date() {
		return sdf7.format(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 获取系统时间
	 * 
	 * @return String yyyyMMdd
	 */
	public static String get8Date() {
		return sdf8.format(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 获取系统时间
	 * 
	 * @return String yyyy-MM-dd
	 */
	public static String get10Date() {
		return sdf10.format(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 获取系统时间
	 * 
	 * @return String yyyy年MM月dd日
	 */
	public static String get11Date() {
		return sdf11.format(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 时间格式转化
	 * 
	 * @param Timestamp
	 * @return String yyyyMMdd
	 */
	public static String time28TimeString(Timestamp time) throws Exception {
		return sdf8.format(time);
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyyMMddHHmmss
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String f14time219Time(String time) throws Exception {
		return sdf19.format(sdf14.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM-dd
	 * @return String yyyyMMddHHmmss
	 */
	public static String f10time214Time(String time) throws Exception {
		return sdf14.format(sdf10.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyyMMdd
	 * @return String yyyy-MM-dd
	 */
	public static String f8time210Time(String time) throws Exception {
		return sdf10.format(sdf8.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @return Timestamp
	 */
	public static Timestamp string192Time(String time) throws ParseException {
		return new Timestamp(sdf19.parse(time).getTime());
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @return String yyyyMMddHHmmss
	 */
	public static String f19time214Time(String time) throws Exception {
		return sdf14.format(sdf19.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @return String yyyyMM
	 */
	public static String f19time26Time(String time) throws Exception {
		return sdf6.format(sdf19.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param Timestamp
	 * @return String yyyyMMddHHmmss
	 */
	public static String ftime214Time(Timestamp time) {
		return sdf14.format(time);
	}

	/**
	 * 时间格式转化
	 * 
	 * @param Date
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String ftime219Time(Date time) {
		return sdf19.format(time);
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyyMMddHHmmss
	 * @return Timestamp
	 */
	public static Timestamp string142Time(String time) throws ParseException {
		return new Timestamp(sdf14.parse(time).getTime());
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @return String yyyyMMdd
	 */
	public static String f19time28Time(String time) throws Exception {
		return sdf8.format(sdf19.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyyMM
	 * @return String yyyy-MM
	 */
	public static String f6time27Time(String time) throws Exception {
		return sdf7.format(sdf6.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM
	 * @return String yyyyMM
	 */
	public static String f7time26Time(String time) throws Exception {
		return sdf6.format(sdf7.parse(time));
	}

	/**
	 * 时间格式转化
	 * 
	 * @param String
	 *            yyyy-MM-dd
	 * @return String yyyyMMdd
	 */
	public static String f10time28Time(String time) throws Exception {
		return sdf8.format(sdf10.parse(time));
	}

	/**
	 * 得到与当前时间相隔前N天的时间
	 * 
	 * @param int 正数表示获得当前时间之后的天数，负数表示或的当前时间之前的天数。
	 * @return String yyyyMMddHHmmss
	 */
	public static String get14Time_SpaceXTime(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day); // 得到前N天
		return sdf14.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与当前时间相隔前N天的时间
	 * 
	 * @param int 正数表示获得当前时间之后的天数，负数表示或的当前时间之前的天数。
	 * @return String yyyy-MM-dd
	 */
	public static String get10Date_SpaceXDate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day); // 得到前N天
		return sdf10.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与当前时间相隔前N天的时间
	 * 
	 * @param int 正数表示获得当前时间之后的天数，负数表示或的当前时间之前的天数。
	 * @return Timestamp
	 */
	public static Timestamp getTime_SpaceXDate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day);// 得到前N天
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N天的时间
	 * 
	 * @param Timestamp
	 * @param int 正数表示获得当前时间之后的天数，负数表示或的当前时间之前的天数。
	 * @return String yyyy-MM-dd
	 */
	public static String get10Date_SpaceXDate(Timestamp time19, int day)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time19);
		calendar.add(Calendar.DATE, day); // 得到前N天
		return sdf10.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N天的时间
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的天数，负数表示或的当前时间之前的天数。
	 * @return String yyyy-MM-dd
	 */
	public static String get10Date_SpaceXDate(String time19, int day)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.DATE, day); // 得到前N天
		return sdf10.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N天的时间
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的天数，负数表示或的当前时间之前的天数。
	 * @return String yyyyMMdd
	 */
	public static String get8Date_SpaceXDay(String time19, int mon)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.DATE, mon); // 得到前N天
		return sdf8.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与当前月相隔前N月的月份
	 * 
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyyy-MM
	 */
	public static String get7Date_SpaceXMon(int mon) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, mon); // 得到前N月
		return sdf7.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N月的月份
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyyy-MM
	 */
	public static String get7Date_SpaceXMon(String time19, int mon)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.MONTH, mon); // 得到前N月
		return sdf7.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N月的月份
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyMM
	 */
	public static String get4Date_SpaceXMon(String time19, int mon)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.MONTH, mon); // 得到前N月
		return sdf6.format(calendar.getTimeInMillis()).substring(2, 6);
	}

	/**
	 * 得到当前时间相隔N小时的时间
	 * 
	 * @param String
	 *            hour
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String get19Time_SpaceXHour(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, hour);
		return sdf19.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔N小时的时间
	 * 
	 * @author helin created in 2012-4-25 上午11:40:41
	 * @param time
	 * @param hour
	 * @return
	 */
	public static String get19Time_SpaceXHour(Timestamp time, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.HOUR, hour);
		return sdf19.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N分钟的时间
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的分钟数，负数表示或的当前时间之前的分钟数。
	 * @return Timestamp
	 */
	public static Timestamp getTSTime_SpaceXMin(String time19, int min)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.MINUTE, min); // 得到前N分钟
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 得到当前时间相隔前N分钟的时间
	 * 
	 * @param int 正数表示获得当前时间之后的分钟数，负数表示或的当前时间之前的分钟数。
	 * @return Timestamp
	 */
	public static String getSringTime_SpaceXMin(int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, min); // 得到前N分钟
		return sdf19.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到当前时间相隔前N分钟的时间
	 * 
	 * @param int 正数表示获得当前时间之后的分钟数，负数表示或的当前时间之前的分钟数。
	 * @return Timestamp
	 */
	public static Timestamp getTSTime_SpaceXMin(int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, min); // 得到前N分钟
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N分钟的时间
	 * 
	 * @param Timestamp
	 *            oldTsp
	 * @param int 正数表示获得当前时间之后的分钟数，负数表示或的当前时间之前的分钟数。
	 * @return Timestamp
	 */
	public static Timestamp getTSTime_SpaceXMin(Timestamp oldTsp, int min)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldTsp);
		calendar.add(Calendar.MINUTE, min); // 得到前N分钟
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N月的最后一天
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyyy-MM-dd
	 */
	public static String get10Date_SpaceXMonEnd(String time19, int mon)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.MONTH, mon + 1);// 减N个月+1月
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		calendar.add(Calendar.DATE, -1);// 日期回滚一天，也就是前月最后一天
		return sdf10.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N月的最后一天
	 * 
	 * @param String
	 *            yyyy-MM-dd HH:mm:ss
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyyyMMdd
	 */
	public static String get8Date_SpaceXMonEnd(String time19, int mon)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf19.parse(time19));
		calendar.add(Calendar.MONTH, mon + 1);// 减N个月+1月
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		calendar.add(Calendar.DATE, -1);// 日期回滚一天，也就是前月最后一天
		return sdf8.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N月的第一天
	 * 
	 * @param String
	 *            yyyyMM
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyyy年MM月dd日
	 */
	public static String get11Date_SpaceXMonStart(String time6, int mon) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf6.parse(time6));
		calendar.add(Calendar.MONTH, mon);// 加N个月
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		return sdf11.format(calendar.getTimeInMillis());
	}

	/**
	 * 得到与传入时间相隔前N月的最后一天
	 * 
	 * @param String
	 *            yyyyMM
	 * @param int 正数表示获得当前时间之后的月数，负数表示或的当前时间之前的月数。
	 * @return String yyyy年MM月dd日
	 */
	public static String get11Date_SpaceXMonEnd(String time6, int mon) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf6.parse(time6));
		calendar.add(Calendar.MONTH, mon + 1);// 减N个月+1月
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		calendar.add(Calendar.DATE, -1);// 日期回滚一天，也就是前月最后一天
		return sdf11.format(calendar.getTimeInMillis());
	}

	/**
	 * 将json对账转化为beanClass
	 * 
	 * @param String
	 *            jsonString
	 * @param Class
	 *            beanClass
	 * @return Object beanClass
	 */
	public static Object jsonString2Bean(String jsonString, Class<?> beanClass) {
		return JSONObject.toBean(JSONObject.fromObject(jsonString), beanClass);
	}

	/**
	 * @param String
	 *            arrayJsonString
	 * @param Class
	 *            beanClass
	 * @return List<Object> beanClass
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> jsonString2List(String arrayJsonString,
			Class<?> beanClass) {
		List<JSONObject> jsonList = (List<JSONObject>) JSONArray
				.fromObject(arrayJsonString);
		List<Object> beanList = new ArrayList<Object>();
		for (JSONObject jsb : jsonList) {
			beanList.add(JSONObject.toBean(jsb, beanClass));
		}
		return beanList;
	}

	/**
	 * 将字符流转化成字符串
	 * 
	 * @param InputStream
	 * @return String
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString("UTF-8");
	}

	/**
	 * 将代码转成中文
	 * 
	 * @author helin created in 2012-6-8 上午8:23:31
	 * @param map
	 * @param str
	 * @return 形如“Labke(Code)”
	 * @throws PatternSyntaxException
	 */
	public static String code2Lable(Map<String, String> map, String str)
			throws PatternSyntaxException {
		return (null == map.get(str) ? "" : map.get(str)) + "(" + str + ")";
	}

	/**
	 * @Title: countNextRunTime
	 * @Description: 根据定时规则计算定时任务执行首次执行时间。
	 * @param taskRule
	 * @param nextRunTime
	 *            用于返回计算的时间。
	 * @return String 返回规则描述。
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午4:29:23
	 */
	public static Map<String, Object> countFirstRunTime(TaskRule taskRule)
			throws Exception {
		Map<String, Object> returnValues = new HashMap<String, Object>();
		String ruleDesc = "";
		Calendar calendar = Calendar.getInstance();
		Calendar c_nextRunTime = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM月dd日  HH:mm分");
		SimpleDateFormat sdf16 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String nowTime = sdf.format(calendar.getTime());
		// 首先判断定时规则类型：cyc-周期性,space-定间隔,onlyone-仅一次
		if ("cyc".equals(taskRule.getRuleType())) {
			// 按照设定周期进行（逻辑最复杂）
			// 再判断周期类型：y-年,m-月,w-周,d-日,h-小时
			String allCycTime = "";
			if ("y".equals(taskRule.getCycType())) {
				// 首先格式化，然后和当前时间比较，如果小于当前时间则加一年
				allCycTime = nowTime.substring(0, 5)
						+ taskRule.getCycTime().trim();
				c_nextRunTime.setTimeInMillis(sdf.parse(allCycTime).getTime());
				while (c_nextRunTime.before(calendar)) {
					c_nextRunTime.add(Calendar.YEAR, 1);
				}
				ruleDesc = "每年" + taskRule.getCycTime();
			} else if ("m".equals(taskRule.getCycType())) {
				if ("Y".equals(taskRule.getIsLastDay())) {
					// 首先格式化。
					allCycTime = nowTime.substring(0, 13)
							+ taskRule.getCycTime().trim();
					c_nextRunTime.setTimeInMillis(sdf.parse(allCycTime)
							.getTime());
					// 如果是最后一天，就计算最后一天，加一天，如果月数变化了，就表示是最后一天了。
					int oldMonth = c_nextRunTime.get(Calendar.MONTH);
					do {
						c_nextRunTime.add(Calendar.DATE, 1);
					} while (oldMonth == c_nextRunTime.get(Calendar.MONTH));
					c_nextRunTime.add(Calendar.DATE, -1);
					// 再当前时间比较，如果小于当前时间，则加一月。
					while (c_nextRunTime.before(calendar)) {
						c_nextRunTime.add(Calendar.MONTH, 1);
					}
					// 然后再取最后一天
					do {
						c_nextRunTime.add(Calendar.DATE, 1);
					} while (oldMonth == c_nextRunTime.get(Calendar.MONTH));
					c_nextRunTime.add(Calendar.DATE, -1);
					ruleDesc = "每月最后一天" + taskRule.getCycTime();
				} else {
					// 首先格式化。
					allCycTime = nowTime.substring(0, 8)
							+ taskRule.getCycTime().trim();
					c_nextRunTime.setTimeInMillis(sdf.parse(allCycTime)
							.getTime());
					// 再当前时间比较，如果小于当前时间，则加一月。
					while (c_nextRunTime.before(calendar)) {
						c_nextRunTime.add(Calendar.MONTH, 1);
					}
					ruleDesc = "每月" + taskRule.getCycTime();
				}
			} else if ("w".equals(taskRule.getCycType())) {
				// 首先格式化，然后和当前时间比较要求的星期数和当前的星期数是否相同，如果不同则加一天，直到相同。
				allCycTime = nowTime.substring(0, 13)
						+ taskRule.getCycTime().trim();
				c_nextRunTime.setTimeInMillis(sdf.parse(allCycTime).getTime());
				while (taskRule.getWeeks() != c_nextRunTime
						.get(Calendar.DAY_OF_WEEK)) {
					c_nextRunTime.add(Calendar.DATE, 1);
				}
				// 再当前时间比较，如果小于当前时间，则加一周。
				while (c_nextRunTime.before(calendar)) {
					c_nextRunTime.add(Calendar.WEDNESDAY, 1);
				}
				ruleDesc = "每周星期" + weeks[taskRule.getWeeks()]
						+ taskRule.getCycTime();
			} else if ("d".equals(taskRule.getCycType())) {
				// 首先格式化，然后和当前时间比较，如果小于当前时间则加一天
				allCycTime = nowTime.substring(0, 13)
						+ taskRule.getCycTime().trim();
				c_nextRunTime.setTimeInMillis(sdf.parse(allCycTime).getTime());
				while (c_nextRunTime.before(calendar)) {
					c_nextRunTime.add(Calendar.DATE, 1);
				}
				ruleDesc = "每天" + taskRule.getCycTime();
			} else if ("h".equals(taskRule.getCycType())) {
				// 首先格式化，然后和当前时间比较，如果小于当前时间则加一小时
				allCycTime = nowTime.substring(0, 16)
						+ taskRule.getCycTime().trim();
				c_nextRunTime.setTimeInMillis(sdf.parse(allCycTime).getTime());
				while (c_nextRunTime.before(calendar)) {
					c_nextRunTime.add(Calendar.HOUR, 1);
				}
				ruleDesc = "每小时" + taskRule.getCycTime();
			}

		} else if ("space".equals(taskRule.getRuleType())) {
			// 固定间隔,做时间加法，也比较简单
			c_nextRunTime.add(Calendar.MINUTE, taskRule.getSpaceTime());
			ruleDesc = "每间隔" + taskRule.getSpaceTime() + "分钟";
		} else if ("onlyone".equals(taskRule.getRuleType())) {
			// 仅执行一次，逻辑最简单。
			c_nextRunTime.setTimeInMillis(sdf16.parse(taskRule.getRunTime())
					.getTime());
			ruleDesc = "仅在" + taskRule.getRunTime();
		}

		returnValues.put("ruleDesc", ruleDesc + "执行");
		returnValues.put("nextRunTime", new Timestamp(c_nextRunTime
				.getTimeInMillis()));
		return returnValues;
	}

	/**
	 * @Title: countNextRunTime
	 * @Description: 根据传入的上次执行时间和定时规则计算定时任务执行的下次时间，当前时间可以不传，默认当前时间。
	 * @param now
	 * @param taskRule
	 * @return Timestamp
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午4:29:23
	 */
	public static Timestamp countNextRunTime(Timestamp oldNextRunTime,
			TaskRule taskRule) throws Exception, ParseException {
		Calendar c_now = Calendar.getInstance();
		Calendar c_nextRunTime = Calendar.getInstance();
		if (null != oldNextRunTime)
			c_nextRunTime.setTime(oldNextRunTime);
		// 首先判断定时规则类型：cyc-周期性,space-定间隔,onlyone-仅一次
		if ("cyc".equals(taskRule.getRuleType())) {
			// 按照设定周期进行（逻辑最复杂）
			// 再判断周期类型：y-年,m-月,w-周,d-日,h-小时
			if ("y".equals(taskRule.getCycType())) {
				while (c_nextRunTime.before(c_now)) {
					c_nextRunTime.add(Calendar.YEAR, 1);
				}
			} else if ("m".equals(taskRule.getCycType())) {
				if ("Y".equals(taskRule.getIsLastDay())) {
					// 每月最后天需要单独处理 先加一个月，再加一天，如果月数变化了，就表示是最后一天了。
					c_nextRunTime.add(Calendar.MONTH, 1);
					int oldMonth = c_nextRunTime.get(Calendar.MONTH);
					do {
						c_nextRunTime.add(Calendar.DATE, 1);
					} while (oldMonth == c_nextRunTime.get(Calendar.MONTH));
					c_nextRunTime.add(Calendar.DATE, -1);
				} else {
					while (c_nextRunTime.before(c_now)) {
						c_nextRunTime.add(Calendar.MONTH, 1);
					}
				}
			} else if ("w".equals(taskRule.getCycType())) {
				while (c_nextRunTime.before(c_now)) {
					c_nextRunTime.add(Calendar.WEDNESDAY, 1);
				}
			} else if ("d".equals(taskRule.getCycType())) {
				while (c_nextRunTime.before(c_now)) {
					c_nextRunTime.add(Calendar.DATE, 1);
				}
			} else if ("h".equals(taskRule.getCycType())) {
				while (c_nextRunTime.before(c_now)) {
					c_nextRunTime.add(Calendar.HOUR, 1);
				}
			}

		} else if ("space".equals(taskRule.getRuleType())) {
			// 固定间隔,做时间加法，也比较简单
			while (c_nextRunTime.before(c_now)) {
				c_nextRunTime.add(Calendar.MINUTE, taskRule.getSpaceTime());
			}
		} else if ("onlyone".equals(taskRule.getRuleType())) {
			// 仅执行一次，逻辑最简单。
			c_nextRunTime.setTimeInMillis(Long.valueOf("29379513599000"));
		}

		return new Timestamp(c_nextRunTime.getTimeInMillis());
	}

	/**
	 * @Title: bigDecimalToString
	 * @Description: bigDecimal转化成字符串
	 * @param
	 * @param bigDecimal
	 * @return
	 * @author wanyong
	 * @date 2013-1-23 上午11:16:51
	 */
	public static String bigDecimalToString(BigDecimal bigDecimal) {
		return Float.toString(bigDecimal.floatValue());
	}

	/**
	 * @Title: get10Date_SpaceXDayEnd
	 * @Description: 根据开始日期和结束日期计算出两个日期中日期差（每天一个计算单位），返回日期集合
	 * @param
	 * @param startDate
	 * @param endDate
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 上午10:59:11
	 */
	public static List<String> get10Date_SpaceXDayEnd(String startDate,
			String endDate) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(startDate);
		long msec = 1000 * 60 * 60 * 24;
		long day = (sdf10.parse(endDate).getTime() - sdf10.parse(startDate)
				.getTime())
				/ msec;
		for (int i = 0; i < day; i++) {
			list.add(sdf10.format(sdf10.parse(startDate).getTime() + (i + 1)
					* msec));
		}

		return list;
	}

	/**
	 * @Title: get10Date_SpaceXMonthEnd
	 * @Description: 计算两个日期的时间间隔（每月一个计算单位），返回日期集合
	 * @param
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 * @author wanyong
	 * @date 2013-3-11 下午04:17:48
	 */
	public static List<String> get10Date_SpaceXMonthEnd(String startDate,
			String endDate) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(startDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf7.parse(endDate));
		// 获得结束时间的年份、月份
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		calendar.setTime(sdf7.parse(startDate));
		// 获得开始时间的年份、月份
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		int month = 12 * (year2 - year1) + month2 - month1;
		for (int i = 0; i < month; i++) {
			calendar.add(Calendar.MONTH, 1);
			list.add(sdf7.format(calendar.getTime()));
		}
		return list;
	}
}
