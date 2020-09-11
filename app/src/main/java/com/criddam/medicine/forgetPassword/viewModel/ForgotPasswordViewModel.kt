package com.criddam.medicine.forgetPassword.viewModel

import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.criddam.medicine.R
import com.criddam.medicine.others.IApiInterface
import com.criddam.medicine.others.RetrofitClientInstance
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel: ViewModel() {
    private lateinit var progressDialog: ProgressDialog
    var isSuccess = MutableLiveData<Boolean>()

    fun updatingPassword(context: Context, phone: String, password: String) {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(context.getString(R.string.updating_password))
        progressDialog.setCancelable(false)
        progressDialog.show()

        val apiInterface =
            RetrofitClientInstance.retrofitInstance?.create(IApiInterface::class.java)
        val call: Call<JsonElement> = apiInterface!!.updatePassword(phone, password)

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