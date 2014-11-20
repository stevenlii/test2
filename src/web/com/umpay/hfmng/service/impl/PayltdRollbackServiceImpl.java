/** *****************  JAVA头文件说明  ****************
 * file name  :  PayltdRollbackServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-2-13
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.PayltdRollback;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.PayltdRollbackService;
import com.umpay.hfmng.service.RestService;


/** ******************  类说明  *********************
 * class       :  PayltdRollbackServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class PayltdRollbackServiceImpl implements PayltdRollbackService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private RestService restService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private AuditDao auditDao;

	/** ********************************************
	 * method name   : auditNotPass 
	 * modified      : xuhuafeng ,  2014-2-13
	 * @see          : @see com.umpay.hfmng.service.PayltdRollbackService#auditNotPass(java.lang.String, java.lang.String)
	 * ********************************************/
	public String auditNotPass(String id, String resultDesc) throws Exception {
		String res = "1"; // 返回结果初始化为成功
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		Audit audit = auditService.load(map);
		String checkResult = checkAuditData(audit);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		audit.setModUser(LoginUtil.getUser().getId());
		audit.setResultDesc(resultDesc);
		audit.setState("1");
		if(auditDao.updateState(audit) == 1){
			log.info("更新审核表成功");
		}else{
			throw new DAOException("更新审核表失败");
		}
		log.info("审核不通过成功");
		return res;
	}

	/** ********************************************
	 * method name   : auditPass 
	 * modified      : xuhuafeng ,  2014-2-13
	 * @see          : @see com.umpay.hfmng.service.PayltdRollbackService#auditPass(java.lang.String, java.lang.String)
	 * ********************************************/
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String auditPass(String id, String resultDesc) throws Exception {
		String res = "1"; // 返回结果初始化为成功
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		Audit audit = auditService.load(map);
		String checkResult = checkAuditData(audit);
		if(!"SUCCESS".equals(checkResult)){
			return checkResult;
		}
		//校验是否跨月
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
		String nowTime = df.format(new Date());
		String inTime = df.format(audit.getInTime());
		if(!nowTime.equals(inTime)){
			res = "此为上月数据，审核不能通过";
			log.info(res);
			return res;
		}
		
		audit.setModUser(LoginUtil.getUser().getId());
		audit.setResultDesc(resultDesc);
		audit.setState("2");
		if(auditDao.updateState(audit) == 1){
			log.info("更新审核表成功");
		}else{
			throw new DAOException("更新审核表失败");
		}
		PayltdRollback payltdRollback = (PayltdRollback) JsonHFUtil.getObjFromJsonArrStr(audit
					.getModData(), PayltdRollback.class);
		if("1".equals(audit.getAuditType())){
			int money = (int)(Double.valueOf(payltdRollback.getAmt()) * 100);
			payltdRollback.setAmt(String.valueOf(money));
			res = restService.revokePayLtd(payltdRollback);
			log.info("限额回滚成功");
		}else{
			res = "未知的审核类型";
			log.info(res);
			return res;
		}
		log.info("审核通过成功");
		return res;
	}

	/** ********************************************
	 * method name   : queryPayed 
	 * modified      : xuhuafeng ,  2014-2-13
	 * @see          : @see com.umpay.hfmng.service.PayltdRollbackService#queryPayed(java.lang.String)
	 * ********************************************/
	public Map<String, String> queryPayed(String mobileId) throws Exception {
		Map<String, String> res = new HashMap<String, String>();
		res = restService.queryPayed(mobileId);
		if("yes".equals(res.get("result"))){
			log.info("查询当月累计额成功");
		}else{
			log.info("查询当月累计额失败");
		}
		return res;
	}

	/** ********************************************
	 * method name   : save 
	 * modified      : xuhuafeng ,  2014-2-13
	 * @see          : @see com.umpay.hfmng.service.PayltdRollbackService#save(com.umpay.hfmng.model.PayltdRollback)
	 * ********************************************/
	public String save(PayltdRollback payltdRollback) throws Exception {
		String result="1";                      //操作结果 1表示成功, 0 表示失败
		payltdRollback.trim();
		Map<String, String> mapWhere = new HashMap<String, String>();
		String tableName = "MOBILE.TXEUSERLTD";
		mapWhere.put("ixData", payltdRollback.getMobileId());
		mapWhere.put("tableName", tableName);
		List list = auditDao.getCheckExactlyFromAudit(mapWhere);
		if(list != null && list.size() != 0){
			result = "此手机号还有未审核数据，请先去审核页面审核后再提交";
			log.info(result);
			return result;
		}
		
		Audit audit = new Audit();
		audit.setTableName(tableName);
		audit.setAuditType("1");                 // 审核类型 1：新增
		String jsonString = JsonHFUtil.getJsonArrStrFrom(payltdRollback);
		audit.setModData(jsonString);
		audit.setCreator(LoginUtil.getUser().getId());    //创建人 是当前登录用户
		audit.setIxData(payltdRollback.getMobileId());
		audit.setIxData2(payltdRollback.getMerId()+"-"+payltdRollback.getGoodsId());
		audit.setDesc(payltdRollback.getAmt()); //保存回滚了多少的金额
		
		auditDao.insert(audit);
		log.info("新增审核信息成功"+audit);
		return result;
	}
	
	private String checkAuditData(Audit audit){
		if(audit == null){
			log.error("不存在的审核数据");
			throw new DAOException("不存在的审核数据");
		}
		audit.trim();  //去空格
		if(!"0".equals(audit.getState())){
			log.error("该数据已审核，不能再次审核"+audit);
			return "该数据已审核，不能再次审核，请刷新后再试";
		}
		if(!auditService.checkAuditUser(audit.getCreator())){
			return "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。";
		}
		return "SUCCESS";
	}

}
