package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.service.OptionService;


public class ProxyOrderExcelView extends AbstractView {
	private static final Logger log = Logger.getLogger(ProxyOrderExcelView.class);
	
	@Override
	protected void renderMergedOutputModel(Map model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");   
		response.setHeader("Cache-Control","no-store, max-age=0, no-cache, must-revalidate");   
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		response.setHeader("Pragma", "no-cache");  
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> proxyOrderList= (List<Map<String, Object>>) model.get("excel");
		    
		String flag = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime());
		String fileName = "代理订单"+flag+".xls";
		String sheetNameString = "代理订单";

		OutputStream os = null;
		os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		WritableWorkbook wwb = Workbook.createWorkbook(os);

		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
		Label label;
		String[] title = {"交易日期","交易流水号","订单号","手机号","一级商户号","二级商户号","二级商户名称","商品号","商品名称","金额（分）","支付类型","订单状态" };
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i]);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);

		}
		OptionService optionService=(OptionService)SpringContextUtil.getBean("optionServiceImpl");
		Map<String, String> secMerNameMap = HfCacheUtil.getCache().getSecMerNameMap();
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
		Map<String, String> businessTypeMap = optionService.getBusinessTypeMap();
		
		// 下面是填充数据
		for (int i = 1; i < proxyOrderList.size() + 1; i++) {
			Map<String, Object> proxyOrder = (Map<String, Object>) proxyOrderList.get(i - 1);
			
			label = new Label(0, i, String.valueOf(proxyOrder.get("ORDERDATE")).trim());
			sheet.addCell(label);
            label =new Label(1,i,String.valueOf(proxyOrder.get("RPID")).trim());
            sheet.addCell(label);
            label =new Label(2,i,String.valueOf(proxyOrder.get("ORDERID")).trim());
            sheet.addCell(label);
            label =new Label(3,i,String.valueOf(proxyOrder.get("MOBILEID")).trim());
            sheet.addCell(label);
            String merId=String.valueOf(proxyOrder.get("MERID")).trim();
            label =new Label(4,i,merId);
            sheet.addCell(label);
            String subMerId=String.valueOf(proxyOrder.get("SUBMERID")).trim();
            label =new Label(5,i,subMerId);
            sheet.addCell(label);
            label =new Label(6,i,secMerNameMap.get(subMerId));
            sheet.addCell(label);
            String goodsId=String.valueOf(proxyOrder.get("GOODSID")).trim();
            label =new Label(7,i,goodsId);
            sheet.addCell(label);
            GoodsInfo goods=goodsMap.get(merId+"-"+goodsId);
            String goodsName="";
            if(goods!=null)
            	goodsName=goods.getGoodsName();
            label =new Label(8,i,goodsName);
            sheet.addCell(label);
            label =new Label(9,i,String.valueOf(proxyOrder.get("AMOUNT")));
            sheet.addCell(label);
            String businessType=String.valueOf(proxyOrder.get("BUSINESSTYPE")).trim();
            label =new Label(10,i,businessTypeMap.get(businessType));
            sheet.addCell(label);
            String orderState=String.valueOf(proxyOrder.get("ORDERSTATE"));
            label =new Label(11,i,"0".equals(orderState) ? "下单"
					:"2".equals(orderState)?"支付成功" 
					:"3".equals(orderState)?"支付失败":"");
            sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}
}
