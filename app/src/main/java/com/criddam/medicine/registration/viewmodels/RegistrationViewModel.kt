package com.criddam.medicine.registration.viewmodels

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.login.models.UserData
import com.criddam.medicine.others.IApiInterface
import com.criddam.medicine.others.RetrofitClientInstance
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    private lateinit var progressDialog: ProgressDialog

    var isSuccess = MutableLiveData<Boolean>()

    fun registerWithNumberAndPass(
        context: Context,
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        type: String
    ) {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(context.getString(R.string.registration_loading))
        progressDialog.setCancelable(false)
        progressDialog.show()

        val apiInterface =
            RetrofitClientInstance.retrofitInstance?.create(IApiInterface::class.java)
        val call: Call<UserData> = apiInterface!!.register(
            firstName.trim(),
            lastName.trim(),
            phone.trim(),
            password.trim(),
            type.trim()
        )

        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val loginData = response.body()
                    if (!loginData?.error!!) {
                        isSuccess.postValue(true)
                    } else isSuccess.postValue(false)
                } else isSuccess.postValue(false)
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                progressDialog.dismiss()
                isSuccess.postValue(false)
            }
        })
    }
}