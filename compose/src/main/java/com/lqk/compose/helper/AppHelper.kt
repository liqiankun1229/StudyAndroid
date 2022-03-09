//package com.lqk.compose.helper
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.content.pm.ApplicationInfo
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import com.lqk.compose.R
//import com.lqk.compose.helper.info.AppData
//
///**
// * 应用帮助类
// */
//@SuppressLint("StaticFieldLeak")
//object AppHelper {
//    const val TAG = "AppHelper"
//    private lateinit var mContext: Context
//
//    private lateinit var packageManager: PackageManager
//
//    // 手机所有应用
//    private var mAllAppList = ArrayList<AppData>()
//
//    // 所有应用市场包名
//    private var markerStore = mutableListOf<String>()
//
//
//    fun initHelper(context: Context) {
//        mContext = context
//        packageManager = context.packageManager
//        loadAllApp()
//    }
//
//    /**
//     * 加载所有的app包
//     */
//    private fun loadAllApp() {
//        val intent = Intent(Intent.ACTION_MAIN, null)
//        intent.addCategory(Intent.CATEGORY_LAUNCHER)
//        val appInfo = packageManager.queryIntentActivities(intent, 0)
//        appInfo.forEachIndexed { index, resolveInfo ->
//            run {
//                val appData = AppData(
//                    resolveInfo.activityInfo.packageName,
//                    resolveInfo.loadLabel(packageManager) as String,
//                    resolveInfo.loadIcon(packageManager),
//                    resolveInfo.activityInfo.name,
//                    resolveInfo.activityInfo.flags == ApplicationInfo.FLAG_SYSTEM
//                )
//                mAllAppList.add(appData)
//            }
//        }
//        Log.e(TAG, "loadAllApp: $mAllAppList")
//        // 构建布局
//        initPageView()
//    }
//
//    var mAppViewList = mutableListOf<View>()
//
//    private fun initPageView() {
//        // 遍历所有app 创建view
//        for (i in 0 until getPageSize()) {
//            // 创建页面
//            val root = View.inflate(mContext, R.layout.item_app_manager, null) as ViewGroup
//            for (j in 0 until 6) {
//                // 行
//                val row = root.getChildAt(j) as ViewGroup
//                for (k in 0 until 4) {
//                    // 列
//                    val col = row.getChildAt(k) as ViewGroup
//                    // 具体的View
//                    val imageView = (col.getChildAt(0) as ImageView)
//                    val textView = (col.getChildAt(1) as TextView)
//                    val index = i * 24 + j * 4 + k
//                    if (index < mAllAppList.size) {
//                        val data = mAllAppList[index]
//                        imageView.setImageDrawable(data.appIcon)
//                        textView.text = data.appName
//                        col.setOnClickListener {
//                            intentApp(data.packageName)
//                        }
//                    }
//                }
//            }
//            mAppViewList.add(root)
//        }
//    }
//
//    /**
//     * 获取分页数量
//     */
//    fun getPageSize(): Int {
//        return mAllAppList.size / 24 + 1
//    }
//
//    /**
//     * 安装
//     */
//    fun isInstallApp(): Boolean {
//        return true
//    }
//
//    /**
//     * 启动 App
//     */
//    fun launcherApp(appName: String): Boolean {
//        if (mAllAppList.isNotEmpty()) {
//            mAllAppList.forEach {
//                if (it.appName == appName) {
//                    intentApp(it.packageName)
//                    return true
//                }
//
//            }
//        }
//        return false
//    }
//
//    /**
//     * 卸载 App
//     */
//    fun uninstallApp(appName: String): Boolean {
//        if (mAllAppList.size > 0) {
//            mAllAppList.forEach {
//                if (it.appName == appName) {
//                    intentUnInstallApp(it.packageName)
//                    return true
//                }
//            }
//        }
//        return false
//    }
//
//    /**
//     * 启动 App
//     */
//    fun intentApp(packageName: String) {
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
//        intent?.let {
//            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            mContext.startActivity(it)
//        }
//    }
//
//    /**
//     * 跳转应用市场
//     */
//    fun launcherAppStore(appName: String): Boolean {
//        return false
//    }
//
//    /**
//     * 启动系统卸载 App 程序
//     */
//    fun intentUnInstallApp(packageName: String) {
//        val intent = Intent(Intent.ACTION_DELETE)
//        intent.data = Uri.parse("package:$packageName")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        mContext.startActivity(intent)
//    }
//
//    /**
//     * 获取非系统应用
//     */
//    fun getNotSystemApp(): List<AppData> {
//        return mAllAppList.filter { !it.isSystemApp }
//    }
//
//    /**
//     * 跳转应用商店
//     */
//    fun intentAppStore(packageName: String, markPackageName: String) {
//        val uri = Uri.parse("marker://details?id=$packageName")
//        val intent = Intent(Intent(Intent.ACTION_VIEW, uri))
//        intent.setPackage(markPackageName)
//        mContext.startActivity(intent)
//    }
//}