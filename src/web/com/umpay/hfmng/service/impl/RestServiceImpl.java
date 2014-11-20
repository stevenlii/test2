package com.umpay.hfmng.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfmng.common.HttpClientControler;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.SessionThreadLocal;
import com.umpay.hfmng.model.PayltdRollback;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.RestService;

@Service
public class RestServiceImpl implements RestService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MessageService messageService;
	@Autowired
	private HttpClientControler httpClientControler;
	
	public String updateRestCache() throws Exception {
		String retCode="";
		String restUrls=messageService.getSystemParam("REST.SRV.URLS");
		restUrls=ObjectUtil.trim(restUrls);
		String[] urls=restUrls.split(",");
		Map<String,String> reqMap = new HashMap<String,String>();
		String rpid=createRpid();
		for(int i=0;i<urls.length;i++){
			String url=new StringBuffer("http://").append(urls[i]).append("/hfrestbusi/cache/")+rpid+"/ALLCACHE.xml".toString();
			retCode=getRetCode(url,reqMap);
			if(!"0000".equals(retCode)){
				log.info("资源层返回码："+retCode+"；url："+url);
				return "0";//0：失败
			}
		}
		return "1";//1：成功
	}
	
	public String revokePayLtd(PayltdRollback payltdRollback) throws Exception{
		String retCode="";
		String restUrl=messageService.getSystemParam("REST.SRV.URL");
		restUrl=ObjectUtil.trim(restUrl);
		if(restUrl.equals("")){
			log.info("未配置资源层URL");
			return "0";//0：失败
		}
		Map<String,String> reqMap = new HashMap<String,String>();
		//必须设置省代码，银行号，本次回滚金额，商户号，商品号，手机号
		String mobileId = ObjectUtil.trim(payltdRollback.getMobileId());
		String rpid=createRpid();
		reqMap.put(HFBusiDict.RPID, rpid);
		reqMap.put(HFBusiDict.MERID, ObjectUtil.trim(payltdRollback.getMerId()));
		reqMap.put(HFBusiDict.GOODSID, ObjectUtil.trim(payltdRollback.getGoodsId()));
		reqMap.put(HFBusiDict.PROVCODE, ObjectUtil.trim(payltdRollback.getProvCode()));
		reqMap.put(HFBusiDict.AMT, ObjectUtil.trim(payltdRollback.getAmt()));
		reqMap.put(HFBusiDict.MOBILEID, ObjectUtil.trim(payltdRollback.getMobileId()));
		reqMap.put(HFBusiDict.BANKID, ObjectUtil.trim(payltdRollback.getBankId()));
		String url=new StringBuffer("http://").append(restUrl).append("/hfrestbusi/xerisk/revokeproc/")+rpid+"/"+mobileId+".xml".toString();
		retCode=getRetCode(url,reqMap);
		if(!"0000".equals(retCode)){
			log.info("资源层返回码："+retCode+"；url："+url);
			return "0";//0：失败
		}
		return "1";
	}
	
	private String createRpid(){
		String rpid = "Mng"+SequenceUtil.formatSequence(SequenceUtil.getInstance().getSequence4File("hfMngBusi.rpid"), 10);
		SessionThreadLocal.setSessionValue(HFBusiDict.RPID, rpid);
		return rpid;
	}
	
	private String getRetCode(String url,Map<String,String> paramMap){
		Map<String,Object> retMsg=postHttpRest(url,paramMap);
		Object retCode=retMsg.get("retCode");
		return (retCode==null)?null:retCode.toString();
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> postHttpRest(String urlstr, Map<String,String> paramMap){
		Map<String,Object> respMessage = new HashMap<String,Object>();
		paramMap.put("x-accept-charset", "UTF-8");
		PostMethod postMethod = new PostMethod(urlstr);
		postMethod.getParams().setContentCharset("UTF-8");
		Object result = httpClientControler.getHttpResPost_Form(paramMap,postMethod);
		if(result == null){
			log.info("rest未返回信息");
		}else if(result instanceof Exception){
			Exception e = (Exception)result;
			log.info("rest服务通信异常",e);
		}else if(!(result instanceof Map)){
			log.info("rest返回格式异常");
		}else{
			respMessage = (Map<String,Object>)result;
		}
		return respMessage;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> getHttpRest(String urlstr, Map<String,String> paramMap){
		Map<String,Object> respMessage = new HashMap<String,Object>();
		paramMap.put("x-accept-charset", "UTF-8");
		GetMethod getMethod = new GetMethod(urlstr);
		getMethod.getParams().setContentCharset("UTF-8");
		Object result = httpClientControler.getHttpResGet_Form(paramMap,getMethod);
		if(result == null){
			log.info("rest未返回信息");
		}else if(result instanceof Exception){
			Exception e = (Exception)result;
			log.info("rest服务通信异常",e);
		}else if(!(result instanceof Map)){
			log.info("rest返回格式异常");
		}else{
			respMessage = (Map<String,Object>)result;
		}
		return respMessage;
	}

	
	/** ********************************************
	 * method name   : queryPayltd 
	 * modified      : xuhuafeng ,  2013-4-15
	 * @see          : @see com.umpay.hfmng.service.RestService#queryPayltd(java.lang.String)
	 * ********************************************/     
	public Map<String, String> queryPayed(String mobileId) {
		Map<String, String> res = new HashMap<String, String>();
		res.put("result", "no");
		String restUrl = messageService.getSystemParam("REST.SRV.URL");
		restUrl = ObjectUtil.trim(restUrl);
		if(restUrl.equals("")){
			log.info("未配置资源层URL");
			return res;//0：失败
		}
		Map<String,String> reqMap = new HashMap<String,String>();
		//必须设置手机号
		String rpid = createRpid();
		reqMap.put(HFBusiDict.MOBILEID, mobileId);
		String url = new StringBuffer("http://").append(restUrl).append("/hfrestbusi/xeuserltd/")+rpid+"/"+mobileId+".xml".toString();
		Map<String,Object> retMsg = getHttpRest(url,reqMap);
		String retCode = (String) retMsg.get(HFBusiDict.RETCODE);
		if(!"0000".equals(retCode)){
			log.info("资源层返回码："+retCode+"；url："+url);
			return res;//0：失败
		}
		double monthpayed = Integer.valueOf(retMsg.get(HFBusiDict.MONTHPAYED).toString());
		monthpayed = monthpayed/100;
		DecimalFormat df = new DecimalFormat("0.00");
		String money = df.format(monthpayed);
		log.info("查询资源层获取手机号["+mobileId+"]的当月累计额"+money+"元");
		res.put(HFBusiDict.MONTHPAYED, money);
		res.put("result", "yes");
		return res;
	}

}
