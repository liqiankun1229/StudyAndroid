package com.lqk.media

/**
 * @author LQK
 * @time 2019/3/10 11:56
 * @remark 视频播放器常量
 */
object VideoConstant {
    // 毫秒单位
    var MILLION_UNIT = 1000
    // 视频的高宽比例
    const val VIDEO_HEIGHT_PERCENT = 9 / 16.0F
    // 视频播放的临界值 -> View 显示的百分比
    var VIDEO_SCREEN_PERCENT = 50

    // 设置自动播放的条件
    var currentSetting = VideoConstant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI

    //自动播放条件
    enum class AutoPlaySetting {
        AUTO_PLAY_ONLY_WIFI,
        AUTO_PLAY_3G_4G_WIFI,
        AUTO_PLAY_NEVER
    }

    //素材类型
    const val MATERIAL_IMAGE = "image"
    const val MATERIAL_HTML = "html"
}