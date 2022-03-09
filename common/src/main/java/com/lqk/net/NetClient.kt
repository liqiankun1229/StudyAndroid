package com.lqk.net

import okhttp3.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


/**
 * @author LQK
 * @time 2019/3/11 16:27
 * @remark
 */
class NetClient {


    companion object {

        // 链接超时
        private val TIME_OUT = 30L
        // 客户端
        private var mOkHttpClient: OkHttpClient? = null

        fun getOkHttpClient(): OkHttpClient? {
            return mOkHttpClient
        }

//    /**
//     * 指定cilent信任指定证书
//     *
//     * @param certificates
//     */
//    public static void setCertificates(InputStream... certificates) {
//        mOkHttpClient.newBuilder().sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null)).build();
//    }

        /**
         * 通过构造好的Request,Callback去发送请求
         */
        operator fun get(request: Request, handle: DisposeDataHandle): Call {
            val call = mOkHttpClient!!.newCall(request)
            call.enqueue(NetJsonCallback(handle))
            return call
        }

        fun post(request: Request, handle: DisposeDataHandle): Call {
            val call = mOkHttpClient!!.newCall(request)
            call.enqueue(NetJsonCallback(handle))
            return call
        }

        fun downloadFile(request: Request, handle: DisposeDataHandle): Call {
            val call = mOkHttpClient!!.newCall(request)
            call.enqueue(NetFileCallback(handle))
            return call
        }
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.hostnameVerifier(object : HostnameVerifier {

            override fun verify(hostname: String?, session: SSLSession?): Boolean {
                return true
            }
        })

        /**
         *  为所有请求添加请求头，看个人需求
         */
        okHttpClientBuilder.addInterceptor(object : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "Imooc-Mobile") // 标明发送本次请求的客户端
                        .build()
                return chain.proceed(request)
            }
        })
        okHttpClientBuilder.cookieJar(NetCookieJar())
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        okHttpClientBuilder.followRedirects(true)
        /**
         * trust all the https point
         */
        okHttpClientBuilder.sslSocketFactory(NetUtil.initSSLSocketFactory(), NetUtil.initTrustManager())
        mOkHttpClient = okHttpClientBuilder.build()
    }

}