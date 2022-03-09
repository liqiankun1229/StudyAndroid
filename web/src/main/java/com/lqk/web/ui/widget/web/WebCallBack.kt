package com.lqk.web.ui.widget.web

import android.view.View
import com.lqk.web.ui.widget.web.script.NativeJavaScriptInterface

/**
 * @author LQK
 * @date 2021/12/30 17:13
 * @remark WebView 的回调
 * 用户 容器部分 组件实现此接口 传入WebView
 */
interface WebCallBack {
    /**
     * 标题回调
     */
    fun onTitle(title: String)

    /**
     * 页面开始加载
     */
    fun onPageStart()

    /**
     * 页面加载进度
     */
    fun onProcess(process: Int)

    /**
     * 页面加载完毕
     */
    fun onPageFinish()

    // 页面弹框相关


    /**
     * 选择文件
     */
    fun chooseFile()

    /**
     * url 拦截/重定向
     */
    fun shouldUrl(url: String) {
        // 遍历白名单 是否拦截
    }

    /**
     * 全屏
     */
    fun onShowCustomView(view: View)

    /**
     * 退出全屏
     */
    fun onHideCustomView()

    /**
     * 关闭
     */
    fun close() {}

    /**
     * h5 通过传参, native 解析数据调用相应函数 callback 回调通知结果
     */
    fun h5callNative(
        funcName: String,
        params: LinkedHashMap<String, Any>,
        callback: NativeJavaScriptInterface.OnJsNeedCallBack?,
        callbackName: String
    )
}