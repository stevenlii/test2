package com.umpay.hfmng.common;


import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonNumberValueProcessorImpl implements JsonValueProcessor {

	public JsonNumberValueProcessorImpl() {
		super();
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if (value instanceof Number[]) {
			Number[] numbers = (Number[]) value;
			obj = new String[numbers.length];
			for (int i = 0; i < numbers.length; i++) {
				obj[i] = null == value ? "null" : value.toString();
			}
		}
		
		return obj;
	}

	public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {
		return null == value ? "null" : value;
	}
}