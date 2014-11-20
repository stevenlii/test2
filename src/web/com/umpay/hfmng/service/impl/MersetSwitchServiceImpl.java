/**
 * @Title: MersetSwitchServiceImpl.java
 * @Package com.umpay.hfmng.service.impl
 * @Description: TODO
 * @author MARCO
 * @date 2014-6-5 下午4:00:55
 * @version V1.0
 */

package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.MersetSwitchDao;
import com.umpay.hfmng.model.MersetSwitch;
import com.umpay.hfmng.service.MersetSwitchService;
import com.umpay.uniquery.util.StringUtil;
@Service
public class MersetSwitchServiceImpl implements MersetSwitchService {
	@Autowired
	private MersetSwitchDao mersetSwitchDao;
	public List<MersetSwitch> mersetValues() {
		return mersetSwitchDao.selectOpen();
	}
	
	public void updateMersetSwitch(String merids) throws Exception {
		//找出所有开启账单的商户
		List<MersetSwitch> openList = mersetSwitchDao.selectOpen();
		List<MersetSwitch> closedList = mersetSwitchDao.selectClosed();
		if(StringUtil.isEmpty(merids)){
			//将开启的修改为无效的
			mersetSwitchDao.updateSwitch(null);
			//新增所有开启的设置为关闭且有效的
			for(MersetSwitch s : openList){
				s.setConfigtime(TimeUtil.date8());
				Timestamp now = new Timestamp(System.currentTimeMillis());
				s.setIntime(now);
				s.setIsuse(2);
				s.setIsopen(4);
				s.setModtime(now);
				String seqid = "MS" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_MERSETSWITCH, 8);
				s.setSeqid(seqid);
				mersetSwitchDao.insertSwitch(s);
			}
		}else{
			String[] merid = merids.split(",");
			for(int i = 0; i < merid.length; i++){
				if(StringUtil.isEmpty(merid[i])){
					continue;
				}
				String mid = merid[i].trim();
				for(MersetSwitch s : openList){ //开启的默认不做操作
					if(mid.equals(s.getMerid().trim())){
						merid[i] = "";
						s.setMerid("");
						continue;
					}
				}
				if(StringUtil.isEmpty(merid[i])){
					continue;
				}
				for(MersetSwitch s : closedList){
					if(mid.equals(s.getMerid().trim())){//关闭的, 先将它设置为无效的, 再新增一条有效的
						mersetSwitchDao.updateSwitch2(s);
						merid[i] = "";
						s.setConfigtime(TimeUtil.date8());
						Timestamp now = new Timestamp(System.currentTimeMillis());
						s.setIntime(now);
						s.setIsuse(2);
						s.setIsopen(2);
						s.setModtime(now);
						String seqid = "MS" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_MERSETSWITCH, 8);
						s.setSeqid(seqid);
						mersetSwitchDao.insertSwitch(s);
						continue;
					}
				}
			}
			for(MersetSwitch s : openList){//将openlist中merid不为空的关闭
				if(StringUtil.isEmpty(s.getMerid())){
					continue;
				}else{
					mersetSwitchDao.updateSwitch3(s);
					s.setConfigtime(TimeUtil.date8());
					Timestamp now = new Timestamp(System.currentTimeMillis());
					s.setIntime(now);
					s.setIsuse(2);
					s.setIsopen(4);
					s.setModtime(now);
					String seqid = "MS" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_MERSETSWITCH, 8);
					s.setSeqid(seqid);
					mersetSwitchDao.insertSwitch(s);
				}
			}
			for(int i = 0; i < merid.length; i++){ //没有出现过得, 新增
				if(StringUtil.isEmpty(merid[i])){
					continue;
				}
				MersetSwitch s = new MersetSwitch();
				s.setConfigtime(TimeUtil.date8());
				Timestamp now = new Timestamp(System.currentTimeMillis());
				s.setIntime(now);
				s.setMerid(merid[i].trim());
				s.setIsuse(2);
				s.setIsopen(2);
				s.setModtime(now);
				String seqid = "MS" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_MERSETSWITCH, 8);
				s.setSeqid(seqid);
				mersetSwitchDao.insertSwitch(s);
			}
		}
	}

}
