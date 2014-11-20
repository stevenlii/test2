/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsBankService.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-9
 * *************************************************/

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.GoodsInfo;

/**
 * ****************** 类说明 ********************* class : GoodsBankService
 * 
 * @author : zhaojunbao
 * @version : 1.0 description :
 * @see :
 * ************************************************/

public interface GoodsBankService {
	/**
	 * ******************************************** method name :
	 * getGoodsIdByMerId description : 通过商户号查询下挂的商品号
	 * 
	 * @return : List<GoodsInfo>
	 * @param : @param mapwhere
	 * @param : @return modified : zhaojunbao , 2012-10-9 下午07:47:46
	 * @see : *******************************************
	 */
	public List<GoodsInfo> getGoodsIdByMerId(Map mapwhere);

	/**
	 * ******************************************** method name :
	 * insertGoodsBankAudit description : 新增商品银行插入审核表中
	 * 
	 * @return : String
	 * @param : @param goodsBank
	 * @param : @return modified : zhaojunbao , 2012-10-9 下午07:49:46
	 * @throws Exception
	 * @see : *******************************************
	 */
	public String insertGoodsBankAudit(GoodsBank goodsBank);

	/**
	 * ******************************************** method name :
	 * batchModGoodsBank description : 批量修改商户银行（来自文件）
	 * 
	 * @return : String
	 * @param : @param file
	 * @param : @return
	 * @param : @throws DataAccessException modified : lz , 2012-10-17
	 *        下午05:17:55
	 * @see : *******************************************
	 */
	public String batchUpdateFromFile(MultipartFile file, GoodsBank goodsBank)
			throws Exception;
//	public String upbatchUpdateFromFile(MultipartFile file, GoodsBank goodsBank)
//			throws Exception;

	/**
	 * ******************************************** method name : load
	 * description : 加载商户银行信息
	 * 
	 * @return : GoodsBank
	 * @param : @param mapwhere
	 * @param : @return modified : zhaojunbao , 2012-10-9 下午08:20:46
	 * @see : *******************************************
	 */
	public GoodsBank load(Map mapwhere);

	/**
	 * ******************************************** method name :
	 * modifyGoodsBank description : 修改商品银行
	 * 
	 * @return : String
	 * @param : @param goodsBank
	 * @param : @return modified : zhaojunbao , 2012-10-15 上午11:19:51
	 * @throws Exception
	 * @see : *******************************************
	 */
	public String modifyGoodsBank(GoodsBank goodsBank) throws Exception;

	/**
	 * ******************************************** method name : closeGoodsBank
	 * description : 关闭商品银行操作
	 * 
	 * @return : String
	 * @param : @param goodsBank
	 * @param : @return modified : zhaojunbao , 2012-11-16 上午10:09:18
	 * @throws Exception
	 * @see : *******************************************
	 */
	public String closeGoodsBank(GoodsBank goodsBank) throws Exception;

	/**
	 * ******************************************** method name : batchUpdate
	 * description : 批量配置商品银行，包括新增和修改
	 * 
	 * @return : String
	 * @param : @param goodsBank
	 * @param : @return
	 * @param : @throws Exception modified : xuhuafeng , 2013-11-12 下午02:07:54
	 *        *******************************************
	 */
	public String batchUpdate(GoodsBank goodsBank) throws Exception;

	public String upBatchUpdate(GoodsBank goodsBank) throws Exception;
/**
 * *****************  方法说明  *****************
 * method name   :  amountCompare
 * @param		 :  @param goodsBank
 * @param		 :  @return
 * @return		 :  String
 * @author       :  lizhiqiang 2014年10月20日 下午3:48:27
 * description   :  
 * @see          :  
 * **********************************************
 */
	public String amountCompare(GoodsBank goodsBank);
	
	public String upBatchUpdateFromFile(MultipartFile file, GoodsBank goodsBank) throws Exception;
}
