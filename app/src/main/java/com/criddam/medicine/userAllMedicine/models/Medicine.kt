package com.criddam.medicine.userAllMedicine.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Medicine {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("medicine_name")
    @Expose
    var medicine_name: String? = null


    @SerializedName("data")
    @Expose
    var data: ArrayList<Datum>? = null

}