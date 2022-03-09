package com.lqk.butter.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lqk.butter.utils.UIUtil;

import androidx.annotation.RequiresApi;

/**
 * @author LQK
 * @time 2019/2/19 15:26
 * @remark
 */
public class CustomLayout extends RelativeLayout {
    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取子控件的个数
        int childCount = getChildCount();
        float sX = UIUtil.getInstance(this.getContext()).getHorizontalScalingRatio();
        float sY = UIUtil.getInstance(this.getContext()).getVerticalScalingRatio();

        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            // 获取 布局参数
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.width = (int) (layoutParams.width * sX);
            layoutParams.height = (int) (layoutParams.height * sY);

            layoutParams.topMargin = (int) (layoutParams.topMargin * sY);
            layoutParams.rightMargin = (int) (layoutParams.rightMargin*sX);
            layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * sY);
            layoutParams.leftMargin = (int) (layoutParams.leftMargin * sX);


        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(this.getContext(), attrs);
//        return super.generateLayoutParams(attrs);
    }

    static class CustomLayoutParams extends RelativeLayout.LayoutParams {

        public CustomLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CustomLayoutParams(int w, int h) {
            super(w, h);
        }

        public CustomLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public CustomLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CustomLayoutParams(LayoutParams source) {
            super(source);
        }
    }

}
