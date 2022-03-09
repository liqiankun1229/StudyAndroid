package com.lqk.butter.ui.activity

import android.content.Intent
import android.net.Uri
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import com.lqk.butter.zxing.CaptureActivity
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_permission
    }

    lateinit var function: () -> Unit

    override fun initView() {
        btn_call.setOnClickListener {
            doPermission(PERMISSION_PHONE) {
                call()
            }
        }
        btn_camera.setOnClickListener {
            doPermission(PERMISSION_CAMERA)
        }
        btn_func.setOnClickListener {
            //            function()
            doPermission(PERMISSION_CAMERA) {
                zxing()
            }
        }
        btn_func2.setOnClickListener {
            zz { zxing() }
        }
        btn_func3.setOnClickListener {
            zz { }
        }
    }

    fun call() {
        val phoneNumber = et_phone.text.toString()
        val phoneIntent = Intent(Intent.ACTION_CALL)
        val userPhone = Uri.parse("tel:$phoneNumber")
        phoneIntent.data = userPhone
        startActivity(phoneIntent)
    }

    fun zz(action: () -> Unit) {
        function = action
    }

    fun zxing() {
//        Toast.makeText(this, "执行方法", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, CaptureActivity::class.java))
    }

    override fun permissionCamera() {
        startActivity(Intent(this, CaptureActivity::class.java))
    }

}
