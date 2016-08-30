package com.everyoo.changtime;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TimeService extends Service{

	private boolean flag = true;
	private TimeManager manager;
	private Intent mIntent;
	private TimerEntity entity;
	private SimpleDateFormat format;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate() {
		super.onCreate();
		manager = TimeManager.instance();
		mIntent = new Intent();
		mIntent.setAction("com.example.time.run");
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		entity = (TimerEntity) intent.getSerializableExtra("time");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (flag) {
					entity = manager.running(entity);
					mIntent.putExtra("newTime", entity);
					String time = format.format(Calendar.getInstance().getTime());
					mIntent.putExtra("bendiTime", time);
					TimeService.this.sendBroadcast(mIntent);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
	}

}
