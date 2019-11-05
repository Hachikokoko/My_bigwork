package com.example.thememorandum;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.thememorandum.Alarm.AlarmModel;
import com.example.thememorandum.Utils.AlarmUtils;
import com.example.thememorandum.Utils.MyApplication;
import com.uniquestudio.library.CircleCheckBox;

import java.util.List;

public class AlarmAdapter extends BaseAdapter {
    private Context context;
    private List<AlarmModel> alarmList;
    private OnClickCallBack clickCallBack;

    public AlarmAdapter(Context context, List<AlarmModel> alarms, OnClickCallBack clickCallBack) {
        this.context = context;
        alarmList = alarms;
        this.clickCallBack = clickCallBack;
    }

    public void setData(List<AlarmModel> alarms) {
        alarmList = alarms;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (alarmList != null) {
            return alarmList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (alarmList != null) {
            return alarmList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        if (alarmList != null) {
            return alarmList.get(position).getId();
        }
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (holder == null) {
            view = LayoutInflater.from(context).inflate(R.layout.alarm_listview_item, null);
            holder = new ViewHolder(view);
        }
        final AlarmModel alarm = (AlarmModel) getItem(position);

        holder.time.setText(String.format("%02d : %02d", alarm.getHour(), alarm.getMinute()));
        holder.name.setText(alarm.getName());
        holder.repeat.setText(AlarmUtils.getRepeatDays(alarm));
        holder.label.setText(alarm.getLabel());

        holder.isEnable.setChecked(alarm.isEnable());


        holder.isEnable.setTag(Long.valueOf(alarm.getId()));
        holder.isEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                Log.e("onCheckedChanged", "onCheckedChanged");
                clickCallBack.setAlarmEnable(Long.valueOf(alarm.getId()), isChecked);
            }
        });
        holder.isfinished.setChecked(alarm.isfisnhed());
        holder.isfinished.setTag(Long.valueOf(alarm.getId()));
        holder.isfinished.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                clickCallBack.setFinish(Long.valueOf(alarm.getId()), isChecked);
            }
        });


        holder.view.setTag(Long.valueOf(alarm.getId()));
        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickCallBack.startAlarmDetailsActivity(Long.valueOf(alarm.getId()));
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                clickCallBack.deleteAlarm(Long.valueOf(alarm.getId()));
                return true;
            }
        });
        return holder.view;
    }

    public interface OnClickCallBack {
        void setAlarmEnable(long id, boolean isChecked);

        void setFinish(long id, boolean isChecked);

        void startAlarmDetailsActivity(long id);

        void deleteAlarm(long id);
    }

    static class ViewHolder {
        View view;
        TextView time;
        TextView name;
        TextView repeat;
        CheckBox isEnable;
        TextView label;
        CircleCheckBox isfinished;

        ViewHolder(View view) {
            this.view = view;
            time = (TextView) view.findViewById(R.id.time);
            name = (TextView) view.findViewById(R.id.name);
            repeat = (TextView) view.findViewById(R.id.repeat);
            isEnable = (CheckBox) view.findViewById(R.id.isEnable);
            label = view.findViewById(R.id.putlabel);
            isfinished = view.findViewById(R.id.isfinished);
        }
    }
}
