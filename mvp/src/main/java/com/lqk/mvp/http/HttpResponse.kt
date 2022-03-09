package com.lqk.mvp.http

import java.io.Serializable

/**
 * @author LQK
 * @time 2019/7/29 15:17
 * @remark
 */
class HttpResponse<T> : Serializable {
    var status: Int = -1
    var message: String = ""
    var data: T? = null


}