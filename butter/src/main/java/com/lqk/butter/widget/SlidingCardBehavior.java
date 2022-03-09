package com.lqk.butter.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * @author LQK
 * @time 2019/2/21 21:31
 * @remark Behavior
 */
public class SlidingCardBehavior extends CoordinatorLayout.Behavior<SlidingCardLayout> {

    private int mInitialOffset;

    /**
     * 获取上一个SlidingCard 控件
     */
    private SlidingCardLayout getPreviousChild(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view instanceof SlidingCardLayout) {
                return (SlidingCardLayout) view;
            }
        }
        return null;
    }

    /**
     * 获取下一个SlidingCard 控件
     */
    private SlidingCardLayout getNextChild(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex + 1; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof SlidingCardLayout) {
                return (SlidingCardLayout) view;
            }
        }
        return null;
    }

    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent,
                                  @NonNull SlidingCardLayout child,
                                  int parentWidthMeasureSpec, int widthUsed,
                                  int parentHeightMeasureSpec, int heightUsed) {
        // 测量 child
        // 卡片的高度 = 父容器给的高度 - 上边和下边 child 头部高度的总和
        // 测量child
        int offset = getChildMeasureOffset(parent, child);
        // SlidingCard 实际高度 => 屏幕高度 - (控件数量 -1) * 控件Header高度
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset;
        // 测量结果
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        return true;
//            return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    /**
     * 获取子控件的顶部top
     */
    private int getChildMeasureOffset(CoordinatorLayout parent,
                                      SlidingCardLayout child) {
        int offset = 0;
        // 上边和下边几个 child 的总和
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlidingCardLayout) {
                offset += ((SlidingCardLayout) view).getHeaderHeight();
            }
        }
        return offset;
    }

    /**
     * 摆放
     */
    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent,
                                 @NonNull SlidingCardLayout child,
                                 int layoutDirection) {
        // 按照默认情况摆放 （卡片堆在一起）, 其他left， right 取默认摆放的值
        if (getPreviousChild(parent, child) == null) {
            // 在第一个 SlidingCard 时 对 Parent 进行摆放
            parent.onLayoutChild(child, layoutDirection);
        }
        parent.onLayoutChild(child, layoutDirection);
        // 获取 上一个 SlidingCardLayout 控件
        SlidingCardLayout previous = getPreviousChild(parent, child);
        if (previous != null) {
            // offset 为上一个 View （SlidingCardLayout）的顶部布局，
            int offset = previous.getTop() + previous.getHeaderHeight();
            // child.layout(0, t, child.getMeasuredWidth(), 0);
            child.offsetTopAndBottom(offset);
        }
        mInitialOffset = child.getTop(); // 0 ~ n*headerView
//            return super.onLayoutChild(parent, child, layoutDirection);
        return true;
    }

    /**
     * 判断是否开始上下滑动
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull SlidingCardLayout child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        // 要监听哪些方向的滑动
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
//            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout parent,
                                  @NonNull SlidingCardLayout child,
                                  @NonNull View target,
                                  int dx, int dy,
                                  @NonNull int[] consumed, int type) {

        // 监听滑动情况，控制自己的滑动，以及其他卡片的联动效果
        // 手指滑动 -- child 要偏移的值， dy ：上滑 + ，下滑 -
        // 滑动范围
        int minOffset = mInitialOffset;
        int maxOffset = mInitialOffset + child.getMeasuredHeight() - child.getHeaderHeight();
        int initialOffset = child.getTop();
//        int offset = -dy;
        int offset = getMiddleValue(initialOffset - dy, minOffset, maxOffset) - initialOffset;
//            child.layout();
        child.offsetTopAndBottom(offset);
//        child.layout(0, offset, 0, offset);
        // 消耗
        consumed[1] = -offset;
        // 联动效果
        shiftSliding(consumed[1], parent, child);
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    /**
     * 实际滑动
     */
    private void shiftSliding(int shift, CoordinatorLayout parent, SlidingCardLayout child) {
        // 判断 滑动方向
        if (shift == 0) {
            return;
        }
        if (shift > 0) {
            // 往上滑
            // 当前卡片
            SlidingCardLayout current = child;
            // 上一个 卡片
            SlidingCardLayout cardLayout = getPreviousChild(parent, current);
            while (cardLayout != null) {
                int offset = getHeaderOverlap(cardLayout, current);
                if (offset > 0) {
                    cardLayout.offsetTopAndBottom(-offset);
                }
                current = cardLayout;
                cardLayout = getPreviousChild(parent, current);
            }
        } else {
            // 往下滑
            // 当前卡片
            SlidingCardLayout current = child;
            // 上一个 卡片
            SlidingCardLayout cardLayout = getNextChild(parent, current);
            while (cardLayout != null) {
                int offset = getHeaderOverlap(current, cardLayout);
                if (offset > 0) {
                    cardLayout.offsetTopAndBottom(offset);
                }
                current = cardLayout;
                cardLayout = getNextChild(parent, current);
            }
        }
    }

    /**
     * 不断更新所有SlidingCardLayout top
     */
    private int getHeaderOverlap(SlidingCardLayout above, SlidingCardLayout current) {
        return above.getTop() + above.getHeaderHeight() - current.getTop();
    }

    /**
     * 取中间值
     */
    private int getMiddleValue(int offset, int minOffset, int maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        } else if (offset < minOffset) {
            return minOffset;
        } else {
            return offset;
        }
    }
}