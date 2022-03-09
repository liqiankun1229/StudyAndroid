package com.lqk.web.ui.activity

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lqk.network.NetWorkCallback
import com.lqk.network.OkHttpUtil
import com.lqk.web.ui.bean.ResponsePackage

/**
 * @author LQK
 * @time 2022/3/2 14:11
 * @remark
 */
class CustomWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    companion object {
        const val TAG = "CustomWorker"
        const val Name = "CustomWorker"
    }

    override fun doWork(): Result {
        Thread.sleep(3000)
        // 缓存目录
        Log.d(TAG, "doWork: ${applicationContext.cacheDir}")
        // 网络请求
        OkHttpUtil.instance.doPost(
//                "$BASE_URL:5000/zip/info",
            "${LocalPackageActivity.BASE_URL}:5000/zip/packages",
            hashMapOf(),
            ResponsePackage::class.java,
            object : NetWorkCallback<ResponsePackage> {
                override fun onSucceed(data: ResponsePackage) {
                    // 版本校验 - 包校验 - 下载包 - 解压到对应目录
                    if (data.code == 200) {
                        data.data?.let {
                            it.map { versionInfo ->
                                Log.d(LocalPackageActivity.TAG, "离线包列表 -: ${versionInfo.appId}")
                            }
                        }
                    }
                }

                override fun onFailed(msg: String) {

                }
            })
        Log.d(TAG, "doWork: ")
        return Result.success()
    }
}