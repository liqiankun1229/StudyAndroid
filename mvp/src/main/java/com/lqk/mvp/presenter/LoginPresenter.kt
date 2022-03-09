package com.lqk.mvp.presenter

import com.lqk.mvp.bean.User
import com.lqk.mvp.model.ILoginModel
import com.lqk.mvp.model.LoginModel
import com.lqk.mvp.ui.ILogin

/**
 * @author lqk
 * @date 2018/6/11
 * @time 23:23
 * @remarks
 */
class LoginPresenter(private var iLogin: ILogin) : ILoginPresenter {

    private var loginModel: ILoginModel = LoginModel()

    override fun doLogin(userName: String, password: String) {

        val user = User(userName, password)
//        iLogin.loginResult(loginModel.doLogin(userName, password))
        iLogin.loginTo(user)
    }

}