package com.example.techorbit.model.five

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Recharge(
    var benefits: String?=null,
    var downlodedCount: Int?=0,
    var localAmount: String?=null,
    var localCurrency: String?=null,
    var mrp: String?=null,
    var planId: Int?=null,
    val name:String?=null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(benefits)
        downlodedCount?.let { parcel.writeInt(it) }
        parcel.writeString(localAmount)
        parcel.writeString(localCurrency)
        parcel.writeString(mrp)
        parcel.writeInt(planId!!)
        parcel.writeString(name)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recharge> {
        override fun createFromParcel(parcel: Parcel): Recharge {
            return Recharge(parcel)
        }

        override fun newArray(size: Int): Array<Recharge?> {
            return arrayOfNulls(size)
        }
    }
}