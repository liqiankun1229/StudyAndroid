package com.lqk.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.lqk.R

/**
 * @author LQK
 * @time 2019/5/28 17:02
 * @remark
 */
class CustomToolbar : FrameLayout {

    // 默认显示样式 白色背景/黑色图标和文字

    // 左边图标
    private lateinit var ivLeft: ImageView
    private var ivLeftShow: Boolean = true
    private var ivLeftDrawable: Drawable? = null
    // 右边图标
    private lateinit var ivRight: ImageView
    private var ivRightShow: Boolean = true
    private var ivRightDrawable: Drawable? = null
    // 标题
    private lateinit var tvTitle: TextView
    private var tvTitleShow: Boolean = true
    private var strTitle: String = ""
    private var colorTitle: Int = Color.BLACK

    // 背景颜色 默认 纯白
    private var bgColor: Int = Color.WHITE

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.toolbar, this, true)
        // 控件 总三个
        ivLeft = findViewById(R.id.iv_left)
        ivRight = findViewById(R.id.iv_right)
        tvTitle = findViewById(R.id.tv_title)
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar)
            ivLeftShow = typedArray.getBoolean(R.styleable.CustomToolbar_show_left_icon, true)
            ivRightShow = typedArray.getBoolean(R.styleable.CustomToolbar_show_right_icon, true)
            tvTitleShow = typedArray.getBoolean(R.styleable.CustomToolbar_show_title, true)
            strTitle = typedArray.getString(R.styleable.CustomToolbar_title) ?: ""
            colorTitle = typedArray.getColor(R.styleable.CustomToolbar_title_color, Color.BLACK)

            ivLeftDrawable = typedArray.getDrawable(R.styleable.CustomToolbar_icon_left)
            ivRightDrawable = typedArray.getDrawable(R.styleable.CustomToolbar_icon_right)

            typedArray.recycle()

        }
        // 是否显示左侧按钮
        if (ivLeftShow) {
            ivLeft.visibility = View.VISIBLE
            if (ivLeftDrawable != null) {
                ivLeft.setImageBitmap(drawableToBitmap(ivLeftDrawable!!))
            }
        } else {
            ivLeft.visibility = View.GONE
        }

        // 是否显示右侧按钮
        if (ivRightShow) {
            ivRight.visibility = View.VISIBLE
            if (ivRightDrawable != null) {
                ivRight.setImageBitmap(drawableToBitmap(ivRightDrawable!!))
            }
        } else {
            ivRight.visibility = View.GONE
        }
        // 标题
        tvTitle.text = strTitle
        tvTitle.setTextColor(colorTitle)


    }

    /**
     * 设置 左侧按钮点击事件
     */
    fun addLeftIconListener(listener: OnClickListener) {
        ivLeft.setOnClickListener(listener)
    }

    /**
     * 设置 左侧按钮点击事件
     */
    fun addRightIconListener(listener: OnClickListener) {
        ivRight.setOnClickListener(listener)
    }


    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        val config = if (drawable.opacity != PixelFormat.OPAQUE) {
            Bitmap.Config.ARGB_8888
        } else {
            Bitmap.Config.RGB_565
        }
        val bitmap = Bitmap.createBitmap(w, h, config)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

}