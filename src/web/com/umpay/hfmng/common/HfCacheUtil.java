/** *****************  JAVA头文件说明  ****************
 * file name  :  HfCacheUtil.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-14
 * *************************************************/ 

package com.umpay.hfmng.common;

import com.umpay.hfmng.cache.HfCache;


/** ******************  类说明  *********************
 * class       :  HfCacheUtil
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  静态类获取缓存信息
 * @see        :                        
 * ************************************************/

public class HfCacheUtil {
 public static HfCache getCache(){
     HfCache cache=(HfCache) SpringContextUtil.getBean("HfCache");
	 return cache;
 }
}
