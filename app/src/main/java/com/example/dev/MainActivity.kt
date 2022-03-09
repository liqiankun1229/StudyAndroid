package com.example.dev

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.dev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var name: String? = null
    private lateinit var user: UserInfoBean
    private lateinit var tv: TextView

    fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        viewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
        viewModel.nameLiveData.observeForever {
            viewBinding.tv.text = it
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }


//        tv = viewBinding.tv
//        setContentView(viewBinding.root)
        initListener()
//        setContent {  }
    }

    var mediaPlayer: MediaPlayer? = null

    @SuppressLint("ServiceCast", "BlockedPrivateApi")
    private fun initListener() {
        viewBinding.btn.setOnClickListener {
            viewBinding.tv.text = user.name
//            startActivity(Intent(this, ComActivity::class.java))
//            LoginActivity.start(this)
        }
        viewBinding.btnSync.setOnClickListener {
            Thread {
                Toast.makeText(this@MainActivity, "123", Toast.LENGTH_SHORT).show()
            }.start()
//            runOnUiThread {
//                Toast.makeText(this@MainActivity, "456", Toast.LENGTH_SHORT).show()
//            }
        }
        viewBinding.btnVideo.setOnClickListener {
            VideoActivity.start(this)
        }
        viewBinding.btnLogin.setOnClickListener {
//            LoginActivity.start(this)
//            // 震动 1 秒
//            var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//            if (vibrator.hasVibrator()) {
//                vibrator.vibrate(1000)
//            }
            audioManager?.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        viewBinding.btnNormal.setOnClickListener {
            audioManager?.ringerMode = AudioManager.RINGER_MODE_NORMAL // 正常模式
        }
        viewBinding.btnVibrate.setOnClickListener {
            audioManager?.ringerMode = AudioManager.RINGER_MODE_VIBRATE // 震动模式
        }
        viewBinding.btnSilent.setOnClickListener {
            // Not allowed to change Do Not Disturb state
            audioManager?.ringerMode = AudioManager.RINGER_MODE_SILENT // 静音模式
        }
        viewBinding.btnRingerExternal.setOnClickListener {
            // Not allowed to change Do Not Disturb state
            audioManager?.let {
//                val cls = Class.forName("com.android.server.audio.AudioService")
//                val method = cls.getDeclaredMethod("getService")
//                // 设置访问权限
//                method.isAccessible = true
//                val mode = method.invoke(cls.newInstance())
//
//                Log.d(TAG, "initListener: ${(it.ringerMode)}")
//                Log.d(TAG, "initListener: ${(mode)}")
//                Log.d(TAG, "initListener: ${(it as AudioService)}")
            }
        }

        viewBinding.btnPlayH.setOnClickListener {
            // 横屏测试
//            VideoPlayerActivity.start(
//                this,
//                "https://api.bilibili.com/x/player/hls/master.m3u8?aid=464591797&cid=454462538&device_type=0&dolby=0&fnval=0&fnver=0&force_host=2&platform=&qn=16&qn_category=0&request_type=0",
//                "横屏"
//            )
        }
        viewBinding.btnPlayV.setOnClickListener {
            // 竖屏测试
//            VideoPlayerActivity.start(
//                this,
//                "https://api.bilibili.com/x/player/hls/master.m3u8?aid=419335354&cid=375155406&device_type=0&dolby=0&fnval=0&fnver=0&force_host=2&platform=&qn=16&qn_category=0&request_type=0",
//                "竖屏"
//            )
        }
        viewBinding.iv.setOnClickListener {
            viewModel.loadData("567")
//            var gUrl = GlideUrl(
//                "http://yuhanghlw.edcall.cn/idm/sys/file/download/2469", LazyHeaders.Builder()
////                    .addHeader("Content-Type", "application/octet-stream;charset=UTF-8")
////                    .addHeader("Connection", "keep-alive")
////                    .addHeader("Accept", "*/*")
////                    .addHeader("Accept-Encoding","gzip, deflate")
////                    .addHeader("Content-Disposition", "attachment; filename=head.jpeg")
//                    .build()
//            )
//            Glide.with(this)
//                .load("https://yuhanghlw.edcall.cn/idm/sys/file/download/2469")
//                .into(viewBinding.iv)

            try {
//                var cls = Class.forName("android.app.ActivityTaskManager")
//                var method = cls.getDeclaredMethod("getService")
//                method.isAccessible = true
//                var iActivityTaskManager = ActivityTaskManager.getService()
//                Log.d(TAG, "initListener: ${iActivityTaskManager.toString()}")
//                var mets = cls.methods
//                mets.forEach {
//                    Log.d(TAG, "initListener: $it")
//                }
//                var ms = cls.declaredMethods
//                ms.forEach {
//                    Log.d(TAG, "dinitListener: $it")
//                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    var audioManager: AudioManager? = null


    var doPermissionFun = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

    }

}