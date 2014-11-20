package com.umpay.hfmng.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class CommonJobListener implements JobListener {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private long startTime = System.currentTimeMillis();
	
	private long endTime = System.currentTimeMillis();

	public String getName() {
		return "CommonJobListener";
	}

	public void jobExecutionVetoed(JobExecutionContext arg0) {

	}

	public void jobToBeExecuted(JobExecutionContext context) {
		this.startTime = System.currentTimeMillis();
		iniJob(context);
	}

	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jee) {
		this.endTime = System.currentTimeMillis();
		long executeTime = this.endTime-this.startTime;
		if(jee!=null){
			logger.error("JOB["+context.getJobDetail().getName()+"] ERROR!!!");
			logger.error(jee);
		}
		logger.info("JOB["+context.getJobDetail().getName()+"] EXETIME["+executeTime+"] END !");
	}
	
	private void iniJob(JobExecutionContext context){
		logger.info("JOB["+context.getJobDetail().getName()+"] STRART ...");
	}
}
