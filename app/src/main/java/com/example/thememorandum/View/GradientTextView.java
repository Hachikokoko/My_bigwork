package com.example.thememorandum.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;


import androidx.appcompat.widget.AppCompatTextView;

import com.example.thememorandum.R;

public class GradientTextView extends AppCompatTextView {
    private int width;
    /** 移动距离*/
    private int translateWidth;
    private Paint paint;
    private Matrix matrix;
    private LinearGradient linearGradient;

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context) {
        super(context);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w!=0){
            width=w;
        }else{
            width=getMeasuredWidth();
        }
        paint=getPaint();
        linearGradient=new LinearGradient(-width, 0,0,0,
                new int[]{getResources().getColor(R.color.colorzise),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorBlue02)},
                new float[]{0,0.5f,1f},
                Shader.TileMode.CLAMP);
        //添加渲染
        paint.setShader(linearGradient);
        paint.setColor(Color.BLACK);
        matrix=new Matrix();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(matrix==null)
            return;
        //每次移动原来宽的15分之一
        translateWidth+=width/86;
        //表示刚刚移动 了width个宽度 即 正好包含了整个textview 的时候还原
        if(translateWidth>width*2){
            translateWidth-=width*2;
        }
        //移动
        matrix.setTranslate(translateWidth,0);
        linearGradient.setLocalMatrix(matrix);
        postInvalidateDelayed(20);
    }
}

