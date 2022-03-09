package com.lqk.activity.service.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.lqk.activity.IFileAidlInterface.Stub

class FileService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private var mIBinder = object :Stub(){
        override fun basicTypes(anInt: Int, aLong: Long, aBoolean: Boolean, aFloat: Float, aDouble: Double, aString: String?) {

        }

        override fun client2Server(pfd: ParcelFileDescriptor?) {

        }
    }
}