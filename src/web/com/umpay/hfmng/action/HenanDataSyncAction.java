package com.umpay.hfmng.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.HNSynTask;
import com.umpay.hfmng.service.HenanDataSyncService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: HenanDataSyncAction
 * @Description: 河南小额数据同步给boss 包括商户和商品信息
 * @author chenwei
 * @date 2013-7-26
 */
@Controller
@RequestMapping("/henandatasync")
public class HenanDataSyncAction extends BaseAction {
	@Autowired
	private HenanDataSyncService HenanDataSyncService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;

	
	/**
	 * ********************************************
	 * method name   : merBankSync 
	 * description   : 商户银行同步
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-8-1  下午03:20:21
	 * *******************************************
	 */
	@RequestMapping(value = "/merbanksync")
	public String merBankSync() {
		return "henandatasync/merbanksync";
	}

	/**
	 * ********************************************
	 * method name   : merBankSync
	 * description   : 商品银行同步
	 * @return       : String
	 * *******************************************
	 */
	@RequestMapping(value = "/goodsbanksync")
	public String goodsBankSync() {
		return "henandatasync/goodsbanksync";
	}

	/**
	 * ********************************************
	 * method name   : queryMerBank
	 * description   : 商户银行查询
	 * @return       : ModelAndView
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querymerbank")
	public ModelAndView queryMerBank(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> merNameMap = HfCacheUtil.getCache().getMerNameMap();
				Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map henan = data.get(i);
					String merId = (String) henan.get("merId");
					String merName = merNameMap.get(merId);
					henan.put("MERNAME", merName);
					String bankId = (String) henan.get("bankId");
					String bankName = bankNameMap.get(bankId);
					henan.put("BANKNAME", bankName);
					data.set(i, henan);
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * ********************************************
	 * method name   : singleMerBankSync 
	 * description   : 同步商户银行信息
	 * @return       : ModelAndView
	 * @author       :  chenwei 
	 * *******************************************
	 */
	@RequestMapping(value = "/singleMerBankSync")
	public ModelAndView singleMerBankSync(String id){
		String string = "同步此商户银行信息失败！";
		JSONObject json=new JSONObject();
		try{
			string = HenanDataSyncService.singleMerBankSync(id);// id=merid
			if ("0000".equals(string)) {
				json.put("retCode", "0000");
				json.put("retMsg", "同步此商户银行信息成功！");
				log.info("同步此商户银行信息成功！");
			} else {
				json.put("retCode", "9999");
				json.put("retMsg", "同步此商户银行信息失败！");
				log.error("同步此商户银行信息失败！");
			}
		}catch (Exception e) {
			json.put("retCode", "9999");
			json.put("retMsg", "同步此商户银行信息失败！");
			log.error("同步此商户银行信息失败！",e);
		}
		return new ModelAndView("jsonView", "ajax_json", json.toString());
		
	}
	/**
	 * ********************************************
	 * method name   : singleGoodsBankSync 
	 * description   : 同步商品银行信息
	 * @return       : ModelAndView
	 * @author       :  chenwei 
	 * *******************************************
	 */
	@RequestMapping(value = "/singleGoodsBankSync")
	public ModelAndView singleGoodsBankSync(String id){
		String string = "同步此商品银行信息失败！";
		JSONObject json=new JSONObject();
		try{
			string = HenanDataSyncService.singleGoodsBankSync(id);// id=merid-goodsid
			if ("0000".equals(string)) {
				json.put("retCode", "0000");
				json.put("retMsg", "同步此商品银行信息成功！");
				log.info("同步此商品银行信息成功！");
			} else {
				json.put("retCode", "9999");
				json.put("retMsg", "同步此商品银行信息失败！");
				log.error("同步此商品银行信息失败！");
			}
		}catch (Exception e) {
			json.put("retCode", "9999");
			json.put("retMsg", "同步此商品银行信息失败！");
			log.error("同步此商品银行信息失败！",e);
		}
		return new ModelAndView("jsonView", "ajax_json", json.toString());
		
	}
	/**
	 * ********************************************
	 * method name   : queryGoodsBank 
	 * description   : 商品银行查询
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-8-2  下午04:27:02
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querygoodsbank")
	public ModelAndView queryGoodsBank(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> merNameMap = HfCacheUtil.getCache().getMerNameMap();
				Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
				Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map henan = data.get(i);
					String merId = (String) henan.get("merId");
					String merName = merNameMap.get(merId);
					henan.put("MERNAME", merName);
					String goodsId = (String) henan.get("GOODSID");
					GoodsInfo goods = (GoodsInfo) goodsMap.get(merId+"-"+goodsId);
					if(goods != null){
						henan.put("GOODSNAME",goods.getGoodsName());
					}
					String bankId = (String) henan.get("bankId");
					String bankName = bankNameMap.get(bankId);
					henan.put("BANKNAME", bankName);
					data.set(i, henan);
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	/**
	 * ********************************************
	 * method name   : batchSync 
	 * description   : 批量完整同步结果页
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-8-2  下午04:22:33
	 * *******************************************
	 */
	@RequestMapping(value = "/batchsync")
	public String batchSync() {
		return "henandatasync/batchsync";
	}
	/**
	 * ********************************************
	 * method name   : queryBatchSync
	 * description   : 批量完整同步查询
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-8-2  下午04:27:14
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryBatchSync")
	public ModelAndView queryBatchSync(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true); //此方法为查询离线库
				ObjectUtil.trimData(data);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map henan = data.get(i);
					String taskId = (String) henan.get("taskId");
					henan.put("TASKID", taskId);//任务ID
					
					Integer.valueOf(String.valueOf(henan.get("taskType"))).intValue();
					int taskType = Integer.valueOf(String.valueOf(henan.get("taskType"))).intValue();
					henan.put("TASKTYPE", taskType);//任务类型
					
					String creator = (String) henan.get("creator");
					Map<String, String> users = optionService.getAllUserIdAndName();
					String creatorName = users.get(ObjectUtil.trim(creator));
					henan.put("CREATOR", creatorName);//任务发起人
					
					Timestamp inTime = (Timestamp) henan.get("inTime");
					String intime = df.format(inTime);
					henan.put("INTIME", intime);//创建时间

					Timestamp modTime = (Timestamp) henan.get("modTime");
				    String modtime = df.format(modTime);
					henan.put("MODTIME", modtime);//最后一次修改时间

					int synState = Integer.valueOf(String.valueOf(henan.get("synState"))).intValue();
					henan.put("SYNSTATE", synState);//任务状态

					int synResult = Integer.valueOf(String.valueOf(henan.get("synResult"))).intValue();
					henan.put("SYNRESULT", synResult);//结果状态
					data.set(i, henan);
				}
				long count = queryCount(queryKey, map, true);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * ********************************************
	 * method name   : bacthMerBankSync 
	 * description   : 批量完整同步商户银行信息
	 * @return       : ModelAndView
	 * @author       :  chenwei 
	 * *******************************************
	 */
	@RequestMapping(value = "/bacthMerBankSync")
	public ModelAndView bacthMerBankSync(String sendFlag){
		JSONObject json=new JSONObject();
		User user = this.getUser(); // 获取当前登录用户
		try{
			String alreadyExit = HenanDataSyncService.checkAll();
			if ("true".equals(alreadyExit)) {
				json.put("retCode", "9999");
				json.put("retMsg", "有正在进行的批量任务，请稍等！");
			} else {
				HenanDataSyncService.batchDataSync(sendFlag,user);
				json.put("retCode", "0000");
				json.put("retMsg", "批量完整同步商户银行信息任务完成！");
				log.info("批量完整同步商户银行信息任务完成！");
			}
		}catch (Exception e) {
			json.put("retCode", "9999");
			json.put("retMsg", "批量完整同步商户银行信息任务失败！");
			log.error("批量完整同步商户银行信息任务失败！",e);
		}
		return new ModelAndView("jsonView", "ajax_json", json.toString());
		
	}
	
	@RequestMapping(value = "/detail")
	public String showDetail(String taskId, ModelMap modeMap){
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("taskId", taskId);
		HNSynTask hnSynTask=HenanDataSyncService.get(mapWhere);

		Map<String, String> users = optionService.getAllUserIdAndName();
		String creatorName = users.get(ObjectUtil.trim(hnSynTask.getCreator()));
		hnSynTask.setCreator(creatorName);
		modeMap.addAttribute("hnSynTask", hnSynTask);//任务发起人
	    return "henandatasync/detail";
	}
	
	@RequestMapping(value = "/downloadDoc")
	public ModelAndView downloadDoc(String fileName, HttpServletRequest request, HttpServletResponse response){
		File file = null;
		String msg = "";
		try {
			//获取下载路径
			String path = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+fileName;
			try {
				path=URLDecoder.decode(path,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}			
			file = new File(path);
			log.info("获取下载文件成功");
		} catch (Exception e) {
			msg = "获取下载文件失败";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		try {
			if (file != null && file.exists()) {
				fileName = file.getName();
				log.info("开始下载文件：" + fileName);
				response.reset();
				response.setContentType("application/x-msdownload;charset=UTF-8");
//				response.setHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
				response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				InputStream inStream = new FileInputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
				// 循环取出流中的数据
				byte[] b = new byte[inStream.available()];
				while (inStream.read(b) != -1)
					bos.write(b, 0, b.length);
				bos.flush();
				bos.close();
				inStream.close();
				log.info("下载文件[" + fileName + "]成功");
			} else {
				log.info("下载失败，目录下没有文件");
			}
		} catch (Exception e) {
			msg = "下载文件失败,请稍后再试";
			log.error(msg, e);
			return new ModelAndView("jsonView", "ajax_json", msg);
		}
		return null;
	}
}