package com.lqk.net

/**
 * @author LQK
 * @time 2019/3/11 16:54
 * @remark
 */
class NetException : Exception {
    companion object {
        private val serialVersionUID = 1L
    }

    /**
     * the server return code
     */
    private var eCode: Int = 0

    /**
     * the server return error message
     */
    private var eMsg: Any? = null

    constructor(eCode: Int, eMsg: Any) {
        this.eCode = eCode
        this.eMsg = eMsg
    }

    fun getEcode(): Int {
        return eCode
    }

    fun getEmsg(): Any? {
        return eMsg
    }
}