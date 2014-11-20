package com.umpay.hfmng.timetask;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public class TimeTaskControl extends TimerTask implements Runnable {

	protected Logger log = Logger.getLogger(this.getClass());

	private ParameterLoad parameterLoadThread = new ParameterLoad("parameterLoadThread");// 各种参数加载
	private TimeTaskDispatch timeTaskDispatchThread = new TimeTaskDispatch("timeTaskDispatchThread");// 定时任务派发
	private TimeTaskSend timeTaskSendThread = new TimeTaskSend("timeTaskSendThread");// 定时任务派发

	@Override
	public void run() {
		// 各种参数加载
		try {
			if (parameterLoadThread == null)
				parameterLoadThread = new ParameterLoad("parameterLoadThread");
			String parameterLoadThreadState = parameterLoadThread.getState().toString();
			log.info("----------parameterLoadThread：" + parameterLoadThreadState);
			// 判断线程是否已经异常，异常回收并重新创建。
			if (parameterLoadThreadState.equals("WAITING")) {
				parameterLoadThread.restart();
			} else if (parameterLoadThreadState.equals("NEW")) {
				parameterLoadThread.start();
			} else if (parameterLoadThreadState.equals("TERMINATED")) {
				parameterLoadThread.interrupt();
				parameterLoadThread = new ParameterLoad("parameterLoadThread");
				parameterLoadThread.start();
			} else {
				log.info("-----parameterLoadThread--还在运行。-");

			}
		} catch (Throwable t) {
			log.error("!!!!!!!!!!parameterLoadThread--错误：", t);
		}

		// 定时任务派发
		try {
			if (timeTaskDispatchThread == null)
				timeTaskDispatchThread = new TimeTaskDispatch("timeTaskDispatchThread");
			String timeTaskDispatchThreadState = timeTaskDispatchThread.getState().toString();
			log.info("----------timeTaskDispatchThreadState：" + timeTaskDispatchThreadState);
			// 判断线程是否已经异常，异常回收并重新创建。
			if (timeTaskDispatchThreadState.equals("WAITING")) {
				timeTaskDispatchThread.restart();
			} else if (timeTaskDispatchThreadState.equals("NEW")) {
				timeTaskDispatchThread.start();
			} else if (timeTaskDispatchThreadState.equals("TERMINATED")) {
				timeTaskDispatchThread.interrupt();
				timeTaskDispatchThread = new TimeTaskDispatch("timeTaskDispatchThread");
				timeTaskDispatchThread.start();
			} else {
				log.info("-----timeTaskDispatchThread--还在运行。-");

			}
		} catch (Throwable t) {
			log.error("!!!!!!!!!!timeTaskDispatchThread--错误：", t);
		}

		// 定时任务触发报文发送，反馈超时检测
		try {
			if (timeTaskSendThread == null)
				timeTaskSendThread = new TimeTaskSend("timeTaskSendThread");
			String timeTaskSendThreadState = timeTaskSendThread.getState().toString();
			log.info("----------timeTaskSendThreadState：" + timeTaskSendThreadState);
			// 判断线程是否已经异常，异常回收并重新创建。
			if (timeTaskSendThreadState.equals("WAITING")) {
				timeTaskSendThread.restart();
			} else if (timeTaskSendThreadState.equals("NEW")) {
				timeTaskSendThread.start();
			} else if (timeTaskSendThreadState.equals("TERMINATED")) {
				timeTaskSendThread.interrupt();
				timeTaskSendThread = new TimeTaskSend("timeTaskSendThread");
				timeTaskSendThread.start();
			} else {
				log.info("-----timeTaskSendThread--还在运行。-");

			}
		} catch (Throwable t) {
			log.error("!!!!!!!!!!timeTaskSendThread--错误：", t);
		}

	}
}
