package com.criddam.medicine.medicineEntry.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

import com.criddam.medicine.R
import com.criddam.medicine.others.Utility
import kotlinx.android.synthetic.main.fragment_medicine_entry_three.*


class MedicineEntryThreeFragment : Fragment() {
    private val MEDICINE_TYPE = "medicine_type"
    private val MEDICINE_NAME = "medicine_name"
    private val MEDICINE_IMAGE = "medicine_image"
    private var medicineType: String? = null
    private var medicineName: String? = null
    private var medicineImage: String? = null
    private var medicineQuantityType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicineType = it.getString(MEDICINE_TYPE)
            medicineName = it.getString(MEDICINE_NAME)
            medicineImage = it.getString(MEDICINE_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_medicine_entry_three, container, false)
        action(root)
        return root
    }

    private fun action(root: View?) {
        val next: Button = root!!.findViewById(R.id.btn_medicine_entry_step_three_next)
        val etMedicineMeasurement: EditText = root.findViewById(R.id.et_medicine_measurement)
        val radioGroup: RadioGroup = root.findViewById(R.id.rg_medicine_quantity_type)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_mgm -> {
                    medicineQuantityType = "mgm"
                }
                R.id.rb_gm -> {
                    medicineQuantityType = "gm"
                }
                R.id.rb_chamoch -> {
                    medicineQuantityType = "chamoch"
                }
                R.id.rb_mi -> {
                    medicineQuantityType = "mi"
                }
            }
        }
        next.setOnClickListener {
            if (etMedicineMeasurement.text.toString() == "") {
                etMedicineMeasurement.error = getString(R.string.enter_medicine_quantity)
            } else if (medicineQuantityType == "") {
                Toast.makeText(context, getString(R.string.select_medicine_type), Toast.LENGTH_LONG)
                    .show()
            } else {
                Utility.replaceFragmentsOverHomeActivity(
                    fragmentManager,

                    MedicineEntryforthfragment.newInstance(medicineType.toString(), medicineName.toString(),
                        medicineImage!!,
                        et_medicine_measurement.text.toString(), medicineQuantityType))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(medicineType: String, medicineName: String, medicineImage: String) =
            MedicineEntryThreeFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDICINE_TYPE, medicineType)
                    putString(MEDICINE_NAME, medicineName)
                    putString(MEDICINE_IMAGE, medicineImage)
                }
            }
    }
}
