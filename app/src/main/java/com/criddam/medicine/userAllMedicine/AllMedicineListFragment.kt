package com.criddam.medicine.userAllMedicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.criddam.medicine.R
import com.criddam.medicine.others.Utility
import com.criddam.medicine.userAllMedicine.models.Datum
import com.criddam.medicine.userAllMedicine.viewModels.AllMedicineViewModel

class AllMedicineListFragment : Fragment(), MedicineListAdapter.ItemListener {
    private lateinit var viewModelProvider: AllMedicineViewModel
    private lateinit var rvMedicineList: RecyclerView
    private lateinit var tvNoMedicine: TextView
    private lateinit var medicineList: ArrayList<Datum>
    private lateinit var adapter: MedicineListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_all_medicine_list, container, false)
        action(root)
        return root
    }

    private fun action(root: View?) {
        medicineList = ArrayList()
        viewModelProvider =
            androidx.lifecycle.ViewModelProviders.of(this).get(AllMedicineViewModel::class.java)
        rvMedicineList = root!!.findViewById(R.id.rv_medicine_list)
        tvNoMedicine = root.findViewById(R.id.tv_no_medicine)
        rvMedicineList.layoutManager = LinearLayoutManager(context)
        adapter = MedicineListAdapter(context, medicineList, this)
        rvMedicineList.adapter = adapter
        if (Utility.verifyAvailableNetwork(activity as AppCompatActivity)) {
            viewModelProvider.getMedicineList(this.context!!)
        } else Toast.makeText(
            context,
            getString(R.string.no_internet_connection),
            Toast.LENGTH_LONG
        ).show()
        viewModelProvider.medicineList.observe(
            AllMedicineListFragment@ this,
            Observer { listOfMedicine ->
                if (listOfMedicine.size > 0) {
                    tvNoMedicine.visibility = View.GONE
                    medicineList.clear()
                    medicineList.addAll(listOfMedicine)
                    adapter.notifyDataSetChanged()
                } else tvNoMedicine.visibility = View.VISIBLE
            })
    }


    companion object {
        @JvmStatic
        fun newInstance() = AllMedicineListFragment().apply {}
    }

    override fun onItemClick(item: Datum?) {
        val fragment = EditMedicineFragment.newInstance(item!!)
        Utility.replaceFragmentsOverHomeActivity(activity!!.supportFragmentManager, fragment)
    }
}