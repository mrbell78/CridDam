package com.criddam.medicine.others

import com.criddam.medicine.userAllMedicine.models.Medicine
import com.criddam.medicine.login.models.UserData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface IApiInterface {
    @Headers("Content-Type: application/x-www-form-urlencoded")

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ): Call<UserData>

    @FormUrlEncoded
    @POST("forgotpassword")
    fun updatePassword(
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("type") type: String
    ): Call<UserData>

    @POST("medicine_entry")
    fun submitMedicine(
        @Body data: JsonObject
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST("view_medicines")
    fun getAllMedicineOfUser(@Field ("user_id")userId: Int): Call<Medicine>

    @GET("all_medicine")
    fun allMedicine(): Call<Medicine>

}