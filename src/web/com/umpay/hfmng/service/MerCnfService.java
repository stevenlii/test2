package com.umpay.hfmng.service;

import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.model.MerCnf;

public interface MerCnfService{
	public MerCnf load(String merId);
	public String saveMerCnf(MerCnf merCnf);
	public String modifyMerCnf(MerCnf merCnf);
	public String updateCert(MultipartFile file,MerCnf merCnf);
}
