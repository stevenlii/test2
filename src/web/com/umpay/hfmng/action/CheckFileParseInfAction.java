package com.umpay.hfmng.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CheckFileParseInf;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.CheckFileParseInfService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: CheckFileParseInfAction
 * @Description: 商户对账文件解析管理action
 * @author wanyong
 * @date 2013-3-1 下午05:23:51
 */
@Controller
@RequestMapping("/checkfileparseinf")
public class CheckFileParseInfAction extends BaseAction {

	@Autowired
	private CheckFileParseInfService checkFileParseInfService;

	/**
	 * @Title: list
	 * @Description: 链接到解析打包任务管理页面
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-3-1 下午04:12:09
	 */
	@RequestMapping(value = "/index")
	public String list() {
		return "checkfileparseinf/index";
	}

	/**
	 * @Title: add
	 * @Description: 链接到添加任务页面
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-3-4 下午02:13:42
	 */
	@RequestMapping(value = "/add")
	public String add() {
		return "checkfileparseinf/addtask";
	}

	/**
	 * @Title: query
	 * @Description: 查询任务信息
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-3-4 上午11:03:48
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> map2 : data) {
					String merId = map2.get("MERID") == null ? null : map2.get(
							"MERID").toString().trim();
					MerInfo merInfo = (MerInfo) HfCacheUtil.getCache()
							.getMerInfoMap().get(merId);
					map2.put("MERNAME", null == merInfo ? null : merInfo
							.getMerName());
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
	 * @Title: save
	 * @Description: 保存任务
	 * @param
	 * @param CheckFileParseInf
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 上午10:29:40
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(CheckFileParseInf checkFileParseInf,
			ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			String fileTypeName = checkFileParseInf.getFileTypeName();
			for (String subFileTypeName : fileTypeName.split(",")) {
				checkFileParseInf
						.setFileType(Integer.parseInt(subFileTypeName));
				List<String> dates = null;
				if ("3".equals(checkFileParseInf.getFileTypeName()))
					// 类型为月清算文件
					dates = StringUtil.get10Date_SpaceXMonthEnd(
							checkFileParseInf.getStartDate(), checkFileParseInf
									.getEndDate());
				else
					// 类型为非月清算文件
					dates = StringUtil.get10Date_SpaceXDayEnd(checkFileParseInf
							.getStartDate(), checkFileParseInf.getEndDate());
				for (String date : dates) {
					checkFileParseInf.setStatDate(date);
					if ("3".equals(checkFileParseInf.getFileTypeName()))
						// 设置文件名
						checkFileParseInf.setFileName(checkFileParseInf
								.getMerId()
								+ "." + StringUtil.f7time26Time(date));
					else
						// 设置文件名
						checkFileParseInf.setFileName(checkFileParseInf
								.getMerId()
								+ "." + StringUtil.f10time28Time(date));
					// 设置状态：初始化
					checkFileParseInf.setFileState(Const.CKFILE_STATE_INIT);
					checkFileParseInfService
							.saveCheckFileParseInf(checkFileParseInf);
				}

			}
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: delete
	 * @Description: 删除任务
	 * @param
	 * @param fileName
	 * @param fileType
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 下午05:17:42
	 */
	@RequestMapping(value = "/delete")
	public ModelAndView delete(String fileName, String fileType,
			ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			CheckFileParseInf checkFileParseInf = new CheckFileParseInf();
			checkFileParseInf.setFileName(fileName);
			checkFileParseInf.setFileType(Integer.parseInt(fileType));
			checkFileParseInfService.deleteCheckFileParseInf(checkFileParseInf);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: resetDealTimes
	 * @Description: 重置错误次数
	 * @param
	 * @param fileName
	 * @param fileType
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-3-5 下午05:19:20
	 */
	@RequestMapping(value = "/resetdealtimes")
	public ModelAndView resetDealTimes(String fileName, String fileType,
			ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			CheckFileParseInf checkFileParseInf = new CheckFileParseInf();
			checkFileParseInf.setFileName(fileName);
			checkFileParseInf.setFileType(Integer.parseInt(fileType));
			checkFileParseInf.setDealTimes(0);
			checkFileParseInfService.modifyCheckFileParseInf(checkFileParseInf);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: reset
	 * @Description: 重置任务
	 * @param
	 * @param fileName
	 * @param fileType
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-3-7 上午11:24:48
	 */
	@RequestMapping(value = "/reset")
	public ModelAndView reset(String fileName, String fileType, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			CheckFileParseInf checkFileParseInf = new CheckFileParseInf();
			checkFileParseInf.setFileName(fileName);
			checkFileParseInf.setFileType(Integer.parseInt(fileType));
			checkFileParseInf.setFileState(Const.CKFILE_STATE_INIT);
			checkFileParseInfService.modifyCheckFileParseInf(checkFileParseInf);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
}
