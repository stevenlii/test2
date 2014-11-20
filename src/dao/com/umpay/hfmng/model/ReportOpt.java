package com.umpay.hfmng.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * 
* @ClassName: ReportOpt
* @Description: 报备操作实体类
* @author wangyuxin
* @date 2014-7-11 下午04:00:05
*
 */
public class ReportOpt implements Entity {
	private String backup_oper_id;
	private String mer_id;
	private String goods_id;
	private String bank_id;
	private String batch_code;
	private String operr_id;
	private int backup_oper_stat;
	private Timestamp submit_time;
	private Timestamp ver_time;
	private Timestamp backup_time;
	private int backup_type;
	private String oper_reason;
	private int is_valid;
	private Timestamp in_time;
	private Timestamp updt_time;
	private String mod_opr_id;

	@Override
	public String toString() {
		return "ReportOpt [backup_oper_id=" + backup_oper_id
				+ ", backup_oper_stat=" + backup_oper_stat + ", backup_time="
				+ backup_time + ", backup_type=" + backup_type + ", bank_id="
				+ bank_id + ", batch_code=" + batch_code + ", goods_id="
				+ goods_id + ", in_time=" + in_time + ", is_valid=" + is_valid
				+ ", mer_id=" + mer_id + ", mod_opr_id=" + mod_opr_id
				+ ", oper_reason=" + oper_reason + ", operr_id=" + operr_id
				+ ", submit_time=" + submit_time + ", updt_time=" + updt_time
				+ ", ver_time=" + ver_time + "]";
	}

	public String getBackup_oper_id() {
		return backup_oper_id;
	}

	public void setBackup_oper_id(String backupOperId) {
		backup_oper_id = backupOperId;
	}

	public String getMer_id() {
		return mer_id;
	}

	public void setMer_id(String merId) {
		mer_id = merId;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goodsId) {
		goods_id = goodsId;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bankId) {
		bank_id = bankId;
	}

	public String getBatch_code() {
		return batch_code;
	}

	public void setBatch_code(String batchCode) {
		batch_code = batchCode;
	}

	public String getOperr_id() {
		return operr_id;
	}

	public void setOperr_id(String operrId) {
		operr_id = operrId;
	}

	public int getBackup_oper_stat() {
		return backup_oper_stat;
	}

	public void setBackup_oper_stat(int backupOperStat) {
		backup_oper_stat = backupOperStat;
	}

	public Timestamp getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(Timestamp submitTime) {
		submit_time = submitTime;
	}

	public Timestamp getVer_time() {
		return ver_time;
	}

	public void setVer_time(Timestamp verTime) {
		ver_time = verTime;
	}

	public Timestamp getBackup_time() {
		return backup_time;
	}

	public void setBackup_time(Timestamp backupTime) {
		backup_time = backupTime;
	}

	public int getBackup_type() {
		return backup_type;
	}

	public void setBackup_type(int backupType) {
		backup_type = backupType;
	}

	public String getOper_reason() {
		return oper_reason;
	}

	public void setOper_reason(String operReason) {
		oper_reason = operReason;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int isValid) {
		is_valid = isValid;
	}

	public Timestamp getIn_time() {
		return in_time;
	}

	public void setIn_time(Timestamp inTime) {
		in_time = inTime;
	}

	public Timestamp getUpdt_time() {
		return updt_time;
	}

	public void setUpdt_time(Timestamp updtTime) {
		updt_time = updtTime;
	}

	public String getMod_opr_id() {
		return mod_opr_id;
	}

	public void setMod_opr_id(String modOprId) {
		mod_opr_id = modOprId;
	}

	public void trim() {
		if (this.backup_oper_id != null) {
			this.setBackup_oper_id(StringUtils.trim(this.backup_oper_id));
		}
		if (this.mer_id != null) {
			this.setMer_id(StringUtils.trim(this.mer_id));
		}
		if (this.goods_id != null) {
			this.setGoods_id(StringUtils.trim(this.goods_id));
		}
		if (this.bank_id != null) {
			this.setBank_id(StringUtils.trim(this.bank_id));
		}
		if (this.batch_code != null) {
			this.setBatch_code(StringUtils.trim(this.batch_code));
		}
		if (this.operr_id != null) {
			this.setOperr_id(StringUtils.trim(this.operr_id));
		}
		if (this.oper_reason != null) {
			this.setOper_reason(StringUtils.trim(this.oper_reason));
		}
		if (this.mod_opr_id != null) {
			this.setMod_opr_id(StringUtils.trim(this.mod_opr_id));
		}

	}
}
