package com.lqk.butter.ui.activity

import android.content.Intent
import android.net.Uri
import com.lqk.butter.R
import com.lqk.butter.base.BaseVBActivity
import com.lqk.butter.databinding.ActivityPermissionBinding
import com.lqk.butter.zxing.CaptureActivity

class PermissionActivity : BaseVBActivity<ActivityPermissionBinding>() {


    override fun layoutId(): Int {
        return R.layout.activity_permission
    }

    override fun initViewBinding(): ActivityPermissionBinding {
        return ActivityPermissionBinding.inflate(layoutInflater)
    }

    lateinit var function: () -> Unit

    override fun initView() {
        vb.btnCall.setOnClickListener {
            doPermission(PERMISSION_PHONE) {
                call()
            }
        }
        vb.btnCamera.setOnClickListener {
            doPermission(PERMISSION_CAMERA)
        }
        vb.btnFunc.setOnClickListener {
            //            function()
            doPermission(PERMISSION_CAMERA) {
                zxing()
            }
        }
        vb.btnFunc2.setOnClickListener {
            zz { zxing() }
        }
        vb.btnFunc3.setOnClickListener {
            zz { }
        }
    }

    fun call() {
        val phoneNumber = vb.etPhone.text.toString()
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
