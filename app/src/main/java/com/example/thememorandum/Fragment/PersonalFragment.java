package com.example.thememorandum.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.thememorandum.Alarm.AlarmDetailsActivity;
import com.example.thememorandum.Alarm.AlarmModel;
import com.example.thememorandum.Alarm.AlarmSetClock;
import com.example.thememorandum.AlarmAdapter;
import com.example.thememorandum.R;
import com.example.thememorandum.Utils.AlarmUtils;
import com.example.thememorandum.db.AlarmTableManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends Fragment implements View.OnClickListener
{
    private TextView datetv,timetv;//年月日和时间
    private AlarmTableManager tableManager;
    private AlarmAdapter alarmAdapter;
    private List<AlarmModel> mList = new ArrayList<>();
    private Button addBtn;

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
        tableManager = new AlarmTableManager(getActivity());
        ListView listView = view.findViewById(R.id.list_item);
        addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
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

        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:ss");
        timetv.setText(format1.format(new Date()));
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
            Toast.makeText(getActivity(), alarm.getName() + " 闹钟已取消", Toast.LENGTH_SHORT).show();
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
        } else {
            alarm.setfished(isChecked);
            tableManager.updateAlarm(alarm);
            tableManager.updateAlarm_all(alarm);
            Toast.makeText(getActivity(), " 已完成", Toast.LENGTH_SHORT).show();
        }
        notifyList();
    }

    //长按删除闹钟对话框
    public void deleteAlarm(long id) {
        final long mId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogStyle);
        builder.setIcon(R.drawable.shanchu);
        builder.setTitle("删除事务");
        builder.setMessage("嘿，完成了这个事务了吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                AlarmSetClock.cancelAlarm(getActivity(), tableManager.getAlarm(mId));
                tableManager.deleteAlarm(mId);
                alarmAdapter.setData(tableManager.getAlarms());
                alarmAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        startAlarmDetailsActivity(-1);
    }
}
