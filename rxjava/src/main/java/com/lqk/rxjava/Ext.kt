package com.lqk.rxjava

import android.content.Context
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

/**
 * @author LQK
 * @time 2019/7/15 10:38
 * @remark
 */
fun Context.doAny(){
  (this as RxAppCompatActivity)
}