package com.umpay.hfmng.common;

/**
 * ****************** 类说明 *********************
 * 
 * @class : Const
 * @author : yangwr
 * @version : 1.0 description : 常量类
 * @see : ***********************************************
 */
public class Const {

	/** 话费信息审核表采序列号文件名 */
	public static String SEQ_FILENAME_AUDIT = "hfaudit";
	/** 文档信息表采序列号文件名 */
	public static String SEQ_FILENAME_DOC = "hfdoc";
	/** 操作日志表采序列号文件名 */
	public static String SEQ_FILENAME_OPERLOG = "operlog";
	/** 兑换券信息表采序列号文件名 */
	public static String SEQ_FILENAME_COUPONINF = "couponinf";
	/** 兑换券规则表采序列号文件名 */
	public static String SEQ_FILENAME_COUPONRULE = "couponrule";
	/** 兑换券批次表采序列号文件名 */
	public static String SEQ_FILENAME_COUPONBATCH = "couponbatch";
	/** 兑换券操作日志序列号文件名 */
	public static String SEQ_FILENAME_COUPONLOG = "couponlog";
	/** 商户商品报备信息表序列号文件名 */
	public static String SEQ_FILENAME_REPORTINF = "reportinf";
	/** 商户商品报备操作表序列号文件名 */
	public static String SEQ_BACKUPID = "backupid";
	/** 定时任务用序号 */
	public static String SEQ_TASK = "taskSeq";
	/** 商户兑换电话表采序列号文件名 */
	public static String SEQ_FILENAME_COUPONMERTEL = "couponmertel";
	/** 商户账单开关序列号文件名 */
	public static String SEQ_FILENAME_MERSETSWITCH = "mersetswitch";
	/** 兑换券状态_未启用 */
	public static final Integer COUPON_INFSTATE_NOENABLE = 4;
	/** 兑换券状态_启用 */
	public static final Integer COUPON_INFSTATE_ENABLE = 2;
	/** 兑换券状态_待启用 */
	public static final Integer COUPON_INFSTATE_INIT = 0;

	/** 兑换券规则状态_待启用 */
	public static final Integer COUPON_RULESTATE_NOENABLE = 0;
	/** 兑换券规则状态_启用 */
	public static final Integer COUPON_RULESTATE_ENABLE = 2;
	/** 兑换券规则状态_暂停 */
	public static final Integer COUPON_RULESTATE_PAUSE = 3;
	/** 兑换券规则状态_停止 */
	public static final Integer COUPON_RULESTATE_STOP = 4;

	/** 兑换码批量导入批次状态_未启用 */
	public static final Integer COUPON_BATCHSTATE_NOENABLE = 0;
	/** 兑换码批量导入批次状态_启用 */
	public static final Integer COUPON_BATCHSTATE_ENABLE = 2;
	/** 兑换码批量导入批次状态_停止 */
	public static final Integer COUPON_BATCHSTATE_STOP = 4;
	/** 兑换码批量导入批次状态_售罄 */
	public static final Integer COUPON_BATCHSTATE_EMPTY = 3;

	/** 兑换码形式_电子码 */
	public static final String COUPON_CODEMODEL_DZM = "ECODE";
	/** 兑换码形式_二维码 */
	public static final String COUPON_CODEMODEL_EWM = "TDCODE";

	/** 兑换码状态_未使用 */
	public static final Integer COUPON_CODESTATE_NOUSE = 3;
	/** 兑换码状态_已使用 */
	public static final Integer COUPON_CODESTATE_USE = 4;
	/** 兑换码状态_已失效 */
	public static final Integer COUPON_CODESTATE_FAIL = 5;
	/** 兑换码状态_已注销 */
	public static final Integer COUPON_CODESTATE_CANCEL = 9;
	/** 兑换码状态_停用 */
	public static final Integer COUPON_CODESTATE_STOP = -1;
	/** 兑换码状态_初始 */
	public static final Integer COUPON_CODESTATE_INIT = 0;
	/** 兑换码状态_审核通过可销售 */
	public static final Integer COUPON_CODESTATE_START = 1;
	/** 兑换码状态_已支付 */
	public static final Integer COUPON_CODESTATE_PAY = 2;

	/** 兑换码生成方式_本平台数字字母随机生成 */
	public static final Integer COUPON_CODEGENMETHOD_LOCALCN = 0;
	/** 兑换码状态_商户提供 */
	public static final Integer COUPON_CODEGENMETHOD_UNLOCAL = 1;
	/** 兑换码生成方式_本平台全数字自动生成 */
	public static final Integer COUPON_CODEGENMETHOD_LOCALN = 2;

	/** 兑换码有效期类型_时间段 */
	public static final Integer COUPON_CODEEFFTYPE_DATE = 1;
	/** 兑换码有效期类型_售出后有效天数 */
	public static final Integer COUPON_CODEEFFTYPE_DAY = 2;

	/** 审核状态_待审核 */
	public static final String AUDIT_STATE_WAIT = "0";
	/** 审核状态_审核通过 */
	public static final String AUDIT_STATE_PASS = "2";
	/** 审核状态_审核拒绝 */
	public static final String AUDIT_STATE_NOPASS = "1";

	/** 日志操作类型-新增 */
	public static final String LOG_OPT_CREATE = "CREATE";
	/** 日志操作类型-修改 */
	public static final String LOG_OPT_MODIFY = "MODIFY";
	/** 日志操作类型-审核 */
	public static final String LOG_OPT_AUDIT = "AUDIT";

	/** 日志操作结果_成功 */
	public static final String LOG_RES_SUCC = "SUCC";
	/** 日志操作结果_失败 */
	public static final String LOG_RES_FAIL = "FAIL";

	/** 审核类型_新增 */
	public static final String AUDIT_TYPE_ADD = "1";
	/** 审核类型_修改 */
	public static final String AUDIT_TYPE_MOD = "2";
	/** 审核类型_启用 */
	public static final String AUDIT_TYPE_ENABLE = "3";
	/** 审核类型_禁用 */
	public static final String AUDIT_TYPE_NOENABLE = "4";
	/** 审核类型_未知 */
	public static final String AUDIT_TYPE_UNKNOWN = "0";

	/** 任务触发方式_自动 */
	public static final int TSEND_TYPE_AUTO = 1;
	/** 任务触发方式_手动 */
	public static final int TSEND_TYPE_MANUAL = 2;

	/** 任务错误是否重试 _否 */
	public static final int TISTRY_YES = 2;
	/** 任务错误是否重试 _是 */
	public static final int TISTRY_NO = 4;

	/** 任务执行状态_初始 */
	public static final int TASK_STATE_START = 0;
	/** 任务执行状态_触发失败 */
	public static final int TASK_STATE_SENDERROR = 1;
	/** 任务执行状态_触发成功 */
	public static final int TASK_STATE_SENDSUCCEED = 2;
	/** 任务执行状态_反馈超时 */
	public static final int TASK_STATE_FBTIMEOUT = 3;
	/** 任务执行状态_执行失败 */
	public static final int TASK_STATE_RUNERROR = 4;
	/** 任务执行状态_执行成功 */
	public static final int TASK_STATE_RUNSUCCEED = 5;

	/** 对账文件状态_初始化 */
	public static final int CKFILE_STATE_INIT = 1;

	/** 对账文件状态_已校验 */
	public static final int CKFILE_STATE_CHECKED = 2;

	/** 对账文件状态_已转换 */
	public static final int CKFILE_STATE_CONVERTED = 3;

	/** 对账文件状态_已完成 */
	public static final int CKFILE_STATE_FINISH = 4;

	/** 对账文件状态_已完成(无交易) */
	public static final int CKFILE_STATE_FINISH_N = 5;

	/** 对账文件类型_日交易文件 */
	public static final int CKFILE_TYPE_JY_DAY = 1;

	/** 对账文件类型_日清算文件 */
	public static final int CKFILE_TYPE_QS_DAY = 2;

	/** 对账文件类型_月清算文件 */
	public static final int CKFILE_TYPE_QS_MON = 3;

	/** 商户评分_商户交易数据文件名中的日 dd */
	public final static String TRADE_GRADE_DAY = "02";
	/** 商户评分_数据库评分信息表中各个评分的默认值 */
	public final static int MERGRADE_DEFAULT = -99;
	/** 商户评分_精确到小数点后的位数 */
	public final static int MERGRADE_SCALE = 2;
	/** 商户评分_评分等级文档的文档类型标号 */
	public final static int MERGRADE_DOC_TYPE = 1;

	/** 返回码——任务处理成功 */
	public static final String RETCODE_TIMER_SUCCESS = "0000";
	/** 返回码——任务接收成功 */
	public static final String RETCODE_TIMER_RECSUCC = "8888";
	/** 返回码——任务接收失败 */
	public static final String RETCODE_TIMER_FAILED = "1234";

	/** CouponMerSet实体int类型变量初始化值 */
	public static final int COUPON_MERSET_INIT_NUM = -99999999;
}
