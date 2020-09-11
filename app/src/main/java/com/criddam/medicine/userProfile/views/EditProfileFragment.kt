package com.criddam.medicine.userProfile.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*


import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.medicineEntry.MedicineEntryViewModel.MedicineEntryViewModel
import com.criddam.medicine.userProfile.viewModels.EditProfileVM

class EditProfileFragment : Fragment() {
    private lateinit var viewModelProvider: EditProfileVM
    private lateinit var offlineInformation: OfflineInformation
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var etBmi: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        action(rootView)
        return rootView
    }

    private fun action(rootView: View?) {
        offlineInformation = OfflineInformation(this.context!!)
        viewModelProvider =
            androidx.lifecycle.ViewModelProviders.of(this).get(EditProfileVM::class.java)

        rootView?.et_edit_prof_first_name?.setText(offlineInformation.userFirstName)
        rootView?.et_edit_prof_last_name?.setText(offlineInformation.userLastName)
        rootView?.et_edit_prof_phone_num?.setText(offlineInformation.userPhone)
        rootView?.et_edit_prof_password?.setText(offlineInformation.userPassword)

        rootView?.btn_edit_prof_submit?.setOnClickListener {
            when {
                rootView.et_edit_prof_first_name?.text?.equals("")!! -> rootView.et_edit_prof_first_name.error =
                    getString(R.string.enter_first_name)
                rootView.et_edit_prof_last_name?.text?.equals("")!! -> rootView.et_edit_prof_last_name.error =
                    getString(R.string.enter_last_name)
                rootView.et_edit_prof_phone_num?.text?.equals("")!! -> rootView.et_edit_prof_phone_num.error =
                    getString(R.string.enter_mobile_number)
                rootView.et_edit_prof_password?.text?.equals("")!! -> rootView.et_edit_prof_password.error =
                    getString(R.string.enter_password)
                rootView.et_edit_prof_password?.text?.length!! < 6 -> rootView.et_edit_prof_password.error =
                    getString(R.string.enter_six_digit_password)
                rootView.et_edit_prof_height?.text?.equals("")!! -> rootView.et_edit_prof_height.error =
                    getString(R.string.enter_height)
                rootView.et_edit_prof_weight?.text?.equals("")!! -> rootView.et_edit_prof_weight.error =
                    getString(R.string.enter_weight)
                rootView.et_edit_prof_bmi?.text?.equals("")!! -> rootView.et_edit_prof_bmi.error =
                    getString(R.string.enter_bmi)
                else -> {

                }
            }

        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment().apply {}
    }
}
