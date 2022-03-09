package com.lqk.mvp.model

/**
 * @author lqk
 * @date 2018/6/11
 * @time 23:31
 * @remarks
 */
interface ILoginModel {
    fun doLogin(userName: String, password: String): Boolean
}