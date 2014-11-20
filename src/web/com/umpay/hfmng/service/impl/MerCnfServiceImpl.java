package com.umpay.hfmng.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.dao.MerCnfDao;
import com.umpay.hfmng.model.MerCnf;
import com.umpay.hfmng.service.MerCnfService;

@Service
public class MerCnfServiceImpl implements MerCnfService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MerCnfDao merCnfDao;
	
	public MerCnf load(String merId) {
		MerCnf merCnf = (MerCnf) merCnfDao.get(merId);
		return merCnf;
	}
	
	public String modifyMerCnf(MerCnf merCnf){
		String result = "0";
		if(1==merCnfDao.update(merCnf))
			result="1";
		return result;
	}

	public String saveMerCnf(MerCnf merCnf) {
		String result="1";
		merCnfDao.saveMerCnf(merCnf);
		return result;
	}
	
	public String updateCert(MultipartFile file,MerCnf merCnf){
		InputStream input=null;
		 String result="0";
		try {
			input = file.getInputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			int merCertSize=output.size();
			//数据库中证书的字段类型为BLOB(4000)
			if(merCertSize>4000){
				return "2";//表示文件超过4000Byte时不更新
			}
			merCnf.setMerCert(output.toByteArray());
			merCnf.setCertName(file.getOriginalFilename());
			if(1==merCnfDao.updateCert(merCnf))
				result="1";
		} catch (IOException e) {
			log.info("获取文件流出错", e);
		}
		return result;
	}
}
