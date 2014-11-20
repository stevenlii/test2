package com.umpay.hfmng.common;

import java.util.Random;
import java.util.UUID;

/** 
 * 随机数工具类
 * <p>创建日期：2012-12-11 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public class RandomUtil {
	private static final Random rand = new Random();
	
	/**
	 * 生成定长随机字符串，使用UUID生成源字符串，然后随机取指定长度字符串。
	 * 小于1或等于32位，返回UUID随机串；超过32位前补0
	 * <p>创建人：jxd ,  2012-12-11  下午02:59:14</p>
	 * <p>修改人：jxd ,  2012-12-11  下午02:59:14</p>
	 * @param length 最长32位
	 * @return 指定长度的随机字符串
	 */
	public  static String random(int length){
		String result = UUID.randomUUID().toString().replaceAll("-", "");
		if(length < 1 || length == result.length()){//小于1或等于32位，返回UUID随机串
			return result;
		}
		if(length > result.length()){//超过32位前补0
			for (int i = result.length(); i < length; i++) {
				result = "0" + result;
			}
			return result;
		}
		int beginIndex = rand.nextInt(result.length() - length);
		result = result.substring(beginIndex, beginIndex + length);
		return result;
	}
	
	/**
	 * 生成指定长度全数字随机字符串
	 * <p>创建人：jxd ,  2013-3-22  下午4:35:29</p>
	 * <p>修改人：jxd ,  2013-3-22  下午4:35:29</p>
	 * @param count 长度
	 * @return 指定长度的随机字符串
	 */
	public static String randomNumeric(int count){
		return random(count,false,true);
	}
	
	/**
	 * 生成指定长度全字母随机字符串
	 * <p>创建人：jxd ,  2013-3-22  下午4:35:29</p>
	 * <p>修改人：jxd ,  2013-3-22  下午4:35:29</p>
	 * @param count 长度
	 * @return 指定长度的随机字符串
	 */
	public static String randomLetters(int count){
		return random(count,true,false);
	}
	
	/**
	 * 生成随机字符串（字母与数字组合）
	 * <p>创建人：jxd ,  2013-3-22  下午4:55:42</p>
	 * <p>修改人：jxd ,  2013-3-22  下午4:55:42</p>
	 * @param count
	 * @return
	 */
	public static String randomLettersNumeric(int count){
		return random(count,true,true);
	}
	
	/**
	 * 生成随机数
	 * @param count 长度
	 * @param letters 是否允许有字母
	 * @param numbers 是否通话有数字
	 * @return 指定长度的随机字符串
	 * @see org.apache.commons.lang.RandomStringUtils
	 */
	private static String random(int count, boolean letters, boolean numbers) {
		return random(count, 0, 0, letters, numbers, null, rand);
	}
	
	/*
	 * 生成随机数
	 * 
	 * @see org.apache.commons.lang.RandomStringUtils
	 */
	private static String random(int count, int start, int end,
			boolean letters, boolean numbers, char[] chars, Random random) {
		if (count == 0)
			return "";
		if (count < 0) {
			throw new IllegalArgumentException(
					"Requested random string length " + count
							+ " is less than 0.");
		}
		if ((start == 0) && (end == 0)) {
			end = 123;
			start = 32;
			if ((!(letters)) && (!(numbers))) {
				start = 0;
				end = 2147483647;
			}
		}

		char[] buffer = new char[count];
		int gap = end - start;

		while (count-- != 0) {
			char ch;
			if (chars == null)
				ch = (char) (random.nextInt(gap) + start);
			else {
				ch = chars[(random.nextInt(gap) + start)];
			}
			if (((letters) && (Character.isLetter(ch)))
					|| ((numbers) && (Character.isDigit(ch)))
					|| ((!(letters)) && (!(numbers)))) {
				if ((ch >= 56320) && (ch <= 57343))
					if (count == 0) {
						++count;
					} else {
						buffer[count] = ch;
						--count;
						buffer[count] = (char) (55296 + random.nextInt(128));
					}
				else if ((ch >= 55296) && (ch <= 56191))
					if (count == 0) {
						++count;
					} else {
						buffer[count] = (char) (56320 + random.nextInt(128));
						--count;
						buffer[count] = ch;
					}
				else if ((ch >= 56192) && (ch <= 56319)) {
					++count;
				} else
					buffer[count] = ch;
			} else {
				++count;
			}
		}
		return new String(buffer);
	}
}
 