package com.umpay.hfmng.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.HFTask;
import com.umpay.hfmng.model.HFTaskMnt;
import com.umpay.hfmng.model.TaskRule;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.hfmng.service.HFTaskService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: TimeTaskAction
 * @Description: 定时任务相关处理
 * @author helin
 * @date 2013-1-15 下午8:40:13
 */
@Controller
@RequestMapping("/timetask")
public class TimeTaskAction extends BaseAction {
	@Autowired
	private HFTaskService hfTaskService;

	@Autowired
	private CouponLogService couponLogService;

	/**
	 * @Title: list
	 * @Description: 链接到任务查询管理页面
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-13 下午3:48:46
	 */
	@RequestMapping(value = "/taskList")
	public String list() {
		return "timetask/taskList";
	}

	/**
	 * @Title: add
	 * @Description: 链接到任务添加页面
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-15 下午8:40:17
	 */
	@RequestMapping(value = "/add")
	public String add() {
		return "timetask/addTask";
	}

	/**
	 * @Title: query
	 * @Description: 查询任务信息
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-15 下午8:40:55
	 */
	@RequestMapping(value = "/queryTaskInfo")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染定时任务所属平台名称
					subMap.put("PLATNAME", ParameterPool.platNames.get(subMap.get("PLATNAME").toString()));
					subMap.put("ISRETRY", ("2".equals(subMap.get("ISRETRY").toString())) ? "是" : "否");
					subMap.put("STATE", ("2".equals(subMap.get("STATE").toString())) ? "启用" : "不启用");
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
	 * @Title: createTask
	 * @Description: 新增任务
	 * @param
	 * @param hFTask
	 * @param taskRule
	 * @return
	 * @author helin
	 * @date 2013-1-15 下午8:41:25
	 */
	@RequestMapping(value = "/createTask")
	public ModelAndView createTask(HFTask hFTask, TaskRule taskRule) {
		String msg = "0"; // 默认操作失败
		try {
			hFTask.setModUser(getUser().getName() + "(" + getUser().getId() + ")");
			hfTaskService.createTask(hFTask, taskRule);

			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_HF_TASK");
			log.setOpertype(Const.LOG_OPT_CREATE);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(hFTask.getTaskId()); // 兑换券编号
			log.setIxdata2(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // 操作日期
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(Const.LOG_OPT_CREATE); // 操作类型

			String optdata = "定时任务添加，添加内容为：定时任务ID：" + hFTask.getTaskId() + "  请求地址： " + hFTask.getPostUrl();
			log.setOperdata(optdata);
			couponLogService.addLog(log);
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
	 * @Title: detail
	 * @Description: 链接到任务详细信息页面
	 * @param
	 * @param taskId
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:41:55
	 */
	@RequestMapping(value = "/detail")
	public String detail(String taskId, ModelMap modelMap) throws Exception {
		HFTask hfTask = hfTaskService.getTask(taskId);
		hfTask.setPlatName(ParameterPool.platNames.get(hfTask.getPlatName()));
		modelMap.addAttribute("taskInf", hfTask);
		return "timetask/detailTaskInf";
	}

	/**
	 * @Title: modify
	 * @Description: 链接到任务修改页面
	 * @param
	 * @param taskId
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:42:18
	 */
	@RequestMapping(value = "/toModify")
	public String modify(String taskId, ModelMap modelMap) throws Exception {
		HFTask hfTask = hfTaskService.getTask(taskId);
		TaskRule taskRule = (TaskRule) JSONObject.toBean(JSONObject.fromObject(hfTask.getTaskRule()), TaskRule.class);
		modelMap.addAttribute("taskInf", hfTask);
		modelMap.addAttribute("taskRule", taskRule);
		return "timetask/modifyTask";
	}

	/**
	 * @Title: update
	 * @Description: 修改任务信息
	 * @param
	 * @param hFTask
	 * @param taskRule
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:42:45
	 */
	@RequestMapping(value = "/modifyTask")
	public ModelAndView update(HFTask hFTask, TaskRule taskRule) throws Exception {
		String msg = "0"; // 默认操作失败
		try {
			hFTask.setModUser(getUser().getName() + "(" + getUser().getId() + ")");
			hfTaskService.modifyTask(hFTask, taskRule);
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
	 * @Title: detail
	 * @Description: 链接到手动执行任务确认页面
	 * @param
	 * @param taskId
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:41:55
	 */
	@RequestMapping(value = "/taskRunConfirm")
	public String taskRunConfirm(String taskId, ModelMap modelMap) throws Exception {
		HFTask hfTask = hfTaskService.getTask(taskId);
		hfTask.setPlatName(ParameterPool.platNames.get(hfTask.getPlatName()));
		modelMap.addAttribute("taskInf", hfTask);
		return "timetask/taskRunConfirm";
	}

	/**
	 * @Title: manualRunTask
	 * @Description: 手动触发任务
	 * @param
	 * @param taskId
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-17 上午11:20:11
	 */
	@RequestMapping(value = "/manualRunTask")
	public ModelAndView manualRunTask(String taskId) throws Exception {
		String msg = "0"; // 默认操作失败
		try {
			hfTaskService.manualRunTask(taskId);
			msg = "2"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: getTaskDropDownList
	 * @Description: 获取任务列表，用于下拉框
	 * @param
	 * @param paraType
	 * @return
	 * @author helin
	 * @date 2013-1-16 下午10:11:39
	 */
	@RequestMapping(value = "/getTaskDropDownList")
	public ModelAndView getTaskDropDownList(String paraType) {
		StringBuffer jsonString = new StringBuffer();
		jsonString.append("[");
		try {
			List<HFTask> hfTasks = hfTaskService.getAllTaskList();
			boolean first = true;
			for (HFTask hfTask : hfTasks) {
				if (first) {
					jsonString.append("{\"value\":\"" + hfTask.getTaskId() + "\",");
					jsonString.append("\"text\":\"" + hfTask.getTaskId() + "-" + hfTask.getTaskDesc() + "\"}");
					first = false;
				} else {
					jsonString.append(",{\"value\":\"" + hfTask.getTaskId() + "\",");
					jsonString.append("\"text\":\"" + hfTask.getTaskId() + "-" + hfTask.getTaskDesc() + "\"}");
				}
			}
			jsonString.append("]");
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			jsonString.append("{}");
		} catch (Exception e) {
			jsonString.append("{}");
		}
		return new ModelAndView("jsonView", "ajax_json", jsonString.toString());
	}

	// **************************************************监控部分*********************************************************************
	/**
	 * @Title: toTaskMnt
	 * @Description: 进入任务监控页面
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-16 下午10:12:14
	 */
	@RequestMapping(value = "/taskMonitor")
	public String toTaskMnt() {
		return "timetask/taskMonitor";
	}

	/**
	 * @Title: query
	 * @Description: 查询任务监控信息
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-15 下午8:40:55
	 */
	@RequestMapping(value = "/queryTaskMntInfo")
	public ModelAndView queryTaskMntInfo() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染定时任务所属平台名称
					subMap.put("STATEV", ParameterPool.timerTaskStates.get(subMap.get("STATE").toString()));
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
	 * @Title: detail
	 * @Description: 链接到任务监控信息页面
	 * @param
	 * @param taskRpid
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:41:55
	 */
	@RequestMapping(value = "/taskMntDetail")
	public String taskMntDetail(String taskRpid, ModelMap modelMap) throws Exception {
		HFTaskMnt hfTaskMnt = hfTaskService.getTaskMntInfo(taskRpid);
		modelMap.addAttribute("taskMnt", hfTaskMnt);
		modelMap.addAttribute("taskRunState", ParameterPool.timerTaskStates.get(""+hfTaskMnt.getState()));
		return "timetask/taskMntDetail";
	}
}
