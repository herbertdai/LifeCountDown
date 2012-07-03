package com.howfun.lifecountdown.widget;

public class Accout {
	public Accout(int serviceId, int appId, long timeLeft, double totalSec) {
		mServiceId = serviceId;
		mAppId = appId;
		mTimeLeft = timeLeft;
		mRun = true;
		mTotalTime = totalSec;
	}
	public boolean mRun;
	public int mServiceId;
	public int mAppId;
	public long mTimeLeft;
	public double mTotalTime;
}