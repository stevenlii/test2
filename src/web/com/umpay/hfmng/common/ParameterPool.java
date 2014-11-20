package com.umpay.hfmng.common;

import java.util.HashMap;

import com.umpay.hfmng.model.HFTask;
import com.umpay.hfmng.model.HFTaskMnt;

public class ParameterPool {

	// 审核状态
	public static HashMap<String, String> auditStates = new HashMap<String, String>();

	// 电子兑换券功能-兑换券状态
	public static HashMap<String, String> couponInfStates = new HashMap<String, String>();
	// 电子兑换券功能-兑换券类型
	public static HashMap<String, String> couponInfTypes = new HashMap<String, String>();
	// 电子兑换券功能-兑换券规则状态
	public static HashMap<String, String> couponRuleStates = new HashMap<String, String>();
	// 电子兑换券功能-批量导入批次状态
	public static HashMap<String, String> couponBatchStates = new HashMap<String, String>();
	// 电子兑换券功能-兑换码状态
	public static HashMap<String, String> couponCodeStates = new HashMap<String, String>();
	// 电子兑换券功能-兑换码生成方式
	public static HashMap<String, String> couponCodeMethod = new HashMap<String, String>();
	// 电子兑换券功能-兑换码有效期类型
	public static HashMap<String, String> couponCodeEffTypes = new HashMap<String, String>();
	// 电子兑换券功能-兑换码形式
	public static HashMap<String, String> couponCodeTypes = new HashMap<String, String>();
	// 电子兑换券功能-兑换券日志操作类型
	public static HashMap<String, String> couponLogOptTypes = new HashMap<String, String>();
	// 电子兑换券功能-业务后台参数
	public static HashMap<String, String> couponBusiBackParas = new HashMap<String, String>();
	// 电子兑换券功能-系统后台参数
	public static HashMap<String, String> couponSystemBackParas = new HashMap<String, String>();
	// 电子兑换券功能-订单状态
	public static HashMap<String, String> couponOrderStates = new HashMap<String, String>();
	// 电子兑换券功能-交易状态
	public static HashMap<String, String> couponTransStates = new HashMap<String, String>();
	// 电子兑换券功能-接入类型
	public static HashMap<String, String> couponJoinTypes = new HashMap<String, String>();
	// 电子兑换券功能-兑换码兑换方式
	public static HashMap<String, String> couponCodeExchangeTypes = new HashMap<String, String>();
	// 电子兑换券功能-商户兑换电话状态
	public static HashMap<String, String> couponMerTelStates = new HashMap<String, String>();
	// 定时任务-任务执行状态
	public static HashMap<String, String> timerTaskStates = new HashMap<String, String>();
	// 定时任务-所属平台名称
	public static HashMap<String, String> platNames = new HashMap<String, String>();
	// 电子兑换券功能-账单操作类型
	public static HashMap<String, String> stlStates = new HashMap<String, String>();
	// 电子兑换券功能-结算账期
	public static HashMap<String, String> stlCycles = new HashMap<String, String>();

	// ********************队列缓存************
	// 定时任务缓存
	public static HashMap<String, HFTask> hfTasks = new HashMap<String, HFTask>();
	// 定时任务监控缓存
	public static HashMap<String, HFTaskMnt> hfTaskMnts = new HashMap<String, HFTaskMnt>();

}