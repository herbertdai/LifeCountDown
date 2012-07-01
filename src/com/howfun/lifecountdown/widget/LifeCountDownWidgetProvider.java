package com.howfun.lifecountdown.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class LifeCountDownWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "LifeCountDownWidgetProvider";
	private long timeLeftSec;
	private AppWidgetManager mAppWidgetManger;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.e(TAG, "onEnabled---------------------------------------");
		mAppWidgetManger = AppWidgetManager.getInstance(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		final int appWidgetId = intent.getIntExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

		if (action.equals(CountDownService.ACTION_TIME_CHANGE)) {
			timeLeftSec = intent.getLongExtra(
					CountDownService.EXTRA_TIME_CHANGE, 10);

			// Update the widget manually.
			//NOTICE: AppWidgetManger.updateAppWidget() would NOT callback onUpdate().
			//So you have to implement update just like the onUpdate().
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_layout);
			
			int min = (int) (timeLeftSec / 60);
			int hour = min / 60;
			int day = hour/ 24;
			int month = day / 30;
			int year = day / 365;
			String timeFormat = context.getResources().getString(R.string.time_format);
			views.setTextViewText(R.id.time_left_text, String.format(timeFormat, year, month, day, hour, min, timeLeftSec));
			mAppWidgetManger = AppWidgetManager.getInstance(context);
			if (mAppWidgetManger != null) {
				mAppWidgetManger.updateAppWidget(appWidgetId, views);
			}
		}
		super.onReceive(context, intent);

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.e(TAG, "onUpdate........................-------");

		for (int i = 0; i < appWidgetIds.length; ++i) {

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_layout);
			views.setTextViewText(R.id.time_left_text, " Sec left: "
					+ timeLeftSec);

			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
			Log.e(TAG, "onUpdate......appIds =  " + appWidgetIds[i]
					+ "..................-------");
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}