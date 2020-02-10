package com.nexters.doctor24.todoc.data.marker.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

internal data class ResMapMarker (
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val placeName: String = "",
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("medicalType") // hospital, pharmacy, animal
    val medicalType: String = "",
    @SerializedName("day")
    val day: OperatingDate?,
    @SerializedName("today")
    val today: OperatingDate?,
    @SerializedName("days")
    val days: List<OperatingDate>?,
    @SerializedName("phone")
    val placePhone: String?,
    @SerializedName("address")
    val placeAddress: String?,
    @SerializedName("categories")
    val categories: List<String>? = emptyList(),
    @SerializedName("emergency")
    val emergency: Boolean = false,
    @SerializedName("nightTimeServe")
    val nightTimeServe: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readParcelable(OperatingDate::class.java.classLoader),
        parcel.readParcelable(OperatingDate::class.java.classLoader),
        parcel.createTypedArrayList(OperatingDate),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(placeName)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(medicalType)
        parcel.writeParcelable(day, flags)
        parcel.writeParcelable(today, flags)
        parcel.writeTypedList(days)
        parcel.writeString(placePhone)
        parcel.writeString(placeAddress)
        parcel.writeStringList(categories)
        parcel.writeByte(if (emergency) 1 else 0)
        parcel.writeByte(if (nightTimeServe) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResMapMarker> {
        override fun createFromParcel(parcel: Parcel): ResMapMarker {
            return ResMapMarker(parcel)
        }

        override fun newArray(size: Int): Array<ResMapMarker?> {
            return arrayOfNulls(size)
        }
    }
}