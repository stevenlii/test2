package com.umpay.hfmng.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.GoodsBankDao;
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.dao.HenanDataSyncDao;
import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.HNSynTask;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.service.HenanDataSyncService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.TradeService;
import com.umpay.sso.org.User;

@Service
public class HenanDataSyncServiceImpl implements HenanDataSyncService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private TradeService tradeService;
	@Autowired
	private MerBankDao merBankDao;
	@Autowired
	private GoodsBankDao goodsBankDao;
	@Autowired
	private HenanDataSyncDao henanDataSyncDao;
	@Autowired
	private MessageService messageService;

	public String singleMerBankSync(String id) {
		Map<String,String> sendMap = new HashMap<String,String>();
		String retCode = null;
		String[] array = id.split("#&");
		//按照短信网关生成16位rpid的规则生成16位的流水号，前缀用PM
		String rpid = String.format("%2s%08x%06d", "PM", System.currentTimeMillis()/1000, makeInt4()%1000000);//确保%06d不超过6位
		sendMap.put(HFBusiDict.RPID, rpid);
		sendMap.put(HFBusiDict.MERID, array[0]);
		sendMap.put(HFBusiDict.BANKID, "XE371000");
		sendMap.put(HFBusiDict.MERNAME, array[1]);
//		log.info("--------------------------------------------------------------------------:"+sendMap.toString());
		retCode = tradeService.hnDataSync(sendMap);
		return retCode;
	}

	public String singleGoodsBankSync(String id) {
		Map<String,String> sendMap = new HashMap<String,String>();
		String retCode = null;
		String[] array = id.split("#&");
		//按照短信网关生成16位rpid的规则生成16位的流水号，前缀用PM
		String rpid = String.format("%2s%08x%06d", "PM", System.currentTimeMillis()/1000, makeInt4()%1000000);//确保%06d不超过6位
		sendMap.put(HFBusiDict.RPID, rpid);
		sendMap.put(HFBusiDict.MERID, array[0]);
		sendMap.put(HFBusiDict.GOODSID, array[1]);
		sendMap.put(HFBusiDict.GOODSNAME, array[2]);
		sendMap.put(HFBusiDict.BANKID, "XE371000");	
//		log.info("--------------------------------------------------------------------------:"+sendMap.toString());
		retCode = tradeService.hnDataSync(sendMap);
		return retCode;
	}
	
	public void batchDataSync(String sendFlag, User user) {
		if ("mer".equals(sendFlag)) {
			HNSynTask hnSynTask = new HNSynTask();
			String taskId = SequenceUtil.formatSequence(SequenceUtil.getInstance().getSequence4File("hnsync"), 8);
			hnSynTask.setTaskId(taskId);
			hnSynTask.setTaskType(4);//4代表批量商户同步
			hnSynTask.setCreator(ObjectUtil.trim(user.getId()));
			hnSynTask.setSynState(1);
			hnSynTask.setSynResult(0);
			hnSynTask.setResultDesc("");
			hnSynTask.setInTime(new Timestamp(System.currentTimeMillis()));
			hnSynTask.setModTime(new Timestamp(System.currentTimeMillis()));
			hnSynTask.setModUser(ObjectUtil.trim(user.getId()));
			henanDataSyncDao.insertSyncBatch(hnSynTask);
			
			int succ=0,fail=0;
			
			List<MerBank> merBankList = merBankDao.findAll();
			String fileName =  RandomFileName("mer");
			String succFileName = fileName+"-succ";
			String failFileName = fileName+"-fail";
			//获取下载路径
			String path = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+fileName+".txt";
			String successPath = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+succFileName+".txt";
			String failPath = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+failFileName+".txt";
			String flag1=",",flag2=",";
			String filesName = "";
			for (int i = 0; i < merBankList.size(); i++) {
				StringBuffer context = new StringBuffer();
				String bankId = ObjectUtil.trim(merBankList.get(i).getBankId());
				String merId = ObjectUtil.trim(merBankList.get(i).getMerId());
				String merName = ObjectUtil.trim(merBankList.get(i).getMerName());
				context.append("150104").append(",").append(bankId).append(",").append(merId).append(",").append(merName).append("\r\n");
				try {
					writeFile(path, context.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Map<String,String> sendMap = new HashMap<String,String>();
				String retCode = null;
				//按照短信网关生成16位rpid的规则生成16位的流水号，前缀用PM
				String rpid = String.format("%2s%08x%06d", "PM", System.currentTimeMillis()/1000, makeInt4()%1000000);//确保%06d不超过6位
				sendMap.put(HFBusiDict.RPID, rpid);
				sendMap.put(HFBusiDict.MERID, merId);
				sendMap.put(HFBusiDict.BANKID, "XE371000");
				sendMap.put(HFBusiDict.MERNAME, merName);
				retCode = tradeService.hnDataSync(sendMap);
				StringBuffer sb = new StringBuffer();
				sb.append("150104").append(",").append(bankId).append(",").append(merId).append(",").append(merName);
				if ("0000".equals(retCode)) {//成功
					succ++;
					try {
						flag1=",s";
						writeFile(successPath, sb.append("。同步成功").append("\r\n").toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					fail++;
					try {
						flag2=",f";
						if (null == retCode) {
							writeFile(failPath, sb.append("。同步失败：trade未返回信息").append("\r\n").toString());
						} else {
							writeFile(failPath, sb.append("。同步失败，移动返回码：").append(retCode).append("\r\n").toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				writeFile(successPath, "成功处理行数："+succ+"行。");
				writeFile(failPath, "未成功处理行数："+fail+"行。");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			filesName+=fileName+flag1+flag2;

			Map<String, Object> mapWhere = new HashMap<String, Object>();
			mapWhere.put("taskId", taskId);
			mapWhere.put("synState", 2);
			mapWhere.put("resultDesc", filesName);
			mapWhere.put("modTime", new Timestamp(System.currentTimeMillis()));
			if (",s".equals(flag1) && ",".equals(flag2)) {//1:处理成功
				mapWhere.put("synResult", 1);
			} else if (",s".equals(flag1) && ",f".equals(flag2)) {//2：部分失败
				mapWhere.put("synResult", 2);
			} else if (",".equals(flag1) && ",f".equals(flag2)) {//2：处理失败
				mapWhere.put("synResult", 3);
			}
			henanDataSyncDao.updateSynTask(mapWhere);

			
		} else if ("goods".equals(sendFlag)) {
			HNSynTask hnSynTask = new HNSynTask();
			String taskId = SequenceUtil.formatSequence(SequenceUtil.getInstance().getSequence4File("hnsync"), 8);
			hnSynTask.setTaskId(taskId);
			hnSynTask.setTaskType(5);//5代表批量商品同步
			hnSynTask.setCreator(ObjectUtil.trim(user.getId()));
			hnSynTask.setSynState(1);
			hnSynTask.setSynResult(0);
			hnSynTask.setResultDesc("");
			hnSynTask.setInTime(new Timestamp(System.currentTimeMillis()));
			hnSynTask.setModTime(new Timestamp(System.currentTimeMillis()));
			hnSynTask.setModUser(ObjectUtil.trim(user.getId()));
			henanDataSyncDao.insertSyncBatch(hnSynTask);
			
			int succ=0,fail=0;
			
			List<GoodsBank> goodsBankList = goodsBankDao.findAll();
			String fileName =  RandomFileName("goods");
			String succFileName = fileName+"-succ";
			String failFileName = fileName+"-fail";
			//获取下载路径
			String path = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+fileName+".txt";
			String successPath = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+succFileName+".txt";
			String failPath = ObjectUtil.trim(messageService.getSystemParam("HNSync.DownloadFilePath"))+failFileName+".txt";
			String flag1=",",flag2=",";
			String filesName = "";
			for (int i = 0; i < goodsBankList.size(); i++) {
				//TODO  只同步开通状态的（新增和续费只要开通一个就可以）??
				StringBuffer context = new StringBuffer();
				String bankId = ObjectUtil.trim(goodsBankList.get(i).getBankId());
				String merId = ObjectUtil.trim(goodsBankList.get(i).getMerId());
				String goodsId = ObjectUtil.trim(goodsBankList.get(i).getGoodsId());
				
				Map<String, Object> goodNameMap = HfCacheUtil.getCache().getGoodsInfoMap();
				GoodsInfo goodsInfo = (GoodsInfo) goodNameMap.get(merId + "-" + goodsId);
//				Map<String, String> mapWhere = new HashMap<String, String>();
//				mapWhere.put("merId", merId);
//				mapWhere.put("goodsId", goodsId);
//				GoodsInfo goodsInfo = (GoodsInfo) goodsInfoDao.get(mapWhere);
				if (null!=goodsInfo) {
					String goodsName = ObjectUtil.trim(goodsInfo.getGoodsName());
					if (!"undefined".equals(goodsName)) {
						context.append("150105").append(",").append(bankId).append(",").append(goodsName).append(",").append(goodsId).append(",").append(merId).append("\r\n");
						try {
							writeFile(path, context.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						Map<String,String> sendMap = new HashMap<String,String>();
						String retCode = null;
						//按照短信网关生成16位rpid的规则生成16位的流水号，前缀用PM
						String rpid = String.format("%2s%08x%06d", "PM", System.currentTimeMillis()/1000, makeInt4()%1000000);//确保%06d不超过6位
						sendMap.put(HFBusiDict.RPID, rpid);
						sendMap.put(HFBusiDict.MERID, merId);
						sendMap.put(HFBusiDict.GOODSID, goodsId);
						sendMap.put(HFBusiDict.GOODSNAME, goodsName);
						sendMap.put(HFBusiDict.BANKID, "XE371000");	
						retCode = tradeService.hnDataSync(sendMap);
						StringBuffer sb = new StringBuffer();
						sb.append("150105").append(",").append(bankId).append(",").append(goodsName).append(",").append(goodsId).append(",").append(merId);
						if ("0000".equals(retCode)) {//成功
							succ++;
							try {
								flag1=",s";
								writeFile(successPath, sb.append("。同步成功").append("\r\n").toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							fail++;
							try {
								flag2=",f";
								if (null == retCode) {
									writeFile(failPath, sb.append("。同步失败：trade未返回信息").append("\r\n").toString());
								} else {
									writeFile(failPath, sb.append("。同步失败，移动返回码：").append(retCode).append("\r\n").toString());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						fail++;
						try {
							flag2=",f";
							StringBuffer sb = new StringBuffer();
							sb.append("150105").append(",").append(bankId).append(",").append(goodsName).append(",").append(goodsId).append(",").append(merId);
							writeFile(failPath, sb.append("。未找到商品名称，失败").append("\r\n").toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					fail++;
					flag2=",f";
					StringBuffer sb = new StringBuffer();
					sb.append("150105").append(",").append(bankId).append(",").append("").append(",").append(goodsId).append(",").append(merId).append("。未找到商品名称，失败").append("\r\n");
					StringBuffer sb2 = new StringBuffer();
					sb2.append("150105").append(",").append(bankId).append(",").append("").append(",").append(goodsId).append(",").append(merId).append("\r\n");
					try {
						writeFile(path, sb2.toString());
						writeFile(failPath, sb.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			filesName+=fileName+flag1+flag2;

			try {
				writeFile(successPath, "成功处理行数："+succ+"行。");
				writeFile(failPath, "未成功处理行数："+fail+"行。");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Map<String, Object> mapWhere = new HashMap<String, Object>();
			mapWhere.put("taskId", taskId);	
			mapWhere.put("synState", 2);
			mapWhere.put("resultDesc", filesName);
			mapWhere.put("modTime", new Timestamp(System.currentTimeMillis()));
			if (",s".equals(flag1) && ",".equals(flag2)) {//1:处理成功
				mapWhere.put("synResult", 1);
			} else if (",s".equals(flag1) && ",f".equals(flag2)) {//2：部分失败
				mapWhere.put("synResult", 2);
			} else if (",".equals(flag1) && ",f".equals(flag2)) {//2：处理失败
				mapWhere.put("synResult", 3);
			}
			henanDataSyncDao.updateSynTask(mapWhere);

		}
	}
	
	/**
	 * 根据当前时间生成随机文件名
	 * @return
	 */
	private String RandomFileName(String flag) {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String formatDate = format.format(new Date());
		if ("mer".equals(flag)) {
			return new StringBuffer().append(formatDate).append("AllMerBank").toString();
		} else {
			return new StringBuffer().append(formatDate).append("AllGoodsBank").toString();
		}
	}
	
	/**
	 * 写文件
	 * @param path
	 * @param content
	 */
	private void writeFile(String path, String content) throws Exception {
		String s = new String();
		String s1 = new String();
		File f = new File(path);
		if (f.exists()) {
//			System.out.println("文件存在");
		} else {
			log.info("文件不存在，正在创建...");
			if (f.createNewFile()) {
				log.info("文件创建成功！");
			} else {
				log.info("文件创建失败！");
			}
		}
		BufferedReader input = new BufferedReader(new FileReader(f));
		while ((s = input.readLine()) != null) {
			s1 += s + "\r\n";
		}
		input.close();
		s1 += content;
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "GB2312"));
		output.write(s1);
		output.close();
	}

	private static long sequence_number = 0; 

	public synchronized static int makeInt4() {//<==> makeSeq()L;
		return (int) ((sequence_number++) % Integer.MAX_VALUE + 1);
	}

	public HNSynTask get(Map<String, String> mapWhere) {
		return henanDataSyncDao.get(mapWhere);
	}

	public String checkAll() {
		List<HNSynTask> hnSynTaskList = henanDataSyncDao.findAll();
		for (int i = 0; i < hnSynTaskList.size(); i++) {
			int synState = hnSynTaskList.get(i).getSynState();
			if (0 == synState || 1 == synState) {
				return "true";
			}
		}
		return "false";
	}
}