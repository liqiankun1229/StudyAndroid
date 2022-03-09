package com.lqk.butter.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author LQK
 * @time 2019/2/18 17:07
 * @remark UI 工具类
 */
public class UIUtil {

    Context context;
    // 默认 UI 尺寸 为 1920*1080
    public static final float STANDARD_HEIGHT = 1920F;
    public static final float STANDARD_WIDTH = 1080F;

    public static final String DIMEN_CLASS = "";

    // 实际 设备的分辨率
    public static float displayMetricsHeight = 0F;
    public static float displayMetricsWidth = 0F;

    // 单列 实现
    private static UIUtil instance;


    public static UIUtil getInstance(Context context) {
        if (instance == null) {
            instance = new UIUtil(context);
        }
        return instance;
    }

    private UIUtil(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (displayMetricsHeight == 0F || displayMetricsWidth == 0F) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        // 状态栏高度
        int statusBarHeight = getStatusHeight(context);
        // 适配平板 一般认为 宽大于高-> 平板
        if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
            // 手机
            this.displayMetricsHeight = displayMetrics.heightPixels;
            this.displayMetricsWidth = displayMetrics.widthPixels;
        } else {
            // 平板
            this.displayMetricsHeight = displayMetrics.widthPixels;
            this.displayMetricsWidth = displayMetrics.heightPixels;
        }

    }

    /**
     * 获取状态栏高度
     */
    private int getStatusHeight(Context context) {
        // 设置 默认高度是 48 获取失败也是 48
        int height = 48;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public float getVerticalScalingRatio() {
        return ((float) this.displayMetricsHeight / STANDARD_HEIGHT);
    }

    public float getHorizontalScalingRatio() {
        return ((float) this.displayMetricsWidth / STANDARD_WIDTH);
    }

}
