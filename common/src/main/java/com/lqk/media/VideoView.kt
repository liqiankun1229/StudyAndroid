package com.lqk.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.lqk.R


/**
 * @author LQK
 * @time 2019/3/8 22:39
 * @remark 自定义视频播放 View
 *
 *
 * 业务：
 * 1.根据露出屏幕的百分比决定是否播放
 * 2.小屏-> 全屏的切换播放
 * 3.监听播放器产生的各种事件
 * 4.统计视频的播放次数 -> 设置接口给用户
 */
class VideoView : RelativeLayout,
        View.OnClickListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        TextureView.SurfaceTextureListener {

    // 定义常量
    companion object {
        const val TAG = "VideoView"
        const val TIME_MSG = 0x01
        const val TIME_INVAL = 1000L

        // 播放器生命状态
        const val STATE_ERROR = -1
        // 空闲
        const val STATE_IDLE = 0
        // 播放中
        const val STATE_PLAYING = 1
        // 暂停
        const val STATE_PAUSING = 2

        // 视频加载失败重新加载的次数
        const val LOAD_TOTAL_COUNT = 3
    }

    // UI
    private var mParentContainer: ViewGroup
    private lateinit var mPlayerView: RelativeLayout
    private lateinit var mVideoView: TextureView
    private lateinit var mMiniPlayBtn: Button
    private lateinit var mFullBtn: ImageView
    private lateinit var mLoadingBtn: ImageView
    private lateinit var mFrameView: ImageView
    // 音量控制器
    private var audioManager: AudioManager
    private var videoSurface: Surface? = null

    // Data
    private var mUrl: String = ""
    private var isMute: Boolean = false
    private var mScreenWidth = 0
    private var mDestationHeight = 0

    // Status 状态保护
    private var canPlay = true
    private var mIsRealPause = false
    private var mIsComplete = false
    private var mCurrentCount = 0
    // 播放状态 -> 默认是处于空闲状态
    private var playerState = STATE_IDLE

    private var mediaPlayer: MediaPlayer? = null
    private var listener: ADVideoPlayerListener? = null
    private var mScreenReceiver: ScreenEventReceiver? = null
    private var mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TIME_MSG -> {
                    if (isPlaying()) {
                        listener?.onBufferUpdate(getCurrentPosition())
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL)
                    }
                }
                else -> {
                }
            }
//            super.handleMessage(msg)
        }
    }

    constructor(context: Context?, parentContainer: ViewGroup) : super(context) {
        mParentContainer = parentContainer
        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // 初始化数据
        initData()
        // 初始化视图
        initView()
        // 注册广播
        registerBroadcastReceiver()
    }

    private fun initData() {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        mScreenWidth = displayMetrics.widthPixels
        mDestationHeight = (mScreenWidth * VideoConstant.VIDEO_HEIGHT_PERCENT).toInt()
    }

    /**
     * 加载 View 布局
     */
    private fun initView() {
        val inflater = LayoutInflater.from(context)
        mPlayerView = inflater.inflate(R.layout.layout_video, this) as RelativeLayout
        mVideoView = mPlayerView.findViewById(R.id.common_ttv_video)
        mVideoView.setOnClickListener(this)
        mVideoView.keepScreenOn = true
        mVideoView.surfaceTextureListener = this

        val params = LayoutParams(mScreenWidth, mDestationHeight)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        mPlayerView.layoutParams = params

        mMiniPlayBtn = mPlayerView.findViewById(R.id.common_btn_play)
        mFullBtn = mPlayerView.findViewById(R.id.common_iv_full)
        mLoadingBtn = mPlayerView.findViewById(R.id.common_iv_loading)
        mFrameView = mPlayerView.findViewById(R.id.common_iv_framing)
        mMiniPlayBtn.setOnClickListener(this)
        mFullBtn.setOnClickListener(this)
    }

    /**
     * 播放地址
     */
    fun setDataSource(url: String) {
        this.mUrl = url
    }

    fun setListener(l: ADVideoPlayerListener) {
        this.listener = l
    }

    /**
     * View 显示和隐藏的变化
     */
    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        Log.d(TAG, "View-$visibility")
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE && playerState == STATE_PAUSING) {
            // 决定是否继续播放 暂停或播放完成
            if (isRealPause() || isComplete()) {
                // 暂停播放
                pause()
            } else {
                decideCanPlay()
            }
        } else {
            pause()
        }
    }

    /**
     * 如果事件传递到 播放器中 则将事件消费掉
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
        return true
    }

    /**
     * 注册广播
     */
    private fun registerBroadcastReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = ScreenEventReceiver()
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_USER_PRESENT)
            context.registerReceiver(mScreenReceiver, filter)
        }
    }

    /**
     * 销毁广播
     */
    private fun unRegisterBroadcastReceiver() {
        if (mScreenReceiver != null) {
            context.unregisterReceiver(mScreenReceiver)
        }
    }

    /**
     * 是否静音 true：静音
     */
    fun mute(mute: Boolean) {
        isMute = mute
        if (mediaPlayer != null && this.audioManager != null) {
            val volume = if (isMute) {
                0.0f
            } else {
                1.0f
            }
            // 设置音量
            mediaPlayer?.setVolume(volume, volume)
        }
    }

    /**
     * 点击 事件
     */
    override fun onClick(v: View?) {
        v ?: return
        when (v) {
            this.mMiniPlayBtn -> {
                if (this.playerState == STATE_PAUSING) {

                }
                if (VideoUtil.getVisiblePercent(mParentContainer) > VideoConstant.VIDEO_SCREEN_PERCENT) {
                    play()
                }
            }
            this.mFullBtn -> {
                this.listener?.onClickFullScreenBtn()
            }
            this.mVideoView -> {
                this.listener?.onClickVideo()
            }
        }
    }

    /**
     * 播放器加载完毕，可以进行视频播放
     */
    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "prepared")
        showPlayView()
        mediaPlayer = mp
        if (mediaPlayer != null) {
            mediaPlayer?.setOnBufferingUpdateListener(this)
            mediaPlayer?.setSurface(videoSurface)
            mCurrentCount = 0
            if (listener != null) {
                listener?.onADVideoLoadSuccess()
            }
            // 满足自动播放
            if (VideoUtil.getVisiblePercent(mParentContainer) >= VideoConstant.VIDEO_SCREEN_PERCENT) {
                setCurrentPlayState(STATE_PAUSING)
                play()
            } else {
                setCurrentPlayState(STATE_PLAYING)
                pause()
            }
        }
    }

    /**
     * 播放器产生异常时回调
     */
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.d(TAG, "video-error")
        this.playerState = STATE_ERROR
        if (mCurrentCount >= LOAD_TOTAL_COUNT) {
            if (listener != null) {
                listener?.onADVideoLoadFailed()
            }
            showPauseView(false)
        }
        stop()
        return false
    }

    /**
     * 视频播放完毕后回调
     */
    override fun onCompletion(mp: MediaPlayer?) {
        Log.d(TAG, "completion")
        if (listener != null) {
            listener?.onADVideoLoadComplete()
        }
        setIsComplete(true)
        setIsRealPause(true)
        // 播放完毕, 回到初始状态
        playBack()
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return false
    }

    /**
     * 表示 TextureView 处于就绪状态
     */
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        videoSurface = Surface(surface)
        checkMediaPlayer()
        // 界面切换时会销毁 surface 需要重新记载一次
        mediaPlayer?.setSurface(videoSurface)
        load()
    }

    /**
     * 加载视频地址
     */
    fun load() {
        if (this.playerState != STATE_IDLE) {
            return
        }
        Log.d(TAG, "video-load")
        showLoadingView()
        try {
            setCurrentPlayState(STATE_IDLE)
            checkMediaPlayer()
            mute(false)
            mediaPlayer?.setDataSource(mUrl)
            mediaPlayer?.setOnPreparedListener(this)
            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            Log.d(TAG, "${e.message}")
            stop()
        }
    }

    /**
     * 暂停视频播放
     */
    fun pause() {
        if (this.playerState != STATE_PLAYING) {
            return
        }
        Log.d(TAG, "video-pause")
        setCurrentPlayState(STATE_PAUSING)
        if (isPlaying()) {
            // 暂停播放
            mediaPlayer?.pause()
        }
        showPauseView(false)
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 恢复播放
     */
    fun play() {
        if (playerState != STATE_PAUSING) {
            return
        }
        Log.d(TAG, "video-play")
        if (!isPlaying()) {
            // 开始播放, 将View 信息修改成播放状态值
            entryResumeState()
            mediaPlayer?.setOnSeekCompleteListener(null)
            mediaPlayer?.start()
            mHandler.sendEmptyMessage(TIME_MSG)
            showPauseView(true)
        } else {
            showPauseView(false)
        }
    }

    /**
     * 返回播放0进度 并暂停播放
     */
    fun playBack() {
        setCurrentPlayState(STATE_PAUSING)
        mHandler.removeCallbacksAndMessages(null)
        if (mediaPlayer != null) {
            mediaPlayer?.setOnSeekCompleteListener(null)
            mediaPlayer?.seekTo(0)
            mediaPlayer?.pause()
        }
        showPauseView(false)
    }

    /**
     * 停止播放
     */
    fun stop() {
        Log.d(TAG, "video-stop")
        if (this.mediaPlayer != null && isPlaying()) {
            this.mediaPlayer?.reset()
            this.mediaPlayer?.setOnSeekCompleteListener(null)
            this.mediaPlayer?.stop()
            // 释放
            this.mediaPlayer?.release()
            this.mediaPlayer = null
        }
        mHandler.removeCallbacksAndMessages(null)
        setCurrentPlayState(STATE_IDLE)

        // 重新加载视频
        if (mCurrentCount < LOAD_TOTAL_COUNT) {
            mCurrentCount += 1
            load()
        } else {
            // 停止重试加载
            showPauseView(false)
        }
    }

    /**
     * 回收播放器资源
     */
    fun destroy() {
        Log.d(TAG, "video-destroy")
        if (this.mediaPlayer != null) {
            this.mediaPlayer?.setOnSeekCompleteListener(null)
            this.mediaPlayer?.stop()
            this.mediaPlayer?.release()
            this.mediaPlayer = null
        }
        setCurrentPlayState(STATE_IDLE)
        mCurrentCount = 0
        setIsComplete(false)
        setIsRealPause(false)
        unRegisterBroadcastReceiver()
        // release all message and runnable
        mHandler.removeCallbacksAndMessages(null)
        // 除了播放和loading外其余任何状态都显示pause
        showPauseView(false)
    }

    /**
     * 快进到指定进度, 并继续播放
     */
    fun seekAndResume(position: Int) {
        Log.d(TAG, "video-seekAndResume")
        if (mediaPlayer != null) {
            showPauseView(true)
            entryResumeState()
            mediaPlayer?.seekTo(position)
            mediaPlayer?.setOnSeekCompleteListener {
                mediaPlayer?.start()
                mHandler.sendEmptyMessage(TIME_MSG)
            }
        }
    }

    /**
     * 快进到指定进度，并暂停视频
     */
    fun seekAndPause(position: Int) {
        if (this.playerState != STATE_PLAYING) {
            return
        }
        showPauseView(false)
        setCurrentPlayState(STATE_PAUSING)
        if (isPlaying()) {
            mediaPlayer?.seekTo(position)
            mediaPlayer?.setOnSeekCompleteListener {
                mediaPlayer?.pause()

            }
        }
    }

    @Synchronized
    fun checkMediaPlayer() {
        if (mediaPlayer == null) {
            // 每次都重新创建一个新的播放器
            mediaPlayer = createMediaPlayer()
        }
    }

    private fun createMediaPlayer(): MediaPlayer {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.reset()
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.setOnInfoListener(this)
        mediaPlayer?.setOnErrorListener(this)
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        if (videoSurface != null && videoSurface!!.isValid) {
            mediaPlayer?.setSurface(videoSurface)
        } else {
            stop()
        }
        return mediaPlayer!!
    }

    /**
     * 判断是否开始播放视频
     */
    private fun decideCanPlay() {
        if (VideoUtil.getVisiblePercent(mParentContainer) >= VideoConstant.VIDEO_SCREEN_PERCENT) {
            setCurrentPlayState(STATE_PAUSING)
            play()
        } else {
            setCurrentPlayState(STATE_PLAYING)
            pause()
        }
    }

    fun isShowFullBtn(isShow: Boolean) {
        mFullBtn.setImageResource(if (isShow) R.drawable.ic_play else R.drawable.ic_loading)
        mFullBtn.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 判断播放状态
     */
    fun isPlaying(): Boolean {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            return true
        }
        return false
    }

    fun isRealPause(): Boolean {
        return mIsRealPause
    }

    fun isComplete(): Boolean {
        return mIsComplete
    }

    /**
     * 暂停/播放
     */
    private fun showPauseView(show: Boolean) {
        mFullBtn.visibility = visibility(show)
        mMiniPlayBtn.visibility = visibility(!show)
        mLoadingBtn.clearAnimation()
        mLoadingBtn.visibility = visibility(!show)
        if (!show) {
            mFrameView.visibility = View.VISIBLE
            loadFrameImage()
        } else {
            mFrameView.visibility = View.GONE
        }
    }

    /**
     * 加载状态
     */
    private fun showLoadingView() {
        mFullBtn.visibility = View.GONE
        mLoadingBtn.visibility = View.VISIBLE
//        val animationDrawable = mLoadingBtn.background as AnimationDrawable
//        animationDrawable.start()
        mMiniPlayBtn.visibility = View.GONE
        mFrameView.visibility = View.GONE
        loadFrameImage()
    }

    /**
     * 异步加载 占位图
     */
    private fun loadFrameImage() {
//        if (mFrameLoadListener!= null){
//
//        }
    }

    private fun showPlayView() {
//        mLoadingBar.clearAnimation();
//        mLoadingBar.setVisibility(View.GONE);
        mMiniPlayBtn.visibility = View.GONE
        mFrameView.visibility = View.GONE
    }

    /**
     * 开始播放
     */
    private fun entryResumeState() {
        canPlay = false
        setCurrentPlayState(STATE_PLAYING)
        setIsRealPause(false)
        setIsComplete(false)
    }

    fun setIsRealPause(b: Boolean) {
        this.mIsRealPause = b
    }

    fun setIsComplete(b: Boolean) {
        this.mIsComplete = b
    }

    /**
     * 修改播放状态
     */
    private fun setCurrentPlayState(state: Int) {
        playerState = state
    }

    /**
     * 播放时长
     */
    fun getDuration(): Int {
        if (this.mediaPlayer != null) {
            return mediaPlayer!!.duration
        }
        return 0
    }

    /**
     * 当前播放位置
     */
    fun getCurrentPosition(): Int {
        if (this.mediaPlayer != null) {
            return mediaPlayer!!.currentPosition
        }
        return 0
    }

    /**
     * View 的显示和隐藏
     */
    private fun visibility(boolean: Boolean): Int {
        return if (boolean) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun isFrameHidden(): Boolean {
        return mFrameView.visibility != View.VISIBLE
    }

    /**
     * 广播
     */
    inner class ScreenEventReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            //主动锁屏时 pause, 主动解锁屏幕时，play
            when (intent?.action) {
                Intent.ACTION_USER_PRESENT -> {
                    if (playerState == STATE_PAUSING) {
                        if (mIsRealPause) {
                            //手动点的暂停，回来后还暂停
                            pause()
                        } else {
                            decideCanPlay()
                        }
                    }
                }
                Intent.ACTION_SCREEN_OFF -> {
                    if (playerState == STATE_PLAYING) {
                        pause()
                    }
                }
            }
        }
    }

    interface ADVideoPlayerListener {
        // 视频播放进度
        fun onBufferUpdate(position: Int)

        // 视频加载成功
        fun onADVideoLoadSuccess()

        // 单击全屏按钮
        fun onClickFullScreenBtn()

        // 单击播放器
        fun onClickVideo()

        // 点击关闭按钮
        fun onClickBackBtn()

        // 点击播放按钮
        fun onClickPlay()

        // 视频加载失败
        fun onADVideoLoadFailed()

        // 视频加载完成
        fun onADVideoLoadComplete()
    }
}