package com.lqk.butter.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.lqk.butter.app.BaseApplication


/**
 * @author LQK
 * @time 2019/2/27 23:00
 * @remark
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        // 权限列表
        // 相机
        const val PERMISSION_CAMERA = 0x000000
        // 拨打电话
        const val PERMISSION_PHONE = 0x000001
        // 麦克风
        const val PERMISSION_MICROPHONE = 0x000002
        // 联系人
        const val PERMISSION_CONTACTS = 0x000003
        // 通话记录
        const val PERMISSION_CALL_LOG = 0x000004
        // 短信
        const val PERMISSION_SMS = 0x000005
        // 日历
        const val PERMISSION_CALENDAR = 0x000006
        // 定位
        const val PERMISSION_LOCATION = 0x000007
        // 存储
        const val PERMISSION_STORAGE = 0x000008
        // 安装应用
        const val PERMISSION_INSTALL = 0x000009
        // 传感器
        const val PERMISSION_SENSORS = 0x000010
        // 下载文件 存储权限
        const val PERMISSION_DOWNLOAD = 0x000011

    }

    // 保存一个空函数
    private var isFunc = false
    var func: () -> Unit = {}

    abstract fun layoutId(): Int

    /**
     * 创建
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    open fun initActivity(){
        BaseApplication.getInstance().addActivity(this)
        setContentView(layoutId())
        initView()
    }

    abstract fun initView()

    /**
     * 销毁
     */
    override fun onDestroy() {
        super.onDestroy()
        BaseApplication.getInstance().removeActivity(this)
    }

    /**
     * 调用需要权限的操作
     */
    fun doPermission(code: Int) {
        if (hasPermission(code)) {
            // 保存闭包函数，权限获取成功后回调
            when (code) {
                PERMISSION_CAMERA -> {
                    onPermissionFunc {
                        permissionCamera()
                    }
                }
                PERMISSION_MICROPHONE -> {
                    onPermissionFunc {
                        permissionRecording()
                    }
                }
                PERMISSION_PHONE -> {
                    onPermissionFunc {
                        permissionCallPhone()
                    }
                }
                PERMISSION_LOCATION -> {
                    onPermissionFunc {
                        permissionLocation()
                    }
                }
                PERMISSION_INSTALL -> {
                    onPermissionFunc {
                        permissionInstall()
                    }
                }
                else -> {
                    Toast.makeText(this, "没有对应权限", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermission(code)
        }
    }

    /**
     * 根据权限标识判断有无权限
     * 根据权限ID -> 生成权限列表 -> 判断有无权限
     */
    private fun hasPermission(permissionCode: Int): Boolean {
        when (permissionCode) {
            PERMISSION_CAMERA -> {
                // 相机
                return hasPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
            }
            PERMISSION_MICROPHONE -> {
                // 麦克风
                return hasPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
            }
            PERMISSION_CONTACTS -> {
                return hasPermission(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS
                )
            }
            PERMISSION_PHONE -> {
                // 打电话
                return hasPermission(Manifest.permission.CALL_PHONE)
            }
            PERMISSION_LOCATION -> {
                // 位置信息
                return hasPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
            }
            PERMISSION_CALL_LOG -> {
                // 通话记录
                return hasPermission(
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.PROCESS_OUTGOING_CALLS)
            }
            PERMISSION_SENSORS -> {
                // 传感器 sdk 20 以上
                return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    hasPermission(
                            Manifest.permission.BODY_SENSORS)
                } else {
                    false
                }
            }
            PERMISSION_SMS -> {
                // 短信
                return hasPermission(
                        Manifest.permission.BROADCAST_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_WAP_PUSH)
            }
            PERMISSION_STORAGE -> {
                // 存储权限
                return hasPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            PERMISSION_CALENDAR -> {
                // 日历
                return hasPermission(
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR)
            }
            PERMISSION_INSTALL -> {
                // 安装应用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return this.packageManager.canRequestPackageInstalls()
                }
                return true
            }
            PERMISSION_DOWNLOAD -> {
                // 存储
                return hasPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else -> {
                return false
            }
        }
    }

    /**
     * 判断有无对应权限 -> 逐一判断
     */
    private fun hasPermission(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 判断权限时 -> 执行第一次
     * 权限请求回调对应的操作
     */
    private fun onPermissionFunc(function: () -> Unit) {
        if (isFunc) {
            // 用户自己传入的函数
            func()
            isFunc = false
        } else {
            // function 函数是已经定义的函数
            function()
        }
    }

    /**
     * 判断授权结果
     */
    private fun checkPermissionSuccess(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 权限回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && checkPermissionSuccess(grantResults)) {
            // 权限获取成功做对应的工作
            when (requestCode) {
                PERMISSION_CAMERA -> {
                    onPermissionFunc {
                        permissionCamera()
                    }
                }
                PERMISSION_PHONE -> {
                    onPermissionFunc {
                        permissionCallPhone()
                    }
                }
                PERMISSION_MICROPHONE -> {
                    onPermissionFunc {
                        permissionRecording()
                    }
                }
                PERMISSION_CONTACTS -> {
                    onPermissionFunc {
                        permissionContacts()
                    }
                }
                PERMISSION_LOCATION -> {
                    onPermissionFunc {
                        permissionLocation()
                    }
                }
                PERMISSION_CALL_LOG -> {
                    onPermissionFunc {

                    }
                }
                PERMISSION_SMS -> {
                    onPermissionFunc {
                        permissionSms()
                    }
                }
                PERMISSION_CALENDAR -> {
                    onPermissionFunc {
                        permissionCalendar()
                    }
                }
                PERMISSION_STORAGE -> {
                    onPermissionFunc {
                        permissionStorage()
                    }
                }
                PERMISSION_INSTALL -> {
                    onPermissionFunc {
                        permissionInstall()
                    }
                }
                PERMISSION_SENSORS -> {
                    onPermissionFunc {
                        permissionSensors()
                    }
                }
                PERMISSION_DOWNLOAD -> {
                    onPermissionFunc {
                        permissionDownload()
                    }
                }
                else -> {
                    Toast.makeText(this, "传入正确的权限值", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // 权限获取失败
            requestPermission(requestCode)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 权限入口, 保存请求权限之后要执行的方法
     * 第一次判断权限,有权限时直接执行该方法/反之申请权限
     *
     */
    fun doPermission(code: Int, action: () -> Unit) {
        this.isFunc = true
        this.func = action
        // 判断 Android 版本
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (hasPermission(code)) {
                // 权限已经授权,保存闭包函数，权限获取成功后回调
                action()
                this.isFunc = false
            } else {
                firstRequestPermission(code)
            }
        } else {
            action()
            this.isFunc = false
        }
    }

    /**
     * 根据自己定义的权限标识请求获取权限
     * 权限封装的权限ID -> 生成请求权限列表
     */
    private fun requestPermission(permissions: Int) {
        when (permissions) {
            PERMISSION_CALENDAR -> {
                requestPermission(permissions,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS)
            }
            PERMISSION_CALL_LOG -> {
                requestPermission(permissions,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.PROCESS_OUTGOING_CALLS)
            }
            PERMISSION_CAMERA -> {
                requestPermission(permissions,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
            }
            PERMISSION_CONTACTS -> {
                requestPermission(permissions,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS)
            }
            PERMISSION_LOCATION -> {
                requestPermission(permissions,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)
            }
            PERMISSION_MICROPHONE -> {
                requestPermission(permissions,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
            }
            PERMISSION_PHONE -> {
                requestPermission(permissions,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_PHONE_STATE,
                        // Manifest.permission.READ_PHONE_NUMBERS,
                        // Manifest.permission.ANSWER_PHONE_CALLS,
                        Manifest.permission.ADD_VOICEMAIL,
                        Manifest.permission.USE_SIP)

            }
            PERMISSION_SENSORS -> {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    requestPermission(permissions,
                            Manifest.permission.BODY_SENSORS)
                }
            }
            PERMISSION_SMS -> {
                requestPermission(permissions,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_WAP_PUSH,
                        Manifest.permission.RECEIVE_MMS)
            }
            PERMISSION_INSTALL -> {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    // 打开允许安装三方程序界面
                    val packageURI = Uri.parse("package:" + this.packageName)
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
                    startActivityForResult(intent, PERMISSION_INSTALL)
                }
            }
            PERMISSION_DOWNLOAD -> {
                requestPermission(permissions,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS)
            }
            else -> {
                Toast.makeText(this, "未知权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 自己传入权限列表获取权限
     * 实际的请求权限操作
     * 逐一判断是都有该权限 -> 保存没有获取到的权限
     */
    private fun firstRequestPermission(requestCode: Int, vararg permissions: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionArray = mutableListOf<String>()
            if (permissions.isNotEmpty()) {
                // 逐一判断权限 -> 提取未获得的权限
                for (permission in permissions) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED
                            || PermissionChecker.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                        // 没有该权限
                        permissionArray.add(permission)
                    }
                }
                // 提取出没有授予的权限 -> 做请求操作
                if (permissionArray.isNotEmpty()) {
                    ActivityCompat.requestPermissions(this, permissionArray.toTypedArray(), requestCode)
                } else {
                    doPermission(requestCode)
                }
            } else {
                Toast.makeText(this, "权限列表不能为空", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 自己传入权限列表获取权限
     * 实际的请求权限操作
     * 逐一判断是都有该权限 -> 保存没有获取到的权限
     */
    private fun requestPermission(requestCode: Int, vararg permissions: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionArray = mutableListOf<String>()
            if (permissions.isNotEmpty()) {
                // 逐一判断权限 -> 提取未获得的权限
                for (permission in permissions) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        // 没有该权限
                        permissionArray.add(permission)
                    }
                }
                // 提取出没有授予的权限
                if (permissionArray.isNotEmpty()) {
                    // 判断是否有改权限
                    for (s in permissionArray) {
                        ActivityCompat.requestPermissions(this, permissionArray.toTypedArray(), requestCode)
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, s)
                                && ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                            // 用户非永久拒绝过该权限 -> 再次申请
                            ActivityCompat.requestPermissions(this, permissionArray.toTypedArray(), requestCode)
//                            requestPermissions(permissions, requestCode)
                        } else {
                            // 需要判断是否申请过该权限
                            openPermission()
                        }
                    }
                } else {
                    doPermission(requestCode)
                }
            } else {
                Toast.makeText(this, "权限列表不能为空", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionDialog(permissionCode: Int) {
        Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show()
        when (permissionCode) {
            PERMISSION_CALENDAR -> {

            }
            PERMISSION_CALL_LOG -> {

            }
            PERMISSION_CAMERA -> {

            }
            PERMISSION_CONTACTS -> {

            }
            PERMISSION_LOCATION -> {

            }
            PERMISSION_MICROPHONE -> {

            }
            PERMISSION_PHONE -> {

            }
            PERMISSION_SENSORS -> {

            }
            PERMISSION_SMS -> {

            }
            PERMISSION_INSTALL -> {

            }
            PERMISSION_DOWNLOAD -> {

            }
            else -> {

            }
        }
    }

    /**
     * 打开权限设置界面
     */
    private fun openPermission() {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", packageName, null)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    /**
     * 日历日程
     */
    open fun permissionCalendar() {}

    /**
     * 打开相机
     */
    open fun permissionCamera() {}

    /**
     * 联系人
     */
    open fun permissionContacts() {}

    /**
     * 定位
     */
    open fun permissionLocation() {}

    /**
     * 传感器
     */
    open fun permissionSensors() {}

    /**
     * 拨打电话权限
     */
    open fun permissionCallPhone() {}

    /**
     * 录音
     */
    open fun permissionRecording() {}

    /**
     * 短信息
     */
    open fun permissionSms() {}

    /**
     * 存储
     */
    open fun permissionStorage() {}

    /**
     * 安装位置来源应用
     */
    open fun permissionInstall() {}

    /**
     * 下载文件
     */
    open fun permissionDownload() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSION_INSTALL -> {
                onPermissionFunc {
                    permissionInstall()
                }
            }
        }
    }
}