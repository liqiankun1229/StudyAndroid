package com.example.dev

import java.io.Serializable

/**
 * @author LQK
 * @time 2022/6/2 9:57
 *
 */

class EventBean(
    var type: Int,
    var isSucceed: Boolean,
    var params: HashMap<String, Any> = hashMapOf()
) : Serializable