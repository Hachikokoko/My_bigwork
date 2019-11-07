package com.example.thememorandum.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.example.thememorandum.R;

public class bolang_View extends View {
        private PaintFlagsDrawFilter mDrawFilter;
        private Paint mWavePaint;
        private float mOffset1 = 0.0f;
        private float mOffset2 = 0.0f;
        private float mSpeed1 = 0.05f;
        private float mSpeed2 = 0.07f;
        public bolang_View(Context context) {
            super(context);
            init();
        }

        public bolang_View(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public bolang_View(Context context,  AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        public void init() {
            // 初始绘制波纹的画笔
            mWavePaint = new Paint();
            // 去除画笔锯齿
            mWavePaint.setAntiAlias(true);
            // 设置风格为实线
            mWavePaint.setStyle(Paint.Style.FILL);
            // 设置画笔颜色
            mWavePaint.setColor(getResources().getColor(R.color.colorBlue));
            mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 从canvas层面去除绘制时锯齿
            canvas.setDrawFilter(mDrawFilter);
            for (int i = 0; i < getWidth(); i++) {

                // y = A * sin( wx + b) + h ; A： 浪高； w：周期；b：初相；
                float endY = (float) (20 * Math.sin(2 * Math.PI / getWidth() * i + mOffset1) + 100);
                //画第一条波浪
                canvas.drawLine(i, 600, i, endY, mWavePaint);

                //画第二条波浪
                float endY2 = (float) (20 * Math.sin(2 * Math.PI / getWidth() * i + mOffset2) + 100);
                canvas.drawLine(i, 600, i, endY2, mWavePaint);
            }

            if (mOffset1 > Float.MAX_VALUE - 1) {//防止数值超过浮点型的最大值
                mOffset1 = 0;
            }
            mOffset1 += mSpeed1;

            if (mOffset2 > Float.MAX_VALUE - 1) {//防止数值超过浮点型的最大值
                mOffset2 = 0;
            }
            mOffset2 += mSpeed2;
            //刷新
            postInvalidate();
        }
    }


