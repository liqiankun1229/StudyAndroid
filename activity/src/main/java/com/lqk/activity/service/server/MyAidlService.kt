package com.lqk.activity.service.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
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
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {

        }

        override fun addPerson(person: Person?) {
            mPersonList.add(person!!)
            Log.d(
                TAG,
                "addPerson: ${Process.myPid()} : ${person.name} : ${person.sex} : ${person.age}"
            )
        }

        override fun getPersonList(): MutableList<Person> {
            mPersonList.forEach { person: Person ->
                Log.d(
                    TAG,
                    "addPerson: ${Process.myPid()} : ${person.name} : ${person.sex} : ${person.age}"
                )
            }
            return mPersonList
        }

        override fun addBytes(bytes: ByteArray?) {
            Log.d(TAG, "${Process.myPid()} addBytes: ${bytes?.size}")
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