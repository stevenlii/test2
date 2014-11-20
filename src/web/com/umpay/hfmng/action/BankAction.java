package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.service.BankService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/bankinf")
public class BankAction extends BaseAction{
	@Autowired
	private OptionService optionService;
	@Autowired
	private BankService bankService;
	/**
	 * ********************************************
	 * method name   : list 
	 * description   : 跳转到支付服务商管理首页
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-9-28  下午02:12:10
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/index")
	public String list(ModelMap modeMap) throws DataAccessException {
		//查找权限
	//	String opts=getOptions("bankinf");
		   //opts="001,002,003,004,005,006,007,008";
	//	modeMap.addAttribute("opts", opts);
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("bankinf");
		modeMap.addAttribute("opts", opts);
		return "bankinf/index";
	}
	/**
	 * ********************************************
	 * method name   : queryAndView 
	 * description   : 查询支付服务商管理方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-9-28  下午02:13:38
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query()  {
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);// 格式化数据
				for (int i = 0; i < data.size(); i++) {
					Map bank = data.get(i);
					Object bankType =  bank.get("BANKTYPE");
					bank.put("BANKTYPE", bankTypeMap.get(bankType.toString()));
					data.set(i, bank);
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
	 * method name   : add 
	 * description   : 跳转到添加支付服务商管理页面
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-9-28  下午02:14:36
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap) {
		return "bankinf/addBank";
	}
	/**
	 * ********************************************
	 * method name   : view 
	 * description   : 查看详情方法
	 * @return       : String
	 * @param        : @param id
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-9-28  下午02:15:30
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/detail")
	public String view(String id, ModelMap modeMap) throws DataAccessException {
		BankInfo bank = new BankInfo();
		try {
			bank = bankService.load(id);
			log.info("查看详情成功");
		} catch (Exception e) {
			log.error("查看支付服务商详情失败", e);
		}
		if(bank != null){
			bank.trim();
		}
		modeMap.addAttribute("bank", bank);
		return "bankinf/detail";
	}
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 跳转到修改支付服务商管理页面
	 * @return       : String
	 * @param        : @param id
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-9-28  下午02:15:59
	 * *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String load(String id, ModelMap modeMap) throws DataAccessException {
		BankInfo bank = new BankInfo();
		try {
			bank = bankService.load(id);
			log.info("加载支付服务商数据成功");
		} catch (Exception e) {
			log.error("加载支付服务商数据失败", e);
		}
		if(bank != null){
			bank.trim();
		}
    	modeMap.addAttribute("bank", bank);
		return "bankinf/modifyBank";
	}
	/**
	 * ********************************************
	 * method name   : save 
	 * description   : 添加操作执行的保存支付服务商方法
	 * @return       : ModelAndView
	 * @param        : @param bank
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-9-28  下午02:16:39
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(BankInfo bank)throws DataAccessException {
	   User user = this.getUser();
       bank.setModUser(user.getId()); // 新增修改人操作
		String string="0";
		try {
			 string = bankService.saveBank(bank);
			 log.info("添加支付服务商成功");
		} catch (Exception e) {
			log.error("添加支付服务商失败", e);
		}
	//	 String string="1";// 1表示添加成功,0代表添加失败
		return new ModelAndView("jsonView", "ajax_json", string);
	}
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 修改支付服务商方法
	 * @return       : ModelAndView
	 * @param        : @param bank
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-9-28  下午02:18:15
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(BankInfo bank, ModelMap modeMap) {
		User user = this.getUser();
		bank.setModUser(user.getId()); // 修改人
		String result = "0";
		try {
			result = bankService.modifyBankInfo(bank);
			log.info("修改支付服务商成功");
		} catch (Exception e) {
			log.error("修改支付服务商失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	/**
	 * ********************************************
	 * method name   : disable 
	 * description   : 禁用方法
	 * @return       : ModelAndView
	 * @param        : @param ID
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-9-28  下午02:18:42
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		// result = srvMerInfo.insertMerAudit(merInfo, "4");
		User user = this.getUser(); // 获取当前登录用户
		String result="0";
		try {
			result = bankService.enableAndDisable(array, user, "4");
			log.info("禁用支付服务商成功");
		} catch (Exception e) {
			log.error("禁用支付服务商失败", e);
		}  //禁用操作,目标状态为"4"
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	/**
	 * ********************************************
	 * method name   : enable 
	 * description   :  启用方法
	 * @return       : ModelAndView
	 * @param        : @param ID
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-9-28  下午02:19:12
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		User user = this.getUser(); //获取当前登录用户
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		String result="0";
		try {
			result = bankService.enableAndDisable(array, user, "2");
			log.info("启用支付服务商成功");
		} catch (Exception e) {
			log.error("启用支付服务商失败", e);
		}  //启用操作,目标状态为 "2"
		// result = srvMerInfo.insertMerAudit(merInfo, "3");
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	/**
	 * ********************************************
	 * method name   : checkBankId 
	 * description   : 验证支付服务商编号唯一性方法 
	 * @return       : ModelAndView
	 * @param        : @param bankId
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-9-28  下午02:19:36
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/checkBankId")
	public ModelAndView checkBankId(String bankId) {
		String msg = "1"; // 默认结果 1表示不存在 0 表示存在
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("bankId", bankId.trim());
		BankInfo bankInfo=null;
		if(bankId != ""){
		 bankInfo=(BankInfo) bankService.load(bankId.trim()); 
		}
		if(bankInfo!= null){
			msg="0";  //如果对象不为空 则表示存在  将结果置为 0
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
}
