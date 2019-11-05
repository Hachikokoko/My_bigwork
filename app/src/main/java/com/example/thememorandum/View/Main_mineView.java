package com.example.thememorandum.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thememorandum.R;
import com.example.thememorandum.Utils.DensityUtils;


public class Main_mineView extends LinearLayout
{
    private View dividerTop,dividerButtom;
    private LinearLayout llroot;//最外容器
    private ImageView imageView_left;//最左边的icon
    private ImageView imageView_right;//右边箭头
    private TextView  CenterText;//中间文字
    private int dividercolor=0xff515151;
    private static Context mContext;

    /*
    * 初始化控件
    * */
    public Main_mineView init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.main_mine_layout,this,true);
        llroot=findViewById(R.id.linerlayout_root);
        dividerTop=findViewById(R.id.divide_top);
        dividerButtom=findViewById(R.id.divide_buttom);
        imageView_left=findViewById(R.id.imageview_left);
        imageView_right=findViewById(R.id.imageview_right);
        CenterText=findViewById(R.id.mine_mian_layout_textview);
        return this;
    }

    public Main_mineView(Context context)
    {
        super(context);
    }
    public Main_mineView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener
    {
        void onRootClick(View view);
    }


    /**
     * 右边箭头的点击事件
     */
    public static interface OnArrowClickListener
    {
        void onArrowClick(View view);
    }



    /*
    * 默认情况下icon+文字+箭头+下分割线
    *
    * */
    public Main_mineView initi(int iconRes,String settext,boolean showarrow)
    {
        init();
        showDivider(false,true);
        setLeftIcon(iconRes);
        setCentertext(settext);
        showArrow(showarrow);
        return this;
    }


    /*
    * 设置上划线的颜色
    * */
    public Main_mineView setdividerTopColor()
    {
        dividerTop.setBackgroundColor(getResources().getColor(dividercolor));
        return this;
    }
    /*
    * 设置上划线的高度
    * */
    public Main_mineView setdividerTopheight(int dividertopgightDp)
    {
        ViewGroup.LayoutParams layoutParams=dividerTop.getLayoutParams();
        layoutParams.height= DensityUtils.dp2px(getContext(),dividertopgightDp);
        dividerTop.setLayoutParams(layoutParams);
        return this;
    }
    /*
     * 设置下划线的颜色
     * */
    public Main_mineView setdividerBottomColor()
    {
        dividerButtom.setBackgroundColor(getResources().getColor(dividercolor));
        return this;
    }
    /*
     * 设置下划线的高度
     * */
    public Main_mineView setdividerBottomheight(int dividertopgightDp)
    {
        ViewGroup.LayoutParams layoutParams=dividerButtom.getLayoutParams();
        layoutParams.height= DensityUtils.dp2px(getContext(),dividertopgightDp);
        dividerButtom.setLayoutParams(layoutParams);
        return this;
    }

    /*
     * 设置上下划线的显示情况
     *
     * */
    public Main_mineView showDivider(Boolean showDividerTop,Boolean showDivderBottom)
    {
        if(showDividerTop)
        {
            dividerTop.setVisibility(VISIBLE);
        }
        else
        {
            dividerTop.setVisibility(GONE);
        }
        if(showDivderBottom)
        {
            dividerButtom.setVisibility(VISIBLE);
        }
        else
        {
            dividerButtom.setVisibility(GONE);
        }
        return this;
    }
    /*
    * 设置左边icon
    * */
    public Main_mineView setLeftIcon(int iconRes)
    {
        imageView_left.setImageResource(iconRes);
        return this;
    }
    public Main_mineView showLeftIcon(boolean imageleft)
    {
        if(imageleft)
        {
            imageView_left.setVisibility(VISIBLE);
        }
        else
        {
            imageView_left.setVisibility(GONE);
        }
        return this;
    }
    /*
    * 设置左边的icon和宽高
    * */
    public Main_mineView setLefticonSize(int widthdp,int heightdp)
    {
        ViewGroup.LayoutParams layoutParams=imageView_left.getLayoutParams();
        layoutParams.width=DensityUtils.dp2px(getContext(),widthdp);
        layoutParams.height=DensityUtils.dp2px(getContext(),heightdp);
        imageView_left.setLayoutParams(layoutParams);
        return this;
    }
    /*
    * 设置中间文字
    * */
    public Main_mineView setCentertext(String settext1)
    {
        CenterText.setText(settext1);
        return this;
    }
    public Main_mineView setContertextColor(int colorRes)
    {
        CenterText.setTextColor(getResources().getColor(colorRes));
        return this;
    }
    public Main_mineView ConterTextSize(int textsize)
    {
        CenterText.setTextSize(textsize);
        return this;
    }
    /*
    * 设置右边箭头显示
    * */
    public Main_mineView showArrow(boolean showArrw)
    {
        if(showArrw)
        {
            imageView_right.setVisibility(VISIBLE);
        }
        else
        {
            imageView_right.setVisibility(GONE);
        }
        return  this;
    }
    /*
    * 设置箭头图案
    * */
    public Main_mineView setArrow(int iconRes)
    {
        imageView_right.setImageResource(iconRes);
        return this;
    }
    /*
    * 获取箭头
    * */
    public ImageView getImageView_right()
    {
        return imageView_right;
    }
    /*
    *
    *
    * 点击事件
    *
    *
    * */
    public Main_mineView setOnRootClickListener(final OnRootClickListener onRootClickListener,final int tag)
    {
        llroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llroot.setTag(tag);
                onRootClickListener.onRootClick(llroot);
            }
        });
        return this;
    }
    public Main_mineView setOnArrowClickListenr(final OnArrowClickListener onArrowClickListenr,final int tag)
    {
        imageView_right.setOnClickListener(v -> {
            imageView_right.setTag(tag);
            onArrowClickListenr.onArrowClick(imageView_right);
        });
        return this;
    }

    /**
     * 设置root的paddingTop 与 PaddingBottom 从而控制整体的行高
     * paddingLeft 与 paddingRight 保持默认 20dp
     */
    public Main_mineView setRootPaddingTopBottom(int paddintTop, int paddintBottom) {
        llroot.setPadding(DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), paddintTop),
                DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), paddintBottom));
        return this;
    }

    /**
     * 设置root的paddingLeft 与 PaddingRight 从而控制整体的行高
     * <p>
     * paddingTop 与 paddingBottom 保持默认 15dp
     */
    public Main_mineView setRootPaddingLeftRight(int paddintTop, int paddintRight) {
        llroot.setPadding(DensityUtils.dp2px(getContext(), paddintTop),
                DensityUtils.dp2px(getContext(), 15),
                DensityUtils.dp2px(getContext(), paddintRight),
                DensityUtils.dp2px(getContext(), 15));
        return this;
    }
    public static Context getmContext(){
        return mContext;
    }
}
