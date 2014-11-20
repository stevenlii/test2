/**
 * @Title: OmItemSelector.java
 * @Package com.umpay.hfmng.model
 * @Description: TODO
 * @author MARCO
 * @date 2014-6-5 下午5:10:12
 * @version V1.0
 */

package com.umpay.hfmng.model;

public class OmItemSelector {
	private String text;
	private String value;
	private int ischecked = 0;
	public int getIschecked() {
		return ischecked;
	}
	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
