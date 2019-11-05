package com.example.thememorandum.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thememorandum.Alarm.AlarmModel;
import com.example.thememorandum.Utils.AlarmUtils;

import java.util.ArrayList;
import java.util.List;

public class AlarmTableManager
{
    private MyDBHelper helper;
    private SQLiteDatabase db;

    public AlarmTableManager(Context context)
    {
        helper=new MyDBHelper(context,"TP.db",null,1);
    }

    private AlarmModel populateAlarmModel(Cursor cursor)
    {
        AlarmModel alarm=new AlarmModel();
        alarm.setId(cursor.getLong(cursor.getColumnIndex("id")));
        alarm.setName(cursor.getString(cursor.getColumnIndex("alarmName")));
        alarm.setDate(cursor.getString(cursor.getColumnIndex("date")));
        alarm.setHour(cursor.getInt(cursor.getColumnIndex("alarmhour")));
        alarm.setMinute(cursor.getInt(cursor.getColumnIndex("alarmminute")));
        alarm.setTone(cursor.getString(cursor.getColumnIndex("music")));
        alarm.setEnable(cursor.getInt(cursor.getColumnIndex("alarmenable"))==0?false:true);
        alarm.setLabel(cursor.getString(cursor.getColumnIndex("label")));
        alarm.setfished(cursor.getInt(cursor.getColumnIndex("finished")) == 0 ? false : true);

        String[] repeat_days=cursor.getString(cursor.getColumnIndex("alarmdays")).split(",");
        for(int i=0;i<7;i++)
        {
            alarm.setRepeating_day(i,repeat_days[i].equals("false")?false:true);
        }
        return alarm;
    }

    private ContentValues populateContentValues(AlarmModel alarm)
    {
        ContentValues values=new ContentValues();
        values.put("alarmName",alarm.getName());
        values.put("alarmName",alarm.getDate());
        values.put("alarmhour",alarm.getHour());
        values.put("alarmminute",alarm.getMinute());
        values.put("music",alarm.getTone());
        values.put("alarmenable",alarm.isEnable());
        values.put("alarmdays", AlarmUtils.makeRepeatDays(alarm));
        values.put("label", alarm.getLabel());
        values.put("finished", alarm.isfisnhed());
        return values;
    }

    /*
    * 获取一条数据
    * */
    public AlarmModel getAlarm(long id)
    {
        db=helper.getReadableDatabase();
        Cursor cursor = db.query("Alarm", null, "id=?", new String[]{id + ""}, null, null, null);
        if(cursor.moveToNext())
        {
            AlarmModel alarm=populateAlarmModel(cursor);
            db.close();
            return alarm;
        }
        return null;
    }
    /*
    * 获取alarmModel列表
    * */

    public List<AlarmModel> getAlarms()
    {
        try{
            List<AlarmModel> alarmList=new ArrayList<>();
            db=helper.getReadableDatabase();
            Cursor cursor=db.query("Alarm",null,null,null,null,null,null);
            while (cursor.moveToNext())
            {
                alarmList.add(populateAlarmModel(cursor));
            }
            db.close();
            if(!alarmList.isEmpty())
            {
                return alarmList;
            }
        }catch (Exception e)
        {

        }
        return null;
    }

    public List<AlarmModel> getAlarms_all()
    {
        try{
            List<AlarmModel> alarmList=new ArrayList<>();
            db=helper.getReadableDatabase();
            Cursor cursor=db.query("Alarm_All",null,null,null,null,null,null);
            while (cursor.moveToNext())
            {
                alarmList.add(populateAlarmModel(cursor));
            }
            db.close();
            if(!alarmList.isEmpty())
            {
                return alarmList;
            }
        }catch (Exception e)
        {

        }
        return null;
    }
    /*
    *
    * 插入数据
    * */
    public long createAlarm(AlarmModel alarm)
    {
        db=helper.getReadableDatabase();
        long result=db.insert("Alarm",null,populateContentValues(alarm));
        db.close();
        return result;
    }

    public long createAlarm_all(AlarmModel alarm)
    {
        db=helper.getWritableDatabase();
        long result=db.insert("Alarm_All",null,populateContentValues(alarm));
        db.close();
        return result;
    }


    /*
    * 更新数据
    * */
    public int updateAlarm(AlarmModel alarm)
    {
        db=helper.getWritableDatabase();
        int result=db.update("Alarm",populateContentValues(alarm),"id=?",new String[]{String.valueOf(alarm.getId())});
        db.close();
        return result;
    }
    public int updateAlarm_all(AlarmModel alarm)
    {
        db=helper.getWritableDatabase();
        int result=db.update("Alarm_All",populateContentValues(alarm),"id=?",new String[]{String.valueOf(alarm.getId())});
        db.close();
        return result;
    }
    public int deleteAlarm(long id) {
        db = helper.getWritableDatabase();
        int result = db.delete("Alarm",  "id=?",
                new String[] { String.valueOf(id) });
        db.close();
        return result;
    }

}
