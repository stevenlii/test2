package com.umpay.hfmng.common.test;

import com.umpay.hfmng.common.SequenceUtil;


public class SequenceUtilTest {

	public static void main(String[] args){
		SequenceUtil su = SequenceUtil.getInstance();
		String str = SequenceUtil.formatSequence(su.getSequence4File("hfaudit"), 10);
		System.out.println(str);
	}
}
