package com.criddam.medicine.sideEffect.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.criddam.medicine.R
import com.criddam.medicine.sideEffect.adapters.SideEffectAdapter

class SideEffectFragment : Fragment() , SideEffectAdapter.ItemListener{
    private lateinit var medicineList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_side_effect, container, false)
        action(rootView)
        return rootView
    }

    private fun action(rootView: View?) {
        val rvSideEffect = rootView!!.findViewById<RecyclerView>(R.id.rv_side_effect)
        medicineList = ArrayList()
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        medicineList.add("One")
        rvSideEffect.layoutManager = LinearLayoutManager(context)
        rvSideEffect.adapter = SideEffectAdapter(context, medicineList, this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SideEffectFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(item: String?) {
        val alertDialog =
            AlertDialog.Builder(context!!).create()
        alertDialog.setMessage("Details of selected medicine")
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }
}
