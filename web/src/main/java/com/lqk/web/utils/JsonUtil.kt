package com.lqk.web.utils

import com.google.gson.Gson
import org.json.JSONObject

/**
 * @author LQK
 * @remark
 */
object JsonUtil {
    private var instance = Gson()


    fun toJson() {
        var jsonObj = JSONObject()
        jsonObj.put("", 1.0)
    }


}