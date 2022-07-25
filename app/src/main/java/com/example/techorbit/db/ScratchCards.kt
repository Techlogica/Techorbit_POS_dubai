package com.example.techorbit.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scrachcard")
data class ScratchCards(
    var name:String?=null,
    var benefits: String?=null,
    var downlodedCount: Int?=null,
    var localAmount: String?=null,
    var localCurrency: String?=null,
    var mrp: String?=null,
    var planId: Int?=null,
    @PrimaryKey
    var id:Int?=null

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(benefits)
        parcel.writeInt(downlodedCount!!)
        parcel.writeString(localAmount)
        parcel.writeString(localCurrency)
        parcel.writeString(mrp)
        parcel.writeInt(planId!!)
        parcel.writeInt(id!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScratchCards> {
        override fun createFromParcel(parcel: Parcel): ScratchCards {
            return ScratchCards(parcel)
        }

        override fun newArray(size: Int): Array<ScratchCards?> {
            return arrayOfNulls(size)
        }
    }
}