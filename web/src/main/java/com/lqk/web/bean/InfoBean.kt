package com.lqk.web.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author LQK
 * @time 2020/6/16 17:39
 * @remark
 */
class InfoBean : Parcelable {
    var userType: Int = 0
    var userName: String = ""
    var userSex: String = ""

    constructor() : this(0, "", "")
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString() ?: "", parcel.readString() ?: "")
    constructor(userType: Int, userName: String, userSex: String) {
        this.userType = userType
        this.userName = userName
        this.userSex = userSex
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest?.writeInt(userType)
        dest?.writeString(userName)
        dest?.writeString(userSex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InfoBean> {
        override fun createFromParcel(parcel: Parcel): InfoBean {
            return InfoBean(parcel)
        }

        override fun newArray(size: Int): Array<InfoBean?> {
            return arrayOfNulls(size)
        }
    }
}