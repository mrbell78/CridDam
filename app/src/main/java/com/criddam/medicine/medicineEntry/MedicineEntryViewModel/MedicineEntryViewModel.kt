package com.criddam.medicine.medicineEntry.MedicineEntryViewModel

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.criddam.medicine.R
import com.criddam.medicine.medicineEntry.models.TakingPeriods
import com.criddam.medicine.others.IApiInterface
import com.criddam.medicine.others.RetrofitClientInstance
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicineEntryViewModel : ViewModel() {
    private lateinit var progressDialog: ProgressDialog
    var isSuccess = MutableLiveData<Boolean>()
    val TAG = "MedicineEntryVM"

    fun submitMedicine(
        context: Context,
        type: String,
        medicine_name: String,
        measurement: String,
        periods: ArrayList<TakingPeriods>,
        userId: Int,
        photo:String,
        barcodvalue:String
    ) {

        Log.e(TAG, "UserID: $userId")

        if (userId != -1) {
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage(context.getString(R.string.creating_medicine))
            progressDialog.setCancelable(false)
            progressDialog.show()

            // Making Json data
            val jsonArray = JsonArray()

            for (period in periods) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("period", period.period)
                jsonObject.addProperty("tds", period.tds)
                jsonArray.add(jsonObject)
            }
            val req = JsonObject()
            req.addProperty("type", type)
            req.addProperty("medicine_name", medicine_name)
            req.addProperty("measurement", measurement)
            req.addProperty("user_id", userId.toString())
            req.addProperty("photo", photo)
            req.addProperty("taking_period", Gson().toJson(jsonArray))
            req.addProperty("qr_bar_code",barcodvalue)

            val apiInterface =
                RetrofitClientInstance.retrofitInstance?.create(IApiInterface::class.java)
            val call: Call<JsonElement> =
                apiInterface!!.submitMedicine(req)


        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    if (!JSONObject(response.body().toString()).getBoolean("error")) {
                        isSuccess.postValue(true)
                    } else isSuccess.postValue(false)
                } else isSuccess.postValue(false)
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                progressDialog.dismiss()
                isSuccess.postValue(false)
            }
        })
        }
    }





}