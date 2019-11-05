package com.example.thememorandum.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.Mine.User;
import com.example.thememorandum.View.Main_mineView;
import com.example.thememorandum.Weather.WeatherActivity;

public class MineFragment extends Fragment implements Main_mineView.OnRootClickListener,Main_mineView.OnArrowClickListener
{
    LinearLayout llroot;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.main_layout_04,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        llroot=getActivity().findViewById(R.id.frgmentroot);
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.user, "账号",  true)
                .setOnRootClickListener(this, 1));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.personal_record, "个人记录",  true)
                .setOnRootClickListener(this, 2));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.team_record, "团队记录",  true)
                .setOnRootClickListener(this, 3));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.tianqi02, "天气情况",  true)
                .setOnRootClickListener(this, 4));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.shiwutixing, "重要事务",  true)
                .setOnRootClickListener(this, 5));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.jishiqi, "计时助手",  true)
                .setOnRootClickListener(this, 6));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.beifen, "备份恢复",  true)
                .setOnRootClickListener(this, 7));
        llroot.addView(new Main_mineView(MyApplication.getContext())
                .initi(R.drawable.clear, "清除",  true)
                .setOnRootClickListener(this, 8));
    }

    public void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);

   }
    @Override
    public void onRootClick(View view)
    {
        Fragment fragment=null;
        int position=0;
        switch ((int)view.getTag())
        {
            case 1:
                Intent intent=new Intent(MyApplication.getContext(), User.class);
                startActivity(intent);
                position=1;
                break;
            case 2:
                position=2;
                break;
            case 3:
                position=3;
                break;
            case 4:
                Intent intent4=new Intent(MyApplication.getContext(), WeatherActivity.class);
                startActivity(intent4);
                position=4;
            break;
            case 5:
                position=5;
            break;
            case 6:
                position=6;
                break;
            case 7:
                position=7;
                break;
            case 8:
                position=8;
                break;
        }
        Toast.makeText(getActivity(),"点击了第"+position+"行",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArrowClick(View view) {

    }
    /*
    * fragment的替换
    * */
    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        transaction.add(R.id.main_layout_4,fragment).commit();
    }
}
