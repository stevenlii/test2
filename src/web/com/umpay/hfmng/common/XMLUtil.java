package com.umpay.hfmng.common;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil<T> {
	/**
	 * ********************************************
	 * method name   : readXML 
	 * description   : xml转对象
	 * @return       : List<T>
	 * @param        : @param XMLPathAndName   XML文件的路径和地址  
	 * @param        : @param t  泛型对象
	 * @param        : @return
	 * modified      : lz ,  2012-9-3  下午15:20:10
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	public List<T> readXML(String XMLPathAndName, T t) {
		List<T> list = new ArrayList<T>();
		try {
			File f = new File(XMLPathAndName);
	    	SAXReader reader = new SAXReader();
	    	Document doc = reader.read(f);
	    	Element root = doc.getRootElement();//获得根节点
	    	Element foo;//二级节点
	    	Field[] properties = t.getClass().getDeclaredFields();//获得实例的属性
	    	//实例的set方法
	    	Method setmeth;
	    	for (Iterator i = root.elementIterator(t.getClass().getSimpleName()); i.hasNext();) {
	    		foo = (Element) i.next();//下一个二级节点
	            t=(T)t.getClass().newInstance();//获得对象的新的实例
	            for (int j = 0; j < properties.length; j++) {//遍历所有孙子节点
	            	//实例的set方法
	            	setmeth = t.getClass().getMethod("set"+ properties[j].getName().substring(0,1).toUpperCase()
	            			+ properties[j].getName().substring(1),properties[j].getType());
	                setmeth.invoke(t, foo.elementText(properties[j].getName()));//将对应节点的值存入
	            }
	            list.add(t);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
