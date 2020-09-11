package com.criddam.medicine.userAllMedicine.models

import android.os.Parcel
import android.os.Parcelable
import com.criddam.medicine.medicineEntry.models.TakingPeriods
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum() : Parcelable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("medicine_name")
    @Expose
    var medicineName: String? = null

    @SerializedName("generic_name")
    @Expose
    var genericName: String? = null

    @SerializedName("measurement")
    @Expose
    var measurement: String? = null

    @SerializedName("photourl")
    @Expose
    var photourl: String? = null

    @SerializedName("photo")
    @Expose
    var photo: String? = null

    @SerializedName("taking_period")
    @Expose
    var takingPeriod: List<TakingPeriods>? = null

    @SerializedName("is_notified")
    @Expose
    var isNotified: Int? = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        type = parcel.readString()
        medicineName = parcel.readString()
        genericName = parcel.readString()
        measurement = parcel.readString()
        photourl = parcel.readString()
        photo = parcel.readString()
        isNotified = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeValue(id)
        dest.writeString(type)
        dest.writeString(medicineName)
        dest.writeString(genericName)
        dest.writeString(measurement)
        dest.writeString(photourl)
        dest.writeString(photo)
        dest.writeValue(isNotified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Datum> {
        override fun createFromParcel(parcel: Parcel): Datum {
            return Datum(parcel)
        }

        override fun newArray(size: Int): Array<Datum?> {
            return arrayOfNulls(size)
        }
    }
}