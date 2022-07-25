package com.example.techorbit.db.OtarRecharge

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_report")
data class OtarRecharge(
    var type:String?=null,
    var rechargeType:String?=null,
    var beneficiaryNumber:String?=null,
    var operator:String?=null,
    var beneficiaryCountry:String?=null,
    var rechargeAmount:Double?=null,
    var commissionAmount: Float? =null,
    var createdTime:String?=null,
    var rechargeStatus:String?=null,
    var traceId:String?=null,
    @PrimaryKey
    var rechargeId: Int? = null,
): Parcelable {
    constructor(parcel:Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ){
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(rechargeType)
        parcel.writeString(beneficiaryNumber)
        parcel.writeString(operator)
        parcel.writeString(beneficiaryCountry)
        parcel.writeDouble(rechargeAmount!!)
        parcel.writeFloat(commissionAmount!!)
        parcel.writeString(createdTime)
        parcel.writeString(rechargeStatus)
        parcel.writeString(traceId)
        parcel.writeInt(rechargeId!!)

    }

    companion object CREATOR : Parcelable.Creator<OtarRecharge> {
        override fun createFromParcel(parcel: Parcel): OtarRecharge {
            return  OtarRecharge(parcel)
        }

        override fun newArray(size: Int): Array<OtarRecharge?> {
            return arrayOfNulls(size)
        }
    }
}
