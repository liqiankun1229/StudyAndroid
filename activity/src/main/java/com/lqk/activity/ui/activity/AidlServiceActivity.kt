package com.lqk.activity.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lqk.activity.IMyAidlInterface
import com.lqk.activity.R
import com.lqk.activity.bean.Person
import com.lqk.activity.service.server.MyAidlService

/**
 * AIDL 跨线程通讯
 */
class AidlServiceActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "MyAidlServiceActivity"
    }

    private lateinit var mIntent: Intent

    private var mAidl: IMyAidlInterface? = null

    private lateinit var mPersonList: MutableList<Person>

    interface IAidlListener {
        fun doAction(string: String)
    }

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mAidl = null
            Log.d(TAG, "---- onServiceDisconnected() 断开链接 ----")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mAidl = IMyAidlInterface.Stub.asInterface(service)
            Log.d(TAG, "---- onServiceConnected() 连接成功----")
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_bind_aidl -> {
                if (mAidl == null) {
                    // 绑定服务 绑定结果会在 serviceConnection 中回调
                    bindService(mIntent, serviceConnection, Context.BIND_AUTO_CREATE)
                } else {
                    Toast.makeText(this, "已经绑定", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.btn_unbind_aidl -> {
                if (mAidl != null) {
                    unbindService(serviceConnection)
                } else {
                    Toast.makeText(this, "未绑定", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_add -> {
                Thread {
                    try {
                        mAidl?.addPerson(Person("LQK", 22, "boy"))
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }.start()
            }
            R.id.btn_get_list -> {
                Thread {
                    try {
                        mPersonList = mAidl!!.personList
                        Log.d(TAG, "${mPersonList.size}")
                        mPersonList.forEach {
                            Log.d(TAG, "${it.name}:${it.age}:${it.sex}")
                        }
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }.start()
            }
            R.id.btn_byte -> {
                Thread {
                    try {
                        mAidl?.addBytes(ByteArray(1 * 1024 * 1024))
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl_service)

        mIntent = Intent(this, MyAidlService::class.java)
        mPersonList = ArrayList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mAidl != null) {
            unbindService(serviceConnection)
            mAidl = null
        }
    }
}
