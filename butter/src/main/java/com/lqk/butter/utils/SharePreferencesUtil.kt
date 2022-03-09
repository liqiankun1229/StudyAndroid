package com.lqk.butter.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.lqk.butter.app.BaseApplication

/**
 * @author LQK
 * @time 2019/2/27 10:48
 * @remark
 */
@SuppressLint("StaticFieldLeak")
object SharePreferencesUtil {

    private val mApp: Context = BaseApplication.getInstance()
    const val KEY_SHARED_PREFERENCES = "KEY_SHARED_PREFERENCES"

    fun saveString(key: String, value: String) {
        val sharedPreferences: SharedPreferences = mApp.getSharedPreferences(KEY_SHARED_PREFERENCES, 0)

    }

    fun saveString(key: String, value: String, context: Context) {

    }
}