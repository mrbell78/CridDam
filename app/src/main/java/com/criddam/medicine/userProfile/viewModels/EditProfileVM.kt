package com.criddam.medicine.userProfile.viewModels

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class  EditProfileVM : ViewModel() {
    private lateinit var progressDialog: ProgressDialog
    var isSuccess = MutableLiveData<Boolean>()
    val TAG = "MedicineEntryVM"

    fun submitMedicine(){
    }
}
