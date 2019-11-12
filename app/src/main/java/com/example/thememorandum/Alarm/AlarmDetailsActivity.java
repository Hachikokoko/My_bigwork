package com.example.thememorandum.Alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.thememorandum.R;
import com.example.thememorandum.Utils.AlarmUtils;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.Utils.NetworkUtil;
import com.example.thememorandum.Utils.Weather_WeekTool;
import com.example.thememorandum.Utils.Weather_streamTool;
import com.example.thememorandum.Mine.Weather.Weather;
import com.example.thememorandum.Mine.Weather.WeatherActivity;
import com.example.thememorandum.Mine.Weather.WeatherInfo;
import com.example.thememorandum.db.AlarmTableManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class AlarmDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private AlarmTableManager tableManager;
    private AlarmModel alarmModel;
    private EditText name;

    private LinearLayout setTime;
    private TextView setalarmtime;

    private LinearLayout setlabel;
    private TextView label;

    private LinearLayout setRepeat;
    private TextView repeat;

    private LinearLayout setTone;
    private TextView tone;

    private FrameLayout cancel;

    private RelativeLayout save;

    private Calendar calendar;
    private int setalarm_hour;
    private int setalarm_min;
    private String getlabel;
    private long id;
    private final static int SUCCESS = 1;
    private final static int FAIL = 0;
    private final static  int update=1;

    private TextView wendu;
    private String date;

    private TextView type;
    private TextView min;
    private TextView max;
    private RelativeLayout relativeLayout;
    private String nowcity;

    private LocationClient locationClient = null;
    private LocationClientOption option;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    WeatherInfo info = (WeatherInfo) msg.obj;
                    updateUI(info);
                    break;
                case FAIL:
                    String str = (String) msg.obj;
                    Toasty.custom(MyApplication.getContext(), str,R.drawable.tishi, R.color.colorAccent,Toast.LENGTH_SHORT,true,true).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);
        nowcity=this.getResources().getString(R.string.nowcity);
        tableManager = new AlarmTableManager(this);
        if (!NetworkUtil.isNetworkAbailable(this))
        {
            Toasty.custom(MyApplication.getContext(), " 无网络连接",R.drawable.duankai, R.color.colorRed,Toast.LENGTH_SHORT,true,true).show();
        }
        initView();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date=format.format(new Date());
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new LocationListenner());
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//高精度
        option.setCoorType("gcj02");//国测局坐标；
        option.setAddrType("all");
        option.setScanSpan(10000);//置发起定位请求的间隔
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                getweather(nowcity);
                msg.what=update;
                msg.obj="更新recycleview";
            }
        }).start();
    }

    private void initView() {
        name = (EditText) this.findViewById(R.id.name);
        setTime = this.findViewById(R.id.set_time);
        setalarmtime = this.findViewById(R.id.wait_time);
        setlabel = this.findViewById(R.id.set_label);
        label = this.findViewById(R.id.label);
        setRepeat = (LinearLayout) this.findViewById(R.id.set_repeat);
        repeat = (TextView) this.findViewById(R.id.repeat);
        setTone = (LinearLayout) this.findViewById(R.id.set_tone);
        tone = (TextView) this.findViewById(R.id.tone);
        cancel = (FrameLayout) this.findViewById(R.id.alarm_cancel);
        save = (RelativeLayout) this.findViewById(R.id.alarm_save);

        wendu = this.findViewById(R.id.wendu);
        type = this.findViewById(R.id.type);
        min = this.findViewById(R.id.min);
        max = this.findViewById(R.id.max);
        relativeLayout = this.findViewById(R.id.wendu_relativelayout);
        setTime.setOnClickListener(this);
        setRepeat.setOnClickListener(this);
        setlabel.setOnClickListener(this);
        setTone.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getExtras().getLong("id");
        if (id == -1) {
            alarmModel = new AlarmModel();
        } else {
            alarmModel = tableManager.getAlarm(id);


            name.setText(alarmModel.getName());
            repeat.setText(AlarmUtils.getRepeatDays(alarmModel));
            label.setText(alarmModel.getLabel());
            if (alarmModel.getTone() != null) {
                tone.setText(RingtoneManager.getRingtone(this,
                        Uri.parse(alarmModel.getTone())).getTitle(this));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_time:
                showTimeSelectDialog();
                break;
            case R.id.set_label:
                showLabelSelectDialog();
                break;
            case R.id.set_repeat:
                showRepeatSlelectDialog();
                break;
            case R.id.set_tone:
                showToneSelectDialog();
                break;
            case R.id.alarm_cancel:
                finish();
                overridePendingTransition(R.anim.push_from_left_in,
                        R.anim.push_from_left_out);
                break;
            case R.id.alarm_save:
                if (getlabel != null && name.getText().toString().length() != 0) {
                    save();
                } else if (name.getText().toString().length() == 0) {
                    //Toast.makeText(MyApplication.getContext(), "你连做啥事都不知道，这是不行滴", Toast.LENGTH_SHORT).show();
                    Toasty.custom(MyApplication.getContext(), "你要干啥呢",R.drawable.wenhao, R.color.colorAccent,Toast.LENGTH_SHORT,true,true).show();
                } else if (getlabel == null) {
                    //Toast.makeText(MyApplication.getContext(), "标签还没选呢！", Toast.LENGTH_SHORT).show();
                    Toasty.custom(MyApplication.getContext(), "标签还没选呢",R.drawable.jinggao, R.color.colorAccent,Toast.LENGTH_SHORT,true,true).show();
                }
                break;

            case R.id.wendu_relativelayout:
                Intent intent=new Intent(AlarmDetailsActivity.this, WeatherActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.touming_in,R.anim.touming_out);
                break;

        }
    }


    private void showTimeSelectDialog() {
        calendar = Calendar.getInstance();
        new TimePickerDialog(AlarmDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setalarm_hour = hourOfDay;
                setalarm_min = minute;
                if (minute < 10) {
                    setalarmtime.setText(setalarm_hour + ":0" + setalarm_min);
                } else {
                    setalarmtime.setText(setalarm_hour + ":" + setalarm_min);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    private void showLabelSelectDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        builder.setIcon(R.drawable.biaoqian);
        builder.setTitle("选择标签");
        builder.setItems(new String[]{"作息", "学习", "工作", "运动", "户外娱乐", "其它"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] chooselabel = new String[]{"作息", "学习", "工作", "运动", "户外娱乐", "其它"};
                        if (which == 0) {
                            label.setText(chooselabel[which]);
                            getlabel = chooselabel[which];
                        } else if (which == 1) {
                            label.setText(chooselabel[which]);
                            getlabel = chooselabel[which];
                        } else if (which == 2) {
                            label.setText(chooselabel[which]);
                            getlabel = chooselabel[which];
                        } else if (which == 3) {
                            label.setText(chooselabel[which]);
                            getlabel = chooselabel[which];
                        } else if (which == 4) {
                            label.setText(chooselabel[which]);
                            getlabel = chooselabel[which];
                        } else {
                            label.setText(chooselabel[which]);
                            getlabel = chooselabel[which];
                        }
                    }
                });
        builder.create().show();


    }

    private void showToneSelectDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        builder.setIcon(R.drawable.lingsheng);
        builder.setTitle("选择铃声");
        builder.setItems(new String[]{"铃声", "音乐或录音"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent;
                        if (which == 0) {
                            // 打开系统铃声设置
                            intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                            startActivityForResult(intent, 200);
                        } else {
                            intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("audio/*");
                            startActivityForResult(Intent.createChooser(intent, "选择铃声"), 300);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    break;
                case 300:
                    uri = data.getData();
                    break;
                default:
                    break;
            }
        }

        if (uri != null) {
            alarmModel.setTone(uri.toString());
            tone.setText(RingtoneManager.getRingtone(this, Uri.parse(alarmModel.getTone())).getTitle(this));
        }
    }

    /**
     * 选择重复天数的对话框
     */
    private void showRepeatSlelectDialog() {
        // TODO Auto-generated method stub
        final boolean repeat_days[] = new boolean[7];
        if (id != -1) {
            for (int i = 0; i < 7; i++) {
                repeat_days[i] = alarmModel.getRepeating_day(i);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        builder.setIcon(R.drawable.zhongfu);
        builder.setTitle("请选择");
        builder.setMultiChoiceItems(R.array.repeat_days,
                alarmModel.getRepeating_days(),
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        // TODO Auto-generated method stub
                        repeat_days[which] = isChecked;
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                alarmModel.setRepeating_days(repeat_days);
                repeat.setText(AlarmUtils.getRepeatDays(alarmModel));
            }
        });
        builder.create().show();
    }

    /**
     * 保存闹钟
     */
    private void save() {

        alarmModel.setHour(setalarm_hour);
        alarmModel.setMinute(setalarm_min);
        alarmModel.setName(name.getText().toString());
        alarmModel.setLabel(getlabel);
        alarmModel.setDate(date);
        int day = -1;
        if (id == -1) {
            alarmModel.setEnable(true);
            long result = tableManager.createAlarm(alarmModel);
            long result2 = tableManager.createAlarm_all(alarmModel);
            alarmModel.setId(result);
            alarmModel.setId(result2);
            day = AlarmSetClock.chooseTime(this, alarmModel);
        } else {
            tableManager.updateAlarm(alarmModel);
            tableManager.updateAlarm_all(alarmModel);
            if (alarmModel.isEnable()) {
                AlarmSetClock.cancelAlarm(this, alarmModel);
                day = AlarmSetClock.chooseTime(this, alarmModel);
            }
        }
        Intent intent = getIntent();
        intent.putExtra("day", day);
        intent.putExtra("id", alarmModel.getId());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.suoxiao_in,
                R.anim.suoxiao_out);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.push_from_left_in, R.anim.push_from_left_out);
        finish();
    }

    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    class LocationListenner extends BDAbstractLocationListener {
        public void onReceiveLocation(BDLocation location) {
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            Log.d("123", city);
        }
    }

    private void updateUI(WeatherInfo info) {
        String now =info.getWendu()+"";
        wendu.setText(now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.DAY_OF_WEEK);
        setData(info.get(0),max,min,type,null);
    }

    protected void setData(Weather weather, TextView r_max, TextView r_min, TextView r_type, TextView r_date) {
        r_max.setText(weather.heigh);
        r_min.setText(weather.low);
        r_type.setText(weather.type);
        if (r_date!=null)
            r_date.setText(Weather_WeekTool.transform(weather.date));
    }

    private void fail(String str) {
        Message msg = Message.obtain();
        msg.what = FAIL;
        msg.obj = str;
        handler.sendMessage(msg);
    }
    private void getweather(String city)
    {
        WeatherInfo info = new WeatherInfo(city);
        String sUrl = getResources().getString(R.string.api)+city;
        Log.d("TAG_APP", sUrl);
        try {
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code != 200){
                fail("获取数据失败");
                return;
            }
            InputStream in = conn.getInputStream();
            String content = Weather_streamTool.transform(in);
            JSONObject jsonObject = new JSONObject(content);
            if (!"OK".equals(jsonObject.get("desc"))){
                fail("获取数据失败");
                return;
            }
            jsonObject = jsonObject.optJSONObject("data");
            String wendu = jsonObject.optString("wendu");
            String note = jsonObject.optString("ganmao");
            String aqi = jsonObject.optString("aqi");
            info.setWendu(wendu);
            if (!"".equals(aqi)){
                info.setAqi(Integer.parseInt(aqi));
            }
            info.setNote(note);
            JSONArray jsonArray = jsonObject.optJSONArray("forecast");
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                Weather weather = new Weather();
                weather.fengxiang = obj.optString("fengxiang");
                weather.fengli = obj.optString("fengli");
                weather.date = obj.optString("date");
                String max[]=obj.optString("high").split(" ");
                weather.heigh = max[1];
                String min[]=obj.optString("low").split(" ");
                weather.low = min[1];
                weather.type = obj.optString("type");
                info.addWeather(weather);
            }
            Message msg = Message.obtain();
            msg.what=SUCCESS;
            msg.obj=info;
            handler.sendMessage(msg);
            Log.d("TAG_APP","");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
