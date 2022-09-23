package com.example.dev.net

import com.example.dev.bean.PackageBean
import retrofit2.http.POST

/**
 * @author LQK
 * @time 2022/8/1 10:43
 *
 */
interface APIService {
    @POST("zip/packages")
    suspend fun packageList(): BaseResponseBean<MutableList<PackageBean>>

    @POST("zip/packages/exception")
    suspend fun packageListException(): BaseResponseBean<MutableList<PackageBean>>
}