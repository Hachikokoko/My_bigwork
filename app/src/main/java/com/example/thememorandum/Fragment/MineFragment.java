package com.example.thememorandum.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.thememorandum.Mine.Timer;
import com.example.thememorandum.Mine.important_thing;
import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.Mine.Weather.WeatherActivity;

public class MineFragment extends Fragment implements View.OnClickListener
{
    private LinearLayout user;
    private LinearLayout personal;
    private LinearLayout team;
    private LinearLayout weather;
    private LinearLayout important;
    private LinearLayout timer;
    private LinearLayout benfen;
    private LinearLayout clear;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.main_layout_04,container,false);
        user=view.findViewById(R.id.mine_user);
        personal=view.findViewById(R.id.mine_perrsonal);
        team=view.findViewById(R.id.mine_team);
        weather=view.findViewById(R.id.mine_weather);
        important=view.findViewById(R.id.mine_important);
        timer=view.findViewById(R.id.mine_timer);
        benfen=view.findViewById(R.id.mine_beifen);
        clear=view.findViewById(R.id.mine_clear);

        user.setOnClickListener(this);
        personal.setOnClickListener(this);
        team.setOnClickListener(this);
        weather.setOnClickListener(this);
        important.setOnClickListener(this);
        timer.setOnClickListener(this);
        benfen.setOnClickListener(this);
        clear.setOnClickListener(this);


        return view;
    }



    public void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);


   }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.mine_user:
                break;
            case R.id.mine_perrsonal:
                break;
            case R.id.mine_team:
                break;
            case R.id.mine_weather:
                Intent intent4=new Intent(MyApplication.getContext(), WeatherActivity.class);
                startActivity(intent4);
                getActivity().overridePendingTransition(R.anim.push_from_right_in,R.anim.push_from_right_out);
                break;
            case R.id.mine_important:
                Intent intent5=new Intent(MyApplication.getContext(), important_thing.class);
                startActivity(intent5);
                getActivity().overridePendingTransition(R.anim.push_from_right_in,R.anim.push_from_right_out);
                break;
            case R.id.mine_timer:
                Intent intent6=new Intent(MyApplication.getContext(), Timer.class);
                startActivity(intent6);
                getActivity().overridePendingTransition(R.anim.push_from_right_in,R.anim.push_from_right_out);
                break;
            case R.id.mine_beifen:
                break;
            case R.id.mine_clear:
                break;
        }

    }
}
