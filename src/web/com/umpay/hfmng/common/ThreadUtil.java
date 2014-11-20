package com.umpay.hfmng.common;


public class ThreadUtil{
	public static void startThread(Runnable rb){
		Thread thread = new Thread(rb);
		thread.start();
	}
}
