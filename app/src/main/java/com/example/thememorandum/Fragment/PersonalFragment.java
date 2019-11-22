package com.example.thememorandum.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thememorandum.Alarm.AlarmAdapter;
import com.example.thememorandum.Alarm.AlarmDetailsActivity;
import com.example.thememorandum.Alarm.AlarmModel;
import com.example.thememorandum.Alarm.AlarmSetClock;
import com.example.thememorandum.R;
import com.example.thememorandum.Utils.AlarmUtils;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.db.AlarmTableManager;
import com.example.thememorandum.db.MyDBHelper;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends Fragment implements View.OnClickListener
{
    private TextView datetv,timetv,timetv02;//年月日和时间
    private AlarmTableManager tableManager;
    private AlarmAdapter alarmAdapter;
    private List<AlarmModel> mList = new ArrayList<>();
    private Button addBtn;
    private Button daka;
    private MyDBHelper dbHelper;
    private TextView mingyan;

    //初始化线程管理类，在UI中显示时间
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    setData();
                    break;
            }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.main_layout_01,container,false);
        datetv = (TextView) view.findViewById(R.id.date_tv);
        timetv = (TextView) view.findViewById(R.id.time_tv);
        timetv02 = (TextView) view.findViewById(R.id.time_tv02);
        tableManager = new AlarmTableManager(getActivity());
        ListView listView = view.findViewById(R.id.list_item);
        addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        dbHelper=new MyDBHelper(MyApplication.getContext(),"TP.db",null,1);
        daka=view.findViewById(R.id.daka);
        daka.setOnClickListener(this);
        mingyan=view.findViewById(R.id.mingyan);
        JumpingBeans jumpingBeans=JumpingBeans.with(mingyan).makeTextJump(0,mingyan.getText().length())
                .setIsWave(true).setLoopDuration(3000).build();
        alarmAdapter = new AlarmAdapter(getActivity(), mList, new AlarmAdapter.OnClickCallBack() {
            @Override
            public void setAlarmEnable(long id, boolean isChecked) {
                PersonalFragment.this.setAlarmEnable(id, isChecked);
            }

            @Override
            public void setFinish(long id, boolean isChecked) {
                PersonalFragment.this.setisfinished(id, isChecked);
            }

            @Override
            public void startAlarmDetailsActivity(long id) {
                PersonalFragment.this.startAlarmDetailsActivity(id);
            }

            @Override
            public void deleteAlarm(long id) {
                PersonalFragment.this.deleteAlarm(id);
            }
        });
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //每1秒发送消息给handler
                handler.sendEmptyMessage(1);
            }
        },0,1, TimeUnit.SECONDS);
        notifyList();
        listView.setAdapter(alarmAdapter);
        return view;
    }

    //设置数据
    private void setData(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        datetv.setText(format.format(new Date()));

        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:");
        timetv.setText(format1.format(new Date()));

        SimpleDateFormat format2 = new SimpleDateFormat("ss");
        timetv02.setText(format2.format(new Date()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
        }
    }

    //启动添加闹钟页
    public void startAlarmDetailsActivity(long id) {
        Intent intent = new Intent(getActivity(), AlarmDetailsActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 100);
    }

    //接收AlarmDetailsActivity返回成功时携带data的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            notifyList();
            if (data.getIntExtra("day", -1) >= 0) {
                AlarmUtils.showFirstRemindTime(getActivity(), data.getIntExtra("day", -1), data.getLongExtra("id", -1));
            }
        }
    }

    //更新列表
    private void notifyList() {
        if (mList.size() > 0) {
            mList.clear();
            alarmAdapter.notifyDataSetChanged();
        }
        if (tableManager.getAlarms() != null) {
            mList.addAll(tableManager.getAlarms());
            alarmAdapter.notifyDataSetChanged();
        }
    }

    //设置闹钟是否响铃
    public void setAlarmEnable(long id, boolean isChecked) {
        AlarmModel alarm = tableManager.getAlarm(id);
        if (isChecked) {
            alarm.setEnable(isChecked);
            tableManager.updateAlarm(alarm);
            int day = AlarmSetClock.chooseTime(getActivity(), alarm);
            AlarmUtils.showFirstRemindTime(getActivity(), day, id);
        } else {
            alarm.setEnable(isChecked);
            tableManager.updateAlarm(alarm);
            AlarmSetClock.cancelAlarm(getActivity(), alarm);
            Toasty.custom(getActivity(), alarm.getName() + " 取消提醒",R.drawable.quxiaotixing1, R.color.colorAccent,Toast.LENGTH_SHORT,true,true).show();
        }
        notifyList();
    }

    //是否已完成
    public void setisfinished(long id, boolean isChecked) {
        AlarmModel alarm = tableManager.getAlarm(id);
        if (isChecked) {
            alarm.setfished(isChecked);
            tableManager.updateAlarm(alarm);
            tableManager.updateAlarm_all(alarm);
            Toasty.custom(MyApplication.getContext(), "继续加油",R.drawable.zan, R.color.colorBlue,Toast.LENGTH_SHORT,true,true).show();

        } else {
            alarm.setfished(isChecked);
            tableManager.updateAlarm(alarm);
            tableManager.updateAlarm_all(alarm);
            Toasty.custom(MyApplication.getContext(), "努力呀",R.drawable.nvli, R.color.colorBlue,Toast.LENGTH_SHORT,true,true).show();
        }
        notifyList();
    }

    //长按删除闹钟对话框
    public void deleteAlarm(long id) {
        final long mId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogStyle);
        builder.setIcon(R.drawable.shanchu);
        builder.setTitle("Tips");
        builder.setMessage("是否要删除该事务？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                AlarmSetClock.cancelAlarm(getActivity(), tableManager.getAlarm(mId));
                tableManager.deleteAlarm(mId);
                alarmAdapter.setData(tableManager.getAlarms());
                alarmAdapter.notifyDataSetChanged();
                Toasty.custom(MyApplication.getContext(), " 删除成功",R.drawable.clear, R.color.colorAccent,Toast.LENGTH_SHORT,true,true).show();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.add_btn:
               startAlarmDetailsActivity(-1);
               getActivity().overridePendingTransition(R.anim.push_from_top_in,R.anim.push_from_top_out);
               break;
           case R.id.daka:
               SQLiteDatabase db=dbHelper.getWritableDatabase();
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               String date=format.format(new Date());
               int daka=1;
               int flag=0;
               Cursor cursor=db.query("Clock", new String[]{"date"},null,null,null,null,null);
               if(cursor.moveToFirst())
               {
                   do
                       {
                       String table_date=cursor.getString(0);
                       Log.d("table",table_date);
                        if(table_date.equals(date))
                        {
                            flag=1;
                        }
                   }while (cursor.moveToNext());
               }
               cursor.close();
               if(flag==0) {
                   ContentValues values2 = new ContentValues();
                   values2.put("date", date);
                   values2.put("tag", 1);
                   db.insert("Clock", null, values2);
                   values2.clear();
                   Toasty.custom(MyApplication.getContext(), "打卡成功", R.drawable.daka02, R.color.colorAccent, Toast.LENGTH_SHORT, true, true).show();
               }
               else
               {
                   Toasty.custom(MyApplication.getContext(), " 今日已打卡",R.drawable.tishi, R.color.colorAccent,Toast.LENGTH_SHORT,true,true).show();
               }
               break;
       }
    }
}
