package com.umpay.hfmng.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 * 
 * @author lz
 */
public class PinYinUtil {
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPinYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		StringBuffer output = new StringBuffer();
		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					output.append(temp[0]);
				} else
					output.append(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output.toString();
	}
	
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变，根据多音字列出所有组合
	 * 
	 * @param inputString
	 * @return
	 */
	public static List<String> getPinYinList(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		List<String[]> list = new ArrayList<String[]>();
		try {
			for (int i = 0; i < input.length; i++) {
				String[] temp=null;
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
				} else{
					temp=new String[1];
					temp[0]=String.valueOf(input[i]);
				}
				list.add(temp);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		Descartes(list, 0, result, "");
		return result;
	}
	
	private static String Descartes(List<String[]> list, int count,List<String> result, String data) {
		String temp = data;
		// 获取当前数组
		String[] astr = list.get(count);
		// 循环当前数组
		for (int i = 0; i < astr.length; i++) {
			if (count + 1 < list.size()) {
				temp += Descartes(list, count + 1, result, data + astr[i]);
			} else {
			    result.add(data + astr[i]);
			}
		}
		return temp;
	}
	/**  
     * 获取汉字串拼音首字母，英文字符不变 ,，根据多音字列出所有组合
     * @param chinese 汉字串  
     * @return 汉语拼音首字母  
     */ 
	public static List<String> getFirstSpellList(String chinese) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = chinese.trim().toCharArray();
		List<String[]> list = new ArrayList<String[]>();
		try {
			for (int i = 0; i < input.length; i++) {
				String[] temp=null;
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
				} else{
					temp=new String[1];
					temp[0]=String.valueOf(input[i]);
				}
				for(int j=0;j<temp.length;j++){
					temp[j]=temp[j].substring(0, 1);
				}
				list.add(temp);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		Descartes(list, 0, result, "");
		return result;
	}
	/**  
     * 获取汉字串拼音首字母，英文字符不变  
     * @param chinese 汉字串  
     * @return 汉语拼音首字母  
     */   
    public static String getFirstSpell(String chinese) {
    	StringBuffer bf = new StringBuffer();
    	HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    	format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    	format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    	format.setVCharType(HanyuPinyinVCharType.WITH_V);
    	for (int i = 0; i < chinese.length(); i++) {
			try {
				if (String.valueOf(chinese.charAt(i)).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(i), format);
    				if (temp != null) {
    					bf.append(temp[0].charAt(0));
    				}
				}else{
					bf.append(chinese.charAt(i));
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
    	}
//    	return bf.toString().replaceAll("\\W", "").trim();
    	return bf.toString().trim();
    }   
    /**  
     * 获取汉字串拼音，英文字符不变  
     * @param chinese 汉字串  
     * @return 汉语拼音  
     */   
    public static String getFullSpell(String chinese) {
    	StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);   
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                	System.out.println(arr[i]);
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();   
    }
    
    public static void main(String[] args) {
//		System.out.println(getPinYin("女囧为w！.ew123我方"));
//		System.out.println(getPinYin("长单"));
//		System.out.println(getFirstSpell("女囧为w！.ew123我方"));
		
		  List<String> list1=getPinYinList("长单");
		  for (int i = 0; i < list1.size(); i++) {
			   System.out.println(list1.get(i));
		  }
		  List<String> list2=getFirstSpellList("长单");
		  for (int i = 0; i < list2.size(); i++) {
			   System.out.println(list2.get(i));
		  }
		  System.out.println(StringUtils.join(list2.toArray(), ","));
	}
}  
