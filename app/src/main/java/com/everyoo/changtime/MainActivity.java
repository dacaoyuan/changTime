package com.everyoo.changtime;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private TextView wangluoT, bendiT;
    private Button getButton, checkButton;
    private TimerEntity entity;
    private TimeRunningReceiver receiver;
    private Intent mIntent;
    private String bendiTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mIntent.putExtra("time", entity);
            // 确保一个Service在运行（方法有点土，待改进）
            MainActivity.this.unbindService(conn);
            MainActivity.this.stopService(mIntent);
            MainActivity.this.startService(mIntent);
            MainActivity.this.bindService(mIntent, conn, BIND_AUTO_CREATE);
        }
    };

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        this.startService(mIntent);
        this.bindService(mIntent, conn, BIND_AUTO_CREATE);
    }

    //初始化
    private void init() {
        bendiT = (TextView) findViewById(R.id.bendi);
        wangluoT = (TextView) findViewById(R.id.wangluo);
        getButton = (Button) findViewById(R.id.getButton);
        getButton.setOnClickListener(this);
        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);

        mIntent = new Intent(this, TimeService.class);

        receiver = new TimeRunningReceiver();
        IntentFilter filter = new IntentFilter("com.example.time.run");
        this.registerReceiver(receiver, filter);
    }

    //设置网络时间
    private void setText(TimerEntity entity) {
        wangluoT.setText(formatText(entity.year) + "-" + formatText(entity.month) + "-" + formatText(entity.day)
                + " " + formatText(entity.hour) + ":" + formatText(entity.minute) + ":" + formatText(entity.second));
    }

    //排版
    private String formatText(int num) {
        String txt = null;
        if (num < 10) {
            txt = "0" + num;
        } else {
            txt = String.valueOf(num);
        }
        return txt;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        this.unbindService(conn);
        this.stopService(mIntent);
    }

    /**
     * 获取网络时间
     */
    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();
            uc.connect();
            long ld = uc.getDate();
            Date date = new Date(ld);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            entity = TimerEntity.instance(calendar);
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 时间接收者
     *
     * @author hys
     */
    class TimeRunningReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            entity = (TimerEntity) intent.getSerializableExtra("newTime");
            if (entity != null) {
                setText(entity);
            }
            bendiTime = intent.getStringExtra("bendiTime");
            bendiT.setText(bendiTime);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getButton:
                new Thread(this).start();
                break;
            case R.id.checkButton:
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, entity.year);
                calendar1.set(Calendar.MONTH, entity.month - 1);
                calendar1.set(Calendar.DAY_OF_MONTH, entity.day);
                calendar1.set(Calendar.HOUR_OF_DAY, entity.hour);
                calendar1.set(Calendar.MINUTE, entity.minute);
                calendar1.set(Calendar.SECOND, entity.second);
                long when = calendar1.getTimeInMillis();
                SystemClock.setCurrentTimeMillis(when);
                break;
        }
    }
}
