package com.umpay.hfmng.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonTimeValueProcessorImpl implements JsonValueProcessor {
	private String format = "yyyy-MM-dd HH:mm:ss";

	public JsonTimeValueProcessorImpl() {
		super();
	}

	public JsonTimeValueProcessorImpl(String format) {
		super();
		this.format = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if (value instanceof Timestamp[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Timestamp[] timestamps = (Timestamp[]) value;
			obj = new String[timestamps.length];
			for (int i = 0; i < timestamps.length; i++) {
				obj[i] = sf.format(timestamps[i]);
			}
		}
		return obj;
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		if (value instanceof Timestamp) {
			String str = new SimpleDateFormat(format).format((Timestamp) value);
			return str;
		}
		return null == value ? null : value.toString();
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}