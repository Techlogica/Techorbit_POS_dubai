package com.example.techorbit.model

import android.os.Parcel
import android.os.Parcelable

class Plans() :Parcelable{
    var benefits:String?=null
    var downlodedCount:Int?=null
    var localAmount: String?=null
    var localCurrency: String?=null
    var mrp: String?=null
    var planId: Int?=null

    constructor(parcel: Parcel) : this() {
        benefits = parcel.readString()
        localAmount = parcel.readString()
        localCurrency = parcel.readString()
        mrp = parcel.readString()
        planId = parcel.readInt()
        downlodedCount = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(benefits)
        parcel.writeString(localAmount)
        parcel.writeString(localCurrency)
        parcel.writeString(mrp)
        parcel.writeInt(planId!!)
        parcel.writeValue(downlodedCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Plans> {
        override fun createFromParcel(parcel: Parcel): Plans {
            return Plans(parcel)
        }

        override fun newArray(size: Int): Array<Plans?> {
            return arrayOfNulls(size)
        }
    }
}
