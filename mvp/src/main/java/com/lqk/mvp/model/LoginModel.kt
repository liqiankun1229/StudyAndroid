package com.lqk.mvp.model

/**
 * @author lqk
 * @date 2018/6/11
 * @time 23:30
 * @remarks
 */
class LoginModel : ILoginModel {
    override fun doLogin(userName: String, password: String): Boolean {
        if (userName.length == password.length) {
            return true
        }
        return false
    }
}