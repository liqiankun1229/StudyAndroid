package com.lqk.net

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class NetCookieJar : CookieJar {
    private val allCookies = arrayListOf<Cookie>()

    @Synchronized
    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val result = arrayListOf<Cookie>()
        for (cookie in allCookies) {
            if (cookie.matches(url)) {
                result.add(cookie)
            }
        }
        return result
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        allCookies.addAll(cookies)
    }
}
