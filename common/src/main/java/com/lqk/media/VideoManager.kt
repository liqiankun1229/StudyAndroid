package com.lqk.media

import android.view.ViewGroup
import com.google.gson.Gson

/**
 * @author LQK
 * @time 2019/3/12 9:08
 * @remark 视频播放业务逻辑
 */
class VideoManager : VideoSlot.ADSDKListener {

    private var mParentView: ViewGroup

    private var mSlot: VideoSlot? = null
    private var mADValue: ADValue? = null

    //the listener to the app layer

    constructor(mParentView: ViewGroup, adValue: String) {
        this.mParentView = mParentView
//        mADValue = ResponseEntityToModule.parseJsonToModule(adValue, ADValue::class.java) as ADValue
        mADValue = Gson().fromJson(adValue, ADValue::class.java)
        load()
    }

    /**
     * 创建 Slot 业务逻辑类
     */
    private fun load() {
        if (mADValue != null && mADValue!!.resource != "") {
            mSlot = VideoSlot(mADValue!!, this)
        }
    }

    fun updateVideoInScrollView() {
        if (mSlot != null) {
            mSlot?.updateVideoInScrollView()
        }
    }

    override fun getADParent(): ViewGroup {
        return mParentView
    }

    override fun onADVideoLoadSuccess() {

    }

    override fun onADVideoLoadFailed() {

    }

    override fun onADVideoLoadComplete() {

    }

    override fun onClickVideo(url: String) {

    }
}