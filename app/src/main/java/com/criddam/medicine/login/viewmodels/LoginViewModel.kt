package com.criddam.medicine.login.viewmodels

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.login.models.UserData
import com.criddam.medicine.others.IApiInterface
import com.criddam.medicine.others.RetrofitClientInstance
import com.criddam.medicine.rubelportion.HealthStatus
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private lateinit var progressDialog: ProgressDialog
    var isSuccess = MutableLiveData<Boolean>()
    private lateinit var offlineInformation: OfflineInformation




    fun loginWithNumberAndPass(context: Context, phone: String, password: String) {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(context.getString(R.string.login_loading))
        progressDialog.setCancelable(false)
        progressDialog.show()

        val apiInterface =
            RetrofitClientInstance.retrofitInstance?.create(IApiInterface::class.java)
        val call: Call<UserData> = apiInterface!!.login(phone, password)

        var HealthStatus = HealthStatus()

        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val loginData = response.body()
                    if (!loginData?.error!!) {


                        isSuccess.postValue(true)
                        offlineInformation = OfflineInformation(context)
                        offlineInformation.saveUserId(loginData.data!!.id)
                        Log.d(TAG, "onResponse: ..............user id " + loginData.data!!.id.toString())
                        offlineInformation.saveUserName(loginData.data!!.username)
                        offlineInformation.saveUserFirstName(loginData.data!!.firstname)
                        offlineInformation.saveUserLastName(loginData.data!!.lastname)
                        offlineInformation.saveUserPhone(phone)
                        offlineInformation.saveUserPassword(password)
                        offlineInformation.saveToken(loginData.data!!.token)
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