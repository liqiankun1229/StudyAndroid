package com.lqk.butter.ui.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.lqk.annotations.MyKotlinBindView
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import com.lqk.butter.common.Ext
import com.lqk.butter.common.Variable
import com.lqk.butter.common.onClick
import com.lqk.butter.compiler.DoBinder
import com.lqk.utils.RecentListUtil
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * 闪频页
 */
@SuppressLint("HandlerLeak")
class SplashActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        DoBinder.bind(this)
        iv.setOnClickListener {
            Toast.makeText(this, "浮动", Toast.LENGTH_SHORT).show()
        }
        // 照相机
        btn_camera.onClick {
            doPermission(PERMISSION_CAMERA) {
                openZxingActivity()
            }
        }
        // 日历 -> 添加日程
        btn_calendar.setOnClickListener {
            doCalendar()
        }
        // 联系人
        btn_contacts.setOnClickListener {
            doPermission(PERMISSION_CONTACTS) {
                getContants()
            }
        }
        // 安装位置来源应用
        btn_install.setOnClickListener {
            doPermission(PERMISSION_INSTALL) {
                doInstallAPK()
            }
        }
        // 定位权限
        btn_location.setOnClickListener {
            doPermission(PERMISSION_LOCATION) {
                doLocation()
            }
        }
        // 打电话
        btn_call.setOnClickListener {
            doPermission(PERMISSION_PHONE) {
                val phoneNumber = "10086"
                val phoneIntent = Intent(Intent.ACTION_CALL)
                val userPhone = Uri.parse("tel:$phoneNumber")
                phoneIntent.data = userPhone
                startActivity(phoneIntent)
            }
        }
        tv.setOnClickListener {

            RecentListUtil.getRecentList(applicationContext)
            RecentListUtil.getRecent(applicationContext)
            // 自定义滑动指示器
            startActivity(Intent(this, MainActivity::class.java))
        }
        btn_download.setOnClickListener {
            ARouter.getInstance().build("/butterknife/main").navigation()
        }
    }

    /**
     * 日程
     */
    private fun doCalendar() {
        Toast.makeText(this, "添加日程", Toast.LENGTH_SHORT).show()
    }

    private fun doInstallAPK() {
        Toast.makeText(this, "开始安装位置来源APK", Toast.LENGTH_SHORT).show()
    }

    private fun doLocation() {
        Toast.makeText(this, "开始定位", Toast.LENGTH_SHORT).show()
    }

    private fun openZxingActivity() {
        Toast.makeText(this, "开启摄像机", Toast.LENGTH_SHORT).show()
    }

    @MyKotlinBindView(myId = R.id.tv)
    lateinit var textView: TextView

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        return super.dispatchTouchEvent(ev)
    }

    fun goMain(view: View) {
//        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//        finish()
    }

    fun getContants() {
        Toast.makeText(this, "获取联系人列表", Toast.LENGTH_SHORT).show()
    }

    /**
     * 打印联系人列表
     */
    @SuppressLint("Range")
    fun logContants() {
        var cursor: Cursor? = null
        val contentResolver: ContentResolver = this.contentResolver
        try {
            cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    Log.d("CONTACTS", "姓名: $name 电话: $phoneNumber")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    override fun onResume() {
        super.onResume()

        val iconName = ".ui.activity.SplashActivity.Test1"
        if (Ext.inIconMap(iconName)) {
            Variable.oldIcon = componentName
            Variable.newIcon = ComponentName(this, "$packageName$iconName")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        textView.text = "succeed"
    }

    override fun permissionInstall() {
        super.permissionInstall()
//        doInstallAPK()
    }

    override fun finish() {
        super.finish()
    }
}
