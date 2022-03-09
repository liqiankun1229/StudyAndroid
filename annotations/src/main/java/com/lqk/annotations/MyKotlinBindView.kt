package com.lqk.annotations

/**
 * @author LQK
 * @time 2019/5/1 17:34
 * @remark 自定义BindView注解
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class MyKotlinBindView(val myId: Int = -1)