package com.criddam.medicine.login.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param data
     * @param error
     */
    constructor(error: Boolean?, data: Data?) : super() {
        this.error = error
        this.data = data
    }

}