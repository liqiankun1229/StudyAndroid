package com.lqk.media

import com.lqk.media.VideoConstant.AutoPlaySetting


object AdParameters {
    //用来记录可自动播放的条件
    private var currentSetting = AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI //默认都可以自动播放

    fun setCurrentSetting(setting: AutoPlaySetting) {
        currentSetting = setting
    }

    fun getCurrentSetting(): AutoPlaySetting {
        return currentSetting
    }

    /**
     * 获取sdk当前版本号
     */
    fun getAdSDKVersion(): String {
        return "1.0.0"
    }
}
