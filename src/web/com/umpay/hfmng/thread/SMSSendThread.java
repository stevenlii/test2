package com.umpay.hfmng.thread;


import java.text.MessageFormat;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.bs.mpsp.busisvr.tools.sms.SmsCenter;
import com.bs.mpsp.busisvr.tools.sms.SmsSender;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.service.MessageService;

public class SMSSendThread implements Runnable {
	
	public static final Logger log = Logger.getLogger("HFBOS");
	
	private MessageService messageService;
	private String[] phones;
	private String msg;
	private static SmsSender smsSender;
	
	public SMSSendThread(){
		// 初始化短信服务
		if(smsSender == null){
			smsSender = SmsCenter.getInstance("010");
			
			messageService=(MessageService)SpringContextUtil.getBean("messageService");
			String ip = messageService.getSystemParam("sms.ip");
			String port = messageService.getSystemParam("sms.port");
			String protocol = messageService.getSystemParam("sms.protocol");
			
	        smsSender.setSmsHost(ip);
	        smsSender.setSmsPort(Integer.valueOf(port));
	        smsSender.setProtocol(protocol);
	        
	        log.info("SMSSendThread--短信服务初始化成功.");
		}
	}
	
	public void run() {
		try {
			//isSendSMS
			String isSendSMS = messageService.getSystemParam("isSendSMS");
			if("0".equals(isSendSMS)){
				return;
			}
			this.send();
		} catch (Exception e) {
			log.info("短信发送失败！！！",e);
		}
	}
	
	private void send(){
		String[] msgArray = splitMsg(msg, 70);
		//这是依据手机号依次发送    每个手机号每次发送的内容相同
		for(String mobileid : phones){
			if(!"".equals(mobileid)){
				for(String sms : msgArray){
					try {
						//new SmsUtil().sendSms(mobileid, sms);
						sendSms(mobileid, sms);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private String getSendSmsRpid() {
		String nodeId = "MNG-";
		String random = UUID.randomUUID().toString();
		String rpid = "HF" + nodeId + random.substring(0, 8);
		return rpid;
	}

	private boolean sendSms(String mobileId, String sms) {
		String rpid = getSendSmsRpid();
		return smsSender.sendSms(true, rpid, "", mobileId, SmsSender.SMS_FMT_CN, sms);
	}

	private String[] splitMsg(String msg,int maxLength){
		int msgLength = msg.length();
		if(msgLength <= maxLength){
			return new String[]{msg};
		}
		else{
			int eachSmsLength = maxLength - 5;
			int smsCount = msgLength/eachSmsLength;
			if(msgLength%eachSmsLength != 0){
				smsCount = smsCount + 1;
			}
			String[] msgArray = new String[smsCount];
			int index = 0;
			while(msg.length() >= eachSmsLength){
				String sms = msg.substring(0, eachSmsLength);
				msgArray[index] = "(" + (index + 1) + "/" + smsCount + ")" + sms;
				
				msg = msg.substring(eachSmsLength);
				index++;
			}
			if(msg.length() > 0){
				msgArray[smsCount - 1] = "(" + smsCount + "/" + smsCount + ")" + msg;
			}
			return msgArray;
		}
	}
	
	public String[] getPhones() {
		return phones;
	}

	public void setPhones(String[] phones) {
		this.phones = phones;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void setMsg(String key,Object... arguments) {
		this.msg = MessageFormat.format(messageService.getSystemParam(key), arguments);
	}
}
