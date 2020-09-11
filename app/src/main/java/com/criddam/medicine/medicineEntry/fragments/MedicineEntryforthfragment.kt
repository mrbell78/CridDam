package com.criddam.medicine.medicineEntry.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.medicineEntry.MedicineEntryViewModel.MedicineEntryViewModel
import com.criddam.medicine.medicineEntry.models.TakingPeriods
import com.criddam.medicine.others.Utility
import kotlinx.android.synthetic.main.fragment_medicine_entry_three.*
import kotlinx.android.synthetic.main.fragment_medicine_entryforthfragment.*
import java.util.*
import kotlin.collections.ArrayList


class MedicineEntryforthfragment : Fragment() {

    private val MEDICINE_TYPE = "medicine_type"
    private val MEDICINE_NAME = "medicine_name"
    private val MEDICINE_IMAGE = "medicine_image"
    private val MEDICINE_MEASUREMENT_NAME = "medicine_measurement_name"
    private val MEDICINE_MEASUREMENT_TYPE = "medicine_measurement_type"
    private var medicineMeasurementName: String? = null
    private var medicineMeasurementType: String? = null
    private var medicineType: String? = null
    private var medicineName: String? = null
    private var medicineImage: String? = null
    private var selectingTimeFor: String = ""
    private lateinit var takingPeriodList: ArrayList<TakingPeriods>
    private lateinit var viewModelProvider: MedicineEntryViewModel
    private lateinit var offlineInformation: OfflineInformation
    private lateinit var calSet: Calendar
    private lateinit var mornCal: Calendar
    private lateinit var evenCal: Calendar
    private lateinit var nightCal: Calendar
    private lateinit var otherCal: Calendar
    private lateinit var hourList: java.util.ArrayList<Int>
    private lateinit var minuteList: java.util.ArrayList<Int>
    private lateinit var alarmList: java.util.ArrayList<Calendar>


    private var medicinetime: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicineType = it.getString(MEDICINE_TYPE)
            medicineName = it.getString(MEDICINE_NAME)
            medicineImage = it.getString(MEDICINE_IMAGE)
            medicineMeasurementName = it.getString(MEDICINE_MEASUREMENT_NAME)
            medicineMeasurementType = it.getString(MEDICINE_MEASUREMENT_TYPE)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_medicine_entryforthfragment, container, false)
        action(root)
        return root

    }

    private fun action(root:View?){

        val next: Button = root!!.findViewById(R.id.btn_medicine_entry_step_four_next)
        val radioGroup: RadioGroup = root.findViewById(R.id.medicine_time)

        androidx.lifecycle.ViewModelProviders.of(this).get(MedicineEntryViewModel::class.java)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_empty -> {
                    medicinetime = "empty_Stomac"
                }
                R.id.rb_full -> {
                    medicinetime = "fillStomac"
                }

            }
        }

        next.setOnClickListener {
            if (medicinetime == "") {
                Toast.makeText(context,"Please Enter medicine time",Toast.LENGTH_SHORT)
            } else {
                Utility.replaceFragmentsOverHomeActivity(
                    fragmentManager,

                    MedicineEntryFinalStepFragment.newInstance(medicineType.toString(), medicineName.toString(),
                        medicineImage!!,
                       medicineMeasurementType.toString(),medicinetime.toString()))
            }
        }





    }

    companion object {
        @JvmStatic
        fun newInstance(
            medicineType: String,
            medicineName: String,
            medicineImage: String,
            medicineMeasurementType: String,
            medicineMeasurementName: String
        ) =
            MedicineEntryforthfragment().apply {
                arguments = Bundle().apply {
                    putString(MEDICINE_TYPE, medicineType)
                    putString(MEDICINE_NAME, medicineName)
                    putString(MEDICINE_IMAGE, medicineImage)
                    putString(MEDICINE_MEASUREMENT_TYPE, medicineMeasurementType)
                    putString(MEDICINE_MEASUREMENT_NAME, medicineMeasurementName)
                }
            }
    }
}