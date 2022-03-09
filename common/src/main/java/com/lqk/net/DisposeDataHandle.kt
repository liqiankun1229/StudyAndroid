package com.lqk.net

class DisposeDataHandle {

    var mListener: DisposeDataListener? = null
    var mClass: Class<*>? = null
    var mSource: String = ""

    constructor(mListener: DisposeDataListener) {
        this.mListener = mListener
    }

    constructor(mListener: DisposeDataListener, mClass: Class<*>?) {
        this.mListener = mListener
        this.mClass = mClass
    }

    constructor(mListener: DisposeDataListener, mSource: String) {
        this.mListener = mListener
        this.mSource = mSource
    }

}
