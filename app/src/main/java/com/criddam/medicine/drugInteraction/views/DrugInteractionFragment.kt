package com.criddam.medicine.drugInteraction.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.criddam.medicine.R
import com.criddam.medicine.drugInteraction.adapters.DrugInteractionAdapter

class DrugInteractionFragment : Fragment(), DrugInteractionAdapter.ItemListener{

    private lateinit var medicineList:ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_drug_interaction, container, false)
        action(rootView)
        return rootView
    }

    private fun action(rootView: View?) {
        val rvDrugInteraction = rootView!!.findViewById<RecyclerView>(R.id.rv_drug_interaction)
        medicineList = ArrayList()
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        rvDrugInteraction.layoutManager = LinearLayoutManager(context)
        rvDrugInteraction.adapter = DrugInteractionAdapter(context, medicineList, this)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DrugInteractionFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(item: String?) {
    }
}
