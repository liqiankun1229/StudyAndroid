package com.example.dev.net

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

/**
 * @author LQK
 * @time 2022/8/1 10:36
 *
 */
class RetrofitHelper private constructor() {

    private val retrofit: Retrofit

    init {
        val gson = Gson().newBuilder()
            .setLenient()
            .serializeNulls()
            .create()
        retrofit = Retrofit.Builder()
//            .baseUrl("http://10.100.156.146:5000/")
            .baseUrl("http://192.168.31.71:5000/")
            .client(initOkhttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    companion object {
        val instance: RetrofitHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitHelper()
        }
    }

    private fun initOkhttpClient(): OkHttpClient {
        return Builder()
            .connectTimeout(5, SECONDS)
            .readTimeout(5, SECONDS)
            .addInterceptor(initLogInterceptor())
            .build()
    }

    /*
    * 日志拦截器
    * */
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message ->
            run {
                Log.i("Retrofit", message)
            }
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /*
    * 具体服务实例化
    * */
    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }
}