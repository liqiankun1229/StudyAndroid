package com.lqk.mvp.ui

import com.lqk.mvp.bean.User

/**
 * @author lqk
 * @date 2018/6/11
 * @time 23:20
 * @remarks
 */
interface ILogin {
    fun login(userName: String, password: String)
    fun loginResult(result: Boolean)
    fun loginTo(user: User)

    // 携程
//    suspend fun loginUser()
}