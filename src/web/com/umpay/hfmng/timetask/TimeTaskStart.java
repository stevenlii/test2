package com.umpay.hfmng.timetask;

import java.util.Timer;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TimeTaskStart
 * @Description: 专门用于等spring环境加载好后启动定时器。
 * @author helin
 * @date 2013-1-20 下午8:49:46
 */
@Component
public class TimeTaskStart implements ApplicationListener<ApplicationEvent> {
	protected Logger log = Logger.getLogger(this.getClass());
	boolean isRun = false;

	/**
	 * @Title: onApplicationEvent
	 * @Description: 监听环境加载事件
	 * @param
	 * @param arg0
	 * @author helin
	 * @date 2013-1-20 下午9:33:21
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) { // spring容器启动完成
			if (-1 < event.toString().indexOf("parent: Root WebApplicationContext") && !isRun) {
				log.info("——————————————————————————————spring容器启动完成————————————————————————————————————————————");
				// 启动定时器。
				Timer managerTimer = new Timer("TimeTaskControl", true);
				managerTimer.schedule(new TimeTaskControl(), 0, 1 * 60 * 1000);
				isRun = true;
				log.info("*********************定时器启动！*********************");
			}
		}

	}

}
