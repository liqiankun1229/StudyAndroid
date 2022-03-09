package com.lqk.mvp.ui

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

/**
 * @author LQK
 * @time 2019/8/1 23:18
 * @remark
 */
interface IHttpData {

    fun <T> dataType(cls: Class<T>): Type {
        when (cls) {
            is ParameterizedType -> {
                return cls
            }
            is GenericArrayType -> {
                return cls
            }
            is WildcardType -> {
                return cls
            }
        }
        return cls
    }
}