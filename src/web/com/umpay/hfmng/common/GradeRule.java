package com.umpay.hfmng.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class GradeRule {
	/**
	 * ********************************************
	 * method name   : getRiseRate 
	 * description   : 根据上月和上上月交易额，计算交易增长率（为真正比值的一百倍）
	 * @return       : BigDecimal
	 * @param        : @param lastTurnover
	 * @param        : @param lastLTurnover
	 * @param        : @return
	 * modified      : lz ,  2013-2-25  下午04:43:03
	 * @see          : 
	 * *******************************************
	 */
	public static BigDecimal getRiseRate(BigDecimal lastTurnover, BigDecimal lastLTurnover){
		BigDecimal riseRate=null;
		BigDecimal defaultGrade=new BigDecimal(Const.MERGRADE_DEFAULT);
		if(lastTurnover!=null && lastTurnover.compareTo(defaultGrade)!=0 && 
				lastLTurnover!=null && lastLTurnover.compareTo(defaultGrade)!=0 
				&& lastLTurnover.compareTo(new BigDecimal(0))!=0){
			BigDecimal sub=lastTurnover.subtract(lastLTurnover);
			sub=sub.multiply(new BigDecimal(100));//数据库中保存的是真正的比值的100倍
	        riseRate=sub.divide(lastLTurnover,Const.MERGRADE_SCALE,BigDecimal.ROUND_HALF_UP);
		}
		return riseRate;
	}
/**
 * 根据交易额和核减金额，计算出核减率（为真正比值的一百倍）
 * @param tradeSum
 * @param reduceSum
 * @return
 */
	public static BigDecimal getReduceRate(BigDecimal tradeSum, BigDecimal reduceSum){
		BigDecimal defaultGrade=new BigDecimal(Const.MERGRADE_DEFAULT);
		if(reduceSum!=null && reduceSum.compareTo(defaultGrade)!=0 && 
				tradeSum!=null && tradeSum.compareTo(defaultGrade)!=0
				&& tradeSum.compareTo(new BigDecimal(0))!=0){
			reduceSum=reduceSum.multiply(new BigDecimal(100));//数据库中保存的是真正的比值的100倍
			return reduceSum.divide(tradeSum,Const.MERGRADE_SCALE,BigDecimal.ROUND_HALF_UP);
		}else{
			return null;
		}
	}
	
/**
 * 根据交易额生成交易额指标，加权30%
 * 100分：当月交易额  ≥ 400万；
 * 90分： 200万 ≤ 当月交易额  < 400万；
 * 80分： 100万 ≤ 当月交易额  < 200万；
 * 70分： 30万  ≤ 当月交易额  < 100万；
 * 60分： 10万  ≤ 当月交易额  < 30万；
 * 50分： 5万   ≤ 当月交易额  < 10万；
 * 30分： 当月交易额  < 5万
 */
	public static BigDecimal getTurnoverIndex(BigDecimal turnover){
		BigDecimal turnoverIndex=null;
		if(turnover!=null){
			Double turnoverD=turnover.doubleValue();
			if(turnoverD!=Const.MERGRADE_DEFAULT){
				Double weight=0.3;
				int standardGrade=0;
				//交易额的单位为分
				if(turnoverD<5000000) standardGrade=30;
				else if(turnoverD<10000000) standardGrade=50;
				else if(turnoverD<30000000) standardGrade=60;
				else if(turnoverD<100000000) standardGrade=70;
				else if(turnoverD<200000000) standardGrade=80;
				else if(turnoverD<400000000) standardGrade=90;
				else standardGrade=100;
				turnoverIndex = new BigDecimal(standardGrade*weight);
			}
		}
		return turnoverIndex;
	}
	/**
	 * 根据交易额增长率生成交易额增长率指标，加权15%；
	 * 100分：当月交易额增长率 ≥  20%；
	 * 90分：10% ≤ 当月交易额增长率 < 20%；
	 * 80分：5%  ≤ 当月交易额增长率 < 10%；
	 * 70分：-10% ≤ 当月交易额增长率 < 5%；
	 * 60分：当月交易额增长率 < -10%
	 */
	public static BigDecimal getRiseRateIndex(BigDecimal riseRate){
		BigDecimal riseRateIndex=null;
		if(riseRate!=null){
			Double riseRateD=riseRate.doubleValue();
			if(riseRateD!=Const.MERGRADE_DEFAULT){
				Double weight=0.15;
				int standardGrade=0;
				if(riseRateD<-10) standardGrade=60;
				else if(riseRateD<5) standardGrade=70;
				else if(riseRateD<10) standardGrade=80;
				else if(riseRateD<20) standardGrade=90;
				else standardGrade=100;
				riseRateIndex = new BigDecimal(standardGrade*weight);
			}
		}
		return riseRateIndex;
	}
	/** 游戏类商户评分指标:(T-2)月账单核减率，加权10%
	 *	100分：:(T-2)月账单核减率在0.2%以下
	 *	90分：:(T-2)月账单核减率在0.2%-0.5%（含0.2%）
	 *	80分：:(T-2)月账单核减率在0.5%-1%（含0.5%）
	 *	60分：:(T-2)月账单核减率在1%-2%（含1%）                       
	 *	40分：:(T-2)月账单核减率在2%-3%（含2%）
	 *	20分：:(T-2)月账单核减率在3%以上（含3%）
	 * @param reduceRate
	 * @return
	 */
	public static BigDecimal getGameMerFTGrade(BigDecimal reduceRate){
		BigDecimal gameMerFTGrade=null;
		if(reduceRate==null)
			return null;
		Double reduceRateD=reduceRate.doubleValue();
		if(reduceRateD!=Const.MERGRADE_DEFAULT){
			Double falsetradeWeight=0.1;
			int standardGrade=0;
			if(reduceRateD<0.2) standardGrade=100;
			else if(reduceRateD<0.5) standardGrade=90;
			else if(reduceRateD<1) standardGrade=80;
			else if(reduceRateD<2) standardGrade=60;
			else if(reduceRateD<3) standardGrade=40;
			else standardGrade=20;
			gameMerFTGrade = new BigDecimal(standardGrade*falsetradeWeight);
		}
		return gameMerFTGrade;
	}
	/**	非游戏类商户评分指标：(T-2)月账单核减率，加权10%
	 *	100分：:(T-2)月账单核减率在3%以下
	 *	90分：(T-2)月账单核减率在3%-5%（含3%）
	 *	80分：(T-2)月账单核减率在5%-8%（含5%）
	 *	60分：(T-2)月账单核减率在8%-10%（含8%）                       
	 *	40分：(T-2)月账单核减率在10%-15%（含10%）
	 *	20分：(T-2)月账单核减率在15%以上（含15%）
	 * @param reduceRate
	 * @return
	 */
	public static BigDecimal getOtherMerFTGrade(BigDecimal reduceRate){
		BigDecimal otherMerFTGrade=null;
		if(reduceRate==null)
			return null;
		Double reduceRateD=reduceRate.doubleValue();
		if(reduceRate!=null && reduceRateD!=Const.MERGRADE_DEFAULT){
			Double falsetradeWeight=0.1;
			int standardGrade=0;
			if(reduceRateD<3) standardGrade=100;
			else if(reduceRateD<5) standardGrade=90;
			else if(reduceRateD<8) standardGrade=80;
			else if(reduceRateD<10) standardGrade=60;
			else if(reduceRateD<15) standardGrade=40;
			else  standardGrade=20;
			otherMerFTGrade =  new BigDecimal(standardGrade*falsetradeWeight);
		}
		return otherMerFTGrade;
	}

	/**根据客诉数和交易笔数计算出投诉率，再生成客诉指标，加权30%
	 * 100分：当月投诉为0
	 * 90分：当月投诉率在1‰以下（含1‰）
	 * 80分：当月投诉率在2‰-1‰（含2‰）  
	 * 70分：当月投诉率在3‰-2‰（含3‰）   
	 * 60分：当月投诉率在4‰-3‰（含4‰）
	 * 30分及以下：当月投诉率在4‰-10‰（含4‰）
	 * 0分：当月投诉率在10‰以上
	 */
	public static BigDecimal getComplaintIndex(Integer complaintCount, Integer tradingCount) {
		BigDecimal complaintIndex=null;
		if(complaintCount!=null && complaintCount!=Const.MERGRADE_DEFAULT && tradingCount!=null && tradingCount!=0 && tradingCount!=Const.MERGRADE_DEFAULT){
			Double complaintWeight = 0.3;
			double complaintRate =complaintCount * 1.0 / tradingCount;
			int standardGrade = 0;
			if (complaintRate == 0) standardGrade = 100;
			else if (complaintRate <= 0.001) standardGrade = 90;
			else if (complaintRate <= 0.002) standardGrade = 80;
			else if (complaintRate <= 0.003) standardGrade = 70;
			else if (complaintRate <= 0.004) standardGrade = 60;
			else if (complaintRate <= 0.01) standardGrade = 30;
			else standardGrade = 0;
			complaintIndex = new BigDecimal(standardGrade * complaintWeight);
		}
		return complaintIndex;
	}
	/**为前三个月评分均存在的商户生成等级
	 * 
	 */
	public static String getRank(BigDecimal lastTotal,BigDecimal lastLTotal,BigDecimal lastLLTotal){
		Double lastTotalD=lastTotal.doubleValue();
		Double lastLTotalD=lastLTotal.doubleValue();
		Double lastLLTotalD=lastLLTotal.doubleValue();
		if(lastTotalD>=90 && lastLTotalD>=90 && lastLLTotalD>=90){
			return "1";
		}
		else if(lastTotalD>=80 && lastLTotalD>=80 && lastLLTotalD>=80){
			return "2";
		}
		else if(lastTotalD>=70 && lastLTotalD>=70 && lastLLTotalD>=70){
			return "3";
		}
		else if(lastTotalD>=60 && lastLTotalD>=60 && lastLLTotalD>=60){
			return "4";
		}
		else if(lastTotalD<60 && lastLTotalD<60 && lastLLTotalD<60){
			return "5";
		}
		else
			return null;
	}
	
	public static String getOperState(Timestamp inTime){
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		c.setTime(inTime);
		c.add(Calendar.MONTH, 4);
		String currentMonth=df.format(c.getTime());
		Timestamp ts=Timestamp.valueOf(currentMonth+"-01 00:00:00");//转为正式运营的时间
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(ts.after(now)){
			return "试运营";
		}else{
			return "正常";
		}
	}
}
