package com.umpay.hfmng.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.service.XERevokeService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.TradeService;

@Service
public class XERevokeServiceImpl implements XERevokeService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private TradeService tradeService;
	@Autowired
	private MessageService messageService;

	/**
	 * ********************************************
	 * method name   : batchRevokeFromFile 
	 * description   : 批量发起小额解冻请求（来自文件）
	 * @return       : String
	 * @param        : @param file
	 * @param        : @throws DataAccessException IOException 
	 * @author       : chenwei
	 * @date         : 2013-7-11 
	 * ********************************************
	 */
	public String batchRevokeFromFile(MultipartFile file, String batchProv) throws DataAccessException, IOException {
		Map<String,String> sendMap = new HashMap<String,String>();
		int count = 0;//文件中记录总条数
		int success = 0;//成功解冻的记录总条数
		int fail = 0;//除了发解冻之外的失败记录总条数
		int failSucc = 0;//发了解冻，但是解冻失败的总条数

		String line = null;//文件中的格式为第一位为交易流水号，第二位为手机号，中间以逗号隔开
		InputStream input = null;
		BufferedReader reader = null;
		try{
			input = file.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(input);
			reader = new BufferedReader(inputStreamReader);
			line = reader.readLine();
		} catch(IOException e){
			return "读取文件发生错误！";
		}
		
		if (batchProv.startsWith("BJ")) {
			//解冻结果文件 eg BJ_REVOKE_20130710112233.txt
			String resFileName = "BJ_" + TimeUtil.date8() + TimeUtil.time6() + "_" + file.getOriginalFilename();
			String fileURL = ObjectUtil.trim(messageService.getSystemParam("XEREVOKE.DownloadFilePath"));
			if ("BJHB".equals(batchProv)) {
				fileURL = ObjectUtil.trim(messageService.getSystemParam("BJHBREVOKE.DownloadFilePath"));
			}
			File resFile = new File(fileURL + resFileName);
			if (!resFile.exists()) {
				resFile.createNewFile();
			}
			FileWriter fw = new FileWriter(resFile);

			String retCode = null;
			while(line != null){
				if ("".equals(ObjectUtil.trim(line))) {
					line = reader.readLine();
					continue;
				}
				String[] revokes = line.split(",");
				if (revokes.length != 3 || ObjectUtil.trim(revokes[1]).length() != 11) {
					fw.write(line+",此记录格式非法"+"\r\n");
					line = reader.readLine();
					count++;
					fail++;
					continue;
				}
				String mobileId = ObjectUtil.trim(revokes[1]);//手机号
				String transeq = ObjectUtil.trim(revokes[0]);//流水号
				String amount = ObjectUtil.trim(revokes[2]);//金额
				sendMap.put(HFBusiDict.RPID, "B"+mobileId.substring(2)+TimeUtil.time6());
				sendMap.put(HFBusiDict.REQDATE, TimeUtil.date8());
				sendMap.put(HFBusiDict.REQTIME, TimeUtil.time6());
				sendMap.put(HFBusiDict.MOBILEID, mobileId);
				sendMap.put(HFBusiDict.TRANSEQ, transeq);
				sendMap.put(HFBusiDict.AMT, amount);
				sendMap.put(HFBusiDict.SERVICESEQUENCE, mobileId.substring(3)+TimeUtil.time6());
				sendMap.put(HFBusiDict.BANKID, "XE010000");
				if ("BJHB".equals(batchProv)) {
					sendMap.put(HFBusiDict.USERTYPE, "8912");
				}

				try {
					retCode = tradeService.xeRevoke(sendMap, batchProv);
					if(!retCode.equals("0000")){
						failSucc++;
						fw.write(line+","+retCode+"\r\n");
					}else{
						success++;
					}
					Thread.sleep(50);
					line = reader.readLine();
					count++;
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					fw.write(line+",其他原因导致失败"+"\r\n");
					line = reader.readLine();
					fail++;
					count++;
					continue;
				}
			}
			
			fw.write("****总结****："+"\r\n");
			fw.write("共处理记录数："+count+"；"+"\r\n");
			fw.write("解冻成功记录数："+success+"；"+"\r\n");
			fw.write("解冻失败记录数："+failSucc+"；"+"\r\n");
			fw.write("其他原因失败记录数："+fail+"。"+"\r\n");

			fw.flush();
			fw.close();
			return "解冻成功笔数："+success+"；失败笔数："+(failSucc+fail);	
		} else if ("YN".equals(batchProv)) {
			//解冻结果文件 eg YN_REVOKE_20130710112233.txt
			//格式：交易日期,联动交易流水号,移动交易流水号,手机号,解冻金额
			String resFileName = "YN_" + TimeUtil.date8() + TimeUtil.time6() + "_" + file.getOriginalFilename();
			String fileURL = ObjectUtil.trim(messageService.getSystemParam("XEREVOKE.DownloadFilePath"));
			File resFile = new File(fileURL + resFileName);
			if (!resFile.exists()) {
				resFile.createNewFile();
			}
			FileWriter fw = new FileWriter(resFile);

			String retCode = null;
			while(line != null){
				if ("".equals(ObjectUtil.trim(line))) {
					line = reader.readLine();
					continue;
				}
				String[] revokes = line.split(",");
				if (revokes.length != 5 || ObjectUtil.trim(revokes[3]).length() != 11) {
					fw.write(line+",此记录格式非法"+"\r\n");
					line = reader.readLine();
					count++;
					fail++;
					continue;
				}
				String platdate = ObjectUtil.trim(revokes[0]);//交易日期
				String transeq = ObjectUtil.trim(revokes[1]);//联动交易流水号
				String bosstrans = ObjectUtil.trim(revokes[2]);//移动交易流水号
				String mobileId = ObjectUtil.trim(revokes[3]);//手机号
				String amount = ObjectUtil.trim(revokes[4]);//解冻金额
				sendMap.put(HFBusiDict.RPID, "B"+mobileId.substring(2)+TimeUtil.time6());
				sendMap.put(HFBusiDict.REQDATE, TimeUtil.date8());
				sendMap.put(HFBusiDict.REQTIME, TimeUtil.time6());
				sendMap.put(HFBusiDict.MOBILEID, mobileId);
				sendMap.put(HFBusiDict.TRANSEQ, transeq);
				sendMap.put(HFBusiDict.AMT, amount);
				sendMap.put(HFBusiDict.SERVICESEQUENCE, mobileId.substring(3)+TimeUtil.time6());
				sendMap.put(HFBusiDict.BANKID, "XE871000");
				sendMap.put(HFBusiDict.PLATDATE, platdate);
				sendMap.put(HFBusiDict.BANKTRACE, bosstrans);

				try {
					retCode = tradeService.xeRevoke(sendMap, batchProv);
					if(!retCode.equals("0000")){
						failSucc++;
						int returnCode = Integer.parseInt(retCode);
						switch (returnCode) {
						case 86001032:
							fw.write(line+","+retCode+"（交易信息不存在）\r\n");
							break;
						case 86011042:
							fw.write(line+","+retCode+"（解冻金额和回滚金额不一致）\r\n");
							break;
						case 86011900:
							fw.write(line+","+retCode+"（获取省代码异常）\r\n");
							break;
						case 86011901:
							fw.write(line+","+retCode+"（解冻失败，云南小额支付前置返回用户余额不足）\r\n");
							break;
						case 86011902:
							fw.write(line+","+retCode+"（解冻失败）\r\n");
							break;
						case 86001054:
							fw.write(line+","+retCode+"（风控回滚失败）\r\n");
							break;
						default:
							fw.write(line+","+retCode+"（其他返回码错误导致的失败）\r\n");
							break;
						}
					}else{
						success++;
					}
					Thread.sleep(50);
					line = reader.readLine();
					count++;
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					fw.write(line+",其他原因导致失败"+"\r\n");
					line = reader.readLine();
					fail++;
					count++;
					continue;
				}
			}
			
			fw.write("****总结****："+"\r\n");
			fw.write("共处理记录数："+count+"；"+"\r\n");
			fw.write("解冻成功记录数："+success+"；"+"\r\n");
			fw.write("解冻失败记录数："+failSucc+"；"+"\r\n");
			fw.write("其他原因失败记录数："+fail+"。"+"\r\n");

			fw.flush();
			fw.close();
			return "解冻成功笔数："+success+"；失败笔数："+(failSucc+fail);
		} else {
			return null;
		}
	}

	/**
	 * ********************************************
	 * method name   : listFile 
	 * description   : 获取目录下面的所有文件名
     * @param        : path 文件路径  
     * @param        : suffix 后缀名, 为空则表示所有文件
     * @param        : isdepth 是否遍历子目录  
     * @return       : list
	 * @author       : chenwei
	 * @date         : 2013-7-12 
	 * ********************************************
	 */
	public List<Map<String, Object>> listFile(List<Map<String, Object>> data, File f, String suffix, boolean isdepth) {
		final List<Long> lastModList = new ArrayList<Long>();
		final Map<Long, String> map = new HashMap<Long, String>();
		@SuppressWarnings("unused")
		String[] fileNameArr = f.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				File tmp = new File(dir.getAbsolutePath() + File.separatorChar + name);
				if (tmp.isFile()) {
					if (name.endsWith(".txt") || name.endsWith(".TXT")) {
						long lastModified = tmp.lastModified();
						lastModList.add(lastModified);
						map.put(lastModified, name);
					}
					return true;
				}
				return false;
			}
		});
//		for (String fileName : fileNameArr) {
//			if (fileName.endsWith(".txt") || fileName.endsWith(".TXT")) {
//				long lastModified = new File(f.getAbsolutePath() + File.separatorChar + fileName).lastModified();
//				lastModList.add(lastModified);
//				map.put(lastModified, fileName);
//			}
//		}
		Collections.sort(lastModList);
		Collections.reverse(lastModList);
		Iterator<Long> iterator = lastModList.iterator();
		while (iterator.hasNext()) {
			String filename = map.get(iterator.next());
	    	Map<String, Object> dataMap = new HashMap<String, Object>();
	    	dataMap.put("DOCNAME", ObjectUtil.trim(filename));
	    	data.add(dataMap);
	    }
		return data;
	}

//	public List<Map<String, Object>> listFile(List<Map<String, Object>> data, File f, String suffix, boolean isdepth) {
//        TreeMap<Long,File> tm = new TreeMap<Long,File>();
//        File subFile[] = f.listFiles();
//        int fileNum = subFile.length;
//        for (int i = 0; i < fileNum; i++) {
//             Long tempLong = new Long(subFile[i].lastModified());
//             if (subFile[i].isFile()) {
//                 tm.put(tempLong + i, subFile[i]);
//			}
//        }
//		Set<Long> set = tm.keySet();
//		Iterator<Long> it = set.iterator();
//		List<Long> list = new ArrayList<Long>();
//		while (it.hasNext()) {
//			list.add(it.next());
//		}
//		Collections.sort(list);
//		Collections.reverse(list);
//		Iterator<Long> iterator = list.iterator();
//		while (iterator.hasNext()) {
//			Object key = iterator.next();
//			Object objValue = tm.get(key);
//			File tempFile = (File) objValue;
//			String filename = tempFile.getName();
//	    	Map<String, Object> dataMap = new HashMap<String, Object>();
//	    	dataMap.put("DOCNAME", ObjectUtil.trim(filename));
//	    	data.add(dataMap);
//	    }
//		return data;
//	}

	public File getDownloadFile(String fileName, String flag) {
		//获取下载路径
		String path = ObjectUtil.trim(messageService.getSystemParam("XEREVOKE.DownloadFilePath"))+fileName;
		if ("BJHB".equals(flag)) {
			path = ObjectUtil.trim(messageService.getSystemParam("BJHBREVOKE.DownloadFilePath"))+fileName;
		}
		try {
			path=URLDecoder.decode(path,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File resultFile = new File(path);
		return resultFile;
	}
}