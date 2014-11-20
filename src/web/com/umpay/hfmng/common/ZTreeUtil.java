/** *****************  JAVA头文件说明  ****************
 * file name  :  ZTreeUtil.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-4-25
 * *************************************************/ 

package com.umpay.hfmng.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.service.OptionService;


/** ******************  类说明  *********************
 * class       :  ZTreeUtil
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  ztree相关公共方法
 * ************************************************/

public class ZTreeUtil {
	@Autowired
	private OptionService optionService;
//	@Autowired
//	private MerBankDao	 merBankDaoImpl;

	/**
	 * ********************************************
	 * method name   : getBankTree 
	 * description   : 初始化银行树，包括所有全网和小额
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-4-25  下午05:33:59
	 * *******************************************
	 */
	public static String getBankTree(){
		String zTreeNodes="";  //构造树节点
		String MWNode="{ id:'1', pId:'0', name:'全网支付银行'}"; //全网银行的父节点
		String XENode="{ id:'2', pId:'0', name:'小额支付银行', open:true}";//小额支付银行父节点
		StringBuffer nodes = new StringBuffer(); //节点内容
		List<BankInfo> list=HfCacheUtil.getCache().getBankList();
		for(BankInfo bankInfo:list){
			bankInfo.trim();
			String bankType=bankInfo.getBankType();
			StringBuffer node = new StringBuffer();
			if(bankType=="3" ||bankType.equals("3")){
				node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'1', name:'").append(bankInfo.getBankName());
			}else if(bankType=="6"||bankType.equals("6")){
				node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'2', name:'").append(bankInfo.getBankName());
			}else{
				continue; //其它银行类型则跳过
			}
			if(!"2".equals(bankInfo.getState())){
				node.append("(已禁用)");
			}
			node.append("'}");
			nodes.append(",").append(node);
		}
		zTreeNodes="["+MWNode+nodes+","+XENode+"]";  //加两个父节点  再加节点内容
		return zTreeNodes;
	}
	/**
	 * *****************  方法说明  *****************
	 * method name   :  getUPBankTree
	 * @param		 :  @return
	 * @return		 :  String
	 * @author       :  lizhiqiang 2014年10月14日 上午9:55:45
	 * description   :  
	 * @see          :  
	 * **********************************************
	 */
	
	public static String getUPBankTree(){

		String zTreeNodes="";  //构造树节点
		String XENode="{ id:'XE', pId:'0', name:'小额支付银行'}"; //全网银行的父节点
		String BSNode="{ id:'BS', pId:'0', name:'博升'}"; //全网银行的父节点
		String GMNode="{ id:'GM', pId:'0', name:'游戏基地', open:true}";//小额支付银行父节点
		StringBuffer nodes = new StringBuffer(); //节点内容
		List<BankInfo> list=HfCacheUtil.getCache().getBankList();
		for(BankInfo bankInfo:list){
			bankInfo.trim();
			String bankType=bankInfo.getBankType();
			StringBuffer node = new StringBuffer();
			if(bankType=="6" ||bankType.equals("6")){
				node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'XE', name:'").append(bankInfo.getBankName());
			}else if(bankType=="61"||bankType.equals("61")){
				node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'BS', name:'").append(bankInfo.getBankName());
			}else if(bankType=="62"||bankType.equals("62")){
				node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'GM', name:'").append(bankInfo.getBankName());
			}else{
				continue; //其它银行类型则跳过
			}
			if(!"2".equals(bankInfo.getState())){
				node.append("(已禁用)");
			}
			node.append("'}");
			nodes.append(",").append(node);
		}
		zTreeNodes="["+XENode+nodes+","+BSNode+","+GMNode+"]";  //加父节点  再加节点内容
		return zTreeNodes;
	}
	public String getUPBankTree2(){
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService.getBankTypeMap("upbankType");
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		StringBuffer zTreeNodes = new StringBuffer("[");// 构造树节点
		List<BankInfo> goodsBankList=HfCacheUtil.getCache().getBankList();
		for (Map.Entry<String, String> entry : upBankTypeMap.entrySet()) {
			String upbanktype = entry.getKey();// GM
			String dbcode = entry.getValue();// 62
			List<BankInfo> singleGoodsBank = new ArrayList<BankInfo>();
			//树的增加只是
//			String GMNode = "{ id:'GM', pId:'0', name:'游戏基地', isParent:true , open:true}";// 游戏基地银行父节点
			String upbanktypeNode = "{ id:'"+upbanktype+"', pId:'0', name:'"+bankTypeMap.get(dbcode)+"', isParent:true}"; // 小额支付银行的父节点
			
			for (BankInfo goodsBank : goodsBankList) {
				goodsBank.trim();
				String id = goodsBank.getBankId();
				if (id != null) {
					if (id.startsWith(upbanktype)) {
						singleGoodsBank.add(goodsBank);
					}
				}
			}
			
			StringBuffer upbanktypeNodes = getUPSubBankTree(dbcode,upbanktype);
			zTreeNodes .append(upbanktypeNode).append(upbanktypeNodes).append(",");// 再加节点内容
		}
		
		zTreeNodes = zTreeNodes.replace(zTreeNodes.length() - 1, zTreeNodes.length(), "]");

		return zTreeNodes.toString();
	}
	/**
	 * *****************  方法说明  *****************
	 * method name   :  getUPSubBankTree
	 * @param		 :  @return
	 * @return		 :  String
	 * @author       :  lizhiqiang 2014年10月14日 上午9:55:45
	 * description   :  
	 * @param pIdNum 
	 * @see          :  
	 * **********************************************
	 */
	public static StringBuffer getUPSubBankTree(String bankCode, String pIdNum){
		StringBuffer nodes = new StringBuffer(); //节点内容
		List<BankInfo> list=HfCacheUtil.getCache().getBankList();
//		List<MerBank> merBankslist = merBankDaoImpl.findByMerId(merId);
		for(BankInfo bankInfo:list){
			bankInfo.trim();
			String bankType=bankInfo.getBankType();//bankCode
			StringBuffer node = new StringBuffer();
			if(bankType.equals(bankCode)){
				nodes.append(",");
				node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'"+ pIdNum +"', name:'").append(bankInfo.getBankName());
				if(!"2".equals(bankInfo.getState())){
					node.append("(已禁用)");
				}
				node.append("'}");
				nodes.append(node);
			}
		}
		return nodes;
	}
	public static StringBuffer getUPSubBankTree(MerBankDao merBankDaoImpl, String merId, String bankType, String pIdNum){
		StringBuffer nodes = new StringBuffer(""); //节点内容
		Map<String, Object> bankinfoMap=HfCacheUtil.getCache().getBankInfoMap();
		List<MerBank> merBankslist = merBankDaoImpl.findByMerId(merId);
		
		for(MerBank merBank:merBankslist){
			merBank.trim();
			BankInfo bankInfo = (BankInfo)bankinfoMap.get(merBank.getBankId());
			if (bankInfo !=  null) {
			String merbankType=bankInfo.getBankId().substring(0,2);//bankCode
			StringBuffer node = new StringBuffer();
			//merbankType:bs. banktype 6
			if(merbankType.equals(pIdNum)){
				nodes.append(",");
				node.append("{ id:'").append(merBank.getBankId()).append("', pId:'"+ pIdNum +"', name:'").append(bankInfo.getBankName());
				if(!"2".equals(bankInfo.getState())){
					node.append("(已禁用)");
				}
				node.append("'}");
				nodes.append(node);
			}
			}
		}
		return nodes;
	}
	
	/**
	 * ********************************************
	 * method name   : getCategoryTree 
	 * description   : 生成分类树的节点串
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-10-31  上午10:27:52
	 * *******************************************
	 */
	private static String getCategoryTree(){
		StringBuffer sb = new StringBuffer();
		Map<String, String> map = HfCacheUtil.getCache().getGoodsCategoryMap();
		for(Entry<String, String> entry : map.entrySet()){
			String id=entry.getKey();
			String name = entry.getValue();
			String pid="0";
			if(id.length() >= 4){
				pid = id.substring(0, id.length()-2);
			}
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("{id:'").append(id).append("',pId:'").append(pid).append("',name:'").append(name).append("', open:true}");
		}
		return sb.toString();
	}
	/**
	 * ********************************************
	 * method name   : getGoodsCategoryWithAll 
	 * description   : 带“全部”的商品分类树
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-10-31  上午10:28:34
	 * *******************************************
	 */
	public static String getGoodsCategoryWithAll(){
		String sb = "[{id:'',name:'全部'}";
		String tree = getCategoryTree();
		if(tree != null && tree.length() != 0){
			sb +="," + tree;
		}
		sb += "]";
		return sb;
	}
	/**
	 * ********************************************
	 * method name   : getGoodsCategory 
	 * description   : 不带“全部”的商品分类树
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-10-31  上午10:29:36
	 * *******************************************
	 */
	public static String getGoodsCategory(){
		String sb = "[";
		String tree = getCategoryTree();
		if(tree != null && tree.length() != 0){
			sb += tree;
		}
		sb += "]";
		return sb;
	}
	
	public static String buildZTree(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		for(Entry<String, String> entry : map.entrySet()){
			String id=entry.getKey();
			String name = entry.getValue();
			String pid="0";
			if(id.length() >= 4){
				pid = id.substring(0, id.length()-2);
			}
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("{id:'").append(id).append("',pId:'").append(pid).append("',name:'").append(name).append("', open:true}");
		}
		return sb.toString();
	}
	/**
	 * ********************************************
	 * method name   : getOperatorTree 
	 * description   : 生成运营负责人树
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-3-4  下午05:07:03
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	public static String getOperatorTree(){
		StringBuffer sb = new StringBuffer();
//		List list = HfCacheUtil.getCache().getOperRoleList();
		Map<String, Object> allUserMap = HfCacheUtil.getCache().getUserInfoMap();
		sb.append("[");
/*		for(Object object : list){
			Map map = (Map) object;
			String id=StringUtils.trim(map.get("id").toString());
			String name = StringUtils.trim(map.get("userName").toString());
			if(sb.length()!=1){
				sb.append(",");
			}
			sb.append("{id:'").append(id).append("',pId:'0',name:'").append(name).append("'}");
		}*/
		if (allUserMap != null && allUserMap.size() > 0) {
			for(Map.Entry<String, Object> entry : allUserMap.entrySet()){
				Map<String, Object> m = (Map<String, Object>) entry.getValue();
				String id=StringUtils.trim(m.get("id").toString());
				String name = StringUtils.trim(m.get("userName").toString());
				if(sb.length()!=1){
					sb.append(",");
				}
				sb.append("{id:'").append(id).append("',pId:'0',name:'").append(name).append("'}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * ********************************************
	 * method name   : getModOperatorTree 
	 * description   : 打开修改页面时，根据商户号生成的运营负责人树
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-3-10  下午05:26:21
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	public static String getModOperatorTree(String merId){
//		List list = HfCacheUtil.getCache().getOperRoleList();
		Map<String, Object> allUserMap = HfCacheUtil.getCache().getUserInfoMap();

		List<HfMerOper> merOperList = HfCacheUtil.getCache().getMerOperListByMerId(merId);
		List<String> openOperList = new ArrayList<String>();
		for(HfMerOper oper : merOperList){
			if(oper.getState() == 2){
				openOperList.add(oper.getOperator());
			}
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		/*for(Object object : list){
			Map map = (Map) object;
			String key = StringUtils.trim(map.get("id").toString());
			String name = StringUtils.trim(map.get("userName").toString());
			if(sb.length()!=1){
				sb.append(",");
			}
			sb.append("{id:'").append(key).append("',pId:'0',name:'").append(name);
			if(openOperList.contains(key)){
				sb.append("',checked:true}");
			}else{
				sb.append("'}");
			}
		}*/
		if (allUserMap != null && allUserMap.size() > 0) {
			for(Map.Entry<String, Object> entry : allUserMap.entrySet()){
				Map<String, Object> m = (Map<String, Object>) entry.getValue();
				String id=StringUtils.trim(m.get("id").toString());
				String name = StringUtils.trim(m.get("userName").toString());
				if(sb.length()!=1){
					sb.append(",");
				}
				sb.append("{id:'").append(id).append("',pId:'0',name:'").append(name);
				if(openOperList.contains(id)){
					sb.append("',checked:true}");
				}else{
					sb.append("'}");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
