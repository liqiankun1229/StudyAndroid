package com.lqk.net

/**
 * @author LQK
 * @time 2019/3/11 16:22
 * @remark
 */
object NetConstant {
    //代表客户端版本号
    val AVS = "avs"
    //代表本次请求的会话，从reqid中取值
    val SID = "sid"
    //代表本次请求的返回的素材id
    val IE = "ie"
    //code代表当前step的结果码
    val STEP_CD = "cd"

    //广告数据返回成功
    val AD_DATA_SUCCESS = "200"
    //广告数据解析失败
    val AD_DATA_FAILED = "202"
    //广告加载成功
    val AD_PLAY_SUCCESS = "300"
    //广告加载失败
    val AD_PLAY_FAILED = "301"

    /**
     * 埋点请求相关参数常量
     */
    enum class Params private constructor(val key: String, val value: String) {

        lvs("lvs", "4"),
        st("st", "12"),
        bt_phone("bt", "1"),
        bt_pad("bt", "0"),
        os("os", "1"),
        p("p", "2"),
        appid("appid", "xya"),
        ad_analize("sp", "2"),
        ad_load("sp", "3")
    }

    //播放器&移动后台监控埋点日志地址
    val ATM_MONITOR = ""

    val ATM_PRE = "val.atm.youku.com"

    /**
     * 广告服务器地址,待定
     */
    val DISPLAY_AD_URL = ""
}