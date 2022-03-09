package com.lqk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author lqk
 * @date 2018/5/25
 * @time 21:27
 * @remarks
 */
public class UIUtil {
    Context context;

    // 基准尺寸 建议使用配置文件
    public final static float STATIC_HEIGHT = 1920F;
    public final static float STATIC_WIDTH = 1080F;

    //
    private final static String DIMEN_CLASS = "com.android.internal.R$dimen";

    private static float displayMetricsWidth;
    private static float getDisplayMetricsHeight;

    // 单列
    private static UIUtil mInstance;

    public static UIUtil getmInstance() {
        return mInstance;
    }

    public static UIUtil getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UIUtil(context);
        }
        return mInstance;
    }

    private UIUtil(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (displayMetricsWidth == 0.0f) {

        }
    }


}
