package com.umpay.hfmng.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class CheckDataUtil{
	public static boolean check(String matched, String reg) {
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(matched).matches();
	}

	public static boolean checkDateTime(String dateTime, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		try {
			sf.parse(dateTime);
		}
		catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static boolean check(String matched, String[] values) {
		for (String value : values) {
			if ((matched.equals(value))) {
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) {
		String serviceId = "5";
		String[] feeTypeId = {"2","3"};
		boolean xs = check(serviceId, feeTypeId);
		System.out.println(xs);
		if(2<4){
			System.out.println("1");
		}else if(3<4){
			System.out.println("2");
		}
	}
}
