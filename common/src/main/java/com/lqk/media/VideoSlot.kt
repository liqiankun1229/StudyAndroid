package com.lqk.media

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup


/**
 * @author LQK
 * @time 2019/3/11 13:36
 * @remark 视频 -> 业务逻辑层
 */
class VideoSlot : VideoView.ADVideoPlayerListener {

    companion object {
        const val TAG = "VideoSlot"
    }

    private var mContext: Context

    // UI
    private lateinit var mVideoView: VideoView
    // 播放器的父容器
    private var mParentView: ViewGroup

    // Data
    private var mVideoInfo: ADValue? = null

    private var mSlotListener: ADSDKListener? = null
    private var canPause: Boolean = false
    private var lastArea = 0

    constructor(mADValue: ADValue, mSlotListener: ADSDKListener) {
        this.mVideoInfo = mADValue
        this.mSlotListener = mSlotListener
        mParentView = mSlotListener.getADParent()
        mContext = mParentView.context
        initVideoView()
    }

    private fun initVideoView() {
        mVideoView = VideoView(mContext, mParentView)
        if (mVideoInfo != null) {
            mVideoView.setDataSource(mVideoInfo!!.resource)
            mVideoView.setListener(this)
        }

        mParentView.addView(mVideoView)
    }


    override fun onBufferUpdate(position: Int) {

    }

    override fun onADVideoLoadSuccess() {
        if (mSlotListener != null) {
            mSlotListener?.onADVideoLoadSuccess()
        }
    }

    /**
     * 全屏播放
     */
    override fun onClickFullScreenBtn() {
        mParentView.removeView(mVideoView)
        // 创建全屏播放 dialog
        val dialog = VideoFullDialog(mContext, mVideoView, mVideoInfo!!)
        dialog.setListener(object : VideoFullDialog.FullToSmallListener {
            override fun getCurrentPlayPosition(position: Int) {
                // 关闭全屏播放
                backToSmallMode(position)
            }

            override fun playComplete() {
                // 全屏播放完毕
                bigPlayComplete()
            }
        })
//        dialog.setSlotListener()
        dialog.show()
    }

    private fun backToSmallMode(position: Int) {
        if (mVideoView.parent == null) {
            mParentView.addView(mVideoView)
        }
        mVideoView.translationY = 0F
        //防止动画导致偏离父容器
        mVideoView.isShowFullBtn(true)
        // 小屏静音播放
        mVideoView.mute(false)
        // 重新设置监听
        mVideoView.setListener(this)
        // 跳到指定位置并播放
        mVideoView.seekAndResume(position)
        // 标为可自动暂停
        canPause = true
    }

    private fun bigPlayComplete() {
        if (mVideoView.parent == null) {
            mParentView.addView(mVideoView)
        }
        mVideoView.translationY = 0F
        // 防止动画导致偏离父容器
        mVideoView.isShowFullBtn(true)
        mVideoView.mute(false)
        mVideoView.setListener(this)
        // 视频进度回到初始状态 并暂停
        mVideoView.seekAndPause(0)
        canPause = false
    }

    override fun onClickVideo() {
        mVideoInfo ?: return
        val desationUrl = mVideoInfo?.resource
        if (!TextUtils.isEmpty(desationUrl)) {
            // 在浏览器页面显示播放地址
            val intent = Intent(mContext, ADBrowserActivity::class.java)
            intent.putExtra(ADBrowserActivity.KEY_URL, mVideoInfo!!.resource)
            mContext.startActivity(intent)
        }
    }

    override fun onClickBackBtn() {

    }

    override fun onClickPlay() {

    }

    /**
     * 视频加载失败
     */
    override fun onADVideoLoadFailed() {
        if (mSlotListener != null) {
            mSlotListener?.onADVideoLoadFailed()
        }
        // 加载失败 设置全部设置回默认状态
        canPause = false
    }

    /**
     * 判断视频是否在播放状态
     */
    private fun isPlaying(): Boolean {
        return if (mVideoView != null) {
            mVideoView.isPlaying()
        } else {
            false
        }
    }

    private fun isRealPause(): Boolean {
        return if (mVideoView != null) {
            mVideoView.isRealPause()
        } else {
            false
        }
    }

    private fun isComplete(): Boolean {
        return if (mVideoView != null) {
            mVideoView.isComplete()
        } else false
    }

    // 暂停播放视频
    private fun pauseVideo(isAuto: Boolean) {
        if (mVideoView != null) {
            if (isAuto) {
                //发自动暂停监测
                if (!isRealPause() && isPlaying()) {
                    try {
//                        ReportManager.pauseVideoReport(mVideoInfo?.event?.pause?.content, getPosition().toLong())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            mVideoView.pause()
//            mVideoView.seekAndPause(0)
        }
    }

    // 播放视频
    private fun resumeVideo() {
        if (mVideoView != null) {
            mVideoView.play()
            if (isPlaying()) {
                sendSUSReport(true) //发自动播放监测
            }
        }
    }

    /**
     * 播放器滑出屏幕则暂停视频
     */
    fun updateVideoInScrollView() {
        val currentArea = VideoUtil.getVisiblePercent(mParentView)
        Log.d(TAG, "$currentArea")
        // 小于0表示未出现在屏幕上，不做任何处理,播放器还在外部，没有出现在屏幕中
        if (currentArea <= 0) {
            return
        }
        // view 在滑入和滑出屏幕时 的异常处理
        if (Math.abs(currentArea - lastArea) >= 100) {
            return
        }
        // 滑动没有超过 50 时处理逻辑
        if (currentArea < VideoConstant.VIDEO_SCREEN_PERCENT) {
            //进入自动暂停状态
            if (canPause) {
                pauseVideo(true)
                canPause = false
            }
            lastArea = 0
            mVideoView.setIsComplete(false) // 滑动出50%后标记为从头开始播
            mVideoView.setIsRealPause(false) //以前叫setPauseButtonClick()
            return
        }
        // 当视频真正进入状态时处理
        if (isRealPause() || isComplete()) {
            //进入手动暂停或者播放结束，播放结束和不满足自动播放条件都作为手动暂停
            pauseVideo(false)
            canPause = false
            return
        }

        //满足自动播放条件或者用户主动点击播放，开始播放
        if (VideoUtil.canAutoPlay(mContext, AdParameters.getCurrentSetting()) || isPlaying()) {
            // 开始播放视频
            lastArea = currentArea
            resumeVideo()
            canPause = true
            mVideoView.setIsRealPause(false)
        } else {
            // 不满足播放条件
            pauseVideo(false)
            mVideoView.setIsRealPause(true) //不能自动播放则设置为手动暂停效果
        }
    }

    fun destroy() {
        mVideoView.destroy()
//        mVideoView = null
//        mContext = null
//        mVideoInfo = null
    }

    override fun onADVideoLoadComplete() {
        try {
//            ReportManager.sueReport(mVideoInfo?.endMonitor, false, getDuration().toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (mSlotListener != null) {
            mSlotListener?.onADVideoLoadComplete()
        }
        mVideoView?.setIsRealPause(true)
    }

    private fun getPosition(): Int {
        return mVideoView.getCurrentPosition() / VideoConstant.MILLION_UNIT
    }

    private fun getDuration(): Int {
        return mVideoView.getDuration()
    }

    /**
     * 发送视频开始播放 埋点统计
     *
     * @param isAuto
     */
    private fun sendSUSReport(isAuto: Boolean) {
        try {
//            ReportManager.susReport(mVideoInfo?.startMonitor, isAuto)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    interface ADSDKListener {
        fun getADParent(): ViewGroup

        fun onADVideoLoadSuccess()

        fun onADVideoLoadFailed()

        fun onADVideoLoadComplete()

        fun onClickVideo(url: String)
    }
}