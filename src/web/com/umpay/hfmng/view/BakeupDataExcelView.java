package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.StringUtil;

public class BakeupDataExcelView extends AbstractView {

	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers.
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// Map<String, Object> res = (Map<String, Object>) model.get("excel");
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) model.get("excel");
		String fileName = "综合业务管理平台_数据报备_" + StringUtil.get8Date() + ".xls";
		OutputStream os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet("Sheet1", 0);
		Label label = null;
		String[] title = { "商户号", "商户名称", "商户分类", "业务属性", "商品号", "商品名称", "商品分类", "服务类型", "价格(分)", "公司简介", "商户网址",
				"注册资本", "公司成立时间", "年收益", "用户数", "业务介绍", "运营支撑(人)", "自有渠道", "商户来源", "商户分成", "北京小额", "重庆小额", "福建小额", 
				"甘肃小额", "广东小额", "广西小额", "海南小额", "河北小额", "黑龙江小额", "河南小额", "湖北小额", "江苏小额", "江西小额", "吉林小额", "辽宁小额", 
				"内蒙古小额","上海小额", "陕西小额", "山西小额", "四川小额", "云南小额", "浙江小额" };
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		// 渲染数据
		// 填充数据
		int j = 0;
		for (int i = 1; i < mapList.size() + 1; i++) {
			j = 0;
			Map<String, Object> map = mapList.get(i - 1);
			label = new Label(j++, i, (map.get("MERID") + "").trim()); // 商户号
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("MERNAME") + "").trim()); // 商户名称
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("MERCATEGORY") + "").trim()); // 商户分类
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("BUSITYPE") + "").trim()); // 业务属性
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("GOODSID") + "").trim()); // 商品号
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("GOODSNAME") + "").trim()); // 商品名称
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("GOODCATEGORY") + "").trim()); // 商品分类
			sheet.addCell(label);
			String servtype = map.get("SERVTYPE").equals("2") ? "按次" : "按月";
			label = new Label(j++, i, servtype.trim()); // 服务类型
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("AMOUNT") + "").trim()); // 价格(分)
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("COMPANY_DESC") + "").trim()); // 公司介绍
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("MER_WEB") + "").trim()); // 商户网址
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("REG_CAPITAL") + "").trim()); // 注册资本
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("REG_TIEM") + "").trim()); // 公司成立时间
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("YEAR_PROFIT") + "").trim()); // 年收益
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("USER_SCALE") + "").trim()); // 用户数
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("BUSI_DESC") + "").trim()); // 业务介绍
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("SUPPORT") + "").trim()); // 运营支撑(人)
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("SALE_CHANNEL") + "").trim()); // 自有渠道
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("SRC_MER") + "").trim()); // 商户来源
			sheet.addCell(label);
			label = new Label(j++, i, (map.get("SHARED_RATE") + "").trim()); // 商户分成
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("BEIJING") + "").trim()); // 北京小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("CHONGQING") + "").trim()); // 重庆小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("FUJIAN") + "").trim()); // 福建小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("GANSU") + "").trim()); // 甘肃小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("GUANGDONG") + "").trim()); // 广东小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("GUANGXI") + "").trim()); // 广西小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("HAINAN") + "").trim()); // 海南小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("HEBEI") + "").trim()); // 河北小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("HEILONGJIANG") + "").trim()); // 黑龙江小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("HENAN") + "").trim()); // 河南小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("HUBEI") + "").trim()); // 湖北小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("JIANGSU") + "").trim()); // 江苏小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("JIANGXI") + "").trim()); // 江西小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("JILIN") + "").trim()); // 吉林小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("LIAONING") + "").trim()); // 辽宁小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("NEIMENGGU") + "").trim()); // 内蒙古小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("SHANGHAI") + "").trim()); // 上海小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("SHANXI1") + "").trim()); // 陕西小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("SHANXI2") + "").trim()); // 山西小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("SICHUAN") + "").trim()); // 四川小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("YUNNAN") + "").trim()); // 云南小额
			sheet.addCell(label);
			label = new Label(j++, i, getBackUPdate(map.get("ZHEJIANG") + "").trim()); // 浙江小额
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();

	}

	// 翻译状态 value 为空 或者 "backupid"-"state" 格式
	private String getBackUPdate(String value) {
		// var
		// backupState={"0":"未报备","1":"提交","2":"通过","4":"不通过","5":"拒绝","6":"已报备","9":"预备"};
		String res = "";
		if (!StringUtil.validateNull(value)) {
			res = "无数据";
		} else {
			String[] tmp = value.split("-");
			String state = tmp[tmp.length - 1].trim();
			if ("0".equals(state)) {
				res = "未报备";
			} else if ("1".equals(state)) {
				res = "提交";
			} else if ("2".equals(state)) {
				res = "通过";
			} else if ("4".equals(state)) {
				res = "不通过";
			} else if ("5".equals(state)) {
				res = "拒绝";
			} else if ("6".equals(state)) {
				res = "已报备";
			} else if ("9".equals(state)) {
				res = "预备";
			} else {
				res = "--";
			}
		}
		return res;
	}
}
