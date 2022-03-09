package com.lqk.butter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.lqk.butter.R;

import androidx.annotation.RequiresApi;

/**
 * @author LQK
 * @time 2019/2/11 14:21
 * @remark 个性化 滑动指示器
 */
public class SweetPanelListView extends ListView implements AbsListView.OnScrollListener {

    private OnScrollPositionChangedListener mListener;

    private View mScrollBarPanel;
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;
    // 滑动条 的 Y 坐标 位置 --> 需要在 滑动过程中不断更新 -> onScroll()
    private int mScrollBarPanelPosition = 0;
    public Animation mInAnimation = null;
    public Animation mOutAnimation = null;

    //  指示器 在 ListView 中的 Y 轴高度
    public int thumbOffset = 0;
    private int mLastPosition = -1;


    public SweetPanelListView(Context context) {
        super(context);
    }

    public SweetPanelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 1.监听滑动 回调onScroll
        super.setOnScrollListener(this);
        // 获取 Xml 文件中定义的属性值
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SweetPanelListView);
        final int layoutId = typedArray.getResourceId(R.styleable.SweetPanelListView_scrollBarPanel, R.layout.layout_scrollbar_panel);
        final int inAnimation = typedArray.getResourceId(R.styleable.SweetPanelListView_scrollBarPanelInAnimation, R.anim.in);
        final int outAnimation = typedArray.getResourceId(R.styleable.SweetPanelListView_scrollBarPanelOutAnimation, R.anim.out);
        typedArray.recycle();
        setScrollBarPanel(layoutId);
        mInAnimation = AnimationUtils.loadAnimation(context, inAnimation);
        mOutAnimation = AnimationUtils.loadAnimation(context, outAnimation);
        long durationMillis = ViewConfiguration.getScrollBarFadeDuration();
        mOutAnimation.setDuration(durationMillis);
        mOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束, 隐藏指示器布局
                if (mScrollBarPanel != null) {
                    mScrollBarPanel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public SweetPanelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SweetPanelListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置监听
     */
    private void setScrollBarPanel(int layoutId) {
        mScrollBarPanel = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
        mScrollBarPanel.setVisibility(View.GONE);
        // 提醒 自定义的 View 重新调整大小
        requestLayout();
    }

    /**
     * 滚动事件
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 1.监听 当前指示器 在 ListView 中的位置

//        computeHorizontalScrollExtent(): 滑动条，在滚动范围内占用的比例
//        computeHorizontalScrollOffset(): 滚动条的水平向幅度的位置
//        computeHorizontalScrollRange(): 滚动范围 0~10000

//        computeVerticalScrollExtent():
//        computeVerticalScrollOffset():
//        computeVerticalScrollRange():


        if (mScrollBarPanel != null && mListener != null) {

            // 1. 确定 滑块的高度 -> 滑块的高度/listView 的高度 = extent/Range
            int height = getMeasuredHeight() * computeVerticalScrollExtent() / computeVerticalScrollRange();
            // 2. 滑块正中间的 Y 坐标 -> 滑块的高度/extent = thumbOffset/offset
            thumbOffset = computeVerticalScrollOffset() * height / computeVerticalScrollExtent();
            thumbOffset += height / 2;
            // 获取 指示器 left
            int left = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth() - getVerticalScrollbarWidth() - 10;
            // 获取 指示器 top
            mScrollBarPanelPosition = thumbOffset - mScrollBarPanel.getMeasuredHeight() / 2;
            // 当指示器在顶端时
            if (mScrollBarPanelPosition < 0) {
                mScrollBarPanelPosition = 0;
            }

            // 不断修改 top 位置， top 位置 和滚动条的位置有关
            mScrollBarPanel.layout(
                    left,
                    mScrollBarPanelPosition,
                    left + mScrollBarPanel.getMeasuredWidth(),
                    mScrollBarPanelPosition + mScrollBarPanel.getMeasuredHeight());
            // 1. 监听当前指示器 的位置
            for (int i = 0; i < getChildCount(); i++) {
                View childView = getChildAt(i);
                if (childView != null) {
                    if (thumbOffset > childView.getTop() && thumbOffset < childView.getBottom()) {
                        if (mLastPosition != firstVisibleItem + i) {
                            mLastPosition = firstVisibleItem + i;
                            // 2. 需要调用回调方法
                            mListener.onPositionChanged(this, mLastPosition, mScrollBarPanel);
                            // 指示器的宽度 会根据内容变化， 需要重新测量 指示器
                            measureChild(mScrollBarPanel, mWidthMeasureSpec, mHeightMeasureSpec);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 滚动条
     */
    @Override
    protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
        // 判断是否 唤醒了滚动条
        boolean isAnimationPlayed = super.awakenScrollBars(startDelay, invalidate);
        // 当滑动的时候会唤醒 滑动条
        if (isAnimationPlayed && mScrollBarPanel != null) {
            if (mScrollBarPanel.getVisibility() == View.GONE) {
                mScrollBarPanel.setVisibility(View.VISIBLE);
                if (mInAnimation != null) {
                    mScrollBarPanel.startAnimation(mInAnimation);
                }
            }
            // 指示器停留 一段时间后 消失
            mHandler.removeCallbacks(mScrollBarOutRunnable);
            mHandler.postAtTime(mScrollBarOutRunnable, startDelay + AnimationUtils.currentAnimationTimeMillis());
        }
        return isAnimationPlayed;
    }

    private final Runnable mScrollBarOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOutAnimation != null) {
                mScrollBarPanel.startAnimation(mOutAnimation);
            }
        }
    };

    private Handler mHandler = new Handler();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算测量尺寸 ，同时要将子控件的尺寸也要计算好
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        if (mScrollBarPanel != null && getAdapter() != null) {
            mWidthMeasureSpec = widthMeasureSpec;
            mHeightMeasureSpec = heightMeasureSpec;
            measureChild(mScrollBarPanel, widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 摆放
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 将指示器 摆放在合适的位置
        if (mScrollBarPanel != null) {
            int left = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth() - getVerticalScrollbarWidth() - 10;

            mScrollBarPanel.layout(
                    left,
                    mScrollBarPanelPosition,
                    left + mScrollBarPanel.getMeasuredWidth(),
                    mScrollBarPanelPosition + mScrollBarPanel.getMeasuredHeight());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * 绘制
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制 指示器 在 ListView 所有子条目绘制完之后绘制
        // 仅在 指示器可见情况下 对View 进行绘制 -> 节省cpu绘制资源
        if (mScrollBarPanel != null && mScrollBarPanel.getVisibility() == View.VISIBLE) {
            drawChild(canvas, mScrollBarPanel, getDrawingTime()); // 绘制的时间
        }
    }

    public void setListener(OnScrollPositionChangedListener listener) {
        this.mListener = listener;
    }

    /**
     * 指示器变化监听回调
     */
    public interface OnScrollPositionChangedListener {
        void onPositionChanged(SweetPanelListView sweetPanelListView, int position, View panelView);
    }

}
