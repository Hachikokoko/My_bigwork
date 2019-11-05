package com.example.thememorandum.Weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thememorandum.R;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<Weather_re> mwather;
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_recyclerview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Weather_re weather_re=mwather.get(position);
        holder.re_days.setText(weather_re.getRe_days());
        holder.sun.setText(weather_re.getSun());
        holder.image01.setImageResource(weather_re.getImage01());
        holder.image02.setImageResource(weather_re.getImage02());
        holder.min.setText(weather_re.getmin());
        holder.max.setText(weather_re.getmax());
    }

    @Override
    public int getItemCount() {
        return mwather.size();
    }
    public WeatherAdapter(List<Weather_re> weatherlist)
    {
        mwather=weatherlist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView re_days;
        TextView sun;
        ImageView image01;
        TextView min;
        TextView max;
        ImageView image02;
        public ViewHolder( View view) {
            super(view);
            re_days=view.findViewById(R.id.re_days);
            sun=view.findViewById(R.id.re_sun);
            image01=view.findViewById(R.id.re_img);
            image02=view.findViewById(R.id.re_img_02);
            min=view.findViewById(R.id.re_min);
            max=view.findViewById(R.id.re_max);
        }
    }
}
