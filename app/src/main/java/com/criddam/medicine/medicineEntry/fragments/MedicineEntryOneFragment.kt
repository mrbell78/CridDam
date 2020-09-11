package com.criddam.medicine.medicineEntry.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.criddam.medicine.R
import com.criddam.medicine.others.Utility


class MedicineEntryOneFragment : Fragment() {
    private var medicineType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_medicine_entry_one, container, false)
        action(root)
        return root
    }

    private fun action(root: View?) {
        val next: Button = root!!.findViewById(R.id.btn_medicine_entry_step_one_next)
        val radioGroup: RadioGroup = root.findViewById(R.id.rg_medicine_type)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_syrap -> {
                    medicineType = "syrap"
                }
                R.id.rb_capsule -> {
                    medicineType = "capsule"
                }
                R.id.rb_tablet -> {
                    medicineType = "tablet"
                }
                R.id.rb_injection -> {
                    medicineType = "injection"
                }
                R.id.rb_suppository -> {
                    medicineType = "suppository"
                }
            }
        }

        next.setOnClickListener {
            if (medicineType == "")
                Toast.makeText(
                    context,
                    getString(R.string.select_medicine_type),
                    Toast.LENGTH_LONG
                ).show()
            else
                Utility.replaceFragmentsOverHomeActivity(
                    fragmentManager, MedicineEntryTwoFragment.newInstance(medicineType)
                )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MedicineEntryOneFragment().apply {}
    }
}
