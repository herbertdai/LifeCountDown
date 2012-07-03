package com.howfun.lifecountdown.widget;

import java.util.HashMap;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class CountDownService extends Service {

	public static final String EXTRA_TIME_CHANGE = "com.howfun.CountDownService extra time changed.";
	public static final String EXTRA_TOTAL_TIME = "com.howfun.CountDownService extra total time";
	public static final String ACTION_TIME_CHANGE = "com.howfun.lifecountdown.widget.ACTION_TIME_CHANGE";
	protected static final long UPDAT_INT = 10;
	private LocalBinder binder = new LocalBinder();

	public class LocalBinder extends Binder {

		public CountDownService getService() {
			return CountDownService.this;
		}

	}

	public void onCreate() {
		Log.e("CountDownService", "service OnCreate()........");

	}
	
    	
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("CountDownService", "service starting , id = " + startId);
		
		final int serviceId = startId;
		long time = intent.getLongExtra(AppWidgetConfigure.EXTRA_COUNT_DOWN_SEC, 10);
		double totalSec = intent.getDoubleExtra(AppWidgetConfigure.EXTRA_TOTAL_SEC, 10000);
		int appid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		
		final Accout accout = new Accout(startId, appid, time, totalSec);
		LifeCountDownWidgetProvider.appIds.put(startId, accout);
		
		HandlerThread ht = new HandlerThread("Count down service thread." + startId, Process.THREAD_PRIORITY_BACKGROUND) {

			public void run() {
				while (accout.mRun) {
					// Send Broadcast to update time.
					sendBoradcast(serviceId);
					
					try {
						sleep(UPDAT_INT * 1000);
						accout.mTimeLeft -= UPDAT_INT;
						if (accout.mTimeLeft <= 0) {
							accout.mRun = false;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};
		ht.start();


		return START_STICKY;
	}
	
	
	protected void sendBoradcast(final int serviceId) {
		final Accout accout = LifeCountDownWidgetProvider.appIds.get(serviceId);
		
		Intent timeChange = new Intent();
		timeChange.putExtra(EXTRA_TIME_CHANGE, accout.mTimeLeft);
		timeChange.putExtra(EXTRA_TOTAL_TIME, accout.mTotalTime);
		timeChange.setAction(ACTION_TIME_CHANGE);
		
		timeChange.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, accout.mAppId);
		this.getApplicationContext().sendBroadcast(timeChange);
		Log.e("CountDownService", "Send broadcast to appid = " + accout.mAppId);
		Log.e("CountDownService", "Send timeleft = " + accout.mTimeLeft + " send total = " + accout.mTotalTime);
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

}
