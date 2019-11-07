package com.example.thememorandum.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thememorandum.EditDiary_ALL.EditDiary;
import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;


public class DiaryFragment extends Fragment implements View.OnClickListener
{
    private Button editbutton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.main_layout_03,container,false);
        editbutton = view.findViewById(R.id.editDiary);
        editbutton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MyApplication.getContext(), EditDiary.class);
        startActivity(intent);    }
}
