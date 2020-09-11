package com.criddam.medicine.userAllMedicine.viewModels

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.criddam.medicine.R
import com.criddam.medicine.userAllMedicine.models.Datum
import com.criddam.medicine.userAllMedicine.models.Medicine
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.others.IApiInterface
import com.criddam.medicine.others.RetrofitClientInstance
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AllMedicineViewModel: ViewModel() {
    private val TAG = "AllMedicineViewModel"
    private lateinit var progressDialog: ProgressDialog
    var medicineList = MutableLiveData<ArrayList<Datum>>()
    private lateinit var offlineInformation: OfflineInformation

    fun getMedicineList(context:Context){
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(context.getString(R.string.getting_all_medicine))
        progressDialog.setCancelable(false)
        progressDialog.show()

        offlineInformation = OfflineInformation(context)

        val apiInterface =
            RetrofitClientInstance.retrofitInstance?.create(IApiInterface::class.java)
        val call: Call<Medicine> = apiInterface!!.getAllMedicineOfUser(offlineInformation.userId!!)

        call.enqueue(object : Callback<Medicine> {
            override fun onResponse(call: Call<Medicine>, response: Response<Medicine?>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val medicine = response.body()
                    val medicines: ArrayList<Datum>? = medicine!!.data
                    if (!medicine.error!!) {
                        medicineList.postValue(medicines)
                    }
                }
            }

            override fun onFailure(call: Call<Medicine>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }
}