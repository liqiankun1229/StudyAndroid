package com.lqk.web.utils

import android.app.Application
import com.tencent.mmkv.MMKV

/**
 * MMKV 工具类
 */
object MMKVUtils {
    private var mmkv: MMKV? = null
    private var rootPath: String = ""

    /**
     * 初始化 MMKV
     */
    fun initMMKV(application: Application) {
        rootPath = MMKV.initialize(application)
        mmkv = MMKV.defaultMMKV()
//        mmkv?.registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
//            val txt = sharedPreferences.getString(s, "")
//        }
    }

    fun gainKeyArr(): Array<String> {
        return mmkv?.allKeys() ?: arrayOf()
    }

    fun saveString(k: String, v: String) {
        mmkv?.encode(k, v)
    }

    fun gainString(k: String): String {
        return mmkv?.decodeString(k) ?: ""
    }

    fun clear() {
        var sp = mmkv?.clear()
    }

    fun clearAll() {
        mmkv?.clearAll()
    }
}