package com.criddam.medicine.login.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("firstname")
    @Expose
    var firstname: String? = null
    @SerializedName("lastname")
    @Expose
    var lastname: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("mobile")
    @Expose
    var mobile: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
}