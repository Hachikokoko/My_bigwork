package com.example.thememorandum.Alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.thememorandum.Utils.MyApplication;


/**
 * 接收定时到时间时消息的service
 */
public class AlarmService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    //接收到消息走这里
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        //跳转到定时提醒页面intent中包含了闹铃的相关信息
        Intent alarmIntent = new Intent(MyApplication.getContext(), AlarmScreenActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);
        return super.onStartCommand(intent, flags, startId);
    }
}
