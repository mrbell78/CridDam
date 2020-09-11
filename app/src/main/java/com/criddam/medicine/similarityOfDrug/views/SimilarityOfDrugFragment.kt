package com.criddam.medicine.similarityOfDrug.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.criddam.medicine.R
import com.criddam.medicine.similarityOfDrug.adapters.SimilarityDrugAdapter

class SimilarityOfDrugFragment : Fragment() {
    private lateinit var medicineList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_similarity_of_drug, container, false)
        action(rootView)
        return rootView}

    private fun action(rootView: View?) {
        val rvSideEffect = rootView!!.findViewById<RecyclerView>(R.id.rv_drug_similarity)
        medicineList = ArrayList()
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        rvSideEffect.layoutManager = LinearLayoutManager(context)
        rvSideEffect.adapter = SimilarityDrugAdapter(context, medicineList)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SimilarityOfDrugFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
