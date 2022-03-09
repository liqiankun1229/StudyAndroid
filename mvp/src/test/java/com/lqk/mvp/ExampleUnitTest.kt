package com.lqk.mvp

import org.junit.Assert.assertEquals
import org.junit.Test

import junit.framework.Assert
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        Assert.assertTrue(true)
    }

    @Test
    @Throws(Exception::class)
    fun testRetrofit() {
        Proxy.newProxyInstance(javaClass.classLoader, arrayOf<Class<*>>()) { proxy, method, args ->
            run {

            }
        }
    }
}
