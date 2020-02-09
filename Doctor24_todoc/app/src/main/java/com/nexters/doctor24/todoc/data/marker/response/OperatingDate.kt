package com.nexters.doctor24.todoc.data.marker.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by jiyoung on 13/01/2020
 */
internal data class OperatingDate (
    @SerializedName("dayType") // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, HOLIDAY
    val dayType: String = "",
    @SerializedName("startTime")
    val startTime: String = "",
    @SerializedName("endTime")
    val endTime: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dayType)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OperatingDate> {
        override fun createFromParcel(parcel: Parcel): OperatingDate {
            return OperatingDate(parcel)
        }

        override fun newArray(size: Int): Array<OperatingDate?> {
            return arrayOfNulls(size)
        }
    }
}