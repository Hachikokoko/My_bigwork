package com.example.thememorandum.EditDiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thememorandum.Alarm.AlarmSetClock;
import com.example.thememorandum.R;
import com.example.thememorandum.Utils.MyApplication;
import com.example.thememorandum.View.GradientTextView;
import com.example.thememorandum.View.recordview;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class EditDiary extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private GridView gridView;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private Button back_button;
    private TextView dateview;
    private Button begin_record;
    private recordview myrecordview;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        GradientTextView textView=findViewById(R.id.huiyitextview);
        JumpingBeans jumpingBeans=JumpingBeans.with(textView).makeTextJump(0,textView.getText().length())
                .setIsWave(true).setLoopDuration(3000).build();
        mContext = this;
        gridView = (GridView) findViewById(R.id.gridView);
        dateview=findViewById(R.id.date_show);
        back_button=findViewById(R.id.back_timer);
        back_button.setOnClickListener(this);
        begin_record=findViewById(R.id.begin_record);
        begin_record.setOnClickListener(this);
        myrecordview=findViewById(R.id.recordview);
        initGridView();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //每1秒发送消息给handler
                handler.sendEmptyMessage(1);
            }
        },0,1, TimeUnit.SECONDS);
    }
    private void setData(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        dateview.setText(format.format(new Date()));
    }
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(mContext, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
                        //最多添加5张图片
                        viewPluImg(position);

                    } else {
                        //添加凭证图片
                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(MyApplication.getContext(), PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多5张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                Log.d("path",localMedia.getPath());
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_timer:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditDiary.this, R.style.MyDialogStyle02);
                builder.setIcon(R.drawable.huiyi);
                builder.setTitle("保存回忆");
                builder.setMessage("嘿，完成了这个事务了吗？");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditDiary.this.finish();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditDiary.this.finish();
                        Toasty.custom(MyApplication.getContext(), " 保存成功",R.drawable.clear, R.color.colorAccent, Toast.LENGTH_SHORT,true,true).show();
                    }
                });
                builder.create().show();
                break;
            case R.id.begin_record:
                myrecordview.start();
                break;
        }
    }
}
