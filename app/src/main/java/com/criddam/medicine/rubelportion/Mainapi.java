package com.criddam.medicine.rubelportion;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Mainapi {

    @FormUrlEncoded
    @POST("GeneralVitalInput")
    Call<ResponseBody> createPost(
            @Field("pulse") int pulse,
            @Field("temperature") int temperature,
            @Field("systolic") int systolic,
            @Field("diastolic") int diastolic,
            @Field("weight") int weight,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("medicine_entry")
    Call<ResponseBody>sendQrdata(

            @Field("qr_bar_code")String qr_bar_code,
            @Field("user_id") int user_id
    );
}
