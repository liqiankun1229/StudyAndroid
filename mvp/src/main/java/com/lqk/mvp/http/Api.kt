package com.lqk.mvp.http

import com.lqk.mvp.bean.User
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author lqk
 * @date 2018/8/8
 * @time 10:17
 * @remarks
 */
interface Api {


    @POST("/gateway/system/getServiceMobile")
    fun getData(): Call<String>

    @GET("/user")
    fun getResultData(): Call<User>

    @POST("/user/login")
    fun userLogin(@Body user: User): Call<HttpResponse<User>>

    @GET("/user")
    fun getRxResultData(): Observable<User>

    @POST("/user")
    fun <T> postData(): Observable<T>

}