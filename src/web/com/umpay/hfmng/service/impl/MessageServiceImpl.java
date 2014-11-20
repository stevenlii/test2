package com.umpay.hfmng.service.impl;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.umpay.hfmng.common.MessageUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.service.MessageService;

public class MessageServiceImpl implements MessageService{
	public static Logger log = Logger.getLogger(MessageServiceImpl.class);
	private MessageSource messageSource;
	private MessageSource sysconfSource;
	public MessageServiceImpl(MessageSource messageSource,MessageSource sysconfSource){
		this.messageSource = messageSource;
		this.sysconfSource = sysconfSource;
	}
	public String getSystemParam(String key){
		return MessageUtil.getLocalProperty(sysconfSource,key);
	}
	public String getSystemParam(String key,String defaultValue){
		String value = getSystemParam(key);
		if(ObjectUtil.isEmpty(value)){
			value = defaultValue;
		}
		return value;
	}
	public String getMessage(String key){
		return MessageUtil.getLocalProperty(messageSource,key);
	}
	public String getMessageDetail(String key){
		String detail = MessageUtil.getLocalProperty(messageSource,key+".detail");
		if(ObjectUtil.isEmpty(detail)){
			detail = MessageUtil.getLocalProperty(messageSource,key);
		}
		return detail;
	}

}
