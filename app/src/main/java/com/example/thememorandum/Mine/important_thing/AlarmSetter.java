package com.example.thememorandum.Mine.important_thing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.thememorandum.Alarm.AlarmService;

import java.util.Calendar;

public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Cursor cursor = context.getContentResolver().query(ReminderContract.All.CONTENT_URI,
                null, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }

        try {
            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndex(ReminderParams.TYPE));
                long time = cursor.getLong(cursor.getColumnIndex(ReminderParams.TIME));

                if (ReminderType.fromString(type) == ReminderType.ALERT
                        && time > Calendar.getInstance().getTimeInMillis()) {
                    Intent service = new Intent(context, AlarmService02.class);
                    service.setAction(AlarmService02.CREATE);
                    service.putExtra(ReminderParams.ID, cursor.getInt(cursor.getColumnIndex(
                            ReminderParams.ID)));
                    context.startService(service);
                }
            }
        } finally {
            cursor.close();
        }

    }

}
