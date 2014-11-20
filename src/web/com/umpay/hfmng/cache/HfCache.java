package com.umpay.hfmng.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.Option;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.XMLUtil;
import com.umpay.hfmng.dao.BankDao;
import com.umpay.hfmng.dao.CategoryDao;
import com.umpay.hfmng.dao.ChannelInfDao;
import com.umpay.hfmng.dao.ChnlInfDao;
import com.umpay.hfmng.dao.CouponInfDao;
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.dao.MerInfoDao;
import com.umpay.hfmng.dao.MerOperDao;
import com.umpay.hfmng.dao.OptionDao;
import com.umpay.hfmng.dao.ParaDao;
import com.umpay.hfmng.dao.SecMerInfDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.Category;
import com.umpay.hfmng.model.ChannelInf;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.model.SecMerInf;
import com.umpay.hfmng.service.MessageService;
import com.umpay.sso.org.User;
import com.umpay.sso.ws.WsPermissionClient;
import com.umpay.sso.ws.WsRoleClient;
import com.umpay.sso.ws.WsUserClient;

@Service
public class HfCache {
	private static Logger log = Logger.getLogger(HfCache.class);
	protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@Autowired
	private MessageService messageService;
	
	private Map<String, Cache> cacheMp;

	public Map<String, Cache> getCacheMp() {
		return cacheMp;
	}

	public void setCacheMp(Map<String, Cache> cacheMp) {
		this.cacheMp = cacheMp;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * @Title: getCouponInfMap
	 * @Description: 获取兑换券信息缓存
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午06:00:51
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCouponInfMap() {
		Cache cache = cacheMp.get("CouponInfCache");
		Element element = cache.get("couponInf");
		Map<String, Object> couponInfMap = new LinkedHashMap<String, Object>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			CouponInfDao couponInfDao = (CouponInfDao) SpringContextUtil.getBean("couponInfDaoImpl");
			List<CouponInf> list = couponInfDao.getCouponInfs();
			couponInfMap.clear();
			for (CouponInf couponInf : list) {
				couponInf.trim();
				couponInfMap.put(couponInf.getCouponId(), couponInf);
			}

			Element gEl = new Element("couponInf", couponInfMap);
			cache.put(gEl);
		} else {
			couponInfMap = (Map<String, Object>) element.getObjectValue();
			log.info("从缓存中获取兑换券数据");
		}
		return couponInfMap;
	}

	/**
	 * @Title: getChannelInfMap
	 * @Description: 获取渠道信息缓存
	 * @param
	 * @return
	 * @author panyouliang
	 * @date 2013-03-25 下午14:00:51
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getChannelInfMap() {
		Cache cache = cacheMp.get("ChannelInfoCache");
		Element element = cache.get("channelInf");
		Map<String, Object> channelInfMap = new LinkedHashMap<String, Object>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			ChannelInfDao channelInfDao = (ChannelInfDao) SpringContextUtil.getBean("channelInfDaoImpl");
			List<ChannelInf> list = channelInfDao.getChannelInfs();
			channelInfMap.clear();
			for (ChannelInf channel : list) {
				channel.trim();
				channelInfMap.put(channel.getChannelId(), channel);
			}

			Element gEl = new Element("channelInf", channelInfMap);
			cache.put(gEl);
		} else {
			channelInfMap = (Map<String, Object>) element.getObjectValue();
			log.info("从缓存中获取渠道数据");
		}
		return channelInfMap;
	}

	/**
	 * ******************************************** method name : getMerInfoMap
	 * description : 获取商户信息缓存
	 * 
	 * @return : Map<String,String>
	 * @param : @return modified : zhaojunbao , 2012-8-23 下午03:01:40
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMerInfoMap() {
		long start = System.currentTimeMillis();
		Cache cache = cacheMp.get("MerInfoCache");
		Element element = cache.get("merInfo");
		Map<String, Object> merInfoMap = new LinkedHashMap<String, Object>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			MerInfoDao merInfoDao = (MerInfoDao) SpringContextUtil.getBean("merInfoDaoImpl");
			List<MerInfo> list = merInfoDao.getMerInfos();

			merInfoMap.clear();
			for (MerInfo merInfo : list) {
				merInfo.trim();
				merInfoMap.put(merInfo.getMerId().trim(), merInfo);
			}

			Element gEl = new Element("merInfo", merInfoMap);
			cache.put(gEl);
			long interval = System.currentTimeMillis() - start;
			log.info("从数据库中获取商户数据，消耗时间：" + interval);
		} else {
			merInfoMap = (Map<String, Object>) element.getObjectValue();
			long interval = System.currentTimeMillis() - start;
			log.info("从缓存中获取商户数据，消耗时间：" + interval);
		}
		return merInfoMap;
	}

	/**
	 * ******************************************** method name :
	 * getMerInfoMapByChannel description : 根据渠道获取商户信息
	 * 
	 * @return : Map<String,String>
	 * @param : @return *******************************************
	 */
	public Map<String, Object> getMerInfoMapByChannel(String channelId) {
		Map<String, Object> merInfoMap = new LinkedHashMap<String, Object>();
		MerInfoDao merInfoDao = (MerInfoDao) SpringContextUtil.getBean("merInfoDaoImpl");
		List<MerInfo> list = merInfoDao.getChannelMerInfos(channelId);
		merInfoMap.clear();
		for (MerInfo merInfo : list) {
			merInfoMap.put(merInfo.getMerId().trim(), merInfo);
		}
		return merInfoMap;
	}

	/**
	 * ******************************************** method name :
	 * getMerInfoMapByChannel description : 根据渠道商户获取商品信息
	 * 
	 * @return : Map<String,String>
	 * @param : @return *******************************************
	 */
	public Map<String, Object> getGoodsInfoMapByChannel(String channelId, String merId) {
		Map<String, Object> goodsInfoMap = new LinkedHashMap<String, Object>();
		// 缓存为空，从数据库中获取
		GoodsInfoDao goodsInfoDao = (GoodsInfoDao) SpringContextUtil.getBean("goodsInfoDaoImpl");
		List<GoodsInfo> list = goodsInfoDao.getAllGoodsInfoByChannelMer(channelId, merId);
		goodsInfoMap.clear();
		for (GoodsInfo goodsInfo : list) {
			goodsInfo.trim();
			goodsInfoMap.put(goodsInfo.getGoodsId(), goodsInfo);
		}
		return goodsInfoMap;
	}

	/**
	 * ******************************************** 
	 * method name : getBankInfoMap
	 * description : 缓存获取支付银行信息
	 * 
	 * @return : Map<String,Object>
	 * @param : @return modified : zhaojunbao , 2012-9-26 下午04:21:02
	 * @see : *******************************************
	 */
	public Map<String, Object> getBankInfoMap() {
		Cache cache = cacheMp.get("BankInfoCache");
		Element element = cache.get("bankInfo");
		Map<String, Object> bankInfoMap = new TreeMap<String, Object>();

		if (element == null) {
			// 缓存为空，从数据库中获取
			BankDao bankDao = (BankDao) SpringContextUtil.getBean("bankDaoImpl");
			List<BankInfo> list = bankDao.getBankInfos();
			for (BankInfo bankInfo : list) {
				bankInfo.trim();
				bankInfoMap.put(bankInfo.getBankId(), bankInfo);
			}
			Element gEl = new Element("bankInfo", bankInfoMap);
			cache.put(gEl);
		} else {
			bankInfoMap = (Map<String, Object>) element.getObjectValue();
			log.info("从缓存中获取银行信息数据");
		}
		return bankInfoMap;
	}
	

	public List<BankInfo> getBankList() {
		Cache cache = cacheMp.get("BankInfoCache");
		Element element = cache.get("bankInfo");
		List<BankInfo> bankList = new ArrayList<BankInfo>();
		Map<String, BankInfo> bankInfoMap = new LinkedHashMap<String, BankInfo>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			BankDao bankDao = (BankDao) SpringContextUtil.getBean("bankDaoImpl");
			bankList = bankDao.getBankInfos();
			bankInfoMap.clear();
			for (BankInfo bankInfo : bankList) {
				bankInfo.trim();
				bankInfoMap.put(bankInfo.getBankId(), bankInfo);
			}
			Element gEl = new Element("bankInfo", bankInfoMap);
			cache.put(gEl);
		} else {
			bankInfoMap = (Map<String, BankInfo>) element.getObjectValue();
			bankList = new ArrayList(bankInfoMap.values());
			// Collection<BankInfo> c = bankInfoMap.values();
			// Iterator it = c.iterator();
			// while (it.hasNext()) {
			// bankList.add((BankInfo) it.next());
			// }
			log.info("从缓存中获取银行信息数据");
		}
		return bankList;
	}
	
	/**
	 * ******************************************** method name : getMerName
	 * description : 通过商户号返回商户名
	 * 
	 * @return : String
	 * @param : @param merId
	 * @param : @return modified : zhaojunbao , 2012-8-23 下午03:03:21
	 * @see : *******************************************
	 */
	public String getMerName(String merId) {
		String merName = ((MerInfo) getMerInfoMap().get(merId)).getMerName();
		return merName;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getMerNameMap() {
		Map<String, Object> merMap = getMerInfoMap();
		List<MerInfo> merList = new ArrayList<MerInfo>();
		merList = new ArrayList(merMap.values());
		Map<String, String> merNameMap = new HashMap<String, String>();
		for (MerInfo mer : merList) {
			merNameMap.put(mer.getMerId(), mer.getMerName());
		}
		return merNameMap;
	}

	/**
	 * ******************************************** method name : getBankName
	 * description : 根据银行号获取银行名称
	 * 
	 * @return : String
	 * @param : @param bankId
	 * @param : @return modified : xhf , 2012-10-9 上午11:23:36
	 *        *******************************************
	 */
	public String getBankName(String bankId) {
		BankInfo bankInfo = (BankInfo) getBankInfoMap().get(bankId);
		if (bankInfo != null) {
			return bankInfo.getBankName();
		} else {
			return "";
		}
	}

	public Map<String, String> getBankNameMap() {
		List<BankInfo> bankList = getBankList();
		Map<String, String> bankNameMap = new HashMap<String, String>();
		for (BankInfo bank : bankList) {
			bank.trim();
			bankNameMap.put(bank.getBankId(), bank.getBankName());
		}
		return bankNameMap;
	}

	/**
	 * ******************************************** method name :
	 * getGoodsInfoMap description : 商品信息缓存，key为“商户号-商品号"，value为商品信息
	 * 
	 * @return : Map<String,Object>
	 * @param : @return modified : zhaojunbao , 2012-11-12 下午05:11:02
	 * @see : *******************************************
	 */
	public Map<String, Object> getGoodsInfoMap() {
		long start = System.currentTimeMillis();
		Cache cache = cacheMp.get("GoodsInfoCache");
		Element element = cache.get("goodsInfo");
		Map<String, Object> goodsInfoMap = new LinkedHashMap<String, Object>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			GoodsInfoDao goodsInfoDao = (GoodsInfoDao) SpringContextUtil.getBean("goodsInfoDaoImpl");
			List<GoodsInfo> list = goodsInfoDao.getGoodsInfos();
			goodsInfoMap.clear();
			for (GoodsInfo goodsInfo : list) {
				goodsInfo.trim();
				// goodsInfoMap.put(goodsInfo.getGoodsId(), goodsInfo);
				goodsInfoMap.put(goodsInfo.getMerId() + "-" + goodsInfo.getGoodsId(), goodsInfo);
			}
			Element gEl = new Element("goodsInfo", goodsInfoMap);
			cache.put(gEl);
			long interval = System.currentTimeMillis() - start;
			log.info("从数据库中获取商品信息数据，消耗时间：" + interval);
		} else {
			goodsInfoMap = (Map<String, Object>) element.getObjectValue();
			long interval = System.currentTimeMillis() - start;
			log.info("从缓存中获取商品信息数据，消耗时间：" + interval);
		}
		return goodsInfoMap;
	}
	/**
	 * ******************************************** 
	 * method name :
	 * getGoodsInfoMap description : 商品信息缓存，key为“商户号-商品号"，value为商品信息
	 * 
	 * @return : Map<String,Object>
	 * @param : 
	 * @return modified :
	 * @see : *******************************************
	 */
//	public Map<String, Object> getUPServiceMap() {
//		long start = System.currentTimeMillis();
//		Cache cache = cacheMp.get("UPServiceCache");
//		Element element = cache.get("UPService");
//		Map<String, Object> goodsInfoMap = new LinkedHashMap<String, Object>();
//		if (element == null) {
//			UPServiceServiceImpl.load(merId, goodsId, gateId);
//			// 缓存为空，从数据库中获取
//			GoodsInfoDao goodsInfoDao = (GoodsInfoDao) SpringContextUtil.getBean("goodsInfoDaoImpl");
//			List<GoodsInfo> list = goodsInfoDao.getGoodsInfos();
//			goodsInfoMap.clear();
//			for (GoodsInfo goodsInfo : list) {
//				goodsInfo.trim();
//				// goodsInfoMap.put(goodsInfo.getGoodsId(), goodsInfo);
//				goodsInfoMap.put(goodsInfo.getMerId() + "-" + goodsInfo.getGoodsId(), goodsInfo);
//			}
//			Element gEl = new Element("goodsInfo", goodsInfoMap);
//			cache.put(gEl);
//			long interval = System.currentTimeMillis() - start;
//			log.info("从数据库中获取商品信息数据，消耗时间：" + interval);
//		} else {
//			goodsInfoMap = (Map<String, Object>) element.getObjectValue();
//			long interval = System.currentTimeMillis() - start;
//			log.info("从缓存中获取商品信息数据，消耗时间：" + interval);
//		}
//		return goodsInfoMap;
//	}

	/**
	 * ******************************************** method name :
	 * getGoodsInfoMap2 description : 与getGoodsInfoMap方法完全相同，只是返回值类型稍有不同
	 * 
	 * @return : Map<String,GoodsInfo>
	 * @param : @return modified : xuhuafeng , 2013-11-11 上午11:35:26
	 *        *******************************************
	 */
	public Map<String, GoodsInfo> getGoodsInfoMap2() {
		long start = System.currentTimeMillis();
		Cache cache = cacheMp.get("GoodsInfoCache");
		Element element = cache.get("goodsInfo");
		Map<String, GoodsInfo> goodsInfoMap = new LinkedHashMap<String, GoodsInfo>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			GoodsInfoDao goodsInfoDao = (GoodsInfoDao) SpringContextUtil.getBean("goodsInfoDaoImpl");
			List<GoodsInfo> list = goodsInfoDao.getGoodsInfos();
			goodsInfoMap.clear();
			for (GoodsInfo goodsInfo : list) {
				goodsInfo.trim();
				// goodsInfoMap.put(goodsInfo.getGoodsId(), goodsInfo);
				goodsInfoMap.put(goodsInfo.getMerId() + "-" + goodsInfo.getGoodsId(), goodsInfo);
			}
			Element gEl = new Element("goodsInfo", goodsInfoMap);
			cache.put(gEl);
			long interval = System.currentTimeMillis() - start;
			log.info("从数据库中获取商品信息数据，消耗时间：" + interval);
		} else {
			goodsInfoMap = (Map<String, GoodsInfo>) element.getObjectValue();
			long interval = System.currentTimeMillis() - start;
			log.info("从缓存中获取商品信息数据，消耗时间：" + interval);
		}
		return goodsInfoMap;
	}

	/**
	 * ******************************************** method name : getUserList
	 * description : 缓存获取用户信息
	 * 
	 * @return : List<?>
	 * @param : @return modified : zhaojunbao , 2012-11-13 上午11:27:32
	 * @see : *******************************************
	 */
	public List<?> getUserList() {
		// Cache cache = cacheMp.get("UserInfoCache");
		// Element element = cache.get("userInfo");
		// List<?> userList = new ArrayList<Object>();
		// if (element == null) {
		// // 缓存为空，从uweb获取
		// String systemDN = messageService.getSystemParam("System.DN");
		// log.debug("System DN:" + systemDN);
		// userList = WsUserClient.findAllByDN(systemDN);
		// Element gEl = new Element("userInfo", userList);
		// cache.put(gEl);
		// } else {
		// userList = (List<?>) element.getObjectValue();
		// log.info("从缓存中获取登陆用户信息数据");
		// }
		Collection<Object> c = getUserInfoMap().values();
		List<Object> list = new ArrayList<Object>(c);
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfoMap() {
		Cache cache = cacheMp.get("UserInfoCache");
		Element element = cache.get("userInfo");
		Map<String, Object> userMap = new LinkedHashMap<String, Object>();
		if (element == null) {
			// 缓存为空，从uweb获取
			String systemDN = messageService.getSystemParam("System.DN");
			log.debug("System DN:" + systemDN);
			List<?> userList = WsUserClient.findAllByDN(systemDN);
			if (userList != null && userList.size() > 0) {
				for (Object o : userList) {
					@SuppressWarnings("unchecked")
					Map<String, Object> m = (Map<String, Object>) o;
					if (m.get("id") != null && m.get("userName") != null) {
						String userid = String.valueOf(m.get("id"));
						userMap.put(StringUtils.trim(userid), o);
					}
				}
			}
			Element gEl = new Element("userInfo", userMap);
			cache.put(gEl);
			log.info("从数据库中根据获取本系统的用户");
		} else {
			userMap = (Map<String, Object>) element.getObjectValue();
			log.info("从缓存中根据获取本系统的用户");
		}
		return userMap;
	}

	/**
	 * ******************************************** method name :
	 * getOperIdAndName description : 获取所有人员ID与名称
	 * 
	 * @return : Map *******************************************
	 */
	public Map<String, String> getOperIdAndName() {
		Map<String, String> retMap = new HashMap<String, String>();
		Map<String, Object> allUserMap = getUserInfoMap();
		if (allUserMap != null && allUserMap.size() > 0) {
			for (Map.Entry<String, Object> entry : allUserMap.entrySet()) {
				@SuppressWarnings("unchecked")
				Map<String, Object> m = (Map<String, Object>) entry.getValue();
				String id = String.valueOf(m.get("id"));
				String userName = String.valueOf(m.get("userName"));
				retMap.put(StringUtils.trim(id), StringUtils.trim(userName));
			}
		}
		return retMap;
	}

	/**
	 * ******************************************** method name :
	 * getOperRoleList description : 缓存获取运营人员列表
	 * 
	 * @return : List
	 * @param : @return modified : zhaojunbao , 2012-11-13 下午02:34:06
	 * @see : *******************************************
	 */
	public List<?> getOperRoleList() {
		Cache cache = cacheMp.get("OperCache");
		Element element = cache.get("operCache");
		List<?> operList = null;
		if (element == null) {
			String operatorRole = messageService.getSystemParam("OperatorRole");
			// 查找运营人员角色
			List roleList = null;
			if (operatorRole != null && !operatorRole.equals("")) {
				roleList = WsRoleClient.findByRoleName(operatorRole);
			}
			if (roleList != null && roleList.size() > 0) {
				Map mapRole = (Map) (roleList.get(0));
				String id = mapRole.get("id").toString();
				// String id="8a8aa656396bd50201396bd5020b0000";
				// 根据角色id查找用户
				operList = WsUserClient.findByRoleId(id);
			}
			Element gEl = new Element("operCache", operList);
			cache.put(gEl);
		} else {
			operList = (List<?>) element.getObjectValue();
		}
		return operList;
	}

	/**
	 * ******************************************** method name : getCreatorList
	 * description : 返回创建人员列表
	 * 
	 * @return : List<?>
	 * @param : @return modified : zhaojunbao , 2012-11-13 下午05:46:02
	 * @see : *******************************************
	 */
	public List<?> getCreatorList() {
		return getUserList();
	}

	/**
	 * ******************************************** method name : clear
	 * description : 移出指定的缓存
	 * 
	 * @return : void
	 * @param : @param cacheName modified : zhaojunbao , 2012-11-13 下午05:26:53
	 * @see : *******************************************
	 */
	public void clear(String cacheName) {
		Cache cache = cacheMp.get(cacheName);
		if (cache != null) {
			cache.removeAll();
		}
	}

	public String getUrlAcl(String XMLName) {
		User user = LoginUtil.getUser();
		// 查找缓存中是否存在该用户对指定页面的访问权限
		Cache cache = cacheMp.get("UrlAclCache");
		String userPage = user.getId() + "-" + XMLName;// 用“用户id-页面”来表示用户对指定页面中的权限
		Element element = cache.get(userPage);
		if (element != null) {
			return (String) element.getObjectValue();
		}
		// 缓存中不存在，则从uweb查询
		log.info("从uweb查询用户权限。userid：" + user.getId() + "；XMLName：" + XMLName);
		XMLUtil<Option> x = new XMLUtil<Option>();
		Option option = new Option();
		String opts = "";
		String path = "/urlacl/" + XMLName + ".xml";
		Resource res = resourcePatternResolver.getResource(path);
		// path=this.getClass().getClassLoader().getResource(path).getPath();
		try {
			path = res.getFile().getAbsolutePath();
			List<Option> list = x.readXML(path, option);
			for (Option opt : list) {
				boolean perm = WsPermissionClient.hasPermission(user.getId(), opt.getUrl());
				if (perm == true) {
					opts += opt.getCode() + ",";
				}
			}
		} catch (Exception e) {
			log.info("从uweb查询用户权限出错。userid：" + user.getId() + "；XMLName：" + XMLName, e);
			return "";
		}
		if (!opts.equals("")) {
			opts = opts.substring(0, opts.length() - 1);
		}
		// 入缓存
		Element gEl = new Element(userPage, opts);
		cache.put(gEl);
		return opts;

		// return "001,002,003,004,005,006,007,008,009";
	}

	/**
	 * ******************************************** method name :
	 * getGoodsTypeMap description : 返回风控类型列表
	 * 
	 * @return : Map<String,Object>
	 * @param : @return modified : xuhuafeng , 2013-1-7 下午04:59:54
	 *        *******************************************
	 */
	@SuppressWarnings("unchecked")
	public List<GoodsTypeModel> getGoodsTypeList() {
		Cache cache = cacheMp.get("GoodsTypeCache");
		Element element = cache.get("goodsType");
		List<GoodsTypeModel> list = new ArrayList<GoodsTypeModel>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			OptionDao optionDao = (OptionDao) SpringContextUtil.getBean("optionDaoImpl");
			list = optionDao.getGoodsType();
			Element gEl = new Element("goodsType", list);
			cache.put(gEl);
		} else {
			list = (List<GoodsTypeModel>) element.getObjectValue();
			log.info("从缓存中获取风控类型数据");
		}
		return list;
	}

	public List<?> getRolesByUser(String userId) {
		List<?> list = getUserRole(userId, "userId");
		return list;
	}

	public List<?> getUsersByRole(String roleId) {
		List<?> list = getUserRole(roleId, "roleId");
		return list;
	}

	private List<?> getUserRole(String id, String flag) {
		Cache cache = cacheMp.get("UserRoleCache");
		Element element = cache.get(flag + id);
		List<?> list = null;
		if (element == null) {
			if ("roleId".equals(flag)) {
				list = WsUserClient.findByRoleId(id);
				Element gEl = new Element(flag + id, list);
				cache.put(gEl);
				log.info("从数据库中根据角色获取用户");
			} else if ("userId".equals(flag)) {
				list = WsRoleClient.findByUserId(id);
				Element gEl = new Element(flag + id, list);
				cache.put(gEl);
				log.info("从数据库中根据用户获取角色");
			}
		} else {
			list = (List<?>) element.getObjectValue();
			if ("roleId".equals(flag)) {
				log.info("从缓存中根据角色获取用户");
			} else if ("userId".equals(flag)) {
				log.info("从缓存中根据用户获取角色");
			}
		}
		return list;
	}

	public Map<String, ChnlInf> getChnlInfMap() {
		Cache cache = cacheMp.get("ChnlInfCache");
		Element element = cache.get("chnlInf");
		Map<String, ChnlInf> chnlInfMap = new TreeMap<String, ChnlInf>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			ChnlInfDao chnlInfDao = (ChnlInfDao) SpringContextUtil.getBean("chnlInfDaoImpl");
			List<ChnlInf> list = chnlInfDao.getChnlList();
			for (ChnlInf chnlInf : list) {
				chnlInf.trim();
				chnlInfMap.put(chnlInf.getChannelId(), chnlInf);
			}
			Element gEl = new Element("chnlInf", chnlInfMap);
			cache.put(gEl);
			log.info("从数据库中获取渠道信息");
		} else {
			chnlInfMap = (Map<String, ChnlInf>) element.getObjectValue();
			log.info("从缓存中获取渠道信息");
		}
		return chnlInfMap;
	}

	public Map<String, SecMerInf> getSecMerMap() {
		Cache cache = cacheMp.get("SecMerInfCache");
		Element element = cache.get("secMerInf");
		Map<String, SecMerInf> merMap = new TreeMap<String, SecMerInf>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			SecMerInfDao secMerInfDao = (SecMerInfDao) SpringContextUtil.getBean("secMerInfDaoImpl");
			List<SecMerInf> list = secMerInfDao.findBy(null);
			for (SecMerInf mer : list) {
				mer.trim();
				merMap.put(mer.getSubMerId(), mer);
			}
			Element gEl = new Element("secMerInf", merMap);
			cache.put(gEl);
			log.info("从数据库中获取二级商户信息");
		} else {
			merMap = (Map<String, SecMerInf>) element.getObjectValue();
			log.info("从缓存中获取二级商户信息");
		}
		return merMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getSecMerNameMap() {
		Map<String, SecMerInf> merMap = getSecMerMap();
		List<SecMerInf> merList = new ArrayList<SecMerInf>();
		merList = new ArrayList(merMap.values());
		Map<String, String> merNameMap = new HashMap<String, String>();
		for (SecMerInf mer : merList) {
			merNameMap.put(mer.getSubMerId(), mer.getSubMerName());
		}
		return merNameMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getGoodsNameMap() {
		Map<String, Object> map = getGoodsInfoMap();
		List<SecMerInf> merList = new ArrayList<SecMerInf>();
		merList = new ArrayList(map.values());
		Map<String, String> goodsNameMap = new HashMap<String, String>();
		for (Object o : merList) {
			GoodsInfo goods = (GoodsInfo) o;
			goodsNameMap.put(goods.getMerId() + "-" + goods.getGoodsId(), goods.getGoodsName());
		}
		return goodsNameMap;
	}

	public Map<String, String> getGoodsCategoryMap() {
		Cache cache = cacheMp.get("GoodsCategoryCache");
		Element element = cache.get("goodsCategory");
		Map<String, String> categoryMap = new TreeMap<String, String>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			CategoryDao categoryDao = (CategoryDao) SpringContextUtil.getBean("categoryDaoImpl");
			List<Category> list = categoryDao.findBy(null);
			for (Category c : list) {
				c.trim();
				categoryMap.put(c.getCategoryId(), c.getCategoryName());
			}
			Element gEl = new Element("goodsCategory", categoryMap);
			cache.put(gEl);
			log.info("从数据库中获取商品分类信息");
		} else {
			categoryMap = (Map<String, String>) element.getObjectValue();
			log.info("从缓存中获取商品分类信息");
		}
		return categoryMap;
	}

	/**
	 * ******************************************** method name :
	 * getAbsoluteCategoryName description :
	 * 根据分类Id获取分类名称，包括父分类。如：商超购物-超市购物券。分类规则
	 * ：编号第一和第二位表示一级分类，第三和第四位表示二级分类，依次类推。如11表示商超购物，1101表示超市购物券。
	 * 
	 * @return : String
	 * @param : @param categoryId
	 * @param : @return modified : xuhuafeng , 2013-10-31 下午04:04:42
	 *        *******************************************
	 */
	public String getCategoryAbsoluteName(String categoryId) {
		Map<String, String> goodsCategoryMap = getGoodsCategoryMap();
		String categoryName = "";
		if (categoryId != null && !"".equals(categoryId)) {
			categoryName = goodsCategoryMap.get(categoryId);
			while (categoryId.length() >= 4) {
				categoryId = categoryId.substring(0, categoryId.length() - 2);
				categoryName = goodsCategoryMap.get(categoryId) + "-" + categoryName;
			}
		}
		return categoryName;
	}

	/**
	 * ******************************************** method name : getParaMap
	 * description : 参数配置缓存，key为“参数类别-参数编码"，value为参数配置信息
	 * 
	 * @return : Map<String,Object>
	 * @return modified : jxd , 2013-12-11下午05:11:02
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParaMap() {
		long start = System.currentTimeMillis();
		Cache cache = cacheMp.get("ParaCache");
		Element element = cache.get("couponPara");
		Map<String, Object> paraMap = new LinkedHashMap<String, Object>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			ParaDao paraDaoImpl = (ParaDao) SpringContextUtil.getBean("paraDaoImpl");
			List<Para> list = paraDaoImpl.findAllParas("coupon");
			paraMap.clear();
			for (Para para : list) {
				para.trim();
				paraMap.put(para.getParaType() + "-" + para.getParaCode(), para);
			}
			Element gEl = new Element("couponPara", paraMap);
			cache.put(gEl);
			long interval = System.currentTimeMillis() - start;
			log.info("从数据库中获取参数配置信息数据，消耗时间：" + interval);
		} else {
			paraMap = (Map<String, Object>) element.getObjectValue();
			long interval = System.currentTimeMillis() - start;
			log.info("从缓存中获取参数配置信息数据，消耗时间：" + interval);
		}
		return paraMap;
	}

	public List<HfMerOper> getMerOperListByMerId(String merId) {
		Cache cache = cacheMp.get("MerOperCache");
		Element element = cache.get(merId);
		List<HfMerOper> merOperList = new ArrayList<HfMerOper>();
		if (element == null) {
			// 缓存为空，从数据库中获取
			MerOperDao merOperDao = (MerOperDao) SpringContextUtil.getBean("merOperDaoImpl");
			HfMerOper merOper = new HfMerOper();
			merOper.setMerId(merId);
			merOperList = merOperDao.findBy(merOper);
			for (int i = 0; i < merOperList.size(); i++) {
				merOperList.get(i).trim();
			}
			Element gEl = new Element(merId, merOperList);
			cache.put(gEl);
			log.info("从数据库中获取商户[" + merId + "]的运营负责人信息");
		} else {
			merOperList = (List<HfMerOper>) element.getObjectValue();
			log.info("从缓存中获取商户[" + merId + "]的运营负责人信息");
		}
		return merOperList;
	}

	/**
	 * @Title: getXeBankInfoMap
	 * @Description: 获取小额支付银行缓存信息
	 * @return
	 * @author wanyong
	 * @date 2014-7-24 下午4:22:49
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getXeBankInfoMap() {
		Cache cache = cacheMp.get("XeBankInfoCache");
		Element element = cache.get("bankInfo");
		Map<String, Object> xeBankInfoMap = new HashMap<String, Object>();

		if (element == null) {
			// 缓存为空，从数据库中获取
			BankDao bankDao = (BankDao) SpringContextUtil.getBean("bankDaoImpl");
			List<BankInfo> list = bankDao.findXEBankInfos();
			for (BankInfo bankInfo : list) {
				bankInfo.trim();
				xeBankInfoMap.put(bankInfo.getBankId(), bankInfo);
			}
			Element gEl = new Element("bankInfo", xeBankInfoMap);
			cache.put(gEl);
		} else {
			xeBankInfoMap = (Map<String, Object>) element.getObjectValue();
			log.info("从缓存中获取银行信息数据");
		}
		return xeBankInfoMap;
	}
}
