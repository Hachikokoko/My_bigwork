package com.example.thememorandum.Mine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;

import com.example.thememorandum.R;

public class User extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Button button=findViewById(R.id.user_back);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        User.this.finish();
    }
}
