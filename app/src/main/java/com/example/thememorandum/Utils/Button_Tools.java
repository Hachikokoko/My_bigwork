package com.example.thememorandum.Utils;

import android.content.Context;

public class Button_Tools {
    public static int dip2px(Context context, float dipVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipVal * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale + 0.5f);
    }

    public static int sp2px(Context context, float spVal) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxVal) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxVal / fontScale + 0.5f);
    }
}
