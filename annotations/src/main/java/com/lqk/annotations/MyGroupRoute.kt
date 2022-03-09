package com.lqk.annotations

import javax.annotation.processing.SupportedAnnotationTypes

/**
 * @author LQK
 * @time 2022/3/4 17:17
 * @remark
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@SupportedAnnotationTypes("com.lqk.annotations.MyGroupRoute")
annotation class MyGroupRoute(val path: String = "", val group: String = "")
