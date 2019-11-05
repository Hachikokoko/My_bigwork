package com.example.thememorandum;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.thememorandum.Fragment.DiaryFragment;
import com.example.thememorandum.Fragment.MineFragment;
import com.example.thememorandum.Fragment.PersonalFragment;
import com.example.thememorandum.Fragment.TeamFragment;
import com.example.thememorandum.Fragment.DataFragment;
import com.example.thememorandum.View.Main_TabView;
import com.example.thememorandum.db.MyDBHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private String tran;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindArray(R.array.tab_array)
    String[] mTabTitles;

    @BindView(R.id.tab_home)
    Main_TabView mTabHome;

    @BindView(R.id.tab_team)
    Main_TabView mTabTeam;

    @BindView(R.id.tab_data)
    Main_TabView mTabData;


    @BindView(R.id.tab_diary)
    Main_TabView mTabDiary;

    @BindView(R.id.tab_mine)
    Main_TabView mTabMine;

    private List<Main_TabView> mTabViews = new ArrayList<>();

    private static final int INDEX_HOME= 0;
    private static final int INDEX_Team = 1;
    private static final int INDEX_DATA= 2;
    private static final int INDEX_DIARY = 3;
    private static final int INDEX_MINE = 4;


    private MyDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper=new MyDBHelper(this,"TP.db",null,1);
        helper.getWritableDatabase();


        /*
        *
        * 底部界面选项
        * */
        ButterKnife.bind(this);
        mTabViews.add(mTabHome);
        mTabViews.add(mTabTeam);
        mTabViews.add(mTabData);
        mTabViews.add(mTabDiary);
        mTabViews.add(mTabMine);
        mViewPager.setOffscreenPageLimit(mTabTitles.length - 1);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            /**
             * @param position 滑动的时候，position总是代表左边的View， position+1总是代表右边的View
             * @param positionOffset 左边View位移的比例
             * @param positionOffsetPixels 左边View位移的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 左边View进行动画
                mTabViews.get(position).setPercentage(1 - positionOffset);
                // 如果positionOffset非0，那么就代表右边的View可见，也就说明需要对右边的View进行动画
                if (positionOffset > 0) {
                    mTabViews.get(position + 1).setPercentage(positionOffset);
                }
            }
        });
        /*
        *
        * 菜单代码
        *
        * */
    }
    /*
     *
     * 底部界面选项代码
     * */
    private void updateCurrentTab(int index) {
        for (int i = 0; i < mTabViews.size(); i++) {
            if (index == i) {
                mTabViews.get(i).setPercentage(1);
            } else {
                mTabViews.get(i).setPercentage(0);
            }
        }
    }
    @OnClick({R.id.tab_home,R.id.tab_team,R.id.tab_data, R.id.tab_diary, R.id.tab_mine})
    public void onClickTab(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                mViewPager.setCurrentItem(INDEX_HOME, false);
                updateCurrentTab(INDEX_HOME);
                break;
            case R.id.tab_team:
                mViewPager.setCurrentItem(INDEX_Team, false);
                updateCurrentTab(INDEX_Team);
                break;
            case R.id.tab_data:
                mViewPager.setCurrentItem(INDEX_DATA, false);
                updateCurrentTab(INDEX_DATA);
                break;
            case R.id.tab_diary:
                mViewPager.setCurrentItem(INDEX_DIARY, false);
                updateCurrentTab(INDEX_DIARY);
                break;
            case R.id.tab_mine:
                mViewPager.setCurrentItem(INDEX_MINE, false);
                updateCurrentTab(INDEX_MINE);
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return getTabFragment(i);
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }
    }

    /*
    *
    * 四个界面的逻辑代码
    *
    * */

    private Fragment getTabFragment(int index) {
        Fragment fragment = null;
        switch (index) {
            case INDEX_HOME:
                PersonalFragment personalFragment=new PersonalFragment();
                fragment=personalFragment;
                break;
            case INDEX_Team:
                TeamFragment teamFragment=new TeamFragment();
                fragment=teamFragment;
                break;
            case INDEX_DATA:
                DataFragment dataFragment=new DataFragment();
                fragment=dataFragment;
                break;
            case INDEX_DIARY:
                DiaryFragment diaryFragment=new DiaryFragment();
                fragment=diaryFragment;
                break;
            case INDEX_MINE:
                MineFragment mineFragment=new MineFragment();
                fragment=mineFragment;
                break;
        }
        return fragment;
    }

}
