package com.lqk.butter.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.lqk.annotations.MyJavaBinderView
import com.lqk.annotations.MyKotlinBindView
import com.lqk.butter.R
import com.lqk.butter.base.BaseVBActivity
import com.lqk.butter.bean.Icon
import com.lqk.butter.common.onClick
import com.lqk.butter.compiler.DoBinder
import com.lqk.butter.databinding.ActivityZxingBinding
import com.lqk.butter.zxing.CaptureActivity
import com.lqk.butter.zxing.CodeUtil
import com.lqk.butter.zxing.camera.open.OpenCamera
import com.tbruyelle.rxpermissions2.RxPermissions

class ZxingActivity : BaseVBActivity<ActivityZxingBinding>() {


    @MyKotlinBindView(R.id.btn_flash)
    lateinit var tvF: Button

    @MyJavaBinderView(value = 123)
    lateinit var textView: TextView

    override fun layoutId(): Int {
        return R.layout.activity_zxing
    }

    @SuppressLint("CheckResult")
    override fun initView() {
        DoBinder.bind(this)
        tvF.setOnClickListener {
            Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show()
        }
        vb.btnAction.onClick {
            // 根据字符串 生成二维码
            Glide.with(this)
                .load(CodeUtil.createCode(this, "哈哈哈，你个渣渣"))
                .into(vb.ivCode)
        }

        val data = mutableListOf<Icon>()
        for (i in 0..23) {
            data.add(Icon("title$i", "data$i"))
        }
        val dataAdapter = MyAdapter(this, data)
        vb.gvData.adapter = dataAdapter
        vb.gvData.viewTreeObserver.addOnGlobalLayoutListener {
            val number = vb.gvData.numColumns
            if (number > 0) {

            }
        }
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.CAMERA)
            .subscribe {
                if (it) {
                    Log.d("granted", "$it")
                } else {
                    Log.d("granted", "$it")
                }
            }
    }

    override fun initViewBinding(): ActivityZxingBinding {
        return ActivityZxingBinding.inflate(layoutInflater)
    }

    var isOpen = false

    var camera: OpenCamera? = null

    fun changeFlash() {

//        if (!isOpen){
//            val parameter = permissionCamera?.permissionCamera?.parameters
//            parameter?.flashMode = android.hardware.Camera.Parameters.FLASH_MODE_ON
//            permissionCamera?.permissionCamera?.parameters = parameter
//            isOpen = true
//        } else {
//            val parameter = permissionCamera?.permissionCamera?.parameters
//            parameter?.flashMode = android.hardware.Camera.Parameters.FLASH_MODE_OFF
//            permissionCamera?.permissionCamera?.parameters = parameter
//            isOpen = false
//        }
        startActivity(Intent(this, CaptureActivity::class.java))
    }


    inner class MyAdapter : BaseAdapter {

        private var mContext: Context
        var data: MutableList<Icon> = mutableListOf()
        private var layoutId: Int

        constructor(context: Context, data: MutableList<Icon>, layoutId: Int = R.layout.item_grid) {
            this.data = data
            this.mContext = context
            this.layoutId = layoutId
        }

        override fun isEmpty(): Boolean {
            return data.size == 0
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder = MyViewHolder()
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_grid, null)
            holder.textView = view.findViewById(R.id.tv_title)
            holder.editView = view.findViewById(R.id.et_data)
            view.tag = holder
            val icon = data[position]
            holder.textView?.text = icon.title
            holder.editView?.setText(icon.data)
            return view
        }

        override fun getItemViewType(position: Int): Int {
            return 1
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun isEnabled(position: Int): Boolean {
            return false
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        override fun areAllItemsEnabled(): Boolean {
            return false
        }

        override fun registerDataSetObserver(observer: DataSetObserver?) {

        }

        override fun unregisterDataSetObserver(observer: DataSetObserver?) {

        }

        override fun getCount(): Int {
            return this.data.size
        }
    }

    inner class MyViewHolder {
        var textView: TextView? = null
        var editView: EditText? = null
    }
}
