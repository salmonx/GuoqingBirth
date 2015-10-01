

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity {

	PendingIntent paIntent;
	SmsManager smsManager;
	Date dd;
	String msg;
	String tel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		paIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
		smsManager = SmsManager.getDefault();

		findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText e = (EditText) findViewById(R.id.year);
				int year = Integer.parseInt(e.getText().toString());
				e = (EditText) findViewById(R.id.month);
				int month = Integer.parseInt(e.getText().toString());
				e = (EditText) findViewById(R.id.day);
				int day = Integer.parseInt(e.getText().toString());
				e = (EditText) findViewById(R.id.hour);
				int hour = Integer.parseInt(e.getText().toString());
				e = (EditText) findViewById(R.id.min);
				int min = Integer.parseInt(e.getText().toString());
				e = (EditText) findViewById(R.id.sec);
				int sec = Integer.parseInt(e.getText().toString());

				final String strtime = "" + year + "-" + month + "-" + day + " "
						+ hour + ":" + min + ":" + sec;
				
				
				e = (EditText) findViewById(R.id.tel);
				tel = e.getText().toString();
				e = (EditText) findViewById(R.id.msg);
				msg =  e.getText().toString();
				
				Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				try {
					dd = df.parse(strtime);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				new Thread() {
					public void run() {

						while (true) {
							
							try {
								Date curDate = new Date(System
										.currentTimeMillis());

								int diff = (int) (dd.getTime() - curDate
										.getTime());
								Log.d("diff",diff/1000+" "+strtime);
								
								if (diff > 3600*100) {
									Thread.sleep(3000 * 1000); // 50min
								} else if (diff > 60 * 1000) { // 1min
									Thread.sleep(50 * 1000); // 50 s
								} else if (diff > 1000) { // 1s
									Thread.sleep(200); // 0.5 s
								} else {
									
									smsManager.sendTextMessage(tel,
											null, msg, paIntent, null);
									return;
								}

							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}

					};

				}.start();

				return;
			}
		});
	}

}
