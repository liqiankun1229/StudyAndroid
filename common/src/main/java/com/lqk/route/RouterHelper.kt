package com.lqk.route

/**
 * @author LQK
 * @time 2022/3/6 21:20
 * @remark
 */
class RouterHelper private constructor() {

    companion object {
        var any: Any = Any()
        var instance: RouterHelper? = null
        fun initInstance(): RouterHelper {
            if (instance == null) {
                synchronized(any){
                    if (instance == null){
                        instance = RouterHelper()
                    }
                }
            }
            return instance!!
        }
    }

    var instance: RouterHelper? = null

}