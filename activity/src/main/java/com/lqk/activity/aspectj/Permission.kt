package com.lqk.activity.aspectj

/**
 * @author LQK
 * @date 2021/12/29 23:41
 * @remark
 */
@Target(AnnotationTarget.FUNCTION)// 作用域
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission {
}