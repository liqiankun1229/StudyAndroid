package com.lqk.web.ui.widget.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lqk.web.R
import com.lqk.web.ui.widget.web.script.NativeJavaScriptInterface
import com.lqk.web.utils.PhoneUtils
import com.lqk.web.utils.getNetworkOperatorName

open class WebActivity : AppCompatActivity(), WebCallBack {

    companion object {
        private const val TAG = "WebActivity"
        private const val KEY_URL = "KEY_URL"

        /**
         * 启动 WebActivity 默认包含一个 url 地址
         */
        fun start(context: Context, url: String) {
            // 当 url 非空, 并且是 http 开头的, 则启动 WebActivity
            if (url.isNotEmpty() && (url.startsWith("http") || url.startsWith("file"))) {
                val webIntent = Intent(context, WebActivity::class.java)
                webIntent.putExtra(KEY_URL, url)
                context.startActivity(webIntent)
            }
        }

        /**
         * 启动 WebActivity 默认包含一个 url 地址
         */
        fun startLocal(context: Context, url: String) {
            // 当 url 非空, 并且是 http 开头的, 则启动 WebActivity
            if (url.isNotEmpty() && (url.startsWith("file"))) {
                val webIntent = Intent(context, WebActivity::class.java)
                webIntent.putExtra(KEY_URL, url)
                context.startActivity(webIntent)
            }
        }
    }

    // <editor-fold desc="WebView 的一些监听">
    /**
     * 网页的标题
     */
    override fun onTitle(title: String) {
        tvTitle?.text = title
    }

    override fun onPageStart() {

    }

    /**
     * 网页加载的进度
     */
    override fun onProcess(process: Int) {

    }

    override fun onPageFinish() {
        // 注入用户拓展的 js

    }

    private var customView: View? = null

    override fun onShowCustomView(view: View) {
        // 赋值, 后面用于删除
        customView = view
        customView?.let {
            if (it.parent == null) {
                (this.window.decorView as ViewGroup).addView(it)
                // 是否开启横屏
//                this@WebActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                val attribute = this@WebActivity?.window?.attributes
//                attribute?.flags = attribute?.flags?.or(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//                this@WebActivity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }
    }

    override fun onHideCustomView() {
        customView?.let {
            if (it.parent != null) {
                (it.parent as ViewGroup).removeView(it)
                customView = null
                // 是否关闭横屏
            }
        }
    }

    /**
     * 选择文件
     */
    override fun chooseFile() {

    }

    /**
     * @param funcName 函数名称
     * @param params 参数列表
     * @param callback 回调 h5
     * 用户拓展 切换到主线程
     */
    override fun h5callNative(
        funcName: String,
        params: LinkedHashMap<String, Any>,
        callback: NativeJavaScriptInterface.OnJsNeedCallBack?,
        callbackName: String
    ) {
        when (funcName) {
            "closeCORS" -> {
                this.baseWebView?.settings?.allowUniversalAccessFromFileURLs = true

                callback?.onSucceed(params, callbackName)
            }
            "" -> {
                // 无法监听
            }
            "openWebView" -> {
                callback?.onSucceed(params, callbackName)
            }
            "closeWebView" -> {
                // todo 判断h5是否拦截
                this.finish()
            }
            "scanCode" -> {
                // 保存扫码
                AlertDialog.Builder(this)
                    .setTitle("是否进行扫码操作")
                    .setNegativeButton("确定") { _, _ ->
                        run {
                            callback?.onSucceed(
                                linkedMapOf("url" to "https://app.xjbsefjz.fun:11180/sdk-demo/#/"),
                                callbackName
                            )
                        }
                    }
                    .setNeutralButton("取消") { _, _ ->
                        run {
                            callback?.onFailed(500, "取消操作", callbackName)
                        }
                    }
                    .create()
                    .show()
            }
            "getNetWorkInfo" -> {
                callback?.onSucceed(
                    linkedMapOf("operator" to application.getNetworkOperatorName()),
                    callbackName
                )
            }
            "getDeviceInfo" -> {
                callback?.onSucceed(
                    linkedMapOf("operator" to application.getNetworkOperatorName()),
                    callbackName
                )
            }
            else -> {
                // 未知字段 用户处理 不处理则不做操作
                customFun(funcName, params, callback, callbackName)
            }
        }
    }

    /**
     * 给用户自己拓展 与 h5 交互的方法
     */
    open fun customFun(
        funcName: String,
        params: LinkedHashMap<String, Any>,
        callback: NativeJavaScriptInterface.OnJsNeedCallBack?,
        callbackName: String
    ) {
        // 如果还是为未知字段则返回结果
        when (funcName) {
            "" -> {}
            else -> {
                AlertDialog.Builder(this)
                    .setTitle(callbackName)
                    .setNegativeButton("成功") { _, _ ->
                        run {
                            callback?.onSucceed(
                                linkedMapOf(),
                                callbackName
                            )
                        }
                    }
                    .setNeutralButton("失败") { _, _ ->
                        run {
                            callback?.onFailed(500, "取消操作", callbackName)
                        }
                    }
                    .create()
                    .show()
            }
        }
    }

    // </editor-fold>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWebActivity()
    }

    private fun initWebActivity() {
        // 支持远程调试
        WebView.setWebContentsDebuggingEnabled(false)
        // 如过用户想要继承拓展, 必须包含 相关按钮
        // ImageView: iv_back, iv_cancel, iv_menu
        // TextView: tv_title
        // ProgressBar: progress
        // BaseWebView: web
        setContentView(initLayoutId())
        initView()
        initListener()
        initEvent()
        initData()
    }

    open fun initLayoutId(): Int {
        return R.layout.activity_web
    }

    // <editor-fold desc="页面组件">
    // webView
    private var baseWebView: BaseWebView? = null

    // 返回
    private var ivBack: ImageView? = null

    // 关闭
    private var ivCancel: ImageView? = null

    // 菜单
    private var ivMenu: ImageView? = null

    // 标题
    private var tvTitle: TextView? = null


    open fun initView() {
        ivBack = findViewById(R.id.iv_back)
        ivCancel = findViewById(R.id.iv_cancel)
        tvTitle = findViewById(R.id.tv_title)
        ivMenu = findViewById(R.id.iv_menu)
        baseWebView = findViewById(R.id.web)
    }
    //</editor-fold>

    // <editor-fold desc="用户点击事件监听">
    open fun initListener() {
        ivBack?.setOnClickListener {
            bindBack()
        }
        ivCancel?.setOnClickListener {
            bindCancel()
        }
        ivMenu?.setOnClickListener {
//            bindMenu()
            baseWebView?.reloadUrl()
//            var s = this.application.getNetworkOperatorName()
//            var s =  getNetWorkInfo()
        }
        // 获取当前 挂载对象名称
        baseWebView?.mountWebString()
        // 添加 web 事件监听 先设置回调监听,再进行挂载
        baseWebView?.initWebCallBack(this)
        initWebListener()
    }

    open fun initWebListener() {
        // 修改关键字
        baseWebView?.resetMountStr("hfiCore")
        baseWebView?.mountObj()
    }
    // </editor-fold>

    /**
     * 重写系统返回事件 统一操作
     */
    override fun onBackPressed() {
        if (baseWebView?.canGoBack() == true) {
            baseWebView?.back()
        } else {
            super.onBackPressed()
        }
    }

    // <editor-fold desc="控件事件绑定">
    /**
     * 返回事件 - 默认绑定到 webView 的back
     */
    open fun bindBack() {
        // 绑定到系统 返回事件上
        onBackPressed()
    }

    /**
     * 关闭事件 -
     */
    open fun bindCancel() {
        // todo 如果 H5 需要知道关闭 web 页面的事件, 需要通知到 h5
        this.finish()
    }

    open fun bindMenu() {
        // 构建数据
        var param = hashMapOf<String, Any>(
            "errCode" to "0",
            "errMsg" to "调用",
            "callBackParam" to "{}"
        )
        // jsbridge
        baseWebView?.loadJs("hfiJSBridge.onWeb", Gson().toJson(param)) {
            Log.d(TAG, "bindMenu: $it")
        }
    }

    // </editor-fold>

    //<editor-fold desc="事件总线">
    open fun initEvent() {}

    //</editor-fold>
    //<editor-fold desc="加载数据">
    open fun initData() {
        // 打印设备信息
        Log.d(TAG, "getPhoneWidth: ${PhoneUtils.getPhoneWidth(this)}")
        Log.d(TAG, "getDeviceWidth: ${PhoneUtils.getDeviceWidth(this)}")
        Log.d(TAG, "getDeviceHeight: ${PhoneUtils.getDeviceHeight(this)}")
//        Log.d(TAG, "getIMEI: ${PhoneUtils.getIMEI(this)}")
        Log.d(TAG, "getDeviceManufacturer: ${PhoneUtils.getDeviceManufacturer()}")
        Log.d(TAG, "getDeviceProduct: ${PhoneUtils.getDeviceProduct()}")
        Log.d(TAG, "getDeviceBrand: ${PhoneUtils.getDeviceBrand()}")
        Log.d(TAG, "getDeviceModel: ${PhoneUtils.getDeviceModel()}")
        Log.d(TAG, "getDeviceBoard: ${PhoneUtils.getDeviceBoard()}")
        Log.d(TAG, "getDeviceDevice: ${PhoneUtils.getDeviceDevice()}")
        Log.d(TAG, "getDeviceFubgerprint: ${PhoneUtils.getDeviceFubgerprint()}")
        Log.d(TAG, "getDeviceHardware: ${PhoneUtils.getDeviceHardware()}")
        Log.d(TAG, "getDeviceHost: ${PhoneUtils.getDeviceHost()}")
//        Log.d(TAG, "getDeviceId: ${PhoneUtils.getDeviceId()}")
        Log.d(TAG, "getDeviceUser: ${PhoneUtils.getDeviceUser()}")
        Log.d(TAG, "getDeviceSerial: ${PhoneUtils.getDeviceSerial()}")
        Log.d(TAG, "getDeviceSDK: ${PhoneUtils.getDeviceSDK()}")
        Log.d(TAG, "getDeviceAndroidVersion: ${PhoneUtils.getDeviceAndroidVersion()}")
        Log.d(TAG, "getDeviceDefaultLanguage: ${PhoneUtils.getDeviceDefaultLanguage()}")


        // 解析参数
//        val url = intent.getStringExtra(KEY_URL) ?: "https://app.xjbsefjz.fun:11180/sdk-demo/#/"
        val url = intent.getStringExtra(KEY_URL) ?: "http://10.100.157.175:8081/"
        if (url.isEmpty()) {
            // 没有 url 地址, 页面没必要启动
            finish()
        }
        baseWebView?.loadUrl(url)
    }
    //</editor-fold>
}