package com.example.thememorandum.Weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.Utils.Weather_WeekTool;
import com.example.thememorandum.Utils.Weather_streamTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    private final static int SUCCESS = 1;
    private final static int FAIL = 0;
    private final static  int update=1;
    private EditText et_city;
    private Button bt_inquiry;
    private TextView tv_now;

    private TextView tv_aqi;

    private TextView tv_city;


    private TextView tv_note;

    private TextView tv_type0;


    private TextView tv_max0;


    private TextView tv_min0;
    private Button back_weather;


    private ArrayList<String> datelist=new ArrayList<>();
    private ArrayList<String> sunlist=new ArrayList<>();
    private ArrayList<String> minlist=new ArrayList<>();
    private ArrayList<String> maxlist=new ArrayList<>();

    private List<Weather_re> weather_reList=new ArrayList<>();
    private WeatherAdapter weatherAdapter = new WeatherAdapter(weather_reList);
    private RecyclerView recyclerView;

    private Handler mhandler =new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case update:
                    weather_reList.removeAll(weather_reList);
                    weatherAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(weatherAdapter);

            }
        }
    };

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
                    Toast.makeText(MyApplication.getContext(),str , Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        recyclerView=findViewById(R.id.re_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MyApplication.getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        init();
        back_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherActivity.this.finish();
            }
        });

    }
    private void init() {

        back_weather=findViewById(R.id.back_weather);
        tv_aqi = findViewById(R.id.tv_aqi);
        tv_city = findViewById(R.id.tv_city);
        tv_now = findViewById(R.id.tv_now_0);


        tv_max0 = findViewById(R.id.tv_max_0);
        tv_min0 = findViewById(R.id.tv_min_0);


        tv_type0 = findViewById(R.id.tv_type_0);


        tv_note = findViewById(R.id.tv_note_0);
        et_city = findViewById(R.id.et_city);
        bt_inquiry = findViewById(R.id.bt_inquiry);

        bt_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        msg.what=update;
                        msg.obj="更新recycleview";
                        mhandler.sendMessage(msg);
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String city = et_city.getText().toString();
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
                                if(datelist.size()<=4)
                                {
                                    datelist.add(Weather_WeekTool.transform(weather.date));
                                    sunlist.add(weather.type);
                                    minlist.add(weather.low);
                                    maxlist.add(weather.heigh);
                                }

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
                }).start();
            }
        });

    }
    private void fail(String str) {
        Message msg = Message.obtain();
        msg.what = FAIL;
        msg.obj = str;
        handler.sendMessage(msg);
    }

    private void updateUI(WeatherInfo info) {
        tv_city.setText(info.getCity()+"");
        String now =info.getWendu()+"";
        tv_now.setText(now);
        tv_note.setText(info.getNote()+"");
        tv_aqi.setText(info.getAqi()+"");


        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.DAY_OF_WEEK);
        setData(info.get(0),tv_max0,tv_min0,tv_type0,null);




        weather_reList.removeAll(weather_reList);
        init2();
        weatherAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(weatherAdapter);

    }

    protected void setData(Weather weather, TextView r_max, TextView r_min, TextView r_type, TextView r_date) {
        r_max.setText(weather.heigh);
        r_min.setText(weather.low);
        r_type.setText(weather.type);
        if (r_date!=null)
            r_date.setText(Weather_WeekTool.transform(weather.date));
    }
    private void init2()
    {
        if(datelist.size()>=4)
        {
            if(datelist.size()!=0&&sunlist.size()!=0&&minlist.size()!=0&&maxlist.size()!=0)
            {
                if(sunlist.get(1).equals("晴"))
                {
                    Weather_re one=new Weather_re("明天",R.drawable.qing,sunlist.get(1),minlist.get(1),R.drawable.img,maxlist.get(1));
                    weather_reList.add(one);
                }
                else if(sunlist.get(1).equals("多云"))
                {
                    Weather_re one=new Weather_re("明天",R.drawable.duoyun,sunlist.get(1),minlist.get(1),R.drawable.img,maxlist.get(1));
                    weather_reList.add(one);
                }
                else
                {
                    Weather_re one=new Weather_re("明天",R.drawable.yin,sunlist.get(1),minlist.get(1),R.drawable.img,maxlist.get(1));
                    weather_reList.add(one);
                }

            }
            if(datelist.size()!=0&&sunlist.size()!=0&&minlist.size()!=0&&maxlist.size()!=0)
            {
                if(sunlist.get(2).equals("晴"))
                {
                    Weather_re two=new Weather_re(datelist.get(2),R.drawable.qing,sunlist.get(2),minlist.get(2),R.drawable.img,maxlist.get(2));
                    weather_reList.add(two);
                }
                else if(sunlist.get(2).equals("多云"))
                {
                    Weather_re two=new Weather_re(datelist.get(2),R.drawable.duoyun,sunlist.get(2),minlist.get(2),R.drawable.img,maxlist.get(2));
                    weather_reList.add(two);
                }
                else
                {
                    Weather_re two=new Weather_re(datelist.get(2),R.drawable.yin,sunlist.get(2),minlist.get(2),R.drawable.img,maxlist.get(2));
                    weather_reList.add(two);
                }
            }

            if(datelist.size()!=0&&sunlist.size()!=0&&minlist.size()!=0&&maxlist.size()!=0)
            {
                if(sunlist.get(3).equals("晴"))
                {
                    Weather_re three=new Weather_re(datelist.get(3),R.drawable.qing,sunlist.get(3),minlist.get(3),R.drawable.img,maxlist.get(3));
                    weather_reList.add(three);
                }
                else if(sunlist.get(3).equals("多云"))
                {
                    Weather_re three=new Weather_re(datelist.get(3),R.drawable.duoyun,sunlist.get(3),minlist.get(3),R.drawable.img,maxlist.get(3));
                    weather_reList.add(three);
                }
                else
                {
                    Weather_re three=new Weather_re(datelist.get(3),R.drawable.yin,sunlist.get(3),minlist.get(3),R.drawable.img,maxlist.get(3));
                    weather_reList.add(three);
                }
            }
            if(datelist.size()!=0&&sunlist.size()!=0&&minlist.size()!=0&&maxlist.size()!=0)
            {
                if(sunlist.get(4).equals("晴"))
                {
                    Weather_re four=new Weather_re(datelist.get(4),R.drawable.qing,sunlist.get(4),minlist.get(4),R.drawable.img,maxlist.get(4));
                    weather_reList.add(four);
                }
                else if(sunlist.get(4).equals("多云"))
                {
                    Weather_re four=new Weather_re(datelist.get(4),R.drawable.duoyun,sunlist.get(4),minlist.get(4),R.drawable.img,maxlist.get(4));
                    weather_reList.add(four);
                }
                else
                {
                    Weather_re four=new Weather_re(datelist.get(4),R.drawable.yin,sunlist.get(4),minlist.get(4),R.drawable.img,maxlist.get(4));
                    weather_reList.add(four);
                }
            }
        }
        datelist.clear();
        sunlist.clear();
        minlist.clear();
        maxlist.clear();
    }
}
