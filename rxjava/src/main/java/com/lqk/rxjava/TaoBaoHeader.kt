package com.lqk.rxjava

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.listener.OnStateChangedListener

/**
 * @author LQK
 * @time 2022/8/29 22:35
 *
 */
@SuppressLint("RestrictedApi")
class TaoBaoHeader : FrameLayout, RefreshHeader {
    companion object {
        const val TAG = "TaoBaoHeader"
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    /**
     * 加载布局
     */
    private fun initView(context: Context) {
        this.removeAllViews()
        val root = View.inflate(context, R.layout.header_refresh, this)
//        this.addView(root)
        root.findViewById<TextView>(R.id.tv).text = "下拉开始刷新"

    }

    // 拖动监听
    private var mOnMoveListener: OnMoveListener? = null
    fun initOnMoveListener(l: OnMoveListener) {
        this.mOnMoveListener = l
    }

    interface OnMoveListener {
        /**
         * @param i 0-1
         */
        fun moveListener(i: Float)
    }

    // 状态监听
    private var mOnStateListener: OnStateListener? = null
    fun initOnStateListener(l: OnStateChangedListener) {}

    interface OnStateListener {
        fun onState(state: RefreshState)
    }


    /**
     * 状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        Log.d(TAG, "onStateChanged: $oldState $newState")
        when (newState) {
            RefreshState.None -> {
                // 下拉开始刷新
                Log.d(TAG, "onStateChanged: 下拉开始刷新")
            }
            // 拖拽过程
            RefreshState.PullUpToLoad -> {}
            RefreshState.PullUpCanceled -> {}
            RefreshState.PullDownToRefresh -> {
                // 下拉开始刷新
                Log.d(TAG, "onStateChanged: 正在下拉进行刷新")
            }
            RefreshState.PullDownCanceled -> {
                Log.d(TAG, "onStateChanged: 释放取消刷新")
            }
            // 上拉
            RefreshState.Loading -> {
                // 正在加载
            }
            RefreshState.LoadFinish -> {
                // 上拉加载更多结束
            }
            RefreshState.LoadReleased -> {

            }
            RefreshState.ReleaseToLoad -> {}
            RefreshState.ReleaseToRefresh -> {
                // 释放进行刷新
                Log.d(TAG, "onStateChanged: 释放进行刷新")
            }
            RefreshState.ReleaseToTwoLevel -> {
                // 释放前往二楼
                Log.d(TAG, "onStateChanged: 释放前往二楼")
            }
            RefreshState.RefreshReleased -> {
                // 释放进行刷新
                Log.d(TAG, "onStateChanged: 释放进行刷新")
            }
            RefreshState.Refreshing -> {
                // 正在刷新
                Log.d(TAG, "onStateChanged: 正在刷新")
            }
            RefreshState.TwoLevel -> {

            }
            RefreshState.TwoLevelReleased -> {

            }
            RefreshState.TwoLevelFinish -> {

            }
            RefreshState.RefreshFinish -> {
                Log.d(TAG, "onStateChanged: 结束刷新")
            }
            else -> {
                // 其他 不做处理
            }
        }
    }


    /**
     * 获取真实视图（必须返回，不能为null）
     */
    override fun getView(): View {
        return this
    }

    /**
     * 获取变换方式（必须指定一个：平移、拉伸、固定、全屏）
     */
    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    /**
     * 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    override fun setPrimaryColors(vararg colors: Int) {

    }

    /**
     * 尺寸定义初始化完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     * @param kernel RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        Log.d(TAG, "onInitialized: $height $maxDragHeight")
        kernel.refreshContent.view
    }

    /**
     * 手指拖动下拉（会连续多次调用，添加isDragging并取代之前的onPulling、onReleasing）
     * @param isDragging true 手指正在拖动 false 回弹动画
     * @param percent 下拉的百分比 值 = offset/footerHeight (0 - percent - (footerHeight+maxDragHeight) / footerHeight )
     * @param offset 下拉的像素偏移量  0 - offset - (footerHeight+maxDragHeight)
     * @param height 高度 HeaderHeight or FooterHeight (offset 可以超过 height 此时 percent 大于 1)
     * @param maxDragHeight 最大拖动高度 offset 可以超过 height 参数 但是不会超过 maxDragHeight
     */
    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
        Log.d(TAG, "onMoving: $isDragging $percent $offset $height $maxDragHeight")
        if (offset > height) {
            // 完全透明
            mOnMoveListener?.moveListener(1f)
        } else {
            // 比例透明
            mOnMoveListener?.moveListener(offset.toFloat() / height.toFloat())
        }
    }

    /**
     * 释放时刻（调用一次，将会触发加载）
     * @param refreshLayout RefreshLayout
     * @param height 高度 HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    /**
     * 开始动画（开始刷新或者开始加载动画）
     * @param refreshLayout RefreshLayout
     * @param height HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    /**
     * 动画结束
     * @param refreshLayout RefreshLayout
     * @param success 数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 0
    }

    /**
     * 水平方向的拖动
     * @param percentX 下拉时，手指水平坐标对屏幕的占比（0 - percentX - 1）
     * @param offsetX 下拉时，手指水平坐标对屏幕的偏移（0 - offsetX - LayoutWidth）
     * @param offsetMax 最大的偏移量
     */
    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    /**
     * 是否支持水平方向的拖动（将会影响到onHorizontalDrag的调用）
     * @return 水平拖动需要消耗更多的时间和资源，所以如果不支持请返回false
     */
    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }
}