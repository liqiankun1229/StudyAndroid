package com.lqk.mvp.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author LQK
 * @time 2019/8/2 15:37
 * @remark
 */
data class Sex(var s: String) : Parcelable {


    constructor(parcel: Parcel) : this(parcel.readString() ?: "") {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(s)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sex> {

        val DEFAULT = Sex("")

        override fun createFromParcel(parcel: Parcel): Sex {
            return Sex(parcel)
        }

        override fun newArray(size: Int): Array<Sex?> {
            return arrayOfNulls(size)
        }
    }
}