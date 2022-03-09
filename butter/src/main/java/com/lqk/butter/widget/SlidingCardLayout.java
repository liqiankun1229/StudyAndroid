package com.lqk.butter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lqk.butter.R;
import com.lqk.butter.adapter.SlidingCardAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author LQK
 * @time 2019/2/21 10:32
 * @remark 自定义控件
 */
@CoordinatorLayout.DefaultBehavior(SlidingCardBehavior.class)
public class SlidingCardLayout extends FrameLayout {

    private int mHeaderViewHeight;

    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_sliding_card, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingCardLayout);
        TextView tvHeader = (TextView) findViewById(R.id.tv_header);
        tvHeader.setBackgroundColor(typedArray.getColor(R.styleable.SlidingCardLayout_android_colorBackground, Color.BLACK));
        tvHeader.setText(typedArray.getText(R.styleable.SlidingCardLayout_android_text));

//        mHeaderViewHeight = tvHeader.getMeasuredHeight();

        RecyclerView list = (RecyclerView) findViewById(R.id.rcv);
        list.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecyclerView.Adapter adapter = new SlidingCardAdapter();
        list.setAdapter(adapter);

        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        getParent().requestLayout();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mHeaderViewHeight = findViewById(R.id.tv_header).getMeasuredHeight();
        }
//        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int getHeaderHeight() {
        return mHeaderViewHeight == 0 ? findViewById(R.id.tv_header).getMeasuredHeight() : mHeaderViewHeight;
    }

}
