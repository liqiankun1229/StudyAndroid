package com.lqk.mvp.bean

import android.annotation.TargetApi
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

/**
 * @author lqk
 * @date 2018/7/9
 * @time 12:33
 * @remarks
 */
//data class User(var name: String, var mobile: String)
data class User(var name: String, var mobile: String, var sex: Sex) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.M)
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readParcelable<Sex>(ClassLoader.getSystemClassLoader())!!)

    constructor(name: String, mobile: String) : this(name, mobile, Sex("男"))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(mobile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        @TargetApi(Build.VERSION_CODES.M)
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}