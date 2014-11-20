package com.umpay.hfmng.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfmng.common.HttpClientControler;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.TradeService;

@Service
public class TradeServiceImpl implements TradeService{
	private static Logger log = Logger.getLogger(TradeServiceImpl.class);
	@Autowired
	private HttpClientControler httpClientControler;
	@Autowired
	private MessageService messageService;

	/**
	 * ********************************************
	 * method name   : xeRevoke 
	 * description   : 小额解冻请求业务层
	 * modified      : chenwei
	 * @see          : 2013-7-11
	 * ********************************************
	 */
	public String xeRevoke(Map<String,String> sendMap, String batchProv) {
		String retCode = "";
		String tradeUrl=ObjectUtil.trim(messageService.getSystemParam("TRADE.SRV.URL"));
		if(tradeUrl.equals("")){
			tradeUrl = "10.10.1.172:6608";
		}
		String url = null;
		if (batchProv.startsWith("BJ")) {
			url = new StringBuffer("http://").append(tradeUrl).append("/hftradebusi/REVOKEBJ").toString();			
		} else {
			url = new StringBuffer("http://").append(tradeUrl).append("/hftradebusi/REVOKEYN").toString();			
		}
		retCode = getRetCode(url,sendMap);
		return retCode;
	}

	private String getRetCode(String url, Map<String,String> paramMap){
		Map<String,Object> retMsg = postHttpRest(url, paramMap);
		Object retCode = retMsg.get(HFBusiDict.RETCODE);
		return (retCode==null)?null:retCode.toString();
	}

	@SuppressWarnings("unchecked")
	private Map<String,Object> postHttpRest(String urlstr, Map<String,String> paramMap){
		Map<String,Object> respMessage = new HashMap<String,Object>();
		paramMap.put("x-accept-charset", "GBK");
		PostMethod postMethod = new PostMethod(urlstr);
		postMethod.getParams().setVersion(HttpVersion.HTTP_1_1);
		postMethod.getParams().setContentCharset("GBK");
		Object result = httpClientControler.getHttpResPost_XML(paramMap,postMethod);
		if(result == null){
			log.info("trade未返回信息");
		}else if(result instanceof Exception){
			Exception e = (Exception)result;
			log.info("trade服务通信异常",e);
		}else if(!(result instanceof Map)){
			log.info("trade返回格式异常");
		}else{
			respMessage = (Map<String,Object>)result;
		}
		return respMessage;
	}

	public String hnDataSync(Map<String, String> sendMap) {
		String retCode = "";
		String tradeUrl=ObjectUtil.trim(messageService.getSystemParam("TRADE.SRV.URL"));
		if(tradeUrl.equals("")){
			tradeUrl = "10.10.1.172:6608";
		}
		String url = new StringBuffer("http://").append(tradeUrl).append("/hftradebusi/HNSJTB").toString();
		retCode = getRetCode(url,sendMap);
		return retCode;
	}
}