package com.example.thememorandum.Mine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;

import com.example.thememorandum.R;

public class important_thing extends AppCompatActivity {
    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_thing);

        backbtn=findViewById(R.id.back_important_thing);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                important_thing.this.finish();
                overridePendingTransition(R.anim.push_from_left_in,R.anim.push_from_left_out);
            }
        });
    }
}
