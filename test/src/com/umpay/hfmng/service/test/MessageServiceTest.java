package com.umpay.hfmng.service.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.ModelMap;

import com.umpay.hfmng.action.DemoAction;
import com.umpay.hfmng.service.MessageService;

/**
 * ******************  类说明  *********************
 * class       :  DemoActionTest
 * @author     :  yangwr
 * @version    :  1.0  
 * description :  执行前注释掉ctx-mvc.xml中的bean xmlViewResolver
 * @see        :                        
 * ***********************************************
 */
public class MessageServiceTest {
	private static Logger logger = Logger.getLogger(MessageServiceTest.class);
	private ApplicationContext ctx = null ;
	private MessageService srv = null;
	@Before
	public void init(){
		String[] locations = {"config/ctx-common.xml", "config/ctx-mvc.xml", "config/ctx-uniQuery.xml"};
	    ctx = new ClassPathXmlApplicationContext(locations);
	    srv = (MessageService)ctx.getBean("messageService");
	}
	@Test
	public void load_case1(){
		srv.getMessage("");
		srv.getMessageDetail("");
		logger.info(""+srv.getMessage("86059999"));
		logger.info(""+srv.getMessageDetail("86059999"));
		logger.info(""+srv.getSystemParam("SystemType"));
	}
}
