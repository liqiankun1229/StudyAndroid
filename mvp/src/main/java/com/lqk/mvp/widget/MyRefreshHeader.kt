package com.lqk.mvp.widget

import android.content.Context
import androidx.annotation.NonNull
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import com.lqk.utils.DensityUtil
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.internal.ArrowDrawable
import com.scwang.smartrefresh.layout.internal.ProgressDrawable

/**
 * @author lqk
 * @date 2018/8/16
 * @time 15:31
 * @remarks
 */
class MyRefreshHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs, 0), RefreshHeader {

    private val mHeaderText: TextView//标题文本
    private val mArrowView: ImageView//下拉箭头
    private val mProgressView: ImageView//刷新动画视图
    private val mProgressDrawable: ProgressDrawable//刷新动画

    init {
        gravity = Gravity.CENTER
        mHeaderText = TextView(context)
        mProgressDrawable = ProgressDrawable()
        mArrowView = ImageView(context)
        mProgressView = ImageView(context)
        mProgressView.setImageDrawable(mProgressDrawable)
        mArrowView.setImageDrawable(ArrowDrawable())
        addView(mProgressView, DensityUtil.dp2px(this.context, 20f), DensityUtil.dp2px(this.context, 20f))
        addView(mArrowView, DensityUtil.dp2px(this.context, 20f), DensityUtil.dp2px(this.context, 20f))
        addView(Space(context), DensityUtil.dp2px(this.context, 20f), DensityUtil.dp2px(this.context, 20f))
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        minimumHeight = DensityUtil.dp2px(this.context, 60f)
    }

    @NonNull
    override fun getView(): View {
        return this//真实的视图就是自己，不能返回null
    }

    @NonNull
    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate//指定为平移，不能null
    }

    override fun onStartAnimator(@NonNull layout: RefreshLayout, height: Int, maxDragHeight: Int) {
        mProgressDrawable.start()//开始动画
    }

    override fun onFinish(@NonNull layout: RefreshLayout, success: Boolean): Int {
        mProgressDrawable.stop()//停止动画
        mProgressView.visibility = GONE//隐藏动画
        if (success) {
            mHeaderText.text = "刷新完成"
        } else {
            mHeaderText.text = "刷新失败"
        }
        return 500//延迟500毫秒之后再弹回
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onStateChanged(@NonNull refreshLayout: RefreshLayout, @NonNull oldState: RefreshState, @NonNull newState: RefreshState) {
        when (newState) {
            RefreshState.None, RefreshState.PullDownToRefresh -> {
                mHeaderText.text = "下拉开始刷新"
                mArrowView.visibility = VISIBLE//显示下拉箭头
                mProgressView.visibility = GONE//隐藏动画
                mArrowView.animate().rotation(0f)//还原箭头方向
            }
            RefreshState.Refreshing -> {
                mHeaderText.text = "正在刷新"
                mProgressView.visibility = VISIBLE//显示加载动画
                mArrowView.visibility = GONE//隐藏箭头
            }
            RefreshState.ReleaseToRefresh -> {
                mHeaderText.text = "释放立即刷新"
                mArrowView.animate().rotation(180f)//显示箭头改为朝上
            }
        }
    }

    override fun setPrimaryColors(vararg colors: Int) {

    }

    override fun onInitialized(@NonNull kernel: RefreshKernel, height: Int, maxDragHeight: Int) {

    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {

    }

    //        @Override
    //        public void onPulling(float percent, int offset, int height, int maxDragHeight) {
    //
    //        }
    //        @Override
    //        public void onReleasing(float percent, int offset, int height, int maxDragHeight) {
    //
    //        }

    override fun onReleased(@NonNull refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }
}