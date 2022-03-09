package com.lqk.http

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author lqk
 * @date 2018/5/25
 * @time 23:32
 * @remarks
 */
object RetrofitHelper {

    val instance = Retrofit.Builder()
            .baseUrl("http://47.95.3.33:8080/iTalker/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    // 创建网络接口的实例


}
