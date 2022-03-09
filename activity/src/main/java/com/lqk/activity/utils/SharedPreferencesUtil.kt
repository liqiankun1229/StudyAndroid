package com.lqk.activity.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * @date 2018/9/19
 * @time 11:34
 * @remarks
 */
object SharedPreferencesUtil {

    private var sharedPreferences: SharedPreferences? = null

    private var sharedPreferencesEditor: SharedPreferences.Editor? = null

    /* 保存数据 -> 正常类型 */

    @SuppressLint("CommitPrefEdits")
    fun saveInt(context: Context, key: String, value: Int) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        sharedPreferencesEditor!!.putInt(key, value)
        // 提交保存
        sharedPreferencesEditor!!.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveFloat(context: Context, key: String, value: Float) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        sharedPreferencesEditor!!.putFloat(key, value)
        // 提交保存
        sharedPreferencesEditor!!.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveLong(context: Context, key: String, value: Long) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        sharedPreferencesEditor!!.putLong(key, value)
        // 提交保存
        sharedPreferencesEditor!!.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        sharedPreferencesEditor!!.putBoolean(key, value)
        // 提交保存
        sharedPreferencesEditor!!.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveString(context: Context, key: String, value: String) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        sharedPreferencesEditor!!.putString(key, value)
        // 提交保存
        sharedPreferencesEditor!!.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveStringSet(context: Context, key: String, value: MutableSet<String>) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        sharedPreferencesEditor!!.putStringSet(key, value)
        // 提交保存
        sharedPreferencesEditor!!.apply()
    }

    /* 需要转换 */


    /* 获取数据 -> 正常类型 */

    fun getIntData(context: Context, key: String): Int {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences!!.getInt(key, -1)
    }

    fun getFloatData(context: Context, key: String): Float {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences!!.getFloat(key, -1F)
    }

    fun getLongData(context: Context, key: String): Long {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences!!.getLong(key, -1L)
    }

    fun getBooleanData(context: Context, key: String): Boolean {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences!!.getBoolean(key, false)
    }

    fun getStringData(context: Context, key: String): String {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences!!.getString(key, "") ?: ""
    }

    fun getSetData(context: Context, key: String): MutableSet<String> {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences!!.getStringSet(key, mutableSetOf()) ?: mutableSetOf()
    }


}