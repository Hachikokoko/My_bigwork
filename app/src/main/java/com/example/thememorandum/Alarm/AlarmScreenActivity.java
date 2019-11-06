package com.example.thememorandum.Alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;

public class AlarmScreenActivity extends AppCompatActivity {
    private static final int WAKELOCK_TIMEOUT = 60 * 1000;//24小时闹钟不会被系统打断
    public final String TAG = this.getClass().getSimpleName();
    private PowerManager.WakeLock mwakeLock;//电源管理类
    private MediaPlayer mediaPlayer;//媒体管理类
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        vibrator= (Vibrator) MyApplication.getContext().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{1000,10000,1000,10000},-1);
        String name = getIntent().getStringExtra(AlarmSetClock.NAME);
        int timeHour = getIntent().getIntExtra(AlarmSetClock.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(AlarmSetClock.TIME_MINUTE, 0);
        String tone = getIntent().getStringExtra(AlarmSetClock.TONE);


        TextView tvName = findViewById(R.id.alarm_screen_name);
        tvName.setText(name);

        TextView tvTime = findViewById(R.id.alarm_screen_time);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));
        Button dismissButton = findViewById(R.id.alarm_screen_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                vibrator.cancel();
                finish();
            }
        });


        mediaPlayer = new MediaPlayer();
        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                if (toneUri != null) {
                    mediaPlayer.setDataSource(this, toneUri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable releaseWakelock = new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                vibrator.cancel();
                finish();
            }
        };
        new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        // 设置窗口以保持屏幕上
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//当window被显示时，系统将它当做一个用户活动事件，以点亮屏幕
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//锁屏时仍显示该window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // 获得wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mwakeLock == null) {
            mwakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mwakeLock.isHeld()) {
            mwakeLock.acquire();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mwakeLock != null && mwakeLock.isHeld()) {
            mwakeLock.release();
        }
    }
}
