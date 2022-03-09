package com.lqk.web.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lqk.web.R
import com.tencent.smtt.sdk.WebView

/**
 * @date 2018/9/9
 * @time 14:33
 * @remarks 网页 Fragment 载体，包含一个 WebView 控件
 */
@SuppressLint("ValidFragment")
class WebContanierFragment : Fragment {

    private var webView: WebView? = null

    // 当webView 为空时 显示空布局（加载失败）
    private var emptyLayout: Int = -1

    // 网页浏览 包含一个webView 控件即可
    private var layoutId: Int = -1

    constructor(layoutId: Int) : super() {

    }

    constructor(emptyLayoutId: Int = R.layout.lay_empty, layoutId: Int) : super() {
        this.emptyLayout = emptyLayoutId
        this.layoutId = layoutId
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (webView == null) {
            // 寻找 webView
            val root: View = if (layoutId != -1) {
                inflater.inflate(R.layout.lay_web, container, false)
            } else {
                inflater.inflate(layoutId, container, false)
            }
            webView = root.findViewById(R.id.web)
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}