package com.umpay.hfmng.action.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.ModelMap;

import com.umpay.hfmng.action.DemoAction;

/**
 * ******************  类说明  *********************
 * class       :  DemoActionTest
 * @author     :  yangwr
 * @version    :  1.0  
 * description :  执行前注释掉ctx-mvc.xml中的bean xmlViewResolver
 * @see        :                        
 * ***********************************************
 */
public class DemoActionTest {
	private static Logger logger = Logger.getLogger(DemoActionTest.class);
	private ApplicationContext ctx = null ;
	private DemoAction action = null;
	@Before
	public void init(){
		String[] locations = {"config/ctx-common.xml", "config/ctx-mvc.xml", "config/ctx-uniQuery.xml"};
	    ctx = new ClassPathXmlApplicationContext(locations);
	    action = (DemoAction)ctx.getBean("demoAction");
	}
	@Test
	public void load_case1(){
		ModelMap modeMap = new ModelMap();
		action.load("9996", modeMap);
		logger.info(""+modeMap);
	}
}
