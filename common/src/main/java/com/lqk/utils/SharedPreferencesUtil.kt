package com.lqk.utils

import android.content.Context

/**
 * @author LQK
 * @time 2019/3/14 9:30
 * @remark 数据持久存储 SharedPreferences
 */
object SharedPreferencesUtil {

  // 获取 SharedPreferences 的 key
  private const val KEY_SHARED_PREFERENCES_NAME = "data"
  // 设置 存储模式私有只有自己可以读写
  private const val MODE_PRIVATE = Context.MODE_PRIVATE
  // 存储模式 先检测是否存在 存在-> 追加内容 ; 不存在-> 创建新文件
  private const val MODE_APPEND = Context.MODE_APPEND
  // 存储模式 表示当前文件 可以被其他应用读取
  private const val MODE_WORLD_READABLE = Context.MODE_WORLD_READABLE
  // 存储模式 表示当前文件 可以被其他应用写入
  private const val MODE_WORLD_WRITEABLE = Context.MODE_WORLD_WRITEABLE

  fun initSharedPreferences(context: Context) {

  }

  /**
   * 存储 Int 类型数据
   */
  fun saveInt(context: Context, key: String, value: Int) {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt(key, value)
    // 实际写入操作
    editor.apply()
  }

  /**
   * 获取 Int 类型的数据
   */
  fun getInt(context: Context, key: String): Int {
    return getInt(context, key, -1)
  }

  /**
   * 获取 Int 类型的数据
   * 可传入默认值
   */
  fun getInt(context: Context, key: String, defaultValue: Int): Int {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    return sharedPreferences.getInt(key, defaultValue)
  }

  /**
   * 保存 Float 类型数据
   */
  fun saveFloat(context: Context, key: String, value: Float) {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putFloat(key, value)
    // 实际写入操作
    editor.apply()
  }

  /**
   * 获取 Float 类型数据 取不到值返回 0F
   */
  fun getFloat(context: Context, key: String): Float {
    return getFloat(context, key, 0F)
  }

  /**
   * 获取 Float 类型数据
   * 可传入默认值
   */
  fun getFloat(context: Context, key: String, defaultValue: Float): Float {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    return sharedPreferences.getFloat(key, defaultValue)
  }

  /**
   * 保存 Long 类型数据
   */
  fun saveLong(context: Context, key: String, value: Long) {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putLong(key, value)
    // 实际写入操作
    editor.apply()
  }

  fun getLong(context: Context, key: String): Long {
    return getLong(context, key, 0L)
  }

  fun getLong(context: Context, key: String, defaultValue: Long): Long {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    return sharedPreferences.getLong(key, defaultValue)
  }

  fun saveBoolean(context: Context, key: String, value: Boolean) {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(key, value)
    // 实际写入操作
    editor.apply()
  }

  fun getBoolean(context: Context, key: String): Boolean {
    return getBoolean(context, key, false)
  }

  fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    return sharedPreferences.getBoolean(key, defaultValue)
  }

//    fun saveString(key: String, value: String) {
//        val sharedPreferences = Application().getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString(key, value)
//        // 实际写入操作
//        editor.apply()
//    }

  /**
   * 存储字符串数据
   */
  fun saveString(context: Context, key: String, value: String) {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    // 实际写入操作
    editor.apply()
  }

  /**
   * 获取字符串数据默认为 ""
   */
  fun getString(context: Context, key: String): String {
    return getString(context, key, "")
  }

  /**
   * 获取字符串数据 传入默认值
   */
  fun getString(context: Context, key: String, defaultValue: String): String {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
  }

  fun saveStringSet(context: Context, key: String, value: MutableSet<String>) {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putStringSet(key, value)
    // 实际写入操作
    editor.apply()
  }

  fun getStringSet(context: Context, key: String): MutableSet<String> {
    return getStringSet(context, key, mutableSetOf())
  }

  fun getStringSet(context: Context, key: String, defaultValue: MutableSet<String>): MutableSet<String> {
    val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    return sharedPreferences.getStringSet(key, defaultValue) ?: mutableSetOf()
  }


}