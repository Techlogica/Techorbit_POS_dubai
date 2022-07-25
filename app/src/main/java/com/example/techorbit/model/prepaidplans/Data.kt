package com.example.techorbit.model.prepaidplans

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(

    val beneficiaryCountryId: Int?,
    val beneficiaryCountryName: String?,
    val benefits: String?,
    val circleName: String?,
    val dataMB: String?,
    val denominationId: String?,
    val localCurrency: String?,
    val modifiedDate: String?,
    var mrp: String?,
    val operatingCountryId: Int?,
    val operatingCountryName: String?,
    val operatingCurrency: String?,
    val operatorName: String?,
    val rechargeAmount: String?,
    val rechargeSubType: String?,
    val status: String?,
    val talktime: String?,
    val validity: String?,
    val maxValue:String?,
    val minValue:String?,
    var commisionPer:Int?,
    @PrimaryKey
    val id: Int?


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(beneficiaryCountryId!!)
        parcel.writeString(beneficiaryCountryName)
        parcel.writeString(benefits)
        parcel.writeString(circleName)
        parcel.writeString(dataMB)
        parcel.writeString(denominationId)
        parcel.writeString(localCurrency)
        parcel.writeString(modifiedDate)
        parcel.writeString(mrp)
        parcel.writeInt(operatingCountryId!!)
        parcel.writeString(operatingCountryName)
        parcel.writeString(operatingCurrency)
        parcel.writeString(operatorName)
        parcel.writeString(rechargeAmount)
        parcel.writeString(rechargeSubType)
        parcel.writeString(status)
        parcel.writeString(talktime)
        parcel.writeString(validity)
        parcel.writeString(maxValue)
        parcel.writeString(minValue)
        commisionPer?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }


}