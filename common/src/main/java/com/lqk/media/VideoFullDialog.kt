package com.lqk.media

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.lqk.R


/**
 * @author LQK
 * @time 2019/3/11 22:08
 * @remark 全屏
 */
class VideoFullDialog : Dialog, VideoView.ADVideoPlayerListener {

    companion object {
        const val TAG = "VideoFullDialog"
    }

    // UI
    private var mVideoView: VideoView
    private lateinit var mParentView: ViewGroup
    private lateinit var mBackBtn: ImageView

    // Data
    private var mADValue: ADValue? = null
    private var mListener: FullToSmallListener? = null
    private var mPosition: Int = 0
    private var isFirst: Boolean = true

    private var mSlotListener: VideoSlot.ADSDKListener? = null

    constructor(context: Context, videoView: VideoView, instance: ADValue) : super(context) {
        // R.style
        mADValue = instance
        mVideoView = videoView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_video)
        window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        initVideoView()
    }

    /**
     * 初始化布局 View
     */
    private fun initVideoView() {
        mParentView = findViewById(R.id.common_rl_content_layout)
        mBackBtn = findViewById(R.id.common_iv_close)
        mBackBtn.setOnClickListener {
            onClickBack()
        }
        mVideoView.setListener(this)
        mVideoView.mute(false)
        mParentView.addView(mVideoView)
    }

    /**
     * 物理返回键
     */
    override fun onBackPressed() {
        onClickBack()
        super.onBackPressed()
    }

    /**
     * 点击返回事件
     */
    private fun onClickBack() {
        dismiss()
        if (mListener != null) {
            mListener?.getCurrentPlayPosition(mVideoView.getCurrentPosition())
        }
    }

    /**
     * 对话框焦点改变回调
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            // 没有获取焦点时的逻辑
            mPosition = mVideoView.getCurrentPosition()
            mVideoView.pause()
        } else {
            if (isFirst) {
                // 对话框时首次创建,且首次获取到焦点
                mVideoView.seekAndResume(mPosition)
            } else {
                // 获取到焦点时的逻辑 恢复视频播放
                mVideoView.play()
            }
        }
        isFirst = false
    }

    /**
     * 关闭对话框
     */
    override fun dismiss() {
        // 将播放器从对话框中移除
        mParentView.removeView(mVideoView)
        super.dismiss()
    }

    override fun onBufferUpdate(position: Int) {
        try {
            if (mADValue != null) {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onADVideoLoadSuccess() {
        if (mVideoView != null) {
            mVideoView.play()
        }
    }

    override fun onClickFullScreenBtn() {
        onClickVideo()
        // 将播放器从 ParentView 中移除
//        mParentView.removeView(mVideoView)
//        // 创建全屏播放 Dialog
//        val dialog = VideoFullDialog

    }

    override fun onClickVideo() {
        val desationUrl = mADValue?.resource ?: ""
        if (mSlotListener != null) {
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                mSlotListener?.onClickVideo(desationUrl)
            }
        } else {
            //走默认样式
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                val intent = Intent(context, ADBrowserActivity::class.java)
                intent.putExtra(ADBrowserActivity.KEY_URL, mADValue?.resource)
                context.startActivity(intent)

            }
        }
    }

    override fun onClickBackBtn() {
        onClickBack()
    }

    override fun onClickPlay() {

    }

    override fun onADVideoLoadFailed() {

    }

    /**
     * 播放完毕 需要关闭全屏播放
     */
    override fun onADVideoLoadComplete() {

        try {
            val position = mVideoView.getDuration() / VideoConstant.MILLION_UNIT
//            ReportManager.sueReport(mADValue?.endMonitor, true, position.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }


        dismiss()
        if (mListener != null) {
            mListener?.playComplete()
        }
    }

    // 设置接口
    fun setListener(l: FullToSmallListener) {
        this.mListener = l
    }

    fun setSlotListener(l: VideoSlot.ADSDKListener) {
        this.mSlotListener = l
    }

    interface FullToSmallListener {
        // 全屏播放点击关闭回调
        fun getCurrentPlayPosition(position: Int)

        // 全屏播放结束回调
        fun playComplete()
    }
}