package com.umpay.hfmng.common.test;

import java.io.File;
import java.util.List;

import com.umpay.hfmng.common.Option;
import com.umpay.hfmng.common.XMLUtil;

public class XMLUtilTest<T> {
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		System.out.println(XMLUtilTest.class.getClassLoader().getResource(""));
		System.out.println(ClassLoader.getSystemResource(""));
		System.out.println(XMLUtilTest.class.getResource(""));
		System.out.println(XMLUtilTest.class.getResource("/"));
		System.out.println(new File("").getAbsolutePath());

		long lasting = System.currentTimeMillis();
		XMLUtil<Option> d = new XMLUtil<Option>();
		Option option = new Option();
		String path="src\\resources\\urlacl\\goodsinf.xml";
		//path="file:/D:/goodsinf.xml";
		//path="file:/E:/workspaces/MyEclipse/hfMngBusi/WebRoot/WEB-INF/classes/urlacl/goodsinf.xml";
		//path="file:///E:/workspaces/MyEclipse/hfMngBusi/src/resources/urlacl/goodsinf.xml";
		List<Option> list=  d.readXML(path,option);
		long lasting2 = System.currentTimeMillis();
		System.out.println("读取XML文件结束,用时"+(lasting2 - lasting)+"ms");
	    System.out.println("XML文件读取结果");
	    for(int i =0;i<list.size();i++){
	    	Option opt=(Option)list.get(i);
	        System.out.println("code:"+opt.getCode());
	        System.out.println("url:"+opt.getUrl());
	        System.out.println("perm:"+opt.getPerm());
	    }
	}
}
