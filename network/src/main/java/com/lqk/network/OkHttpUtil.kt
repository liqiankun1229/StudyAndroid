package com.lqk.network

import android.util.Log
import com.google.gson.Gson
import com.lqk.network.thread.Action
import com.lqk.network.thread.Run
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * @author LQK
 * @time 2019/4/14 14:17
 * @remark OkHttp
 */
class OkHttpUtil : NetWorkOperation {

    companion object {
        const val TIME_OUT = 10L

        val instance: OkHttpUtil by lazy { OkHttpUtil() }
    }

    private val okHttpClient: OkHttpClient

    init {

        val okHttpClientBuilder = OkHttpClient.Builder()
        // 设置连接超时时间
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        // 写入超时时间
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        // 读取超时时间
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
        // 设置重定向 默认 true
        okHttpClientBuilder.followRedirects(true)
        // 添加请求头
        okHttpClientBuilder.addInterceptor { chain ->
            run {
                val request = chain.request()
                    .newBuilder()
                    .addHeader("User-Agent", "Android-Mobile")
                    .build()
                return@run chain.proceed(request)
            }
        }
        // 添加 https 支持
        okHttpClientBuilder.hostnameVerifier(HostnameVerifier { hostname, session ->
            run {
                return@run true
            }
        })
        okHttpClientBuilder.sslSocketFactory(initSSLSockFactory(), initX509TrustManager())
        // 配置客户端
        okHttpClient = okHttpClientBuilder.build()
    }

    fun getOkHttpClient(): OkHttpClient {
        return this.okHttpClient
    }

    private fun initSSLSockFactory(): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("SSL")
        try {
            val x509TrustManager = arrayOf(initX509TrustManager())
            sslContext.init(null, x509TrustManager, SecureRandom())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sslContext.socketFactory
    }

    private fun initX509TrustManager(): X509TrustManager {
        return object : X509TrustManager {

            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                Log.d("", "")
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                Log.d("", "")
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    /**
     * Get请求
     */
    private fun doGetRequest(url: String, params: HashMap<String, Any>): Request {
        var paramsUrl = "?"
        for (entry in params) {
            paramsUrl = "$paramsUrl${entry.key}=${entry.value}"
        }
        if (paramsUrl != "?") {
            paramsUrl = "$url$paramsUrl"
        }
        return Request.Builder()
            .url(paramsUrl)
            .get()
            .build()
    }

    /**
     * post 请求
     */
    private fun <T> doPostRequest(url: String, params: T): Request {

        val mediaType: MediaType = "application/json".toMediaType()
        val jsonObject = Gson().toJson(params)
        val requestBody = jsonObject.toRequestBody(mediaType)

        return Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
    }

    private fun doPostRequest(url: String, params: HashMap<String, Any>): Request {
        val mediaType: MediaType = "application/json".toMediaType()
        val jsonObject = JSONObject()
        for (entry in params) {
            jsonObject.put(entry.key, entry.value)
        }
        val requestBody = jsonObject.toString().toRequestBody(mediaType)
        return Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
    }

    private fun doCall(request: Request): Call {
        return okHttpClient.newCall(request)
    }

    override fun <T> doGet(
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<T>,
        callback: NetWorkCallback<T>?
    ) {
        doCall(doGetRequest(url, params)).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback?.onFailed("网络请求失败")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    val data = response.body?.string()
                    if (data == null) {
                        callback?.onFailed("数据获取失败")
                    } else {
                        val resultData = Gson().fromJson<T>(data, resultClass) as T
                        Run.onUiAsync(object : Action {
                            override fun call() {
                                callback?.onSucceed(resultData)
                            }
                        })
                    }
                }
            }
        })
    }

    override fun <T> doPost(
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<T>,
        callback: NetWorkCallback<T>?
    ) {
        // 请求 url 地址
        if (!url.startsWith("http")) return
//        val realUrl = if (!url.startsWith("http")) {
//            "$BASE_URL$url"
//        } else {
//            url
//        }

        doCall(doPostRequest(url, params)).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Run.onUiAsync(object : Action {
                    override fun call() {
                        callback?.onFailed("网络请求失败")
                    }
                })
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    val data = response.body?.string()
                    if (data == null) {
                        callback?.onFailed("数据获取失败")
                    } else {
//                        val typeToken = object : TypeToken<T>() {}.rawType
//                        val resultData = Gson().fromJson(data, typeToken) as T
                        if (resultClass == String::class.java) {
                            Run.onUiAsync(object : Action {
                                override fun call() {
                                    callback?.onSucceed(data as T)
                                }
                            })
                        } else {
                            val type = resultClass as Type
                            val resultData = Gson().fromJson(data, type) as T
                            Run.onUiAsync(object : Action {
                                override fun call() {
                                    callback?.onSucceed(resultData)
                                }
                            })
                        }
                    }
                }
            }
        })
    }

    override fun <T> doSyncPost(
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<T>
    ): T? {
        val response = doCall(doPostRequest(url, params)).execute()
        if (response.body != null) {
            val data = response.body?.string()
            return if (data == null) {
                null
            } else {
                try {
                    if (resultClass == String::class.java) {

                        if (data != null) {
                            data as T
                        } else {
                            null
                        }
                    } else {
                        val type = resultClass as Type
                        Gson().fromJson(data, type) as T
                    }
                } catch (e:Exception){
                    e.printStackTrace()
                    return null
                }
            }
        }
        return null
    }

    override fun download(
        filePath: String,
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<File>,
        callback: NetWorkOperation.OnDownLoadListener
    ) {
        // 地址 -> 本地路径
        try {
            val request = Request.Builder().url(url).build()
//            var response = okHttpClient.newCall(request).execute()

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onFailed(e.message ?: "下载失败")
                }

                override fun onResponse(call: Call, response: Response) {
                    var inputStream: InputStream
                    val buffer = ByteArray(2048)
                    var len = 0
                    var fileOutputStream: FileOutputStream
                    // 存储
                    val file = File(filePath)
                    // 删除路径中原有的zip
                    file.delete()
                    // 重新创建该文件
                    file.createNewFile()

                    try {
                        if (response.body != null) {
                            response.body?.let {
                                inputStream = it.byteStream()
                                var fileLen = it.contentLength()
                                fileOutputStream = FileOutputStream(file)
                                len = inputStream.read(buffer)
                                var nums = 0
                                while (len != -1) {
                                    fileOutputStream.write(buffer, 0, len)
                                    nums += len
                                    // 回调进度
                                    Run.onUiAsync(object : Action {
                                        override fun call() {
                                            callback.downloadProcess((nums * 1.0f / fileLen * 100).toInt())
                                        }
                                    })
                                    len = inputStream.read(buffer)
                                }
                                fileOutputStream.flush()
                                Run.onUiAsync(object : Action {
                                    override fun call() {
                                        callback.onSucceed(file)
                                    }
                                })
                            }
                        } else {
                            callback.onFailed("下载失败")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback.onFailed(e.message ?: "下载失败")
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onFailed(e.message ?: "下载失败")
        }
    }

    override fun doPut() {
    }

    override fun doDelete() {
    }


}