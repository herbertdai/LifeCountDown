package com.howfun.lifecountdown;

/**
 * Count down your life. Assume your max age is 80.
 * @author herbert dai
 * @date 2012.6.26
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LifeCountDownActivity extends Activity implements OnClickListener {

	private static final int LIFE_LEN = 80;
	private TextView mCurDate;
	private EditText mBirtInput;
	private Button mStartBtn;
	private TextView mTimeLeft;
	private GregorianCalendar mCurCalendar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViews();

		init();

	}

	private void findViews() {
		mCurDate = (TextView) findViewById(R.id.cur_date_text);
		mBirtInput = (EditText) findViewById(R.id.birth_input);
		mStartBtn = (Button) findViewById(R.id.comfirm_btn);
		mTimeLeft = (TextView) findViewById(R.id.time_left_text);

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
			Log.e("LifeCountDownActivity", "Start count down");
			String dateStr = mBirtInput.getText().toString();
			SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = null;
			try {
				date = sFormat.parse(dateStr);
			} catch (Exception e) {
				Toast.makeText(LifeCountDownActivity.this,
						"Please input Birth like: 198401", 3000).show();

			}
			if (date == null) {
				Toast.makeText(LifeCountDownActivity.this,
						"Please input Birth like: 198401", 3000).show();
				return;
			}
		
			GregorianCalendar deadCalendar = new GregorianCalendar(
					date.getYear() + 1900 + LIFE_LEN, date.getMonth(), date.getDate());

			// calc the left time.
			// get dead line, left = dead - current
			long leftSec = diffCalendar(deadCalendar, mCurCalendar);
			long leftDay = leftSec / 60 / 60 / 24;
			long leftYear = leftDay / 365;
			
			Log.e("Life left", " day:" + leftDay) ;
			Log.e("Life left", " year:" + leftYear);
			mTimeLeft.setText("Day left: " + leftDay + " \n" + "Year left: " + leftYear);
			
			// start count down.
			
			break;
		default:
			break;
		}

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
		
		Log.e("Count down >>>",
				"cur year = " + curCalendar.get(Calendar.YEAR) + "mon = "
						+ curCalendar.get(Calendar.MONTH) + " day = "
						+ curCalendar.get(Calendar.DAY_OF_MONTH));

		return (deadMs - curMs) / 1000;
	}
}