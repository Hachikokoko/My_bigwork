package com.example.thememorandum.Utils;

import android.content.Context;
import android.widget.Toast;

import com.example.thememorandum.Alarm.AlarmModel;
import com.example.thememorandum.R;
import com.example.thememorandum.db.AlarmTableManager;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class AlarmUtils
{
    /*
    * 获取闹钟列表，将布尔数组转换为字符串数组
    * */
    public static String getRepeatDays(AlarmModel alarm) {
        StringBuilder repeat_days = new StringBuilder();
        if (alarm.getRepeating_day(AlarmModel.Mon)) {
            repeat_days.append("周一,");
        }
        if (alarm.getRepeating_day(AlarmModel.Tues)) {
            repeat_days.append("周二,");
        }
        if (alarm.getRepeating_day(AlarmModel.Wed)) {
            repeat_days.append("周三,");
        }
        if (alarm.getRepeating_day(AlarmModel.Thur)) {
            repeat_days.append("周四,");
        }
        if (alarm.getRepeating_day(AlarmModel.Fri)) {
            repeat_days.append("周五,");
        }
        if (alarm.getRepeating_day(AlarmModel.Sat)) {
            repeat_days.append("周六, ");
        }
        if (alarm.getRepeating_day(AlarmModel.Sun)) {
            repeat_days.append("周日,");
        }
        if (repeat_days.length() != 0)
            repeat_days.deleteCharAt(repeat_days.length() - 1);
        else
            return "不重复";
        return repeat_days.toString();
    }
    /**
     * 保存或更新警报时，将布尔数组转换为字符串
     *
     * @param alarm
     * @return
     */
    public static String makeRepeatDays(AlarmModel alarm) {
        StringBuilder repeat_days = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            repeat_days.append(String.valueOf(alarm.getRepeating_day(i)) + ",");
        }
        if (repeat_days.length() != 0)
            repeat_days.deleteCharAt(repeat_days.length() - 1);
        return repeat_days.toString();
    }

    //开启闹钟响铃提示
    public static void showFirstRemindTime(Context context, int day, long id) {
        AlarmTableManager manager = new AlarmTableManager(context);
        AlarmModel model = manager.getAlarm(id);
        int setHour = model.getHour();
        int setMin = model.getMinute();

        long remindDay = day;
        long remindHour = 0, remindMin = 0;
        int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int nowMin = Calendar.getInstance().get(Calendar.MINUTE);
        String toast = "";

        long allMin = setHour * 60 + setMin - nowHour * 60 - nowMin;
        if (allMin >= 0) {
            remindHour = allMin / 60;
            remindMin = allMin % 60;
        } else {
            remindDay--;
            remindHour = (24 * 60 + allMin) / 60;
            remindMin = (24 * 60 + allMin) % 60;
        }
        if (remindDay > 0)
            toast = remindDay + "天 " + remindHour + "小时 " + remindMin + "分钟后提醒";
        else
            toast = remindHour + "小时 " + remindMin + "分钟后提醒";
        Toasty.custom(MyApplication.getContext(), toast, R.drawable.shiwutixing,R.color.colorAccent,Toast.LENGTH_LONG,true,true).show();

    }

}
