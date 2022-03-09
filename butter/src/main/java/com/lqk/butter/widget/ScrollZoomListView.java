package com.lqk.butter.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import com.lqk.butter.R;

import androidx.annotation.RequiresApi;

/**
 * @author LQK
 * @time 2019/2/18 10:11
 * @remark 仿QQ空间可拉伸的头部
 */
public class ScrollZoomListView extends ListView {

    // 顶部大图
    private ImageView mImageView;

    private int mImageHeight = 60;

    public ScrollZoomListView(Context context) {
        super(context);
        mImageHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_image_height);
    }

    public ScrollZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_image_height);
    }

    public ScrollZoomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_image_height);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollZoomListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mImageHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_image_height);
    }

    public ImageView getmImageView() {
        return mImageView;
    }

    public void setmImageView(ImageView imageView) {
        this.mImageView = imageView;
    }

    /**
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     * @remark 3.0之后的方法 Y 方向 的一个增量
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // deltaY
        // 下拉过度时为 负值 -
        // 上拉过度时为 正值 +
        //
        if (deltaY < 0) {
            // 下拉过度，放大顶部大图
            if (mImageView != null) {
                // 不断修改 imageView 的高度
                mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;

                mImageView.requestLayout();
            }
        } else {
            // 上拉过度
            if (mImageView != null) {
                // 不断修改 imageView 的高度
                mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;

                mImageView.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // 当 ImageView 被放大后，需要执行缩小
        View header = (View) mImageView.getParent();
        // ListView 滑出去的 距离 (top 为 负值)
        int deltaY = header.getTop();

        if (mImageView.getHeight() > 0 && mImageView.getHeight() > mImageHeight) {
            mImageView.getLayoutParams().height = mImageView.getHeight() + deltaY;
            // 固定 header 的位置
            header.layout(header.getLeft(), 0, header.getRight(), header.getHeight());
            // 通知刷新
            mImageView.requestLayout();
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP) {
            // 松开手指
            ResetAnimation resetAnimation = new ResetAnimation(mImageHeight);
//            resetAnimation.setInterpolator(new BounceInterpolator());
//            resetAnimation.setInterpolator(new OvershootInterpolator());
//            resetAnimation.setInterpolator(new AnticipateInterpolator());
            resetAnimation.setInterpolator(new AnticipateOvershootInterpolator());
            resetAnimation.setDuration(300);
            mImageView.setAnimation(resetAnimation);
        }
        return super.onTouchEvent(ev);
    }

    public class ResetAnimation extends Animation {
        // 需要变化的高度
        private int extraHeight;
        private int currentHeight;

        public ResetAnimation(int targetHeight) {
            this.extraHeight = mImageView.getHeight() - targetHeight;
            this.currentHeight = mImageView.getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // 0 ~ 1
            // height ~ 初始高度

            mImageView.getLayoutParams().height = (int) (currentHeight - extraHeight * interpolatedTime);
            mImageView.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }
}
