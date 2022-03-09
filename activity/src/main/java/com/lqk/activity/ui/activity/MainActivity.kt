package com.lqk.activity.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.library.config.UpdateConfiguration
import com.library.manager.DownloadManager
import com.lqk.activity.R
import com.lqk.activity.bean.UpDateApk
import com.lqk.activity.common.Constacts
import com.lqk.activity.data.DownloadService
import com.lqk.activity.ui.fragment.ParentFragment
import com.lqk.activity.utils.ApkUtil
import com.lqk.activity.utils.SharedPreferencesUtil
import com.lqk.activity.utils.UpdateApkUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 学习 Activity 的生命周期
 * 通过实例，打印出 Activity 的过程
 * 横竖屏切换
 * 知识点：
 * -> Activity 的生命周期
 * -> Activity 的启动模式
 * -> Activity 之间的传值
 * -> Activity 与 Fragment 之间的传值
 * ->
 *
 *
 * Activity 之间的传值
 * 1. Bundle
 * 2. 类静态变量
 * 3. 全局变量
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var loginReceiver: LoginReceiver? = null
    private var imgGlide: ImageView? = null
    internal var file: File? = null
    private var isUpdate = true
    private var isDownload = true
    private var textView: TextView? = null
    private var downloadBinder: DownloadService.DownloadBinder? = null

    // 服务绑定
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            downloadBinder = service as DownloadService.DownloadBinder
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private var updateApkUtil: UpdateApkUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        var s = Runtime.getRuntime().exec("su")
//        s.waitFor()
//        synchronized()

        EventBus.getDefault().register(this)

        Log.d(TAG, "onCreate: ")
        textView = findViewById(R.id.activity_demo)
        imgGlide = findViewById(R.id.img_glide)
        imgGlide!!.setOnClickListener {
            Glide.with(this@MainActivity)
                    .load("https://lqk-im-talker.oss-cn-shanghai.aliyuncs.com/head_image.jpg")
                    .placeholder(R.drawable.ic_place_holder)
                    .fitCenter()
                    .centerCrop()
                    .into(imgGlide!!)
            textView!!.text = SharedPreferencesUtil.getStringData(this@MainActivity, "version")
        }

        val intent = Intent(this, DownloadService::class.java)
        startService(intent) // 启动服务
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE) // 绑定服务
        // 获取权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        //        startActivityForResult();
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ParentFragment())
                .commit()
        iv_button.setMBackgroundColor(Color.RED)

        val texts = arrayOf("确认身份信息", "确认入住信息", "选择房型", "支付押金", "完成入住")
        sv.setDotCount(5)
        sv.setDescription(texts)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        if (updateApkUtil != null) {
            updateApkUtil!!.resume()
        }
        Log.d(TAG, "onResume: ")
        if (isUpdate && !isDownload) {
            AlertDialog.Builder(this)
                    .setTitle("更新")
                    .setMessage("有新的版本，是否下载更新")
                    .setPositiveButton("下载") { dialog, _ ->
                        if (downloadBinder != null) {
                            // downloadBinder.startDownload("http://192.168.137.1:8080/apkFile/Test.apk");
                            // downloadBinder.startDownload("http://192.168.137.1:8080/apkFile/activitydemo-release-unsigned.apk");
                            // String url = "http://192.168.137.1:8080/apkFile/activitydemo-debug.apk";
                            val url = "http://imtt.dd.qq.com/16891/CA693EC5A79330C80CE6230B8818C7D1.apk?fsname=com.hfi.hangzhoubanshi_1.2.1_6.apk&csr=1bbd"
                            val fileName = url.substring(url.lastIndexOf("/"))
                            // Constacts.INSTANCE.setApkName(fileName);
                            // if (SharedPreferencesUtil.INSTANCE.getStringData(MainActivity.this, "version").equals(fileName)) {
                            //     dialog.dismiss();
                            //     Log.d(TAG, "不用更新");
                            //     return;
                            // }
                            SharedPreferencesUtil.saveString(this@MainActivity, "version", fileName)
                            downloadBinder!!.startDownload(url)
                            isDownload = true
                        } else {
                            Toast.makeText(applicationContext, "下载出错", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("以后再说") { dialog, _ -> dialog.dismiss() }
                    .setCancelable(false).create().show()
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(upDateApk: UpDateApk) {
        Log.d(TAG, "$upDateApk")
        AlertDialog.Builder(this)
                .setTitle("升级")
                .setMessage("下载完成是否安装？")
                .setPositiveButton("安装") { dialog, _ ->
                    ApkUtil.installApk(applicationContext,
                            Constacts.apkFilePath + "/" + Constacts.apkName)

                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }.setCancelable(false).create().show()
    }

//    /**
//     * 异常终止
//     * 关闭时 对数据进行 存储
//     *
//     * @param outState 状态信息
//     */
//    override fun onSaveInstanceState(outState: Bundle) {
//        val teView = 0
//        Log.d(TAG, "$teView")
//        super.onSaveInstanceState(outState)
//    }
//
//    /**
//     * 异常终止 重新启动
//     * 重启时 对保存的数据进行恢复
//     *
//     * @param savedInstanceState 保存的数据
//     */
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        val teView = 0
//        Log.d(TAG, "$teView")
//        super.onRestoreInstanceState(savedInstanceState)
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        val teView = 0
        Log.d(TAG, "$teView")
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        val teView = 0
        Log.d(TAG, "$teView")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onClick(v: View) {
        val intent: Intent
        if (downloadBinder == null) {
            return
        }
        when (v.id) {
            R.id.activity_demo -> {
                Toast.makeText(this, "Activity 的两种启动方式 - 未更新", Toast.LENGTH_SHORT).show()
            }
            R.id.explicit_activity -> {
                // 显式启动 Activity：在构造函数中明确指出了要启动的 Activity
                val secondIntent = Intent(this@MainActivity, SecondActivity::class.java)
                // 创建 Bundle 对象存储 需要传递的数据 数据类型为基本类型的数据 或者序列化过的数据
                val secondBundle = Bundle()
                secondBundle.putString("name", "LQK")
                secondIntent.putExtras(secondBundle)
                startActivity(secondIntent)
                // putExtra("Key", Object) 实际也是 将数据存放在 Bundle 对象中
                //                startActivity((secondIntent).putExtra("startupMode", "explicit"));
//                finish()
            }

            R.id.implicit_activity -> {
                // 隐式启动 Activity：在构造函数中用xml文件中注册的 Action 的 name 属性启动
                startActivity(Intent("second").putExtra("startupMode", "implicit"))
            }
            R.id.btn_open_http -> {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://www.baidu.com")
                startActivity(intent)
            }
            R.id.btn_call -> {
                intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:10086")
                startActivity(intent)
            }
            R.id.btn_register_receiver -> {
                val intentFilter = IntentFilter()
                intentFilter.addAction("com.lqk.activitydome.login")
                loginReceiver = LoginReceiver()
                registerReceiver(loginReceiver, intentFilter)
                Log.d(TAG, "onClicked: " + "注册广播")
            }
            R.id.btn_send_receiver -> sendBroadcast(Intent("com.lqk.activity.login"))
            R.id.btn_notification -> startActivity(Intent(this, NotificationActivity::class.java))
            R.id.btn_main3 -> startActivity(Intent(this, Main2Activity::class.java))
            R.id.btn_service_activity -> startActivity(Intent(this, ServiceActivity::class.java))
            R.id.btn_callback_service -> startActivity(Intent(this, ServiceCallbackActivity::class.java))
            R.id.btn_aidl -> startActivity(Intent(this, MyAidlServiceActivity::class.java))
            R.id.btn_download_start -> {
                //                String url = "http://192.168.0.103:8080/apkFile/Test.apk";
                //                String url = "http://192.168.0.103:8080/apkFile/version.txt";
                //                String url = "http://192.168.137.1:8080/apkFile/version.txt";
                //                String url = "http://192.168.137.1:8080/apkFile/Test.apk";
                val url = "https://www.pgyer.com/app/install/65bbbb8882a97fbe7b994943ec8bd600"
                //                String url = "http://192.168.137.1:8080/apkFile/CentOS.iso";
                Thread {
                    run {
                        downloadBinder!!.startDownload(url)
                    }
                }.start()
            }
            R.id.btn_download_paused -> downloadBinder!!.pauseDownload()
            R.id.btn_download_cancel -> downloadBinder!!.cancelDownload()
            R.id.btn_update_apk -> ApkUtil.installApk(applicationContext, Constacts.apkFilePath + "/" + Constacts.apkName)
            R.id.btn_handle -> startActivity(Intent(this, HandlerActivity::class.java))
            R.id.btn_up -> {
                Log.d("TAG", "更新")
                updateApkUtil = UpdateApkUtil(this)
                updateApkUtil!!.setUpdateListener(object : UpdateApkUtil.OnUpdateListener {
                    override fun update(currentByte: Int, totalByte: Int) {
                        Log.d("下载进度", currentByte.toString() + "" + totalByte)
                    }
                })
                updateApkUtil!!.downloadApk("http://imtt.dd.qq.com/16891/CA693EC5A79330C80CE6230B8818C7D1.apk?fsname=com.hfi.hangzhoubanshi_1.2.1_6.apk&csr=1bbd",
                        "更新 APP", "新功能更新")
            }
            R.id.btn_lib -> {
                val downloadManager = DownloadManager.getInstance(this)
                val config = UpdateConfiguration()
                config.isBreakpointDownload = true
                downloadManager
                        .setApkName("杭州办事.apk")
                        .setDownloadPath(this.externalCacheDir?.path ?: cacheDir.absolutePath)
                        .setConfiguration(config)
                        .setApkUrl("https://www.pgyer.com/app/install/65bbbb8882a97fbe7b994943ec8bd600")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setShowNewerToast(true)
                        .download()
            }
            R.id.btn_view -> {
                startActivity(Intent(this, CustomActivity::class.java))
            }
            R.id.btn_mv_vm -> {
                startActivity(Intent(this, MVVMActivity::class.java))
                this.overridePendingTransition(R.anim.anim_activity_open, R.anim.anim_activity_close)
            }
            R.id.btn_butter -> {
//                toast("1111")
//                startActivity(Intent(this,MainActivity::class.java))
            }
            R.id.iv_button -> {
//                toast("#000000")
                iv_button.setMBackgroundColor(Color.parseColor("#000000"))
            }
            else -> {
            }
        }//                Intent receiver = new Intent();
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拒绝相关下载权限， 无法进行下载操作", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        super.onPause()
        if (updateApkUtil != null) {
            updateApkUtil!!.onPause()
        }
        if (loginReceiver != null) {
            unregisterReceiver(loginReceiver)
            loginReceiver = null
        }
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unbindService(serviceConnection)
    }

    internal inner class LoginReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive: " + "登录")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        when (requestCode) {
            SecondActivity.CODE -> {
                if (data != null && data.extras != null) {
                    Log.d(TAG, "onActivityResult: ${data!!.extras!!["dataResult"] ?: "返回"}")
                } else {
                    Log.d(TAG, "onActivityResult: }")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        private val TAG = "生命周期 MainActivity："

        @Throws(Exception::class)
        fun getFileFromServer(path: String, pd: ProgressDialog): File? {
            //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val url = URL(path)
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 5000
                //获取到文件的大小
                pd.max = conn.contentLength
                val inputStream = conn.inputStream
                val file = File(Environment.getExternalStorageDirectory(), "updata.apk")
                val fos = FileOutputStream(file)
                val bis = BufferedInputStream(inputStream)
                val buffer = ByteArray(1024)
                var total = 0
                var len: Int = bis.read(buffer)
                while (len != -1) {
                    fos.write(buffer, 0, len)
                    total += len
                    //获取当前下载量
                    pd.progress = total
                    len = bis.read(buffer)
                }
                fos.close()
                bis.close()
                inputStream.close()
                return file
            } else {
                return null
            }
        }
    }
}
