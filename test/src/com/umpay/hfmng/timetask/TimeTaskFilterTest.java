package com.umpay.hfmng.timetask;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.umpay.coupon.system.restclient.CouponRestRequester;
import com.umpay.hfmng.common.RandomUtil;

/** 
 * 定时过滤器测试类
 * <p>创建日期：2014-1-8 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public class TimeTaskFilterTest extends TestCase {
	private String addr = "http://localhost:9919/hfMngBusi/timetaskckfile/";

	public void testStlPayBack(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", RandomUtil.random(8));
		map.put("callbackUrl", "http://localhost:9919/hfMngBusi/timetaskfeedback/id.xml");// 定时任务反馈回调地址。
		
		String uri = addr+"stl/payBackTask" ;
		Map<String, Object> res;
		try {
			CouponRestRequester crr=new CouponRestRequester();
			res = crr.sendRequestForPost(uri, map);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void testStlStageChange2payingTask(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", RandomUtil.random(8));
		map.put("callbackUrl", "http://localhost:9919/hfMngBusi/timetaskfeedback/id.xml");// 定时任务反馈回调地址。
		
		String uri = addr+"stl/stageChange2payingTask" ;
		Map<String, Object> res;
		try {
			CouponRestRequester crr=new CouponRestRequester();
			res = crr.sendRequestForPost(uri, map);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void testStlStageChange2payedTask(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", RandomUtil.random(8));
		map.put("callbackUrl", "http://localhost:9919/hfMngBusi/timetaskfeedback/id.xml");// 定时任务反馈回调地址。
		
		String uri = addr+"stl/stageChange2payedTask" ;
		Map<String, Object> res;
		try {
			CouponRestRequester crr=new CouponRestRequester();
			res = crr.sendRequestForPost(uri, map);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void testStlmergerTask(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", RandomUtil.random(8));
		map.put("callbackUrl", "http://localhost:9919/hfMngBusi/timetaskfeedback/id.xml");// 定时任务反馈回调地址。
		
		String uri = addr+"stl/mergerTask" ;
		Map<String, Object> res;
		try {
			CouponRestRequester crr=new CouponRestRequester();
			res = crr.sendRequestForPost(uri, map);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
