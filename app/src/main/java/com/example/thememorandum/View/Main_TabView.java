package com.example.thememorandum.View;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.thememorandum.R;

public class Main_TabView extends FrameLayout
{
    private ImageView NormalImageView;
    private ImageView SelectedImageView;
    private TextView TitleView;
    private String Title;
    private Drawable NormalDrawable;
    private Drawable SelectDrawable;
    private int TargetColor;

    //标题和轮廓图标设置为黑色
    private static final int TAB_BEFORE_COLOR=0xff000000;
    //标题和轮廓图标设置为玫色
    private static final int TAB_AFTER_COLOR=0xFF1296db;

    public Main_TabView( Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //加载布局
        inflate(context, R.layout.main_tabview_layout,this);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.Main_TabView);//obtainStyledAttributes检索对应数组属性值
        for(int i=0;i<typedArray.getIndexCount();i++)
        {
            int attr=typedArray.getIndex(i);
            switch (attr)
            {
                //获取图标和轮廓的最终的颜色
                case R.styleable.Main_TabView_tabColor:
                    TargetColor=typedArray.getColor(attr,TAB_AFTER_COLOR);
                    break;
                //获取轮廓图
                case R.styleable.Main_TabView_tabImage:
                    NormalDrawable=typedArray.getDrawable(attr);
                    break;
                //获取选中的图
                case R.styleable.Main_TabView_tabSelectedImage:
                    SelectDrawable=typedArray.getDrawable(attr);
                    break;
                //获取标题
                case R.styleable.Main_TabView_tabTitle:
                    Title=typedArray.getString(attr);
                    break;
            }
        }
        typedArray.recycle();
    }
    protected void onFinishInflate() {

        super.onFinishInflate();
        //设置标题，默认为黑色
        TitleView=findViewById(R.id.tab_title);
        TitleView.setTextColor(TAB_BEFORE_COLOR);
        TitleView.setText(Title);

        //设置轮廓图片为不透明，默认为黑色
        NormalImageView=findViewById(R.id.tab_image);
        NormalDrawable.setTint(TAB_BEFORE_COLOR);
        NormalDrawable.setAlpha(255);
        NormalImageView.setImageDrawable(NormalDrawable);

        //设置选中图片，透明，默认颜色为黑色
        SelectedImageView=findViewById(R.id.tab_selected_image);
        SelectDrawable.setAlpha(0);
        SelectedImageView.setImageDrawable(SelectDrawable);
    }

    /*
    * 根据进度值进行变色和透明度处理
    * percentage（进度值），取值区间在【0,1】
    * */
    public void setPercentage(float percentage)
    {
        if(percentage<0||percentage>1)
        {
            return;
        }
        //1.颜色变换
        int finalColor=evaluate(percentage,TAB_BEFORE_COLOR,TargetColor);
        TitleView.setTextColor(finalColor);
        NormalDrawable.setTint(finalColor);

        //2.透明度转换
        if(percentage>=0.5&&percentage<=1)
        {
            /*进度值在0.5--1，透明度0--1
            * 计算公式：percentage-1=(alpha-1)*0.5
            * */
            int alpha= (int) Math.ceil(255*((percentage-1)*2+1));
            NormalDrawable.setAlpha(255-alpha);
            SelectDrawable.setAlpha(alpha);
        }
        else
        {
            NormalDrawable.setAlpha(255);
            SelectDrawable.setAlpha(0);
        }
        //3.更新UI
        invalidateUI();
    }
    private void invalidateUI()
    {
        if(Looper.myLooper()==Looper.getMainLooper())
        {
            //在主线程绘制
            invalidate();
        }
        else
        {
            //在工作线程绘制
            postInvalidate();
        }
    }
    /**
     * 计算不同进度值对应的颜色值，这个方法取自 ArgbEvaluator.java 类。
     *  percentage 进度值，范围[0, 1]。
     *  startValue 起始颜色值。
     *  endValue 最终颜色值。
     *  返回与进度值相应的颜色值。
     */
    private int evaluate(float percentage, int startValue, int endValue) {
        int startInt = startValue;
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >>  8) & 0xff) / 255.0f;
        float startB = ( startInt        & 0xff) / 255.0f;

        int endInt = endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >>  8) & 0xff) / 255.0f;
        float endB = ( endInt        & 0xff) / 255.0f;

        // convert from sRGB to linear
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // compute the interpolated color in linear space
        float a = startA + percentage * (endA - startA);
        float r = startR + percentage * (endR - startR);
        float g = startG + percentage * (endG - startG);
        float b = startB + percentage * (endB - startB);

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }
}
