package com.lqk.network.callback

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * @author LQK
 * @time 2019/7/27 14:46
 * @remark
 */
interface OkHttpCallback<T> : Callback {
    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {

    }
}