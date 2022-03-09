package com.lqk.activity.service.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.lqk.activity.IMyAidlInterface
import com.lqk.activity.bean.Person

/**
 * @date 2018/9/17
 * @time 23:32
 *
 * 服务端
 */
class MyAidlService : Service() {

    companion object {
        const val TAG = "MyAidlService"
    }

    private var mPersonList: ArrayList<Person> = ArrayList()

    private var mIBinder = object : IMyAidlInterface.Stub() {
        override fun basicTypes(anInt: Int,
                                aLong: Long,
                                aBoolean: Boolean,
                                aFloat: Float,
                                aDouble: Double,
                                aString: String?) {

        }

        override fun addPerson(person: Person?) {
            mPersonList.add(person!!)
        }

        override fun getPersonList(): MutableList<Person> {
            return mPersonList
        }

    }


    override fun onBind(intent: Intent?): IBinder {
        mPersonList.clear()

//        mIBinder.
        Log.d(TAG, "---- onBind() ----")
//        mIBinder.linkToDeath()
        return mIBinder
    }

}