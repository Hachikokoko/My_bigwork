package com.example.thememorandum.Mine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.View.bolang_View;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Timer extends AppCompatActivity implements View.OnClickListener {
    private Button backbtn;
    private Button start_btn;
    private Button reset_btn;
    private TextView textView01;
    private TextView textView02;
    private TextView textView03;
    //执行周期性或定时任务类
    private ScheduledExecutorService service;
    //总微秒
    private int milliSeconds;
    //总秒
    private int seconds;
    //总分钟
    private int minute;
    //线程是否停止的判断
    private boolean isStart = false;
    //是否重置的判断重置了handler就不在接收消息
    private boolean isReset = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        backbtn=findViewById(R.id.back_timer);
        start_btn=findViewById(R.id.start_btn);
        reset_btn=findViewById(R.id.reset_btn);
        textView01=findViewById(R.id.time_tv1);
        textView02=findViewById(R.id.time_tv2);
        textView03=findViewById(R.id.time_tv3);
        backbtn.setOnClickListener(this);
        start_btn.setOnClickListener(this);
        reset_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_timer:
                Timer.this.finish();
                break;
            case R.id.start_btn:
                isReset = false;
                if (isStart) {
                    start_btn.setBackground(getResources().getDrawable(R.drawable.kaishi));
                    isStart = false;
                    service.shutdown();
                } else {
                    start_btn.setBackground(getResources().getDrawable(R.drawable.tingzhi));
                    isStart = true;
                    start();
                }
                break;
            case R.id.reset_btn:
                start_btn.setBackground(getResources().getDrawable(R.drawable.kaishi));
                isReset = true;
                isStart = false;
                if (service != null) {
                    resetTime();
                }
                break;
        }
    }
    //设置时间数据
    private void setData() {

        if(!isReset){
            if (milliSeconds == 99) {
                milliSeconds = 0;
                if (seconds == 59) {
                    seconds = 0;
                    minute = minute + 1;

                    if (minute == 59) {
                        minute = 0;
                        Toasty.error(MyApplication.getContext(), "已是最大数值", Toast.LENGTH_SHORT).show();
                        start_btn.setText("开始");
                        isStart = false;
                        service.shutdown();
                    }
                } else {
                    seconds = seconds + 1;
                }
            } else {
                milliSeconds = milliSeconds + 1;
            }
            setText();
        }
    }
    //数据转换成两位String
    private String getNum(int num) {
        return String.format("%02d", num);
    }
        //显示时间
    private void setText() {

        textView01.setText(getNum(minute) + " : ");
        textView02.setText(getNum(seconds) + " . ");
        textView03.setText(getNum(milliSeconds));
    }
    //重置后的操作
    private void resetTime() {
        service.shutdown();
        milliSeconds = 0;
        seconds = 0;
        minute = 0;
        setText();

    }

    //启动倒计时
    private void start() {
        service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 10, 10, TimeUnit.MILLISECONDS);
    }
}
