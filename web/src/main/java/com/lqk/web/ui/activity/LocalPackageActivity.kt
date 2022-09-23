package com.lqk.web.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.lqk.network.NetWorkCallback
import com.lqk.network.NetWorkOperation
import com.lqk.network.OkHttpUtil
import com.lqk.web.R
import com.lqk.web.ToastUtil
import com.lqk.web.databinding.ActivityLocalPackageBinding
import com.lqk.web.ext.showDialog
import com.lqk.web.ui.PackageAdapter
import com.lqk.web.ui.bean.BaseResponse
import com.lqk.web.ui.bean.ResponsePackage
import com.lqk.web.ui.bean.TestBean
import com.lqk.web.ui.bean.VersionInfo
import com.lqk.web.ui.widget.web.WebActivity
import com.lqk.web.ui.widget.web.local.PackageUtils
import com.lqk.web.ui.widget.web.local.cachePackage
import com.lqk.web.ui.widget.web.local.searchPackage
import com.lqk.web.utils.MMKVUtils
import com.unionpay.UPPayAssistEx
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile
import kotlin.concurrent.thread

class LocalPackageActivity : BaseVBActivity<ActivityLocalPackageBinding>() {

    companion object {
        const val TAG = "LocalPackageActivity"

//        const val BASE_URL = "http://10.100.156.146"
        const val BASE_URL = "http://192.168.31.71"
        const val packageAssets = "file:///android_asset"
    }

    override fun initLayoutId(): Int {
        return R.layout.activity_local_package
    }

    override fun initViewBinding(): ActivityLocalPackageBinding {
        return ActivityLocalPackageBinding.inflate(layoutInflater)
    }


    private var listPackage = mutableListOf<VersionInfo>()
    private lateinit var mAdapter: PackageAdapter
    private lateinit var name: String
    override fun initView() {
        super.initView()
        WebView.setWebContentsDebuggingEnabled(true)
        Log.d(TAG, "initView: ${Thread.currentThread().name}")
        mAdapter = PackageAdapter(listPackage)
        viewBinding.rcv.layoutManager = LinearLayoutManager(this)
        viewBinding.rcv.adapter = mAdapter
    }

    override fun initListener() {
        super.initListener()
        mAdapter.initListener(object : PackageAdapter.Listener {
            override fun openPackage(versionInfo: VersionInfo) {
                // 下载离线包链接
                when (versionInfo.packageType) {
                    -1 -> {
                        showDialog("提示",
                            "${versionInfo.appId} 预置离线包",
                            "取消",
                            "打开",
                            {
                                Toast.makeText(
                                    this@LocalPackageActivity,
                                    "${versionInfo.appId} 用户取消",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                WebActivity.start(
                                    this@LocalPackageActivity,
                                    "$packageAssets/${versionInfo.appId}/${versionInfo.home}"
                                )
                            })
                    }
                    0 -> {
                        if (searchPackage(versionInfo)) {
                            // 包版本判断 - 点击的是在线离线包信息
                            if (!PackageUtils.checkPackageVersion(versionInfo)) {
                                openLocal(versionInfo)
                            } else {
                                showDialog("提示",
                                    "${versionInfo.appId} 离线包有更新 版本:${versionInfo.versionName}",
                                    "取消",
                                    "更新",
                                    {
                                        // 不下载 打开在线
                                        showDialog("提示",
                                            "${versionInfo.appId} 离线包有更新 版本:${versionInfo.versionName} 未下载",
                                            "取消",
                                            "打开在线地址",
                                            {
                                                Toast.makeText(
                                                    this@LocalPackageActivity,
                                                    "${versionInfo.appId} 离线包未更新-且拒绝打开在线地址",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            {
                                                WebActivity.start(
                                                    this@LocalPackageActivity,
                                                    versionInfo.onlineUrl
                                                )
                                            })
                                    },
                                    {
                                        doDownload(versionInfo)
                                    })
                            }
                        } else {
                            showDialog("提示",
                                "${versionInfo.appId} 离线包未下载",
                                "取消",
                                "下载",
                                {
                                    Toast.makeText(
                                        this@LocalPackageActivity,
                                        "${versionInfo.appId} 离线包未下载",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // 不下载 打开在线
                                    showDialog("提示",
                                        "${versionInfo.appId} 离线包有更新 版本:${versionInfo.versionName} 未下载",
                                        "取消",
                                        "打开在线地址",
                                        {
                                            Toast.makeText(
                                                this@LocalPackageActivity,
                                                "${versionInfo.appId} 离线包未更新-且拒绝打开在线地址",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        {
                                            WebActivity.start(
                                                this@LocalPackageActivity,
                                                versionInfo.onlineUrl
                                            )
                                        })
                                },
                                {
                                    doDownload(versionInfo)
                                })
                        }
                    }
                    else -> {
                        showDialog("提示",
                            "${versionInfo.appId} 没有离线包",
                            "取消",
                            "打开在线地址",
                            {
                                Toast.makeText(
                                    this@LocalPackageActivity,
                                    "${versionInfo.appId} 用户取消",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                WebActivity.start(this@LocalPackageActivity, versionInfo.onlineUrl)
                            })
                    }
                }
            }
        })

        // 制造异常
        viewBinding.btnError.setOnClickListener {
            val i = 1 / 0
        }
        // 子线程异常
        viewBinding.btnErrorThread.setOnClickListener {
            Thread {
                val i = 1 / 0
            }.start()
        }
        viewBinding.btnErrorNull.setOnClickListener {
//            name.equals("123")
//            var json = "{\"name\":\"123\",\"age\":null}"
            var json = "{\"name\":\"123\"}"
            var s = Gson().fromJson(json, TestBean::class.java)
            if (s.cls.equals("123")) {

            }
        }
        viewBinding.btnErrorToast.setOnClickListener {
            ToastUtil.toast = Toast.makeText(this, "1234", Toast.LENGTH_SHORT)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        viewBinding.btnPay.setOnClickListener {
            UPPayAssistEx.startPay(this, null, null, "786493409505075790311", "01")
        }

        viewBinding.btnUnzip.setOnClickListener {
            mVersionInfo?.let {
                doUnZipFile(it)
            }
        }
        viewBinding.btnDownload.setOnClickListener {
            this.mVersionInfo?.let {
                doDownload(it)
            }
        }
        viewBinding.btnClear.setOnClickListener {

        }
        viewBinding.btnCheck.setOnClickListener {
            val requestData = hashMapOf<String, Any>()
            if (viewBinding.etScan.text.toString().isNotEmpty()) {
                requestData["appId"] = viewBinding.etScan.text.toString()
            }
            // 请求
            OkHttpUtil.instance.doPost(
//                "$BASE_URL:5000/zip/info",
                "$BASE_URL:5000/zip/infoById",
                requestData,
                BaseResponse::class.java,
                object : NetWorkCallback<BaseResponse> {
                    override fun onSucceed(data: BaseResponse) {
                        // 版本校验 - 包校验 - 下载包 - 解压到对应目录
                        if (data.code == 200) {
                            data.data?.let {
                                checkVersion(it)
                            }
                        } else {
                            Toast.makeText(this@LocalPackageActivity, data.msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailed(msg: String) {

                    }
                })
        }
        viewBinding.btnOpen.setOnClickListener {
            mVersionInfo?.let {
//                val path = cacheDir.absolutePath
//                val htmlPath = "$path/${it.name}/${it.appId}/index.html"
//                WebActivity.startLocal(this, "file:$htmlPath")
                openLocal(versionInfo = it)
            }
        }
        viewBinding.btnOpenUrl.setOnClickListener {
            WebActivity.start(this, "$BASE_URL:8081")
        }
        viewBinding.btnOpenAssets.setOnClickListener {
            // 启动 assets 预置 离线包 -> file:///android_asset/$appId/$home
            WebActivity.start(this, "file:///android_asset/package/index.html")
        }
        viewBinding.btnUpload.setOnClickListener {
            showPackages(mutableListOf())
            Thread.sleep(500)
            packages()
        }
        viewBinding.btnLoadLocal.setOnClickListener {
            showPackages(PackageUtils.localPackage())
        }
        viewBinding.btnShowLocal.setOnClickListener {
            viewBinding.tvResult.text = MMKVUtils.gainString(PackageUtils.KEY)
        }
        viewBinding.btnStartWork.setOnClickListener {
            Log.d(TAG, "initListener: CustomWorker")
//            WorkManager.getInstance(applicationContext)
//                .beginUniqueWork(
//                    CustomWorker.Name,
//                    ExistingWorkPolicy.REPLACE,
//                    OneTimeWorkRequest.from(CustomWorker::class.java)
//                )
//                .enqueue()
            OkHttpUtil.instance.doPost(
//                "$BASE_URL:5000/zip/info",
                "$BASE_URL:5000/zip/packages",
                hashMapOf(),
                ResponsePackage::class.java,
                object : NetWorkCallback<ResponsePackage> {
                    override fun onSucceed(data: ResponsePackage) {
                        // 版本校验 - 包校验 - 下载包 - 解压到对应目录
                        if (data.code == 200) {
                            data.data?.let {
//                                checkVersion(it)
                                it.forEach { v ->
                                    Log.d(TAG, "onSucceed: ${v.home}: ${v.url}")
                                }
                            }
                        } else {
                            Toast.makeText(this@LocalPackageActivity, data.msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailed(msg: String) {

                    }
                })
        }
    }

    private fun downloadById(versionInfo: VersionInfo) {
        val requestData = hashMapOf<String, Any>()
        requestData["appId"] = versionInfo.appId
        // 请求
        OkHttpUtil.instance.doPost(
//                "$BASE_URL:5000/zip/info",
            "$BASE_URL:5000/zip/infoById",
            requestData,
            BaseResponse::class.java,
            object : NetWorkCallback<BaseResponse> {
                override fun onSucceed(data: BaseResponse) {
                    // 版本校验 - 包校验 - 下载包 - 解压到对应目录
                    if (data.code == 200) {
                        data.data?.let {
                            checkVersion(it)
                        }
                    } else {
                        Toast.makeText(this@LocalPackageActivity, data.msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailed(msg: String) {

                }
            })
    }

    private fun clear() {
        // 离线包缓存目录路径
        val path = cachePackage()


    }

    private fun openLocal(versionInfo: VersionInfo) {
        val path = cachePackage()
        val htmlPath = "$path/${versionInfo.appId}/${versionInfo.home}"
        WebActivity.startLocal(this, "file:$htmlPath")
    }

    private var mVersionInfo: VersionInfo? = null

    private fun doUnZipFile(versionInfo: VersionInfo) {
        thread {
            // 存放压缩包路径, 没有则创建, 保证该文件夹存在
            val path = cacheDir.absolutePath
            val fileDir = "$path/${versionInfo.name}/"
            val dir = File(fileDir)
            dir.mkdirs()
            // 压缩包路径
            val filePath = "$path/${versionInfo.name}/${versionInfo.appId}.zip"
            val file = File(filePath)
            Log.d(TAG, "downVersion: ${versionInfo.url}")
            // 压缩文件已经下载 -> 校验
            if (file.exists()) {
                Log.d(TAG, "doUnZipFile: 开始解压")

                val zipFile = ZipFile(file)
                val zipDir = file.absolutePath.substring(0, file.absolutePath.lastIndexOf('/'))

                Log.d(TAG, "doUnZipFile: $zipDir")
                Log.d(TAG, "doUnZipFile: ${zipFile.name}")

                val e = zipFile.entries()
                while (e.hasMoreElements()) {
                    val zipEntry = e.nextElement()
                    val entryName = zipEntry.name
                    // 文件路径
                    val path = "${zipDir}/$entryName"
                    var zipFileName = entryName.substring(0, entryName.lastIndexOf('/'))
                    Log.d(TAG, "doUnZipFile-: $path")

                    if (zipEntry.isDirectory) {
                        // 创建 文件夹
                        Log.d(TAG, "dir-doUnZipFile: $entryName")
                        val dirs = File(path)
                        if (!dirs.exists()) {
                            dirs.mkdirs()
                        }
                    } else {
                        // 文件
                        Log.d(TAG, "file-doUnZipFile: $entryName")
                        val mFileDir = File(fileDir)
                        if (!mFileDir.exists()) {
                            mFileDir.mkdirs()
                        }
                        val mFile = File("$fileDir/$entryName")

                        val bos = BufferedOutputStream(FileOutputStream(mFile))
                        val bi = BufferedInputStream(zipFile.getInputStream(zipEntry))
                        val readContent = ByteArray(1024)
                        var read = bi.read(readContent)
                        while (read != -1) {
                            bos.write(readContent, 0, read)
                            read = bi.read(readContent)
                        }
                        bos.close()
                    }
                }
                zipFile.close()
                runOnUiThread {
                    Toast.makeText(this, "${versionInfo.appId} 离线包下载完毕", Toast.LENGTH_SHORT).show()
                    checkVersion()
                }
            } else {
                Log.d(TAG, "doUnZipFile: 文件不存在请前往下载")
            }
        }.run()

    }

    private fun checkVersion() {
//        this.mVersionInfo?.let {
//            val path = cachePackage()
//
//            // 压缩文件
//            val file = File("$path/${it.appId}.zip")
//            viewBinding.btnDownload.visibility = if (file.exists()) {
//                View.GONE
//            } else {
//                View.VISIBLE
//            }
//            // 入口
//            val home = File("$path/web/${it.appId}/${it.home}")
//            viewBinding.btnUnzip.visibility = if (file.exists()) {
//                if (home.exists()) {
//                    View.GONE
//                } else {
//                    View.VISIBLE
//                }
//            } else {
//                View.GONE
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkVersion(versionInfo: VersionInfo) {
        this.mVersionInfo = versionInfo

//        val fileDir = cachePackage()
//        val dir = File(fileDir)
//        dir.mkdirs()
//        val filePath = "$fileDir/${versionInfo.appId}.zip"
//        val file = File(filePath)
//        Log.d(TAG, "downVersion: ${versionInfo.url}")
//        viewBinding.btnDownload.visibility = if (file.exists()) {
//            View.GONE
//        } else {
//            View.VISIBLE
//        }
//        viewBinding.btnUnzip.visibility = if (file.exists()) {
//            View.VISIBLE
//        } else {
//            View.GONE
//        }
        // appid -> 应用唯一标识 对应的路径
//        Log.d(TAG, "downVersion: $fileDir")
//        Log.d(TAG, "downVersion: $filePath")
        viewBinding.tvResult.text = """
            appId: ${versionInfo.appId}
            category: ${versionInfo.category},
            name: ${versionInfo.name}
            url: ${versionInfo.url}
            versionCode: ${versionInfo.versionCode}
            versionName: ${versionInfo.versionName}
            home: ${versionInfo.home}
        """.trimIndent()
    }

    private fun doDownload(versionInfo: VersionInfo) {

        val fileDir = cachePackage()
        val dir = File(fileDir)
        dir.mkdirs()
        val filePath = "$fileDir/${versionInfo.appId}.zip"
        Log.d(TAG, "downVersion: ${versionInfo.url}")
        // appid -> 应用唯一标识 对应的路径
        Log.d(TAG, "downVersion: $fileDir")
        Log.d(TAG, "downVersion: $filePath")
        OkHttpUtil.instance.download(
            filePath,
            versionInfo.url,
            hashMapOf(),
            File::class.java,
            object : NetWorkOperation.OnDownLoadListener {
                override fun onSucceed(data: File) {
                    Log.d(TAG, "onSucceed: ${data.path}")
                    Toast.makeText(
                        this@LocalPackageActivity,
                        "离线包${filePath}下载成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    PackageUtils.upZipPackage(versionInfo, data.path)
                    checkVersion()
                }

                override fun onFailed(msg: String) {
                    Log.d(TAG, "onFailed: $msg")
                }

                override fun downloadProcess(process: Int) {
                    Log.d(TAG, "downloadProcess: $process")
                }
            })
    }

    override fun initData() {
        super.initData()
    }

    override fun onResume() {
        super.onResume()
        Log.d("start_time", "attachBaseContext: ${System.currentTimeMillis()}")
        packages()
    }

    // 请求离线包列表
    private fun packages() {
        // 请求
        OkHttpUtil.instance.doPost(
            "$BASE_URL:5000/zip/packages",
            hashMapOf(),
            ResponsePackage::class.java,
            object : NetWorkCallback<ResponsePackage> {
                override fun onSucceed(data: ResponsePackage) {
                    // 版本校验 - 包校验 - 下载包 - 解压到对应目录
                    if (data.code == 200) {
                        data.data?.let {
                            showPackages(it)
                            it.map { versionInfo ->
                                Log.d(TAG, "离线包列表 -: ${versionInfo.appId}")
                            }
                        }
                    } else {
                        Toast.makeText(this@LocalPackageActivity, data.msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailed(msg: String) {
                    Log.d(TAG, "onFailed: $msg")
                    val data = PackageUtils.localPackage()
                    showPackages(data)
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPackages(list: MutableList<VersionInfo>) {
        listPackage.clear()
        listPackage.addAll(list)
        mAdapter.notifyDataSetChanged()
    }


}
