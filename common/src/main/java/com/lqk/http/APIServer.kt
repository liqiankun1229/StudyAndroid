package com.lqk.http

import com.lqk.bean.User

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author lqk
 * @date 2018/5/26
 * @time 14:13
 * @remarks
 */
interface APIServer {

    @GET("api/account/test")
    fun getTest(): Call<String>

    @GET("api/account/test")
    fun getTestString(): Call<ResponseBody>

    @POST("api/account/testuser")
    fun getUser(): Call<User>

}
