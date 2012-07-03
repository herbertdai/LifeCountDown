package com.howfun.lifecountdown.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class AppWidgetConfigure extends Activity implements
		View.OnClickListener {
	private static final String TAG = "AppWidgetConfigure";

	private static final int LIFE_LEN = 80;
	public static final String EXTRA_COUNT_DOWN_SEC = "com.howfun.extra count down seconds";
	public static final String EXTRA_TOTAL_SEC = "com.howfun.extra total count down sec";
	private TextView mCurDate;
	private EditText mBirtInput;
	private Button mStartBtn;
	private TextView mTimeLeft;
	private GregorianCalendar mCurCalendar;
	private int mAppWidgetId;
	private EditText mHowLongYearInput;

	private double mTotalSec;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure);

		findViews();

		init();

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			Log.e(TAG, "appWidget id = " + mAppWidgetId + "...............................");
		} else {
			Log.e(TAG, "extra is null...............................");
		}
	}

	private void findViews() {
		mCurDate = (TextView) findViewById(R.id.cur_date_text);
		mBirtInput = (EditText) findViewById(R.id.birth_input);
		mStartBtn = (Button) findViewById(R.id.comfirm_btn);
		mHowLongYearInput = (EditText)findViewById(R.id.how_long_year_input);

		mStartBtn.setOnClickListener(this);

	}

	private void init() {

		mCurCalendar = new GregorianCalendar();
		mCurDate.setText("Current is:" + mCurCalendar.get(Calendar.YEAR)
				+ " / " + (mCurCalendar.get(Calendar.MONTH) + 1) + " / "
				+ mCurCalendar.get(Calendar.DAY_OF_MONTH));
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.comfirm_btn:

			long sec = 0;
			try {
				sec = getLeftSec();
			} catch (Exception e) {
				return;
			}

			// Update the widget manually.
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(this);
			RemoteViews views = new RemoteViews(getPackageName(),
					R.layout.appwidget_layout);
			appWidgetManager.updateAppWidget(mAppWidgetId, views);

			passTimetoService(sec, mTotalSec);

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();

			break;
		default:
			break;
		}
	}

	private void passTimetoService(long sec, double totalsec) {

		Intent service = new Intent(this, CountDownService.class);
		service.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		service.putExtra(EXTRA_COUNT_DOWN_SEC, sec);
		service.putExtra(EXTRA_TOTAL_SEC, totalsec);
		Log.e("---------", "Send total = " + totalsec);
		this.startService(service);

	}

	private long getLeftSec() throws Exception {
		String dateStr = mBirtInput.getText().toString();
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = sFormat.parse(dateStr);
		} catch (Exception e) {
			Toast.makeText(AppWidgetConfigure.this,
					"Please input Birth like: 19840101", 3000).show();
			throw (new Exception());
		}

		if (date == null) {
			Toast.makeText(AppWidgetConfigure.this,
					"Please input Birth like: 19840101", 3000).show();

			throw (new Exception());
		}
		
		int lifeLen =  LIFE_LEN;
		try {
			lifeLen = Integer.valueOf(mHowLongYearInput.getText().toString());
		}catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(AppWidgetConfigure.this,
					"Assume you can live " + LIFE_LEN + " years long.", 3000).show();
			lifeLen = LIFE_LEN;
		}

		GregorianCalendar deadCalendar = new GregorianCalendar(date.getYear()
				+ 1900 + lifeLen, date.getMonth(), date.getDate());

		// calc the left time.
		// get dead line, left = dead - current
		long leftSec = diffCalendar(deadCalendar, mCurCalendar);
		long leftDay = leftSec / 60 / 60 / 24;
		long leftYear = leftDay / 365;
		mTotalSec = lifeLen * 365 ;

		Log.e("Life left", " day:" + leftDay);
		Log.e("Life left", " year:" + leftYear);
		Log.e("Life left", " total sec :" + mTotalSec);
		return leftSec;
	}

	/**
	 * 
	 * @param deadCalendar
	 * @param mCurCalendar2
	 * @return period in ms.
	 */
	private long diffCalendar(GregorianCalendar deadCalendar,
			GregorianCalendar curCalendar) {
		long deadMs = deadCalendar.getTimeInMillis();
		long curMs = curCalendar.getTimeInMillis();

		Log.e("Count down >>>",
				"dead year = " + deadCalendar.get(Calendar.YEAR) + "mon = "
						+ deadCalendar.get(Calendar.MONTH) + " day = "
						+ deadCalendar.get(Calendar.DAY_OF_MONTH));

		Log.e("Count down >>>", "cur year = " + curCalendar.get(Calendar.YEAR)
				+ "mon = " + curCalendar.get(Calendar.MONTH) + " day = "
				+ curCalendar.get(Calendar.DAY_OF_MONTH));

		return (deadMs - curMs) / 1000;
	}
}
