package com.lqk.mvp.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author LQK
 * @time 2019/8/8 22:01
 * @remark
 */
class RetrofitHelper private constructor() {
    companion object {
        val instance: RetrofitHelper by lazy { RetrofitHelper() }
    }

    private val interceptor: Interceptor
    private val retrofit: Retrofit

    // 存放请求头 Header
    private var headerMap: LinkedHashMap<String, String> = linkedMapOf()

    init {
        interceptor = Interceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("charset", "utf-8")
                    .addHeader("Content-Type", "application/json")
                    .build()
            )
        }
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.31.71:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(initClient())
            .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(initLogInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun initLogInterceptor(): Interceptor {
//        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return HttpLoggingInterceptor(HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * 用户调用 Retrofit.instance.initService.doPost
     */
    fun <T> initService(service: Class<T>): T {
        return retrofit.create(service)
    }
}