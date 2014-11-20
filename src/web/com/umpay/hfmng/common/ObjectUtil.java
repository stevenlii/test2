package com.umpay.hfmng.common;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectUtil {

	/**
	 * 去掉空格。str为null时返回空串，不为null则去掉首尾的空格
	 */
	public static String trim(String str){
		if(str == null){
			return "";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * Object转为String。obj为null时返回null，不为null则返回obj.toString()
	 */
	public static String toString(Object obj){
		if(obj == null){
			return null;
		}else{
			return obj.toString();
		}
	}
	
	/**
	 * Object转为String。obj为null时返回空串，不为null则返回obj.toString()
	 */
	public static String toStringNoNull(Object obj){
		if(obj == null){
			return "";
		}else{
			return obj.toString();
		}
	}

	/**
	 * 去掉Map数据中的空格，解决DB2 CHAR型字段检索的问题
	 */
	public static void trimData(List<Map<String, Object>> data){
		for(Map<String, Object> map:data){
			Set<String> keys = map.keySet();
			for(String key:keys){
				Object obj = map.get(key);
				if(obj instanceof String){
					String tmp = ObjectUtil.trim((String)obj);
					map.put(key, tmp);
				}
			}
		}
	}
	
    public static String unifyUrl(String srvURL){
    	if(srvURL.endsWith("/")){
			srvURL = srvURL.substring(0, srvURL.length()-1); 
		}
    	return srvURL;
     }
	public static boolean isEmpty(String str){
		if(str == null){
			return true;
		}else{
			if(str.trim().length() <= 0){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	/**
	 * 异常处理函数
	 */
	public static String handlerException(Throwable e,String prefix) {
		StringBuffer sbe = new StringBuffer();	
		StringWriter strWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(strWriter);
		e.printStackTrace(printWriter);
		printWriter.flush();
		LineNumberReader reader = new LineNumberReader(new StringReader(strWriter.toString()));
		try {
			String errInfo = reader.readLine();
			//sbe.append("\r\n");
			while(errInfo != null){
				sbe.append(prefix).append(":").append(errInfo).append("\r\n");
				errInfo = reader.readLine();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			try {
				reader.close();
				printWriter.close();
				strWriter.close();
				reader = null;
				strWriter = null;
				printWriter = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return sbe.toString();
	}
}
