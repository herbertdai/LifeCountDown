package com.howfun.lifecountdown.widget;

public class Accout {
	public Accout(int serviceId, int appId, long time) {
		mServiceId = serviceId;
		mAppId = appId;
		mTimeLeft = time;
		mRun = true;
	}
	public boolean mRun;
	public int mServiceId;
	public int mAppId;
	public long mTimeLeft;
}