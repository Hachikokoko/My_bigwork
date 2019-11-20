package com.example.thememorandum.Mine.important_thing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thememorandum.Mine.Weather.WeatherActivity;
import com.example.thememorandum.R;

public class important_thing extends AppCompatActivity {

    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_thing);
        mToolbar = findViewById(R.id.tool_bar);
        this.setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        setActionBarTitle(actionBar, "备忘事务");
        mFragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putSerializable(ReminderParams.TYPE, ReminderType.ALL);
        Fragment reminderFragment = new ReminderFragment();
        reminderFragment.setArguments(args);
        mFragmentManager.beginTransaction().add(R.id.content_frame, reminderFragment).commit();
    }
    private void setActionBarTitle(ActionBar actionBar, String title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                important_thing.this.finish();
                overridePendingTransition(R.anim.push_from_left_in,R.anim.push_from_left_out);
                break;
            default:
                break;
        }
        return true;
    }

}
