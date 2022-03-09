package com.lqk.mvp.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author lqk
 * @date 2018/8/16
 * @time 15:40
 * @remarks
 */
class GifView : AppCompatImageView {



    constructor(context: Context)
            : super(context)
    constructor(context: Context, attributeSet: AttributeSet)
            : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, def: Int)
            : super(context, attributeSet, def)





    interface OnGifPlayListener {

        // 开始播放
        fun onPlayStart()

        // 正在播放
        fun onPlaying()

        // 是否循环播放
        fun onPlayPause()

        // 重新播放
        fun onPlayRestart()

        // 停止播放
        fun onPlayEnd()

    }
}