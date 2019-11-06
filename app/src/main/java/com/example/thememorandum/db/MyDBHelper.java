package com.example.thememorandum.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="TP.db";

    //闹钟数据表
    public static final String CREATE_ALARMTABLE="create table Alarm("
        +"id integer primary key autoincrement,"
        +"alarmName text,"
        +"alarmhour integer,"
            + "label text,"
            + "finished boolean,"
        +"alarmminute interger,"
        +"alarmdays text,"
        +"music text,"
            +"date text,"
        +"alarmenable boolean)";

    //闹钟数据表保存所有数据
    public static final String CREATE_ALARMTABLE_Save="create table Alarm_All("
            +"id integer primary key autoincrement,"
            +"alarmName text,"
            + "label text,"
            + "finished boolean,"
            +"alarmhour integer,"
            +"alarmminute interger,"
            +"alarmdays text,"
            +"music text,"
            +"date text,"
            +"alarmenable boolean)";

    public static final String CREATE_DAKA="create table Clock("
            +"id integer primary key autoincrement,"
            +"date text,"
            +"tag interger)";

    private Context mcontext;
    public MyDBHelper( Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARMTABLE);
        db.execSQL(CREATE_ALARMTABLE_Save);
        db.execSQL(CREATE_DAKA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Alarm");
        db.execSQL("drop table if exists Alarm_All");
        db.execSQL("drop table if exists Clock");
        onCreate(db);
    }
}
