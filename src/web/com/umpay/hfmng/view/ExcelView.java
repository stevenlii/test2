package com.umpay.hfmng.view;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.AbstractView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.MerInfo;
public class ExcelView extends AbstractView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control","no-store, max-age=0, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers.
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		Map<String, Object> res=(Map<String, Object>) model.get("excel");
		List<Map<String, Object>> listGoodsInfos =(List<Map<String, Object>>) res.get("goodsInfo");
		Map<String,String> goodsFeeCodeMap = (Map<String,String>) res.get("goodsFeeCode");
		Map<String,String> goodsCategoryMap = (Map<String,String>) res.get("goodsCategoryMap");
		Map<String,String> goodsTypeMap = (Map<String,String>) res.get("goodsTypeMap");
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
		
		//商品金额是商品信息扩展表中的金额，不是商品银行表中的金额
		String[] title = { "更新时间", "商户名称", "商户号", "商品号", "商品名称", "业务覆盖",
				"商品分类", "风控类型", "服务类型", "服务月份", "续费方式", "间隔周期", 
				"价格模式", "商品短信","商品描述", "客服电话", "添加方式", "状态",
				"商品金额(分)", "商品最长计费天数", "商品开通状态", "支付银行", "计费代码" };

		String fileName = "商品信息.xls";

		// 输出的excel的路径
		// 批量导出
		String sheetNameString = "商品信息列表";

		OutputStream os = null;
		os = response.getOutputStream(); // 取得输出流
		response.reset(); // 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
		WritableWorkbook wwb = Workbook.createWorkbook(os);

		// 添加第一个工作表并设置第一个Sheet的名字
		WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
		Label label;
		jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.BOLD);//宋体12号加粗
		WritableCellFormat wCFormat = new WritableCellFormat(wfont);
		wCFormat.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
		for (int i = 0; i < title.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			label = new Label(i, 0, title[i],wCFormat);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
		}
		// 下面是填充数据
		for (int i = 1; i < listGoodsInfos.size() + 1; i++) {
			Map<String, Object> m = listGoodsInfos.get(i - 1);
			label = new Label(0, i, m.get("MODTIME").toString().substring(0, 19));
			sheet.addCell(label);
			Object merId=m.get("MERID");
			String merName = "";
			if(merId!=null){
				MerInfo merInfo = (MerInfo) merMap.get(merId);
				if(merInfo != null){
					merName = merInfo.getMerName();
				}
			}
			label = new Label(1, i, merName);//商户名称
			sheet.addCell(label);
			label = new Label(2, i, merId == null ? "" : merId.toString());
			sheet.addCell(label);
			label = new Label(3, i, m.get("GOODSID") == null ? "" : m.get("GOODSID").toString());
			sheet.addCell(label);
			label = new Label(4, i, m.get("GOODSNAME") == null ? "" : m.get("GOODSNAME").toString());
			sheet.addCell(label);
			// 业务覆盖
			Object busiType=m.get("BUSITYPE");
			label = new Label(5, i, busiType == null ? "" : "01".equals(busiType.toString()) ? "省网"
														  : "10".equals(busiType.toString()) ? "全网" 
														  : "11".equals(busiType.toString()) ? "省网、全网":""); 
			sheet.addCell(label);
			// 商品分类
			String goodsCategory="";
			if(m.get("CATEGORY")!=null){
				goodsCategory=goodsCategoryMap.get(m.get("CATEGORY").toString());
			}
			label = new Label(6, i, goodsCategory); 
			sheet.addCell(label);
			// 商品风控类型
			String goodsType="";
			if(m.get("goodsType")!=null){
				goodsType=goodsTypeMap.get(m.get("goodsType").toString());
			}
			label = new Label(7, i, goodsType); 
			sheet.addCell(label);
			label = new Label(8, i, m.get("SERVTYPE")==null ? "" : "2".equals(m.get("SERVTYPE").toString()) ? "按次商品" : "包月商品");
			sheet.addCell(label);
			label = new Label(9, i, m.get("servMonth") == null ? "" : m.get("servMonth").toString());
			sheet.addCell(label);
			// 续费方式
			if(m.get("SERVTYPE")!=null && "3".equals(m.get("SERVTYPE")) && m.get("CONMODE")!=null){
				label = new Label(10, i, m.get("CONMODE").toString().equals("0") ? "正向续费"
						: m.get("CONMODE").toString().equals("1") ? "反向续费" : "默认续费");
				sheet.addCell(label);
			}else{
				label = new Label(10, i, "");
				sheet.addCell(label);
			}
			label = new Label(11, i, m.get("INTERVAL") == null ? "" : m.get("INTERVAL").toString());// 间隔周期
			sheet.addCell(label);
			label = new Label(12, i, m.get("PRICEMODE")==null ? "" : "0".equals(m.get("SERVTYPE").toString()) ? "定价模式" : "非定价模式");
			sheet.addCell(label);
			
			Object pushInf=m.get("PUSHINF");
			Object mtNum=m.get("MTNUM");
			if(pushInf!=null && mtNum!=null){
				String pm=pushInf.toString()+mtNum.toString();
				label = new Label(13, i, pm.equals("01")?"仅下发商品描述":pm.equals("11")?"仅下发商品信息":pm.equals("12")?"下发描述+信息":"");
				sheet.addCell(label);
			}else{
				label = new Label(13, i, "");
				sheet.addCell(label);
			}
			
			label = new Label(14, i, m.get("GOODSDESC") == null ? "" : m.get("GOODSDESC").toString());
			sheet.addCell(label);
			label = new Label(15, i, m.get("CUSPHONE") == null ? "" : m.get("CUSPHONE").toString());
			sheet.addCell(label);
			label = new Label(16, i, m.get("ADDWAY")==null ? "" : "0".equals(m.get("ADDWAY").toString()) ? "本地添加" : "其他"); //添加方式
			sheet.addCell(label);
			label = new Label(17, i,m.get("STATE")==null ? "" : "2".equals(m.get("STATE").toString()) ? "启用" : "禁用");
			sheet.addCell(label);
			//"商品金额", "商品最长计费天数", "商品开通状态", "支付银行", "计费代码"
			label = new Label(18, i, m.get("AMOUNT") == null ? "" : m.get("AMOUNT").toString());//商品金额
			sheet.addCell(label);
			label = new Label(19, i, m.get("CHECKDAY") == null ? "" : m.get("CHECKDAY").toString());//商品最长计费天数
			sheet.addCell(label);
			//商品开通状态
			Object kState= m.get("KSTATE");
			if(kState!=null){
				label = new Label(20, i, kState == null ? "" 
										: "11".equals(kState.toString())?"只开通新增"
										: "12".equals(kState.toString())?"只开通续费"
										: "13".equals(kState.toString())?"新增与续费全部开通"
										: "23".equals(kState.toString())?"新增与续费全部注销"
										:"");//商品开通状态
				sheet.addCell(label);
			}else{
				label = new Label(20, i, "");
				sheet.addCell(label);
			}
			//支付银行
			Object bankId=m.get("BANKID");
			String bankName = "";
			if(bankId!=null){
				BankInfo bank = (BankInfo) bankMap.get(bankId);
				if(bank != null){
					bankName = bank.getBankName()+"("+bankId+")";
				}
			}
			label = new Label(21, i, bankName);
			sheet.addCell(label);
			//计费代码
			String feeCode = "";
			Object goodsId=m.get("GOODSID");
			if(merId!=null && goodsId!=null){
				feeCode=goodsFeeCodeMap.get(merId+"-"+goodsId);
			}
			label = new Label(22, i, feeCode);
			sheet.addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();

	}

}
