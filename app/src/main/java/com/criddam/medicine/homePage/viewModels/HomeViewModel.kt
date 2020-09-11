package com.criddam.medicine.homePage.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.others.IApiInterface
import com.criddam.medicine.others.RetrofitClientInstance
import com.criddam.medicine.userAllMedicine.models.Datum
import com.criddam.medicine.userAllMedicine.models.Medicine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeViewModel : ViewModel() {
    var medicineList = MutableLiveData<ArrayList<Datum>>()
    private lateinit var offlineInformation: OfflineInformation

    fun getMedicineList(context: Context){

        offlineInformation = OfflineInformation(context)

        val apiInterface =
            RetrofitClientInstance.retrofitInstance?.create(IApiInterface::class.java)
        val call: Call<Medicine> = apiInterface!!.getAllMedicineOfUser(offlineInformation.userId!!)

        call.enqueue(object : Callback<Medicine> {
            override fun onResponse(call: Call<Medicine>, response: Response<Medicine?>) {
                if (response.isSuccessful) {
                    val medicine = response.body()
                    val medicines: ArrayList<Datum>? = medicine!!.data
                    if (!medicine.error!!) {
                        medicineList.postValue(medicines)
                    }
                }
            }

            override fun onFailure(call: Call<Medicine>, t: Throwable) {
            }
        })
    }
}