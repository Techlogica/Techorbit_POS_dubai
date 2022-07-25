package com.example.techorbit.model.prepaidplans

import android.os.Parcel
import android.os.Parcelable

data class PrepaidPlans(
    val data: List<Data>?,
    val error_message: String?,
    val flushStatus: Int?,
    val response_code: Int?,
    val syncVersion: Int?,
    val total: Int?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Data),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(data)
        parcel.writeString(error_message)
        parcel.writeInt(flushStatus!!)
        parcel.writeInt(response_code!!)
        parcel.writeInt(syncVersion!!)
        parcel.writeInt(total!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrepaidPlans> {
        override fun createFromParcel(parcel: Parcel): PrepaidPlans {
            return PrepaidPlans(parcel)
        }

        override fun newArray(size: Int): Array<PrepaidPlans?> {
            return arrayOfNulls(size)
        }
    }
}