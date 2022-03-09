package com.lqk.activity.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lqk.activity.R
import com.lqk.activity.service.FirstService
import com.lqk.activity.utils.DataUtil
import com.lqk.activity.utils.ServiceStatusUtil
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "ServiceActivity"
    }

    private var mIntent: Intent? = null
    private var mServiceBinder: FirstService.MyBinder? = null

    private var connection = object : ServiceConnection {
        /**
         * service 解绑成功
         */
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "解除绑定服务成功")
            btn_unbind.isEnabled = false
        }

        /**
         * service 绑定成功
         * 获取到 Binder 对象
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mServiceBinder = service as FirstService.MyBinder
            Log.d(TAG, "返回 Binder 绑定服务成功")
            btn_unbind.isEnabled = true
        }
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        when (v.id) {
            R.id.btn_bind -> {
                if (mServiceBinder != null) {
                    mServiceBinder!!.setMyData("启动 服务")
                    Log.d(TAG, mServiceBinder!!.getServiceString())
                    Log.d(TAG, mServiceBinder!!.getData2())
                } else {
                    bindService(mIntent, connection, Context.BIND_AUTO_CREATE)
                    Toast.makeText(this, "Binder is null", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_unbind -> {
                if (mServiceBinder != null) {
                    unbindService(connection)
                    mServiceBinder = null
                }
            }
            R.id.btn_start_service -> {
                startService(mIntent)
            }
            R.id.btn_stop_service -> {
                stopService(mIntent)
            }
            R.id.btn_get_data -> {
                Toast.makeText(this, DataUtil.name, Toast.LENGTH_SHORT).show()
            }
            R.id.btn_get_service_status -> {
                Toast.makeText(this,
                        "${ServiceStatusUtil.isServiceRunning(this, "com.lqk.activity.service.FirstService")}",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        mIntent = Intent(this, FirstService::class.java)
        // 使用 Intent 向 Service 传值
        mIntent!!.putExtra("data", "来自 Activity 的 data")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mServiceBinder != null) {
            unbindService(connection)
            mServiceBinder = null
        }
        stopService(mIntent)
    }
}
