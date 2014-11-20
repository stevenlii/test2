package com.umpay.hfmng.timetask;

import org.apache.log4j.Logger;

import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.service.ParameterLoadService;

/**
 * @ClassName: ParameterLoad
 * @Description: 负责系统参数表定时加载
 * @author helin
 * @date 2013-1-20 下午3:09:36
 */
public class ParameterLoad extends Thread {

	protected Logger log = Logger.getLogger(this.getClass());

	public ParameterLoad(String threadName) {
		super(threadName);
	}

	@Override
	public synchronized void run() {
		int nspSTCount = 1;
		while (true) {
			try {
				// 系统参数表定时加载控制
				if (nspSTCount % 5 == 0) {// 5分钟加载一次，现在写死了，没有做配置
					ParameterLoadService paraLoadService = (ParameterLoadService) SpringContextUtil
							.getBean("parameterLoadServiceImpl");
					// 加载所有参数
					paraLoadService.loadAll();
					nspSTCount = 1;
					paraLoadService = null;
					log.info("参数加载完毕，开始休眠***");
				} else {
					nspSTCount++;
				}
				this.wait();
			} catch (InterruptedException e) {
				log.info("参数加载完毕，休眠失败***", e);
				return;
			} catch (Exception e) {
				log.info("参数加载失败!", e);
			}
		}
	}

	public synchronized void restart() {
		this.notifyAll();
	}

}