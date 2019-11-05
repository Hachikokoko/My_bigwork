package com.example.thememorandum.Alarm;

public class AlarmModel {
    //天数的静态常亮
    public static final int Sun = 0;
    public static final int Mon = 1;
    public static final int Tues = 2;
    public static final int Wed = 3;
    public static final int Thur = 4;
    public static final int Fri = 5;
    public static final int Sat = 6;

    private long id;
    //闹铃的提示信息
    private String name;
    private String date;
    //小时
    private int hour;
    //分钟
    private int minute;

    //标签的布尔数组
    private String label;
    //重复天数的布尔数组
    private boolean repeating_days[];
    //闹铃地址
    private String tone;
    //是否响铃的判断
    private boolean enable;
    //是否响铃的判断
    private boolean fiished;

    public AlarmModel() {
        repeating_days = new boolean[7];
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean[] getRepeating_days() {
        return repeating_days;
    }

    public void setRepeating_days(boolean[] repeat) {
        repeating_days = repeat;
    }

    public boolean getRepeating_day(int day) {
        return repeating_days[day];
    }


    public void setRepeating_day(int day, boolean value) {
        repeating_days[day] = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isfisnhed() {
        return fiished;
    }

    public void setfished(boolean fiished) {
        this.fiished = fiished;
    }
}
