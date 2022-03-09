package com.lqk.net

import android.os.Handler
import android.os.Looper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


/**
 * @author LQK
 * @time 2019/3/11 16:47
 * @remark
 */
class NetJsonCallback : Callback {

    /**
     * the logic layer exception, may alter in different app
     */
    protected val RESULT_CODE = "ecode" // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected val RESULT_CODE_VALUE = 0
    protected val ERROR_MSG = "emsg"
    protected val EMPTY_MSG = ""
    protected val COOKIE_STORE = "Set-Cookie" // decide the server it
    // can has the value of
    // set-cookie2

    /**
     * the java layer exception, do not same to the logic error
     */
    protected val NETWORK_ERROR = -1 // the network relative error
    protected val JSON_ERROR = -2 // the JSON relative error
    protected val OTHER_ERROR = -3 // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private var mDeliveryHandler: Handler? = null
    private var mListener: DisposeDataListener? = null
    private var mClass: Class<*>? = null

    constructor(handler: DisposeDataHandle) {
        this.mListener = handler.mListener
        this.mClass = handler.mClass
        this.mDeliveryHandler = Handler(Looper.getMainLooper())
    }

    override fun onFailure(call: Call, e: IOException) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler?.post {
            mListener?.onFailure(NetException(NETWORK_ERROR, e))
        }
    }

    override fun onResponse(call: Call, response: Response) {
        val result = response.body?.string()
        val cookieLists = handleCookie(response.headers)
        mDeliveryHandler?.post {
            handleResponse(result)
            /**
             * handle the cookie
             */
            /**
             * handle the cookie
             */
            if (mListener is DisposeHandleCookieListener) {
                (mListener as DisposeHandleCookieListener).onCookie(cookieLists)
            }
        }
    }

    private fun handleCookie(headers: Headers): ArrayList<String> {
        val tempList = ArrayList<String>()
        for (i in 0 until headers.size) {
            if (headers.name(i).equals(COOKIE_STORE, true)) {
                tempList.add(headers.value(i))
            }
        }
        return tempList
    }

    private fun handleResponse(responseObj: Any?) {
        if (responseObj == null || responseObj.toString().trim { it <= ' ' } == "") {
            mListener?.onFailure(NetException(NETWORK_ERROR, EMPTY_MSG))
            return
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            val result = JSONObject(responseObj.toString())
            if (mClass == null) {
                mListener?.onSuccess(result)
            } else {
                val obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass!!)
                if (obj != null) {
                    mListener?.onSuccess(obj)
                } else {
                    mListener?.onFailure(NetException(JSON_ERROR, EMPTY_MSG))
                }
            }
        } catch (e: Exception) {
            mListener?.onFailure(NetException(OTHER_ERROR, e.message ?: ""))
            e.printStackTrace()
        }

    }
}